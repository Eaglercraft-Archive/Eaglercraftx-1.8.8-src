/*
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.collect.Collections2;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.EaglerXBungeeAPIHelper;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.EnumWebViewState;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.NotificationBadgeBuilder;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftVoiceStatusChangeEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerBungeeConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.backend_rpc_protocol.BackendRPCSessionHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.backend_rpc_protocol.EnumSubscribedEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.protocol.GameProtocolMessageController;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.SimpleRateLimiter;
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
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.Property;
import net.md_5.bungee.protocol.packet.EncryptionResponse;
import net.md_5.bungee.protocol.packet.Handshake;
import net.md_5.bungee.protocol.packet.LegacyHandshake;
import net.md_5.bungee.protocol.packet.LegacyPing;
import net.md_5.bungee.protocol.packet.LoginPayloadResponse;
import net.md_5.bungee.protocol.packet.LoginRequest;
import net.md_5.bungee.protocol.packet.PingPacket;
import net.md_5.bungee.protocol.packet.PluginMessage;
import net.md_5.bungee.protocol.packet.StatusRequest;

public class EaglerInitialHandler extends InitialHandler {

	public static class ClientCertificateHolder {
		public final byte[] data;
		public final int hash;
		public ClientCertificateHolder(byte[] data, int hash) {
			this.data = data;
			this.hash = hash;
		}
	}

	protected final int clientProtocolVersion;
	protected final int gameProtocolVersion;
	protected final String clientBrandString;
	protected final String clientVersionString;
	protected final UUID clientBrandUUID;
	protected final String username;
	protected final UUID playerUUID;
	protected final UUID playerUUIDOffline;
	protected final UUID playerUUIDRewrite;
	protected final InetSocketAddress eaglerAddress;
	protected final InetSocketAddress virtualHost;
	protected final Unsafe eaglerUnsafe;
	public final SimpleRateLimiter skinLookupRateLimiter;
	public final SimpleRateLimiter skinUUIDLookupRateLimiter;
	public final SimpleRateLimiter skinTextureDownloadRateLimiter;
	public final SimpleRateLimiter capeLookupRateLimiter;
	public final SimpleRateLimiter voiceConnectRateLimiter;
	protected final String origin;
	protected final String userAgent;
	public final ClientCertificateHolder clientCertificate;
	public final Set<ClientCertificateHolder> certificatesToSend;
	public final TIntSet certificatesSent;
	public final EaglerChannelWrapper ch;
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
	protected final AtomicReference<EaglercraftVoiceStatusChangeEvent.EnumVoiceState> lastVoiceState = new AtomicReference<>(
			EaglercraftVoiceStatusChangeEvent.EnumVoiceState.SERVER_DISABLE);

	private static final Property[] NO_PROPERTIES = new Property[0];

	public EaglerInitialHandler(BungeeCord bungee, EaglerListenerConfig listener, final EaglerChannelWrapper ch,
			int clientProtocolVersion, int gameProtocolVersion, String clientBrandString, String clientVersionString,
			UUID clientBrandUUID, String username, UUID playerUUID, UUID offlineUUID, InetSocketAddress address,
			String host, String origin, String userAgent, ClientCertificateHolder clientCertificate,
			boolean allowCookie, byte[] cookie, Map<String,byte[]> otherProfileData) {
		super(bungee, listener);
		this.ch = ch;
		this.clientProtocolVersion = clientProtocolVersion;
		this.gameProtocolVersion = gameProtocolVersion;
		this.clientBrandString = clientBrandString;
		this.clientVersionString = clientVersionString;
		this.clientBrandUUID = clientBrandUUID;
		this.username = username;
		this.playerUUID = playerUUID;
		this.playerUUIDOffline = offlineUUID;
		this.playerUUIDRewrite = bungee.config.isIpForward() ? playerUUID : offlineUUID;
		this.eaglerAddress = address;
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
		this.certificatesToSend = new HashSet<>();
		this.certificatesSent = new TIntHashSet();
		EaglerBungeeConfig conf = EaglerXBungee.getEagler().getConfig();
		SPacketCustomizePauseMenuV4EAG pkt = conf.getPauseMenuConf().getPacket();
		this.isWebViewChannelAllowed = pkt != null
				&& (pkt.serverInfoEmbedPerms & SPacketCustomizePauseMenuV4EAG.SERVER_INFO_EMBED_PERMS_MESSAGE_API) != 0;
		this.backedRPCSessionHandler = conf.getEnableBackendRPCAPI()
				? BackendRPCSessionHandler.createForPlayer(this) : null;
		if(clientCertificate != null) {
			this.certificatesSent.add(clientCertificate.hashCode());
		}
		if(host == null) host = "";
		int port = 25565;
		if(host.contains(":")) {
			int ind = host.lastIndexOf(':');
			try {
				port = Integer.parseInt(host.substring(ind + 1));
				host = host.substring(0, ind);
			} catch (NumberFormatException e) {
				//
			}
		}
		this.virtualHost = InetSocketAddress.createUnresolved(host, port);
		this.eaglerUnsafe = new Unsafe() {
			@Override
			public void sendPacket(DefinedPacket arg0) {
				ch.getHandle().writeAndFlush(arg0);
			}
		};
		Property[] profileProperties = NO_PROPERTIES;
		if(EaglerXBungee.getEagler().getConfig().getEnableIsEaglerPlayerProperty()) {
			profileProperties = new Property[] { EaglerBungeeConfig.isEaglerProperty };
		}
		setLoginProfile(new LoginResult(playerUUID.toString(), username, profileProperties));
		try {
			super.connected(ch);
		} catch (Exception e) {
		}
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
				this.disconnect(new TextComponent("Failed to write eaglercraft packet! (" + e.toString() + ")"));
			}
		}else {
			throw new IllegalStateException("Race condition detected, messageProtocolController is null! (wait until getEaglerMessageController() does not return null before sending the packet)");
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
			EaglerXBungee.logger().warning("[" + getSocketAddress().toString() + "]: Some plugin tried to send a webview message to player \"" + username + "\", but the player doesn't have a webview message channel open!");
		}
	}
	public void sendWebViewMessage(String str) {
		if(webViewMessageChannelOpen.get()) {
			sendEaglerMessage(new SPacketWebViewMessageV4EAG(str));
		}else {
			EaglerXBungee.logger().warning("[" + getSocketAddress().toString() + "]: Some plugin tried to send a webview message to player \"" + username + "\", but the player doesn't have a webview message channel open!");
		}
	}

	public void sendWebViewMessage(byte[] bin) {
		if(webViewMessageChannelOpen.get()) {
			sendEaglerMessage(new SPacketWebViewMessageV4EAG(bin));
		}else {
			EaglerXBungee.logger().warning("[" + getSocketAddress().toString() + "]: Some plugin tried to send a webview message to player \"" + username + "\", but the player doesn't have a webview message channel open!");
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
			EaglerXBungee.logger().warning("[" + getSocketAddress().toString() + "]: Some plugin tried to set a cookie for player \"" + username + "\", but the player has cookies disabled!");
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
			EaglerXBungee.logger().warning("[" + getSocketAddress().toString() + "]: Some plugin tried to register notification icons for player \"" + username + "\", but the player has notifications disabled!");
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
			EaglerXBungee.logger().warning("[" + getSocketAddress().toString() + "]: Some plugin tried to register notification icons for player \"" + username + "\", but the player has notifications disabled!");
		}
	}

	public void showNotificationBadge(NotificationBadgeBuilder badgeBuilder) {
		if(clientProtocolVersion >= 4) {
			sendEaglerMessage(badgeBuilder.buildPacket());
		}else {
			EaglerXBungee.logger().warning("[" + getSocketAddress().toString() + "]: Some plugin tried to show notification badges to player \"" + username + "\", but the player has notifications disabled!");
		}
	}

	public void showNotificationBadge(SPacketNotifBadgeShowV4EAG badgePacket) {
		if(clientProtocolVersion >= 4) {
			sendEaglerMessage(badgePacket);
		}else {
			EaglerXBungee.logger().warning("[" + getSocketAddress().toString() + "]: Some plugin tried to show notification badges to player \"" + username + "\", but the player has notifications disabled!");
		}
	}

	public void hideNotificationBadge(UUID badgeUUID) {
		if(clientProtocolVersion >= 4) {
			sendEaglerMessage(new SPacketNotifBadgeHideV4EAG(badgeUUID.getMostSignificantBits(), badgeUUID.getLeastSignificantBits()));
		}else {
			EaglerXBungee.logger().warning("[" + getSocketAddress().toString() + "]: Some plugin tried to hide notification badges for player \"" + username + "\", but the player has notifications disabled!");
		}
	}

	public void releaseNotificationIcon(UUID uuid) {
		if(clientProtocolVersion >= 4) {
			sendEaglerMessage(new SPacketNotifIconsReleaseV4EAG(
				Arrays.asList(new SPacketNotifIconsReleaseV4EAG.DestroyIcon(uuid.getMostSignificantBits(),
						uuid.getLeastSignificantBits()))));
		}else {
			EaglerXBungee.logger().warning("[" + getSocketAddress().toString() + "]: Some plugin tried to release notification icons for player \"" + username + "\", but the player has notifications disabled!");
		}
	}

	public void releaseNotificationIcons(Collection<UUID> uuids) {
		if(clientProtocolVersion >= 4) {
			sendEaglerMessage(new SPacketNotifIconsReleaseV4EAG(new ArrayList<>(Collections2.transform(uuids,
					(etr) -> new SPacketNotifIconsReleaseV4EAG.DestroyIcon(etr.getMostSignificantBits(),
							etr.getLeastSignificantBits())))));
		}else {
			EaglerXBungee.logger().warning("[" + getSocketAddress().toString() + "]: Some plugin tried to release notification icons for player \"" + username + "\", but the player has notifications disabled!");
		}
	}

	public boolean redirectToWebSocketSupported() {
		return clientProtocolVersion >= 4;
	}

	public void redirectPlayerToWebSocket(String serverAddress) {
		if(getEaglerProtocol().ver >= 4) {
			sendEaglerMessage(new SPacketRedirectClientV4EAG(serverAddress));
		}else {
			EaglerXBungee.logger().warning("[" + getSocketAddress().toString() + "]: Some plugin tried to redirect player \"" + username + "\" to a different websocket, but that player's client doesn't support this feature!");
		}
	}

	public BackendRPCSessionHandler getRPCSessionHandler() {
		return backedRPCSessionHandler;
	}

	public boolean getRPCEventSubscribed(EnumSubscribedEvent event) {
		return backedRPCSessionHandler != null && backedRPCSessionHandler.isSubscribed(event);
	}

	public void handleBackendRPCPacket(Server server, byte[] data) {
		if(backedRPCSessionHandler != null) {
			backedRPCSessionHandler.handleRPCPacket(server, data);
		}else {
			EaglerXBungee.logger().severe("[" + getSocketAddress().toString() + "]: Server tried to send backend RPC packet to player \"" + username + "\" but this feature is not enabled. Enable it by setting \"enable_backend_rpc_api: true\" in settings.yml");
		}
	}

	public void fireVoiceStateChange(EaglercraftVoiceStatusChangeEvent.EnumVoiceState state) {
		EaglercraftVoiceStatusChangeEvent.EnumVoiceState oldState = lastVoiceState.getAndSet(state);
		if(state != oldState) {
			BungeeCord.getInstance().getPluginManager().callEvent(new EaglercraftVoiceStatusChangeEvent(
					EaglerXBungeeAPIHelper.getPlayer(this), getEaglerListenerConfig(), this, oldState, state));
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

	public static UUID generateOfflineUUID(byte[] username) {
		String offlinePlayerStr = "OfflinePlayer:";
		byte[] uuidHashGenerator = new byte[offlinePlayerStr.length() + username.length];
		System.arraycopy(offlinePlayerStr.getBytes(StandardCharsets.US_ASCII), 0, uuidHashGenerator, 0, offlinePlayerStr.length());
		System.arraycopy(username, 0, uuidHashGenerator, offlinePlayerStr.length(), username.length);
		return UUID.nameUUIDFromBytes(uuidHashGenerator);
	}

	private static final Field loginProfileField;
	
	static {
		try {
			loginProfileField = InitialHandler.class.getDeclaredField("loginProfile");
			loginProfileField.setAccessible(true);
		}catch(Throwable t) {
			throw new RuntimeException("Could not access loginProfile field", t);
		}
	}

	void setLoginProfile(LoginResult obj) {
		try {
			loginProfileField.set(this, obj);
		}catch(Throwable t) {
			throw new RuntimeException("Could not perform reflection", t);
		}
	}

	public byte[] getOtherProfileDataFromHandshake(String name) {
		return otherProfileDataFromHanshake.get(name);
	}

	@Override
	public void handle(PacketWrapper packet) throws Exception {
	}

	@Override
	public void handle(PluginMessage pluginMessage) throws Exception {
	}

	@Override
	public void handle(LegacyHandshake legacyHandshake) throws Exception {
	}

	@Override
	public void handle(LegacyPing ping) throws Exception {
	}

	@Override
	public void handle(StatusRequest statusRequest) throws Exception {
	}

	@Override
	public void handle(PingPacket ping) throws Exception {
	}

	@Override
	public void handle(Handshake handshake) throws Exception {
	}

	@Override
	public void handle(LoginRequest loginRequest) throws Exception {
	}

	@Override
	public void handle(EncryptionResponse encryptResponse) throws Exception {
	}

	@Override
	public void handle(LoginPayloadResponse response) throws Exception {
	}

	@Override
	public void disconnect(String reason) {
		super.disconnect(reason);
	}

	@Override
	public void disconnect(BaseComponent... reason) {
		super.disconnect(reason);
	}

	@Override
	public void disconnect(BaseComponent reason) {
		super.disconnect(reason);
	}

	@Override
	public String getName() {
		return username;
	}

	@Override
	public int getVersion() {
		return gameProtocolVersion;
	}

	@Override
	public Handshake getHandshake() {
		return new Handshake(gameProtocolVersion, virtualHost.getHostName(), virtualHost.getPort(),
				gameProtocolVersion);
	}

	@Override
	public LoginRequest getLoginRequest() {
		throw new UnsupportedOperationException("A plugin attempted to retrieve the LoginRequest packet from an EaglercraftX connection, "
				+ "which is not supported because Eaglercraft does not use online mode encryption. Please analyze the stack trace of this "
				+ "exception and reconfigure or remove the offending plugin to fix this issue.");
	}

	@Override
	public PluginMessage getBrandMessage() {
		String brand = "EaglercraftX";
		byte[] pkt = new byte[brand.length() + 1];
		pkt[0] = (byte)brand.length();
		System.arraycopy(brand.getBytes(StandardCharsets.US_ASCII), 0, pkt, 1, brand.length());
		return new PluginMessage("MC|Brand", pkt, true);
	}

	@Override
	public UUID getUniqueId() {
		return playerUUID;
	}

	@Override
	public UUID getOfflineId() {
		return playerUUIDOffline;
	}

	@Override
	public UUID getRewriteId() {
		return playerUUIDRewrite;
	}

	@Override
	public void setUniqueId(UUID uuid) {
		throw new UnsupportedOperationException("EaglercraftXBungee does not support changing player UUIDs at login! (yet)");
	}

	@Override
	public void setOnlineMode(boolean onlineMode) {
		throw new UnsupportedOperationException("EaglercraftXBungee does not support changing player online mode status at login! (yet)");
	}

	@Override
	public String getUUID() {
		return playerUUID.toString().replace("-", "");
	}

	@Override
	public InetSocketAddress getVirtualHost() {
		return virtualHost;
	}

	@Override
	public SocketAddress getSocketAddress() {
		return eaglerAddress;
	}

	@Override
	public Unsafe unsafe() {
		return eaglerUnsafe;
	}

	public String getOrigin() {
		return origin;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public EaglerListenerConfig getEaglerListenerConfig() {
		return (EaglerListenerConfig)getListener();
	}

}