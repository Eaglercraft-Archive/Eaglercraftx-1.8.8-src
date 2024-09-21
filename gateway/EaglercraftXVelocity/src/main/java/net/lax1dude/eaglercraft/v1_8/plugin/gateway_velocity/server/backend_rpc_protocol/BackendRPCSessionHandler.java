package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.backend_rpc_protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.proxy.connection.MinecraftConnection;
import com.velocitypowered.proxy.connection.backend.VelocityServerConnection;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;
import com.velocitypowered.proxy.protocol.packet.PluginMessagePacket;

import io.netty.buffer.Unpooled;
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
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.EnumVoiceState;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event.EaglercraftVoiceStatusChangeEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPlayerData;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.voice.VoiceService;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.ReusableByteArrayInputStream;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.ReusableByteArrayOutputStream;

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

	public static BackendRPCSessionHandler createForPlayer(EaglerPlayerData eaglerHandler) {
		return new BackendRPCSessionHandler(eaglerHandler);
	}

	protected final EaglerPlayerData eaglerHandler;
	private ServerConnection currentServer = null;
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

	private BackendRPCSessionHandler(EaglerPlayerData eaglerHandler) {
		this.eaglerHandler = eaglerHandler;
	}

	public void handleRPCPacket(ServerConnection server, byte[] data) {
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
			EaglerXVelocity.logger().error("[{}]: Recieved invalid backend RPC protocol packet for user \"{}\"",
					eaglerHandler.getSocketAddress(), eaglerHandler.getName(), e);
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
			EaglerXVelocity.logger().warn(
					"[{}]: Failed to write backend RPC protocol version for user \"{}\", the RPC connection is not initialized!",
					eaglerHandler.getSocketAddress(), eaglerHandler.getName());
		}
	}

	protected void sendRPCPacket(EaglerBackendRPCProtocol protocol, ServerConnection server, EaglerBackendRPCPacket packet) {
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
			EaglerXVelocity.logger().warn(
					"[{}]: Backend RPC packet type {} was the wrong length for user \"{}\" after serialization: {} != {}",
					eaglerHandler.getSocketAddress(), packet.getClass().getSimpleName(), eaglerHandler.getName(),
					ret.length, len);
		}
		sendPluginMessage(server, EaglerBackendRPCProtocol.CHANNEL_NAME, ret);
	}

	public void handleConnectionLost() {
		if(currentServer != null) {
			handleDestroyContext();
		}
	}

	private void handleDestroyContext() {
		currentServer = null;
		currentProtocol = null;
		currentHandler = null;
		subscribedEvents = 0;
	}

	private void handleCreateContext(ServerConnection server, byte[] data) {
		EaglerBackendRPCPacket packet;
		try {
			packet = decodeRPCPacket(EaglerBackendRPCProtocol.INIT, data);
		} catch (IOException e) {
			EaglerXVelocity.logger().error("[{}]: Recieved invalid backend RPC protocol handshake for user \"{}\"",
					eaglerHandler.getSocketAddress(), eaglerHandler.getName(), e);
			return;
		}
		if(!(packet instanceof CPacketRPCEnabled)) {
			throw new WrongRPCPacketException();
		}
		if(!containsProtocol(((CPacketRPCEnabled)packet).supportedProtocols, EaglerBackendRPCProtocol.V1.vers)) {
			EaglerXVelocity.logger().error("[{}]: Unsupported backend RPC protocol version for user \"{}\"", eaglerHandler.getSocketAddress(), eaglerHandler.getName());
			sendRPCPacket(EaglerBackendRPCProtocol.INIT, server, new SPacketRPCEnabledFailure(SPacketRPCEnabledFailure.FAILURE_CODE_OUTDATED_SERVER));
			return;
		}
		sendRPCPacket(EaglerBackendRPCProtocol.INIT, server, new SPacketRPCEnabledSuccess(EaglerBackendRPCProtocol.V1.vers, eaglerHandler.getEaglerProtocolHandshake()));
		currentServer = server;
		currentProtocol = EaglerBackendRPCProtocol.V1;
		currentHandler = new ServerV1RPCProtocolHandler(this, server, eaglerHandler);
	}

	private static boolean containsProtocol(int[] supportedProtocols, int vers) {
		for(int i = 0; i < supportedProtocols.length; ++i) {
			if(supportedProtocols[i] == vers) {
				return true;
			}
		}
		return false;
	}

	public static void handlePacketOnVanilla(ServerConnection server, ConnectedPlayer player, byte[] data) {
		EaglerBackendRPCPacket packet;
		try {
			packet = EaglerBackendRPCProtocol.INIT.readPacket(new DataInputStream(new ByteArrayInputStream(data)), EaglerBackendRPCProtocol.CLIENT_TO_SERVER);
		} catch (IOException e) {
			EaglerXVelocity.logger().error("[{}]: Recieved invalid backend RPC protocol handshake for user \"{}\"", player.getRemoteAddress(), player.getUsername(), e);
			EaglerXVelocity.logger().error("(Note: this player is not using Eaglercraft!)");
			return;
		}
		if(!(packet instanceof CPacketRPCEnabled)) {
			throw new WrongRPCPacketException();
		}
		if(!containsProtocol(((CPacketRPCEnabled)packet).supportedProtocols, EaglerBackendRPCProtocol.V1.vers)) {
			EaglerXVelocity.logger().error("[{}]: Unsupported backend RPC protocol version for user \"{}\"", player.getRemoteAddress(), player.getUsername());
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			try {
				EaglerBackendRPCProtocol.INIT.writePacket(new DataOutputStream(bao),
						EaglerBackendRPCProtocol.SERVER_TO_CLIENT,
						new SPacketRPCEnabledFailure(SPacketRPCEnabledFailure.FAILURE_CODE_OUTDATED_SERVER));
			}catch(IOException ex) {
				EaglerXVelocity.logger().error("[{}]: Failed to write backend RPC protocol version for user \"{}\"", player.getRemoteAddress(), player.getUsername(), ex);
				EaglerXVelocity.logger().error("(Note: this player is not using Eaglercraft!)");
				return;
			}
			sendPluginMessage(server, EaglerBackendRPCProtocol.CHANNEL_NAME, bao.toByteArray());
			return;
		}
		EaglerXVelocity.logger().warn("[{}]: Tried to open backend RPC protocol connection for non-eagler player \"{}\"", player.getRemoteAddress(), player.getUsername());
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			EaglerBackendRPCProtocol.INIT.writePacket(new DataOutputStream(bao),
					EaglerBackendRPCProtocol.SERVER_TO_CLIENT,
					new SPacketRPCEnabledFailure(SPacketRPCEnabledFailure.FAILURE_CODE_NOT_EAGLER_PLAYER));
		}catch(IOException ex) {
			EaglerXVelocity.logger().error("[{}]: Failed to write backend RPC protocol version for user \"{}\"", player.getRemoteAddress(), player.getUsername(), ex);
			return;
		}
		sendPluginMessage(server, EaglerBackendRPCProtocol.CHANNEL_NAME, bao.toByteArray());
	}

	public void setSubscribedEvents(int eventsToEnable) {
		int oldSubscribedEvents = subscribedEvents;
		subscribedEvents = eventsToEnable;
		if ((oldSubscribedEvents & CPacketRPCSubscribeEvents.SUBSCRIBE_EVENT_TOGGLE_VOICE) == 0
				&& (eventsToEnable & CPacketRPCSubscribeEvents.SUBSCRIBE_EVENT_TOGGLE_VOICE) != 0) {
			currentVoiceState.set(SPacketRPCEventToggledVoice.VOICE_STATE_SERVER_DISABLE);
			VoiceService svc = EaglerXVelocity.getEagler().getVoiceService();
			if(svc != null && eaglerHandler.getEaglerListenerConfig().getEnableVoiceChat()) {
				EnumVoiceState state = svc.getPlayerVoiceState(eaglerHandler.getUniqueId(), currentServer.getServerInfo());
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
			EaglerXVelocity.logger().error(
					"[{}]: Unsupported events were subscribed to for \"{}\" via backend RPC protocol, bitfield: {}",
					eaglerHandler.getSocketAddress(), eaglerHandler.getName(), subscribedEvents);
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

	public static void sendPluginMessage(ServerConnection conn, String channel, byte[] data) {
		// Velocity channel registry can go fuck itself
		MinecraftConnection mc = ((VelocityServerConnection)conn).getConnection();
		if(mc != null) {
			mc.write(new PluginMessagePacket(channel, Unpooled.wrappedBuffer(data)));
		}
	}

}
