/*
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.collect.Collections2;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.util.GameProfile;
import com.velocitypowered.proxy.connection.backend.VelocityServerConnection;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;

import net.kyori.adventure.text.Component;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.EnumWebViewState;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.NotificationBadgeBuilder;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event.EaglercraftVoiceStatusChangeEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerVelocityConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.backend_rpc_protocol.BackendRPCSessionHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.backend_rpc_protocol.EnumSubscribedEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.protocol.GameProtocolMessageController;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.SimpleRateLimiter;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageProtocol;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketCustomizePauseMenuV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketNotifBadgeHideV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketNotifBadgeShowV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketNotifIconsRegisterV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketNotifIconsReleaseV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketRedirectClientV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketSetServerCookieV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketWebViewMessageV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.PacketImageData;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SkinPacketVersionCache;

public class EaglerPlayerData {

	public static class ClientCertificateHolder {
		public final byte[] data;
		public final int hash;
		public ClientCertificateHolder(byte[] data, int hash) {
			this.data = data;
			this.hash = hash;
		}
	}

	protected final EaglerConnectionInstance connInstance;
	protected final int clientProtocolVersion;
	protected final int gameProtocolVersion;
	protected final String clientBrandString;
	protected final String clientVersionString;
	protected final UUID clientBrandUUID;
	protected final String username;
	protected final UUID playerUUID;
	protected final InetSocketAddress eaglerAddress;
	public final SimpleRateLimiter skinLookupRateLimiter;
	public final SimpleRateLimiter skinUUIDLookupRateLimiter;
	public final SimpleRateLimiter skinTextureDownloadRateLimiter;
	public final SimpleRateLimiter capeLookupRateLimiter;
	public final SimpleRateLimiter voiceConnectRateLimiter;
	public final EaglerListenerConfig listener;
	public final String origin;
	protected final String userAgent;
	public final ClientCertificateHolder clientCertificate;
	public final Set<ClientCertificateHolder> certificatesToSend;
	public final Set<Integer> certificatesSent;
	public final AtomicBoolean currentFNAWSkinEnableStatus = new AtomicBoolean(true);
	public final AtomicBoolean currentFNAWSkinForceStatus = new AtomicBoolean(false);
	volatile GameProtocolMessageController messageProtocolController = null;
	protected final boolean allowCookie;
	protected volatile byte[] cookie;
	public volatile SkinPacketVersionCache originalSkin = null;
	public volatile GameMessagePacket originalCape = null;
	protected final Map<String,byte[]> otherProfileDataFromHanshake;
	public boolean isWebViewChannelAllowed = false;
	public final AtomicBoolean webViewMessageChannelOpen = new AtomicBoolean(false);
	public volatile String webViewMessageChannelName = null;
	public final AtomicBoolean hasSentServerInfo = new AtomicBoolean(false);
	public final List<GameMessagePacket> serverInfoSendBuffer = new LinkedList<>();
	protected BackendRPCSessionHandler backedRPCSessionHandler = null;
	protected GameProfile gameProfile;
	protected final AtomicReference<EaglercraftVoiceStatusChangeEvent.EnumVoiceState> lastVoiceState = new AtomicReference<>(
			EaglercraftVoiceStatusChangeEvent.EnumVoiceState.SERVER_DISABLE);

	public EaglerPlayerData(EaglerConnectionInstance connInstance, EaglerListenerConfig listener,
			int clientProtocolVersion, int gameProtocolVersion, String clientBrandString, String clientVersionString,
			UUID clientBrandUUID, String username, UUID playerUUID, InetSocketAddress eaglerAddress, String origin,
			String userAgent, ClientCertificateHolder clientCertificate, boolean allowCookie, byte[] cookie,
			Map<String, byte[]> otherProfileData, GameProfile gameProfile) {
		this.connInstance = connInstance;
		this.listener = listener;
		this.clientProtocolVersion = clientProtocolVersion;
		this.gameProtocolVersion = gameProtocolVersion;
		this.clientBrandString = clientBrandString;
		this.clientVersionString = clientVersionString;
		this.clientBrandUUID = clientBrandUUID;
		this.username = username;
		this.playerUUID = playerUUID;
		this.eaglerAddress = eaglerAddress;
		this.origin = origin;
		this.userAgent = userAgent;
		this.skinLookupRateLimiter = new SimpleRateLimiter();
		this.skinUUIDLookupRateLimiter = new SimpleRateLimiter();
		this.skinTextureDownloadRateLimiter = new SimpleRateLimiter();
		this.capeLookupRateLimiter = new SimpleRateLimiter();
		this.voiceConnectRateLimiter = new SimpleRateLimiter();
		this.allowCookie = allowCookie;
		this.cookie = cookie;
		this.otherProfileDataFromHanshake = otherProfileData;
		this.clientCertificate = clientCertificate;
		this.gameProfile = gameProfile;
		this.certificatesToSend = new HashSet<>();
		this.certificatesSent = new HashSet<>();
		EaglerVelocityConfig conf = EaglerXVelocity.getEagler().getConfig();
		SPacketCustomizePauseMenuV4EAG pkt = conf.getPauseMenuConf().getPacket();
		this.isWebViewChannelAllowed = pkt != null
				&& (pkt.serverInfoEmbedPerms & SPacketCustomizePauseMenuV4EAG.SERVER_INFO_EMBED_PERMS_MESSAGE_API) != 0;
		this.backedRPCSessionHandler = conf.getEnableBackendRPCAPI()
				? BackendRPCSessionHandler.createForPlayer(this) : null;
		if(clientCertificate != null) {
			this.certificatesSent.add(clientCertificate.hashCode());
		}
	}

	public InetSocketAddress getSocketAddress() {
		return eaglerAddress;
	}

	public GameProtocolMessageController getEaglerMessageController() {
		return messageProtocolController;
	}

	public GamePluginMessageProtocol getEaglerProtocol() {
		return messageProtocolController == null ? GamePluginMessageProtocol.getByVersion(clientProtocolVersion)
				: messageProtocolController.protocol;
	}

	public int getEaglerProtocolHandshake() {
		return clientProtocolVersion;
	}

	public void sendEaglerMessage(GameMessagePacket pkt) {
		if(messageProtocolController != null) {
			try {
				messageProtocolController.sendPacket(pkt);
			} catch (IOException e) {
				connInstance.userConnection.disconnect(Component.text("Failed to write eaglercraft packet! (" + e.toString() + ")"));
			}
		}else {
			throw new IllegalStateException("Race condition detected, messageProtocolController is null!");
		}
	}

	public boolean getWebViewSupport() {
		return getEaglerProtocol().ver >= 4;
	}

	public void setWebViewChannelAllowed(boolean en) {
		isWebViewChannelAllowed = en;
	}
	public boolean getWebViewChannelAllowed() {
		return isWebViewChannelAllowed;
	}

	public boolean getWebViewMessageChannelOpen() {
		return webViewMessageChannelOpen.get();
	}

	public String getWebViewMessageChannelName() {
		return webViewMessageChannelName;
	}

	public void sendWebViewMessage(int type, byte[] bytes) {
		if(webViewMessageChannelOpen.get()) {
			sendEaglerMessage(new SPacketWebViewMessageV4EAG(type, bytes));
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Some plugin tried to send a webview message to player \"{}\", but the player doesn't have a webview message channel open!",
					getSocketAddress(), username);
		}
	}
	public void sendWebViewMessage(String str) {
		if(webViewMessageChannelOpen.get()) {
			sendEaglerMessage(new SPacketWebViewMessageV4EAG(str));
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Some plugin tried to send a webview message to player \"{}\", but the player doesn't have a webview message channel open!",
					getSocketAddress(), username);
		}
	}

	public void sendWebViewMessage(byte[] bin) {
		if(webViewMessageChannelOpen.get()) {
			sendEaglerMessage(new SPacketWebViewMessageV4EAG(bin));
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Some plugin tried to send a webview message to player \"{}\", but the player doesn't have a webview message channel open!",
					getSocketAddress(), username);
		}
	}

	public EnumWebViewState getWebViewState() {
		if(!getWebViewSupport()) {
			return EnumWebViewState.NOT_SUPPORTED;
		}
		if(isWebViewChannelAllowed) {
			if(webViewMessageChannelOpen.get()) {
				return EnumWebViewState.CHANNEL_OPEN;
			}else {
				return EnumWebViewState.CHANNEL_CLOSED;
			}
		}else {
			return EnumWebViewState.SERVER_DISABLE;
		}
	}

	public boolean getCookieAllowed() {
		return allowCookie;
	}

	public byte[] getCookieData() {
		return allowCookie ? cookie : null;
	}

	public void setCookieData(byte[] data, long expiresAfter, TimeUnit timeUnit) {
		setCookieData(data, timeUnit.toSeconds(expiresAfter), false, true);
	}

	public void setCookieData(byte[] data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported) {
		setCookieData(data, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, true);
	}

	public void setCookieData(byte[] data, long expiresAfter, TimeUnit timeUnit, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		setCookieData(data, timeUnit.toSeconds(expiresAfter), revokeQuerySupported, clientSaveCookieToDisk);
	}

	public void setCookieData(byte[] data, long expiresAfterSec, boolean revokeQuerySupported, boolean clientSaveCookieToDisk) {
		if(allowCookie) {
			if(expiresAfterSec < 0l) {
				expiresAfterSec = 0l;
				data = null;
			}
			if(data == null) {
				cookie = null;
				sendEaglerMessage(new SPacketSetServerCookieV4EAG(null, 01, false, false));
				return;
			}
			if(data.length > 255) {
				throw new IllegalArgumentException("Cookie cannot be longer than 255 bytes!");
			}
			if(expiresAfterSec > 604800l) {
				throw new IllegalArgumentException("Cookie cannot be set for longer than 7 days! (tried " + (expiresAfterSec / 604800l) + " days)");
			}
			cookie = data;
			sendEaglerMessage(new SPacketSetServerCookieV4EAG(data, expiresAfterSec, revokeQuerySupported, clientSaveCookieToDisk));
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Some plugin tried to set a cookie for player \"{}\", but the player has cookies disabled!",
					getSocketAddress(), username);
		}
	}

	public void clearCookieData() {
		setCookieData(null, 0, false, false);
	}

	public boolean notificationSupported() {
		return clientProtocolVersion >= 4;
	}

	public void registerNotificationIcon(UUID uuid, PacketImageData imageData) {
		if(clientProtocolVersion >= 4) {
			sendEaglerMessage(new SPacketNotifIconsRegisterV4EAG(
					Arrays.asList(new SPacketNotifIconsRegisterV4EAG.CreateIcon(uuid.getMostSignificantBits(),
							uuid.getLeastSignificantBits(), imageData))));
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Some plugin tried to register notification icons for player \"{}\", but the player has notifications disabled!",
					getSocketAddress(), username);
		}
	}

	public void registerNotificationIcons(Map<UUID,PacketImageData> imageDatas) {
		if(clientProtocolVersion >= 4) {
			sendEaglerMessage(new SPacketNotifIconsRegisterV4EAG(
					new ArrayList<>(Collections2.transform(imageDatas.entrySet(), (etr) -> {
						UUID key = etr.getKey();
						return new SPacketNotifIconsRegisterV4EAG.CreateIcon(key.getMostSignificantBits(),
								key.getLeastSignificantBits(), etr.getValue());
					}))));
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Some plugin tried to register notification icons for player \"{}\", but the player has notifications disabled!",
					getSocketAddress(), username);
		}
	}

	public void showNotificationBadge(NotificationBadgeBuilder badgeBuilder) {
		if(clientProtocolVersion >= 4) {
			sendEaglerMessage(badgeBuilder.buildPacket());
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Some plugin tried to show notification badges to player \"{}\", but the player has notifications disabled!",
					getSocketAddress(), username);
		}
	}

	public void showNotificationBadge(SPacketNotifBadgeShowV4EAG badgePacket) {
		if(clientProtocolVersion >= 4) {
			sendEaglerMessage(badgePacket);
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Some plugin tried to show notification badges to player \"{}\", but the player has notifications disabled!",
					getSocketAddress(), username);
		}
	}

	public void hideNotificationBadge(UUID badgeUUID) {
		if(clientProtocolVersion >= 4) {
			sendEaglerMessage(new SPacketNotifBadgeHideV4EAG(badgeUUID.getMostSignificantBits(), badgeUUID.getLeastSignificantBits()));
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Some plugin tried to hide notification badges for player \"{}\", but the player has notifications disabled!",
					getSocketAddress(), username);
		}
	}

	public void releaseNotificationIcon(UUID uuid) {
		if(clientProtocolVersion >= 4) {
			sendEaglerMessage(new SPacketNotifIconsReleaseV4EAG(
				Arrays.asList(new SPacketNotifIconsReleaseV4EAG.DestroyIcon(uuid.getMostSignificantBits(),
						uuid.getLeastSignificantBits()))));
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Some plugin tried to release notification icons for player \"{}\", but the player has notifications disabled!",
					getSocketAddress(), username);
		}
	}

	public void releaseNotificationIcons(Collection<UUID> uuids) {
		if(clientProtocolVersion >= 4) {
			sendEaglerMessage(new SPacketNotifIconsReleaseV4EAG(new ArrayList<>(Collections2.transform(uuids,
					(etr) -> new SPacketNotifIconsReleaseV4EAG.DestroyIcon(etr.getMostSignificantBits(),
							etr.getLeastSignificantBits())))));
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Some plugin tried to release notification icons for player \"{}\", but the player has notifications disabled!",
					getSocketAddress(), username);
		}
	}

	public boolean redirectToWebSocketSupported() {
		return clientProtocolVersion >= 4;
	}

	public void redirectPlayerToWebSocket(String serverAddress) {
		if(getEaglerProtocol().ver >= 4) {
			sendEaglerMessage(new SPacketRedirectClientV4EAG(serverAddress));
		}else {
			EaglerXVelocity.logger().warn(
					"[{}]: Some plugin tried to redirect player \"{}\" to a different websocket, but that player's client doesn't support this feature!",
					getSocketAddress(), username);
		}
	}

	public BackendRPCSessionHandler getRPCSessionHandler() {
		return backedRPCSessionHandler;
	}

	public boolean getRPCEventSubscribed(EnumSubscribedEvent event) {
		return backedRPCSessionHandler != null && backedRPCSessionHandler.isSubscribed(event);
	}

	public void handleBackendRPCPacket(ServerConnection server, byte[] data) {
		if(backedRPCSessionHandler != null) {
			backedRPCSessionHandler.handleRPCPacket(server, data);
		}else {
			EaglerXVelocity.logger().error(
					"[{}]: Server tried to send backend RPC packet to player \"{}\" but this feature is not enabled. Enable it by setting \"enable_backend_rpc_api: true\" in settings.yml",
					getSocketAddress(), username);
		}
	}

	public void fireVoiceStateChange(EaglercraftVoiceStatusChangeEvent.EnumVoiceState state) {
		EaglercraftVoiceStatusChangeEvent.EnumVoiceState oldState = lastVoiceState.getAndSet(state);
		if(state != oldState) {
			EaglerXVelocity.proxy().getEventManager().fireAndForget(
					new EaglercraftVoiceStatusChangeEvent(getPlayerObj(), listener, this, oldState, state));
		}
	}

	public String getEaglerBrandString() {
		return clientBrandString;
	}

	public String getEaglerVersionString() {
		return clientVersionString;
	}

	public UUID getClientBrandUUID() {
		return clientBrandUUID;
	}

	public byte[] getOtherProfileDataFromHandshake(String name) {
		return otherProfileDataFromHanshake.get(name);
	}

	public String getOrigin() {
		return origin;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public EaglerListenerConfig getEaglerListenerConfig() {
		return listener;
	}

	public String getName() {
		return username;
	}

	public UUID getUniqueId() {
		return playerUUID;
	}

	public ConnectedPlayer getPlayerObj() {
		return connInstance.userConnection;
	}

	public VelocityServerConnection getConnectedServer() {
		ConnectedPlayer conn = connInstance.userConnection;
		return conn != null ? conn.getConnectedServer() : null;
	}

	public boolean isOnlineMode() {
		ConnectedPlayer conn = connInstance.userConnection;
		return conn != null && conn.isOnlineMode();
	}

	public GameProfile getGameProfile() {
		return gameProfile;
	}

}