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

const eagruntimeImpl = {
	WASMGCBufferAllocator: {},
	platformApplication: {},
	platformAssets: {},
	platformAudio: {},
	platformFilesystem: {},
	platformInput: {},
	platformNetworking: {},
	platformOpenGL: {},
	platformRuntime: {},
	platformScreenRecord: {},
	platformVoiceClient: {},
	platformWebRTC: {},
	platformWebView: {},
	clientPlatformSingleplayer: {},
	serverPlatformSingleplayer: {}
};

/** @type {WebAssembly.Module} */
var classesWASMModule = null;
/** @type {WebAssembly.Module} */
var classesDeobfWASMModule = null;
/** @type {Int8Array} */
var classesTEADBG = null;
/** @type {function(Array<number>):Array<Object>|null} */
var deobfuscatorFunc = null;
/** @type {Array} */
var epkFileList = null;
/** @type {string|null} */
var splashURL = null;
/** @type {string|null} */
var pressAnyKeyURL = null;
/** @type {string|null} */
var crashURL = null;
/** @type {string|null} */
var faviconURL = null;
/** @type {Object} */
var eaglercraftXOpts = null;
/** @type {string|null} */
var eagRuntimeJSURL = null;
/** @type {HTMLElement} */
var rootElement = null;
/** @type {HTMLElement} */
var parentElement = null;
/** @type {HTMLCanvasElement} */
var canvasElement = null;
/** @type {WebGL2RenderingContext} */
var webglContext = null;
/** @type {boolean} */
var webglExperimental = false;
/** @type {number} */
var webglGLESVer = 0;
/** @type {AudioContext} */
var audioContext = null;
/** @type {WebAssembly.Memory} */
var heapMemory = null;
/** @type {ArrayBuffer} */
var heapArrayBuffer = null;
/** @type {Uint8Array} */
var heapU8Array = null;
/** @type {Int8Array} */
var heapI8Array = null;
/** @type {Uint16Array} */
var heapU16Array = null;
/** @type {Int16Array} */
var heapI16Array = null;
/** @type {Int32Array} */
var heapI32Array = null;
/** @type {Uint32Array} */
var heapU32Array = null;
/** @type {Float32Array} */
var heapF32Array = null;
/** @type {function(Int8Array,Uint8Array,Int16Array,Uint16Array,Int32Array,Uint32Array,Float32Array)|null} */
var heapResizeHandler = null;
/** @type {boolean} */
var isLikelyMobileBrowser = false;
/** @type {function(string, !ArrayBuffer)|null} */
var serverLANPeerPassIPCFunc = null;
/** @type {function(string, !ArrayBuffer)|null} */
var sendIPCPacketFunc = null;
/** @type {boolean} */
var isCrashed = false;
/** @type {Array<string>} */
const crashReportStrings = [];
/** @type {function()|null} */
var removeEventHandlers = null;
/** @type {function()|null} */
var keepAliveCallback = null;
/** @type {function()|null} */
var showDebugConsole = null;
/** @type {function()|null} */
var resetSettings = null;

const runtimeOpts = {
	localStorageNamespace: "_eaglercraftX",
	openDebugConsoleOnLaunch: false,
	fixDebugConsoleUnloadListener: false,
	forceWebViewSupport: false,
	enableWebViewCSP: true,
	forceWebGL1: false,
	forceWebGL2: false,
	allowExperimentalWebGL1: true,
	useWebGLExt: true,
	useDelayOnSwap: false
};

function setupRuntimeOpts() {
	if(typeof eaglercraftXOpts["localStorageNamespace"] === "string") runtimeOpts.localStorageNamespace = eaglercraftXOpts["localStorageNamespace"];
	if(typeof eaglercraftXOpts["openDebugConsoleOnLaunch"] === "boolean") runtimeOpts.openDebugConsoleOnLaunch = eaglercraftXOpts["openDebugConsoleOnLaunch"];
	if(typeof eaglercraftXOpts["fixDebugConsoleUnloadListener"] === "boolean") runtimeOpts.fixDebugConsoleUnloadListener = eaglercraftXOpts["fixDebugConsoleUnloadListener"];
	if(typeof eaglercraftXOpts["forceWebViewSupport"] === "boolean") runtimeOpts.forceWebViewSupport = eaglercraftXOpts["forceWebViewSupport"];
	if(typeof eaglercraftXOpts["enableWebViewCSP"] === "boolean") runtimeOpts.enableWebViewCSP = eaglercraftXOpts["enableWebViewCSP"];
	if(typeof eaglercraftXOpts["forceWebGL1"] === "boolean") runtimeOpts.forceWebGL1 = eaglercraftXOpts["forceWebGL1"];
	if(typeof eaglercraftXOpts["forceWebGL2"] === "boolean") runtimeOpts.forceWebGL2 = eaglercraftXOpts["forceWebGL2"];
	if(typeof eaglercraftXOpts["allowExperimentalWebGL1"] === "boolean") runtimeOpts.allowExperimentalWebGL1 = eaglercraftXOpts["allowExperimentalWebGL1"];
	if(typeof eaglercraftXOpts["useWebGLExt"] === "boolean") runtimeOpts.useWebGLExt = eaglercraftXOpts["useWebGLExt"];
	if(typeof eaglercraftXOpts["useDelayOnSwap"] === "boolean") runtimeOpts.useDelayOnSwap = eaglercraftXOpts["useDelayOnSwap"];
}

/**
 * @return {!Promise<boolean>}
 */
async function initializeContext() {
	setupRuntimeOpts();
	
	currentRedirectorFunc = addLogMessageImpl;
	
	if(window.__isEaglerX188UnloadListenerSet !== "yes") {
		window.onbeforeunload = function(evt) {
			if(window.__curEaglerX188UnloadListenerCB) {
				window.__curEaglerX188UnloadListenerCB();
			}
			return false;
		};
		window.__isEaglerX188UnloadListenerSet = "yes";
	}
	
	eagInfo("Initializing EagRuntime JS context...");
	
	await initializePlatfRuntime();
	initializePlatfApplication(eagruntimeImpl.platformApplication);
	initializePlatfScreenRecord(eagruntimeImpl.platformScreenRecord);
	initializePlatfVoiceClient(eagruntimeImpl.platformVoiceClient);
	initializePlatfWebRTC(eagruntimeImpl.platformWebRTC);
	initializePlatfWebView(eagruntimeImpl.platformWebView);
	initializeClientPlatfSP(eagruntimeImpl.clientPlatformSingleplayer);
	initializeNoServerPlatfSP(eagruntimeImpl.serverPlatformSingleplayer);
	
	rootElement.classList.add("_eaglercraftX_root_element");
	rootElement.style.overflow = "hidden";
	
	/** @type {HTMLElement} */
	var oldSplash = null;
	
	var node;
	while(node = rootElement.lastChild) {
		if(!oldSplash) {
			oldSplash = /** @type {HTMLElement} */ (node);
		}
		rootElement.removeChild(node);
	}
	
	parentElement = /** @type {HTMLElement} */ (document.createElement("div"));
	parentElement.classList.add("_eaglercraftX_wrapper_element");
	parentElement.style.position = "relative";
	parentElement.style.width = "100%";
	parentElement.style.height = "100%";
	parentElement.style.overflow = "hidden";
	parentElement.style.backgroundColor = "black";
	rootElement.appendChild(parentElement);
	
	if(oldSplash) {
		oldSplash.style.position = "absolute";
		oldSplash.style.top = "0px";
		oldSplash.style.left = "0px";
		oldSplash.style.right = "0px";
		oldSplash.style.bottom = "0px";
		oldSplash.style.zIndex = "2";
		oldSplash.classList.add("_eaglercraftX_early_splash_element");
		parentElement.appendChild(oldSplash);
	}
	
	await promiseTimeout(10);
	
	const d = window.devicePixelRatio;
	const iw = parentElement.clientWidth;
	const ih = parentElement.clientHeight;
	const sw = (d * iw) | 0;
	const sh = (d * ih) | 0;
	const canvasW = sw;
	const canvasH = sh;
	
	eagInfo("Initializing audio context");
	
	if(typeof document.exitPointerLock === "function") {
		var ua = navigator.userAgent;
		if(ua !== null) {
			ua = ua.toLowerCase();
			isLikelyMobileBrowser = ua.indexOf("mobi") !== -1 || ua.indexOf("tablet") !== -1;
		}else {
			isLikelyMobileBrowser = false;
		}
	}else {
		isLikelyMobileBrowser = true;
	}
	
	var audioCtx = null;
	
	const createAudioContext = function() {
		try {
			audioCtx = new AudioContext();
		}catch(ex) {
			eagStackTrace(ERROR, "Could not initialize audio context", ex);
		}
	};
	
	if(isLikelyMobileBrowser || !navigator.userActivation || !navigator.userActivation.hasBeenActive) {
		const pressAnyKeyImage = /** @type {HTMLElement} */ (document.createElement("div"));
		pressAnyKeyImage.classList.add("_eaglercraftX_press_any_key_image");
		pressAnyKeyImage.style.position = "absolute";
		pressAnyKeyImage.style.top = "0px";
		pressAnyKeyImage.style.left = "0px";
		pressAnyKeyImage.style.right = "0px";
		pressAnyKeyImage.style.bottom = "0px";
		pressAnyKeyImage.style.width = "100%";
		pressAnyKeyImage.style.height = "100%";
		pressAnyKeyImage.style.zIndex = "3";
		pressAnyKeyImage.style.touchAction = "pan-x pan-y";
		pressAnyKeyImage.style.background = "center / contain no-repeat url(\"" + pressAnyKeyURL + "\"), left / 1000000% 100% no-repeat url(\"" + pressAnyKeyURL + "\") white";
		pressAnyKeyImage.style.setProperty("image-rendering", "pixelated");
		parentElement.appendChild(pressAnyKeyImage);
	
		await new Promise(function(resolve, reject) {
			var resolved = false;
			var mobilePressAnyKeyScreen;
			var createAudioContextHandler = function() {
				if(!resolved) {
					resolved = true;
					if(isLikelyMobileBrowser) {
						parentElement.removeChild(mobilePressAnyKeyScreen);
					}else {
						window.removeEventListener("keydown", /** @type {function(Event)} */ (createAudioContextHandler));
						parentElement.removeEventListener("mousedown", /** @type {function(Event)} */ (createAudioContextHandler));
						parentElement.removeEventListener("touchstart", /** @type {function(Event)} */ (createAudioContextHandler));
					}
					try {
						createAudioContext();
					}catch(ex) {
						reject(ex);
						return;
					}
					resolve();
				}
			};
			if(isLikelyMobileBrowser) {
				mobilePressAnyKeyScreen = /** @type {HTMLElement} */ (document.createElement("div"));
				mobilePressAnyKeyScreen.classList.add("_eaglercraftX_mobile_press_any_key");
				mobilePressAnyKeyScreen.setAttribute("style", "position:absolute;background-color:white;font-family:sans-serif;top:10%;left:10%;right:10%;bottom:10%;border:5px double black;padding:calc(5px + 7vh) 15px;text-align:center;font-size:20px;user-select:none;z-index:10;");
				mobilePressAnyKeyScreen.innerHTML = "<h3 style=\"margin-block-start:0px;margin-block-end:0px;margin:20px 5px;\">Mobile Browser Detected</h3>"
						+ "<p style=\"margin-block-start:0px;margin-block-end:0px;margin:20px 5px;\">Warning: EaglercraftX WASM-GC requires a lot of memory and may not be stable on most mobile devices!</p>"
						+ "<p style=\"margin-block-start:0px;margin-block-end:0px;margin:20px 2px;\"><button style=\"font: 24px sans-serif;font-weight:bold;\" class=\"_eaglercraftX_mobile_launch_client\">Launch EaglercraftX</button></p>"
						/*+ (allowBootMenu ? "<p style=\"margin-block-start:0px;margin-block-end:0px;margin:20px 2px;\"><button style=\"font: 24px sans-serif;\" class=\"_eaglercraftX_mobile_enter_boot_menu\">Enter Boot Menu</button></p>" : "")*/
						+ "<p style=\"margin-block-start:0px;margin-block-end:0px;margin:25px 5px;\">(Tablets and phones with large screens work best)</p>";
				mobilePressAnyKeyScreen.querySelector("._eaglercraftX_mobile_launch_client").addEventListener("click", /** @type {function(Event)} */ (createAudioContextHandler));
				parentElement.appendChild(mobilePressAnyKeyScreen);
			}else {
				window.addEventListener("keydown", /** @type {function(Event)} */ (createAudioContextHandler));
				parentElement.addEventListener("mousedown", /** @type {function(Event)} */ (createAudioContextHandler));
				parentElement.addEventListener("touchstart", /** @type {function(Event)} */ (createAudioContextHandler));
			}
		});
		
		parentElement.removeChild(pressAnyKeyImage);
	}else {
		createAudioContext();
	}
	
	if(audioCtx) {
		setCurrentAudioContext(audioCtx, eagruntimeImpl.platformAudio);
	}else {
		setNoAudioContext(eagruntimeImpl.platformAudio);
	}
	
	eagInfo("Creating main canvas");
	
	canvasElement = /** @type {HTMLCanvasElement} */ (document.createElement("canvas"));
	canvasElement.classList.add("_eaglercraftX_canvas_element");
	canvasElement.style.width = "100%";
	canvasElement.style.height = "100%";
	canvasElement.style.zIndex = "1";
	canvasElement.style.touchAction = "pan-x pan-y";
	canvasElement.style.setProperty("-webkit-touch-callout", "none");
	canvasElement.style.setProperty("-webkit-tap-highlight-color", "rgba(255, 255, 255, 0)");
	canvasElement.style.setProperty("image-rendering", "pixelated");
	
	canvasElement.width = canvasW;
	canvasElement.height = canvasH;
	
	parentElement.appendChild(canvasElement);
	
	await initPlatformInput(eagruntimeImpl.platformInput);
	
	eagInfo("Creating WebGL context");
	
	parentElement.addEventListener("webglcontextcreationerror", function(evt) {
		eagError("[WebGL Error]: {}", evt.statusMessage);
	});
	
	/** @type {Object} */
	const contextCreationHints = {
		"antialias": false,
		"depth": false,
		"powerPreference": "high-performance",
		"desynchronized": true,
		"preserveDrawingBuffer": false,
		"premultipliedAlpha": false,
		"alpha": false
	};
	
	/** @type {number} */
	var glesVer;
	/** @type {boolean} */
	var experimental = false;
	/** @type {WebGL2RenderingContext|null} */
	var webgl_;
	if(runtimeOpts.forceWebGL2) {
		eagInfo("Note: Forcing WebGL 2.0 context");
		glesVer = 300;
		webgl_ = /** @type {WebGL2RenderingContext} */ (canvasElement.getContext("webgl2", contextCreationHints));
		if(!webgl_) {
			showIncompatibleScreen("WebGL 2.0 is not supported on this device!");
			return false;
		}
	}else {
		if(runtimeOpts.forceWebGL1) {
			eagInfo("Note: Forcing WebGL 1.0 context");
			glesVer = 200;
			webgl_ = /** @type {WebGL2RenderingContext} */ (canvasElement.getContext("webgl", contextCreationHints));
			if(!webgl_) {
				if(runtimeOpts.allowExperimentalWebGL1) {
					experimental = true;
					webgl_ = /** @type {WebGL2RenderingContext} */ (canvasElement.getContext("experimental-webgl", contextCreationHints));
					if(!webgl_) {
						showIncompatibleScreen("WebGL is not supported on this device!");
						return false;
					}
				}else {
					showIncompatibleScreen("WebGL is not supported on this device!");
					return false;
				}
			}
		}else {
			glesVer = 300;
			webgl_ = /** @type {WebGL2RenderingContext} */ (canvasElement.getContext("webgl2", contextCreationHints));
			if(!webgl_) {
				glesVer = 200;
				webgl_ = /** @type {WebGL2RenderingContext} */ (canvasElement.getContext("webgl", contextCreationHints));
				if(!webgl_) {
					if(runtimeOpts.allowExperimentalWebGL1) {
						experimental = true;
						webgl_ = /** @type {WebGL2RenderingContext} */ (canvasElement.getContext("experimental-webgl", contextCreationHints));
						if(!webgl_) {
							showIncompatibleScreen("WebGL is not supported on this device!");
							return false;
						}
					}else {
						showIncompatibleScreen("WebGL is not supported on this device!");
						return false;
					}
				}
			}
		}
	}
	
	if(experimental) {
		alert("WARNING: Detected \"experimental\" WebGL 1.0 support, certain graphics API features may be missing, and therefore EaglercraftX may malfunction and crash!");
	}
	
	webglGLESVer = glesVer;
	webglContext = webgl_;
	webglExperimental = experimental;
	
	setCurrentGLContext(webgl_, glesVer, runtimeOpts.useWebGLExt, eagruntimeImpl.platformOpenGL);
	
	eagInfo("OpenGL Version: {}", eagruntimeImpl.platformOpenGL["glGetString"](0x1F02));
	eagInfo("OpenGL Renderer: {}", eagruntimeImpl.platformOpenGL["glGetString"](0x1F01));
	
	/** @type {Array<string>} */
	const exts = eagruntimeImpl.platformOpenGL["dumpActiveExtensions"]();
	if(exts.length === 0) {
		eagInfo("Unlocked the following OpenGL ES extensions: (NONE)");
	}else {
		exts.sort();
		eagInfo("Unlocked the following OpenGL ES extensions:");
		for(var i = 0; i < exts.length; ++i) {
			eagInfo(" - {}", exts[i]);
		}
	}
	
	eagruntimeImpl.platformOpenGL["glClearColor"](0.0, 0.0, 0.0, 1.0);
	eagruntimeImpl.platformOpenGL["glClear"](0x4000);
	
	await promiseTimeout(20);
	
	eagInfo("EagRuntime JS context initialization complete");
	return true;
}

async function initializeContextWorker() {
	setupRuntimeOpts();
	
	/**
	 * @param {string} txt
	 * @param {boolean} err
	 */
	currentRedirectorFunc = function(txt, err) {
		postMessage({
			"ch": "~!LOGGER",
			"txt": txt,
			"err": err
		});
	};
	
	eagInfo("Initializing EagRuntime worker JS context...");
	
	await initializePlatfRuntime();
	initializeNoPlatfApplication(eagruntimeImpl.platformApplication);
	setNoAudioContext(eagruntimeImpl.platformAudio);
	initNoPlatformInput(eagruntimeImpl.platformInput);
	setNoGLContext(eagruntimeImpl.platformOpenGL);
	initializeNoPlatfScreenRecord(eagruntimeImpl.platformScreenRecord);
	initializeNoPlatfVoiceClient(eagruntimeImpl.platformVoiceClient);
	initializeNoPlatfWebRTC(eagruntimeImpl.platformWebRTC);
	initializeNoPlatfWebView(eagruntimeImpl.platformWebView);
	initializeNoClientPlatfSP(eagruntimeImpl.clientPlatformSingleplayer);
	initializeServerPlatfSP(eagruntimeImpl.serverPlatformSingleplayer);
	
	eagInfo("EagRuntime worker JS context initialization complete");
}

/**
 * @param {WebAssembly.Memory} mem
 */
function handleMemoryResized(mem) {
	heapMemory = mem;
	heapArrayBuffer = mem.buffer;
	eagInfo("WebAssembly direct memory resized to {} MiB", ((heapArrayBuffer.byteLength / 1024.0 / 10.24) | 0) * 0.01);
	heapU8Array = new Uint8Array(heapArrayBuffer);
	heapI8Array = new Int8Array(heapArrayBuffer);
	heapU16Array = new Uint16Array(heapArrayBuffer);
	heapI16Array = new Int16Array(heapArrayBuffer);
	heapU32Array = new Uint32Array(heapArrayBuffer);
	heapI32Array = new Int32Array(heapArrayBuffer);
	heapF32Array = new Float32Array(heapArrayBuffer);
	callHeapViewCallback();
}

function callHeapViewCallback() {
	if (heapResizeHandler) {
		heapResizeHandler(heapI8Array, heapU8Array, heapI16Array, heapU16Array, heapI32Array, heapU32Array, heapF32Array);
	}
}

const EVENT_TYPE_INPUT = 0;
const EVENT_TYPE_RUNTIME = 1;
const EVENT_TYPE_VOICE = 2;
const EVENT_TYPE_WEBVIEW = 3;

const mainEventQueue = new EaglerLinkedQueue();

/**
 * @param {number} eventType
 * @param {number} eventId
 * @param {*} eventObj
 */
function pushEvent(eventType, eventId, eventObj) {
	mainEventQueue.push({
		"eventType": ((eventType << 5) | eventId),
		"eventObj": eventObj,
		"_next": null
	});
}

let exceptionFrameRegex2 = /.+:wasm-function\[[0-9]+]:0x([0-9a-f]+).*/;

/**
 * @param {string|null} stack
 * @return {Array<string>}
 */
function deobfuscateStack(stack) {
	if(!stack) return null;
	/** @type {!Array<string>} */
	const stackFrames = [];
	for(let line of stack.split("\n")) {
		if(deobfuscatorFunc) {
			const match = exceptionFrameRegex2.exec(line);
			if(match !== null && match.length >= 2) {
				const val = parseInt(match[1], 16);
				if(!isNaN(val)) {
					try {
						/** @type {Array<Object>} */
						const resultList = deobfuscatorFunc([val]);
						if(resultList.length > 0) {
							for(let obj of resultList) {
								stackFrames.push("" + obj["className"] + "." + obj["method"] + "(" + obj["file"] + ":" + obj["line"] + ")");
							}
							continue;
						}
					}catch(ex) {
					}
				}
			}
		}
		line = line.trim();
		if(line.startsWith("at ")) {
			line = line.substring(3);
		}
		stackFrames.push(line);
	}
	return stackFrames;
}

/**
 * @return {HTMLElement}
 */
function createToolButtons() {
	const buttonResetSettings = /** @type {HTMLButtonElement} */ (document.createElement("button"));
	buttonResetSettings.setAttribute("style", "margin-left:10px;");
	buttonResetSettings.innerText = "Reset Settings";
	buttonResetSettings.addEventListener("click", function(/** Event */ evt) {
		if (resetSettings) {
			resetSettings();
		} else {
			window.alert("Local storage has not been initialized yet");
		}
	});
	const buttonOpenConsole = /** @type {HTMLButtonElement} */ (document.createElement("button"));
	buttonOpenConsole.setAttribute("style", "margin-left:10px;");
	buttonOpenConsole.innerText = "Open Debug Console";
	buttonOpenConsole.addEventListener("click", function(/** Event */ evt) {
		if (showDebugConsole) {
			showDebugConsole();
		} else {
			window.alert("Debug console has not been initialized yet");
		}
	});
	const div1 = /** @type {HTMLElement} */ (document.createElement("div"));
	div1.setAttribute("style", "position:absolute;bottom:5px;right:0px;");
	div1.appendChild(buttonResetSettings);
	div1.appendChild(buttonOpenConsole);
	const div2 = /** @type {HTMLElement} */ (document.createElement("div"));
	div2.setAttribute("style", "position:relative;");
	div2.appendChild(div1);
	const div3 = /** @type {HTMLElement} */ (document.createElement("div"));
	div3.classList.add("_eaglercraftX_crash_tools_element");
	div3.setAttribute("style", "z-index:101;position:absolute;top:135px;left:10%;right:10%;height:0px;");
	div3.appendChild(div2);
	return div3;
}

function displayUncaughtCrashReport(error) {
	const stack = error ? deobfuscateStack(error.stack) : null;
	const crashContent = "Native Browser Exception\n" +
		"----------------------------------\n" +
		"  Line: " + ((error && (typeof error.fileName === "string")) ? error.fileName : "unknown") +
		":" + ((error && (typeof error.lineNumber === "number")) ? error.lineNumber : "unknown") +
		":" + ((error && (typeof error.columnNumber === "number")) ? error.columnNumber : "unknown") +
		"\n  Type: " + ((error && (typeof error.name === "string")) ? error.name : "unknown") +
		"\n  Desc: " + ((error && (typeof error.message === "string")) ? error.message : "null") +
		"\n----------------------------------\n\n" +
		"Deobfuscated stack trace:\n    at " + (stack ? stack.join("\n    at ") : "null") +
		"\n\nThis exception was not handled by the WASM binary\n";
	if(typeof window !== "undefined") {
		displayCrashReport(crashContent, true);
	}else if(sendIntegratedServerCrash) {
		eagError("\n{}", crashContent);
		try {
			sendIntegratedServerCrash(crashContent, true);
		}catch(ex) {
			console.log(ex);
		}
	}else {
		eagError("\n{}", crashContent);
	}
}

/**
 * @param {string} crashReport
 * @param {boolean} enablePrint
 */
function displayCrashReport(crashReport, enablePrint) {
	eagError("Game crashed!");
	
	var strBefore = "Game Crashed! I have fallen and I can't get up!\n\n"
			+ crashReport
			+ "\n\n";
	
	var strAfter = "eaglercraft.version = \""
			+ crashReportStrings[0]
			+ "\"\neaglercraft.minecraft = \""
			+ crashReportStrings[2]
			+ "\"\neaglercraft.brand = \""
			+ crashReportStrings[1]
			+ "\"\n\n"
			+ addWebGLToCrash()
			+ "\nwindow.eaglercraftXOpts = "
			+ JSON.stringify(eaglercraftXOpts)
			+ "\n\ncurrentTime = "
			+ (new Date()).toLocaleString()
			+ "\n\n"
			+ addDebugNav("userAgent")
			+ addDebugNav("vendor")
			+ addDebugNav("language")
			+ addDebugNav("hardwareConcurrency")
			+ addDebugNav("deviceMemory")
			+ addDebugNav("platform")
			+ addDebugNav("product")
			+ addDebugNavPlugins()
			+ "\n"
			+ addDebug("localStorage")
			+ addDebug("sessionStorage")
			+ addDebug("indexedDB")
			+ "\n"
			+ "rootElement.clientWidth = "
			+ (parentElement ? parentElement.clientWidth : "undefined")
			+ "\nrootElement.clientHeight = "
			+ (parentElement ? parentElement.clientHeight : "undefined")
			+ "\n"
			+ addDebug("innerWidth")
			+ addDebug("innerHeight")
			+ addDebug("outerWidth")
			+ addDebug("outerHeight")
			+ addDebug("devicePixelRatio")
			+ addDebugScreen("availWidth")
			+ addDebugScreen("availHeight")
			+ addDebugScreen("colorDepth")
			+ addDebugScreen("pixelDepth")
			+ "\n"
			+ addDebugLocation("href")
			+ "\n";
	
	var strFinal = strBefore + strAfter;
	const additionalInfo = [];
	try {
		if((typeof eaglercraftXOpts === "object") && (typeof eaglercraftXOpts["hooks"] === "object")
				&& (typeof eaglercraftXOpts["hooks"]["crashReportShow"] === "function")) {
			eaglercraftXOpts["hooks"]["crashReportShow"](strFinal, function(str) {
				additionalInfo.push(str);
			});
		}
	}catch(ex) {
		eagStackTrace(ERROR, "Uncaught exception invoking crash report hook", ex);
	}
	
	if(!isCrashed) {
		isCrashed = true;
		
		if(additionalInfo.length > 0) {
			strFinal = strBefore + "Got the following messages from the crash report hook registered in eaglercraftXOpts:\n\n";
			for(var i = 0; i < additionalInfo.length; ++i) {
				strFinal += "----------[ CRASH HOOK ]----------\n"
						+ additionalInfo[i]
						+ "\n----------------------------------\n\n";
			}
			strFinal += strAfter;
		}
		
		var parentEl = parentElement || rootElement;
		
		if(!parentEl) {
			alert("Root element not found, crash report was printed to console");
			eagError("\n{}", strFinal);
			return;
		}
		
		if(enablePrint) {
			eagError("\n{}", strFinal);
		}
		
		const img = document.createElement("img");
		const div = document.createElement("div");
		img.setAttribute("style", "z-index:100;position:absolute;top:10px;left:calc(50% - 151px);");
		img.src = crashURL;
		div.setAttribute("style", "z-index:100;position:absolute;top:135px;left:10%;right:10%;bottom:50px;background-color:white;border:1px solid #cccccc;overflow-x:hidden;overflow-y:scroll;overflow-wrap:break-word;white-space:pre-wrap;font: 14px monospace;padding:10px;");
		div.classList.add("_eaglercraftX_crash_element");
		parentEl.appendChild(img);
		parentEl.appendChild(div);
		parentEl.appendChild(createToolButtons());
		div.appendChild(document.createTextNode(strFinal));
		
		if(removeEventHandlers) removeEventHandlers();
		window.__curEaglerX188UnloadListenerCB = null;
	}else {
		eagError("");
		eagError("An additional crash report was supressed:");
		var s = crashReport.split(/[\r\n]+/);
		for(var i = 0; i < s.length; ++i) {
			eagError("  {}", s[i]);
		}
		if(additionalInfo.length > 0) {
			for(var i = 0; i < additionalInfo.length; ++i) {
				var str2 = additionalInfo[i];
				if(str2) {
					eagError("");
					eagError("  ----------[ CRASH HOOK ]----------");
					s = str2.split(/[\r\n]+/);
					for(var i = 0; i < s.length; ++i) {
						eagError("  {}", s[i]);
					}
					eagError("  ----------------------------------");
				}
			}
		}
	}
}

/**
 * @param {string} msg
 */
function showIncompatibleScreen(msg) {
	if(!isCrashed) {
		isCrashed = true;
		
		var parentEl = parentElement || rootElement;
		
		eagError("Compatibility error: {}", msg);
		
		if(!parentEl) {
			alert("Compatibility error: " + msg);
			return;
		}
		
		const img = document.createElement("img");
		const div = document.createElement("div");
		img.setAttribute("style", "z-index:100;position:absolute;top:10px;left:calc(50% - 151px);");
		img.src = crashURL;
		div.setAttribute("style", "z-index:100;position:absolute;top:135px;left:10%;right:10%;bottom:50px;background-color:white;border:1px solid #cccccc;overflow-x:hidden;overflow-y:scroll;font:18px sans-serif;padding:40px;");
		div.classList.add("_eaglercraftX_incompatible_element");
		parentEl.appendChild(img);
		parentEl.appendChild(div);
		parentEl.appendChild(createToolButtons());
		div.innerHTML = "<h2><svg style=\"vertical-align:middle;margin:0px 16px 8px 8px;\" xmlns=\"http://www.w3.org/2000/svg\" width=\"48\" height=\"48\" viewBox=\"0 0 48 48\" fill=\"none\"><path stroke=\"#000000\" stroke-width=\"3\" stroke-linecap=\"square\" d=\"M1.5 8.5v34h45v-28m-3-3h-10v-3m-3-3h-10m15 6h-18v-3m-3-3h-10\"/><path stroke=\"#000000\" stroke-width=\"2\" stroke-linecap=\"square\" d=\"M12 21h0m0 4h0m4 0h0m0-4h0m-2 2h0m20-2h0m0 4h0m4 0h0m0-4h0m-2 2h0\"/><path stroke=\"#000000\" stroke-width=\"2\" stroke-linecap=\"square\" d=\"M20 30h0 m2 2h0 m2 2h0 m2 2h0 m2 -2h0 m2 -2h0 m2 -2h0\"/></svg>+ This device is incompatible with Eaglercraft&ensp;:(</h2>"
					+ "<div style=\"margin-left:40px;\">"
					+ "<p style=\"font-size:1.2em;\"><b style=\"font-size:1.1em;\">Issue:</b> <span style=\"color:#BB0000;\" id=\"_eaglercraftX_crashReason\"></span><br /></p>"
					+ "<p style=\"margin-left:10px;font:0.9em monospace;\" id=\"_eaglercraftX_crashUserAgent\"></p>"
					+ "<p style=\"margin-left:10px;font:0.9em monospace;\" id=\"_eaglercraftX_crashWebGL\"></p>"
					+ "<p style=\"margin-left:10px;font:0.9em monospace;\">Current Date: " + (new Date()).toLocaleString() + "</p>"
					+ "<p><br /><span style=\"font-size:1.1em;border-bottom:1px dashed #AAAAAA;padding-bottom:5px;\">Things you can try:</span></p>"
					+ "<ol>"
					+ "<li><span style=\"font-weight:bold;\">Just try using Eaglercraft on a different device</span>, it isn't a bug it's common sense</li>"
					+ "<li style=\"margin-top:7px;\">If this screen just appeared randomly, try restarting your browser or device</li>"
					+ "<li style=\"margin-top:7px;\">If you are not using Chrome/Edge, try installing the latest Google Chrome</li>"
					+ "<li style=\"margin-top:7px;\">If your browser is out of date, please update it to the latest version</li>"
					+ "</ol>"
					+ "</div>";
		
		div.querySelector("#_eaglercraftX_crashReason").appendChild(document.createTextNode(msg));
		div.querySelector("#_eaglercraftX_crashUserAgent").appendChild(document.createTextNode(getStringNav("userAgent")));
		
		if(removeEventHandlers) removeEventHandlers();
		window.__curEaglerX188UnloadListenerCB = null;
			
		var webGLRenderer = "No GL_RENDERER string could be queried";
		
		try {
			const cvs = /** @type {HTMLCanvasElement} */ (document.createElement("canvas"));
			
			cvs.width = 64;
			cvs.height = 64;
			
			const ctx = /** @type {WebGLRenderingContext} */ (cvs.getContext("webgl"));
			
			if(ctx) {
				/** @type {string|null} */
				var r;
				if(ctx.getExtension("WEBGL_debug_renderer_info")) {
					r = /** @type {string|null} */ (ctx.getParameter(/* UNMASKED_RENDERER_WEBGL */ 0x9246));
				}else {
					r = /** @type {string|null} */ (ctx.getParameter(WebGLRenderingContext.RENDERER));
					if(r) {
						r += " [masked]";
					}
				}
				if(r) {
					webGLRenderer = r;
				}
			}
		}catch(tt) {
		}
		
		div.querySelector("#_eaglercraftX_crashWebGL").appendChild(document.createTextNode(webGLRenderer));
	}
}

/**
 * @param {string} msg
 */
function showContextLostScreen(msg) {
	if(!isCrashed) {
		isCrashed = true;
		
		var parentEl = parentElement || rootElement;
		
		if(!parentEl) {
			alert("WebGL context lost!");
			eagError("WebGL context lost!");
			return;
		}
		
		const img = document.createElement("img");
		const div = document.createElement("div");
		img.setAttribute("style", "z-index:100;position:absolute;top:10px;left:calc(50% - 151px);");
		img.src = crashURL;
		div.setAttribute("style", "z-index:100;position:absolute;top:135px;left:10%;right:10%;bottom:50px;background-color:white;border:1px solid #cccccc;overflow-x:hidden;overflow-y:scroll;font:18px sans-serif;padding:40px;");
		div.classList.add("_eaglercraftX_context_lost_element");
		parentEl.appendChild(img);
		parentEl.appendChild(div);
		parentEl.appendChild(createToolButtons());
		div.innerHTML = "<h2><svg style=\"vertical-align:middle;margin:0px 16px 8px 8px;\" xmlns=\"http://www.w3.org/2000/svg\" width=\"48\" height=\"48\" viewBox=\"0 0 48 48\" fill=\"none\"><path stroke=\"#000000\" stroke-width=\"3\" stroke-linecap=\"square\" d=\"M1.5 8.5v34h45v-28m-3-3h-10v-3m-3-3h-10m15 6h-18v-3m-3-3h-10\"/><path stroke=\"#000000\" stroke-width=\"2\" stroke-linecap=\"square\" d=\"M12 21h0m0 4h0m4 0h0m0-4h0m-2 2h0m20-2h0m0 4h0m4 0h0m0-4h0m-2 2h0\"/><path stroke=\"#000000\" stroke-width=\"2\" stroke-linecap=\"square\" d=\"M20 30h0 m2 2h0 m2 2h0 m2 2h0 m2 -2h0 m2 -2h0 m2 -2h0\"/></svg> + WebGL context lost!</h2>"
				+ "<div style=\"margin-left:40px;\">"
				+ "<p style=\"font-size:1.2em;\">Your browser has forcibly released all of the resources "
				+ "allocated by the game's 3D rendering context. EaglercraftX cannot continue, please refresh "
				+ "the page to restart the game, sorry for the inconvenience.</p>"
				+ "<p style=\"font-size:1.2em;\">This is not a bug, it is usually caused by the browser "
				+ "deciding it no longer has sufficient resources to continue rendering this page. If it "
				+ "happens again, try closing your other browser tabs and windows.</p>"
				+ "<p style=\"font-size:1.2em;\">If you're playing with vsync disabled, try enabling vsync "
				+ "to allow the browser to control the GPU usage more precisely.</p>"
				+ "<p style=\"overflow-wrap:break-word;white-space:pre-wrap;font:0.75em monospace;margin-top:1.5em;\" id=\"_eaglercraftX_contextLostTrace\"></p>"
				+ "</div>";
		
		div.querySelector("#_eaglercraftX_contextLostTrace").appendChild(document.createTextNode(msg));
	}
}

/** @type {string|null} */
var webGLCrashStringCache = null;

/**
 * @return {string}
 */
function addWebGLToCrash() {
	if(webGLCrashStringCache) {
		return webGLCrashStringCache;
	}

	try {
		/** @type {WebGL2RenderingContext} */
		var ctx = webglContext;
		var experimental = webglExperimental;
		
		if(!ctx) {
			experimental = false;
			var cvs = document.createElement("canvas");
			cvs.width = 64;
			cvs.height = 64;
			ctx = /** @type {WebGL2RenderingContext} */ (cvs.getContext("webgl2"));
			if(!ctx) {
				ctx = /** @type {WebGL2RenderingContext} */ (cvs.getContext("webgl"));
				if(!ctx) {
					experimental = true;
					ctx = /** @type {WebGL2RenderingContext} */ (cvs.getContext("experimental-webgl"));
				}
			}
		}
		
		if(ctx) {
			var ret = "";
			
			if(webglGLESVer > 0) {
				ret += "webgl.version = "
						+ ctx.getParameter(/* VERSION */ 0x1F02)
						+ "\n";
			}
			
			if(ctx.getExtension("WEBGL_debug_renderer_info")) {
				ret += "webgl.renderer = "
						+ ctx.getParameter(/* UNMASKED_RENDERER_WEBGL */ 0x9246)
						+ "\nwebgl.vendor = "
						+ ctx.getParameter(/* UNMASKED_VENDOR_WEBGL */ 0x9245)
						+ "\n";
			}else {
				ret += "webgl.renderer = "
						+ ctx.getParameter(/* RENDERER */ 0x1F01)
						+ " [masked]\nwebgl.vendor = "
						+ ctx.getParameter(/* VENDOR */ 0x1F00)
						+ " [masked]\n";
			}
			
			if(webglGLESVer > 0) {
				ret += "\nwebgl.version.id = "
						+ webglGLESVer
						+ "\nwebgl.experimental = "
						+ experimental;
				if(webglGLESVer === 200) {
					ret += "\nwebgl.ext.ANGLE_instanced_arrays = "
							+ !!ctx.getExtension("ANGLE_instanced_arrays")
							+ "\nwebgl.ext.EXT_color_buffer_half_float = "
							+ !!ctx.getExtension("EXT_color_buffer_half_float")
							+ "\nwebgl.ext.EXT_shader_texture_lod = "
							+ !!ctx.getExtension("EXT_shader_texture_lod")
							+ "\nwebgl.ext.OES_fbo_render_mipmap = "
							+ !!ctx.getExtension("OES_fbo_render_mipmap")
							+ "\nwebgl.ext.OES_texture_float = "
							+ !!ctx.getExtension("OES_texture_float")
							+ "\nwebgl.ext.OES_texture_half_float = "
							+ !!ctx.getExtension("OES_texture_half_float")
							+ "\nwebgl.ext.OES_texture_half_float_linear = "
							+ !!ctx.getExtension("OES_texture_half_float_linear");
				}else if(webglGLESVer >= 300) {
					ret += "\nwebgl.ext.EXT_color_buffer_float = "
							+ !!ctx.getExtension("EXT_color_buffer_float")
							+ "\nwebgl.ext.EXT_color_buffer_half_float = "
							+ !!ctx.getExtension("EXT_color_buffer_half_float")
							+ "\nwebgl.ext.OES_texture_float_linear = "
							+ !!ctx.getExtension("OES_texture_float_linear");
				}
				ret += "\nwebgl.ext.EXT_texture_filter_anisotropic = "
						+ !!ctx.getExtension("EXT_texture_filter_anisotropic")
						+ "\n";
			}else {
				ret += "webgl.ext.ANGLE_instanced_arrays = "
						+ !!ctx.getExtension("ANGLE_instanced_arrays")
						+ "\nwebgl.ext.EXT_color_buffer_float = "
						+ !!ctx.getExtension("EXT_color_buffer_float")
						+ "\nwebgl.ext.EXT_color_buffer_half_float = "
						+ !!ctx.getExtension("EXT_color_buffer_half_float")
						+ "\nwebgl.ext.EXT_shader_texture_lod = "
						+ !!ctx.getExtension("EXT_shader_texture_lod")
						+ "\nwebgl.ext.OES_fbo_render_mipmap = "
						+ !!ctx.getExtension("OES_fbo_render_mipmap")
						+ "\nwebgl.ext.OES_texture_float = "
						+ !!ctx.getExtension("OES_texture_float")
						+ "\nwebgl.ext.OES_texture_float_linear = "
						+ !!ctx.getExtension("OES_texture_float_linear")
						+ "\nwebgl.ext.OES_texture_half_float = "
						+ !!ctx.getExtension("OES_texture_half_float")
						+ "\nwebgl.ext.OES_texture_half_float_linear = "
						+ !!ctx.getExtension("OES_texture_half_float_linear")
						+ "\nwebgl.ext.EXT_texture_filter_anisotropic = "
						+ !!ctx.getExtension("EXT_texture_filter_anisotropic")
						+ "\n";
			}
			
			return webGLCrashStringCache = ret;
		}else {
			return webGLCrashStringCache = "Failed to query GPU info!\n";
		}
	}catch(ex) {
		return webGLCrashStringCache = "ERROR: could not query webgl info - " + ex + "\n";
	}
}

/**
 * @param {string} k
 * @return {string}
 */
function addDebugNav(k) {
	var val;
	try {
		val = window.navigator[k];
	} catch(e) {
		val = "<error>";
	}
	return "window.navigator." + k + " = " + val + "\n";
}

/**
 * @param {string} k
 * @return {string}
 */
function getStringNav(k) {
	try {
		return window.navigator[k];
	} catch(e) {
		return "<error>";
	}
}

/**
 * @return {string}
 */
function addDebugNavPlugins() {
	var val;
	try {
		var retObj = new Array();
		if(typeof navigator.plugins === "object") {
			var len = navigator.plugins.length;
			if(len > 0) {
				for(var idx = 0; idx < len; ++idx) {
					var thePlugin = navigator.plugins[idx];
					retObj.push({
						"name": thePlugin.name,
						"filename": thePlugin.filename,
						"desc": thePlugin.description
					});
				}
			}
		}
		val = JSON.stringify(retObj);
	} catch(e) {
		val = "<error>";
	}
	return "window.navigator.plugins = " + val + "\n";
}

/**
 * @param {string} k
 * @return {string}
 */
function addDebugScreen(k) {
	var val;
	try {
		val = window.screen[k];
	} catch(e) {
		val = "<error>";
	}
	return "window.screen." + k + " = " + val + "\n";
}

/**
 * @param {string} k
 * @return {string}
 */
function addDebugLocation(k) {
	var val;
	try {
		val = window.location[k];
	} catch(e) {
		val = "<error>";
	}
	return "window.location." + k + " = " + val + "\n";
}

/**
 * @param {string} k
 * @return {string}
 */
function addDebug(k) {
	var val;
	try {
		val = window[k];
	} catch(e) {
		val = "<error>";
	}
	return "window." + k + " = " + val + "\n";
}
