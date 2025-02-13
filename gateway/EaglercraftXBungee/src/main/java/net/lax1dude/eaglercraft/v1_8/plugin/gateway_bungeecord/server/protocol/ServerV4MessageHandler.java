/*
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.protocol;

import java.util.Arrays;
import java.util.UUID;

import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.server.SPacketRPCEventWebViewMessage;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.server.SPacketRPCEventWebViewOpenClose;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.EaglerXBungeeAPIHelper;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftWebViewChannelEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftWebViewMessageEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerPauseMenuConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.backend_rpc_protocol.EnumSubscribedEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.voice.VoiceService;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessageHandler;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.*;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.*;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServerV4MessageHandler implements GameMessageHandler {

	private final UserConnection conn;
	private final EaglerXBungee plugin;

	public ServerV4MessageHandler(UserConnection conn, EaglerXBungee plugin) {
		this.conn = conn;
		this.plugin = plugin;
	}

	public void handleClient(CPacketGetOtherCapeEAG packet) {
		plugin.getCapeService().processGetOtherCape(new UUID(packet.uuidMost, packet.uuidLeast), conn);
	}

	public void handleClient(CPacketGetOtherSkinEAG packet) {
		plugin.getSkinService().processGetOtherSkin(new UUID(packet.uuidMost, packet.uuidLeast), conn);
	}

	public void handleClient(CPacketGetSkinByURLEAG packet) {
		plugin.getSkinService().processGetOtherSkin(new UUID(packet.uuidMost, packet.uuidLeast), packet.url, conn);
	}

	public void handleClient(CPacketVoiceSignalConnectEAG packet) {
		VoiceService svc = plugin.getVoiceService();
		if(svc != null && ((EaglerInitialHandler)conn.getPendingConnection()).getEaglerListenerConfig().getEnableVoiceChat()) {
			svc.handleVoiceSignalPacketTypeConnect(conn);
		}
	}

	public void handleClient(CPacketVoiceSignalDescEAG packet) {
		VoiceService svc = plugin.getVoiceService();
		if(svc != null && ((EaglerInitialHandler)conn.getPendingConnection()).getEaglerListenerConfig().getEnableVoiceChat()) {
			svc.handleVoiceSignalPacketTypeDesc(new UUID(packet.uuidMost, packet.uuidLeast), packet.desc, conn);
		}
	}

	public void handleClient(CPacketVoiceSignalDisconnectV4EAG packet) {
		VoiceService svc = plugin.getVoiceService();
		if(svc != null && ((EaglerInitialHandler)conn.getPendingConnection()).getEaglerListenerConfig().getEnableVoiceChat()) {
			svc.handleVoiceSignalPacketTypeDisconnect(conn);
		}
	}

	public void handleClient(CPacketVoiceSignalDisconnectPeerV4EAG packet) {
		VoiceService svc = plugin.getVoiceService();
		if(svc != null && ((EaglerInitialHandler)conn.getPendingConnection()).getEaglerListenerConfig().getEnableVoiceChat()) {
			svc.handleVoiceSignalPacketTypeDisconnectPeer(new UUID(packet.uuidMost, packet.uuidLeast), conn);
		}
	}

	public void handleClient(CPacketVoiceSignalICEEAG packet) {
		VoiceService svc = plugin.getVoiceService();
		if(svc != null && ((EaglerInitialHandler)conn.getPendingConnection()).getEaglerListenerConfig().getEnableVoiceChat()) {
			svc.handleVoiceSignalPacketTypeICE(new UUID(packet.uuidMost, packet.uuidLeast), packet.ice, conn);
		}
	}

	public void handleClient(CPacketVoiceSignalRequestEAG packet) {
		VoiceService svc = plugin.getVoiceService();
		if(svc != null && ((EaglerInitialHandler)conn.getPendingConnection()).getEaglerListenerConfig().getEnableVoiceChat()) {
			svc.handleVoiceSignalPacketTypeRequest(new UUID(packet.uuidMost, packet.uuidLeast), conn);
		}
	}

	public void handleClient(CPacketGetOtherClientUUIDV4EAG packet) {
		ProxiedPlayer player = plugin.getProxy().getPlayer(new UUID(packet.playerUUIDMost, packet.playerUUIDLeast));
		if(player != null) {
			PendingConnection conn2 = player.getPendingConnection();
			if(conn2 instanceof EaglerInitialHandler) {
				UUID uuid = ((EaglerInitialHandler)conn2).getClientBrandUUID();
				if (uuid != null) {
					((EaglerInitialHandler) conn.getPendingConnection())
							.sendEaglerMessage(new SPacketOtherPlayerClientUUIDV4EAG(packet.requestId,
									uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()));
					return;
				}
			}else {
				((EaglerInitialHandler) conn.getPendingConnection())
						.sendEaglerMessage(new SPacketOtherPlayerClientUUIDV4EAG(packet.requestId,
								EaglerXBungeeAPIHelper.BRAND_VANILLA_UUID.getMostSignificantBits(),
								EaglerXBungeeAPIHelper.BRAND_VANILLA_UUID.getLeastSignificantBits()));
				return;
			}
		}
		((EaglerInitialHandler) conn.getPendingConnection()).sendEaglerMessage(new SPacketOtherPlayerClientUUIDV4EAG(packet.requestId, 0l, 0l));
	}

	public void handleClient(CPacketRequestServerInfoV4EAG packet) {
		EaglerPauseMenuConfig conf = plugin.getConfig().getPauseMenuConf();
		if (conf != null && conf.getEnabled()
				&& conf.getPacket().serverInfoMode == SPacketCustomizePauseMenuV4EAG.SERVER_INFO_MODE_SHOW_EMBED_OVER_WS
				&& Arrays.equals(conf.getServerInfoHash(), packet.requestHash)) {
			EaglerInitialHandler eaglerHandler = (EaglerInitialHandler)conn.getPendingConnection();
			synchronized(eaglerHandler.serverInfoSendBuffer) {
				if(eaglerHandler.hasSentServerInfo.getAndSet(true)) {
					conn.disconnect(new TextComponent("Duplicate server info request"));
					return;
				}
				eaglerHandler.serverInfoSendBuffer.clear();
				eaglerHandler.serverInfoSendBuffer.addAll(conf.getServerInfo());
			}
		}else {
			conn.disconnect(new TextComponent("Invalid server info request"));
		}
	}

	public void handleClient(CPacketWebViewMessageV4EAG packet) {
		EaglerInitialHandler eaglerHandler = (EaglerInitialHandler)conn.getPendingConnection();
		if(eaglerHandler.isWebViewChannelAllowed) {
			if(eaglerHandler.webViewMessageChannelOpen.get()) {
				if(eaglerHandler.getRPCEventSubscribed(EnumSubscribedEvent.WEBVIEW_MESSAGE)) {
					eaglerHandler.getRPCSessionHandler().sendRPCPacket(new SPacketRPCEventWebViewMessage(
							eaglerHandler.webViewMessageChannelName, packet.type, packet.data));
				}
				BungeeCord.getInstance().getPluginManager().callEvent(new EaglercraftWebViewMessageEvent(conn,
						eaglerHandler.getEaglerListenerConfig(), eaglerHandler.webViewMessageChannelName, packet));
			}
		}else {
			conn.disconnect(new TextComponent("Webview channel permissions have not been enabled!"));
		}
	}

	public void handleClient(CPacketWebViewMessageEnV4EAG packet) {
		EaglerInitialHandler eaglerHandler = (EaglerInitialHandler)conn.getPendingConnection();
		if(eaglerHandler.isWebViewChannelAllowed) {
			eaglerHandler.webViewMessageChannelOpen.set(packet.messageChannelOpen);
			String oldChannelName = eaglerHandler.webViewMessageChannelName;
			eaglerHandler.webViewMessageChannelName = packet.messageChannelOpen ? packet.channelName : null;
			if(eaglerHandler.getRPCEventSubscribed(EnumSubscribedEvent.WEBVIEW_OPEN_CLOSE)) {
				eaglerHandler.getRPCSessionHandler().sendRPCPacket(new SPacketRPCEventWebViewOpenClose(
						packet.messageChannelOpen, packet.messageChannelOpen ? packet.channelName : oldChannelName));
			}
			BungeeCord.getInstance().getPluginManager()
					.callEvent(new EaglercraftWebViewChannelEvent(conn, eaglerHandler.getEaglerListenerConfig(),
							packet.messageChannelOpen ? eaglerHandler.webViewMessageChannelName : oldChannelName,
							packet.messageChannelOpen ? EaglercraftWebViewChannelEvent.EventType.CHANNEL_OPEN
									: EaglercraftWebViewChannelEvent.EventType.CHANNEL_CLOSE));
		}else {
			conn.disconnect(new TextComponent("Webview channel permissions have not been enabled!"));
		}
	}

}