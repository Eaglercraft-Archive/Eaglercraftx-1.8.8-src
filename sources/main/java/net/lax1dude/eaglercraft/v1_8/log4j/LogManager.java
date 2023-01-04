package net.lax1dude.eaglercraft.v1_8.log4j;

import java.util.HashMap;
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
public class LogManager {
	
	private static final Map<String,Logger> loggerInstances = new HashMap();
	
	public static final Object logLock = new Object();
	public static Level logLevel = Level.DEBUG;
	
	public static Logger getLogger() {
		return getLogger("Minecraft");
	}
	
	public static Logger getLogger(String name) {
		Logger ret;
		synchronized(loggerInstances) {
			ret = loggerInstances.get(name);
			if(ret == null) {
				ret = new Logger(name);
			}
		}
		return ret;
	}
	
	public static void setLevel(Level lv) {
		logLevel = lv;
	}

}
