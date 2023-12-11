package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.shit;

import java.util.logging.Logger;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.md_5.bungee.api.ProxyServer;

/**
 * Copyright (c) 2023 LAX1DUDE. All Rights Reserved.
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
public class CompatWarning {

	public static void displayCompatWarning() {
		String stfu = System.getProperty("eaglerxbungee.stfu");
		if("true".equalsIgnoreCase(stfu)) {
			return;
		}
		String[] compatWarnings = new String[] {
				":>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>",
				":>  ",
				":>      EAGLERCRAFTXBUNGEE WARNING:",
				":>  ",
				":>  This plugin wasn\'t tested to be \'working\'",
				":>  with ANY version of BungeeCord (and forks)",
				":>  apart from the versions listed below:",
				":>  ",
				":>  - BungeeCord: " + EaglerXBungee.NATIVE_BUNGEECORD_BUILD,
				":>  - Waterfall: " + EaglerXBungee.NATIVE_WATERFALL_BUILD,
				":>  - FlameCord: " + EaglerXBungee.NATIVE_FLAMECORD_BUILD,
				":>  ",
				":>  This is not a Bukkit/Spigot plugin!",
				":>  ",
				":>  Use \"-Deaglerxbungee.stfu=true\" to hide",
				":>  ",
				":>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>:>"
		};
		
		try {
			Logger fuck = ProxyServer.getInstance().getLogger();
			for(int i = 0; i < compatWarnings.length; ++i) {
				fuck.warning(compatWarnings[i]);
			}
		}catch(Throwable t) {
			for(int i = 0; i < compatWarnings.length; ++i) {
				System.err.println(compatWarnings[i]);
			}
		}
	}

}
