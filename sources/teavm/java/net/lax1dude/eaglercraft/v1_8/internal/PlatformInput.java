package net.lax1dude.eaglercraft.v1_8.internal;

import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.TimerHandler;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.KeyboardEvent;
import org.teavm.jso.dom.events.MouseEvent;
import org.teavm.jso.dom.events.WheelEvent;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.webgl.WebGLFramebuffer;
import org.teavm.jso.webgl.WebGLRenderbuffer;

import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.EarlyLoadScreen;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.WebGL2RenderingContext;

import static net.lax1dude.eaglercraft.v1_8.internal.teavm.WebGL2RenderingContext.*;

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

	private static Window win = null;
	private static HTMLCanvasElement canvas = null;
	static WebGL2RenderingContext context = null;

	static WebGLFramebuffer mainFramebuffer = null;
	static WebGLRenderbuffer mainColorRenderbuffer = null;
	static WebGLRenderbuffer mainDepthRenderbuffer = null;
	private static int framebufferWidth = -1;
	private static int framebufferHeight = -1;
	
	private static EventListener contextmenu = null;
	private static EventListener mousedown = null;
	private static EventListener mouseup = null;
	private static EventListener mousemove = null;
	private static EventListener mouseenter = null;
	private static EventListener mouseleave = null;
	private static EventListener keydown = null;
	private static EventListener keyup = null;
	private static EventListener keypress = null;
	private static EventListener wheel = null;
	private static EventListener pointerlock = null;

	private static List<MouseEvent> mouseEvents = new LinkedList();
	private static List<KeyboardEvent> keyEvents = new LinkedList();

	private static int mouseX = 0;
	private static int mouseY = 0;
	private static double mouseDX = 0.0D;
	private static double mouseDY = 0.0D;
	private static double mouseDWheel = 0.0D;
	private static int width = 0;
	private static int height = 0;
	private static boolean enableRepeatEvents = true;
	private static boolean isWindowFocused = true;
	private static boolean isMouseOverWindow = true;
	static boolean unpressCTRL = false;

	private static int windowWidth = -1;
	private static int windowHeight = -1;
	private static int lastWasResizedWindowWidth = -2;
	private static int lastWasResizedWindowHeight = -2;
	
	private static MouseEvent currentEvent = null;
	private static KeyboardEvent currentEventK = null;
	private static boolean[] buttonStates = new boolean[8];
	private static boolean[] keyStates = new boolean[256];

	private static int functionKeyModifier = KeyboardConstants.KEY_F;

	private static long mouseUngrabTimer = 0l;
	private static long mouseGrabTimer = 0l;
	private static int mouseUngrabTimeout = -1;
	private static boolean pointerLockFlag = false;

	private static JSObject fullscreenQuery = null;

	public static boolean keyboardLockSupported = false;
	public static boolean lockKeys = false;
	
	@JSBody(params = { }, script = "window.onbeforeunload = () => {return false;};")
	private static native void onBeforeCloseRegister();
	
	static void initHooks(Window window, HTMLCanvasElement canvaz) {
		win = window;
		canvas = canvaz;
		
		win.addEventListener("contextmenu", contextmenu = new EventListener<MouseEvent>() {
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
				int b = evt.getButton();
				buttonStates[b == 1 ? 2 : (b == 2 ? 1 : b)] = true;
				mouseEvents.add(evt);
			}
		});
		canvas.addEventListener("mouseup", mouseup = new EventListener<MouseEvent>() {
			@Override
			public void handleEvent(MouseEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
				int b = evt.getButton();
				buttonStates[b == 1 ? 2 : (b == 2 ? 1 : b)] = false;
				mouseEvents.add(evt);
			}
		});
		canvas.addEventListener("mousemove", mousemove = new EventListener<MouseEvent>() {
			@Override
			public void handleEvent(MouseEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
				mouseX = (int)(getOffsetX(evt) * win.getDevicePixelRatio());
				mouseY = (int)((canvas.getClientHeight() - getOffsetY(evt)) * win.getDevicePixelRatio());
				mouseDX += evt.getMovementX();
				mouseDY += -evt.getMovementY();
				if(hasBeenActive()) {
					mouseEvents.add(evt);
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
		win.addEventListener("keydown", keydown = new EventListener<KeyboardEvent>() {
			@Override
			public void handleEvent(KeyboardEvent evt) {
				int w = getWhich(evt);
				if (w == 122) { // F11
					toggleFullscreen();
				}
				evt.preventDefault();
				evt.stopPropagation();
				if(!enableRepeatEvents && evt.isRepeat()) return;
				int ww = processFunctionKeys(w);
				keyStates[KeyboardConstants.getEaglerKeyFromBrowser(ww, ww == w ? evt.getLocation() : 0)] = true;
				keyEvents.add(evt);
			}
		});
		win.addEventListener("keyup", keyup = new EventListener<KeyboardEvent>() {
			@Override
			public void handleEvent(KeyboardEvent evt) {
				int w = getWhich(evt);
				evt.preventDefault();
				evt.stopPropagation();
				if(!enableRepeatEvents && evt.isRepeat()) return;
				int ww = processFunctionKeys(w);
				int eagKey = KeyboardConstants.getEaglerKeyFromBrowser(ww, ww == w ? evt.getLocation() : 0);
				keyStates[eagKey] = false;
				if(eagKey == functionKeyModifier) {
					for(int key = KeyboardConstants.KEY_F1; key <= KeyboardConstants.KEY_F10; ++key) {
						keyStates[key] = false;
					}
				}
				keyEvents.add(evt);
			}
		});
		win.addEventListener("keypress", keypress = new EventListener<KeyboardEvent>() {
			@Override
			public void handleEvent(KeyboardEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
				if(enableRepeatEvents && evt.isRepeat()) keyEvents.add(evt);
			}
		});
		canvas.addEventListener("wheel", wheel = new EventListener<WheelEvent>() {
			@Override
			public void handleEvent(WheelEvent evt) {
				evt.preventDefault();
				evt.stopPropagation();
				mouseEvents.add(evt);
				mouseDWheel += evt.getDeltaY();
			}
		});
		win.addEventListener("blur", new EventListener<WheelEvent>() {
			@Override
			public void handleEvent(WheelEvent evt) {
				isWindowFocused = false;
				for(int i = 0; i < buttonStates.length; ++i) {
					buttonStates[i] = false;
				}
				for(int i = 0; i < keyStates.length; ++i) {
					keyStates[i] = false;
				}
			}
		});
		win.addEventListener("focus", new EventListener<WheelEvent>() {
			@Override
			public void handleEvent(WheelEvent evt) {
				isWindowFocused = true;
			}
		});
		win.getDocument().addEventListener("pointerlockchange", pointerlock = new EventListener<WheelEvent>() {
			@Override
			public void handleEvent(WheelEvent evt) {
				Window.setTimeout(new TimerHandler() {
					@Override
					public void onTimer() {
						boolean grab = isPointerLocked();
						if(!grab) {
							if(pointerLockFlag) {
								mouseUngrabTimer = System.currentTimeMillis();
							}
						}
						pointerLockFlag = grab;
					}
				}, 60);
				mouseDX = 0.0D;
				mouseDY = 0.0D;
			}
		});
		onBeforeCloseRegister();

		fullscreenQuery = fullscreenMediaQuery();
		if (keyboardLockSupported = checkKeyboardLockSupported()) {
			TeaVMUtils.addEventListener(fullscreenQuery, "change", new EventListener<Event>() {
				@Override
				public void handleEvent(Event evt) {
					if (!mediaQueryMatches(evt)) {
						unlockKeys();
						lockKeys = false;
					}
				}
			});
		}
	}

	@JSBody(params = { }, script = "if(window.navigator.userActivation){return window.navigator.userActivation.hasBeenActive;}else{return false;}")
	public static native boolean hasBeenActive();
	
	@JSBody(params = { "m" }, script = "return m.offsetX;")
	private static native int getOffsetX(MouseEvent m);
	
	@JSBody(params = { "m" }, script = "return m.offsetY;")
	private static native int getOffsetY(MouseEvent m);
	
	@JSBody(params = { "e" }, script = "return e.which;")
	private static native int getWhich(KeyboardEvent e);
	
	public static int getWindowWidth() {
		return windowWidth;
	}

	public static int getWindowHeight() {
		return windowHeight;
	}

	public static boolean getWindowFocused() {
		return isWindowFocused || isPointerLocked();
	}

	public static boolean isCloseRequested() {
		return false;
	}

	public static void update() {
		double r = win.getDevicePixelRatio();
		int w = PlatformRuntime.parent.getClientWidth();
		int h = PlatformRuntime.parent.getClientHeight();
		int w2 = windowWidth = (int)(w * r);
		int h2 = windowHeight = (int)(h * r);
		if(canvas.getWidth() != w2) {
			canvas.setWidth(w2);
		}
		if(canvas.getHeight() != h2) {
			canvas.setHeight(h2);
		}
		flipBuffer();
		EagUtils.sleep(1l);
	}
	
	static void initFramebuffer(WebGL2RenderingContext ctx, WebGLFramebuffer fbo, int sw, int sh) {
		context = ctx;
		mainFramebuffer = fbo;
		
		framebufferWidth = windowWidth = sw;
		framebufferHeight = windowHeight = sh;
		
		ctx.bindFramebuffer(FRAMEBUFFER, fbo);

		mainColorRenderbuffer = ctx.createRenderbuffer();
		mainDepthRenderbuffer = ctx.createRenderbuffer();
		
		ctx.bindRenderbuffer(RENDERBUFFER, mainColorRenderbuffer);
		ctx.renderbufferStorage(RENDERBUFFER, RGBA8, sw, sh);
		ctx.framebufferRenderbuffer(FRAMEBUFFER, COLOR_ATTACHMENT0, RENDERBUFFER, mainColorRenderbuffer);
		
		ctx.bindRenderbuffer(RENDERBUFFER, mainDepthRenderbuffer);
		ctx.renderbufferStorage(RENDERBUFFER, DEPTH_COMPONENT32F, sw, sh);
		ctx.framebufferRenderbuffer(FRAMEBUFFER, DEPTH_ATTACHMENT, RENDERBUFFER, mainDepthRenderbuffer);
		
		ctx.drawBuffers(new int[] { COLOR_ATTACHMENT0 });
	}
	
	private static void flipBuffer() {
		
		context.bindFramebuffer(READ_FRAMEBUFFER, mainFramebuffer);
		context.bindFramebuffer(DRAW_FRAMEBUFFER, null);
		context.blitFramebuffer(0, 0, framebufferWidth, framebufferHeight, 0, 0, windowWidth, windowHeight, COLOR_BUFFER_BIT, NEAREST);
		
		context.bindFramebuffer(FRAMEBUFFER, mainFramebuffer);
		
		if(windowWidth != framebufferWidth || windowHeight != framebufferHeight) {
			framebufferWidth = windowWidth;
			framebufferHeight = windowHeight;
			
			context.bindRenderbuffer(RENDERBUFFER, mainColorRenderbuffer);
			context.renderbufferStorage(RENDERBUFFER, RGBA8, framebufferWidth, framebufferHeight);
			
			context.bindRenderbuffer(RENDERBUFFER, mainDepthRenderbuffer);
			context.renderbufferStorage(RENDERBUFFER, DEPTH_COMPONENT32F, framebufferWidth, framebufferHeight);
		}
		
	}
	
	public static boolean wasResized() {
		if(windowWidth != lastWasResizedWindowWidth || windowHeight != lastWasResizedWindowHeight) {
			lastWasResizedWindowWidth = windowWidth;
			lastWasResizedWindowHeight = windowHeight;
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean keyboardNext() {
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

	public static boolean keyboardGetEventKeyState() {
		return currentEventK == null? false : !currentEventK.getType().equals("keyup");
	}

	public static int keyboardGetEventKey() {
		int w = processFunctionKeys(getWhich(currentEventK));
		return currentEventK == null ? -1 : KeyboardConstants.getEaglerKeyFromBrowser(w, currentEventK.getLocation());
	}

	public static char keyboardGetEventCharacter() {
		if(currentEventK == null) return '\0';
		String s = currentEventK.getKey();
		return currentEventK == null ? ' ' : (char) (s.length() > 1 ? '\0' : s.charAt(0));
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
		return currentEventK == null ? false : currentEventK.isRepeat();
	}

	public static void keyboardEnableRepeatEvents(boolean b) {
		enableRepeatEvents = b;
	}

	public static boolean mouseNext() {
		currentEvent = null;
		return !mouseEvents.isEmpty() && (currentEvent = mouseEvents.remove(0)) != null;
	}

	public static boolean mouseGetEventButtonState() {
		return currentEvent == null ? false : currentEvent.getType().equals(MouseEvent.MOUSEDOWN);
	}

	public static int mouseGetEventButton() {
		if(currentEvent == null || currentEvent.getType().equals(MouseEvent.MOUSEMOVE)) return -1;
		int b = currentEvent.getButton();
		return b == 1 ? 2 : (b == 2 ? 1 : b);
	}

	public static int mouseGetEventX() {
		return currentEvent == null ? -1 : (int)(currentEvent.getClientX() * win.getDevicePixelRatio());
	}

	public static int mouseGetEventY() {
		return currentEvent == null ? -1 : (int)((canvas.getClientHeight() - currentEvent.getClientY()) * win.getDevicePixelRatio());
	}

	public static int mouseGetEventDWheel() {
		return ("wheel".equals(currentEvent.getType())) ? (((WheelEvent)currentEvent).getDeltaY() == 0.0D ? 0 : (((WheelEvent)currentEvent).getDeltaY() > 0.0D ? -1 : 1)) : 0;
	}

	public static int mouseGetX() {
		return mouseX;
	}

	public static int mouseGetY() {
		return mouseY;
	}

	public static boolean mouseIsButtonDown(int i) {
		return buttonStates[i];
	}

	public static int mouseGetDWheel() {
		int ret = (int)mouseDWheel;
		mouseDWheel = 0.0D;
		return ret;
	}

	public static void mouseSetGrabbed(boolean grab) {
		long t = System.currentTimeMillis();
		pointerLockFlag = grab;
		mouseGrabTimer = t;
		if(grab) {
			canvas.requestPointerLock();
			if(mouseUngrabTimeout != -1) Window.clearTimeout(mouseUngrabTimeout);
			mouseUngrabTimeout = -1;
			if(t - mouseUngrabTimer < 3000l) {
				mouseUngrabTimeout = Window.setTimeout(new TimerHandler() {
					@Override
					public void onTimer() {
						canvas.requestPointerLock();
					}
				}, 3100 - (int)(t - mouseUngrabTimer));
			}
		}else {
			if(mouseUngrabTimeout != -1) Window.clearTimeout(mouseUngrabTimeout);
			mouseUngrabTimeout = -1;
			Window.current().getDocument().exitPointerLock();
		}
		mouseDX = 0.0D;
		mouseDY = 0.0D;
	}

	public static boolean isMouseGrabbed() {
		return pointerLockFlag;
	}

	@JSBody(params = { }, script = "return document.pointerLockElement != null;")
	public static native boolean isPointerLocked();

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
		win.removeEventListener("contextmenu", contextmenu);
		canvas.removeEventListener("mousedown", mousedown);
		canvas.removeEventListener("mouseup", mouseup);
		canvas.removeEventListener("mousemove", mousemove);
		canvas.removeEventListener("mouseenter", mouseenter);
		canvas.removeEventListener("mouseleave", mouseleave);
		win.removeEventListener("keydown", keydown);
		win.removeEventListener("keyup", keyup);
		win.removeEventListener("keypress", keypress);
		canvas.removeEventListener("wheel", wheel);
		win.getDocument().removeEventListener("pointerlockchange", pointerlock);
		if(mouseUngrabTimeout != -1) {
			Window.clearTimeout(mouseUngrabTimeout);
			mouseUngrabTimeout = -1;
		}
	}

	public static void pressAnyKeyScreen() {
		if(mouseEvents.isEmpty() && keyEvents.isEmpty() && !hasBeenActive()) {
			EarlyLoadScreen.paintEnable();
			
			while(mouseEvents.isEmpty() && keyEvents.isEmpty()) {
				EagUtils.sleep(100l);
			}
		}
	}

	public static void clearEvenBuffers() {
		mouseEvents.clear();
		keyEvents.clear();
	}

	@JSBody(params = {}, script = "return window.matchMedia('(display-mode: fullscreen)');")
	private static native JSObject fullscreenMediaQuery();

	@JSBody(params = { "mediaQuery" }, script = "return mediaQuery.matches;")
	private static native boolean mediaQueryMatches(JSObject mediaQuery);

	public static void toggleFullscreen() {
		if (isFullscreen()) {
			if (keyboardLockSupported) {
				unlockKeys();
				lockKeys = false;
			}
			exitFullscreen();
		} else {
			if (keyboardLockSupported) {
				lockKeys();
				lockKeys = true;
			}
			requestFullscreen(canvas);
		}
	}

	public static boolean isFullscreen() {
		return mediaQueryMatches(fullscreenQuery);
	}

	@JSBody(params = { }, script = "window.navigator.keyboard.lock();")
	private static native void lockKeys();

	@JSBody(params = { }, script = "window.navigator.keyboard.unlock();")
	private static native void unlockKeys();

	@JSBody(params = { }, script = "return 'keyboard' in window.navigator && 'lock' in window.navigator.keyboard;")
	private static native boolean checkKeyboardLockSupported();

	@JSBody(params = { }, script = "document.exitFullscreen();")
	private static native void exitFullscreen();

	@JSBody(params = { "element" }, script = "element.requestFullscreen();")
	private	 static native void requestFullscreen(HTMLElement element);
	
}
