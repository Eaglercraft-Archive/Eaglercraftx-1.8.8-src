package net.lax1dude.eaglercraft.v1_8.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TouchEvent;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.VisualViewport;

import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Navigator;
import org.teavm.jso.browser.TimerHandler;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.dom.css.CSSStyleDeclaration;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.KeyboardEvent;
import org.teavm.jso.dom.events.MouseEvent;
import org.teavm.jso.dom.events.WheelEvent;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLFormElement;
import org.teavm.jso.dom.html.HTMLInputElement;
import org.teavm.jso.dom.html.TextRectangle;
import org.teavm.jso.gamepad.Gamepad;
import org.teavm.jso.gamepad.GamepadButton;
import org.teavm.jso.gamepad.GamepadEvent;

import net.lax1dude.eaglercraft.v1_8.internal.teavm.ClientMain;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.EarlyLoadScreen;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.InputEvent;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.LegacyKeycodeTranslator;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.OffsetTouch;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.SortedTouchEvent;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.WebGLBackBuffer;

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

	private static Window win = null;
	private static HTMLElement parent = null;
	private static HTMLCanvasElement canvas = null;
	private static HTMLElement touchKeyboardOpenZone = null;
	private static int touchOpenZoneX = 0;
	private static int touchOpenZoneY = 0;
	private static int touchOpenZoneW = 0;
	private static int touchOpenZoneH = 0;
	private static HTMLFormElement touchKeyboardForm = null;
	private static HTMLInputElement touchKeyboardField = null;
	private static boolean shownTouchKeyboardEventWarning = false;
	private static boolean shownLegacyTouchKeyboardWarning = false;
	private static boolean showniOSReturnTouchKeyboardWarning = false;
	private static double lastTouchKeyboardEvtA = 0.0;
	private static double lastTouchKeyboardEvtB = 0.0;
	private static double lastTouchKeyboardEvtC = 0.0;

	private static EventListener<?> contextmenu = null;
	private static EventListener<?> mousedown = null;
	private static EventListener<?> mouseup = null;
	private static EventListener<?> mousemove = null;
	private static EventListener<?> mouseenter = null;
	private static EventListener<?> mouseleave = null;
	private static EventListener<?> touchstart = null;
	private static EventListener<?> touchend = null;
	private static EventListener<?> touchmove = null;
	private static EventListener<?> touchcancel = null;
	private static EventListener<?> gamepadconnected = null;
	private static EventListener<?> gamepaddisconnected = null;
	private static EventListener<?> keydown = null;
	private static EventListener<?> keyup = null;
	private static EventListener<?> touchKeyboardOpenZone_touchstart = null;
	private static EventListener<?> touchKeyboardOpenZone_touchend = null;
	private static EventListener<?> touchKeyboardOpenZone_touchmove = null;
	private static EventListener<?> wheel = null;
	private static EventListener<?> focus = null;
	private static EventListener<?> blur = null;
	private static EventListener<?> pointerlock = null;
	private static EventListener<?> pointerlockerr = null;
	private static EventListener<?> fullscreen = null;

	private static Map<String,LegacyKeycodeTranslator.LegacyKeycode> keyCodeTranslatorMap = null;

	public static Map<String,LegacyKeycodeTranslator.LegacyKeycode> getKeyCodeTranslatorMapTeaVM() {
		return keyCodeTranslatorMap;
	}

	private static final List<String> pastedStrings = new LinkedList<>();

	private static final int EVENT_KEY_DOWN = 0;
	private static final int EVENT_KEY_UP = 1;
	private static final int EVENT_KEY_REPEAT = 2;

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

	private static final List<VMouseEvent> mouseEvents = new LinkedList<>();
	private static final List<VKeyEvent> keyEvents = new LinkedList<>();
	private static final List<SortedTouchEvent> touchEvents = new LinkedList<>();

	private static boolean hasShownPressAnyKey = false;
	private static boolean isOnMobilePressAnyKey = false;

	private static interface MobilePressAnyKeyHandler {
		void call(boolean enterBootMenu);
	}

	private static HTMLElement mobilePressAnyKeyScreen = null;
	private static MobilePressAnyKeyHandler mobilePressAnyKey = null;
	static boolean isLikelyMobileBrowser = false;

	private static int mouseX = 0;
	private static int mouseY = 0;
	private static double mouseDX = 0.0D;
	private static double mouseDY = 0.0D;
	private static double mouseDWheel = 0.0D;
	private static boolean enableRepeatEvents = true;
	private static boolean isWindowFocused = true;
	private static boolean isMouseOverWindow = true;
	static boolean unpressCTRL = false;

	private static SortedTouchEvent currentTouchState = null;
	private static SortedTouchEvent currentTouchEvent = null;

	public static int touchOffsetXTeaVM = 0;
	public static int touchOffsetYTeaVM = 0;

	private static boolean gamepadSupported = false;
	private static final List<Gamepad> gamepadList = new ArrayList<>();
	private static Gamepad selectedGamepad = null;
	private static String selectedGamepadName = null;
	private static double gamepadTimestamp = -1.0;
	private static final boolean[] gamepadButtonStates = new boolean[24];
	private static final float[] gamepadAxisStates = new float[4];

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

	private static VMouseEvent currentEvent = null;
	private static VKeyEvent currentEventK = null;
	private static boolean[] buttonStates = new boolean[8];
	private static boolean[] keyStates = new boolean[256];

	private static int functionKeyModifier = KeyboardConstants.KEY_F;

	// Can't support webkit vendor prefix since there's no document.pointerLockElement
	private static final int POINTER_LOCK_NONE = 0;
	private static final int POINTER_LOCK_CORE = 1;
	private static final int POINTER_LOCK_MOZ = 2;
	private static int pointerLockSupported = POINTER_LOCK_NONE;
	private static long mouseUngrabTimer = 0l;
	private static long mouseGrabTimer = 0l;
	private static int mouseUngrabTimeout = -1;
	private static boolean pointerLockFlag = false;
	private static boolean pointerLockWaiting = false;

	private static final int FULLSCREEN_NONE = 0;
	private static final int FULLSCREEN_CORE = 1;
	private static final int FULLSCREEN_WEBKIT = 2;
	private static final int FULLSCREEN_MOZ = 3;
	private static int fullscreenSupported = FULLSCREEN_NONE;

	private static JSObject fullscreenQuery = null;

	public static boolean keyboardLockSupported = false;
	public static boolean lockKeys = false;

	static boolean vsync = true;
	static boolean vsyncSupport = false;

	private static long vsyncWaiting = -1l;
	private static AsyncCallback<Void> vsyncAsyncCallback = null;
	private static int vsyncTimeout = -1;
	
	// hack to fix occasional freeze on iOS
	private static int vsyncSaveLockInterval = -1;

	@JSFunctor
	private static interface UnloadCallback extends JSObject {
		void call();
	}

	@JSBody(params = { "win", "cb" }, script = "win.__curEaglerX188UnloadListenerCB = cb; if((typeof win.__isEaglerX188UnloadListenerSet === \"string\") && win.__isEaglerX188UnloadListenerSet === \"yes\") return; win.onbeforeunload = function(evt) { if(win.__curEaglerX188UnloadListenerCB) win.__curEaglerX188UnloadListenerCB(); return false; }; win.__isEaglerX188UnloadListenerSet = \"yes\";")
	private static native void onBeforeCloseRegister(Window win, UnloadCallback cb);

	static void initHooks(Window window, HTMLElement parent_, HTMLCanvasElement canvaz) {
		win = window;
		parent = parent_;
		canvas = canvaz;
		canvas.getStyle().setProperty("cursor", "default");
		updateTouchOffset();
		lastWasResizedWindowWidth = -2;
		lastWasResizedWindowHeight = -2;
		lastWasResizedWindowDPI = -2.0f;
		lastWasResizedVisualViewportX = -2;
		lastWasResizedVisualViewportY = -2;
		lastWasResizedVisualViewportW = -2;
		lastWasResizedVisualViewportH = -2;
		hasShownPressAnyKey = false;
		touchOpenZoneX = 0;
		touchOpenZoneY = 0;
		touchOpenZoneW = 0;
		touchOpenZoneH = 0;
		touchKeyboardForm = null;
		touchKeyboardField = null;
		shownLegacyTouchKeyboardWarning = false;
		shownTouchKeyboardEventWarning = false;
		showniOSReturnTouchKeyboardWarning = false;
		lastTouchKeyboardEvtA = 0.0;
		lastTouchKeyboardEvtB = 0.0;
		lastTouchKeyboardEvtC = 0.0;
		touchKeyboardOpenZone = win.getDocument().createElement("div");
		touchKeyboardOpenZone.getClassList().add("_eaglercraftX_keyboard_open_zone");
		CSSStyleDeclaration decl = touchKeyboardOpenZone.getStyle();
		decl.setProperty("display", "none");
		decl.setProperty("position", "absolute");
		decl.setProperty("background-color", "transparent");
		decl.setProperty("top", "0px");
		decl.setProperty("left", "0px");
		decl.setProperty("width", "0px");
		decl.setProperty("height", "0px");
		decl.setProperty("z-index", "100");
		decl.setProperty("touch-action", "pan-x pan-y");
		decl.setProperty("-webkit-touch-callout", "none");
		decl.setProperty("-webkit-tap-highlight-color", "rgba(255, 255, 255, 0)");
		parent.appendChild(touchKeyboardOpenZone);
		
		PlatformRuntime.logger.info("Loading keyboard layout data");
		
		LegacyKeycodeTranslator keycodeTranslator = new LegacyKeycodeTranslator();
		if(checkKeyboardLayoutSupported()) {
			try {
				iterateKeyboardLayout(keycodeTranslator::addBrowserLayoutMapping);
			}catch(Throwable t) {
				PlatformRuntime.logger.error("Caught exception querying keyboard layout from browser, using the default layout instead");
				PlatformRuntime.logger.error(t);
			}
			int cnt = keycodeTranslator.getRemappedKeyCount();
			if(cnt > 0) {
				PlatformRuntime.logger.info("KeyboardLayoutMap remapped {} keys from their default codes", cnt);
			}
		}
		keyCodeTranslatorMap = keycodeTranslator.buildLayoutTable();
		
		parent.addEventListener("contextmenu", contextmenu = new EventListener<MouseEvent>() {
			@Override
			public void handleEvent(MouseEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
			}
		});
		canvas.addEventListener("mousedown", mousedown = new EventListener<MouseEvent>() {
			@Override
			public void handleEvent(MouseEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
				if(tryGrabCursorHook()) return;
				int b = evt.getButton();
				b = b == 1 ? 2 : (b == 2 ? 1 : b);
				if(b >= 0 && b < buttonStates.length) buttonStates[b] = true;
				int eventX = (int)(getOffsetX(evt, touchOffsetXTeaVM) * windowDPI);
				int eventY = windowHeight - (int)(getOffsetY(evt, touchOffsetYTeaVM) * windowDPI) - 1;
				synchronized(mouseEvents) {
					mouseEvents.add(new VMouseEvent(eventX, eventY, b, 0.0f, EVENT_MOUSE_DOWN));
					if(mouseEvents.size() > 64) {
						mouseEvents.remove(0);
					}
				}
			}
		});
		canvas.addEventListener("mouseup", mouseup = new EventListener<MouseEvent>() {
			@Override
			public void handleEvent(MouseEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
				int b = evt.getButton();
				b = b == 1 ? 2 : (b == 2 ? 1 : b);
				if(b >= 0 && b < buttonStates.length) buttonStates[b] = false;
				int eventX = (int)(getOffsetX(evt, touchOffsetXTeaVM) * windowDPI);
				int eventY = windowHeight - (int)(getOffsetY(evt, touchOffsetYTeaVM) * windowDPI) - 1;
				synchronized(mouseEvents) {
					mouseEvents.add(new VMouseEvent(eventX, eventY, b, 0.0f, EVENT_MOUSE_UP));
					if(mouseEvents.size() > 64) {
						mouseEvents.remove(0);
					}
				}
			}
		});
		canvas.addEventListener("mousemove", mousemove = new EventListener<MouseEvent>() {
			@Override
			public void handleEvent(MouseEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
				mouseX = (int)(getOffsetX(evt, touchOffsetXTeaVM) * windowDPI);
				mouseY = windowHeight - (int)(getOffsetY(evt, touchOffsetYTeaVM) * windowDPI) - 1;
				if(pointerLockFlag) {
					mouseDX += evt.getMovementX();
					mouseDY += -evt.getMovementY();
				}
				if(hasShownPressAnyKey) {
					int eventX = (int)(getOffsetX(evt, touchOffsetXTeaVM) * windowDPI);
					int eventY = windowHeight - (int)(getOffsetY(evt, touchOffsetYTeaVM) * windowDPI) - 1;
					synchronized(mouseEvents) {
						mouseEvents.add(new VMouseEvent(eventX, eventY, -1, 0.0f, EVENT_MOUSE_MOVE));
						if(mouseEvents.size() > 64) {
							mouseEvents.remove(0);
						}
					}
				}
			}
		});
		canvas.addEventListener("mouseenter", mouseenter = new EventListener<MouseEvent>() {
			@Override
			public void handleEvent(MouseEvent evt) {
				isMouseOverWindow = true;
			}
		});
		canvas.addEventListener("mouseleave", mouseleave = new EventListener<MouseEvent>() {
			@Override
			public void handleEvent(MouseEvent evt) {
				isMouseOverWindow = false;
			}
		});
		canvas.addEventListener("touchstart", touchstart = new EventListener<TouchEvent>() {
			@Override
			public void handleEvent(TouchEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
				SortedTouchEvent sorted = new SortedTouchEvent(evt, touchUIDMapperCreate);
				currentTouchState = sorted;
				List<OffsetTouch> lst = sorted.getEventTouches();
				synchronized(touchEvents) {
					touchEvents.add(sorted);
					if(touchEvents.size() > 64) {
						touchEvents.remove(0);
					}
				}
				touchCloseDeviceKeyboard0(false);
			}
		});
		canvas.addEventListener("touchend", touchend = new EventListener<TouchEvent>() {
			@Override
			public void handleEvent(TouchEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
				SortedTouchEvent sorted = new SortedTouchEvent(evt, touchUIDMapper);
				currentTouchState = sorted;
				List<OffsetTouch> lst = sorted.getEventTouches();
				int len = lst.size();
				for (int i = 0; i < len; ++i) {
					touchIDtoUID.remove(lst.get(i).touch.getIdentifier());
				}
				synchronized(touchEvents) {
					touchEvents.add(sorted);
					if(touchEvents.size() > 64) {
						touchEvents.remove(0);
					}
				}
			}
		});
		canvas.addEventListener("touchmove", touchmove = new EventListener<TouchEvent>() {
			@Override
			public void handleEvent(TouchEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
				SortedTouchEvent sorted = new SortedTouchEvent(evt, touchUIDMapperCreate);
				currentTouchState = sorted;
				if(hasShownPressAnyKey) {
					synchronized(touchEvents) {
						touchEvents.add(sorted);
						if(touchEvents.size() > 64) {
							touchEvents.remove(0);
						}
					}
				}
			}
		});
		canvas.addEventListener("touchcancel", touchcancel = new EventListener<TouchEvent>() {
			@Override
			public void handleEvent(TouchEvent evt) {
				SortedTouchEvent sorted = new SortedTouchEvent(evt, touchUIDMapper);
				currentTouchState = sorted;
				List<OffsetTouch> lst = sorted.getEventTouches();
				int len = lst.size();
				for (int i = 0; i < len; ++i) {
					touchIDtoUID.remove(lst.get(i).touch.getIdentifier());
				}
				if(hasShownPressAnyKey) {
					synchronized(touchEvents) {
						touchEvents.add(sorted);
						if(touchEvents.size() > 64) {
							touchEvents.remove(0);
						}
					}
				}
			}
		});
		win.addEventListener("keydown", keydown = new EventListener<KeyboardEvent>() {
			@Override
			public void handleEvent(KeyboardEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
				if(!enableRepeatEvents && evt.isRepeat()) return;
				LegacyKeycodeTranslator.LegacyKeycode keyCode = null;
				if(keyCodeTranslatorMap != null && hasCodeVar(evt)) {
					keyCode = keyCodeTranslatorMap.get(evt.getCode());
				}
				int w;
				int loc;
				if(keyCode != null) {
					w = keyCode.keyCode;
					loc = keyCode.location;
				}else {
					w = getWhich(evt);
					loc = getLocationSafe(evt);
				}
				if (w == 122 && !evt.isRepeat()) { // F11
					toggleFullscreen();
				}
				int ww = processFunctionKeys(w);
				int eag = KeyboardConstants.getEaglerKeyFromBrowser(ww, ww == w ? loc : 0);
				if(isOnMobilePressAnyKey && mobilePressAnyKey != null) {
					if(eag == KeyboardConstants.KEY_DELETE) {
						mobilePressAnyKey.call(true);
						return;
					}
				}
				if(eag != 0) {
					keyStates[eag] = true;
				}
				String s = getCharOrNull(evt);
				int l = s.length();
				char c;
				if(l == 1) {
					c = s.charAt(0);
				}else if(l == 0) {
					c = keyToAsciiLegacy(w, evt.isShiftKey());
				}else if(s.equals("Unidentified")) {
					return;
				}else {
					c = '\0';
				}
				synchronized(keyEvents) {
					keyEvents.add(new VKeyEvent(ww, loc, eag, c, EVENT_KEY_DOWN));
					if(keyEvents.size() > 64) {
						keyEvents.remove(0);
					}
				}
				JSObject obj = evt.getTimeStamp();
				if(TeaVMUtils.isTruthy(obj)) {
					lastTouchKeyboardEvtA = ((JSNumber)obj).doubleValue();
				}
			}
		});
		win.addEventListener("keyup", keyup = new EventListener<KeyboardEvent>() {
			@Override
			public void handleEvent(KeyboardEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
				LegacyKeycodeTranslator.LegacyKeycode keyCode = null;
				if(keyCodeTranslatorMap != null && hasCodeVar(evt)) {
					keyCode = keyCodeTranslatorMap.get(evt.getCode());
				}
				int w;
				int loc;
				if(keyCode != null) {
					w = keyCode.keyCode;
					loc = keyCode.location;
				}else {
					w = getWhich(evt);
					loc = getLocationSafe(evt);
				}
				int ww = processFunctionKeys(w);
				int eag = KeyboardConstants.getEaglerKeyFromBrowser(ww, ww == w ? loc : 0);
				if(eag != 0) {
					keyStates[eag] = false;
					if(eag == functionKeyModifier) {
						for(int key = KeyboardConstants.KEY_F1; key <= KeyboardConstants.KEY_F10; ++key) {
							keyStates[key] = false;
						}
					}
				}
				String s = getCharOrNull(evt);
				int l = s.length();
				char c;
				if(l == 1) {
					c = s.charAt(0);
				}else if(l == 0) {
					c = keyToAsciiLegacy(w, evt.isShiftKey());
				}else if(s.equals("Unidentified")) {
					return;
				}else {
					c = '\0';
				}
				synchronized(keyEvents) {
					keyEvents.add(new VKeyEvent(ww, loc, eag, c, EVENT_KEY_UP));
					if(keyEvents.size() > 64) {
						keyEvents.remove(0);
					}
				}
			}
		});
		touchKeyboardOpenZone.addEventListener("touchstart", touchKeyboardOpenZone_touchstart = new EventListener<TouchEvent>() {
			@Override
			public void handleEvent(TouchEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
			}
		});
		touchKeyboardOpenZone.addEventListener("touchend", touchKeyboardOpenZone_touchend = new EventListener<TouchEvent>() {
			@Override
			public void handleEvent(TouchEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
				touchOpenDeviceKeyboard();
			}
		});
		touchKeyboardOpenZone.addEventListener("touchmove", touchKeyboardOpenZone_touchmove = new EventListener<TouchEvent>() {
			@Override
			public void handleEvent(TouchEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
			}
		});
		canvas.addEventListener("wheel", wheel = new EventListener<WheelEvent>() {
			@Override
			public void handleEvent(WheelEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
				double delta = -evt.getDeltaY();
				mouseDWheel += delta;
				if(hasShownPressAnyKey) {
					int eventX = (int)(getOffsetX(evt, touchOffsetXTeaVM) * windowDPI);
					int eventY = windowHeight - (int)(getOffsetY(evt, touchOffsetYTeaVM) * windowDPI) - 1;
					synchronized(mouseEvents) {
						mouseEvents.add(new VMouseEvent(eventX, eventY, -1, (float)delta, EVENT_MOUSE_WHEEL));
						if(mouseEvents.size() > 64) {
							mouseEvents.remove(0);
						}
					}
				}
			}
		});
		win.addEventListener("blur", blur = new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				isWindowFocused = false;
				for(int i = 0; i < buttonStates.length; ++i) {
					buttonStates[i] = false;
				}
				for(int i = 0; i < keyStates.length; ++i) {
					keyStates[i] = false;
				}
			}
		});
		win.addEventListener("focus", focus = new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				isWindowFocused = true;
			}
		});
		
		try {
			pointerLockSupported = getSupportedPointerLock(win.getDocument());
		}catch(Throwable t) {
			pointerLockSupported = POINTER_LOCK_NONE;
		}
		if(pointerLockSupported != POINTER_LOCK_NONE) {
			win.getDocument().addEventListener(pointerLockSupported == POINTER_LOCK_MOZ ? "mozpointerlockchange" : "pointerlockchange", pointerlock = new EventListener<Event>() {
				@Override
				public void handleEvent(Event evt) {
					Window.setTimeout(new TimerHandler() {
						@Override
						public void onTimer() {
							boolean grab = isPointerLockedImpl();
							if(!grab) {
								if(pointerLockFlag) {
									mouseUngrabTimer = PlatformRuntime.steadyTimeMillis();
								}
							}
							pointerLockFlag = grab;
						}
					}, 60);
					mouseDX = 0.0D;
					mouseDY = 0.0D;
					pointerLockWaiting = false;
				}
			});
			win.getDocument().addEventListener(pointerLockSupported == POINTER_LOCK_MOZ ? "mozpointerlockerror" : "pointerlockerror", pointerlockerr = new EventListener<Event>() {
				@Override
				public void handleEvent(Event evt) {
					pointerLockWaiting = false;
				}
			});
			if(pointerLockSupported == POINTER_LOCK_MOZ) {
				PlatformRuntime.logger.info("Using moz- vendor prefix for pointer lock");
			}
		}else {
			PlatformRuntime.logger.error("Pointer lock is not supported on this browser");
		}

		if(pointerLockSupported != POINTER_LOCK_NONE) {
			String ua = PlatformRuntime.getUserAgentString();
			if(ua != null) {
				ua = ua.toLowerCase();
				isLikelyMobileBrowser = ua.contains("mobi") || ua.contains("tablet");
			}else {
				isLikelyMobileBrowser = false;
			}
		}else {
			isLikelyMobileBrowser = true;
		}

		try {
			fullscreenSupported = getSupportedFullScreen(win.getDocument());
		}catch(Throwable t) {
			fullscreenSupported = FULLSCREEN_NONE;
		}
		if(fullscreenSupported != FULLSCREEN_NONE) {
			fullscreenQuery = fullscreenMediaQuery();
			if(fullscreenSupported == FULLSCREEN_CORE && (keyboardLockSupported = checkKeyboardLockSupported())) {
				TeaVMUtils.addEventListener(fullscreenQuery, "change", fullscreen = new EventListener<Event>() {
					@Override
					public void handleEvent(Event evt) {
						if (!mediaQueryMatches(evt)) {
							unlockKeys();
							lockKeys = false;
						}
					}
				});
			}
			if(fullscreenSupported == FULLSCREEN_WEBKIT) {
				PlatformRuntime.logger.info("Using webkit- vendor prefix for fullscreen");
			}else if(fullscreenSupported == FULLSCREEN_MOZ) {
				PlatformRuntime.logger.info("Using moz- vendor prefix for fullscreen");
			}
		}else {
			PlatformRuntime.logger.error("Fullscreen is not supported on this browser");
		}

		try {
			onBeforeCloseRegister(window, () -> PlatformRuntime.beforeUnload());
		}catch(Throwable t) {
		}

		vsyncWaiting = -1l;
		vsyncAsyncCallback = null;
		vsyncTimeout = -1;
		vsyncSupport = false;

		try {
			asyncRequestAnimationFrame();
			vsyncSupport = true;
		}catch(Throwable t) {
			PlatformRuntime.logger.error("VSync is not supported on this browser!");
		}

		if(vsyncSupport) {
			if(vsyncSaveLockInterval != -1) {
				try {
					Window.clearInterval(vsyncSaveLockInterval);
				}catch(Throwable t) {
				}
				vsyncSaveLockInterval = -1;
			}
			// fix for iOS freezing randomly...?
			vsyncSaveLockInterval =  Window.setInterval(() -> {
				if(vsyncWaiting != -1l) {
					long steadyTime = PlatformRuntime.steadyTimeMillis();
					if(steadyTime - vsyncWaiting > 1000) {
						PlatformRuntime.logger.error("VSync lockup detected! Attempting to recover...");
						vsyncWaiting = -1l;
						if(vsyncTimeout != -1) {
							try {
								Window.clearTimeout(vsyncTimeout);
							}catch(Throwable t) {
							}
							vsyncTimeout = -1;
						}
						if(vsyncAsyncCallback != null) {
							AsyncCallback<Void> cb = vsyncAsyncCallback;
							vsyncAsyncCallback = null;
							cb.complete(null);
						}else {
							PlatformRuntime.logger.error("Async callback is null!");
						}
					}
				}
			}, 1000);
		}

		try {
			gamepadSupported = gamepadSupported();
			if(gamepadSupported) {
				win.addEventListener("gamepadconnected", gamepadconnected = new EventListener<GamepadEvent>() {
					@Override
					public void handleEvent(GamepadEvent evt) {
						enumerateGamepads();
					}
				});
				win.addEventListener("gamepaddisconnected", gamepaddisconnected = new EventListener<GamepadEvent>() {
					@Override
					public void handleEvent(GamepadEvent evt) {
						if(selectedGamepad != null && evt.getGamepad().getIndex() == selectedGamepad.getIndex()) {
							selectedGamepad = null;
						}
						enumerateGamepads();
					}
				});
			}
		}catch(Throwable t) {
			gamepadSupported = false;
			PlatformRuntime.logger.error("Gamepad detected as unsupported!");
		}

		enumerateGamepads();
	}

	@JSFunctor
	private static interface KeyboardLayoutIterator extends JSObject {
		void call(String key, String val);
	}

	@JSFunctor
	private static interface KeyboardLayoutDone extends JSObject {
		void call();
	}

	@JSBody(params = { "cb", "cbDone" }, script = "return navigator.keyboard.getLayoutMap()"
			+ ".then(function(layoutMap) { if(layoutMap && layoutMap.forEach) layoutMap.forEach(cb); cbDone(); })"
			+ ".catch(function() { cbDone(); });")
	private static native void iterateKeyboardLayout0(KeyboardLayoutIterator cb, KeyboardLayoutDone cbDone);

	@Async
	private static native void iterateKeyboardLayout(KeyboardLayoutIterator cb);

	private static void iterateKeyboardLayout(KeyboardLayoutIterator cb, final AsyncCallback<Void> complete) {
		iterateKeyboardLayout0(cb, () -> complete.complete(null));
	}

	@JSBody(params = { }, script = "return !!(navigator.keyboard && navigator.keyboard.getLayoutMap);")
	private static native boolean checkKeyboardLayoutSupported();

	@JSBody(params = { "doc" }, script = "return (typeof doc.exitPointerLock === \"function\") ? 1"
			+ ": ((typeof doc.mozExitPointerLock === \"function\") ? 2 : -1);")
	private static native int getSupportedPointerLock(HTMLDocument doc);

	@JSBody(params = { "doc" }, script = "return (typeof doc.exitFullscreen === \"function\") ? 1"
			+ ": ((typeof doc.webkitExitFullscreen === \"function\") ? 2"
			+ ": ((typeof doc.mozExitFullscreen === \"function\") ? 3 : -1));")
	private static native int getSupportedFullScreen(HTMLDocument doc);

	@JSBody(params = { "evt" }, script = "return (typeof evt.key === \"string\");")
	private static native boolean hasKeyVar(KeyboardEvent evt);

	@JSBody(params = { "evt" }, script = "return (typeof evt.code === \"string\");")
	private static native boolean hasCodeVar(KeyboardEvent evt);

	@JSBody(params = { "evt" }, script = "return evt.keyIdentifier;")
	private static native String getKeyIdentifier(KeyboardEvent evt);

	@JSBody(params = { "fallback" }, script = "if(window.navigator.userActivation){return window.navigator.userActivation.hasBeenActive;}else{return fallback;}")
	public static native boolean hasBeenActiveTeaVM(boolean fallback);
	
	@JSBody(params = { "m", "off" }, script = "return (typeof m.offsetX === \"number\") ? m.offsetX : (m.pageX - off);")
	private static native int getOffsetX(MouseEvent m, int offX);
	
	@JSBody(params = { "m", "off" }, script = "return (typeof m.offsetY === \"number\") ? m.offsetY : (m.pageY - off);")
	private static native int getOffsetY(MouseEvent m, int offY);
	
	@JSBody(params = { "e" }, script = "return (typeof e.which === \"number\") ? e.which : ((typeof e.keyCode === \"number\") ? e.keyCode : 0);")
	private static native int getWhich(KeyboardEvent e);

	@JSBody(params = { "e" }, script = "return (typeof e.location === \"number\") ? e.location : 0;")
	private static native int getLocationSafe(KeyboardEvent e);

	@JSBody(params = { "el", "i", "j" }, script = "el.setSelectionRange(i, j)")
	private static native boolean setSelectionRange(HTMLElement el, int i, int j);

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

	@JSBody(params = { "doc" }, script = "return (typeof doc.visibilityState !== \"string\") || (doc.visibilityState === \"visible\");")
	private static native boolean getVisibilityState(JSObject doc);

	@JSBody(params = { "win" }, script = "return (typeof win.devicePixelRatio === \"number\") ? win.devicePixelRatio : 1.0;")
	static native double getDevicePixelRatio(Window win);

	public static void update() {
		update(0);
	}

	private static double syncTimer = 0.0;

	public static void update(int fpsLimit) {
		double r = getDevicePixelRatio(win);
		if(r < 0.01) r = 1.0;
		windowDPI = (float)r;
		updateTouchOffset();
		int w = parent.getClientWidth();
		int h = parent.getClientHeight();
		int w2 = windowWidth = (int)(w * r);
		int h2 = windowHeight = (int)(h * r);
		if(PlatformRuntime.useVisualViewport) {
			VisualViewport vv = PlatformRuntime.getVisualViewport();
			double scale = vv.getScale();
			visualViewportX = (int)(vv.getPageLeft() * r * scale);
			visualViewportY = (int)(vv.getPageTop() * r * scale);
			visualViewportW = (int)(vv.getWidth() * r * scale);
			visualViewportH = (int)(vv.getHeight() * r * scale);
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
		}else {
			visualViewportX = 0;
			visualViewportY = 0;
			visualViewportW = w2;
			visualViewportH = h2;
		}
		if(canvas.getWidth() != w2) {
			canvas.setWidth(w2);
		}
		if(canvas.getHeight() != h2) {
			canvas.setHeight(h2);
		}
		WebGLBackBuffer.flipBuffer(w2, h2);
		PlatformScreenRecord.captureFrameHook();
		if(getVisibilityState(win.getDocument())) {
			if(vsyncSupport && vsync) {
				syncTimer = 0.0;
				asyncRequestAnimationFrame();
			}else {
				if(fpsLimit <= 0 || fpsLimit > 1000) {
					syncTimer = 0.0;
					PlatformRuntime.swapDelayTeaVM();
				}else {
					double frameMillis = (1000.0 / fpsLimit);
					if(syncTimer == 0.0) {
						syncTimer = PlatformRuntime.steadyTimeMillisTeaVM() + frameMillis;
						PlatformRuntime.swapDelayTeaVM();
					}else {
						double millis = PlatformRuntime.steadyTimeMillisTeaVM();
						int remaining = (int)(syncTimer - millis);
						if(remaining > 0) {
							if(!PlatformRuntime.useDelayOnSwap && PlatformRuntime.immediateContinueSupport) {
								PlatformRuntime.immediateContinue(); // cannot stack setTimeouts, or it will throttle
								millis = PlatformRuntime.steadyTimeMillisTeaVM();
								remaining = (int)(syncTimer - millis);
								if(remaining > 0) {
									PlatformRuntime.sleep((int)remaining);
								}
							}else {
								PlatformRuntime.sleep((int)remaining);
							}
						}else {
							PlatformRuntime.swapDelayTeaVM();
						}
						millis = PlatformRuntime.steadyTimeMillisTeaVM();
						if((syncTimer += frameMillis) < millis) {
							syncTimer = millis;
						}
					}
				}
			}
		}else {
			syncTimer = 0.0;
			PlatformRuntime.sleep(50);
		}
	}

	@Async
	private static native void asyncRequestAnimationFrame();

	private static void asyncRequestAnimationFrame(AsyncCallback<Void> cb) {
		if(vsyncWaiting != -1l) {
			cb.error(new IllegalStateException("Already waiting for vsync!"));
			return;
		}
		vsyncWaiting = PlatformRuntime.steadyTimeMillis();
		vsyncAsyncCallback = cb;
		final boolean[] hasTimedOut = new boolean[] { false };
		final int[] timeout = new int[] { -1 };
		Window.requestAnimationFrame((d) -> {
			if(!hasTimedOut[0]) {
				hasTimedOut[0] = true;
				if(vsyncWaiting != -1l) {
					vsyncWaiting = -1l;
					if(vsyncTimeout != -1 && vsyncTimeout == timeout[0]) {
						try {
							Window.clearTimeout(vsyncTimeout);
						}catch(Throwable t) {
						}
						vsyncTimeout = -1;
					}
					vsyncAsyncCallback = null;
					cb.complete(null);
				}
			}
		});
		vsyncTimeout = timeout[0] = Window.setTimeout(() -> {
			if(!hasTimedOut[0]) {
				hasTimedOut[0] = true;
				if(vsyncWaiting != -1l) {
					vsyncTimeout = -1;
					vsyncWaiting = -1l;
					vsyncAsyncCallback = null;
					cb.complete(null);
				}
			}
		}, 50);
	}

	public static boolean isVSyncSupported() {
		return vsyncSupport;
	}

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
		synchronized(keyEvents) {
			if(unpressCTRL) { //un-press ctrl after copy/paste permission
				keyEvents.clear();
				currentEventK = null;
				keyStates[29] = false;
				keyStates[157] = false;
				keyStates[28] = false;
				keyStates[219] = false;
				keyStates[220] = false;
				unpressCTRL = false;
				return false;
			}
			currentEventK = null;
			return !keyEvents.isEmpty() && (currentEventK = keyEvents.remove(0)) != null;
		}
	}

	public static void keyboardFireEvent(EnumFireKeyboardEvent eventType, int eagKey, char keyChar) {
		synchronized(keyEvents) {
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
			if(keyEvents.size() > 64) {
				keyEvents.remove(0);
			}
		}
	}

	public static boolean keyboardGetEventKeyState() {
		return currentEventK == null ? false : (currentEventK.type != EVENT_KEY_UP);
	}

	public static int keyboardGetEventKey() {
		return currentEventK == null ? -1 : currentEventK.eagKey;
	}

	@JSBody(params = { "evt" }, script = "return (typeof evt.key === \"string\") ? evt.key : \"\";")
	private static native String getCharOrNull(KeyboardEvent evt);

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
		return currentEventK == null ? '\0' : currentEventK.keyChar;
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
		return currentEventK == null ? false : (currentEventK.type == EVENT_KEY_REPEAT);
	}

	public static void keyboardEnableRepeatEvents(boolean b) {
		enableRepeatEvents = b;
	}

	public static boolean keyboardAreKeysLocked() {
		return lockKeys;
	}

	public static boolean mouseNext() {
		currentEvent = null;
		synchronized(mouseEvents) {
			return !mouseEvents.isEmpty() && (currentEvent = mouseEvents.remove(0)) != null;
		}
	}

	public static void mouseFireMoveEvent(EnumFireMouseEvent eventType, int posX, int posY) {
		if(eventType == EnumFireMouseEvent.MOUSE_MOVE) {
			synchronized(mouseEvents) {
				mouseEvents.add(new VMouseEvent(posX, posY, -1, 0.0f, EVENT_MOUSE_MOVE));
				if(mouseEvents.size() > 64) {
					mouseEvents.remove(0);
				}
			}
		}else {
			throw new UnsupportedOperationException();
		}
	}

	public static void mouseFireButtonEvent(EnumFireMouseEvent eventType, int posX, int posY, int button) {
		synchronized(mouseEvents) {
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
			if(mouseEvents.size() > 64) {
				mouseEvents.remove(0);
			}
		}
	}

	public static void mouseFireWheelEvent(EnumFireMouseEvent eventType, int posX, int posY, float wheel) {
		if(eventType == EnumFireMouseEvent.MOUSE_WHEEL) {
			synchronized(mouseEvents) {
				mouseEvents.add(new VMouseEvent(posX, posY, -1, wheel, EVENT_MOUSE_WHEEL));
				if(mouseEvents.size() > 64) {
					mouseEvents.remove(0);
				}
			}
		}else {
			throw new UnsupportedOperationException();
		}
	}

	public static boolean mouseGetEventButtonState() {
		return currentEvent == null ? false : (currentEvent.type == EVENT_MOUSE_DOWN);
	}

	public static int mouseGetEventButton() {
		if(currentEvent == null || (currentEvent.type == EVENT_MOUSE_MOVE)) return -1;
		return currentEvent.button;
	}

	public static int mouseGetEventX() {
		return currentEvent == null ? -1 : currentEvent.posX;
	}

	public static int mouseGetEventY() {
		return currentEvent == null ? -1 : currentEvent.posY;
	}

	public static int mouseGetEventDWheel() {
		return (currentEvent.type == EVENT_MOUSE_WHEEL) ? fixWheel(currentEvent.wheel) : 0;
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

	public static void mouseSetGrabbed(boolean grab) {
		if(pointerLockSupported == POINTER_LOCK_NONE) {
			return;
		}
		long t = PlatformRuntime.steadyTimeMillis();
		pointerLockFlag = grab;
		mouseGrabTimer = t;
		if(grab) {
			pointerLockWaiting = true;
			callRequestPointerLock(canvas);
			if(mouseUngrabTimeout != -1) Window.clearTimeout(mouseUngrabTimeout);
			mouseUngrabTimeout = -1;
			if(t - mouseUngrabTimer < 3000l) {
				mouseUngrabTimeout = Window.setTimeout(new TimerHandler() {
					@Override
					public void onTimer() {
						callRequestPointerLock(canvas);
					}
				}, 3100 - (int)(t - mouseUngrabTimer));
			}
		}else {
			if(mouseUngrabTimeout != -1) Window.clearTimeout(mouseUngrabTimeout);
			mouseUngrabTimeout = -1;
			if(!pointerLockWaiting) {
				callExitPointerLock(win.getDocument());
			}
		}
		mouseDX = 0.0D;
		mouseDY = 0.0D;
	}

	private static boolean tryGrabCursorHook() {
		if(pointerLockSupported == POINTER_LOCK_NONE) {
			return false;
		}
		if(pointerLockFlag && !isPointerLocked()) {
			mouseSetGrabbed(true);
			return true;
		}
		return false;
	}

	private static void callRequestPointerLock(HTMLElement el) {
		switch(pointerLockSupported) {
		case POINTER_LOCK_CORE:
			try {
				el.requestPointerLock();
			}catch(Throwable t) {
			}
			break;
		case POINTER_LOCK_MOZ:
			try {
				mozRequestPointerLock(el);
			}catch(Throwable t) {
			}
			break;
		default:
			PlatformRuntime.logger.warn("Failed to request pointer lock, it is not supported!");
			break;
		}
	}

	@JSBody(params = { "el" }, script = "el.mozRequestPointerLock();")
	private static native void mozRequestPointerLock(HTMLElement el);

	private static void callExitPointerLock(HTMLDocument doc) {
		switch(pointerLockSupported) {
		case POINTER_LOCK_CORE:
			try {
				doc.exitPointerLock();
			}catch(Throwable t) {
			}
			break;
		case POINTER_LOCK_MOZ:
			try {
				mozExitPointerLock(doc);
			}catch(Throwable t) {
			}
			break;
		default:
			PlatformRuntime.logger.warn("Failed to exit pointer lock, it is not supported!");
			break;
		}
	}

	@JSBody(params = { "doc" }, script = "doc.mozExitPointerLock();")
	private static native void mozExitPointerLock(HTMLDocument el);

	public static boolean mouseGrabSupported() {
		return pointerLockSupported != POINTER_LOCK_NONE;
	}

	public static boolean isMouseGrabbed() {
		return pointerLockFlag;
	}

	public static boolean isPointerLocked() {
		if(pointerLockWaiting) return true; // workaround for chrome bug
		return isPointerLockedImpl();
	}

	private static boolean isPointerLockedImpl() {
		switch(pointerLockSupported) {
		case POINTER_LOCK_CORE:
			return isPointerLocked0(win.getDocument(), canvas);
		case POINTER_LOCK_MOZ:
			return isMozPointerLocked0(win.getDocument(), canvas);
		default:
			return false;
		}
	}

	@JSBody(params = { "doc", "canvasEl" }, script = "return doc.pointerLockElement === canvasEl;")
	private static native boolean isPointerLocked0(HTMLDocument doc, HTMLCanvasElement canvasEl);

	@JSBody(params = { "doc", "canvasEl" }, script = "return doc.mozPointerLockElement === canvasEl;")
	private static native boolean isMozPointerLocked0(HTMLDocument doc, HTMLCanvasElement canvasEl);

	public static int mouseGetDX() {
		int ret = (int)mouseDX;
		mouseDX = 0.0D;
		return ret;
	}

	public static int mouseGetDY() {
		int ret = (int)mouseDY;
		mouseDY = 0.0D;
		return ret;
	}

	public static void mouseSetCursorPosition(int x, int y) {
		// obsolete
	}

	public static boolean mouseIsInsideWindow() {
		return isMouseOverWindow;
	}

	public static boolean contextLost() {
		return PlatformRuntime.webgl.isContextLost();
	}
	
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

	public static void removeEventHandlers() {
		if(contextmenu != null) {
			parent.removeEventListener("contextmenu", contextmenu);
			contextmenu = null;
		}
		if(mousedown != null) {
			canvas.removeEventListener("mousedown", mousedown);
			mousedown = null;
		}
		if(mouseup != null) {
			canvas.removeEventListener("mouseup", mouseup);
			mouseup = null;
		}
		if(mousemove != null) {
			canvas.removeEventListener("mousemove", mousemove);
			mousemove = null;
		}
		if(mouseenter != null) {
			canvas.removeEventListener("mouseenter", mouseenter);
			mouseenter = null;
		}
		if(mouseleave != null) {
			canvas.removeEventListener("mouseleave", mouseleave);
			mouseleave = null;
		}
		if(touchstart != null) {
			canvas.removeEventListener("touchstart", touchstart);
			touchstart = null;
		}
		if(touchmove != null) {
			canvas.removeEventListener("touchmove", touchmove);
			touchmove = null;
		}
		if(touchend != null) {
			canvas.removeEventListener("touchend", touchend);
			touchend = null;
		}
		if(touchcancel != null) {
			canvas.removeEventListener("touchcancel", touchcancel);
			touchcancel = null;
		}
		if(gamepadconnected != null) {
			win.removeEventListener("gamepadconnected", gamepadconnected);
			gamepadconnected = null;
		}
		if(gamepaddisconnected != null) {
			win.removeEventListener("gamepaddisconnected", gamepaddisconnected);
			gamepaddisconnected = null;
		}
		if(keydown != null) {
			win.removeEventListener("keydown", keydown);
			keydown = null;
		}
		if(keyup != null) {
			win.removeEventListener("keyup", keyup);
			keyup = null;
		}
		if(focus != null) {
			win.removeEventListener("focus", focus);
			focus = null;
		}
		if(blur != null) {
			win.removeEventListener("blur", blur);
			blur = null;
		}
		if(wheel != null) {
			canvas.removeEventListener("wheel", wheel);
			wheel = null;
		}
		if(pointerlock != null) {
			win.getDocument().removeEventListener("pointerlockchange", pointerlock);
			pointerlock = null;
		}
		if(pointerlockerr != null) {
			win.getDocument().removeEventListener("pointerlockerror", pointerlockerr);
			pointerlockerr = null;
		}
		if(fullscreen != null) {
			TeaVMUtils.removeEventListener(fullscreenQuery, "change", fullscreen);
			fullscreen = null;
		}
		if(mouseUngrabTimeout != -1) {
			Window.clearTimeout(mouseUngrabTimeout);
			mouseUngrabTimeout = -1;
		}
		if(vsyncSaveLockInterval != -1) {
			try {
				Window.clearInterval(vsyncSaveLockInterval);
			}catch(Throwable t) {
			}
			vsyncSaveLockInterval = -1;
		}
		if(touchKeyboardField != null) {
			touchKeyboardField.blur();
			if(parent != null) {
				parent.removeChild(touchKeyboardField);
			}
			touchKeyboardField = null;
		}
		if(touchKeyboardOpenZone != null) {
			if(touchKeyboardOpenZone_touchstart != null) {
				touchKeyboardOpenZone.removeEventListener("touchstart", touchKeyboardOpenZone_touchstart);
				touchKeyboardOpenZone_touchstart = null;
			}
			if(touchKeyboardOpenZone_touchend != null) {
				touchKeyboardOpenZone.removeEventListener("touchend", touchKeyboardOpenZone_touchend);
				touchKeyboardOpenZone_touchend = null;
			}
			if(touchKeyboardOpenZone_touchmove != null) {
				touchKeyboardOpenZone.removeEventListener("touchmove", touchKeyboardOpenZone_touchmove);
				touchKeyboardOpenZone_touchmove = null;
			}
			if(parent != null) {
				parent.removeChild(touchKeyboardOpenZone);
			}
			touchKeyboardOpenZone = null;
		}
		try {
			callExitPointerLock(win.getDocument());
		}catch(Throwable t) {
		}
		ClientMain.removeErrorHandler(win);
	}

	public static void pressAnyKeyScreen() {
		IClientConfigAdapter cfgAdapter = PlatformRuntime.getClientConfigAdapter();
		boolean allowBootMenu = cfgAdapter.isAllowBootMenu();
		if(isLikelyMobileBrowser) {
			EarlyLoadScreen.paintEnable(PlatformOpenGL.checkVAOCapable(), allowBootMenu);
			try {
				isOnMobilePressAnyKey = true;
				setupAnyKeyScreenMobile(allowBootMenu);
				if(pressAnyKeyScreenMobile() && allowBootMenu) {
					PlatformRuntime.enterBootMenu(true);
				}
			}finally {
				isOnMobilePressAnyKey = false;
			}
		}else {
			if(mouseEvents.isEmpty() && keyEvents.isEmpty() && !hasBeenActiveTeaVM(false)) {
				EarlyLoadScreen.paintEnable(PlatformOpenGL.checkVAOCapable(), allowBootMenu);
				
				while(mouseEvents.isEmpty() && keyEvents.isEmpty() && touchEvents.isEmpty()) {
					PlatformRuntime.sleep(100);
				}
			}
		}
		hasShownPressAnyKey = true;
	}

	private static void setupAnyKeyScreenMobile(boolean allowBootMenu) {
		if(mobilePressAnyKeyScreen != null) {
			parent.removeChild(mobilePressAnyKeyScreen);
		}
		mobilePressAnyKeyScreen = win.getDocument().createElement("div");
		mobilePressAnyKeyScreen.getClassList().add("_eaglercraftX_mobile_press_any_key");
		mobilePressAnyKeyScreen.setAttribute("style", "position:absolute;background-color:white;font-family:sans-serif;top:10%;left:10%;right:10%;bottom:10%;border:5px double black;padding:calc(5px + 7vh) 15px;text-align:center;font-size:20px;user-select:none;z-index:10;");
		mobilePressAnyKeyScreen.setInnerHTML("<h3 style=\"margin-block-start:0px;margin-block-end:0px;-webkit-margin-before:0px;-webkit-margin-after:0px;margin:20px 5px;\">Mobile Browser Detected</h3>"
				+ "<p style=\"margin-block-start:0px;margin-block-end:0px;-webkit-margin-before:0px;-webkit-margin-after:0px;margin:20px 5px;\">You must manually select an option below to continue</p>"
				+ "<p style=\"margin-block-start:0px;margin-block-end:0px;-webkit-margin-before:0px;-webkit-margin-after:0px;margin:20px 2px;\"><button style=\"font: 24px sans-serif;font-weight:bold;\" class=\"_eaglercraftX_mobile_launch_client\">Launch EaglercraftX</button></p>"
				+ (allowBootMenu ? "<p style=\"margin-block-start:0px;margin-block-end:0px;-webkit-margin-before:0px;-webkit-margin-after:0px;margin:20px 2px;\"><button style=\"font: 24px sans-serif;\" class=\"_eaglercraftX_mobile_enter_boot_menu\">Enter Boot Menu</button></p>" : "")
				+ "<p style=\"margin-block-start:0px;margin-block-end:0px;-webkit-margin-before:0px;-webkit-margin-after:0px;margin:25px 5px;\">(Tablets and phones with large screens work best)</p>");
		mobilePressAnyKeyScreen.querySelector("._eaglercraftX_mobile_launch_client").addEventListener("click", new EventListener<Event>() {
			@Override
			public void handleEvent(Event evt) {
				if(isOnMobilePressAnyKey && mobilePressAnyKey != null) {
					mobilePressAnyKey.call(false);
				}
			}
		});
		if(allowBootMenu) {
			mobilePressAnyKeyScreen.querySelector("._eaglercraftX_mobile_enter_boot_menu").addEventListener("click", new EventListener<Event>() {
				@Override
				public void handleEvent(Event evt) {
					if(isOnMobilePressAnyKey && mobilePressAnyKey != null) {
						mobilePressAnyKey.call(true);
					}
				}
			});
		}
		parent.appendChild(mobilePressAnyKeyScreen);
	}

	@Async
	private static native boolean pressAnyKeyScreenMobile();

	private static void pressAnyKeyScreenMobile(final AsyncCallback<Boolean> complete) {
		mobilePressAnyKey = new MobilePressAnyKeyHandler() {
			@Override
			public void call(boolean enterBootMenu) {
				mobilePressAnyKey = null;
				if(mobilePressAnyKeyScreen != null && parent != null) {
					parent.removeChild(mobilePressAnyKeyScreen);
				}
				mobilePressAnyKeyScreen = null;
				complete.complete(enterBootMenu);
			}
		};
		PlatformRuntime.logger.info("Waiting for user to select option on mobile press any key screen");
	}

	public static void clearEvenBuffers() {
		mouseEvents.clear();
		keyEvents.clear();
		touchEvents.clear();
		net.lax1dude.eaglercraft.v1_8.Gamepad.clearEventBuffer();
	}

	@JSBody(params = {}, script = "return window.matchMedia(\"(display-mode: fullscreen)\");")
	private static native JSObject fullscreenMediaQuery();

	@JSBody(params = { "mediaQuery" }, script = "return mediaQuery.matches;")
	private static native boolean mediaQueryMatches(JSObject mediaQuery);

	public static boolean supportsFullscreen() {
		return fullscreenSupported != FULLSCREEN_NONE;
	}

	public static void toggleFullscreen() {
		if(fullscreenSupported == FULLSCREEN_NONE) return;
		if (isFullscreen()) {
			if (keyboardLockSupported) {
				unlockKeys();
				lockKeys = false;
			}
			callExitFullscreen(win.getDocument());
		} else {
			if (keyboardLockSupported) {
				lockKeys();
				lockKeys = true;
			}
			callRequestFullscreen(canvas);
		}
	}

	public static boolean isFullscreen() {
		return fullscreenSupported != FULLSCREEN_NONE && mediaQueryMatches(fullscreenQuery);
	}

	@JSBody(params = { }, script = "navigator.keyboard.lock();")
	private static native void lockKeys();

	@JSBody(params = { }, script = "navigator.keyboard.unlock();")
	private static native void unlockKeys();

	@JSBody(params = { }, script = "return !!(navigator.keyboard && navigator.keyboard.lock);")
	private static native boolean checkKeyboardLockSupported();

	private static void callRequestFullscreen(HTMLElement el) {
		switch(fullscreenSupported) {
		case FULLSCREEN_CORE:
			try {
				requestFullscreen(el);
			}catch(Throwable t) {
			}
			break;
		case FULLSCREEN_WEBKIT:
			try {
				webkitRequestFullscreen(el);
			}catch(Throwable t) {
			}
			break;
		case FULLSCREEN_MOZ:
			try {
				mozRequestFullscreen(el);
			}catch(Throwable t) {
			}
			break;
		default:
			PlatformRuntime.logger.warn("Failed to request fullscreen, it is not supported!");
			break;
		}
	}

	@JSBody(params = { "el" }, script = "el.requestFullscreen();")
	private static native void requestFullscreen(HTMLElement element);

	@JSBody(params = { "el" }, script = "el.webkitRequestFullscreen();")
	private static native void webkitRequestFullscreen(HTMLElement element);

	@JSBody(params = { "el" }, script = "el.mozRequestFullScreen();")
	private static native void mozRequestFullscreen(HTMLElement element);

	private static void callExitFullscreen(HTMLDocument doc) {
		switch(fullscreenSupported) {
		case FULLSCREEN_CORE:
			try {
				exitFullscreen(doc);
			}catch(Throwable t) {
			}
			break;
		case FULLSCREEN_WEBKIT:
			try {
				webkitExitFullscreen(doc);
			}catch(Throwable t) {
			}
			break;
		case FULLSCREEN_MOZ:
			try {
				mozCancelFullscreen(doc);
			}catch(Throwable t) {
			}
			break;
		default:
			PlatformRuntime.logger.warn("Failed to exit fullscreen, it is not supported!");
			break;
		}
	}

	@JSBody(params = { "doc" }, script = "doc.exitFullscreen();")
	private	static native void exitFullscreen(HTMLDocument doc);

	@JSBody(params = { "doc" }, script = "doc.webkitExitFullscreen();")
	private	static native void webkitExitFullscreen(HTMLDocument doc);

	@JSBody(params = { "doc" }, script = "doc.mozCancelFullscreen();")
	private	static native void mozCancelFullscreen(HTMLDocument doc);

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
		return currentTouchEvent != null ? currentTouchEvent.getEventTouches().get(pointId).posX : 0;
	}

	public static int touchGetEventTouchY(int pointId) {
		return currentTouchEvent != null ? currentTouchEvent.getEventTouches().get(pointId).posY : 0;
	}

	public static float touchGetEventTouchRadiusMixed(int pointId) {
		if(currentTouchEvent != null) {
			return currentTouchEvent.getEventTouches().get(pointId).radius;
		}else {
			return 1.0f;
		}
	}

	public static float touchGetEventTouchForce(int pointId) {
		return currentTouchEvent != null ? (float)currentTouchEvent.getEventTouches().get(pointId).touch.getForceSafe(0.5) : 0.0f;
	}

	public static int touchGetEventTouchPointUID(int pointId) {
		return currentTouchEvent != null ? currentTouchEvent.getEventTouches().get(pointId).eventUID : -1;
	}

	public static int touchPointCount() {
		return currentTouchState != null ? currentTouchState.getTargetTouchesSize() : 0;
	}

	public static int touchPointX(int pointId) {
		return currentTouchState != null ? currentTouchState.getTargetTouches().get(pointId).posX : -1;
	}

	public static int touchPointY(int pointId) {
		return currentTouchState != null ? currentTouchState.getTargetTouches().get(pointId).posY : -1;
	}

	public static float touchRadiusX(int pointId) {
		return currentTouchState != null ? (float)currentTouchState.getTargetTouches().get(pointId).touch.getRadiusXSafe(5.0 * windowDPI) : 1.0f;
	}

	public static float touchRadiusY(int pointId) {
		return currentTouchState != null ? (float)currentTouchState.getTargetTouches().get(pointId).touch.getRadiusYSafe(5.0 * windowDPI) : 1.0f;
	}

	public static float touchRadiusMixed(int pointId) {
		if(currentTouchState != null) {
			return currentTouchState.getTargetTouches().get(pointId).radius;
		}else {
			return 1.0f;
		}
	}

	public static float touchForce(int pointId) {
		return currentTouchState != null ? (float)currentTouchState.getTargetTouches().get(pointId).touch.getForceSafe(0.5) : 0.0f;
	}

	public static int touchPointUID(int pointId) {
		return currentTouchState != null ? currentTouchState.getTargetTouches().get(pointId).eventUID : -1;
	}

	private static final Map<Integer,Integer> touchIDtoUID = new HashMap<>();
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

	// Note: this can't be called from the main loop, don't try
	private static void touchOpenDeviceKeyboard() {
		if(!touchIsDeviceKeyboardOpenMAYBE()) {
			if(touchKeyboardField != null) {
				touchKeyboardField.blur();
				touchKeyboardField.setValue("");
				PlatformRuntime.sleep(10);
				if(touchKeyboardForm != null) {
					touchKeyboardForm.removeChild(touchKeyboardField);
				}else {
					touchKeyboardField.delete();
				}
				touchKeyboardField = null;
				if(touchKeyboardForm != null) {
					parent.removeChild(touchKeyboardForm);
					touchKeyboardForm = null;
				}
				return;
			}
			if(touchKeyboardForm != null) {
				parent.removeChild(touchKeyboardForm);
				touchKeyboardForm = null;
			}
			touchKeyboardForm = (HTMLFormElement) win.getDocument().createElement("form");
			touchKeyboardForm.setAttribute("autocomplete", "off");
			touchKeyboardForm.getClassList().add("_eaglercraftX_text_input_wrapper");
			CSSStyleDeclaration decl = touchKeyboardForm.getStyle();
			decl.setProperty("position", "absolute");
			decl.setProperty("top", "0px");
			decl.setProperty("left", "0px");
			decl.setProperty("right", "0px");
			decl.setProperty("bottom", "0px");
			decl.setProperty("z-index", "-100");
			decl.setProperty("margin", "0px");
			decl.setProperty("padding", "0px");
			decl.setProperty("border", "none");
			touchKeyboardForm.addEventListener("submit", new EventListener<Event>() {
				@Override
				public void handleEvent(Event evt) {
					evt.preventDefault();
					evt.stopPropagation();
					JSObject obj = evt.getTimeStamp();
					if(obj != null && TeaVMUtils.isTruthy(obj)) {
						double d = ((JSNumber)obj).doubleValue();
						if(lastTouchKeyboardEvtA != 0.0 && (d - lastTouchKeyboardEvtA) < 10.0) {
							return;
						}
						if(lastTouchKeyboardEvtB != 0.0 && (d - lastTouchKeyboardEvtB) < 10.0) {
							return;
						}
						if(lastTouchKeyboardEvtC != 0.0 && (d - lastTouchKeyboardEvtC) < 10.0) {
							return;
						}
						if(!showniOSReturnTouchKeyboardWarning) {
							PlatformRuntime.logger.info("Note: Generating return keystroke from submit event on form, this browser probably doesn't generate keydown/beforeinput/input when enter/return is pressed on the on-screen keyboard");
							showniOSReturnTouchKeyboardWarning = true;
						}
						keyboardFireEvent(EnumFireKeyboardEvent.KEY_DOWN, KeyboardConstants.KEY_RETURN, '\n');
						keyboardFireEvent(EnumFireKeyboardEvent.KEY_UP, KeyboardConstants.KEY_RETURN, '\n');
					}
					
				}
			});
			touchKeyboardField = (HTMLInputElement) win.getDocument().createElement("input");
			touchKeyboardField.setType("password");
			touchKeyboardField.setValue(" ");
			touchKeyboardField.getClassList().add("_eaglercraftX_text_input_element");
			touchKeyboardField.setAttribute("autocomplete", "off");
			decl = touchKeyboardField.getStyle();
			decl.setProperty("position", "absolute");
			decl.setProperty("top", "0px");
			decl.setProperty("left", "0px");
			decl.setProperty("right", "0px");
			decl.setProperty("bottom", "0px");
			decl.setProperty("z-index", "-100");
			decl.setProperty("margin", "0px");
			decl.setProperty("padding", "0px");
			decl.setProperty("border", "none");
			decl.setProperty("-webkit-touch-callout", "default");
			touchKeyboardField.addEventListener("beforeinput", new EventListener<InputEvent>() {
				@Override
				public void handleEvent(InputEvent evt) {
					if(touchKeyboardField != evt.getTarget()) return;
					if(!shownTouchKeyboardEventWarning) {
						PlatformRuntime.logger.info("Note: Caught beforeinput event from on-screen keyboard, browser probably does not generate global keydown/keyup events on text fields, or does not respond to cancelling keydown");
						shownTouchKeyboardEventWarning = true;
					}
					JSObject obj = evt.getTimeStamp();
					if(obj != null && TeaVMUtils.isTruthy(obj)) {
						double d = ((JSNumber)obj).doubleValue();
						if(lastTouchKeyboardEvtA != 0.0 && (d - lastTouchKeyboardEvtA) < 10.0) {
							return;
						}
						lastTouchKeyboardEvtB = d;
					}
					evt.preventDefault();
					evt.stopPropagation();
					switch(evt.getInputType()) {
					case "insertParagraph":
					case "insertLineBreak":
						keyboardFireEvent(EnumFireKeyboardEvent.KEY_DOWN, KeyboardConstants.KEY_RETURN, '\n');
						keyboardFireEvent(EnumFireKeyboardEvent.KEY_UP, KeyboardConstants.KEY_RETURN, '\n');
						break;
					case "deleteWordBackward":
					case "deleteSoftLineBackward":
					case "deleteHardLineBackward":
					case "deleteEntireSoftLine":
					case "deleteContentBackward":
					case "deleteContent":
						keyboardFireEvent(EnumFireKeyboardEvent.KEY_DOWN, KeyboardConstants.KEY_BACK, '\0');
						keyboardFireEvent(EnumFireKeyboardEvent.KEY_UP, KeyboardConstants.KEY_BACK, '\0');
						break;
					case "deleteWordForward":
					case "deleteSoftLineForward":
					case "deleteHardLineForward":
					case "deleteContentForward":
						keyboardFireEvent(EnumFireKeyboardEvent.KEY_DOWN, KeyboardConstants.KEY_DELETE, '\0');
						keyboardFireEvent(EnumFireKeyboardEvent.KEY_UP, KeyboardConstants.KEY_DELETE, '\0');
						break;
					case "insertText":
					case "insertCompositionText":
					case "insertReplacementText":
						String eventsToGenerate = evt.getData();
						for(int i = 0, l = eventsToGenerate.length(); i < l; ++i) {
							char c = eventsToGenerate.charAt(i);
							int eag = KeyboardConstants.getEaglerKeyFromBrowser(asciiUpperToKeyLegacy(Character.toUpperCase(c)), 0);
							keyboardFireEvent(EnumFireKeyboardEvent.KEY_DOWN, eag, c);
							keyboardFireEvent(EnumFireKeyboardEvent.KEY_UP, eag, c);
						}
						break;
					case "insertFromPaste":
					case "insertFromPasteAsQuotation":
					case "insertFromDrop":
					case "insertFromYank":
					case "insertLink":
						synchronized(pastedStrings) {
							pastedStrings.add(evt.getData());
							if(pastedStrings.size() > 64) {
								pastedStrings.remove(0);
							}
						}
						break;
					case "historyUndo":
					case "historyRedo":
					case "deleteByDrag":
					case "deleteByCut":
						break;
					default:
						PlatformRuntime.logger.info("Ignoring InputEvent.inputType \"{}\" from on-screen keyboard", evt.getInputType());
						break;
					}
				}
			});
			touchKeyboardField.addEventListener("input", new EventListener<Event>() {
				@Override
				public void handleEvent(Event evt) {
					if(touchKeyboardField != evt.getTarget()) return;
					JSObject obj = evt.getTimeStamp();
					if(!shownLegacyTouchKeyboardWarning) {
						PlatformRuntime.logger.info("Note: Caught legacy input events from on-screen keyboard, browser could be outdated and doesn't support beforeinput event, or does not respond to cancelling beforeinput");
						shownLegacyTouchKeyboardWarning = true;
					}
					if(obj != null && TeaVMUtils.isTruthy(obj)) {
						double d = ((JSNumber)obj).doubleValue();
						if(lastTouchKeyboardEvtA != 0.0 && (d - lastTouchKeyboardEvtA) < 10.0) {
							return;
						}
						if(lastTouchKeyboardEvtB != 0.0 && (d - lastTouchKeyboardEvtB) < 10.0) {
							return;
						}
						lastTouchKeyboardEvtC = d;
					}
					String val = touchKeyboardField.getValue();
					int l = val.length();
					if(l == 0) {
						keyboardFireEvent(EnumFireKeyboardEvent.KEY_DOWN, KeyboardConstants.KEY_BACK, '\0');
						keyboardFireEvent(EnumFireKeyboardEvent.KEY_UP, KeyboardConstants.KEY_BACK, '\0');
					}else if(l == 1) {
						char c = val.charAt(0);
						int eag = KeyboardConstants.getEaglerKeyFromBrowser(asciiUpperToKeyLegacy(Character.toUpperCase(c)), 0);
						keyboardFireEvent(EnumFireKeyboardEvent.KEY_DOWN, eag, c);
						keyboardFireEvent(EnumFireKeyboardEvent.KEY_UP, eag, c);
					}else {
						val = val.trim();
						l = val.length();
						if(l == 0) {
							keyboardFireEvent(EnumFireKeyboardEvent.KEY_DOWN, KeyboardConstants.KEY_SPACE, ' ');
							keyboardFireEvent(EnumFireKeyboardEvent.KEY_UP, KeyboardConstants.KEY_SPACE, ' ');
						}else {
							char c = val.charAt(l - 1);
							int eag = KeyboardConstants.getEaglerKeyFromBrowser(asciiUpperToKeyLegacy(Character.toUpperCase(c)), 0);
							keyboardFireEvent(EnumFireKeyboardEvent.KEY_DOWN, eag, c);
							keyboardFireEvent(EnumFireKeyboardEvent.KEY_UP, eag, c);
						}
					}
					touchKeyboardField.setValue(" ");
					setSelectionRange(touchKeyboardField, 1, 1);
				}
			});
			touchKeyboardField.addEventListener("focus", new EventListener<Event>() {
				@Override
				public void handleEvent(Event evt) {
					if(touchKeyboardField != evt.getTarget()) return;
					touchKeyboardField.setValue(" ");
					setSelectionRange(touchKeyboardField, 1, 1);
				}
			});
			touchKeyboardField.addEventListener("select", new EventListener<Event>() {
				@Override
				public void handleEvent(Event evt) {
					if(touchKeyboardField != evt.getTarget()) return;
					evt.preventDefault();
					evt.stopPropagation();
					touchKeyboardField.setValue(" ");
					setSelectionRange(touchKeyboardField, 1, 1);
				}
			});
			touchKeyboardForm.appendChild(touchKeyboardField);
			parent.appendChild(touchKeyboardForm);
			touchKeyboardField.setValue(" ");
			touchKeyboardField.focus();
			touchKeyboardField.select();
			setSelectionRange(touchKeyboardField, 1, 1);
		}else {
			touchCloseDeviceKeyboard0(false);
		}
	}

	public static String touchGetPastedString() {
		synchronized(pastedStrings) {
			return pastedStrings.isEmpty() ? null : pastedStrings.remove(0);
		}
	}

	public static void touchSetOpenKeyboardZone(int x, int y, int w, int h) {
		if(w != 0 && h != 0) {
			int xx = (int)(x / windowDPI);
			int yy = (int)((windowHeight - y - h) / windowDPI);
			int ww = (int)(w / windowDPI);
			int hh = (int)(h / windowDPI);
			if(xx != touchOpenZoneX || yy != touchOpenZoneY || ww != touchOpenZoneW || hh != touchOpenZoneH) {
				CSSStyleDeclaration decl = touchKeyboardOpenZone.getStyle();
				decl.setProperty("display", "block");
				decl.setProperty("left", "" + xx + "px");
				decl.setProperty("top", "" + yy + "px");
				decl.setProperty("width", "" + ww + "px");
				decl.setProperty("height", "" + hh + "px");
				touchOpenZoneX = xx;
				touchOpenZoneY = yy;
				touchOpenZoneW = ww;
				touchOpenZoneH = hh;
			}
		}else {
			if(touchOpenZoneW != 0 || touchOpenZoneH != 0) {
				CSSStyleDeclaration decl = touchKeyboardOpenZone.getStyle();
				decl.setProperty("display", "none");
				decl.setProperty("top", "0px");
				decl.setProperty("left", "0px");
				decl.setProperty("width", "0px");
				decl.setProperty("height", "0px");
			}
			touchOpenZoneX = 0;
			touchOpenZoneY = 0;
			touchOpenZoneW = 0;
			touchOpenZoneH = 0;
		}
	}

	public static void touchCloseDeviceKeyboard() {
		touchCloseDeviceKeyboard0(true);
	}

	private static void touchCloseDeviceKeyboard0(boolean sync) {
		if(touchKeyboardField != null) {
			touchKeyboardField.blur();
			touchKeyboardField.setValue("");
			if(sync) {
				PlatformRuntime.sleep(10);
				if(touchKeyboardForm != null) {
					touchKeyboardForm.removeChild(touchKeyboardField);
				}else {
					touchKeyboardField.delete();
				}
				touchKeyboardField = null;
			}else {
				final HTMLInputElement el = touchKeyboardField;
				final HTMLFormElement el2 = touchKeyboardForm;
				Window.setTimeout(() -> {
					if(el2 != null) {
						el2.removeChild(el);
						el2.delete();
					}else {
						el.delete();
					}
				}, 10);
				touchKeyboardField = null;
				touchKeyboardForm = null;
				return;
			}
		}
		if(touchKeyboardForm != null) {
			if(parent != null) {
				parent.removeChild(touchKeyboardForm);
			}else {
				touchKeyboardForm.delete();
			}
			touchKeyboardForm = null;
		}
	}

	public static boolean touchIsDeviceKeyboardOpenMAYBE() {
		return touchKeyboardField != null && isActiveElement(win.getDocument(), touchKeyboardField);
	}

	@JSBody(params = { "doc", "el" }, script = "return doc.activeElement === el;")
	private static native boolean isActiveElement(HTMLDocument doc, HTMLElement el);

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
	private static native boolean gamepadSupported();

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

	@JSBody(params = { "el" }, script = "var xx = 0; var yy = 0;"
			+ "while(el && !isNaN(el.offsetLeft) && !isNaN(el.offsetTop)) {"
			+ "xx += el.offsetLeft - el.scrollLeft;"
			+ "yy += el.offsetTop - el.scrollTop;"
			+ "el = el.offsetParent;"
			+ "} return { left: xx, top: yy };")
	private static native TextRectangle getPositionOf(HTMLElement el);

	private static void updateTouchOffset() {
		try {
			TextRectangle bounds = getPositionOf(canvas);
			touchOffsetXTeaVM = bounds.getLeft();
			touchOffsetYTeaVM = bounds.getTop();
		}catch(Throwable t) {
			touchOffsetXTeaVM = 0;
			touchOffsetYTeaVM = 0;
		}
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
