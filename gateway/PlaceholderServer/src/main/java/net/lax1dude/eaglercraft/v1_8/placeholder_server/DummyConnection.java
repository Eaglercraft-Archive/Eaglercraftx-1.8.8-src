package net.lax1dude.eaglercraft.v1_8.placeholder_server;

import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
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
public class DummyConnection {

	public final long age;
	public long sendRedirectAt = 0l;
	public final WebSocket sock;

	private boolean hasFirstPacket = false;

	public DummyConnection(WebSocket sock) {
		this.age = System.currentTimeMillis();
		this.sock = sock;
	}

	public void processString(String str) {
		if(!hasFirstPacket) {
			hasFirstPacket = true;
			str = str.toLowerCase();
			if(str.startsWith("accept:")) {
				str = str.substring(7).trim();
				if(str.startsWith("motd")) {
					String subType = "motd";
					int i = str.indexOf('.');
					if(i > 0 && i != str.length() - 1) {
						subType = str.substring(i + 1);
					}
					JSONObject json = new JSONObject();
					json.put("name", PlaceholderServerConfig.serverName);
					json.put("brand", "lax1dude");
					json.put("vers", "EaglerXPlaceholder/1.0");
					json.put("cracked", true);
					json.put("secure", false);
					json.put("time", System.currentTimeMillis());
					json.put("uuid", PlaceholderServerConfig.serverUUID);
					json.put("type", str);
					JSONObject motdData = new JSONObject();
					boolean icon = false;
					if(subType.startsWith("cache.anim")) {
						motdData.put("unsupported", true);
					}else {
						if(subType.startsWith("cache")) {
							JSONArray arr = new JSONArray();
							arr.put("animation");
							arr.put("results");
							arr.put("trending");
							arr.put("portfolio");
							motdData.put("cache", arr);
							motdData.put("ttl", 7200);
						}else {
							motdData.put("cache", true);
						}
						JSONArray motdLines = new JSONArray();
						String motd1 = PlaceholderServerConfig.motd1;
						String motd2 = PlaceholderServerConfig.motd2;
						if(motd1 != null && motd1.length() > 0) motdLines.put(motd1);
						if(motd2 != null && motd2.length() > 0) motdLines.put(motd2);
						motdData.put("motd", motdLines);
						icon = PlaceholderServerConfig.cachedIconPacket != null && !subType.startsWith("noicon")
								&& !subType.startsWith("cache.noicon");
						motdData.put("icon", icon);
						motdData.put("online", 0);
						motdData.put("max", 0);
						motdData.put("players", new JSONArray());
					}
					json.put("data", motdData);
					sock.send(json.toString());
					if(icon) {
						sock.send(PlaceholderServerConfig.cachedIconPacket);
					}
				}
			}
			sock.close();
		}
	}

	public void processBinary(ByteBuffer bin) {
		if(!hasFirstPacket) {
			hasFirstPacket = true;
			if(bin.remaining() > 2) {
				if(bin.get(0) == (byte)2 && bin.get(1) == (byte)69) {
					sock.send(PlaceholderServerConfig.cachedLegacyKickRedirectPacket);
					sendRedirectAt = System.currentTimeMillis();
					return;
				}else if(bin.get(0) == (byte)1) {
					sock.send(PlaceholderServerConfig.cachedKickPacket);
				}
			}
			sock.close();
		}
	}

}
