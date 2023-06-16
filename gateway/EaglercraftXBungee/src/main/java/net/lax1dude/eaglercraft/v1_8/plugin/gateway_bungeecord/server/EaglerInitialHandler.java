package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerBungeeConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.SimpleRateLimiter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ListenerInfo;
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
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info)
 * 
 */
public class EaglerInitialHandler extends InitialHandler {

	private final int gameProtocolVersion;
	private final String username;
	private final UUID playerUUID;
	private LoginResult loginResult;
	private final InetSocketAddress eaglerAddress;
	private final InetSocketAddress virtualHost;
	private final Unsafe eaglerUnsafe;
	public final SimpleRateLimiter skinLookupRateLimiter;
	public final SimpleRateLimiter skinUUIDLookupRateLimiter;
	public final SimpleRateLimiter skinTextureDownloadRateLimiter;
	public final String origin;

	public EaglerInitialHandler(BungeeCord bungee, ListenerInfo listener, final ChannelWrapper ch,
			int gameProtocolVersion, String username, UUID playerUUID, InetSocketAddress address, String host,
			String origin) {
		super(bungee, listener);
		this.gameProtocolVersion = gameProtocolVersion;
		this.username = username;
		this.playerUUID = playerUUID;
		this.eaglerAddress = address;
		this.origin = origin;
		this.skinLookupRateLimiter = new SimpleRateLimiter();
		this.skinUUIDLookupRateLimiter = new SimpleRateLimiter();
		this.skinTextureDownloadRateLimiter = new SimpleRateLimiter();
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
		setLoginProfile(new LoginResult(playerUUID.toString(), username, new Property[] { EaglerBungeeConfig.isEaglerProperty }));
		try {
			super.connected(ch);
		} catch (Exception e) {
		}
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
		return playerUUID;
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

}
