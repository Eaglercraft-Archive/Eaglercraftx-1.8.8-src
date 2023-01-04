package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.query;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.HttpServerQueryHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.query.QueryManager;

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
public abstract class EaglerQueryHandler extends HttpServerQueryHandler {

	public static void registerQueryType(String name, Class<? extends EaglerQueryHandler> clazz) {
		QueryManager.registerQueryType(name, clazz);
	}

	public static void unregisterQueryType(String name) {
		QueryManager.unregisterQueryType(name);
	}

}
