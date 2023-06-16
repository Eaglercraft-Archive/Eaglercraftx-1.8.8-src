package net.lax1dude.eaglercraft.v1_8.internal;

import static org.lwjgl.glfw.GLFW.*;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;

import net.lax1dude.eaglercraft.v1_8.EagUtils;

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
public class PlatformInput {
	
	private static long win = 0l;

	private static boolean windowFocused = true;
	private static boolean windowResized = true;
	
	private static boolean windowCursorEntered = true;
	private static boolean windowMouseGrabbed = false;
	private static int cursorX = 0;
	private static int cursorY = 0;
	private static int cursorDX = 0;
	private static int cursorDY = 0;
	private static int DWheel = 0;
	
	private static int windowWidth = 640;
	private static int windowHeight = 480;
	
	private static final List<KeyboardEvent> keyboardEventList = new LinkedList();
	private static KeyboardEvent currentKeyboardEvent = null;
	
	private static final char[] keyboardReleaseEventChars = new char[256];
	
	private static boolean enableRepeatEvents = false;
	private static int functionKeyModifier = GLFW_KEY_F;

	public static boolean lockKeys = false;

	private static final List<Character> keyboardCharList = new LinkedList();
	
	private static class KeyboardEvent {
		
		protected final int key;
		protected final boolean pressed;
		protected final boolean repeating;
		protected char resolvedCharacter = '\0';
		
		protected KeyboardEvent(int key, boolean pressed, boolean repeating) {
			this.key = key;
			this.pressed = pressed;
			this.repeating = repeating;
		}
		
	}
	
	private static final List<MouseEvent> mouseEventList = new LinkedList();
	private static MouseEvent currentMouseEvent = null;
	
	private static class MouseEvent {
		
		protected final int button;
		protected final boolean pressed;
		protected final int posX;
		protected final int posY;
		protected final float wheel;
		
		protected MouseEvent(int button, boolean pressed, int posX, int posY, float wheel) {
			this.button = button;
			this.pressed = pressed;
			this.posX = posX;
			this.posY = posY;
			this.wheel = wheel;
		}
		
	}

	static void initHooks(long glfwWindow) {
		win = glfwWindow;
		
		glfwSetErrorCallback((arg0, arg1) -> {
			String errorString = "<null>";
			if(arg1 != 0l) {
				try(MemoryStack stack = MemoryStack.stackPush()) {
					PointerBuffer pbuffer = stack.mallocPointer(1);
					pbuffer.put(0, arg1);
					errorString = pbuffer.getStringUTF8(0);
				}
			}
			PlatformRuntime.logger.error("GLFW Error #{}: {}", arg0, errorString);
		});
		
		if(!glfwRawMouseMotionSupported()) {
			throw new UnsupportedOperationException("Raw mouse movement (cursor lock) is not supported!");
		}
		
		int[] v1 = new int[1], v2 = new int[1];
		glfwGetFramebufferSize(glfwWindow, v1, v2);
		
		windowWidth = v1[0];
		windowHeight = v2[0];
		
		glfwSetFramebufferSizeCallback(glfwWindow, (window, width, height) -> {
			windowWidth = width;
			windowHeight = height;
			windowResized = true;
		});
		
		glfwSetWindowFocusCallback(glfwWindow, (window, focused) -> {
			windowFocused = focused;
		});
		
		glfwSetKeyCallback(glfwWindow, (window, key, scancode, action, mods) -> {
			if(glfwGetKey(glfwWindow, functionKeyModifier) == GLFW_PRESS) {
				if(key >= GLFW_KEY_1 && key <= GLFW_KEY_9) {
					key = key - GLFW_KEY_1 + GLFW_KEY_F1;
				}
			}
			key = KeyboardConstants.getEaglerKeyFromGLFW(key);
			keyboardEventList.add(new KeyboardEvent(key, action != GLFW_RELEASE, action == GLFW_REPEAT));
			if(keyboardEventList.size() > 64) {
				keyboardEventList.remove(0);
			}
		});
		
		glfwSetCharCallback(glfwWindow, (window, character) -> {
			keyboardCharList.add(Character.valueOf((char)character));
			if(keyboardCharList.size() > 64) {
				keyboardCharList.remove(0);
			}
		});
		
		glfwSetCursorPosCallback(glfwWindow, (window, posX, posY) -> {
			posY = windowHeight - posY;
			if(windowMouseGrabbed) {
				cursorDX -= (cursorX - (int)posX);
				cursorDY -= (cursorY - (int)posY);
				cursorX = (int)posX;
				cursorY = (int)posY;
			}else {
				cursorX = (int)posX;
				cursorY = (int)posY;
				mouseEventList.add(new MouseEvent(-1, false, cursorX, cursorY, 0.0f));
				if(mouseEventList.size() > 64) {
					mouseEventList.remove(0);
				}
			}
		});
		
		glfwSetMouseButtonCallback(glfwWindow, (window, button, action, mods) -> {
			mouseEventList.add(new MouseEvent(button, action != GLFW_RELEASE, cursorX, cursorY, 0.0f));
			if(mouseEventList.size() > 64) {
				mouseEventList.remove(0);
			}
		});
		
		glfwSetCursorEnterCallback(glfwWindow, (window, enter) -> {
			windowCursorEntered = enter;
		});
		
		glfwSetScrollCallback(glfwWindow, (window, scrollX, scrollY) -> {
			DWheel += (int)scrollY;
			mouseEventList.add(new MouseEvent(-1, false, cursorX, cursorY, (float)scrollY));
			if(mouseEventList.size() > 64) {
				mouseEventList.remove(0);
			}
		});
		
	}
	
	public static int getWindowWidth() {
		return windowWidth;
	}
	
	public static int getWindowHeight() {
		return windowHeight;
	}

	public static boolean getWindowFocused() {
		return windowFocused;
	}

	public static boolean isCloseRequested() {
		return glfwWindowShouldClose(win);
	}

	public static void update() {
		glfwPollEvents();
		glfwSwapBuffers(win);
	}

	public static boolean wasResized() {
		boolean b = windowResized;
		windowResized = false;
		return b;
	}

	public static boolean keyboardNext() {
		if(keyboardEventList.size() > 0) {
			currentKeyboardEvent = keyboardEventList.remove(0);
			if(currentKeyboardEvent.resolvedCharacter == '\0' && KeyboardConstants
					.getKeyCharFromEagler(currentKeyboardEvent.key) != '\0') {
				if(currentKeyboardEvent.pressed && keyboardCharList.size() > 0) {
					currentKeyboardEvent.resolvedCharacter = keyboardCharList.remove(0);
					keyboardReleaseEventChars[currentKeyboardEvent.key] = 
							currentKeyboardEvent.resolvedCharacter;
				}else if(!currentKeyboardEvent.pressed) {
					currentKeyboardEvent.resolvedCharacter = 
							keyboardReleaseEventChars[currentKeyboardEvent.key];
					keyboardReleaseEventChars[currentKeyboardEvent.key] = '\0';
				}
			}
			if(currentKeyboardEvent.repeating && !enableRepeatEvents) {
				return keyboardNext();
			}else {
				return true;
			}
		}else {
			if(keyboardCharList.size() > 0) {
				currentKeyboardEvent = new KeyboardEvent(KeyboardConstants.KEY_SPACE, true, false);
				currentKeyboardEvent.resolvedCharacter = keyboardCharList.remove(0);
				KeyboardEvent releaseEvent = new KeyboardEvent(KeyboardConstants.KEY_SPACE, false, false);
				releaseEvent.resolvedCharacter = currentKeyboardEvent.resolvedCharacter;
				keyboardEventList.add(releaseEvent);
				return true;
			}else {
				return false;
			}
		}
	}

	public static boolean keyboardGetEventKeyState() {
		return currentKeyboardEvent.pressed;
	}

	public static int keyboardGetEventKey() {
		return currentKeyboardEvent.key;
	}

	public static char keyboardGetEventCharacter() {
		return currentKeyboardEvent.resolvedCharacter;
	}

	public static boolean keyboardIsKeyDown(int key) {
		if(glfwGetKey(win, functionKeyModifier) == GLFW_PRESS) {
			if(key >= GLFW_KEY_1 && key <= GLFW_KEY_9) {
				return false;
			}
			if(key >= GLFW_KEY_F1 && key <= GLFW_KEY_F9) {
				key = key - GLFW_KEY_F1 + GLFW_KEY_1;
			}
		}
		return glfwGetKey(win, KeyboardConstants.getGLFWKeyFromEagler(key)) == GLFW_PRESS;
	}

	public static boolean keyboardIsRepeatEvent() {
		return currentKeyboardEvent.repeating;
	}

	public static void keyboardEnableRepeatEvents(boolean b) {
		enableRepeatEvents = b;
	}

	public static boolean mouseNext() {
		if(mouseEventList.size() > 0) {
			currentMouseEvent = mouseEventList.remove(0);
			return true;
		}else {
			return false;
		}
	}

	public static boolean mouseGetEventButtonState() {
		return currentMouseEvent.pressed;
	}

	public static int mouseGetEventButton() {
		return currentMouseEvent.button;
	}

	public static int mouseGetEventX() {
		return currentMouseEvent.posX;
	}

	public static int mouseGetEventY() {
		return currentMouseEvent.posY;
	}

	public static int mouseGetEventDWheel() {
		return (int)currentMouseEvent.wheel;
	}

	public static int mouseGetX() {
		return cursorX;
	}

	public static int mouseGetY() {
		return cursorY;
	}

	public static boolean mouseIsButtonDown(int i) {
		return glfwGetMouseButton(win, i) == GLFW_PRESS;
	}

	public static int mouseGetDWheel() {
		int i = DWheel;
		DWheel = 0;
		return i;
	}

	public static void mouseSetGrabbed(boolean grab) {
		if(grab != windowMouseGrabbed) {
			cursorX = windowWidth / 2;
			cursorY = windowHeight / 2;
			glfwSetCursorPos(win, cursorX, cursorY);
			windowMouseGrabbed = grab;
			cursorDX = 0;
			cursorDY = 0;
			glfwSetInputMode(win, GLFW_CURSOR, grab ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
			glfwSetInputMode(win, GLFW_RAW_MOUSE_MOTION, grab ? GLFW_TRUE : GLFW_FALSE);
		}
	}

	public static boolean isPointerLocked() {
		return windowMouseGrabbed;
	}

	public static boolean isMouseGrabbed() {
		return windowMouseGrabbed;
	}

	public static int mouseGetDX() {
		int i = cursorDX;
		cursorDX = 0;
		return i;
	}

	public static int mouseGetDY() {
		int i = cursorDY;
		cursorDY = 0;
		return i;
	}

	public static void mouseSetCursorPosition(int x, int y) {
		cursorX = x;
		cursorY = y;
		glfwSetCursorPos(win, x, y);
	}

	public static boolean mouseIsInsideWindow() {
		return windowCursorEntered;
	}
	
	public static boolean contextLost() {
		return glfwGetWindowAttrib(win, GLFW_ICONIFIED) == GLFW_TRUE;
	}

	public static void setFunctionKeyModifier(int key) {
		functionKeyModifier = KeyboardConstants.getGLFWKeyFromEagler(key);
	}

	public static void toggleFullscreen() {
		//
	}

	public static boolean isFullscreen() {
		return false;
	}
	
}
