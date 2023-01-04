package net.lax1dude.eaglercraft.v1_8;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformInput;

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
public class Display {

	private static long lastSwap = 0l;

	public static int getWidth() {
		return PlatformInput.getWindowWidth();
	}
	
	public static int getHeight() {
		return PlatformInput.getWindowHeight();
	}

	public static boolean isActive() {
		return PlatformInput.getWindowFocused();
	}

	public static void create() {
		
	}

	public static void setTitle(String string) {
		
	}

	public static boolean isCloseRequested() {
		return PlatformInput.isCloseRequested();
	}

	public static void update() {
		PlatformInput.update();
	}

	public static void sync(int limitFramerate) {
		boolean limitFPS = limitFramerate > 0 && limitFramerate < 1000;
		
		if(limitFPS) {
			long millis = System.currentTimeMillis();
			long frameMillis = (1000l / limitFramerate) - (millis - lastSwap);
			if(frameMillis > 0l) {
				EagUtils.sleep(frameMillis);
			}
		}
		
		lastSwap = System.currentTimeMillis();
	}

	public static boolean contextLost() {
		return PlatformInput.contextLost();
	}
	
	public static boolean wasResized() {
		return PlatformInput.wasResized();
	}

	public static boolean isFullscreen() {
		return PlatformInput.isFullscreen();
	}

	public static void toggleFullscreen() {
		PlatformInput.toggleFullscreen();
	}

}
