package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerBungeeConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerListenerConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.SimpleRateLimiter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.netty.ChannelWrapper;
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

/**
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
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
public class EaglerInitialHandler extends InitialHandler {

	public static class ClientCertificateHolder {
		public final byte[] data;
		public final int hash;
		public ClientCertificateHolder(byte[] data, int hash) {
			this.data = data;
			this.hash = hash;
		}
	}

	private final int gameProtocolVersion;
	private final String username;
	private final UUID playerUUID;
	private final UUID playerUUIDOffline;
	private final UUID playerUUIDRewrite;
	private LoginResult loginResult;
	private final InetSocketAddress eaglerAddress;
	private final InetSocketAddress virtualHost;
	private final Unsafe eaglerUnsafe;
	public final SimpleRateLimiter skinLookupRateLimiter;
	public final SimpleRateLimiter skinUUIDLookupRateLimiter;
	public final SimpleRateLimiter skinTextureDownloadRateLimiter;
	public final SimpleRateLimiter capeLookupRateLimiter;
	public final SimpleRateLimiter voiceConnectRateLimiter;
	public final String origin;
	public final ClientCertificateHolder clientCertificate;
	public final Set<ClientCertificateHolder> certificatesToSend;
	public final TIntSet certificatesSent;
	public boolean currentFNAWSkinEnableStatus = true;

	private static final Property[] NO_PROPERTIES = new Property[0];

	public EaglerInitialHandler(BungeeCord bungee, EaglerListenerConfig listener, final ChannelWrapper ch,
			int gameProtocolVersion, String username, UUID playerUUID, UUID offlineUUID, InetSocketAddress address,
			String host, String origin, ClientCertificateHolder clientCertificate) {
		super(bungee, listener);
		this.gameProtocolVersion = gameProtocolVersion;
		this.username = username;
		this.playerUUID = playerUUID;
		this.playerUUIDOffline = offlineUUID;
		this.playerUUIDRewrite = bungee.config.isIpForward() ? playerUUID : offlineUUID;
		this.eaglerAddress = address;
		this.origin = origin;
		this.skinLookupRateLimiter = new SimpleRateLimiter();
		this.skinUUIDLookupRateLimiter = new SimpleRateLimiter();
		this.skinTextureDownloadRateLimiter = new SimpleRateLimiter();
		this.capeLookupRateLimiter = new SimpleRateLimiter();
		this.voiceConnectRateLimiter = new SimpleRateLimiter();
		this.clientCertificate = clientCertificate;
		this.certificatesToSend = new HashSet();
		this.certificatesSent = new TIntHashSet();
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

	public static UUID generateOfflineUUID(byte[] username) {
		String offlinePlayerStr = "OfflinePlayer:";
		byte[] uuidHashGenerator = new byte[offlinePlayerStr.length() + username.length];
		System.arraycopy(offlinePlayerStr.getBytes(StandardCharsets.US_ASCII), 0, uuidHashGenerator, 0, offlinePlayerStr.length());
		System.arraycopy(username, 0, uuidHashGenerator, offlinePlayerStr.length(), username.length);
		return UUID.nameUUIDFromBytes(uuidHashGenerator);
	}

	void setLoginProfile(LoginResult obj) {
		this.loginResult = obj;
		try {
			Field f = InitialHandler.class.getDeclaredField("loginProfile");
			f.setAccessible(true);
			f.set(this, obj);
		}catch(Throwable t) {
		}
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
	public LoginResult getLoginProfile() {
		return loginResult;
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

	public EaglerListenerConfig getEaglerListenerConfig() {
		return (EaglerListenerConfig)getListener();
	}
}
