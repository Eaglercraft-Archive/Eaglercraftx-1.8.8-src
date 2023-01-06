package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins;

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
public class SimpleRateLimiter {

	private long timer;
	private int count;

	public SimpleRateLimiter() {
		timer = System.currentTimeMillis();
		count = 0;
	}

	public boolean rateLimit(int maxPerMinute) {
		int t = 60000 / maxPerMinute;
		long millis = System.currentTimeMillis();
		int decr = (int)(millis - timer) / t;
		if(decr > 0) {
			timer += decr * t;
			count -= decr;
			if(count < 0) {
				count = 0;
			}
		}
		if(count >= maxPerMinute) {
			return false;
		}else {
			++count;
			return true;
		}
	}

}
