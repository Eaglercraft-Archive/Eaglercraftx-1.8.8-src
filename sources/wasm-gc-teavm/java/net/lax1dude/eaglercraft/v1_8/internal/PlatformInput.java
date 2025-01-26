package net.lax1dude.eaglercraft.v1_8.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.teavm.interop.Import;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.browser.Navigator;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArrayReader;
import org.teavm.jso.core.JSString;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.gamepad.Gamepad;
import org.teavm.jso.gamepad.GamepadButton;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.BetterJSStringConverter;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.LegacyKeycodeTranslator;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.SortedTouchEvent;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.WASMGCClientConfigAdapter;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.TeaVMUtils;
import net.lax1dude.eaglercraft.v1_8.internal.wasm_gc_teavm.WebGLBackBuffer;

/**
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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

	private static Window win = null;
	private static HTMLElement parent = null;
	private static HTMLCanvasElement canvas = null;
	private static int touchOpenZoneX = 0;
	private static int touchOpenZoneY = 0;
	private static int touchOpenZoneW = 0;
	private static int touchOpenZoneH = 0;

	private static int windowWidth = -1;
	private static int windowHeight = -1;
	private static float windowDPI = 1.0f;
	private static int visualViewportX = -1;
	private static int visualViewportY = -1;
	private static int visualViewportW = -1;
	private static int visualViewportH = -1;
	private static int lastWasResizedWindowWidth = -2;
	private static int lastWasResizedWindowHeight = -2;
	private static float lastWasResizedWindowDPI = -2.0f;
	private static int lastWasResizedVisualViewportX = -2;
	private static int lastWasResizedVisualViewportY = -2;
	private static int lastWasResizedVisualViewportW = -2;
	private static int lastWasResizedVisualViewportH = -2;

	private static final List<VMouseEvent> mouseEvents = new LinkedList<>();
	private static final List<VKeyEvent> keyEvents = new LinkedList<>();
	private static final List<SortedTouchEvent> touchEvents = new LinkedList<>();
	private static final List<String> pastedStrings = new LinkedList<>();
	private static VMouseEvent currentMouseEvent = null;
	private static VKeyEvent currentKeyEvent = null;
	private static SortedTouchEvent currentTouchEvent = null;
	private static boolean[] buttonStates = new boolean[8];
	private static boolean[] keyStates = new boolean[256];
	private static SortedTouchEvent currentTouchState = null;

	private static boolean gamepadSupported = false;
	private static final List<Gamepad> gamepadList = new ArrayList<>();
	private static Gamepad selectedGamepad = null;
	private static String selectedGamepadName = null;
	private static double gamepadTimestamp = -1.0;
	private static final boolean[] gamepadButtonStates = new boolean[24];
	private static final float[] gamepadAxisStates = new float[4];

	private static int functionKeyModifier = KeyboardConstants.KEY_F;

	private static int mouseX = 0;
	private static int mouseY = 0;
	private static float mouseDX = 0.0f;
	private static float mouseDY = 0.0f;
	private static float mouseDWheel = 0.0f;
	private static boolean enableRepeatEvents = true;
	private static boolean isWindowFocused = true;
	private static boolean isMouseOverWindow = true;
	private static boolean isMouseGrabSupported = false;
	static boolean unpressCTRL = false;

	private static boolean fullscreenSupported = false;

	static boolean useVisualViewport = false;

	public static boolean keyboardLockSupported = false;
	public static boolean lockKeys = false;

	static boolean vsync = true;
	static boolean vsyncSupport = false;

	private static Map<String, LegacyKeycodeTranslator.LegacyKeycode> keyCodeTranslatorMap = null;

	static void initContext(Window window, HTMLElement parent_, HTMLCanvasElement canvaz) {
		win = window;
		parent = parent_;
		canvas = canvaz;
		canvas.getStyle().setProperty("cursor", "default");
		isMouseGrabSupported = mouseGrabSupported0();
		fullscreenSupported = supportsFullscreen0();
		vsyncSupport = isVSyncSupported0();
		WASMGCClientConfigAdapter conf = (WASMGCClientConfigAdapter)PlatformRuntime.getClientConfigAdapter();
		useVisualViewport = conf.isUseVisualViewportTeaVM() && isVisualViewport0();
		lastWasResizedWindowWidth = -2;
		lastWasResizedWindowHeight = -2;
		lastWasResizedWindowDPI = -2.0f;
		lastWasResizedVisualViewportX = -2;
		lastWasResizedVisualViewportY = -2;
		lastWasResizedVisualViewportW = -2;
		lastWasResizedVisualViewportH = -2;

		PlatformRuntime.logger.info("Loading keyboard layout data");

		LegacyKeycodeTranslator keycodeTranslator = new LegacyKeycodeTranslator();
		if(checkKeyboardLayoutSupported()) {
			JSArrayReader<JSKeyboardLayoutEntry> arr = iterateKeyboardLayout();
			for(int i = 0, l = arr.getLength(); i < l; ++i) {
				JSKeyboardLayoutEntry etr = arr.get(i);
				keycodeTranslator.addBrowserLayoutMapping(etr.getKey(), etr.getValue());
			}
			int cnt = keycodeTranslator.getRemappedKeyCount();
			if(cnt > 0) {
				PlatformRuntime.logger.info("KeyboardLayoutMap remapped {} keys from their default codes", cnt);
			}
		}
		keyCodeTranslatorMap = keycodeTranslator.buildLayoutTable();
		
		gamepadSupported = isGamepadSupported();
	}

	private interface JSKeyboardLayoutEntry extends JSObject {

		@JSProperty
		String getKey();

		@JSProperty
		String getValue();

	}

	@Import(module = "platformInput", name = "keyboardLayoutSupported")
	private static native boolean checkKeyboardLayoutSupported();

	@Import(module = "platformInput", name = "iterateKeyboardLayout")
	private static native JSArrayReader<JSKeyboardLayoutEntry> iterateKeyboardLayout();

	private static final int EVENT_MOUSE_DOWN = 0;
	private static final int EVENT_MOUSE_UP = 1;
	private static final int EVENT_MOUSE_MOVE = 2;
	private static final int EVENT_MOUSE_WHEEL = 3;

	private static class VMouseEvent {

		private final int posX;
		private final int posY;
		private final int button;
		private final float wheel;
		private final int type;

		private VMouseEvent(int posX, int posY, int button, float wheel, int type) {
			this.posX = posX;
			this.posY = posY;
			this.button = button;
			this.wheel = wheel;
			this.type = type;
		}

	}

	private interface JSMouseEvent extends JSObject {

		@JSProperty
		int getEventType();

		@JSProperty
		int getPosX();

		@JSProperty
		int getPosY();

		@JSProperty
		int getMovementX();

		@JSProperty
		int getMovementY();

		@JSProperty
		int getButton();

		@JSProperty
		float getWheel();

	}

	private static final int EVENT_KEY_DOWN = 0;
	private static final int EVENT_KEY_UP = 1;
	private static final int EVENT_KEY_REPEAT = 2;

	private static final int EVENT_KEY_MOD_CONTROL = 1;
	private static final int EVENT_KEY_MOD_SHIFT = 2;
	private static final int EVENT_KEY_MOD_ALT = 4;

	private static class VKeyEvent {

		private final int keyCode;
		private final int location;
		private final int eagKey;
		private final char keyChar;
		private final int type;

		private VKeyEvent(int keyCode, int location, int eagKey, char keyChar, int type) {
			this.keyCode = keyCode;
			this.location = location;
			this.eagKey = eagKey;
			this.keyChar = keyChar;
			this.type = type;
		}

	}

	private interface JSKeyEvent extends JSObject {

		@JSProperty
		int getEventType();

		@JSProperty
		String getKeyCode();

		@JSProperty
		String getKeyName();

		@JSProperty
		int getLegacyCode();

		@JSProperty
		int getLocation();

		@JSProperty
		int getMods();

	}

	private static final int EVENT_TOUCH_START = 0;
	private static final int EVENT_TOUCH_MOVE = 1;
	private static final int EVENT_TOUCH_END = 2;

	private interface JSTouchEvent extends JSObject {

		@JSProperty
		int getEventType();

		@JSProperty
		JSArrayReader<SortedTouchEvent.JSTouchPoint> getChangedTouches();

		@JSProperty
		JSArrayReader<SortedTouchEvent.JSTouchPoint> getTargetTouches();

	}

	private static final int EVENT_TOUCH_KEYBOARD_ABSOLUTE = 0;
	private static final int EVENT_TOUCH_KEYBOARD_CODEPOINTS = 1;

	private interface JSTouchKeyboardEvent extends JSObject {

		@JSProperty
		int getEventType();

		@JSProperty
		int getAbsoluteCode();

		@JSProperty
		char getAbsoluteChar();

		@JSProperty
		JSString getCodepoints();

	}

	private static final int EVENT_RESIZE_WINDOW = 1;
	private static final int EVENT_RESIZE_VISUAL_VIEWPORT = 2;
	private static final int EVENT_RESIZE_DPI = 4;

	private interface JSWindowResizeEvent extends JSObject {

		@JSProperty
		int getEventTypeMask();

		@JSProperty
		int getWindowWidth();

		@JSProperty
		int getWindowHeight();

		@JSProperty
		int getVisualViewportX();

		@JSProperty
		int getVisualViewportY();

		@JSProperty
		int getVisualViewportW();

		@JSProperty
		int getVisualViewportH();

		@JSProperty
		float getWindowDPI();

	}

	private static final int EVENT_GAMEPAD_CONNECTED = 0;
	private static final int EVENT_GAMEPAD_DISCONNECTED = 1;

	private interface JSGamepadEvent extends JSObject {

		@JSProperty
		int getEventType();

		@JSProperty
		Gamepad getGamepad();

	}

	private static final BiMap<Integer,Integer> touchIDtoUID = HashBiMap.create();
	private static int touchUIDnum = 0;

	private static final SortedTouchEvent.ITouchUIDMapper touchUIDMapperCreate = (idx) -> {
		Integer ret = touchIDtoUID.get(idx);
		if(ret != null) return ret.intValue();
		int r = touchUIDnum++;
		touchIDtoUID.put(idx, r);
		return r;
	};

	private static final SortedTouchEvent.ITouchUIDMapper touchUIDMapper = (idx) -> {
		Integer ret = touchIDtoUID.get(idx);
		return ret != null ? ret.intValue() : -1;
	};

	private static final int EVENT_INPUT_MOUSE = 0;
	private static final int EVENT_INPUT_KEYBOARD = 1;
	private static final int EVENT_INPUT_TOUCH = 2;
	private static final int EVENT_INPUT_TOUCH_KEYBOARD = 3;
	private static final int EVENT_INPUT_TOUCH_PASTE = 4;
	private static final int EVENT_INPUT_FOCUS = 5;
	private static final int EVENT_INPUT_BLUR = 6;
	private static final int EVENT_INPUT_MOUSE_ENTER = 7;
	private static final int EVENT_INPUT_MOUSE_EXIT = 8;
	private static final int EVENT_INPUT_WINDOW_RESIZE = 9;
	private static final int EVENT_INPUT_GAMEPAD = 10;

	static void handleJSEvent(PlatformRuntime.JSEagRuntimeEvent evt) {
		switch(evt.getEventType() & 31) {
			case EVENT_INPUT_MOUSE: {
				JSMouseEvent obj = evt.getEventObj();
				int type = obj.getEventType();
				int posX = (int)(obj.getPosX() * windowDPI);
				int posY = windowHeight - (int)(obj.getPosY() * windowDPI) - 1;
				switch(type) {
					case EVENT_MOUSE_DOWN: {
						handleWindowFocus();
						int button = obj.getButton();
						button = button == 1 ? 2 : (button == 2 ? 1 : button);
						if(button >= 0 && button < buttonStates.length) {
							buttonStates[button] = true;
						}
						mouseEvents.add(new VMouseEvent(posX, posY, button, 0.0f, EVENT_MOUSE_DOWN));
						break;
					}
					case EVENT_MOUSE_UP: {
						int button = obj.getButton();
						button = button == 1 ? 2 : (button == 2 ? 1 : button);
						if(button >= 0 && button < buttonStates.length) {
							buttonStates[button] = false;
						}
						mouseEvents.add(new VMouseEvent(posX, posY, button, 0.0f, EVENT_MOUSE_UP));
						break;
					}
					case EVENT_MOUSE_MOVE: {
						if(isMouseGrabbed()) {
							mouseDX += obj.getMovementX();
							mouseDY -= obj.getMovementY();
						}
						mouseX = posX;
						mouseY = posY;
						mouseEvents.add(new VMouseEvent(posX, posY, -1, 0.0f, EVENT_MOUSE_MOVE));
						break;
					}
					case EVENT_MOUSE_WHEEL: {
						float wheel = -obj.getWheel();
						mouseDWheel += wheel;
						mouseEvents.add(new VMouseEvent(posX, posY, -1, wheel, EVENT_MOUSE_WHEEL));
						break;
					}
				}
				break;
			}
			case EVENT_INPUT_KEYBOARD: {
				JSKeyEvent obj = evt.getEventObj();
				int type = obj.getEventType();
				if(!enableRepeatEvents && type == EVENT_KEY_REPEAT) break;
				String keyCodeStr = obj.getKeyCode();
				LegacyKeycodeTranslator.LegacyKeycode keyCode = null;
				if(keyCodeTranslatorMap != null && keyCodeStr != null) {
					keyCode = keyCodeTranslatorMap.get(keyCodeStr);
				}
				int w;
				int loc;
				if(keyCode != null) {
					w = keyCode.keyCode;
					loc = keyCode.location;
				}else {
					w = obj.getLegacyCode();
					loc = obj.getLocation();
				}
				int ww = processFunctionKeys(w);
				int eag = KeyboardConstants.getEaglerKeyFromBrowser(ww, ww == w ? loc : 0);
				if(eag != 0) {
					keyStates[eag] = type != EVENT_KEY_UP;
					if(eag == functionKeyModifier) {
						for(int key = KeyboardConstants.KEY_F1; key <= KeyboardConstants.KEY_F10; ++key) {
							keyStates[key] = false;
						}
					}
				}
				char c;
				String s = obj.getKeyName();
				if(s == null || s.length() == 0) {
					c = keyToAsciiLegacy(w, (obj.getMods() & EVENT_KEY_MOD_SHIFT) != 0);
				}else {
					if(s.length() == 1) {
						c = s.charAt(0);
					}else if(s.equals("Unidentified")) {
						break;
					}else {
						c = '\0';
					}
				}
				keyEvents.add(new VKeyEvent(ww, loc, eag, c, type));
				break;
			}
			case EVENT_INPUT_TOUCH: {
				JSTouchEvent obj = evt.getEventObj();
				switch(obj.getEventType()) {
				case EVENT_TOUCH_START:
					handleWindowFocus();
					touchEvents.add(currentTouchState = SortedTouchEvent.createTouchEvent(EnumTouchEvent.TOUCHSTART, obj.getChangedTouches(),
							obj.getTargetTouches(), touchUIDMapperCreate, windowHeight, windowDPI));
					break;
				case EVENT_TOUCH_MOVE:
					touchEvents.add(currentTouchState = SortedTouchEvent.createTouchEvent(EnumTouchEvent.TOUCHMOVE, obj.getChangedTouches(),
							obj.getTargetTouches(), touchUIDMapper, windowHeight, windowDPI));
					break;
				case EVENT_TOUCH_END:
					currentTouchState = SortedTouchEvent.createTouchEvent(EnumTouchEvent.TOUCHEND, obj.getChangedTouches(),
							obj.getTargetTouches(), touchUIDMapper, windowHeight, windowDPI);
					List<SortedTouchEvent.TouchPoint> lst = currentTouchState.getEventTouches();
					int len = lst.size();
					BiMap<Integer,Integer> inv = touchIDtoUID.inverse();
					for (int i = 0; i < len; ++i) {
						inv.remove(lst.get(i).uid);
					}
					touchEvents.add(currentTouchState);
					break;
				}
				break;
			}
			case EVENT_INPUT_TOUCH_KEYBOARD: {
				JSTouchKeyboardEvent obj = evt.getEventObj();
				switch(obj.getEventType()) {
					case EVENT_TOUCH_KEYBOARD_ABSOLUTE: {
						int code = obj.getAbsoluteCode();
						char character = obj.getAbsoluteChar();
						keyboardFireEvent(EnumFireKeyboardEvent.KEY_DOWN, code, character);
						keyboardFireEvent(EnumFireKeyboardEvent.KEY_UP, code, character);
						break;
					}
					case EVENT_TOUCH_KEYBOARD_CODEPOINTS: {
						JSString eventsToGenerate = obj.getCodepoints();
						for(int i = 0, l = eventsToGenerate.getLength(); i < l; ++i) {
							char c = (char)eventsToGenerate.charCodeAt(i);
							int eag = KeyboardConstants.getEaglerKeyFromBrowser(asciiUpperToKeyLegacy(Character.toUpperCase(c)), 0);
							keyboardFireEvent(EnumFireKeyboardEvent.KEY_DOWN, eag, c);
							keyboardFireEvent(EnumFireKeyboardEvent.KEY_UP, eag, c);
						}
						break;
					}
				}
				break;
			}
			case EVENT_INPUT_TOUCH_PASTE: {
				pastedStrings.add(BetterJSStringConverter.stringFromJS(evt.getEventObj()));
				break;
			}
			case EVENT_INPUT_FOCUS: {
				isWindowFocused = true;
				break;
			}
			case EVENT_INPUT_BLUR: {
				isWindowFocused = false;
				Arrays.fill(buttonStates, false);
				Arrays.fill(keyStates, false);
				break;
			}
			case EVENT_INPUT_MOUSE_ENTER: {
				isMouseOverWindow = true;
				break;
			}
			case EVENT_INPUT_MOUSE_EXIT: {
				isMouseOverWindow = false;
				break;
			}
			case EVENT_INPUT_WINDOW_RESIZE: {
				JSWindowResizeEvent obj = evt.getEventObj();
				int mask = obj.getEventTypeMask();
				if((mask & EVENT_RESIZE_DPI) != 0) {
					windowDPI = obj.getWindowDPI();
				}
				if((mask & EVENT_RESIZE_WINDOW) != 0) {
					windowWidth = obj.getWindowWidth();
					windowHeight = obj.getWindowHeight();
					if(!useVisualViewport) {
						visualViewportX = 0;
						visualViewportY = 0;
						visualViewportW = windowWidth;
						visualViewportH = windowHeight;
					}
				}
				if((mask & EVENT_RESIZE_VISUAL_VIEWPORT) != 0) {
					if(useVisualViewport) {
						visualViewportX = obj.getVisualViewportX();
						visualViewportY = obj.getVisualViewportY();
						visualViewportW = obj.getVisualViewportW();
						visualViewportH = obj.getVisualViewportH();
						if(visualViewportW < 1) visualViewportW = 1;
						if(visualViewportH < 1) visualViewportH = 1;
						if(visualViewportX < 0) {
							visualViewportW += visualViewportX;
							visualViewportX = 0;
						}else if(visualViewportX >= windowWidth) {
							visualViewportX = windowWidth - 1;
						}
						if(visualViewportY < 0) {
							visualViewportH += visualViewportY;
							visualViewportY = 0;
						}else if(visualViewportY >= windowHeight) {
							visualViewportY = windowHeight - 1;
						}
						if((visualViewportX + visualViewportW) > windowWidth) {
							visualViewportW = windowWidth - visualViewportX;
						}
						if((visualViewportY + visualViewportH) > windowHeight) {
							visualViewportH = windowHeight - visualViewportY;
						}
					}
				}
				break;
			}
			case EVENT_INPUT_GAMEPAD: {
				JSGamepadEvent obj = evt.getEventObj();
				switch(obj.getEventType()) {
					case EVENT_GAMEPAD_CONNECTED: {
						enumerateGamepads();
						break;
					}
					case EVENT_GAMEPAD_DISCONNECTED: {
						if(selectedGamepad != null && obj.getGamepad().getIndex() == selectedGamepad.getIndex()) {
							selectedGamepad = null;
						}
						enumerateGamepads();
						break;
					}
				}
				break;
			}
		}
	}

	private static void handleWindowFocus() {
		if(!isWindowFocused) {
			PlatformRuntime.logger.warn("Detected mouse input while the window was not focused, setting the window focused so the client doesn't pause");
			isWindowFocused = true;
		}
		isMouseOverWindow = true;
	}

	public static int getWindowWidth() {
		return windowWidth;
	}

	public static int getWindowHeight() {
		return windowHeight;
	}

	public static int getVisualViewportX() {
		return visualViewportX;
	}

	public static int getVisualViewportY() {
		return visualViewportY;
	}

	public static int getVisualViewportW() {
		return visualViewportW;
	}

	public static int getVisualViewportH() {
		return visualViewportH;
	}

	public static boolean getWindowFocused() {
		return isWindowFocused || isPointerLocked();
	}

	public static boolean isCloseRequested() {
		return false;
	}

	public static void setVSync(boolean enable) {
		vsync = enable;
	}

	public static void update() {
		update(0);
	}

	public static void update(int fpsLimit) {
		if(windowWidth > 0 && windowHeight > 0) {
			updateCanvasSize(windowWidth, windowHeight);
			WebGLBackBuffer.flipBuffer(windowWidth, windowHeight);
			PlatformScreenRecord.captureFrameHook();
		}
		
		updatePlatformAndSleep(fpsLimit, vsync && vsyncSupport);// && vsync
		PlatformRuntime.pollJSEventsAfterSleep();
	}

	@Import(module = "platformInput", name = "updateCanvasSize")
	private static native void updateCanvasSize(int width, int height);

	@Import(module = "platformInput", name = "updatePlatformAndSleep")
	private static native void updatePlatformAndSleep(int fpsLimit, boolean vsync);

	public static boolean isVSyncSupported() {
		return vsyncSupport;
	}

	@Import(module = "platformInput", name = "isVSyncSupported")
	private static native boolean isVSyncSupported0();

	public static boolean wasResized() {
		if (windowWidth != lastWasResizedWindowWidth || windowHeight != lastWasResizedWindowHeight
				|| windowDPI != lastWasResizedWindowDPI) {
			lastWasResizedWindowWidth = windowWidth;
			lastWasResizedWindowHeight = windowHeight;
			lastWasResizedWindowDPI = windowDPI;
			return true;
		}else {
			return false;
		}
	}

	public static boolean wasVisualViewportResized() {
		if (visualViewportX != lastWasResizedVisualViewportX || visualViewportY != lastWasResizedVisualViewportY
				|| visualViewportW != lastWasResizedVisualViewportW
				|| visualViewportH != lastWasResizedVisualViewportH) {
			lastWasResizedVisualViewportX = visualViewportX;
			lastWasResizedVisualViewportY = visualViewportY;
			lastWasResizedVisualViewportW = visualViewportW;
			lastWasResizedVisualViewportH = visualViewportH;
			return true;
		}else {
			return false;
		}
	}

	public static boolean keyboardNext() {
		if(unpressCTRL) { //un-press ctrl after copy/paste permission
			keyEvents.clear();
			currentKeyEvent = null;
			keyStates[29] = false;
			keyStates[157] = false;
			keyStates[28] = false;
			keyStates[219] = false;
			keyStates[220] = false;
			unpressCTRL = false;
			return false;
		}
		currentKeyEvent = null;
		return !keyEvents.isEmpty() && (currentKeyEvent = keyEvents.remove(0)) != null;
	}

	public static void keyboardFireEvent(EnumFireKeyboardEvent eventType, int eagKey, char keyChar) {
		switch(eventType) {
		case KEY_DOWN:
			keyEvents.add(new VKeyEvent(-1, 0, eagKey, keyChar, EVENT_KEY_DOWN));
			break;
		case KEY_UP:
			keyEvents.add(new VKeyEvent(-1, 0, eagKey, '\0', EVENT_KEY_UP));
			break;
		case KEY_REPEAT:
			keyEvents.add(new VKeyEvent(-1, 0, eagKey, keyChar, EVENT_KEY_REPEAT));
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	public static boolean keyboardGetEventKeyState() {
		return currentKeyEvent == null ? false : (currentKeyEvent.type != EVENT_KEY_UP);
	}

	public static int keyboardGetEventKey() {
		return currentKeyEvent == null ? -1 : currentKeyEvent.eagKey;
	}

	private static char keyToAsciiLegacy(int whichIn, boolean shiftUp) {
		switch(whichIn) {
		case 188: whichIn = 44; break;
		case 109: whichIn = 45; break;
		case 190: whichIn = 46; break;
		case 191: whichIn = 47; break;
		case 192: whichIn = 96; break;
		case 220: whichIn = 92; break;
		case 222: whichIn = 39; break;
		case 221: whichIn = 93; break;
		case 219: whichIn = 91; break;
		case 173: whichIn = 45; break;
		case 187: whichIn = 61; break;
		case 186: whichIn = 59; break;
		case 189: whichIn = 45; break;
		default: break;
		}
		if(shiftUp) {
			switch(whichIn) {
			case 96: return '~';
			case 49: return '!';
			case 50: return '@';
			case 51: return '#';
			case 52: return '$';
			case 53: return '%';
			case 54: return '^';
			case 55: return '&';
			case 56: return '*';
			case 57: return '(';
			case 48: return ')';
			case 45: return '_';
			case 61: return '+';
			case 91: return '{';
			case 93: return '}';
			case 92: return '|';
			case 59: return ':';
			case 39: return '\"';
			case 44: return '<';
			case 46: return '>';
			case 47: return '?';
			default: return (char)whichIn;
			}
		}else {
			if(whichIn >= 65 && whichIn <= 90) {
				return (char)(whichIn + 32);
			}else {
				return (char)whichIn;
			}
		}
	}

	private static int asciiUpperToKeyLegacy(char charIn) {
		switch(charIn) {
		case '\n': return 17;
		case '~': return 192;
		case '!': return 49;
		case '@': return 50;
		case '#': return 51;
		case '$': return 52;
		case '%': return 53;
		case '^': return 54;
		case '&': return 55;
		case '*': return 56;
		case '(': return 57;
		case ')': return 48;
		case '_': return 173;
		case '+': return 187;
		case '{': return 219;
		case '}': return 221;
		case '|': return 220;
		case ':': return 186;
		case '\"': return 222;
		case '<': return 188;
		case '>': return 190;
		case '?': return 191;
		case '.': return 190;
		case '\'': return 222;
		case ';': return 186;
		case '[': return 219;
		case ']': return 221;
		case ',': return 188;
		case '/': return 191;
		case '\\': return 220;
		case '-': return 189;
		case '`': return 192;
		default: return (int)charIn;
		}
	}

	public static char keyboardGetEventCharacter() {
		return currentKeyEvent == null ? '\0' : currentKeyEvent.keyChar;
	}

	public static boolean keyboardIsKeyDown(int key) {
		if(unpressCTRL) { //un-press ctrl after copy/paste permission
			keyStates[28] = false;
			keyStates[29] = false;
			keyStates[157] = false;
			keyStates[219] = false;
			keyStates[220] = false;
		}
		return key < 0 || key >= keyStates.length ? false : keyStates[key];
	}

	public static boolean keyboardIsRepeatEvent() {
		return currentKeyEvent == null ? false : (currentKeyEvent.type == EVENT_KEY_REPEAT);
	}

	public static void keyboardEnableRepeatEvents(boolean b) {
		enableRepeatEvents = b;
	}

	public static boolean keyboardAreKeysLocked() {
		return lockKeys;
	}

	public static boolean mouseNext() {
		currentMouseEvent = null;
		return !mouseEvents.isEmpty() && (currentMouseEvent = mouseEvents.remove(0)) != null;
	}

	public static void mouseFireMoveEvent(EnumFireMouseEvent eventType, int posX, int posY) {
		if(eventType == EnumFireMouseEvent.MOUSE_MOVE) {
			mouseEvents.add(new VMouseEvent(posX, posY, -1, 0.0f, EVENT_MOUSE_MOVE));
		}else {
			throw new UnsupportedOperationException();
		}
	}

	public static void mouseFireButtonEvent(EnumFireMouseEvent eventType, int posX, int posY, int button) {
		switch(eventType) {
		case MOUSE_DOWN:
			mouseEvents.add(new VMouseEvent(posX, posY, button, 0.0f, EVENT_MOUSE_DOWN));
			break;
		case MOUSE_UP:
			mouseEvents.add(new VMouseEvent(posX, posY, button, 0.0f, EVENT_MOUSE_UP));
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	public static void mouseFireWheelEvent(EnumFireMouseEvent eventType, int posX, int posY, float wheel) {
		if(eventType == EnumFireMouseEvent.MOUSE_WHEEL) {
			mouseEvents.add(new VMouseEvent(posX, posY, -1, wheel, EVENT_MOUSE_WHEEL));
		}else {
			throw new UnsupportedOperationException();
		}
	}

	public static boolean mouseGetEventButtonState() {
		return currentMouseEvent == null ? false : (currentMouseEvent.type == EVENT_MOUSE_DOWN);
	}

	public static int mouseGetEventButton() {
		if(currentMouseEvent == null || (currentMouseEvent.type == EVENT_MOUSE_MOVE)) return -1;
		return currentMouseEvent.button;
	}

	public static int mouseGetEventX() {
		return currentMouseEvent == null ? -1 : currentMouseEvent.posX;
	}

	public static int mouseGetEventY() {
		return currentMouseEvent == null ? -1 : currentMouseEvent.posY;
	}

	public static int mouseGetEventDWheel() {
		return (currentMouseEvent.type == EVENT_MOUSE_WHEEL) ? fixWheel(currentMouseEvent.wheel) : 0;
	}

	private static int fixWheel(float val) {
		return (val > 0.0f ? 1 : (val < 0.0f ? -1 : 0));
	}

	public static int mouseGetX() {
		return mouseX;
	}

	public static int mouseGetY() {
		return mouseY;
	}

	public static boolean mouseIsButtonDown(int i) {
		return (i < 0 || i >= buttonStates.length) ? false : buttonStates[i];
	}

	public static int mouseGetDWheel() {
		int ret = (int)mouseDWheel;
		mouseDWheel -= ret;
		return ret;
	}

	public static boolean mouseGrabSupported() {
		return isMouseGrabSupported;
	}

	public static void mouseSetGrabbed(boolean grab) {
		if(isMouseGrabSupported) {
			mouseSetGrabbed0(grab);
		}
	}

	@Import(module = "platformInput", name = "mouseSetGrabbed")
	private static native void mouseSetGrabbed0(boolean grab);

	@Import(module = "platformInput", name = "isMouseGrabSupported")
	private static native boolean mouseGrabSupported0();

	@Import(module = "platformInput", name = "isMouseGrabbed")
	public static native boolean isMouseGrabbed();

	@Import(module = "platformInput", name = "isPointerLocked")
	public static native boolean isPointerLocked();

	public static int mouseGetDX() {
		int ret = (int)mouseDX;
		mouseDX = 0.0f;
		return ret;
	}

	public static int mouseGetDY() {
		int ret = (int)mouseDY;
		mouseDY = 0.0f;
		return ret;
	}

	public static void mouseSetCursorPosition(int x, int y) {
		// obsolete
	}

	public static boolean mouseIsInsideWindow() {
		return isMouseOverWindow;
	}

	@Import(module = "platformOpenGL", name = "isContextLost")
	public static native boolean contextLost();

	private static int processFunctionKeys(int key) {
		if(keyboardIsKeyDown(functionKeyModifier)) {
			if(key >= 49 && key <= 57) {
				key = key - 49 + 112;
			}
		}
		return key;
	}

	public static void setFunctionKeyModifier(int key) {
		functionKeyModifier = key;
	}

	public static void clearEvenBuffers() {
		mouseEvents.clear();
		keyEvents.clear();
		//touchEvents.clear();
		net.lax1dude.eaglercraft.v1_8.Gamepad.clearEventBuffer();
	}

	@Import(module = "platformInput", name = "supportsFullscreen")
	private static native boolean supportsFullscreen0();

	public static boolean supportsFullscreen() {
		return fullscreenSupported;
	}

	public static void toggleFullscreen() {
		if(fullscreenSupported) {
			toggleFullscreen0();
		}
	}

	@Import(module = "platformInput", name = "toggleFullscreen")
	private static native void toggleFullscreen0();

	public static boolean isFullscreen() {
		return fullscreenSupported && isFullscreen0();
	}

	@Import(module = "platformInput", name = "isFullscreen")
	private static native boolean isFullscreen0();

	@Import(module = "platformInput", name = "isVisualViewport")
	private static native boolean isVisualViewport0();

	public static void showCursor(EnumCursorType cursor) {
		switch(cursor) {
		case DEFAULT:
		default:
			canvas.getStyle().setProperty("cursor", "default");
			break;
		case HAND:
			canvas.getStyle().setProperty("cursor", "pointer");
			break;
		case TEXT:
			canvas.getStyle().setProperty("cursor", "text");
			break;
		}
	}

	public static boolean touchNext() {
		currentTouchEvent = null;
		return !touchEvents.isEmpty() && (currentTouchEvent = touchEvents.remove(0)) != null;
	}

	public static EnumTouchEvent touchGetEventType() {
		return currentTouchEvent != null ? currentTouchEvent.type : null;
	}

	public static int touchGetEventTouchPointCount() {
		return currentTouchEvent != null ? currentTouchEvent.getEventTouches().size() : 0;
	}

	public static int touchGetEventTouchX(int pointId) {
		return currentTouchEvent != null ? currentTouchEvent.getEventTouches().get(pointId).pointX : 0;
	}

	public static int touchGetEventTouchY(int pointId) {
		return currentTouchEvent != null ? currentTouchEvent.getEventTouches().get(pointId).pointY : 0;
	}

	public static float touchGetEventTouchRadiusMixed(int pointId) {
		if(currentTouchEvent != null) {
			return currentTouchEvent.getEventTouches().get(pointId).radius;
		}else {
			return 1.0f;
		}
	}

	public static float touchGetEventTouchForce(int pointId) {
		return currentTouchEvent != null ? (float)currentTouchEvent.getEventTouches().get(pointId).force : 0.0f;
	}

	public static int touchGetEventTouchPointUID(int pointId) {
		return currentTouchEvent != null ? currentTouchEvent.getEventTouches().get(pointId).uid : -1;
	}

	public static int touchPointCount() {
		return currentTouchState != null ? currentTouchState.getTargetTouchesSize() : 0;
	}

	public static int touchPointX(int pointId) {
		return currentTouchState != null ? currentTouchState.getTargetTouches().get(pointId).pointX : -1;
	}

	public static int touchPointY(int pointId) {
		return currentTouchState != null ? currentTouchState.getTargetTouches().get(pointId).pointY : -1;
	}

	public static float touchRadiusMixed(int pointId) {
		if(currentTouchState != null) {
			return currentTouchState.getTargetTouches().get(pointId).radius;
		}else {
			return 1.0f;
		}
	}

	public static float touchForce(int pointId) {
		return currentTouchState != null ? (float)currentTouchState.getTargetTouches().get(pointId).force : 0.0f;
	}

	public static int touchPointUID(int pointId) {
		return currentTouchState != null ? currentTouchState.getTargetTouches().get(pointId).uid : -1;
	}

	public static String touchGetPastedString() {
		return pastedStrings.isEmpty() ? null : pastedStrings.remove(0);
	}

	@Import(module = "platformInput", name = "touchSetOpenKeyboardZone")
	public static native void touchSetOpenKeyboardZone(int x, int y, int w, int h);

	@Import(module = "platformInput", name = "touchCloseDeviceKeyboard")
	public static native void touchCloseDeviceKeyboard();

	@Import(module = "platformInput", name = "touchIsDeviceKeyboardOpenMAYBE")
	public static native boolean touchIsDeviceKeyboardOpenMAYBE();

	private static void enumerateGamepads() {
		if(!gamepadSupported) return;
		if(selectedGamepad != null) {
			selectedGamepad = updateGamepad(selectedGamepad);
			if(selectedGamepad == null || !TeaVMUtils.isTruthy(selectedGamepad) || !selectedGamepad.isConnected()) {
				selectedGamepad = null;
			}
		}
		List<Gamepad> oldList = null;
		if(!gamepadList.isEmpty()) {
			oldList = new ArrayList<>(gamepadList);
			gamepadList.clear();
		}
		Gamepad[] gamepads = Navigator.getGamepads();
		if(gamepads != null && gamepads.length > 0) {
			for(int i = 0; i < gamepads.length; ++i) {
				Gamepad g = gamepads[i];
				if(TeaVMUtils.isTruthy(g) && g.isConnected() && "standard".equals(g.getMapping())) {
					gamepadList.add(g);
				}
			}
		}
		if(selectedGamepad != null) {
			selectedGamepadName = selectedGamepad.getId();
		}
		if(oldList == null) {
			if(!gamepadList.isEmpty()) {
				for(int i = 0, l = gamepadList.size(); i < l; ++i) {
					PlatformRuntime.logger.info("Found controller: {}", gamepadList.get(i).getId());
				}
			}
		}else {
			if(gamepadList.isEmpty()) {
				for(int i = 0, l = oldList.size(); i < l; ++i) {
					PlatformRuntime.logger.info("Lost controller: {}", oldList.get(i).getId());
				}
			}else {
				Map<String,Integer> oldDevCounts = new HashMap<>();
				for(Gamepad gg : oldList) {
					String s = gg.getId();
					Integer i = oldDevCounts.get(s);
					if(i != null) {
						oldDevCounts.put(s, Integer.valueOf(i.intValue() + 1));
					}else {
						oldDevCounts.put(s, Integer.valueOf(1));
					}
				}
				Map<String,Integer> newDevCounts = new HashMap<>();
				for(Gamepad gg : gamepadList) {
					String s = gg.getId();
					Integer i = newDevCounts.get(s);
					if(i != null) {
						newDevCounts.put(s, Integer.valueOf(i.intValue() + 1));
					}else {
						newDevCounts.put(s, Integer.valueOf(1));
					}
				}
				for(Entry<String,Integer> etr : oldDevCounts.entrySet()) {
					Integer i = newDevCounts.get(etr.getKey());
					if(i == null) {
						for(int j = 0, l = etr.getValue().intValue(); j < l; ++j) {
							PlatformRuntime.logger.info("Lost controller: {}", etr.getKey());
						}
					}else {
						int j = i.intValue();
						int k = etr.getValue().intValue();
						if(k != j) {
							if(k < j) {
								for(int m = 0, l = j - k; m < l; ++m) {
									PlatformRuntime.logger.info("Found controller: {}", etr.getKey());
								}
							}else {
								for(int m = 0, l = k - j; m < l; ++m) {
									PlatformRuntime.logger.info("Lost controller: {}", etr.getKey());
								}
							}
						}
					}
				}
				for(Entry<String,Integer> etr : newDevCounts.entrySet()) {
					Integer i = oldDevCounts.get(etr.getKey());
					if(i == null) {
						for(int j = 0, l = etr.getValue().intValue(); j < l; ++j) {
							PlatformRuntime.logger.info("Found controller: {}", etr.getKey());
						}
					}
				}
			}
			
		}
	}

	public static int gamepadGetValidDeviceCount() {
		if(!gamepadSupported) return 0;
		return gamepadList.size();
	}

	public static String gamepadGetDeviceName(int deviceId) {
		if(gamepadSupported && deviceId >= 0 && deviceId < gamepadList.size()) {
			return gamepadList.get(deviceId).getId();
		}else {
			return "Unknown";
		}
	}

	public static void gamepadSetSelectedDevice(int deviceId) {
		if(!gamepadSupported) return;
		gamepadReset();
		if(deviceId >= 0 && deviceId < gamepadList.size()) {
			selectedGamepad = gamepadList.get(deviceId);
			gamepadTimestamp = -1.0;
			if(!TeaVMUtils.isTruthy(selectedGamepad) || !selectedGamepad.isConnected()) {
				selectedGamepad = null;
			}
		}else {
			selectedGamepad = null;
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

	@JSBody(params = { }, script = "return (typeof navigator.getGamepads === \"function\");")
	private static native boolean isGamepadSupported();

	@JSBody(params = { "gp" }, script = "return navigator.getGamepads()[gp.index];")
	private static native Gamepad updateGamepad(Gamepad gp);

	public static void gamepadUpdate() {
		if(!gamepadSupported) return;
		if(selectedGamepad != null) {
			selectedGamepad = updateGamepad(selectedGamepad);
			if(selectedGamepad == null || !TeaVMUtils.isTruthy(selectedGamepad) || !selectedGamepad.isConnected()) {
				gamepadReset();
				selectedGamepad = null;
				return;
			}
			double ts = selectedGamepad.getTimestamp();
			if(ts != gamepadTimestamp) {
				gamepadTimestamp = ts;
				gamepadReset();
				GamepadButton[] btns = selectedGamepad.getButtons();
				for(int i = 0; i < btns.length; ++i) {
					int j = GamepadConstants.getEaglerButtonFromBrowser(i);
					if(j >= 0 && j < gamepadButtonStates.length) {
						gamepadButtonStates[j] = btns[i].isPressed();
					}
				}
				double[] axes = selectedGamepad.getAxes();
				for(int i = 0; i < axes.length; ++i) {
					if(i >= 4) {
						break;
					}
					gamepadAxisStates[i] = (float)axes[i];
				}
			}
		}else {
			gamepadReset();
		}
	}

	public static boolean gamepadIsValid() {
		if(!gamepadSupported) return false;
		return selectedGamepad != null;
	}

	public static String gamepadGetName() {
		return gamepadSupported && selectedGamepad != null ? selectedGamepadName : "Unknown";
	}

	public static boolean gamepadGetButtonState(int button) {
		return gamepadSupported && selectedGamepad != null && button >= 0 && button < gamepadButtonStates.length ? gamepadButtonStates[button] : false;
	}

	public static float gamepadGetAxis(int axis) {
		return gamepadSupported && selectedGamepad != null && axis >= 0 && axis < gamepadAxisStates.length ? gamepadAxisStates[axis] : 0.0f;
	}

	public static float getDPI() {
		return windowDPI;
	}

	static void initWindowSize(int sw, int sh, float dpi) {
		windowWidth = sw;
		windowHeight = sh;
		windowDPI = dpi;
		visualViewportX = 0;
		visualViewportY = 0;
		visualViewportW = sw;
		visualViewportH = sh;
	}

}
