package net.lax1dude.eaglercraft.v1_8.internal;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

/**
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class PlatformInput {
	
	private static long win = 0l;

	private static long cursorDefault = 0l;
	private static long cursorHand = 0l;
	private static long cursorText = 0l;
	
	private static boolean windowFocused = true;
	private static boolean windowResized = true;
	private static boolean windowResized2 = true;
	
	private static boolean windowCursorEntered = true;
	private static boolean windowMouseGrabbed = false;
	private static int cursorX = 0;
	private static int cursorY = 0;
	private static int cursorDX = 0;
	private static int cursorDY = 0;
	private static int DWheel = 0;
	
	private static int windowWidth = 640;
	private static int windowHeight = 480;
	
	private static final List<KeyboardEvent> keyboardEventList = new LinkedList<>();
	private static KeyboardEvent currentKeyboardEvent = null;
	
	private static final char[] keyboardReleaseEventChars = new char[256];
	
	private static boolean enableRepeatEvents = false;
	private static int functionKeyModifier = GLFW_KEY_F;

	public static boolean lockKeys = false;

	private static final List<Character> keyboardCharList = new LinkedList<>();

	private static boolean vsync = true;
	private static boolean glfwVSyncState = false;

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

	private static final List<MouseEvent> mouseEventList = new LinkedList<>();
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

	private static final List<Gamepad> gamepadList = new ArrayList<>();
	private static int selectedGamepad = -1;
	private static String selectedGamepadName = null;
	private static String selectedGamepadUUID = null;
	private static final boolean[] gamepadButtonStates = new boolean[24];
	private static final float[] gamepadAxisStates = new float[4];
	
	private static class Gamepad {
		
		protected final int gamepadId;
		protected final String gamepadName;
		protected final String gamepadUUID;
		
		protected Gamepad(int gamepadId, String gamepadName, String gamepadUUID) {
			this.gamepadId = gamepadId;
			this.gamepadName = gamepadName;
			this.gamepadUUID = gamepadUUID;
		}

		@Override
		public int hashCode() {
			return Objects.hash(gamepadId, gamepadName, gamepadUUID);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Gamepad other = (Gamepad) obj;
			return gamepadId == other.gamepadId && Objects.equals(gamepadName, other.gamepadName)
					&& Objects.equals(gamepadUUID, other.gamepadUUID);
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
		windowResized = true;
		windowResized2 = true;
		
		glfwSetFramebufferSizeCallback(glfwWindow, (window, width, height) -> {
			if(windowWidth != width || windowHeight != height) {
				windowWidth = width;
				windowHeight = height;
				windowResized = true;
				windowResized2 = true;
			}
		});
		
		windowFocused = true;
		
		glfwSetWindowFocusCallback(glfwWindow, (window, focused) -> {
			windowFocused = focused;
		});
		
		glfwSetKeyCallback(glfwWindow, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_F11 && action == GLFW_PRESS) {
				toggleFullscreen();
			}
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

		cursorDefault = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		cursorHand = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
		cursorText = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
		glfwSetCursor(glfwWindow, cursorDefault);

		if(!fullscreen && startupFullscreen) {
			toggleFullscreen();
		}
		
		gamepadEnumerateDevices();
		
		glfwSetJoystickCallback((jid, event) -> {
			if(event == GLFW_DISCONNECTED && jid == selectedGamepad) {
				selectedGamepad = -1;
			}
			gamepadEnumerateDevices();
		});
	}
	
	public static int getWindowWidth() {
		return windowWidth;
	}
	
	public static int getWindowHeight() {
		return windowHeight;
	}

	public static int getVisualViewportX() {
		return 0;
	}

	public static int getVisualViewportY() {
		return 0;
	}

	public static int getVisualViewportW() {
		return windowWidth;
	}

	public static int getVisualViewportH() {
		return windowHeight;
	}

	public static boolean getWindowFocused() {
		return windowFocused;
	}

	public static boolean isCloseRequested() {
		return glfwWindowShouldClose(win);
	}

	public static void setVSync(boolean enable) {
		vsync = enable;
	}

	public static void update() {
		glfwPollEvents();
		if(vsync != glfwVSyncState) {
			glfwSwapInterval(vsync ? 1 : 0);
			glfwVSyncState = vsync;
		}
		glfwSwapBuffers(win);
	}

	public static boolean isVSyncSupported() {
		return true;
	}

	public static boolean wasResized() {
		boolean b = windowResized;
		windowResized = false;
		return b;
	}

	public static boolean wasVisualViewportResized() {
		boolean b = windowResized2;
		windowResized2 = false;
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

	public static void keyboardFireEvent(EnumFireKeyboardEvent eventType, int eagKey, char keyChar) {
		switch(eventType) {
		case KEY_DOWN:
			keyboardCharList.add(keyChar);
			keyboardEventList.add(new KeyboardEvent(eagKey, true, false));
			break;
		case KEY_UP:
			if(eagKey >= 0 && eagKey < keyboardReleaseEventChars.length) {
				keyboardReleaseEventChars[eagKey] = keyChar;
			}
			keyboardEventList.add(new KeyboardEvent(eagKey, false, false));
			break;
		case KEY_REPEAT:
			keyboardCharList.add(keyChar);
			keyboardEventList.add(new KeyboardEvent(eagKey, true, true));
			break;
		default:
			throw new UnsupportedOperationException();
		}
		if(keyboardEventList.size() > 64) {
			keyboardEventList.remove(0);
		}
		if(keyboardCharList.size() > 64) {
			keyboardCharList.remove(0);
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

	public static void mouseFireMoveEvent(EnumFireMouseEvent eventType, int posX, int posY) {
		if(eventType == EnumFireMouseEvent.MOUSE_MOVE) {
			mouseEventList.add(new MouseEvent(-1, false, posX, posY, 0.0f));
			if(mouseEventList.size() > 64) {
				mouseEventList.remove(0);
			}
		}else {
			throw new UnsupportedOperationException();
		}
	}

	public static void mouseFireButtonEvent(EnumFireMouseEvent eventType, int posX, int posY, int button) {
		switch(eventType) {
		case MOUSE_DOWN:
			mouseEventList.add(new MouseEvent(button, true, posX, posY, 0.0f));
			break;
		case MOUSE_UP:
			mouseEventList.add(new MouseEvent(button, false, posX, posY, 0.0f));
			break;
		default:
			throw new UnsupportedOperationException();
		}
		if(mouseEventList.size() > 64) {
			mouseEventList.remove(0);
		}
	}

	public static void mouseFireWheelEvent(EnumFireMouseEvent eventType, int posX, int posY, float wheel) {
		if(eventType == EnumFireMouseEvent.MOUSE_WHEEL) {
			mouseEventList.add(new MouseEvent(-1, false, posX, posY, wheel));
			if(mouseEventList.size() > 64) {
				mouseEventList.remove(0);
			}
		}else {
			throw new UnsupportedOperationException();
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

	public static boolean mouseGrabSupported() {
		return true;
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

	public static boolean supportsFullscreen() {
		return true;
	}

	private static boolean fullscreen = false;
	private static boolean startupFullscreen = false;
	private static int[] lastPos = new int[4];

	public static void toggleFullscreen() {
		long win = PlatformRuntime.getWindowHandle();
		long mon = getCurrentMonitor(win);
		GLFWVidMode mode = glfwGetVideoMode(mon);
		if (fullscreen) {
			glfwSetWindowMonitor(win, 0, lastPos[0], lastPos[1], lastPos[2], lastPos[3], mode.refreshRate());
		} else {
			int[] x = new int[1], y = new int[1];
			glfwGetWindowPos(win, x, y);
			lastPos[0] = x[0];
			lastPos[1] = y[0];
			glfwGetWindowSize(win, x, y);
			lastPos[2] = x[0];
			lastPos[3] = y[0];
			glfwSetWindowMonitor(win, mon, 0, 0, mode.width(), mode.height(), mode.refreshRate());
		}
		fullscreen = !fullscreen;
	}

	public static void setStartupFullscreen(boolean bool) {
		startupFullscreen = bool;
	}

	// https://stackoverflow.com/a/31526753
	private static long getCurrentMonitor(long window) {
		int nmonitors, i;
		int[] wx = new int[1], wy = new int[1], ww = new int[1], wh = new int[1];
		int[] mx = new int[1], my = new int[1], mw = new int[1], mh = new int[1];
		int overlap, bestoverlap = 0;
		long bestmonitor = 0;
		PointerBuffer monitors;
		GLFWVidMode mode;

		glfwGetWindowPos(window, wx, wy);
		glfwGetWindowSize(window, ww, wh);
		monitors = glfwGetMonitors();
		nmonitors = monitors.remaining();

		for (i = 0; i < nmonitors; ++i) {
			mode = glfwGetVideoMode(monitors.get(i));
			glfwGetMonitorPos(monitors.get(i), mx, my);
			mw[0] = mode.width();
			mh[0] = mode.height();

			overlap =
					Math.max(0, Math.min(wx[0] + ww[0], mx[0] + mw[0]) - Math.max(wx[0], mx[0])) *
							Math.max(0, Math.min(wy[0] + wh[0], my[0] + mh[0]) - Math.max(wy[0], my[0]));

			if (bestoverlap < overlap) {
				bestoverlap = overlap;
				bestmonitor = monitors.get(i);
			}
		}

		return bestmonitor;
	}

	public static boolean isFullscreen() {
		return fullscreen;
	}

	public static void showCursor(EnumCursorType cursor) {
		switch(cursor) {
		case DEFAULT:
		default:
			glfwSetCursor(win, cursorDefault);
			break;
		case HAND:
			glfwSetCursor(win, cursorHand);
			break;
		case TEXT:
			glfwSetCursor(win, cursorText);
			break;
		}
	}

	public static boolean touchNext() {
		return false;
	}

	public static EnumTouchEvent touchGetEventType() {
		return null;
	}

	public static int touchGetEventTouchPointCount() {
		return 0;
	}

	public static int touchGetEventTouchX(int pointId) {
		return 0;
	}

	public static int touchGetEventTouchY(int pointId) {
		return 0;
	}

	public static float touchGetEventTouchRadiusX(int pointId) {
		return 0.0f;
	}

	public static float touchGetEventTouchRadiusY(int pointId) {
		return 0.0f;
	}

	public static float touchGetEventTouchRadiusMixed(int pointId) {
		return touchGetEventTouchRadiusX(pointId) * 0.5f + touchGetEventTouchRadiusY(pointId) * 0.5f;
	}

	public static float touchGetEventTouchForce(int pointId) {
		return 0.0f;
	}

	public static int touchGetEventTouchPointUID(int pointId) {
		return 0;
	}

	public static int touchPointCount() {
		return 0;
	}

	public static int touchPointX(int pointId) {
		return 0;
	}

	public static int touchPointY(int pointId) {
		return 0;
	}

	public static float touchRadiusX(int pointId) {
		return 0.0f;
	}

	public static float touchRadiusY(int pointId) {
		return 0.0f;
	}

	public static float touchRadiusMixed(int pointId) {
		return touchRadiusX(pointId) * 0.5f + touchRadiusY(pointId) * 0.5f;
	}

	public static float touchForce(int pointId) {
		return 0.0f;
	}

	public static int touchPointUID(int pointId) {
		return 0;
	}

	public static void touchSetOpenKeyboardZone(int x, int y, int w, int h) {
		
	}

	public static void touchCloseDeviceKeyboard() {
		
	}

	public static boolean touchIsDeviceKeyboardOpenMAYBE() {
		return false;
	}

	public static String touchGetPastedString() {
		return null;
	}

	private static void gamepadEnumerateDevices() {
		if(selectedGamepad != -1 && !glfwJoystickIsGamepad(selectedGamepad)) {
			selectedGamepad = -1;
		}
		List<Gamepad> oldList = null;
		if(!gamepadList.isEmpty()) {
			oldList = new ArrayList<>(gamepadList);
			gamepadList.clear();
		}
		for(int i = GLFW_JOYSTICK_1; i <= GLFW_JOYSTICK_16; ++i) {
			if(glfwJoystickIsGamepad(i)) {
				gamepadList.add(new Gamepad(i, gamepadMakeName(i), glfwGetJoystickGUID(i)));
			}
		}
		vigg: if(selectedGamepad != -1) {
			for(int i = 0, l = gamepadList.size(); i < l; ++i) {
				Gamepad gp = gamepadList.get(i);
				if(gp.gamepadId == selectedGamepad && gp.gamepadUUID.equals(selectedGamepadUUID)) {
					break vigg;
				}
			}
			selectedGamepad = -1;
		}
		if(oldList == null) {
			if(!gamepadList.isEmpty()) {
				for(int i = 0, l = gamepadList.size(); i < l; ++i) {
					PlatformRuntime.logger.info("Found controller: {}", gamepadList.get(i).gamepadName);
				}
			}
		}else {
			if(gamepadList.isEmpty()) {
				for(int i = 0, l = oldList.size(); i < l; ++i) {
					PlatformRuntime.logger.info("Lost controller: {}", oldList.get(i).gamepadName);
				}
			}else {
				Set<String> oldGamepadUUIDs = new HashSet<>();
				for(int i = 0, l = oldList.size(); i < l; ++i) {
					oldGamepadUUIDs.add(oldList.get(i).gamepadUUID);
				}
				Set<String> newGamepadUUIDs = new HashSet<>();
				for(int i = 0, l = gamepadList.size(); i < l; ++i) {
					newGamepadUUIDs.add(gamepadList.get(i).gamepadUUID);
				}
				for(int i = 0, l = oldList.size(); i < l; ++i) {
					Gamepad g = oldList.get(i);
					if(!newGamepadUUIDs.contains(g.gamepadUUID)) {
						PlatformRuntime.logger.info("Lost controller: {}", g.gamepadName);
					}
				}
				for(int i = 0, l = gamepadList.size(); i < l; ++i) {
					Gamepad g = gamepadList.get(i);
					if(!oldGamepadUUIDs.contains(g.gamepadUUID)) {
						PlatformRuntime.logger.info("Found controller: {}", g.gamepadName);
					}
				}
			}
		}
		
	}

	private static String gamepadMakeName(int glfwId) {
		String s = glfwGetGamepadName(glfwId);
		if(s.endsWith(" (GLFW)")) {
			s = s.substring(0, s.length() - 7);
		}
		return glfwGetJoystickName(glfwId) + " (" + s + ")";
	}

	public static int gamepadGetValidDeviceCount() {
		return gamepadList.size();
	}

	public static String gamepadGetDeviceName(int deviceId) {
		if(deviceId >= 0 && deviceId < gamepadList.size()) {
			return gamepadList.get(deviceId).gamepadName;
		}else {
			return "Unknown";
		}
	}

	public static void gamepadSetSelectedDevice(int deviceId) {
		gamepadReset();
		if(deviceId >= 0 && deviceId < gamepadList.size()) {
			selectedGamepad = gamepadList.get(deviceId).gamepadId;
			if(!glfwJoystickIsGamepad(selectedGamepad)) {
				selectedGamepad = -1;
			}
		}else {
			selectedGamepad = -1;
		}
	}

	private static void gamepadReset() {
		for(int i = 0; i < gamepadButtonStates.length; ++i) {
			gamepadButtonStates[i] = false;
		}
		for(int i = 0; i < gamepadAxisStates.length; ++i) {
			gamepadAxisStates[i] = 0.0f;
		}
	}

	public static void gamepadUpdate() {
		gamepadReset();
		if(selectedGamepad != -1) {
			if(!glfwJoystickIsGamepad(selectedGamepad)) {
				selectedGamepad = -1;
				return;
			}
			try(MemoryStack ms = MemoryStack.stackPush()) {
				GLFWGamepadState state = GLFWGamepadState.calloc(ms);
				glfwGetGamepadState(selectedGamepad, state);
				java.nio.FloatBuffer axes = state.axes();
				axes.get(gamepadAxisStates);
				java.nio.ByteBuffer buttons = state.buttons();
				for(int i = 0, l = buttons.remaining(); i < l && i < gamepadButtonStates.length; ++i) {
					boolean v = buttons.get() != (byte)0;
					int j = GamepadConstants.getEaglerButtonFromGLFW(i);
					if(j != -1) {
						gamepadButtonStates[j] = v;
					}
				}
				gamepadButtonStates[GamepadConstants.GAMEPAD_LEFT_TRIGGER] = axes.get() > 0.4f;
				gamepadButtonStates[GamepadConstants.GAMEPAD_RIGHT_TRIGGER] = axes.get() > 0.4f;
			}
		}
	}

	public static boolean gamepadIsValid() {
		return selectedGamepad != -1;
	}

	public static String gamepadGetName() {
		return selectedGamepad != -1 ? selectedGamepadName : "Unknown";
	}

	public static boolean gamepadGetButtonState(int button) {
		return selectedGamepad != -1 && button >= 0 && button < gamepadButtonStates.length ? gamepadButtonStates[button] : false;
	}

	public static float gamepadGetAxis(int axis) {
		return selectedGamepad != -1 && axis >= 0 && axis < gamepadAxisStates.length ? gamepadAxisStates[axis] : 0.0f;
	}

	public static float getDPI() {
		float[] dpiX = new float[1];
		float[] dpiY = new float[1];
		glfwGetWindowContentScale(win, dpiX, dpiY);
		float ret = dpiX[0] * 0.5f + dpiY[0] * 0.5f;
		if(ret <= 0.0f) {
			ret = 1.0f;
		}
		return ret;
	}

}
