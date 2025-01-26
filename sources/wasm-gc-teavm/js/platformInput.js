/*
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

const platfInputName = "platformInput";

async function initPlatformInput(inputImports) {

	const EVENT_INPUT_MOUSE = 0;
	const EVENT_INPUT_KEYBOARD = 1;
	const EVENT_INPUT_TOUCH = 2;
	const EVENT_INPUT_TOUCH_KEYBOARD = 3;
	const EVENT_INPUT_TOUCH_PASTE = 4;
	const EVENT_INPUT_FOCUS = 5;
	const EVENT_INPUT_BLUR = 6;
	const EVENT_INPUT_MOUSE_ENTER = 7;
	const EVENT_INPUT_MOUSE_EXIT = 8;
	const EVENT_INPUT_WINDOW_RESIZE = 9;
	const EVENT_INPUT_GAMEPAD = 10;

	const EVENT_MOUSE_DOWN = 0;
	const EVENT_MOUSE_UP = 1;
	const EVENT_MOUSE_MOVE = 2;
	const EVENT_MOUSE_WHEEL = 3;

	const EVENT_KEY_DOWN = 0;
	const EVENT_KEY_UP = 1;
	const EVENT_KEY_REPEAT = 2;

	const EVENT_KEY_MOD_CONTROL = 1;
	const EVENT_KEY_MOD_SHIFT = 2;
	const EVENT_KEY_MOD_ALT = 4;

	const EVENT_TOUCH_START = 0;
	const EVENT_TOUCH_MOVE = 1;
	const EVENT_TOUCH_END = 2;

	const EVENT_TOUCH_KEYBOARD_ABSOLUTE = 0;
	const EVENT_TOUCH_KEYBOARD_CODEPOINTS = 1;

	const EVENT_RESIZE_WINDOW = 1;
	const EVENT_RESIZE_VISUAL_VIEWPORT = 2;
	const EVENT_RESIZE_DPI = 4;

	const EVENT_GAMEPAD_CONNECTED = 0;
	const EVENT_GAMEPAD_DISCONNECTED = 1;

	var touchKeyboardOpenZone = null;
	var touchOffsetX = 0;
	var touchOffsetY = 0;
	var touchKeyboardForm = null;
	var touchKeyboardField = null;
	var shownTouchKeyboardEventWarning = false;
	var shownLegacyTouchKeyboardWarning = false;
	var showniOSReturnTouchKeyboardWarning = false;
	var lastTouchKeyboardEvtA = 0.0;
	var lastTouchKeyboardEvtB = 0.0;
	var lastTouchKeyboardEvtC = 0.0;

	var lastWindowWidth = -1;
	var lastWindowHeight = -1;
	var lastWindowDPI = -1.0;
	var useVisualViewport = false;
	var lastVisualViewportX = -1;
	var lastVisualViewportY = -1;
	var lastVisualViewportW = -1;
	var lastVisualViewportH = -1;

	var pointerLockSupported = false;
	var pointerLockFlag = false;
	var pointerLockWaiting = false;
	var mouseUngrabTimer = 0;
	var mouseGrabTimer = 0;
	var mouseUngrabTimeout = -1;

	var fullscreenSupported = false;
	var fullscreenQuery = null;
	var keyboardLockSupported = false;
	var lockKeys = false;

	var vsyncSupport = false;
	var vsyncTimeout = -1;

	var gamepadSupported = false;

	const currentEventListeners = {
		contextmenu: null,
		mousedown: null,
		mouseup: null,
		mousemove: null,
		mouseenter: null,
		mouseleave: null,
		touchstart: null,
		touchend: null,
		touchmove: null,
		touchcancel: null,
		gamepadconnected: null,
		gamepaddisconnected: null,
		keydown: null,
		keyup: null,
		touchKeyboardOpenZone_touchstart: null,
		touchKeyboardOpenZone_touchend: null,
		touchKeyboardOpenZone_touchmove: null,
		wheel: null,
		focus: null,
		blur: null,
		pointerlock: null,
		pointerlockerr: null,
		fullscreenChange: null
	};

	touchKeyboardOpenZone = document.createElement("div");
	touchKeyboardOpenZone.classList.add("_eaglercraftX_keyboard_open_zone");
	touchKeyboardOpenZone.style.display = "none";
	touchKeyboardOpenZone.style.position = "absolute";
	touchKeyboardOpenZone.style.backgroundColor = "transparent";
	touchKeyboardOpenZone.style.top = "0px";
	touchKeyboardOpenZone.style.left = "0px";
	touchKeyboardOpenZone.style.width = "0px";
	touchKeyboardOpenZone.style.height = "0px";
	touchKeyboardOpenZone.style.zIndex = "100";
	touchKeyboardOpenZone.style.touchAction = "pan-x pan-y";
	touchKeyboardOpenZone.style.setProperty("-webkit-touch-callout", "none");
	touchKeyboardOpenZone.style.setProperty("-webkit-tap-highlight-color", "rgba(255, 255, 255, 0)");
	parentElement.appendChild(touchKeyboardOpenZone);

	function updateTouchOffset() {
		touchOffsetX = 0;
		touchOffsetY = 0;
		var el = canvasElement;
		while(el && !isNaN(el.offsetLeft) && !isNaN(el.offsetTop)) {
			touchOffsetX += el.offsetLeft - el.scrollLeft;
			touchOffsetY += el.offsetTop - el.scrollTop;
			el = el.offsetParent;
		}
	}

	function reportWindowSize() {
		var newWindowDPI = window.devicePixelRatio;
		if(newWindowDPI < 0.01) newWindowDPI = 1.0;
		updateTouchOffset();
		const w = parentElement.clientWidth;
		const h = parentElement.clientHeight;
		var newWindowWidth = (w * newWindowDPI) | 0;
		var newWindowHeight = (h * newWindowDPI) | 0;
		var newVisualViewportX = 0;
		var newVisualViewportY = 0;
		var newVisualViewportW = newWindowWidth;
		var newVisualViewportH = newWindowHeight;
		if(useVisualViewport) {
			const vv = window.visualViewport;
			const scale = vv.scale;
			newVisualViewportX = (vv.pageLeft * newWindowDPI * scale);
			newVisualViewportY = (vv.pageTop * newWindowDPI * scale);
			newVisualViewportW = (vv.width * newWindowDPI * scale);
			newVisualViewportH = (vv.height * newWindowDPI * scale);
			if(newVisualViewportW < 1) newVisualViewportW = 1;
			if(newVisualViewportH < 1) newVisualViewportH = 1;
			if(newVisualViewportX < 0) {
				newVisualViewportW += newVisualViewportX;
				newVisualViewportX = 0;
			}else if(newVisualViewportX >= newWindowWidth) {
				newVisualViewportX = newWindowWidth - 1;
			}
			if(newVisualViewportY < 0) {
				newVisualViewportH += newVisualViewportY;
				newVisualViewportY = 0;
			}else if(newVisualViewportY >= newWindowHeight) {
				newVisualViewportY = newWindowHeight - 1;
			}
			if((newVisualViewportX + newVisualViewportW) > newWindowWidth) {
				newVisualViewportW = newWindowWidth - newVisualViewportX;
			}
			if((newVisualViewportY + newVisualViewportH) > newWindowHeight) {
				newVisualViewportH = newWindowHeight - newVisualViewportY;
			}
		}
		const eventData = {
			"eventTypeMask": 0
		};
		if(lastWindowDPI !== newWindowDPI) {
			lastWindowDPI = newWindowDPI;
			eventData["eventTypeMask"] |= EVENT_RESIZE_DPI;
			eventData["windowDPI"] = newWindowDPI;
		}
		if(lastWindowWidth !== newWindowWidth || lastWindowHeight !== newWindowHeight) {
			lastWindowWidth = newWindowWidth;
			lastWindowHeight = newWindowHeight;
			eventData["eventTypeMask"] |= EVENT_RESIZE_WINDOW;
			eventData["windowWidth"] = newWindowWidth;
			eventData["windowHeight"] = newWindowHeight;
		}
		if(lastVisualViewportX !== newVisualViewportX || lastVisualViewportY !== newVisualViewportY
				|| lastVisualViewportW !== newVisualViewportW || lastVisualViewportH !== newVisualViewportH) {
			lastVisualViewportX = newVisualViewportX;
			lastVisualViewportY = newVisualViewportY;
			lastVisualViewportW = newVisualViewportW;
			lastVisualViewportH = newVisualViewportH;
			eventData["eventTypeMask"] |= EVENT_RESIZE_VISUAL_VIEWPORT;
			eventData["visualViewportX"] = newVisualViewportX;
			eventData["visualViewportY"] = newVisualViewportY;
			eventData["visualViewportW"] = newVisualViewportW;
			eventData["visualViewportH"] = newVisualViewportH;
		}
		if(eventData["eventTypeMask"] !== 0) {
			pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_WINDOW_RESIZE, eventData);
		}
	}

	reportWindowSize();

	parentElement.addEventListener("contextmenu", currentEventListeners.contextmenu = function(/** Event */ evt) {
		if(evt.target === integratedServerCrashPanel) return;
		evt.preventDefault();
		evt.stopPropagation();
	});

	canvasElement.addEventListener("mousedown", /** @type {function(Event)} */ (currentEventListeners.mousedown = function(/** MouseEvent */ evt) {
		evt.preventDefault();
		evt.stopPropagation();
		if(pointerLockSupported && pointerLockFlag && document.pointerLockElement !== canvasElement) {
			mouseSetGrabbed(1);
			return;
		}
		pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_MOUSE, {
			"eventType": EVENT_MOUSE_DOWN,
			"posX": evt.offsetX,
			"posY": evt.offsetY,
			"button": evt.button
		});
	}));

	canvasElement.addEventListener("mouseup", /** @type {function(Event)} */ (currentEventListeners.mouseup = function(/** MouseEvent */ evt) {
		evt.preventDefault();
		evt.stopPropagation();
		pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_MOUSE, {
			"eventType": EVENT_MOUSE_UP,
			"posX": evt.offsetX,
			"posY": evt.offsetY,
			"button": evt.button
		});
	}));

	canvasElement.addEventListener("mousemove", /** @type {function(Event)} */ (currentEventListeners.mousemove = function(/** MouseEvent */ evt) {
		evt.preventDefault();
		evt.stopPropagation();
		pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_MOUSE, {
			"eventType": EVENT_MOUSE_MOVE,
			"posX": evt.offsetX,
			"posY": evt.offsetY,
			"movementX": evt.movementX,
			"movementY": evt.movementY
		});
	}));

	canvasElement.addEventListener("mouseenter", currentEventListeners.mouseenter = function(/** Event */ evt) {
		pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_MOUSE_ENTER, null);
	});

	canvasElement.addEventListener("mouseleave", currentEventListeners.mouseleave = function(/** Event */ evt) {
		pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_MOUSE_EXIT, null);
	});

	/**
	 * @param {TouchList} lstIn
	 * @return {Array}
	 */
	function createTouchList(lstIn) {
		const len = lstIn.length;
		const ret = new Array(len);
		for(var i = 0; i < len; ++i) {
			const itm = lstIn.item(i);
			ret[i] = {
				"pointX": itm.pageX - touchOffsetX,
				"pointY": itm.pageY - touchOffsetY,
				"radius": (itm.radiusX + itm.radiusY) * 0.5,
				"force": itm.force,
				"pointUID": itm.identifier
			};
		}
		return ret;
	}

	canvasElement.addEventListener("touchstart", /** @type {function(Event)} */ (currentEventListeners.touchstart = function(/** TouchEvent */ evt) {
		evt.preventDefault();
		evt.stopPropagation();
		pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_TOUCH, {
			"eventType": EVENT_TOUCH_START,
			"changedTouches": createTouchList(evt.changedTouches),
			"targetTouches": createTouchList(evt.targetTouches)
		});
		touchCloseDeviceKeyboard();
	}));

	canvasElement.addEventListener("touchend", /** @type {function(Event)} */ (currentEventListeners.touchend = function(/** TouchEvent */ evt) {
		evt.preventDefault();
		evt.stopPropagation();
		pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_TOUCH, {
			"eventType": EVENT_TOUCH_END,
			"changedTouches": createTouchList(evt.changedTouches),
			"targetTouches": createTouchList(evt.targetTouches)
		});
	}));

	canvasElement.addEventListener("touchmove", /** @type {function(Event)} */ (currentEventListeners.touchmove = function(/** TouchEvent */ evt) {
		evt.preventDefault();
		evt.stopPropagation();
		pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_TOUCH, {
			"eventType": EVENT_TOUCH_MOVE,
			"changedTouches": createTouchList(evt.changedTouches),
			"targetTouches": createTouchList(evt.targetTouches)
		});
	}));

	canvasElement.addEventListener("touchcancel", /** @type {function(Event)} */ (currentEventListeners.touchcancel = function(/** TouchEvent */ evt) {
		pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_TOUCH, {
			"eventType": EVENT_TOUCH_END,
			"changedTouches": createTouchList(evt.changedTouches),
			"targetTouches": createTouchList(evt.targetTouches)
		});
	}));

	canvasElement.addEventListener("wheel", /** @type {function(Event)} */ (currentEventListeners.wheel = function(/** WheelEvent */ evt) {
		evt.preventDefault();
		evt.stopPropagation();
		pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_MOUSE, {
			"eventType": EVENT_MOUSE_WHEEL,
			"posX": evt.offsetX,
			"posY": evt.offsetY,
			"wheel": evt.deltaY
		});
	}));

	window.addEventListener("blur", currentEventListeners.blur = function(/** Event */ evt) {
		pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_BLUR, null);
	});

	window.addEventListener("focus", currentEventListeners.focus = function(/** Event */ evt) {
		pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_FOCUS, null);
	});

	/**
	 * @param {number} evtType
	 * @param {KeyboardEvent} evt
	 * @return {Object}
	 */
	function createKeyEvent(evtType, evt) {
		return {
			"eventType": evtType,
			"keyCode": (evt.code || null),
			"keyName": (evt.key || null),
			"legacyCode": (typeof evt.which === "number") ? evt.which : ((typeof evt.keyCode === "number") ? evt.keyCode : 0),
			"location": evt.location,
			"mods": ((evt.ctrlKey ? EVENT_KEY_MOD_CONTROL : 0)
				| (evt.shiftKey ? EVENT_KEY_MOD_SHIFT : 0)
				| (evt.metaKey ? EVENT_KEY_MOD_ALT : 0))
		};
	}

	window.addEventListener("keydown", /** @type {function(Event)} */ (currentEventListeners.keydown = function(/** KeyboardEvent */ evt) {
		if(!integratedServerCrashPanelShowing) {
			evt.preventDefault();
			evt.stopPropagation();
		}
		if(evt.key === "F11" && !evt.repeat) {
			toggleFullscreenImpl();
			return;
		}
		pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_KEYBOARD, createKeyEvent(evt.repeat ? EVENT_KEY_REPEAT : EVENT_KEY_DOWN, evt));
		if(evt.timeStamp && evt.key !== "Unidentified") lastTouchKeyboardEvtA = evt.timeStamp;
	}));

	window.addEventListener("keyup", /** @type {function(Event)} */ (currentEventListeners.keyup = function(/** KeyboardEvent */ evt) {
		if(!integratedServerCrashPanelShowing) {
			evt.preventDefault();
			evt.stopPropagation();
		}
		pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_KEYBOARD, createKeyEvent(EVENT_KEY_UP, evt));
	}));

	touchKeyboardOpenZone.addEventListener("touchstart", /** @type {function(Event)} */ (currentEventListeners.touchKeyboardOpenZone_touchstart = function(/** TouchEvent */ evt) {
		evt.preventDefault();
		evt.stopPropagation();
	}));

	touchKeyboardOpenZone.addEventListener("touchend", /** @type {function(Event)} */ (currentEventListeners.touchKeyboardOpenZone_touchend = function(/** TouchEvent */ evt) {
		evt.preventDefault();
		evt.stopPropagation();
		touchOpenDeviceKeyboard();
	}));

	touchKeyboardOpenZone.addEventListener("touchmove", /** @type {function(Event)} */ (currentEventListeners.touchKeyboardOpenZone_touchmove = function(/** TouchEvent */ evt) {
		evt.preventDefault();
		evt.stopPropagation();
	}));

	pointerLockSupported = (typeof document.exitPointerLock === "function");

	if(pointerLockSupported) {
		document.addEventListener("pointerlockchange", /** @type {function(Event)} */ (currentEventListeners.pointerlock = function(evt) {
			window.setTimeout(function() {
				const grab = (document.pointerLockElement === canvasElement);
				if(!grab) {
					if(pointerLockFlag) {
						mouseUngrabTimer = performance.now() | 0;
					}
				}
				pointerLockFlag = grab;
			}, 60);
			pointerLockWaiting = false;
		}));
		document.addEventListener("pointerlockerror", /** @type {function(Event)} */ (currentEventListeners.pointerlockerr = function(evt) {
			pointerLockWaiting = false;
		}));
	}else {
		eagError("Pointer lock is not supported on this browser");
	}

	fullscreenSupported = (typeof document.exitFullscreen === "function");

	if(fullscreenSupported) {
		fullscreenQuery = window.matchMedia("(display-mode: fullscreen)");
		keyboardLockSupported = !!(navigator.keyboard && navigator.keyboard.lock);
		if(keyboardLockSupported) {
			fullscreenQuery.addEventListener("change", /** @type {function(Event)} */ (currentEventListeners.fullscreenChange = function(evt) {
				if(!fullscreenQuery.matches) {
					navigator.keyboard.unlock();
					lockKeys = false;
				}
			}));
		}
	}else {
		eagError("Fullscreen is not supported on this browser");
	}

	if(typeof window.visualViewport !== "undefined") {
		if(rootElement.tagName.toLowerCase() === "body") {
			useVisualViewport = true;
		}else {
			const bodyTag = document.body;
			if (Math.abs(bodyTag.clientWidth - parentElement.clientWidth) <= 10
					&& Math.abs(bodyTag.clientHeight - parentElement.clientHeight) <= 10) {
				useVisualViewport = true;
			}
		}
	}else {
		useVisualViewport = false;
	}

	if(useVisualViewport) {
		eagInfo("Note: Detected game is embedded in body, some screens may be resized to window.visualViewport instead for a better mobile experience");
	}

	function asyncRequestAnimationFrame() {
		return new Promise(function(resolve) {
			if(vsyncTimeout !== -1) {
				throw new Error("Main thread is already waiting for VSync!");
			}
			const hasCompleted = [ false ];
			window.requestAnimationFrame(function() {
				if(!hasCompleted[0]) {
					hasCompleted[0] = true;
					if(vsyncTimeout !== -1) {
						window.clearTimeout(vsyncTimeout);
						vsyncTimeout = -1;
					}
					resolve();
				}
			});
			vsyncTimeout = window.setTimeout(function() {
				if(!hasCompleted[0]) {
					hasCompleted[0] = true;
					vsyncTimeout = -1;
					resolve();
				}
			}, 20);
		});
	}

	try {
		await asyncRequestAnimationFrame();
		vsyncSupport = true;
	}catch(ex) {
		vsyncSupport = false;
	}

	if(!vsyncSupport) {
		eagError("VSync is not supported on this browser");
	}

	gamepadSupported = (typeof navigator.getGamepads === "function");
	if(gamepadSupported) {
		window.addEventListener("gamepadconnected", /** @type {function(Event)} */ (currentEventListeners.gamepadconnected = function(/** GamepadEvent */ evt) {
			pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_GAMEPAD, {
				"eventType": EVENT_GAMEPAD_CONNECTED
			});
		}));
		window.addEventListener("gamepaddisconnected", /** @type {function(Event)} */ (currentEventListeners.gamepaddisconnected = function(/** GamepadEvent */ evt) {
			pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_GAMEPAD, {
				"eventType": EVENT_GAMEPAD_DISCONNECTED,
				"gamepad": evt.gamepad
			});
		}));
	}else {
		eagError("Gamepad detected as unsupported!");
	}

	/**
	 * @return {boolean}
	 */
	inputImports["keyboardLayoutSupported"] = function() {
		return !!(navigator.keyboard && navigator.keyboard.getLayoutMap);
	};

	/**
	 * @return {Promise<Array>}
	 */
	async function iterateKeyboardLayoutImpl() {
		const ret = [];
		try {
			const layout = await navigator.keyboard.getLayoutMap();
			if(layout && layout["forEach"]) {
				layout["forEach"](function(/** string */ k, /** string */ v) {
					ret.push({
						"key": k,
						"value": v
					});
				});
			}
		}catch(ex) {
		}
		return ret;
	}

	inputImports["iterateKeyboardLayout"] = new WebAssembly.Suspending(iterateKeyboardLayoutImpl);

	/**
	 * @param {number} width
	 * @param {number} height
	 */
	inputImports["updateCanvasSize"] = function(width, height) {
		if(canvasElement.width !== width) {
			canvasElement.width = width;
		}
		if(canvasElement.height !== height) {
			canvasElement.height = height;
		}
	};

	function sleepPromise(ms) {
		return new Promise(function(resolve) {
			setTimeout(resolve, ms);
		});
	}

	var manualSyncTimer = 0;

	/**
	 * @param {number} fpsLimit
	 * @return {Promise}
	 */
	function syncDelay(fpsLimit) {
		if(fpsLimit > 0 && fpsLimit <= 1000) {
			const frameMillis = (1000 / fpsLimit);
			if(manualSyncTimer === 0) {
				manualSyncTimer = performance.now() + frameMillis;
			}else {
				var millis = performance.now();
				var remaining = (manualSyncTimer - millis) | 0;
				if(remaining > 0) {
					if(!runtimeOpts.useDelayOnSwap && allowImmediateContinue) {
						return immediateContinueImpl().then(function() {
							var millis0 = performance.now();
							var remaining0 = (manualSyncTimer - millis0) | 0;
							if(remaining0 > 0) {
								return sleepPromise(remaining0).then(function() {
									var millis1 = performance.now();
									if((manualSyncTimer += frameMillis) < millis1) {
										manualSyncTimer = millis1;
									}
								});
							}else if((manualSyncTimer += frameMillis) < millis0) {
								manualSyncTimer = millis0;
							}
						});
					}else {
						return sleepPromise(remaining).then(function() {
							var millis0 = performance.now();
							if((manualSyncTimer += frameMillis) < millis0) {
								manualSyncTimer = millis0;
							}
						});
					}
				}else if((manualSyncTimer += frameMillis) < millis) {
					manualSyncTimer = millis;
				}
			}
		}else {
			manualSyncTimer = 0;
		}
		
		return swapDelayImpl();
	}

	/**
	 * @param {number} fpsLimit
	 * @param {number} vsync
	 * @return {Promise}
	 */
	function updatePlatformAndSleepImpl(fpsLimit, vsync) {
		reportWindowSize();
		if((typeof document.visibilityState !== "string") || (document.visibilityState === "visible")) {
			if(vsyncSupport && vsync) {
				manualSyncTimer = 0;
				return asyncRequestAnimationFrame();
			}else {
				if(fpsLimit <= 0) {
					manualSyncTimer = 0;
					return swapDelayImpl();
				}else {
					return syncDelay(fpsLimit);
				}
			}
		}else {
			manualSyncTimer = 0;
			return sleepPromise(50);
		}
	}

	inputImports["updatePlatformAndSleep"] = new WebAssembly.Suspending(updatePlatformAndSleepImpl);

	/**
	 * @return {boolean}
	 */
	inputImports["isVSyncSupported"] = function() {
		return vsyncSupport;
	}

	/**
	 * @param {number} grab
	 */
	function mouseSetGrabbed(grab) {
		if(!pointerLockSupported) return;
		pointerLockFlag = !!grab;
		const t = performance.now() | 0;
		mouseGrabTimer = t;
		if(grab) {
			pointerLockWaiting = true;
			try {
				canvasElement.requestPointerLock();
			}catch(ex) {
			}
			if(mouseUngrabTimeout !== -1) window.clearTimeout(mouseUngrabTimeout);
			mouseUngrabTimeout = -1;
			if(t - mouseUngrabTimer < 3000) {
				mouseUngrabTimeout = window.setTimeout(function() {
					try {
						canvasElement.requestPointerLock();
					}catch(ex) {
					}
				}, 3100 - (t - mouseUngrabTimer));
			}
		}else {
			if(mouseUngrabTimeout !== -1) window.clearTimeout(mouseUngrabTimeout);
			mouseUngrabTimeout = -1;
			if(!pointerLockWaiting) {
				try {
					document.exitPointerLock();
				}catch(ex) {
				}
			}
		}
	};

	inputImports["mouseSetGrabbed"] = mouseSetGrabbed;

	/**
	 * @return {boolean}
	 */
	inputImports["isMouseGrabSupported"] = function() {
		return pointerLockSupported;
	};

	/**
	 * @return {boolean}
	 */
	inputImports["isMouseGrabbed"] = function() {
		return pointerLockFlag;
	};

	/**
	 * @return {boolean}
	 */
	inputImports["isPointerLocked"] = function() {
		return pointerLockSupported && (pointerLockWaiting || document.pointerLockElement === canvasElement);
	};

	/**
	 * @return {boolean}
	 */
	inputImports["supportsFullscreen"] = function() {
		return fullscreenSupported;
	};

	function toggleFullscreenImpl() {
		if(!fullscreenSupported) return;
		if(fullscreenQuery.matches) {
			if(keyboardLockSupported) {
				try {
					navigator.keyboard.unlock();
				}catch(ex) {
				}
				lockKeys = false;
			}
			try {
				document.exitFullscreen();
			}catch(ex) {
			}
		}else {
			if(keyboardLockSupported) {
				try {
					navigator.keyboard.lock();
				}catch(ex) {
				}
				lockKeys = true;
			}
			try {
				canvasElement.requestFullscreen();
			}catch(ex) {
			}
		}
	}

	inputImports["toggleFullscreen"] = toggleFullscreenImpl;

	/**
	 * @return {boolean}
	 */
	inputImports["isFullscreen"] = function() {
		return fullscreenSupported && fullscreenQuery.matches;
	};

	function touchOpenDeviceKeyboard() {
		if(!touchIsDeviceKeyboardOpenMAYBE()) {
			if(touchKeyboardField) {
				touchKeyboardField.blur();
				touchKeyboardField.value = "";
				setTimeout(function() {
					if(touchKeyboardField) {
						if(touchKeyboardForm) {
							touchKeyboardForm.removeChild(touchKeyboardField);
						}else {
							touchKeyboardField.remove();
						}
						touchKeyboardField = null;
					}
					if(touchKeyboardForm) {
						parentElement.removeChild(touchKeyboardForm);
						touchKeyboardForm = null;
					}
				}, 10);
				return;
			}
			if(touchKeyboardForm) {
				parentElement.removeChild(touchKeyboardForm);
				touchKeyboardForm = null;
			}
			touchKeyboardForm = document.createElement("form");
			touchKeyboardForm.setAttribute("autocomplete", "off");
			touchKeyboardForm.classList.add("_eaglercraftX_text_input_wrapper");
			touchKeyboardForm.style.position = "absolute";
			touchKeyboardForm.style.top = "0px";
			touchKeyboardForm.style.left = "0px";
			touchKeyboardForm.style.right = "0px";
			touchKeyboardForm.style.bottom = "0px";
			touchKeyboardForm.style.zIndex = "-100";
			touchKeyboardForm.style.margin = "0px";
			touchKeyboardForm.style.padding = "0px";
			touchKeyboardForm.style.border = "none";
			touchKeyboardForm.addEventListener("submit", function(/** Event */ evt) {
				evt.preventDefault();
				evt.stopPropagation();
				const d = evt.timeStamp;
				if(d) {
					if(lastTouchKeyboardEvtA !== 0.0 && (d - lastTouchKeyboardEvtA) < 10.0) {
						return;
					}
					if(lastTouchKeyboardEvtB !== 0.0 && (d - lastTouchKeyboardEvtB) < 10.0) {
						return;
					}
					if(lastTouchKeyboardEvtC !== 0.0 && (d - lastTouchKeyboardEvtC) < 10.0) {
						return;
					}
				}
				if(!showniOSReturnTouchKeyboardWarning) {
					eagInfo("Note: Generating return keystroke from submit event on form, this browser probably doesn't generate keydown/beforeinput/input when enter/return is pressed on the on-screen keyboard");
					showniOSReturnTouchKeyboardWarning = true;
				}
				pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_TOUCH_KEYBOARD, {
					"eventType": EVENT_TOUCH_KEYBOARD_ABSOLUTE,
					"absoluteCode": 0x1C, // KEY_RETURN
					"absoluteChar": 10 // '\n'
				});
			});
			touchKeyboardField = document.createElement("input");
			touchKeyboardField.type = "password";
			touchKeyboardField.value = " ";
			touchKeyboardField.classList.add("_eaglercraftX_text_input_element");
			touchKeyboardField.setAttribute("autocomplete", "off");
			touchKeyboardField.style.position = "absolute";
			touchKeyboardField.style.top = "0px";
			touchKeyboardField.style.left = "0px";
			touchKeyboardField.style.right = "0px";
			touchKeyboardField.style.bottom = "0px";
			touchKeyboardField.style.zIndex = "-100";
			touchKeyboardField.style.margin = "0px";
			touchKeyboardField.style.padding = "0px";
			touchKeyboardField.style.border = "none";
			touchKeyboardField.style.setProperty("-webkit-touch-callout", "default");
			touchKeyboardField.addEventListener("beforeinput", /** @type {function(Event)} */ (function(/** InputEvent */ evt) {
				if(touchKeyboardField !== evt.target) return;
				if(!shownTouchKeyboardEventWarning) {
					eagInfo("Note: Caught beforeinput event from on-screen keyboard, browser probably does not generate global keydown/keyup events on text fields, or does not respond to cancelling keydown");
					shownTouchKeyboardEventWarning = true;
				}
				const d = evt.timeStamp;
				if(d) {
					if(lastTouchKeyboardEvtA !== 0.0 && (d - lastTouchKeyboardEvtA) < 10.0) {
						return;
					}
					lastTouchKeyboardEvtB = d;
				}
				evt.preventDefault();
				evt.stopPropagation();
				switch(evt.inputType) {
				case "insertParagraph":
				case "insertLineBreak":
					pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_TOUCH_KEYBOARD, {
						"eventType": EVENT_TOUCH_KEYBOARD_ABSOLUTE,
						"absoluteCode": 0x1C, // KEY_RETURN
						"absoluteChar": 10 // '\n'
					});
					break;
				case "deleteWordBackward":
				case "deleteSoftLineBackward":
				case "deleteHardLineBackward":
				case "deleteEntireSoftLine":
				case "deleteContentBackward":
				case "deleteContent":
					pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_TOUCH_KEYBOARD, {
						"eventType": EVENT_TOUCH_KEYBOARD_ABSOLUTE,
						"absoluteCode": 0x0E, // KEY_BACK
						"absoluteChar": 0
					});
					break;
				case "deleteWordForward":
				case "deleteSoftLineForward":
				case "deleteHardLineForward":
				case "deleteContentForward":
					pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_TOUCH_KEYBOARD, {
						"eventType": EVENT_TOUCH_KEYBOARD_ABSOLUTE,
						"absoluteCode": 0xD3, // KEY_DELETE
						"absoluteChar": 0
					});
					break;
				case "insertText":
				case "insertCompositionText":
				case "insertReplacementText": {
					const txt = evt.data;
					if(txt && txt.length > 0) {
						pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_TOUCH_KEYBOARD, {
							"eventType": EVENT_TOUCH_KEYBOARD_CODEPOINTS,
							"codepoints": txt
						});
					}
					break;
				}
				case "insertFromPaste":
				case "insertFromPasteAsQuotation":
				case "insertFromDrop":
				case "insertFromYank":
				case "insertLink": {
					const txt = evt.data;
					if(txt && txt.length > 0) {
						pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_TOUCH_PASTE, txt);
					}
					break;
				}
				case "historyUndo":
				case "historyRedo":
				case "deleteByDrag":
				case "deleteByCut":
					break;
				default:
					eagInfo("Ignoring InputEvent.inputType \"{}\" from on-screen keyboard", evt.inputType);
					break;
				}
			}));
			touchKeyboardField.addEventListener("input", function(/** Event */ evt) {
				if(touchKeyboardField !== evt.target) return;
				if(!shownLegacyTouchKeyboardWarning) {
					eagInfo("Note: Caught legacy input events from on-screen keyboard, browser could be outdated and doesn't support beforeinput event, or does not respond to cancelling beforeinput");
					shownLegacyTouchKeyboardWarning = true;
				}
				const d = evt.timeStamp;
				if(d) {
					if(lastTouchKeyboardEvtA !== 0.0 && (d - lastTouchKeyboardEvtA) < 10.0) {
						return;
					}
					if(lastTouchKeyboardEvtB !== 0.0 && (d - lastTouchKeyboardEvtB) < 10.0) {
						return;
					}
					lastTouchKeyboardEvtC = d;
				}
				var val = touchKeyboardField.value;
				var l = val.length;
				if(l === 0) {
					pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_TOUCH_KEYBOARD, {
						"eventType": EVENT_TOUCH_KEYBOARD_ABSOLUTE,
						"absoluteCode": 0x0E, // KEY_BACK
						"absoluteChar": 0
					});
				}else if(l === 1) {
					pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_TOUCH_KEYBOARD, {
						"eventType": EVENT_TOUCH_KEYBOARD_CODEPOINTS,
						"codepoints": val
					});
				}else {
					val = val.trim();
					l = val.length;
					if(l === 0) {
						pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_TOUCH_KEYBOARD, {
							"eventType": EVENT_TOUCH_KEYBOARD_ABSOLUTE,
							"absoluteCode": 0x39, // KEY_SPACE
							"absoluteChar": 32
						});
					}else {
						pushEvent(EVENT_TYPE_INPUT, EVENT_INPUT_TOUCH_KEYBOARD, {
							"eventType": EVENT_TOUCH_KEYBOARD_CODEPOINTS,
							"codepoints": val.charAt(l - 1)
						});
					}
				}
				touchKeyboardField.value = " ";
				touchKeyboardField.setSelectionRange(1, 1);
			});
			touchKeyboardField.addEventListener("focus", function(/** Event */ evt) {
				if(touchKeyboardField !== evt.target) return;
				touchKeyboardField.value = " ";
				touchKeyboardField.setSelectionRange(1, 1);
			});
			touchKeyboardField.addEventListener("select", function(/** Event */ evt) {
				if(touchKeyboardField !== evt.target) return;
				evt.preventDefault();
				evt.stopPropagation();
				touchKeyboardField.value = " ";
				touchKeyboardField.setSelectionRange(1, 1);
			});
			touchKeyboardForm.appendChild(touchKeyboardField);
			parentElement.appendChild(touchKeyboardForm);
			touchKeyboardField.value = " ";
			touchKeyboardField.focus();
			touchKeyboardField.select();
			touchKeyboardField.setSelectionRange(1, 1);
		}else {
			touchCloseDeviceKeyboard();
		}
	}

	/**
	 * @param {number} x
	 * @param {number} y
	 * @param {number} w
	 * @param {number} h
	 */
	inputImports["touchSetOpenKeyboardZone"] = function(x, y, w, h) {
		if(w !== 0 && h !== 0) {
			touchKeyboardOpenZone.style.display = "block";
			touchKeyboardOpenZone.style.top = "" + ((lastWindowHeight - y - h) / lastWindowDPI) + "px";
			touchKeyboardOpenZone.style.left = "" + (x / lastWindowDPI) + "px";
			touchKeyboardOpenZone.style.width = "" + (w / lastWindowDPI) + "px";
			touchKeyboardOpenZone.style.height = "" + (h / lastWindowDPI) + "px";
		}else {
			touchKeyboardOpenZone.style.display = "none";
			touchKeyboardOpenZone.style.top = "0px";
			touchKeyboardOpenZone.style.left = "0px";
			touchKeyboardOpenZone.style.width = "0px";
			touchKeyboardOpenZone.style.height = "0px";
		}
	};

	function touchCloseDeviceKeyboard() {
		if(touchKeyboardField) {
			touchKeyboardField.blur();
			touchKeyboardField.value = "";
			const el = touchKeyboardField;
			const el2 = touchKeyboardForm;
			window.setTimeout(function() {
				if(el2 !== null) {
					el2.removeChild(el);
					el2.remove();
				}else {
					el.remove();
				}
			}, 10);
			touchKeyboardField = null;
			touchKeyboardForm = null;
		}else if(touchKeyboardForm) {
			if(parentElement) {
				parentElement.removeChild(touchKeyboardForm);
			}else {
				touchKeyboardForm.remove();
			}
			touchKeyboardForm = null;
		}
	}

	inputImports["touchCloseDeviceKeyboard"] = touchCloseDeviceKeyboard;

	function touchIsDeviceKeyboardOpenMAYBE() {
		return !!touchKeyboardField && document.activeElement === touchKeyboardField;
	}

	/**
	 * @return {boolean}
	 */
	inputImports["touchIsDeviceKeyboardOpenMAYBE"] = touchIsDeviceKeyboardOpenMAYBE;

	/**
	 * @return {boolean}
	 */
	inputImports["gamepadSupported"] = function() {
		return gamepadSupported;
	};

	/**
	 * @return {boolean}
	 */
	inputImports["isVisualViewport"] = function() {
		return useVisualViewport;
	};

	removeEventHandlers = function() {
		try {
			if(currentEventListeners.contextmenu) {
				parentElement.removeEventListener("contextmenu", /** @type {function(Event)} */ (currentEventListeners.contextmenu));
				currentEventListeners.contextmenu = null;
			}
			if(currentEventListeners.mousedown) {
				canvasElement.removeEventListener("mousedown", /** @type {function(Event)} */ (currentEventListeners.mousedown));
				currentEventListeners.mousedown = null;
			}
			if(currentEventListeners.mouseup) {
				canvasElement.removeEventListener("mouseup", /** @type {function(Event)} */ (currentEventListeners.mouseup));
				currentEventListeners.mouseup = null;
			}
			if(currentEventListeners.mousemove) {
				canvasElement.removeEventListener("mousemove", /** @type {function(Event)} */ (currentEventListeners.mousemove));
				currentEventListeners.mousemove = null;
			}
			if(currentEventListeners.mouseenter) {
				canvasElement.removeEventListener("mouseenter", /** @type {function(Event)} */ (currentEventListeners.mouseenter));
				currentEventListeners.mouseenter = null;
			}
			if(currentEventListeners.mouseleave) {
				canvasElement.removeEventListener("mouseleave", /** @type {function(Event)} */ (currentEventListeners.mouseleave));
				currentEventListeners.mouseleave = null;
			}
			if(currentEventListeners.touchstart) {
				canvasElement.removeEventListener("touchstart", /** @type {function(Event)} */ (currentEventListeners.touchstart));
				currentEventListeners.touchstart = null;
			}
			if(currentEventListeners.touchend) {
				canvasElement.removeEventListener("touchend", /** @type {function(Event)} */ (currentEventListeners.touchend));
				currentEventListeners.touchend = null;
			}
			if(currentEventListeners.touchmove) {
				canvasElement.removeEventListener("touchmove", /** @type {function(Event)} */ (currentEventListeners.touchmove));
				currentEventListeners.touchmove = null;
			}
			if(currentEventListeners.touchcancel) {
				canvasElement.removeEventListener("touchcancel", /** @type {function(Event)} */ (currentEventListeners.touchcancel));
				currentEventListeners.touchcancel = null;
			}
			if(currentEventListeners.gamepadconnected) {
				window.removeEventListener("gamepadconnected", /** @type {function(Event)} */ (currentEventListeners.gamepadconnected));
				currentEventListeners.gamepadconnected = null;
			}
			if(currentEventListeners.gamepaddisconnected) {
				window.removeEventListener("gamepaddisconnected", /** @type {function(Event)} */ (currentEventListeners.gamepaddisconnected));
				currentEventListeners.gamepaddisconnected = null;
			}
			if(currentEventListeners.keydown) {
				window.removeEventListener("keydown", /** @type {function(Event)} */ (currentEventListeners.keydown));
				currentEventListeners.keydown = null;
			}
			if(currentEventListeners.keyup) {
				window.removeEventListener("keyup", /** @type {function(Event)} */ (currentEventListeners.keyup));
				currentEventListeners.keyup = null;
			}
			if(currentEventListeners.touchKeyboardOpenZone_touchstart) {
				touchKeyboardOpenZone.removeEventListener("touchstart", /** @type {function(Event)} */ (currentEventListeners.touchKeyboardOpenZone_touchstart));
				currentEventListeners.touchKeyboardOpenZone_touchstart = null;
			}
			if(currentEventListeners.touchKeyboardOpenZone_touchend) {
				touchKeyboardOpenZone.removeEventListener("touchend", /** @type {function(Event)} */ (currentEventListeners.touchKeyboardOpenZone_touchend));
				currentEventListeners.touchKeyboardOpenZone_touchend = null;
			}
			if(currentEventListeners.touchKeyboardOpenZone_touchmove) {
				touchKeyboardOpenZone.removeEventListener("touchmove", /** @type {function(Event)} */ (currentEventListeners.touchKeyboardOpenZone_touchmove));
				currentEventListeners.touchKeyboardOpenZone_touchmove = null;
			}
			if(currentEventListeners.wheel) {
				canvasElement.removeEventListener("wheel", /** @type {function(Event)} */ (currentEventListeners.wheel));
				currentEventListeners.wheel = null;
			}
			if(currentEventListeners.focus) {
				window.removeEventListener("focus", /** @type {function(Event)} */ (currentEventListeners.focus));
				currentEventListeners.focus = null;
			}
			if(currentEventListeners.blur) {
				window.removeEventListener("blur", /** @type {function(Event)} */ (currentEventListeners.blur));
				currentEventListeners.blur = null;
			}
			if(currentEventListeners.pointerlock) {
				document.removeEventListener("pointerlockchange", /** @type {function(Event)} */ (currentEventListeners.pointerlock));
				currentEventListeners.pointerlock = null;
			}
			if(currentEventListeners.pointerlockerr) {
				document.removeEventListener("pointerlockerror", /** @type {function(Event)} */ (currentEventListeners.pointerlockerr));
				currentEventListeners.pointerlockerr = null;
			}
			if(currentEventListeners.fullscreenChange) {
				fullscreenQuery.removeEventListener("change", /** @type {function(Event)} */ (currentEventListeners.fullscreenChange));
				currentEventListeners.fullscreenChange = null;
			}
		}catch(ex) {
			eagError("Failed to remove event listeners! {}", ex);
		}
		removeEventHandlers = null;
	};
}

function initNoPlatformInput(inputImports) {
	setUnsupportedFunc(inputImports, platfInputName, "keyboardLayoutSupported");
	setUnsupportedFunc(inputImports, platfInputName, "iterateKeyboardLayout");
	setUnsupportedFunc(inputImports, platfInputName, "updateCanvasSize");
	setUnsupportedFunc(inputImports, platfInputName, "updatePlatformAndSleep");
	setUnsupportedFunc(inputImports, platfInputName, "isVSyncSupported");
	setUnsupportedFunc(inputImports, platfInputName, "mouseSetGrabbed");
	setUnsupportedFunc(inputImports, platfInputName, "isMouseGrabSupported");
	setUnsupportedFunc(inputImports, platfInputName, "isMouseGrabbed");
	setUnsupportedFunc(inputImports, platfInputName, "isPointerLocked");
	setUnsupportedFunc(inputImports, platfInputName, "supportsFullscreen");
	setUnsupportedFunc(inputImports, platfInputName, "toggleFullscreen");
	setUnsupportedFunc(inputImports, platfInputName, "isFullscreen");
	setUnsupportedFunc(inputImports, platfInputName, "touchSetOpenKeyboardZone");
	setUnsupportedFunc(inputImports, platfInputName, "touchCloseDeviceKeyboard");
	setUnsupportedFunc(inputImports, platfInputName, "touchIsDeviceKeyboardOpenMAYBE");
	setUnsupportedFunc(inputImports, platfInputName, "gamepadSupported");
	setUnsupportedFunc(inputImports, platfInputName, "isVisualViewport");
}
