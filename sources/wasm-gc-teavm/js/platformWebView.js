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

const platfWebViewName = "platformWebView";

function initializePlatfWebView(webViewImports) {

	const BEGIN_SHOWING_DIRECT = 0;
	const BEGIN_SHOWING_ENABLE_JAVASCRIPT = 1;
	const BEGIN_SHOWING_CONTENT_BLOCKED = 2;

	const EVENT_WEBVIEW_CHANNEL = 0;
	const EVENT_WEBVIEW_MESSAGE = 1;
	const EVENT_WEBVIEW_PERMISSION_ALLOW = 2;
	const EVENT_WEBVIEW_PERMISSION_BLOCK = 3;
	const EVENT_WEBVIEW_PERMISSION_CLEAR = 4;

	const EVENT_CHANNEL_OPEN = 0;
	const EVENT_CHANNEL_CLOSE = 1;

	const EVENT_MESSAGE_STRING = 0;
	const EVENT_MESSAGE_BINARY = 1;

	const utf8Decoder = new TextDecoder("utf-8");

	var supported = false;
	var cspSupport = false;
	var supportForce = runtimeOpts.forceWebViewSupport;
	var enableCSP = runtimeOpts.enableWebViewCSP;
	if(supportForce) {
		supported = true;
		cspSupport = true;
	}else {
		supported = false;
		cspSupport = false;
		try {
			var tmp = /** @type {HTMLIFrameElement} */ (document.createElement("iframe"));
			supported = tmp != null && (typeof tmp.allow === "string") && (typeof tmp.sandbox === "object");
			cspSupport = enableCSP && supported && (typeof tmp.csp === "string");
		}catch(ex) {
			eagError("Error checking iframe support");
			eagError(ex);
		}
	}
	if(!supported) {
		eagError("This browser does not meet the safety requirements for webview support, this feature will be disabled");
	}else if(!cspSupport && enableCSP) {
		eagWarn("This browser does not support CSP attribute on iframes! (try Chrome)");
	}

	const requireSSL = location.protocol && "https:" === location.protocol.toLowerCase();

	var webviewResetSerial = 0;

	/** @type {HTMLElement} */
	var currentIFrameContainer = null;

	/** @type {HTMLElement} */
	var currentAllowJavaScript = null;

	/** @type {HTMLIFrameElement} */
	var currentIFrame = null;

	/** @type {function(MessageEvent)|null} */
	var currentMessageHandler = null;

	window.addEventListener("message", /** @type {function(Event)} */ (function(/** MessageEvent */ evt) {
		if(currentMessageHandler && evt.source !== window) {
			currentMessageHandler(evt);
		}
	}));

	/**
	 * @return {boolean}
	 */
	webViewImports["checkSupported"] = function() {
		return supported;
	};

	/**
	 * @return {boolean}
	 */
	webViewImports["checkCSPSupported"] = function() {
		return cspSupport;
	};

	/**
	 * @param {string} ch
	 * @param {string} str
	 */
	webViewImports["sendStringMessage"] = function(ch, str) {
		try {
			var w;
			if(currentIFrame != null && (w = currentIFrame.contentWindow) != null) {
				w.postMessage({
					"ver": 1,
					"channel": ch,
					"type": "string",
					"data": str
				}, "*");
			}else {
				eagError("Server tried to send the WebView a message, but the message channel is not open!");
			}
		}catch(/** Error */ ex) {
			eagStackTrace(ERROR, "Failed to send string message to WebView!", ex);
		}
	};

	/**
	 * @param {string} ch
	 * @param {number} addr
	 * @param {number} length
	 */
	webViewImports["sendBinaryMessage"] = function(ch, addr, length) {
		try {
			var w;
			if(currentIFrame != null && (w = currentIFrame.contentWindow) != null) {
				w.postMessage({
					"ver": 1,
					"channel": ch,
					"type": "binary",
					"data": heapArrayBuffer.slice(addr, addr + length)
				}, "*");
			}else {
				eagError("Server tried to send the WebView a message, but the message channel is not open!");
			}
		}catch(/** Error */ ex) {
			eagStackTrace(ERROR, "Failed to send string message to WebView!", ex);
		}
	};

	/**
	 * @param {number} state
	 * @param {Object} options
	 * @param {number} x
	 * @param {number} y
	 * @param {number} w
	 * @param {number} h
	 */
	webViewImports["beginShowing"] = function(state, options, x, y, w, h) {
		if(!supported) {
			return;
		}
		try {
			setupShowing(x, y, w, h);
			switch(state) {
			case BEGIN_SHOWING_DIRECT:
				beginShowingDirect(options);
				break;
			case BEGIN_SHOWING_ENABLE_JAVASCRIPT:
				if(options["contentMode"] === 1) {
					const copiedBlob = new Uint8Array(options["blob"].length);
					copiedBlob.set(options["blob"], 0);
					options["blob"] = copiedBlob;
				}
				beginShowingEnableJavaScript(options);
				break;
			case BEGIN_SHOWING_CONTENT_BLOCKED:
				if(options["contentMode"] === 1) {
					const copiedBlob = new Uint8Array(options["blob"].length);
					copiedBlob.set(options["blob"], 0);
					options["blob"] = copiedBlob;
				}
				beginShowingContentBlocked(options);
				break;
			default:
				break;
			}
		}catch(/** Error */ ex) {
			eagStackTrace(ERROR, "Failed to begin showing WebView!", ex);
		}
	};

	/**
	 * @param {number} x
	 * @param {number} y
	 * @param {number} w
	 * @param {number} h
	 */
	function setupShowing(x, y, w, h) {
		if(currentIFrameContainer !== null) {
			endShowingImpl();
		}
		currentIFrameContainer = /** @type {HTMLElement} */ (document.createElement("div"));
		currentIFrameContainer.classList.add("_eaglercraftX_webview_container_element");
		currentIFrameContainer.style.border = "5px solid #333333";
		currentIFrameContainer.style.zIndex = "11";
		currentIFrameContainer.style.position = "absolute";
		currentIFrameContainer.style.backgroundColor = "#DDDDDD";
		currentIFrameContainer.style.fontFamily = "sans-serif";
		resizeImpl(x, y, w, h);
		parentElement.appendChild(currentIFrameContainer);
	}

	/**
	 * @param {HTMLIFrameElement} iframeElement
	 * @param {string} str
	 */
	function setAllowSafe(iframeElement, str) {
		iframeElement.allow = str;
		return iframeElement.allow === str;
	}

	/**
	 * @param {HTMLIFrameElement} iframeElement
	 * @param {Array<string>} args
	 */
	function setSandboxSafe(iframeElement, args) {
		const theSandbox = iframeElement.sandbox;
		for(var i = 0; i < args.length; ++i) {
			theSandbox.add(args[i]);
		}
		for(var i = 0; i < args.length; ++i) {
			if(!theSandbox.contains(args[i])) {
				return false;
			}
		}
		for(var i = 0; i < theSandbox.length; ++i) {
			if(!args.find(itm => itm === theSandbox[i])) {
				return false;
			}
		}
		return true;
	}

	function beginShowingDirect(options) {
		if(!supportForce) {
			currentIFrame = /** @type {HTMLIFrameElement} */ (document.createElement("iframe"));
			currentIFrame.referrerPolicy = "strict-origin";
			const sandboxArgs = [ "allow-downloads" ];
			if(options["scriptEnabled"]) {
				sandboxArgs.push("allow-scripts");
				sandboxArgs.push("allow-pointer-lock");
			}
			if(!setAllowSafe(currentIFrame, "") || !setSandboxSafe(currentIFrame, sandboxArgs)) {
				eagError("Caught safety exception while opening webview!");
				if(currentIFrame !== null) {
					currentIFrame.remove();
					currentIFrame = null;
				}
				eagError("Things you can try:");
				eagError("1. Set window.eaglercraftXOpts.forceWebViewSupport to true");
				eagError("2. Set window.eaglercraftXOpts.enableWebViewCSP to false");
				eagError("(these settings may compromise security)");
				beginShowingSafetyError();
				return;
			}
		}else {
			currentIFrame = /** @type {HTMLIFrameElement} */ (document.createElement("iframe"));
			currentIFrame.allow = "";
			currentIFrame.referrerPolicy = "strict-origin";
			currentIFrame.sandbox.add("allow-downloads");
			if(options["scriptEnabled"]) {
				currentIFrame.sandbox.add("allow-scripts");
				currentIFrame.sandbox.add("allow-pointer-lock");
			}
		}
		currentIFrame.credentialless = true;
		currentIFrame.loading = "lazy";
		var cspWarn = false;
		if(options["contentMode"] === 1) {
			if(enableCSP && cspSupport) {
				if(typeof currentIFrame.csp === "string") {
					var csp = "default-src 'none';";
					var protos = options["strictCSPEnable"] ? "" : (requireSSL ? " https:" : " http: https:");
					if(options["scriptEnabled"]) {
						csp += (" script-src 'unsafe-eval' 'unsafe-inline' data: blob:" + protos + ";");
						csp += (" style-src 'unsafe-eval' 'unsafe-inline' data: blob:" + protos + ";");
						csp += (" img-src data: blob:" + protos + ";");
						csp += (" font-src data: blob:" + protos + ";");
						csp += (" child-src data: blob:" + protos + ";");
						csp += (" frame-src data: blob:;");
						csp += (" media-src data: mediastream: blob:" + protos + ";");
						csp += (" connect-src data: blob:" + protos + ";");
						csp += (" worker-src data: blob:" + protos + ";");
					}else {
						csp += (" style-src data: 'unsafe-inline'" + protos + ";");
						csp += (" img-src data:" + protos + ";");
						csp += (" font-src data:" + protos + ";");
						csp += (" media-src data:" + protos + ";");
					}
					currentIFrame.csp = csp;
				}else {
					eagWarn("This browser does not support CSP attribute on iframes! (try Chrome)");
					cspWarn = true;
				}
			}else {
				cspWarn = true;
			}
			if(cspWarn && options["strictCSPEnable"]) {
				eagWarn("Strict CSP was requested for this webview, but that feature is not available!");
			}
		}else {
			cspWarn = true;
		}
		currentIFrame.style.border = "none";
		currentIFrame.style.backgroundColor = "white";
		currentIFrame.style.width = "100%";
		currentIFrame.style.height = "100%";
		currentIFrame.classList.add("_eaglercraftX_webview_iframe_element");
		currentIFrameContainer.appendChild(currentIFrame);
		if(options["contentMode"] === 1) {
			const decodedText = utf8Decoder.decode(options["blob"]);
			options["blob"] = null;
			currentIFrame.srcdoc = decodedText;
		}else {
			currentIFrame.src = options["uri"];
		}
		const resetSer = webviewResetSerial;
		const curIFrame = currentIFrame;
		let focusTracker = false;
		currentIFrame.addEventListener("mouseover", function(/** Event */ evt) {
			if(resetSer === webviewResetSerial && curIFrame === currentIFrame) {
				if(!focusTracker) {
					focusTracker = true;
					currentIFrame.contentWindow.focus();
				}
			}
		});
		currentIFrame.addEventListener("mouseout", function(/** Event */ evt) {
			if(resetSer === webviewResetSerial && curIFrame === currentIFrame) {
				if(focusTracker) {
					focusTracker = false;
					window.focus();
				}
			}
		});
		if(options["scriptEnabled"] && options["serverMessageAPIEnabled"]) {
			currentMessageHandler = function(/** MessageEvent */ evt) {
				if(resetSer === webviewResetSerial && curIFrame === currentIFrame && evt.source === curIFrame.contentWindow) {
					/** @type {Object} */
					const obj = evt.data;
					if((typeof obj === "object") && (obj["ver"] === 1) && ((typeof obj["channel"] === "string") && obj["channel"]["length"] > 0)) {
						if(typeof obj["open"] === "boolean") {
							pushEvent(EVENT_TYPE_WEBVIEW, EVENT_WEBVIEW_CHANNEL, {
								"eventType": (obj["open"] ? EVENT_CHANNEL_OPEN : EVENT_CHANNEL_CLOSE),
								"channelName": obj["channel"]
							});
							return;
						}else if(typeof obj["data"] === "string") {
							pushEvent(EVENT_TYPE_WEBVIEW, EVENT_WEBVIEW_MESSAGE, {
								"eventType": EVENT_MESSAGE_STRING,
								"channelName": obj["channel"],
								"eventData": obj["data"]
							});
							return;
						}else if(obj["data"] instanceof ArrayBuffer) {
							pushEvent(EVENT_TYPE_WEBVIEW, EVENT_WEBVIEW_MESSAGE, {
								"eventType": EVENT_MESSAGE_BINARY,
								"channelName": obj["channel"],
								"eventData": obj["data"]
							});
							return;
						}
					}
					eagWarn("WebView sent an invalid message!");
				}else {
					eagWarn("Recieved message from on dead IFrame handler: (#{}) {}", resetSer, curIFrame.src);
				}
			};
		}
		eagInfo("WebView is loading: \"{}\"", options["contentMode"] === 1 ? "about:srcdoc" : currentIFrame.src);
		eagInfo("JavaScript: {}, Strict CSP: {}, Message API: {}", options["scriptEnabled"],
				options["strictCSPEnable"] && !cspWarn, options["serverMessageAPIEnabled"]);
	}

	function beginShowingEnableJSSetup() {
		if(currentAllowJavaScript !== null) {
			++webviewResetSerial;
			currentAllowJavaScript.remove();
			currentAllowJavaScript = null;
		}
		currentAllowJavaScript = /** @type {HTMLElement} */ (document.createElement("div"));
		currentAllowJavaScript.style.backgroundColor = "white";
		currentAllowJavaScript.style.width = "100%";
		currentAllowJavaScript.style.height = "100%";
		currentAllowJavaScript.classList.add("_eaglercraftX_webview_permission_screen");
		currentIFrameContainer.appendChild(currentAllowJavaScript);
	}

	function beginShowingEnableJavaScript(options) {
		beginShowingEnableJSSetup();
		var strictCSPMarkup;
		if(options["contentMode"] !== 1) {
			strictCSPMarkup = "<span style=\"color:red;\">Impossible</span>";
		}else if(!cspSupport || !enableCSP) {
			strictCSPMarkup = "<span style=\"color:red;\">Unsupported</span>";
		}else if(options["strictCSPEnable"]) {
			strictCSPMarkup = "<span style=\"color:green;\">Enabled</span>";
		}else {
			strictCSPMarkup = "<span style=\"color:red;\">Disabled</span>";
		}
		var messageAPIMarkup;
		if(options["serverMessageAPIEnabled"]) {
			messageAPIMarkup = "<span style=\"color:red;\">Enabled</span>";
		}else {
			messageAPIMarkup = "<span style=\"color:green;\">Disabled</span>";
		}
		currentAllowJavaScript.innerHTML =
				"<div style=\"padding-top:13vh;\"><div style=\"margin:auto;max-width:450px;border:6px double black;text-align:center;padding:20px;\">"
				+ "<h2><img width=\"32\" height=\"32\" style=\"vertical-align:middle;\" src=\"" + faviconURL + "\">&emsp;Allow JavaScript</h2>"
				+ "<p style=\"font-family:monospace;text-decoration:underline;word-wrap:break-word;\" class=\"_eaglercraftX_permission_target_url\"></p>"
				+ "<h4 style=\"line-height:1.4em;\">Strict CSP: " + strictCSPMarkup + "&ensp;|&ensp;"
				+ "Message API: " + messageAPIMarkup + "</h4>"
				+ "<p><input class=\"_eaglercraftX_remember_javascript\" type=\"checkbox\" checked> Remember my choice</p>"
				+ "<p><button style=\"font-size:1.5em;\" class=\"_eaglercraftX_allow_javascript\">Allow</button>&emsp;"
				+ "<button style=\"font-size:1.5em;\" class=\"_eaglercraftX_block_javascript\">Block</button></p></div></div>";
		const serial = webviewResetSerial;
		if(options["contentMode"] !== 1) {
			const urlStr = options["url"];
			currentAllowJavaScript.querySelector("._eaglercraftX_permission_target_url").innerText = urlStr.length() > 255 ? (urlStr.substring(0, 253) + "...") : urlStr;
		}
		currentAllowJavaScript.querySelector("._eaglercraftX_allow_javascript").addEventListener("click", function(/** Event */ evt) {
			if(webviewResetSerial === serial && currentAllowJavaScript !== null) {
				const chkbox = currentAllowJavaScript.querySelector("._eaglercraftX_remember_javascript");
				if(chkbox !== null && chkbox.checked) {
					pushEvent(EVENT_TYPE_WEBVIEW, EVENT_WEBVIEW_PERMISSION_ALLOW, null);
				}
				currentAllowJavaScript.remove();
				currentAllowJavaScript = null;
				++webviewResetSerial;
				beginShowingDirect(options);
			}
		});
		currentAllowJavaScript.querySelector("._eaglercraftX_block_javascript").addEventListener("click", function(/** Event */ evt) {
			if(webviewResetSerial === serial && currentAllowJavaScript !== null) {
				const chkbox = currentAllowJavaScript.querySelector("._eaglercraftX_remember_javascript");
				if(chkbox !== null && chkbox.checked) {
					pushEvent(EVENT_TYPE_WEBVIEW, EVENT_WEBVIEW_PERMISSION_BLOCK, null);
				}
				beginShowingContentBlocked(options);
			}
		});
	}

	function beginShowingContentBlocked(options) {
		beginShowingEnableJSSetup();
		currentAllowJavaScript.innerHTML =
				"<div style=\"padding-top:13vh;\"><h1 style=\"text-align:center;\">"
				+ "<img width=\"48\" height=\"48\" style=\"vertical-align:middle;\" src=\"" + faviconURL + "\">&emsp;Content Blocked</h1>"
				+ "<h4 style=\"text-align:center;\">You chose to block JavaScript execution for this embed</h4>"
				+ "<p style=\"text-align:center;\"><button style=\"font-size:1.0em;\" class=\"_eaglercraftX_re_evaluate_javascript\">Re-evaluate</button></p></div>";
		const serial = webviewResetSerial;
		currentAllowJavaScript.querySelector("._eaglercraftX_re_evaluate_javascript").addEventListener("click", function(/** Event */ evt) {
			if(webviewResetSerial === serial && currentAllowJavaScript !== null) {
				pushEvent(EVENT_TYPE_WEBVIEW, EVENT_WEBVIEW_PERMISSION_CLEAR, null);
				beginShowingEnableJavaScript(options);
			}
		});
	}

	function beginShowingSafetyError() {
		beginShowingEnableJSSetup();
		currentAllowJavaScript.innerHTML =
				"<div style=\"padding-top:13vh;\"><h1 style=\"text-align:center;\">"
				+ "<img width=\"48\" height=\"48\" style=\"vertical-align:middle;\" src=\"" + faviconURL + "\">&emsp;IFrame Safety Error</h1>"
				+ "<h4 style=\"text-align:center;\">The content cannot be displayed safely!</h4>"
				+ "<h4 style=\"text-align:center;\">Check console for more details</h4></div>";
	}

	/**
	 * @param {number} x
	 * @param {number} y
	 * @param {number} w
	 * @param {number} h
	 */
	function resizeImpl(x, y, w, h) {
		if(currentIFrameContainer) {
			const s = window.devicePixelRatio;
			currentIFrameContainer.style.top = "" + (y / s) + "px";
			currentIFrameContainer.style.left = "" + (x / s) + "px";
			currentIFrameContainer.style.width = "" + ((w / s) - 10) + "px";
			currentIFrameContainer.style.height = "" + ((h / s) - 10) + "px";
		}
	}

	function endShowingImpl() {
		++webviewResetSerial;
		if(currentIFrame) {
			currentIFrame.remove();
			currentIFrame = null;
		}
		currentMessageHandler = null;
		if(currentAllowJavaScript) {
			currentAllowJavaScript.remove();
			currentAllowJavaScript = null;
		}
		if(currentIFrameContainer) {
			currentIFrameContainer.remove();
			currentIFrameContainer = null;
		}
		window.focus();
	}

	webViewImports["resize"] = resizeImpl;

	webViewImports["endShowing"] = endShowingImpl;

}

function initializeNoPlatfWebView(webViewImports) {
	setUnsupportedFunc(webViewImports, platfWebViewName, "checkSupported");
	setUnsupportedFunc(webViewImports, platfWebViewName, "checkCSPSupported");
	setUnsupportedFunc(webViewImports, platfWebViewName, "sendStringMessage");
	setUnsupportedFunc(webViewImports, platfWebViewName, "sendBinaryMessage");
	setUnsupportedFunc(webViewImports, platfWebViewName, "beginShowing");
	setUnsupportedFunc(webViewImports, platfWebViewName, "resize");
	setUnsupportedFunc(webViewImports, platfWebViewName, "endShowing");
}
