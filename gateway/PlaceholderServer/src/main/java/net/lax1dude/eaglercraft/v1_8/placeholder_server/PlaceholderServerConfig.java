package net.lax1dude.eaglercraft.v1_8.placeholder_server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Copyright (c) 2022 LAX1DUDE. All Rights Reserved.
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
public class PlaceholderServerConfig {

	public static final File configFile = new File("config.json");

	public static String host = "0.0.0.0";
	public static int port = 8081;
	public static String icon = null;
	public static String serverName = "EaglercraftX 1.8 Server";
	public static String serverUUID = "";
	public static int clientTimeout = 3000;
	public static String motd1 = "Coming Soon";
	public static String motd2 = "";
	public static String kick = "This server is still under construction";
	public static String redirect = "";

	public static byte[] cachedIconPacket = null;
	public static byte[] cachedLegacyKickRedirectPacket = null;
	public static byte[] cachedKickPacket = null;

	public static void load() throws IOException {
		if(!configFile.exists()) {
			System.out.println("Writing new config file to: " + configFile.getName());
			int i;
			try(Reader is = new InputStreamReader(PlaceholderServerConfig.class.getResourceAsStream("/config_default.json"));
					OutputStream os = new FileOutputStream(configFile)) {
				char[] copyBuffer = new char[1024];
				StringBuilder sb = new StringBuilder();
				while((i = is.read(copyBuffer)) != -1) {
					sb.append(copyBuffer, 0, i);
				}
				String str = sb.toString();
				str = str.replace("${random_uuid}", UUID.randomUUID().toString());
				os.write(str.getBytes(StandardCharsets.UTF_8));
			}
			try(InputStream is = PlaceholderServerConfig.class.getResourceAsStream("/server-icon_default.png");
					OutputStream os = new FileOutputStream(new File("server-icon.png"))) {
				byte[] copyBuffer = new byte[1024];
				while((i = is.read(copyBuffer)) != -1) {
					os.write(copyBuffer, 0, i);
				}
			}
		}
		
		System.out.println("Reading config file: " + configFile.getName());
		byte[] fileBytes = new byte[(int)configFile.length()];
		try(InputStream is = new FileInputStream(configFile)) {
			int i = 0, j;
			while(i < fileBytes.length && (j = is.read(fileBytes, i, fileBytes.length - i)) != -1) {
				i += j;
			}
		}
		
		try {
			JSONObject loaded = new JSONObject(new String(fileBytes, StandardCharsets.UTF_8));
			host = loaded.getString("server_host");
			port = loaded.getInt("server_port");
			icon = loaded.getString("server_icon");
			serverName = loaded.getString("server_name");
			serverUUID = loaded.getString("server_uuid");
			clientTimeout = loaded.getInt("client_timeout");
			JSONArray motd = loaded.getJSONArray("server_motd");
			motd1 = motd.getString(0);
			if(motd.length() > 1) {
				motd2 = motd.getString(1);
			}
			kick = loaded.getString("kick_message");
			if(kick.length() > 255) {
				kick = kick.substring(0, 255);
				System.err.println("Warning: kick message was truncated to 255 characters");
			}
			redirect = loaded.optString("redirect_legacy", null);
		}catch(Throwable t) {
			throw new IOException("Could not load config file \"" + configFile.getAbsolutePath() + "\"!", t);
		}
		
		cacheKickPacket();
		cacheRedirectPacket();
		
		if(icon != null && icon.length() > 0) {
			cacheIconPacket();
		}
	}
	
	private static void cacheKickPacket() throws IOException {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bao);
		dos.writeByte(0xFF);
		dos.writeByte(0x08);
		dos.writeByte(kick.length());
		for(int i = 0, l = kick.length(); i < l; ++i) {
			dos.writeByte(kick.charAt(i) & 0xFF);
		}
		cachedKickPacket = bao.toByteArray();
	}
	
	private static void cacheRedirectPacket() throws IOException {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bao);
		if(redirect == null || redirect.length() == 0) {
			String message = "This is an EaglercraftX 1.8 server, it is not compatible with 1.5.2!";
			dos.writeByte(0xFF);
			dos.writeShort(message.length());
			for(int i = 0, l = message.length(), j; i < l; ++i) {
				j = message.charAt(i);
				dos.writeByte((j >> 8) & 0xFF);
				dos.writeByte(j & 0xFF);
			}
		}else {
			// Packet1Login
			dos.writeByte(0x01);
			dos.writeInt(0);
			dos.writeShort(0);
			dos.writeByte(0);
			dos.writeByte(0);
			dos.writeByte(0xFF);
			dos.writeByte(0);
			dos.writeByte(0);
			// Packet250CustomPayload
			dos.writeByte(0xFA);
			String channel = "EAG|Reconnect";
			int cl = channel.length();
			dos.writeShort(cl);
			for(int i = 0; i < cl; ++i) {
				dos.writeChar(channel.charAt(i));
			}
			byte[] redirect_ = redirect.getBytes(StandardCharsets.UTF_8);
			dos.writeShort(redirect_.length);
			dos.write(redirect_);
		}
		cachedLegacyKickRedirectPacket = bao.toByteArray();
	}
	
	private static void cacheIconPacket() throws IOException {
		File f = new File(icon);
		int[] iconPixels = ServerIconLoader.createServerIcon(f);
		if(iconPixels != null) {
			cachedIconPacket = new byte[16384];
			for(int i = 0, j; i < 4096; ++i) {
				j = i << 2;
				cachedIconPacket[j] = (byte)((iconPixels[i] >> 16) & 0xFF);
				cachedIconPacket[j + 1] = (byte)((iconPixels[i] >> 8) & 0xFF);
				cachedIconPacket[j + 2] = (byte)(iconPixels[i] & 0xFF);
				cachedIconPacket[j + 3] = (byte)((iconPixels[i] >> 24) & 0xFF);
			}
		}else {
			System.err.println("Could not load server icon \"" + f.getAbsolutePath() + "\"!");
		}
	}
}
