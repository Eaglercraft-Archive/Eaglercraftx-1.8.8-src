package net.lax1dude.eaglercraft.v1_8;

import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;
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
public class Keyboard {
	
	public static void enableRepeatEvents(boolean b) {
		PlatformInput.keyboardEnableRepeatEvents(b);
	}

	public static boolean isCreated() {
		return true;
	}

	public static boolean next() {
		return PlatformInput.keyboardNext();
	}

	public static boolean getEventKeyState() {
		return PlatformInput.keyboardGetEventKeyState();
	}

	public static char getEventCharacter() {
		return PlatformInput.keyboardGetEventCharacter();
	}

	public static int getEventKey() {
		return PlatformInput.keyboardGetEventKey();
	}

	public static void setFunctionKeyModifier(int key) {
		PlatformInput.setFunctionKeyModifier(key);
	}

	public static boolean isKeyDown(int key) {
		return PlatformInput.keyboardIsKeyDown(key);
	}

	public static String getKeyName(int key) {
		return KeyboardConstants.getKeyName(key);
	}

	public static boolean isRepeatEvent() {
		return PlatformInput.keyboardIsRepeatEvent();
	}

}
