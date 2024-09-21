package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.protocol;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;

import net.kyori.adventure.text.Component;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.server.SPacketRPCEventWebViewMessage;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.server.SPacketRPCEventWebViewOpenClose;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.EaglerXVelocityAPIHelper;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event.EaglercraftWebViewChannelEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event.EaglercraftWebViewMessageEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerPauseMenuConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPipeline;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPlayerData;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.backend_rpc_protocol.EnumSubscribedEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.voice.VoiceService;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessageHandler;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.*;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.*;

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
public class ServerV4MessageHandler implements GameMessageHandler {

	private final ConnectedPlayer conn;
	private final EaglerPlayerData eaglerHandle;
	private final EaglerXVelocity plugin;

	public ServerV4MessageHandler(ConnectedPlayer conn, EaglerPlayerData eaglerHandle, EaglerXVelocity plugin) {
		this.conn = conn;
		this.eaglerHandle = eaglerHandle;
		this.plugin = plugin;
	}

	public void handleClient(CPacketGetOtherCapeEAG packet) {
		plugin.getCapeService().processGetOtherCape(new UUID(packet.uuidMost, packet.uuidLeast), eaglerHandle);
	}

	public void handleClient(CPacketGetOtherSkinEAG packet) {
		plugin.getSkinService().processGetOtherSkin(new UUID(packet.uuidMost, packet.uuidLeast), eaglerHandle);
	}

	public void handleClient(CPacketGetSkinByURLEAG packet) {
		plugin.getSkinService().processGetOtherSkin(new UUID(packet.uuidMost, packet.uuidLeast), packet.url, eaglerHandle);
	}

	public void handleClient(CPacketVoiceSignalConnectEAG packet) {
		VoiceService svc = plugin.getVoiceService();
		if(svc != null && eaglerHandle.getEaglerListenerConfig().getEnableVoiceChat()) {
			svc.handleVoiceSignalPacketTypeConnect(eaglerHandle);
		}
	}

	public void handleClient(CPacketVoiceSignalDescEAG packet) {
		VoiceService svc = plugin.getVoiceService();
		if(svc != null && eaglerHandle.getEaglerListenerConfig().getEnableVoiceChat()) {
			svc.handleVoiceSignalPacketTypeDesc(new UUID(packet.uuidMost, packet.uuidLeast), packet.desc, eaglerHandle);
		}
	}

	public void handleClient(CPacketVoiceSignalDisconnectV4EAG packet) {
		VoiceService svc = plugin.getVoiceService();
		if(svc != null && eaglerHandle.getEaglerListenerConfig().getEnableVoiceChat()) {
			svc.handleVoiceSignalPacketTypeDisconnect(eaglerHandle);
		}
	}

	public void handleClient(CPacketVoiceSignalDisconnectPeerV4EAG packet) {
		VoiceService svc = plugin.getVoiceService();
		if(svc != null && eaglerHandle.getEaglerListenerConfig().getEnableVoiceChat()) {
			svc.handleVoiceSignalPacketTypeDisconnectPeer(new UUID(packet.uuidMost, packet.uuidLeast), eaglerHandle);
		}
	}

	public void handleClient(CPacketVoiceSignalICEEAG packet) {
		VoiceService svc = plugin.getVoiceService();
		if(svc != null && eaglerHandle.getEaglerListenerConfig().getEnableVoiceChat()) {
			svc.handleVoiceSignalPacketTypeICE(new UUID(packet.uuidMost, packet.uuidLeast), packet.ice, eaglerHandle);
		}
	}

	public void handleClient(CPacketVoiceSignalRequestEAG packet) {
		VoiceService svc = plugin.getVoiceService();
		if(svc != null && eaglerHandle.getEaglerListenerConfig().getEnableVoiceChat()) {
			svc.handleVoiceSignalPacketTypeRequest(new UUID(packet.uuidMost, packet.uuidLeast), eaglerHandle);
		}
	}

	public void handleClient(CPacketGetOtherClientUUIDV4EAG packet) {
		Optional<Player> player = plugin.getProxy().getPlayer(new UUID(packet.playerUUIDMost, packet.playerUUIDLeast));
		if(player.isPresent()) {
			EaglerPlayerData conn2 = EaglerPipeline.getEaglerHandle(player.get());
			if(conn2 != null) {
				UUID uuid = conn2.getClientBrandUUID();
				if (uuid != null) {
					eaglerHandle.sendEaglerMessage(new SPacketOtherPlayerClientUUIDV4EAG(packet.requestId,
									uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()));
					return;
				}
			}else {
				eaglerHandle.sendEaglerMessage(new SPacketOtherPlayerClientUUIDV4EAG(packet.requestId,
								EaglerXVelocityAPIHelper.BRAND_VANILLA_UUID.getMostSignificantBits(),
								EaglerXVelocityAPIHelper.BRAND_VANILLA_UUID.getLeastSignificantBits()));
				return;
			}
		}
		eaglerHandle.sendEaglerMessage(new SPacketOtherPlayerClientUUIDV4EAG(packet.requestId, 0l, 0l));
	}

	public void handleClient(CPacketRequestServerInfoV4EAG packet) {
		EaglerPauseMenuConfig conf = plugin.getConfig().getPauseMenuConf();
		if (conf != null && conf.getEnabled()
				&& conf.getPacket().serverInfoMode == SPacketCustomizePauseMenuV4EAG.SERVER_INFO_MODE_SHOW_EMBED_OVER_WS
				&& Arrays.equals(conf.getServerInfoHash(), packet.requestHash)) {
			synchronized(eaglerHandle.serverInfoSendBuffer) {
				if(eaglerHandle.hasSentServerInfo.getAndSet(true)) {
					eaglerHandle.getPlayerObj().disconnect(Component.text("Duplicate server info request"));
					return;
				}
				eaglerHandle.serverInfoSendBuffer.clear();
				eaglerHandle.serverInfoSendBuffer.addAll(conf.getServerInfo());
			}
		}else {
			eaglerHandle.getPlayerObj().disconnect(Component.text("Invalid server info request"));
		}
	}

	public void handleClient(CPacketWebViewMessageV4EAG packet) {
		if(eaglerHandle.isWebViewChannelAllowed) {
			if(eaglerHandle.webViewMessageChannelOpen.get()) {
				if(eaglerHandle.getRPCEventSubscribed(EnumSubscribedEvent.WEBVIEW_MESSAGE)) {
					eaglerHandle.getRPCSessionHandler().sendRPCPacket(new SPacketRPCEventWebViewMessage(
							eaglerHandle.webViewMessageChannelName, packet.type, packet.data));
				}
				plugin.getProxy().getEventManager().fire(new EaglercraftWebViewMessageEvent(conn,
						eaglerHandle.getEaglerListenerConfig(), eaglerHandle.webViewMessageChannelName, packet));
			}
		}else {
			eaglerHandle.getPlayerObj().disconnect(Component.text("Webview channel permissions have not been enabled!"));
		}
	}

	public void handleClient(CPacketWebViewMessageEnV4EAG packet) {
		if(eaglerHandle.isWebViewChannelAllowed) {
			eaglerHandle.webViewMessageChannelOpen.set(packet.messageChannelOpen);
			String oldChannelName = eaglerHandle.webViewMessageChannelName;
			eaglerHandle.webViewMessageChannelName = packet.messageChannelOpen ? packet.channelName : null;
			if(eaglerHandle.getRPCEventSubscribed(EnumSubscribedEvent.WEBVIEW_OPEN_CLOSE)) {
				eaglerHandle.getRPCSessionHandler().sendRPCPacket(new SPacketRPCEventWebViewOpenClose(
						packet.messageChannelOpen, packet.messageChannelOpen ? packet.channelName : oldChannelName));
			}
			plugin.getProxy().getEventManager()
					.fire(new EaglercraftWebViewChannelEvent(conn, eaglerHandle.getEaglerListenerConfig(),
							packet.messageChannelOpen ? eaglerHandle.webViewMessageChannelName : oldChannelName,
							packet.messageChannelOpen ? EaglercraftWebViewChannelEvent.EventType.CHANNEL_OPEN
									: EaglercraftWebViewChannelEvent.EventType.CHANNEL_CLOSE));
		}else {
			eaglerHandle.getPlayerObj().disconnect(Component.text("Webview channel permissions have not been enabled!"));
		}
	}

}
