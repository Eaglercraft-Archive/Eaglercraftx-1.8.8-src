package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.backend_rpc_protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

import org.apache.commons.lang3.ArrayUtils;

import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.EaglerBackendRPCProtocol;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.EaglerBackendRPCHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.EaglerBackendRPCPacket;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.WrongRPCPacketException;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.client.CPacketRPCEnabled;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.client.CPacketRPCSubscribeEvents;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.server.SPacketRPCEnabledFailure;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.server.SPacketRPCEnabledSuccess;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.server.SPacketRPCEventToggledVoice;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.server.SPacketRPCEventWebViewOpenClose;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.EnumVoiceState;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.voice.VoiceService;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.ReusableByteArrayInputStream;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.ReusableByteArrayOutputStream;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;

/**
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class BackendRPCSessionHandler {

	public static BackendRPCSessionHandler createForPlayer(EaglerInitialHandler eaglerHandler) {
		return new BackendRPCSessionHandler(eaglerHandler);
	}

	protected final EaglerInitialHandler eaglerHandler;
	private Server currentServer = null;
	private String channelName = null;
	private EaglerBackendRPCProtocol currentProtocol = null;
	private EaglerBackendRPCHandler currentHandler = null;
	private int subscribedEvents = 0;
	private final AtomicInteger currentVoiceState = new AtomicInteger(SPacketRPCEventToggledVoice.VOICE_STATE_SERVER_DISABLE);
	private final ReentrantLock inputStreamLock = new ReentrantLock();
	private final ReentrantLock outputStreamLock = new ReentrantLock();
	private final ReusableByteArrayInputStream reusableInputStream = new ReusableByteArrayInputStream();
	private final ReusableByteArrayOutputStream reusableOutputStream = new ReusableByteArrayOutputStream();
	private final DataInputStream dataInputStream = new DataInputStream(reusableInputStream);
	private final DataOutputStream dataOutputStream = new DataOutputStream(reusableOutputStream);

	private BackendRPCSessionHandler(EaglerInitialHandler eaglerHandler) {
		this.eaglerHandler = eaglerHandler;
	}

	public void handleRPCPacket(Server server, byte[] data) {
		synchronized(this) {
			if(currentServer != null) {
				if(currentServer != server) {
					return;
				}
			}else {
				handleCreateContext(server, data);
				return;
			}
		}
		EaglerBackendRPCPacket packet;
		try {
			packet = decodeRPCPacket(currentProtocol, data);
		} catch (IOException e) {
			EaglerXBungee.logger().log(Level.SEVERE, "[" + eaglerHandler.getSocketAddress()
					+ "]: Recieved invalid backend RPC protocol packet for user \"" + eaglerHandler.getName() + "\"", e);
			return;
		}
		packet.handlePacket(currentHandler);
	}

	protected EaglerBackendRPCPacket decodeRPCPacket(EaglerBackendRPCProtocol protocol, byte[] data) throws IOException {
		EaglerBackendRPCPacket ret;
		if(inputStreamLock.tryLock()) {
			try {
				reusableInputStream.feedBuffer(data);
				ret = protocol.readPacket(dataInputStream, EaglerBackendRPCProtocol.CLIENT_TO_SERVER);
			}finally {
				inputStreamLock.unlock();
			}
		}else {
			ReusableByteArrayInputStream bai = new ReusableByteArrayInputStream();
			bai.feedBuffer(data);
			ret = protocol.readPacket(new DataInputStream(bai), EaglerBackendRPCProtocol.CLIENT_TO_SERVER);
		}
		return ret;
	}

	public void sendRPCPacket(EaglerBackendRPCPacket packet) {
		if(currentServer != null) {
			sendRPCPacket(currentProtocol, currentServer, packet);
		}else {
			EaglerXBungee.logger()
					.warning("[" + eaglerHandler.getSocketAddress()
							+ "]: Failed to write backend RPC protocol version for user \"" + eaglerHandler.getName()
							+ "\", the RPC connection is not initialized!");
		}
	}

	protected void sendRPCPacket(EaglerBackendRPCProtocol protocol, Server server, EaglerBackendRPCPacket packet) {
		byte[] ret;
		int len = packet.length() + 1;
		if(outputStreamLock.tryLock()) {
			try {
				reusableOutputStream.feedBuffer(new byte[len > 0 ? len : 64]);
				try {
					protocol.writePacket(dataOutputStream, EaglerBackendRPCProtocol.SERVER_TO_CLIENT, packet);
				}catch(IOException ex) {
					throw new IllegalStateException("Failed to serialize packet: " + packet.getClass().getSimpleName(), ex);
				}
				ret = reusableOutputStream.returnBuffer();
			}finally {
				outputStreamLock.unlock();
			}
		}else {
			ReusableByteArrayOutputStream bao = new ReusableByteArrayOutputStream();
			bao.feedBuffer(new byte[len > 0 ? len : 64]);
			try {
				protocol.writePacket(new DataOutputStream(bao), EaglerBackendRPCProtocol.SERVER_TO_CLIENT, packet);
			}catch(IOException ex) {
				throw new IllegalStateException("Failed to serialize packet: " + packet.getClass().getSimpleName(), ex);
			}
			ret = bao.returnBuffer();
		}
		if(len > 0 && len != ret.length) {
			EaglerXBungee.logger()
					.warning("[" + eaglerHandler.getSocketAddress() + "]: Backend RPC packet type "
							+ packet.getClass().getSimpleName() + " was the wrong length for user \""
							+ eaglerHandler.getName() + "\" after serialization: " + ret.length + " != " + len);
		}
		server.sendData(channelName, ret);
	}

	public void handleConnectionLost(ServerInfo server) {
		if(currentServer != null) {
			handleDestroyContext();
		}
	}

	private void handleDestroyContext() {
		currentServer = null;
		channelName = null;
		currentProtocol = null;
		currentHandler = null;
		subscribedEvents = 0;
	}

	private void handleCreateContext(Server server, byte[] data) {
		EaglerBackendRPCPacket packet;
		try {
			packet = decodeRPCPacket(EaglerBackendRPCProtocol.INIT, data);
		} catch (IOException e) {
			EaglerXBungee.logger().log(Level.SEVERE, "[" + eaglerHandler.getSocketAddress()
					+ "]: Recieved invalid backend RPC protocol handshake for user \"" + eaglerHandler.getName() + "\"", e);
			return;
		}
		if(!(packet instanceof CPacketRPCEnabled)) {
			throw new WrongRPCPacketException();
		}
		channelName = getChNameFor((ServerConnection)server);
		if(!ArrayUtils.contains(((CPacketRPCEnabled)packet).supportedProtocols, EaglerBackendRPCProtocol.V1.vers)) {
			EaglerXBungee.logger().severe("[" + eaglerHandler.getSocketAddress()
					+ "]: Unsupported backend RPC protocol version for user \"" + eaglerHandler.getName() + "\"");
			sendRPCPacket(EaglerBackendRPCProtocol.INIT, server, new SPacketRPCEnabledFailure(SPacketRPCEnabledFailure.FAILURE_CODE_OUTDATED_SERVER));
			return;
		}
		sendRPCPacket(EaglerBackendRPCProtocol.INIT, server, new SPacketRPCEnabledSuccess(EaglerBackendRPCProtocol.V1.vers, eaglerHandler.getEaglerProtocolHandshake()));
		currentServer = server;
		currentProtocol = EaglerBackendRPCProtocol.V1;
		currentHandler = new ServerV1RPCProtocolHandler(this, server, eaglerHandler);
	}

	public static void handlePacketOnVanilla(Server server, UserConnection player, byte[] data) {
		EaglerBackendRPCPacket packet;
		try {
			packet = EaglerBackendRPCProtocol.INIT.readPacket(new DataInputStream(new ByteArrayInputStream(data)), EaglerBackendRPCProtocol.CLIENT_TO_SERVER);
		} catch (IOException e) {
			EaglerXBungee.logger().log(Level.SEVERE, "[" + player.getSocketAddress()
					+ "]: Recieved invalid backend RPC protocol handshake for user \"" + player.getName() + "\"", e);
			EaglerXBungee.logger().severe("(Note: this player is not using Eaglercraft!)");
			return;
		}
		if(!(packet instanceof CPacketRPCEnabled)) {
			throw new WrongRPCPacketException();
		}
		if(!ArrayUtils.contains(((CPacketRPCEnabled)packet).supportedProtocols, EaglerBackendRPCProtocol.V1.vers)) {
			EaglerXBungee.logger().severe("[" + player.getSocketAddress()
					+ "]: Unsupported backend RPC protocol version for user \"" + player.getName() + "\"");
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			try {
				EaglerBackendRPCProtocol.INIT.writePacket(new DataOutputStream(bao),
						EaglerBackendRPCProtocol.SERVER_TO_CLIENT,
						new SPacketRPCEnabledFailure(SPacketRPCEnabledFailure.FAILURE_CODE_OUTDATED_SERVER));
			}catch(IOException ex) {
				EaglerXBungee.logger().log(Level.SEVERE, "[" + player.getSocketAddress()
						+ "]: Failed to write backend RPC protocol version for user \"" + player.getName() + "\"", ex);
				EaglerXBungee.logger().severe("(Note: this player is not using Eaglercraft!)");
				return;
			}
			server.sendData(getChNameFor((ServerConnection)server), bao.toByteArray());
			return;
		}
		EaglerXBungee.logger().warning("[" + player.getSocketAddress()
				+ "]: Tried to open backend RPC protocol connection for non-eagler player \"" + player.getName() + "\"");
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			EaglerBackendRPCProtocol.INIT.writePacket(new DataOutputStream(bao),
					EaglerBackendRPCProtocol.SERVER_TO_CLIENT,
					new SPacketRPCEnabledFailure(SPacketRPCEnabledFailure.FAILURE_CODE_NOT_EAGLER_PLAYER));
		}catch(IOException ex) {
			EaglerXBungee.logger().log(Level.SEVERE, "[" + player.getSocketAddress()
					+ "]: Failed to write backend RPC protocol version for user \"" + player.getName() + "\"", ex);
			return;
		}
		server.sendData(getChNameFor((ServerConnection)server), bao.toByteArray());
	}

	public void setSubscribedEvents(int eventsToEnable) {
		int oldSubscribedEvents = subscribedEvents;
		subscribedEvents = eventsToEnable;
		if ((oldSubscribedEvents & CPacketRPCSubscribeEvents.SUBSCRIBE_EVENT_TOGGLE_VOICE) == 0
				&& (eventsToEnable & CPacketRPCSubscribeEvents.SUBSCRIBE_EVENT_TOGGLE_VOICE) != 0) {
			currentVoiceState.set(SPacketRPCEventToggledVoice.VOICE_STATE_SERVER_DISABLE);
			VoiceService svc = EaglerXBungee.getEagler().getVoiceService();
			if(svc != null && eaglerHandler.getEaglerListenerConfig().getEnableVoiceChat()) {
				EnumVoiceState state = svc.getPlayerVoiceState(eaglerHandler.getUniqueId(), currentServer.getInfo());
				if(state == EnumVoiceState.DISABLED) {
					handleVoiceStateTransition(SPacketRPCEventToggledVoice.VOICE_STATE_DISABLED);
				}else if(state == EnumVoiceState.ENABLED) {
					handleVoiceStateTransition(SPacketRPCEventToggledVoice.VOICE_STATE_ENABLED);
				}
			}
		}
		if ((oldSubscribedEvents & CPacketRPCSubscribeEvents.SUBSCRIBE_EVENT_WEBVIEW_OPEN_CLOSE) == 0
				&& (eventsToEnable & CPacketRPCSubscribeEvents.SUBSCRIBE_EVENT_WEBVIEW_OPEN_CLOSE) != 0) {
			if(eaglerHandler.webViewMessageChannelOpen.get()) {
				sendRPCPacket(new SPacketRPCEventWebViewOpenClose(true, eaglerHandler.webViewMessageChannelName));
			}
		}
		if ((eventsToEnable & ~(CPacketRPCSubscribeEvents.SUBSCRIBE_EVENT_WEBVIEW_OPEN_CLOSE
				| CPacketRPCSubscribeEvents.SUBSCRIBE_EVENT_WEBVIEW_MESSAGE
				| CPacketRPCSubscribeEvents.SUBSCRIBE_EVENT_TOGGLE_VOICE)) != 0) {
			EaglerXBungee.logger()
					.severe("[" + eaglerHandler.getSocketAddress() + "]: Unsupported events were subscribed to for \""
							+ eaglerHandler.getName() + "\" via backend RPC protocol, bitfield: " + subscribedEvents);
		}
	}

	public boolean isSubscribed(EnumSubscribedEvent eventType) {
		switch(eventType) {
		case WEBVIEW_OPEN_CLOSE:
			return (subscribedEvents & CPacketRPCSubscribeEvents.SUBSCRIBE_EVENT_WEBVIEW_OPEN_CLOSE) != 0;
		case WEBVIEW_MESSAGE:
			return (subscribedEvents & CPacketRPCSubscribeEvents.SUBSCRIBE_EVENT_WEBVIEW_MESSAGE) != 0;
		case TOGGLE_VOICE:
			return (subscribedEvents & CPacketRPCSubscribeEvents.SUBSCRIBE_EVENT_TOGGLE_VOICE) != 0;
		default:
			return false;
		}
	}

	public void handleDisabled() {
		handleDestroyContext();
	}

	public void handleVoiceStateTransition(int voiceState) {
		if((subscribedEvents & CPacketRPCSubscribeEvents.SUBSCRIBE_EVENT_TOGGLE_VOICE) != 0) {
			int oldState = currentVoiceState.getAndSet(voiceState);
			if(oldState != voiceState) {
				sendRPCPacket(new SPacketRPCEventToggledVoice(oldState, voiceState));
			}
		}
	}

	private static int getVerSafe(ServerConnection server) {
		try {
			return server.getCh().getEncodeVersion();
		}catch(Throwable t) {
			return -1;
		}
	}

	public static String getChNameFor(ServerConnection server) {
		return (EaglerXBungee.getEagler().getConfig().getUseModernizedChannelNames() || getVerSafe(server) >= 393)
				? EaglerBackendRPCProtocol.CHANNEL_NAME_MODERN
				: EaglerBackendRPCProtocol.CHANNEL_NAME;
	}

	public static String getReadyChNameFor(ServerConnection server) {
		return (EaglerXBungee.getEagler().getConfig().getUseModernizedChannelNames() || getVerSafe(server) >= 393)
				? EaglerBackendRPCProtocol.CHANNEL_NAME_READY_MODERN
				: EaglerBackendRPCProtocol.CHANNEL_NAME_READY;
	}

}
