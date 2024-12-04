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

/**
 * @param {*} msg
 */
function logInfo(msg) {
	console.log("LoaderBootstrap: [INFO] " + msg);
}

/**
 * @param {*} msg
 */
function logWarn(msg) {
	console.log("LoaderBootstrap: [WARN] " + msg);
}

/**
 * @param {*} msg
 */
function logError(msg) {
	console.error("LoaderBootstrap: [ERROR] " + msg);
}

/** @type {function(string,number):ArrayBuffer|null} */
var decodeBase64Impl = null;

/**
 * @return {function(string,number):ArrayBuffer}
 */
function createBase64Decoder() {
	const revLookup = [];
	const code = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	for (var i = 0, len = code.length; i < len; ++i) {
		revLookup[code.charCodeAt(i)] = i;
	}

	revLookup["-".charCodeAt(0)] = 62;
	revLookup["_".charCodeAt(0)] = 63;

	/**
	 * @param {string} b64
	 * @param {number} start
	 * @return {!Array<number>}
	 */
	function getLens(b64, start) {
		const len = b64.length - start;
		if (len % 4 > 0) {
			throw new Error("Invalid string. Length must be a multiple of 4");
		}
		var validLen = b64.indexOf("=", start);
		if (validLen === -1) {
			validLen = len;
		}else {
			validLen -= start;
		}
		const placeHoldersLen = validLen === len ? 0 : 4 - (validLen % 4);
		return [validLen, placeHoldersLen];
	}
	
	/**
	 * @param {string} b64
	 * @param {number} start
	 * @return {ArrayBuffer}
	 */
	function decodeImpl(b64, start) {
		var tmp;
		const lens = getLens(b64, start);
		const validLen = lens[0];
		const placeHoldersLen = lens[1];
		const arr = new Uint8Array(((validLen + placeHoldersLen) * 3 / 4) - placeHoldersLen);
		var curByte = 0;
		const len = (placeHoldersLen > 0 ? validLen - 4 : validLen) + start;
		var i;
		for (i = start; i < len; i += 4) {
			tmp = (revLookup[b64.charCodeAt(i)] << 18) |
				(revLookup[b64.charCodeAt(i + 1)] << 12) |
				(revLookup[b64.charCodeAt(i + 2)] << 6) |
				revLookup[b64.charCodeAt(i + 3)]
			arr[curByte++] = (tmp >> 16) & 0xFF
			arr[curByte++] = (tmp >> 8) & 0xFF
			arr[curByte++] = tmp & 0xFF
		}
		if (placeHoldersLen === 2) {
			tmp = (revLookup[b64.charCodeAt(i)] << 2) |
				(revLookup[b64.charCodeAt(i + 1)] >> 4)
			arr[curByte++] = tmp & 0xFF
		}else if (placeHoldersLen === 1) {
			tmp = (revLookup[b64.charCodeAt(i)] << 10) |
				(revLookup[b64.charCodeAt(i + 1)] << 4) |
				(revLookup[b64.charCodeAt(i + 2)] >> 2)
			arr[curByte++] = (tmp >> 8) & 0xFF
			arr[curByte++] = tmp & 0xFF
		}
		return arr.buffer;
	}
	
	return decodeImpl;
}

/**
 * @param {string} url
 * @param {number} start
 * @return {ArrayBuffer}
 */
function decodeBase64(url, start) {
	if(!decodeBase64Impl) {
		decodeBase64Impl = createBase64Decoder();
	}
	return decodeBase64Impl(url, start);
}

/**
 * @param {number} ms
 * @return {!Promise}
 */
function asyncSleep(ms) {
	return new Promise(function(resolve) {
		setTimeout(resolve, ms);
	});
}

/**
 * @param {string} url
 * @return {!Promise<ArrayBuffer>}
 */
function downloadURL(url) {
	return new Promise(function(resolve) {
		fetch(url, { "cache": "force-cache" })
			.then(function(res) {
				return res.arrayBuffer();
			})
			.then(resolve)
			.catch(function(ex) {
				logError("Failed to fetch URL! " + ex);
				resolve(null);
			});
	});
}

/**
 * @param {string} url
 * @return {!Promise<ArrayBuffer>}
 */
function downloadDataURL(url) {
	if(!url.startsWith("data:application/octet-stream;base64,")) {
		return downloadURL(url);
	}else {
		return new Promise(function(resolve) {
			downloadURL(url).then(function(res) {
				if(res) {
					resolve(res);
				}else {
					logWarn("Failed to decode base64 via fetch, doing it the slow way instead...");
					try {
						resolve(decodeBase64(url, 37));
					}catch(ex) {
						logError("Failed to decode base64! " + ex);
						resolve(null);
					}
				}
			});
		});
	}
}

/**
 * @param {HTMLElement} rootElement
 * @param {string} msg
 */
function displayInvalidEPW(rootElement, msg) {
	const downloadFailureMsg = /** @type {HTMLElement} */ (document.createElement("h2"));
	downloadFailureMsg.style.color = "#AA0000";
	downloadFailureMsg.style.padding = "25px";
	downloadFailureMsg.style.fontFamily = "sans-serif";
	downloadFailureMsg.style["marginBlock"] = "0px";
	downloadFailureMsg.appendChild(document.createTextNode(msg));
	rootElement.appendChild(downloadFailureMsg);
	const downloadFailureMsg2 = /** @type {HTMLElement} */ (document.createElement("h4"));
	downloadFailureMsg2.style.color = "#AA0000";
	downloadFailureMsg2.style.padding = "25px";
	downloadFailureMsg2.style.fontFamily = "sans-serif";
	downloadFailureMsg2.style["marginBlock"] = "0px";
	downloadFailureMsg2.appendChild(document.createTextNode("Try again later"));
	rootElement.style.backgroundColor = "white";
	rootElement.appendChild(downloadFailureMsg2);
}

window.main = async function() {
	if(typeof window.eaglercraftXOpts === "undefined") {
		const msg = "window.eaglercraftXOpts is not defined!";
		logError(msg);
		alert(msg);
		return;
	}
	
	const containerId = window.eaglercraftXOpts.container;
	if(typeof containerId !== "string") {
		const msg = "window.eaglercraftXOpts.container is not a string!";
		logError(msg);
		alert(msg);
		return;
	}
	
	var assetsURI = window.eaglercraftXOpts.assetsURI;
	if(typeof assetsURI !== "string") {
		if((typeof assetsURI === "object") && (typeof assetsURI[0] === "object") && (typeof assetsURI[0]["url"] === "string")) {
			assetsURI = assetsURI[0]["url"];
		}else {
			const msg = "window.eaglercraftXOpts.assetsURI is not a string!";
			logError(msg);
			alert(msg);
			return;
		}
	}
	
	const rootElement = /** @type {HTMLElement} */ (document.getElementById(containerId));
	
	if(!rootElement) {
		const msg = "window.eaglercraftXOpts.container \"" + containerId + "\" is not a known element id!";
		logError(msg);
		alert(msg);
		return;
	}
	
	var node;
	while(node = rootElement.lastChild) {
		rootElement.removeChild(node);
	}
	
	const splashElement = /** @type {HTMLElement} */ (document.createElement("div"));
	splashElement.style.width = "100%";
	splashElement.style.height = "100%";
	splashElement.style.setProperty("image-rendering", "pixelated");
	splashElement.style.background = "center / contain no-repeat url(\"data:image/png;base64,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\") white";
	rootElement.appendChild(splashElement);
	
	/** @type {ArrayBuffer} */
	var theEPWFileBuffer;
	if(assetsURI.startsWith("data:")) {
		logInfo("Downloading EPW file \"<data: " + assetsURI.length + " chars>\"...");
		theEPWFileBuffer = await downloadDataURL(assetsURI);
	}else {
		logInfo("Downloading EPW file \"" + assetsURI + "\"...");
		theEPWFileBuffer = await downloadURL(assetsURI);
	}
	
	var isInvalid = false;
	if(!theEPWFileBuffer) {
		isInvalid = true;
	}else if(theEPWFileBuffer.byteLength < 384) {
		logError("The EPW file is too short");
		isInvalid = true;
	}
	
	if(isInvalid) {
		rootElement.removeChild(splashElement);
		const msg = "Failed to download EPW file!";
		displayInvalidEPW(rootElement, msg);
		logError(msg);
		return;
	}
	
	const dataView = new DataView(theEPWFileBuffer);
	
	if(dataView.getUint32(0, true) !== 608649541 || dataView.getUint32(4, true) !== 1297301847) {
		logError("The file is not an EPW file");
		isInvalid = true;
	}
	
	const phileLength = theEPWFileBuffer.byteLength;
	if(dataView.getUint32(8, true) !== phileLength) {
		logError("The EPW file is the wrong length");
		isInvalid = true;
	}

	if(isInvalid) {
		rootElement.removeChild(splashElement);
		const msg = "EPW file is invalid!";
		displayInvalidEPW(rootElement, msg);
		logError(msg);
		return;
	}

	const textDecoder = new TextDecoder("utf-8");

	const splashDataOffset = dataView.getUint32(100, true);
	const splashDataLength = dataView.getUint32(104, true);
	const splashMIMEOffset = dataView.getUint32(108, true);
	const splashMIMELength = dataView.getUint32(112, true);
	
	if(splashDataOffset < 0 || splashDataOffset + splashDataLength > phileLength
			|| splashMIMEOffset < 0 || splashMIMEOffset + splashMIMELength > phileLength) {
		logError("The EPW file contains an invalid offset (component: splash)");
		isInvalid = true;
	}

	if(isInvalid) {
		rootElement.removeChild(splashElement);
		const msg = "EPW file is invalid!";
		displayInvalidEPW(rootElement, msg);
		logError(msg);
		return;
	}

	const splashBinSlice = new Uint8Array(theEPWFileBuffer, splashDataOffset, splashDataLength);
	const splashMIMESlice = new Uint8Array(theEPWFileBuffer, splashMIMEOffset, splashMIMELength);
	const splashURL = URL.createObjectURL(new Blob([ splashBinSlice ], { "type": textDecoder.decode(splashMIMESlice) }));
	logInfo("Loaded splash img: " + splashURL);
	splashElement.style.background = "center / contain no-repeat url(\"" + splashURL + "\"), 0px 0px / 1000000% 1000000% no-repeat url(\"" + splashURL + "\") white";

	// allow the screen to update
	await asyncSleep(20);

	const loaderJSOffset = dataView.getUint32(164, true);
	const loaderJSLength = dataView.getUint32(168, true);
	const loaderWASMOffset = dataView.getUint32(180, true);
	const loaderWASMLength = dataView.getUint32(184, true);
	
	if(loaderJSOffset < 0 || loaderJSOffset + loaderJSLength > phileLength
			|| loaderWASMOffset < 0 || loaderWASMOffset + loaderWASMLength > phileLength) {
		logError("The EPW file contains an invalid offset (component: loader)");
		isInvalid = true;
	}

	if(isInvalid) {
		rootElement.removeChild(splashElement);
		const msg = "EPW file is invalid!";
		displayInvalidEPW(rootElement, msg);
		logError(msg);
		return;
	}

	const loaderJSSlice = new Uint8Array(theEPWFileBuffer, loaderJSOffset, loaderJSLength);
	const loaderJSURL = URL.createObjectURL(new Blob([ loaderJSSlice ], { "type": "text/javascript;charset=utf-8" }));
	logInfo("Loaded loader.js: " + splashURL);
	const loaderWASMSlice = new Uint8Array(theEPWFileBuffer, loaderWASMOffset, loaderWASMLength);
	const loaderWASMURL = URL.createObjectURL(new Blob([ loaderWASMSlice ], { "type": "application/wasm" }));
	logInfo("Loaded loader.wasm: " + loaderWASMURL);

	const optsObj = {};
	for(const [key, value] of Object.entries(window.eaglercraftXOpts)) {
		if(key !== "container" && key !== "assetsURI") {
			optsObj[key] = value;
		}
	}

	window.__eaglercraftXLoaderContextPre = {
		"rootElement": rootElement,
		"eaglercraftXOpts": optsObj,
		"theEPWFileBuffer": theEPWFileBuffer,
		"loaderWASMURL": loaderWASMURL,
		"splashURL": splashURL
	};

	logInfo("Appending loader.js to document...");

	const scriptElement = /** @type {HTMLScriptElement} */ (document.createElement("script"));
	scriptElement.type = "text/javascript";
	scriptElement.src = loaderJSURL;
	document.head.appendChild(scriptElement);

};

