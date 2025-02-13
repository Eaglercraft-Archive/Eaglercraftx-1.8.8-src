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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.backend_rpc_protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.RandomAccess;
import java.util.UUID;

import com.velocitypowered.api.proxy.ServerConnection;

import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.EaglerBackendRPCHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.client.*;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.server.*;
import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.util.PacketImageData;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.EaglerXVelocityAPIHelper;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.EnumVoiceState;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.EnumWebViewState;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerPauseMenuConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPlayerData;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.protocol.GameProtocolMessageController;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.voice.VoiceService;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketCustomizePauseMenuV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketNotifBadgeHideV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketNotifBadgeShowV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketNotifIconsRegisterV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketNotifIconsReleaseV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SkinPacketVersionCache;

public class ServerV1RPCProtocolHandler implements EaglerBackendRPCHandler {

	protected final BackendRPCSessionHandler sessionHandler;
	protected final ServerConnection server;
	protected final EaglerPlayerData eaglerHandler;

	public ServerV1RPCProtocolHandler(BackendRPCSessionHandler sessionHandler, ServerConnection server, EaglerPlayerData eaglerHandler) {
		this.sessionHandler = sessionHandler;
		this.server = server;
		this.eaglerHandler = eaglerHandler;
	}

	public void handleClient(CPacketRPCRequestPlayerInfo packet) {
		switch(packet.requestType) {
		case CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_REAL_UUID: {
				sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeUUID(packet.requestID, eaglerHandler.getUniqueId()));
			}
			break;
		case CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_REAL_IP: {
				sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeString(packet.requestID, ((InetSocketAddress)eaglerHandler.getSocketAddress()).getAddress().getHostAddress()));
			}
			break;
		case CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_ORIGIN: {
				String origin = eaglerHandler.getOrigin();
				if(origin != null) {
					sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeString(packet.requestID, origin));
				}else {
					sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeNull(packet.requestID));
				}
			}
			break;
		case CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_USER_AGENT: {
				String userAgent = eaglerHandler.getUserAgent();
				if(userAgent != null) {
					sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeString(packet.requestID, userAgent));
				}else {
					sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeNull(packet.requestID));
				}
			}
			break;
		case CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_SKIN_DATA: {
				SkinPacketVersionCache skinData = EaglerXVelocity.getEagler().getSkinService().getSkin(eaglerHandler.getUniqueId());
				if(skinData != null) {
					sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeBytes(packet.requestID, skinData.getV3HandshakeData()));
				}else {
					sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeNull(packet.requestID));
				}
			}
			break;
		case CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_CAPE_DATA: {
				byte[] capeData = EaglerXVelocity.getEagler().getCapeService().getCapeHandshakeData(eaglerHandler.getUniqueId());
				if(capeData != null) {
					sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeBytes(packet.requestID, capeData));
				}else {
					sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeNull(packet.requestID));
				}
			}
			break;
		case CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_COOKIE: {
				boolean cookieEnabled = eaglerHandler.getCookieAllowed();
				byte[] cookieData = cookieEnabled ? eaglerHandler.getCookieData() : null;
				sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeCookie(packet.requestID, cookieEnabled, cookieData));
			}
			break;
		case CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_CLIENT_BRAND_STR: {
				String clientBrandStr = eaglerHandler.getEaglerBrandString();
				if(clientBrandStr != null) {
					sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeString(packet.requestID, clientBrandStr));
				}else {
					sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeNull(packet.requestID));
				}
			}
			break;
		case CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_CLIENT_VERSION_STR: {
				String clientVersionStr = eaglerHandler.getEaglerVersionString();
				if(clientVersionStr != null) {
					sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeString(packet.requestID, clientVersionStr));
				}else {
					sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeNull(packet.requestID));
				}
			}
			break;
		case CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_CLIENT_BRAND_VERSION_STR: {
				String clientBrandStr = eaglerHandler.getEaglerBrandString();
				String clientVersionStr = eaglerHandler.getEaglerVersionString();
				sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeString(packet.requestID, "" + clientBrandStr + " " + clientVersionStr));
			}
			break;
		case CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_CLIENT_BRAND_UUID: {
				UUID brandUUID = eaglerHandler.getClientBrandUUID();
				if(brandUUID != null) {
					sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeUUID(packet.requestID, brandUUID));
				}else {
					sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeNull(packet.requestID));
				}
			}
			break;
		case CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_CLIENT_VOICE_STATUS: {
				int voiceState;
				VoiceService svc = EaglerXVelocity.getEagler().getVoiceService();
				if(svc != null && eaglerHandler.getEaglerListenerConfig().getEnableVoiceChat()) {
					EnumVoiceState enumVoiceState = svc.getPlayerVoiceState(eaglerHandler.getUniqueId(), server.getServerInfo());
					switch(enumVoiceState) {
					case SERVER_DISABLE:
					default:
						voiceState = SPacketRPCResponseTypeVoiceStatus.VOICE_STATE_SERVER_DISABLE;
						break;
					case DISABLED:
						voiceState = SPacketRPCResponseTypeVoiceStatus.VOICE_STATE_DISABLED;
						break;
					case ENABLED:
						voiceState = SPacketRPCResponseTypeVoiceStatus.VOICE_STATE_ENABLED;
						break;
					}
				}else {
					voiceState = SPacketRPCResponseTypeVoiceStatus.VOICE_STATE_SERVER_DISABLE;
				}
				sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeVoiceStatus(packet.requestID, voiceState));
			}
			break;
		case CPacketRPCRequestPlayerInfo.REQUEST_PLAYER_CLIENT_WEBVIEW_STATUS: {
				EnumWebViewState enumWebViewState = eaglerHandler.getWebViewState();
				int webViewStatus;
				String webViewChannel;
				switch(enumWebViewState) {
				case NOT_SUPPORTED:
				default:
					webViewStatus = SPacketRPCResponseTypeWebViewStatus.WEBVIEW_STATE_NOT_SUPPORTED;
					webViewChannel = null;
					break;
				case SERVER_DISABLE:
					webViewStatus = SPacketRPCResponseTypeWebViewStatus.WEBVIEW_STATE_SERVER_DISABLE;
					webViewChannel = null;
					break;
				case CHANNEL_CLOSED:
					webViewStatus = SPacketRPCResponseTypeWebViewStatus.WEBVIEW_STATE_CHANNEL_CLOSED;
					webViewChannel = null;
					break;
				case CHANNEL_OPEN:
					webViewStatus = SPacketRPCResponseTypeWebViewStatus.WEBVIEW_STATE_CHANNEL_OPEN;
					webViewChannel = eaglerHandler.getWebViewMessageChannelName();
					break;
				}
				sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeWebViewStatus(packet.requestID, webViewStatus, webViewChannel));
			}
			break;
		default: {
				sessionHandler.sendRPCPacket(new SPacketRPCResponseTypeError(packet.requestID, "Unknown request type: " + packet.requestType));
			}
			break;
		}
	}

	public void handleClient(CPacketRPCSubscribeEvents packet) {
		sessionHandler.setSubscribedEvents(packet.eventsToEnable);
	}

	public void handleClient(CPacketRPCSetPlayerSkin packet) {
		try {
			byte[] bs = packet.skinPacket;
			if(bs.length < 5) {
				throw new IOException();
			}
			if(bs[0] == (byte)1) {
				if(bs.length != 5) {
					throw new IOException();
				}
				EaglerXVelocityAPIHelper.changePlayerSkinPreset(EaglerXVelocityAPIHelper.getPlayer(eaglerHandler),
						(bs[1] << 24) | (bs[2] << 16) | (bs[3] << 8) | (bs[4] & 0xFF), packet.notifyOthers);
			}else if(bs[0] == (byte)2) {
				if(bs.length < 2) {
					throw new IOException();
				}
				byte[] cust = new byte[bs.length - 2];
				System.arraycopy(bs, 2, cust, 0, cust.length);
				EaglerXVelocityAPIHelper.changePlayerSkinCustom(EaglerXVelocityAPIHelper.getPlayer(eaglerHandler),
						bs[1] & 0xFF, cust, packet.notifyOthers);
			}else {
				throw new IOException();
			}
		}catch(IOException ex) {
			EaglerXVelocity.logger().error(
					"[{}]: Invalid CPacketRPCSetPlayerSkin packet recieved for player \"{}\" from backend RPC protocol!",
					eaglerHandler.getSocketAddress(), eaglerHandler.getName());
			return;
		}
	}

	public void handleClient(CPacketRPCSetPlayerCape packet) {
		try {
			byte[] bs = packet.capePacket;
			if(bs.length < 5) {
				throw new IOException();
			}
			if(bs[0] == (byte)1) {
				if(bs.length != 5) {
					throw new IOException();
				}
				EaglerXVelocityAPIHelper.changePlayerCapePreset(EaglerXVelocityAPIHelper.getPlayer(eaglerHandler),
						(bs[1] << 24) | (bs[2] << 16) | (bs[3] << 8) | (bs[4] & 0xFF), packet.notifyOthers);
			}else if(bs[0] == (byte)2) {
				if(bs.length != 1174) {
					throw new IOException();
				}
				byte[] cust = new byte[bs.length - 1];
				System.arraycopy(bs, 1, cust, 0, cust.length);
				EaglerXVelocityAPIHelper.changePlayerCapeCustom(EaglerXVelocityAPIHelper.getPlayer(eaglerHandler),
						cust, true, packet.notifyOthers);
			}else {
				throw new IOException();
			}
		}catch(IOException ex) {
			EaglerXVelocity.logger().error(
					"[{}]: Invalid CPacketRPCSetPlayerCape packet recieved for player \"{}\" from backend RPC protocol!",
					eaglerHandler.getSocketAddress(), eaglerHandler.getName());
			return;
		}
	}

	public void handleClient(CPacketRPCSetPlayerCookie packet) {
		eaglerHandler.setCookieData(packet.cookieData, packet.expires, packet.revokeQuerySupported, packet.saveToDisk);
	}

	public void handleClient(CPacketRPCSetPlayerFNAWEn packet) {
		EaglerXVelocityAPIHelper.setEnableForceFNAWSkins(eaglerHandler, packet.enable, packet.force);
	}

	public void handleClient(CPacketRPCRedirectPlayer packet) {
		eaglerHandler.redirectPlayerToWebSocket(packet.redirectURI);
	}

	public void handleClient(CPacketRPCResetPlayerMulti packet) {
		EaglerXVelocityAPIHelper.resetPlayerMulti(EaglerXVelocityAPIHelper.getPlayer(eaglerHandler), packet.resetSkin,
				packet.resetCape, packet.resetFNAWForce, packet.notifyOtherPlayers);
	}

	public void handleClient(CPacketRPCSendWebViewMessage packet) {
		eaglerHandler.sendWebViewMessage(packet.messageType, packet.messageContent);
	}

	public void handleClient(CPacketRPCSetPauseMenuCustom packet) {
		if(eaglerHandler.getEaglerProtocol().ver >= 4) {
			EaglerPauseMenuConfig defaultConf = EaglerXVelocity.getEagler().getConfig().getPauseMenuConf();
			SPacketCustomizePauseMenuV4EAG defaultPacket = defaultConf.getPacket();
			int serverInfoMode = packet.serverInfoMode;
			String serverInfoButtonText;
			String serverInfoURL;
			byte[] serverInfoHash;
			int serverInfoEmbedPerms;
			String serverInfoEmbedTitle;
			if(serverInfoMode == CPacketRPCSetPauseMenuCustom.SERVER_INFO_MODE_INHERIT_DEFAULT) {
				serverInfoMode = defaultPacket.serverInfoMode;
				serverInfoButtonText = defaultPacket.serverInfoButtonText;
				serverInfoURL = defaultPacket.serverInfoURL;
				serverInfoHash = defaultPacket.serverInfoHash;
				serverInfoEmbedPerms = defaultPacket.serverInfoEmbedPerms;
				serverInfoEmbedTitle = defaultPacket.serverInfoEmbedTitle;
			}else {
				serverInfoButtonText = packet.serverInfoButtonText;
				serverInfoURL = packet.serverInfoURL;
				serverInfoHash = packet.serverInfoHash;
				serverInfoEmbedPerms = packet.serverInfoEmbedPerms;
				serverInfoEmbedTitle = packet.serverInfoEmbedTitle;
			}
			int discordButtonMode = packet.discordButtonMode;
			String discordButtonText;
			String discordInviteURL;
			if(discordButtonMode == CPacketRPCSetPauseMenuCustom.DISCORD_MODE_INHERIT_DEFAULT) {
				discordButtonMode = defaultPacket.discordButtonMode;
				discordButtonText = defaultPacket.discordButtonText;
				discordInviteURL = defaultPacket.discordInviteURL;
			}else {
				discordButtonText = packet.discordButtonText;
				discordInviteURL = packet.discordInviteURL;
			}
			Map<String, Integer> imageMappings = packet.imageMappings;
			List<PacketImageData> imageData = packet.imageData;
			List<net.lax1dude.eaglercraft.v1_8.socket.protocol.util.PacketImageData> imageDataConv = imageData != null
					? new ArrayList<>(imageData.size()) : null;
			if(imageDataConv != null) {
				for(int i = 0, l = imageData.size(); i < l; ++i) {
					PacketImageData etr = imageData.get(i);
					imageDataConv.add(new net.lax1dude.eaglercraft.v1_8.socket.protocol.util.PacketImageData
							(etr.width, etr.height, etr.rgba));
				}
			}
			eaglerHandler.sendEaglerMessage(new SPacketCustomizePauseMenuV4EAG(serverInfoMode, serverInfoButtonText,
					serverInfoURL, serverInfoHash, serverInfoEmbedPerms, serverInfoEmbedTitle, discordButtonMode,
					discordButtonText, discordInviteURL, imageMappings, imageDataConv));
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Recieved packet CPacketRPCSetPauseMenuCustom for player \"{}\" from backend RPC protocol, but their client does not support pause menu customization!",
					eaglerHandler.getSocketAddress(), eaglerHandler.getName());
		}
	}

	public void handleClient(CPacketRPCNotifIconRegister packet) {
		if(eaglerHandler.notificationSupported()) {
			List<SPacketNotifIconsRegisterV4EAG.CreateIcon> createIconsConv = new ArrayList<>(packet.notifIcons.size());
			for(Entry<UUID,PacketImageData> etr : packet.notifIcons.entrySet()) {
				UUID uuid = etr.getKey();
				PacketImageData imgData = etr.getValue();
				createIconsConv.add(new SPacketNotifIconsRegisterV4EAG.CreateIcon(uuid.getMostSignificantBits(),
						uuid.getLeastSignificantBits(), new net.lax1dude.eaglercraft.v1_8.socket.protocol.util.PacketImageData
						(imgData.width, imgData.height, imgData.rgba)));
			}
			eaglerHandler.sendEaglerMessage(new SPacketNotifIconsRegisterV4EAG(createIconsConv));
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Recieved packet CPacketRPCNotifIconRegister for player \"{}\" from backend RPC protocol, but their client does not support notifications!",
					eaglerHandler.getSocketAddress(), eaglerHandler.getName());
		}
	}

	public void handleClient(CPacketRPCNotifIconRelease packet) {
		if(eaglerHandler.notificationSupported()) {
			List<SPacketNotifIconsReleaseV4EAG.DestroyIcon> destroyIconsConv = new ArrayList<>(packet.iconsToRelease.size());
			if(packet.iconsToRelease instanceof RandomAccess) {
				List<UUID> lst = (List<UUID>)packet.iconsToRelease;
				for(int i = 0, l = lst.size(); i < l; ++i) {
					UUID uuid = lst.get(i);
					destroyIconsConv.add(new SPacketNotifIconsReleaseV4EAG.DestroyIcon(uuid.getMostSignificantBits(),
							uuid.getLeastSignificantBits()));
				}
			}else {
				for(UUID uuid : packet.iconsToRelease) {
					destroyIconsConv.add(new SPacketNotifIconsReleaseV4EAG.DestroyIcon(uuid.getMostSignificantBits(),
							uuid.getLeastSignificantBits()));
				}
			}
			eaglerHandler.sendEaglerMessage(new SPacketNotifIconsReleaseV4EAG(destroyIconsConv));
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Recieved packet CPacketRPCNotifIconRelease for player \"{}\" from backend RPC protocol, but their client does not support notifications!",
					eaglerHandler.getSocketAddress(), eaglerHandler.getName());
		}
	}

	public void handleClient(CPacketRPCNotifBadgeShow packet) {
		if(eaglerHandler.notificationSupported()) {
			SPacketNotifBadgeShowV4EAG.EnumBadgePriority translatedEnum;
			switch(packet.priority) {
			case LOW:
			default:
				translatedEnum = SPacketNotifBadgeShowV4EAG.EnumBadgePriority.LOW;
				break;
			case NORMAL:
				translatedEnum = SPacketNotifBadgeShowV4EAG.EnumBadgePriority.NORMAL;
				break;
			case HIGHER:
				translatedEnum = SPacketNotifBadgeShowV4EAG.EnumBadgePriority.HIGHER;
				break;
			case HIGHEST:
				translatedEnum = SPacketNotifBadgeShowV4EAG.EnumBadgePriority.HIGHEST;
				break;
			}
			eaglerHandler.sendEaglerMessage(new SPacketNotifBadgeShowV4EAG(packet.badgeUUID.getMostSignificantBits(),
					packet.badgeUUID.getLeastSignificantBits(), packet.bodyComponent, packet.titleComponent,
					packet.sourceComponent, packet.originalTimestampSec, packet.silent, translatedEnum,
					(packet.mainIconUUID != null ? packet.mainIconUUID.getMostSignificantBits() : 0l),
					(packet.mainIconUUID != null ? packet.mainIconUUID.getLeastSignificantBits() : 0l),
					(packet.titleIconUUID != null ? packet.titleIconUUID.getMostSignificantBits() : 0l),
					(packet.titleIconUUID != null ? packet.titleIconUUID.getLeastSignificantBits() : 0l),
					packet.hideAfterSec, packet.expireAfterSec, packet.backgroundColor, packet.bodyTxtColor,
					packet.titleTxtColor, packet.sourceTxtColor));
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Recieved packet CPacketRPCNotifBadgeShow for player \"{}\" from backend RPC protocol, but their client does not support notifications!",
					eaglerHandler.getSocketAddress(), eaglerHandler.getName());
		}
	}

	public void handleClient(CPacketRPCNotifBadgeHide packet) {
		if(eaglerHandler.notificationSupported()) {
			eaglerHandler.sendEaglerMessage(new SPacketNotifBadgeHideV4EAG(packet.badgeUUID.getMostSignificantBits(),
					packet.badgeUUID.getLeastSignificantBits()));
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Recieved packet CPacketRPCNotifBadgeHide for player \"{}\" from backend RPC protocol, but their client does not support notifications!",
					eaglerHandler.getSocketAddress(), eaglerHandler.getName());
		}
	}

	public void handleClient(CPacketRPCDisabled packet) {
		sessionHandler.handleDisabled();
	}

	public void handleClient(CPacketRPCSendRawMessage packet) {
		GameProtocolMessageController.sendPluginMessage(eaglerHandler.getEaglerMessageController().getUserConnection(), packet.messageChannel, packet.messageData);
	}

}