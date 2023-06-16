package net.lax1dude.eaglercraft.v1_8.internal;

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
public enum EnumPlatformAgent {
	DESKTOP("LWJGL3"), CHROME("Chrome"), EDGE("Edge"), IE("IE"),
	FIREFOX("Firefox"), SAFARI("Safari"), OPERA("Opera"), WEBKIT("WebKit"),
	GECKO("Gecko"), UNKNOWN("Unknown");
	
	private final String name;
	
	private EnumPlatformAgent(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
	public static EnumPlatformAgent getFromUA(String ua) {
		ua = " " + ua.toLowerCase();
		if(ua.contains(" edg/")) {
			return EDGE;
		}else if(ua.contains(" opr/")) {
			return OPERA;
		}else if(ua.contains(" chrome/")) {
			return CHROME;
		}else if(ua.contains(" firefox/")) {
			return FIREFOX;
		}else if(ua.contains(" safari/")) {
			return SAFARI;
		}else if(ua.contains(" trident/") || ua.contains(" msie")) {
			return IE;
		}else if(ua.contains(" webkit/")) {
			return WEBKIT;
		}else if(ua.contains(" gecko/")) {
			return GECKO;
		}else if(ua.contains(" desktop/")) {
			return DESKTOP;
		}else {
			return UNKNOWN;
		}
	}
	
}
