package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.query;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.query.EaglerQuerySimpleHandler;
import net.md_5.bungee.api.ProxyServer;

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
public class VersionQueryHandler extends EaglerQuerySimpleHandler {

	@Override
	protected void begin(String queryType) {
		JsonObject responseObj = new JsonObject();
		JsonArray handshakeVersions = new JsonArray();
		handshakeVersions.add(2);
		handshakeVersions.add(3);
		responseObj.add("handshakeVersions", handshakeVersions);
		JsonArray protocolVersions = new JsonArray();
		protocolVersions.add(47);
		responseObj.add("protocolVersions", protocolVersions);
		JsonArray gameVersions = new JsonArray();
		gameVersions.add("1.8");
		responseObj.add("gameVersions", gameVersions);
		JsonObject proxyInfo = new JsonObject();
		proxyInfo.addProperty("brand", ProxyServer.getInstance().getName());
		proxyInfo.addProperty("vers", ProxyServer.getInstance().getVersion());
		responseObj.add("proxyVersions", proxyInfo);
		sendJsonResponseAndClose("version", responseObj);
	}

}
