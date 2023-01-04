package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.query;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.google.gson.JsonObject;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerBungeeConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.HttpServerQueryHandler;
import net.md_5.bungee.api.plugin.PluginDescription;

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
public class QueryManager {

	private static final Map<String, Class<? extends HttpServerQueryHandler>> queryTypes = new HashMap();

	static {
		queryTypes.put("motd", MOTDQueryHandler.class);
		queryTypes.put("motd.cache", MOTDQueryHandler.class);
		queryTypes.put("version", VersionQueryHandler.class);
	}

	public static HttpServerQueryHandler createQueryHandler(String type) {
		Class<? extends HttpServerQueryHandler> clazz;
		synchronized(queryTypes) {
			clazz = queryTypes.get(type);
		}
		if(clazz != null) {
			HttpServerQueryHandler obj = null;
			try {
				obj = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				EaglerXBungee.logger().log(Level.SEVERE, "Exception creating query handler for '" + type + "'!", e);
			}
			if(obj != null) {
				return obj;
			}
		}
		return null;
	}

	public static void registerQueryType(String name, Class<? extends HttpServerQueryHandler> clazz) {
		synchronized(queryTypes) {
			if(queryTypes.put(name, clazz) != null) {
				EaglerXBungee.logger().warning("Query type '" + name + "' was registered twice, probably by two different plugins!");
				Thread.dumpStack();
			}
		}
	}

	public static void unregisterQueryType(String name) {
		synchronized(queryTypes) {
			queryTypes.remove(name);
		}
	}

	private static JsonObject createBaseResponse() {
		EaglerXBungee plugin = EaglerXBungee.getEagler();
		EaglerBungeeConfig conf = plugin.getConfig();
		JsonObject json = new JsonObject();
		json.addProperty("name", conf.getServerName());
		json.addProperty("brand", "lax1dude");
		PluginDescription desc = plugin.getDescription();
		json.addProperty("vers", "EaglerXBungee/" + desc.getVersion());
		json.addProperty("cracked", conf.isCracked());
		json.addProperty("secure", false);
		json.addProperty("time", System.currentTimeMillis());
		json.addProperty("uuid", conf.getServerUUID().toString());
		return json;
	}

	public static JsonObject createStringResponse(String type, String str) {
		JsonObject ret = createBaseResponse();
		ret.addProperty("type", type);
		ret.addProperty("data", str);
		return ret;
	}

	public static JsonObject createJsonObjectResponse(String type, JsonObject json) {
		JsonObject ret = createBaseResponse();
		ret.addProperty("type", type);
		ret.add("data", json);
		return ret;
	}

}
