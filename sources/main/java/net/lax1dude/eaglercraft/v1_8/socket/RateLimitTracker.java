package net.lax1dude.eaglercraft.v1_8.socket;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
public class RateLimitTracker {

	private static long lastTickUpdate = 0l;

	private static final Map<String, Long> blocks = new HashMap();
	private static final Map<String, Long> lockout = new HashMap();

	public static boolean isLockedOut(String addr) {
		Long lockoutStatus = lockout.get(addr);
		return lockoutStatus != null && System.currentTimeMillis() - lockoutStatus.longValue() < 300000l;
	}

	public static boolean isProbablyLockedOut(String addr) {
		return blocks.containsKey(addr) || lockout.containsKey(addr);
	}

	public static void registerBlock(String addr) {
		blocks.put(addr, System.currentTimeMillis());
	}

	public static void registerLockOut(String addr) {
		long millis = System.currentTimeMillis();
		blocks.put(addr, millis);
		lockout.put(addr, millis);
	}

	public static void tick() {
		long millis = System.currentTimeMillis();
		if(millis - lastTickUpdate > 5000l) {
			lastTickUpdate = millis;
			Iterator<Long> blocksItr = blocks.values().iterator();
			while(blocksItr.hasNext()) {
				if(millis - blocksItr.next().longValue() > 900000l) {
					blocksItr.remove();
				}
			}
			blocksItr = lockout.values().iterator();
			while(blocksItr.hasNext()) {
				if(millis - blocksItr.next().longValue() > 900000l) {
					blocksItr.remove();
				}
			}
		}
	}

}
