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
public class Mouse {

	public static int getEventDWheel() {
		return PlatformInput.mouseGetEventDWheel();
	}

	public static int getX() {
		return PlatformInput.mouseGetX();
	}

	public static int getY() {
		return PlatformInput.mouseGetY();
	}

	public static boolean getEventButtonState() {
		return PlatformInput.mouseGetEventButtonState();
	}

	public static boolean isCreated() {
		return true;
	}

	public static boolean next() {
		return PlatformInput.mouseNext();
	}

	public static int getEventX() {
		return PlatformInput.mouseGetEventX();
	}

	public static int getEventY() {
		return PlatformInput.mouseGetEventY();
	}

	public static int getEventButton() {
		return PlatformInput.mouseGetEventButton();
	}

	public static boolean isButtonDown(int i) {
		return PlatformInput.mouseIsButtonDown(i);
	}

	public static int getDWheel() {
		return PlatformInput.mouseGetDWheel();
	}

	public static void setGrabbed(boolean grab) {
		PlatformInput.mouseSetGrabbed(grab);
	}

	public static int getDX() {
		return PlatformInput.mouseGetDX();
	}

	public static int getDY() {
		return PlatformInput.mouseGetDY();
	}

	public static void setCursorPosition(int x, int y) {
		PlatformInput.mouseSetCursorPosition(x, y);
	}

	public static boolean isInsideWindow() {
		return PlatformInput.mouseIsInsideWindow();
	}

	public static boolean isActuallyGrabbed() {
		return PlatformInput.isPointerLocked();
	}

	public static boolean isMouseGrabbed() {
		return PlatformInput.isMouseGrabbed();
	}

}
