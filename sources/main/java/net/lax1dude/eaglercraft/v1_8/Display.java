package net.lax1dude.eaglercraft.v1_8;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformInput;

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

}
