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

addToLibrary({
	getJSPISupported: function() {
		return (typeof WebAssembly.Suspending !== "undefined");
	},
	getEPWLength: function() {
		return epwFile.byteLength;
	},
	memcpyFromEPW: function(dest, off, len) {
		HEAPU8.set(new Uint8Array(epwFile, off, len), dest);
	},
	initResult: function(bufLen) {
		const id = results.length;
		results.push(new Uint8Array(bufLen));
		return id;
	},
	memcpyToResult: function(bufId, src, off, len) {
		results[bufId].set(new Uint8Array(HEAPU8.buffer, src, len), off);
	},
	memcpyFromEPWToResult: function(bufId, dest, off, len) {
		results[bufId].set(new Uint8Array(epwFile, off, len), dest);
	},
	initEPWStringResult: function(off, len) {
		const id = results.length;
		results.push(UTF8Decoder.decode(new Uint8Array(epwFile, off, len)));
		return id;
	},
	resultFailed: function(msg) {
		results = null;
		epwFile = null;
		
		setTimeout(unsetHeapViews, 20);
		
		const parentElement = createCrashParentElement();
		
		const messageContainer = document.createElement("div");
		messageContainer.setAttribute("style", "z-index:100;position:absolute;top:10%;left:10%;right:10%;bottom:10%;background-color:white;border:2px solid #cccccc;overflow-x:hidden;overflow-y:scroll;");
		messageContainer.classList.add("_eaglercraftX_loader_failed_container");
		
		const failMsg = UTF8ToString(msg);
		console.error("LoaderMain: [FAILED] " + failMsg);
		
		const failureMsgElement = document.createElement("h2");
		failureMsgElement.style.color = "#AA0000";
		failureMsgElement.style.padding = "25px";
		failureMsgElement.style.fontFamily = "sans-serif";
		failureMsgElement.style["marginBlock"] = "0px";
		failureMsgElement.appendChild(document.createTextNode(failMsg));
		messageContainer.appendChild(failureMsgElement);
		
		const failureMsgElement2 = document.createElement("h4");
		failureMsgElement2.style.color = "#AA0000";
		failureMsgElement2.style.padding = "25px";
		failureMsgElement2.style.fontFamily = "sans-serif";
		failureMsgElement2.style["marginBlock"] = "0px";
		failureMsgElement2.appendChild(document.createTextNode("Try again later"));
		messageContainer.appendChild(failureMsgElement2);
		
		parentElement.appendChild(messageContainer);
	},
	resultSuccess: function(result) {
		const idx = result >> 2;
		
		const eagRuntimeJSURL = URL.createObjectURL(new Blob([results[HEAP32[idx]]], { type: "text/javascript;charset=utf-8" }));
		const classesWASMURL = URL.createObjectURL(new Blob([results[HEAP32[idx + 1]]], { type: "application/wasm" }));
		const classesDeobfTEADBGURL = HEAP32[idx + 2] !== -1 ? URL.createObjectURL(new Blob([results[HEAP32[idx + 2]]], { type: "application/octet-stream" })) : null;
		const classesDeobfWASMURL = HEAP32[idx + 3] !== -1 ? URL.createObjectURL(new Blob([results[HEAP32[idx + 3]]], { type: "application/wasm" })) : null;
		
		const pressAnyKey = URL.createObjectURL(new Blob([results[HEAP32[idx + 4]]], { type: results[HEAP32[idx + 5]] }));
		const crashImg = URL.createObjectURL(new Blob([results[HEAP32[idx + 6]]], { type: results[HEAP32[idx + 7]] }));
		const faviconImg = URL.createObjectURL(new Blob([results[HEAP32[idx + 8]]], { type: results[HEAP32[idx + 9]] }));
		
		const numEPKs = HEAP32[idx + 10];
		const epkFiles = new Array(numEPKs);
		for(var i = 0, j; i < numEPKs; ++i) {
			j = idx + 11 + i * 3;
			epkFiles[i] = {
				"data": results[HEAP32[j]],
				"name": results[HEAP32[j + 1]],
				"path": results[HEAP32[j + 2]]
			};
		}
		
		results = null;
		epwFile = null;
		
		setTimeout(unsetHeapViews, 20);
		
		window["__eaglercraftXLoaderContext"] = {
			"getEaglercraftXOpts": function() {
				return optsObj;
			},
			"getEagRuntimeJSURL": function() {
				return eagRuntimeJSURL;
			},
			"getClassesWASMURL": function() {
				return classesWASMURL;
			},
			"getClassesDeobfWASMURL": function() {
				return classesDeobfWASMURL;
			},
			"getClassesTEADBGURL": function() {
				return classesDeobfTEADBGURL;
			},
			"getEPKFiles": function() {
				return epkFiles;
			},
			"getRootElement": function() {
				return rootElement;
			},
			"getMainArgs": function() {
				return [];
			},
			"getImageURL": function(idx) {
				switch(idx) {
				case 0:
					return splashURL;
				case 1:
					return pressAnyKey;
				case 2:
					return crashImg;
				case 3:
					return faviconImg;
				default:
					return null;
				}
			},
			"runMain": function(fn) {
				setTimeout(fn, 10);
			}
		};
		
		const scriptElement = document.createElement("script");
		scriptElement.type = "text/javascript";
		scriptElement.src = eagRuntimeJSURL;
		document.head.appendChild(scriptElement);
	},
	resultJSPIUnsupported: function(result) {
		const idx = result >> 2;
		
		const crashImgData = results[HEAP32[idx]];
		const crashImgMIME = results[HEAP32[idx + 1]];
		const crashImg = crashImgData ? URL.createObjectURL(new Blob([crashImgData], { type: (crashImgMIME || "image/png") })) : null;
		
		const markupData = results[HEAP32[idx + 2]];
		const markup = markupData ? UTF8Decoder.decode(markupData) : "<h1>Failed to load error screen</h1>";
		
		results = null;
		epwFile = null;
		
		setTimeout(unsetHeapViews, 20);
		
		const parentElement = createCrashParentElement();
		
		const img = document.createElement("img");
		img.setAttribute("style", "z-index:100;position:absolute;top:10px;left:calc(50% - 151px);");
		img.src = crashImg;
		parentElement.appendChild(img);
		
		const iframeContainer = document.createElement("div");
		iframeContainer.setAttribute("style", "z-index:100;position:absolute;top:135px;left:10%;right:10%;bottom:50px;background-color:white;border:2px solid #cccccc;");
		iframeContainer.classList.add("_eaglercraftX_jspi_unsupported_container");
		
		const iframe = document.createElement("iframe");
		iframe.classList.add("_eaglercraftX_jspi_unsupported_frame");
		iframe.setAttribute("style", "border:none;width:100%;height:100%;");
		iframe.srcdoc = markup;
		
		iframeContainer.appendChild(iframe);
		parentElement.appendChild(iframeContainer);
	},
	dbgLog: function(msg) {
		console.log("LoaderMain: [INFO] " + UTF8ToString(msg));
	},
	dbgErr: function(msg) {
		console.error("LoaderMain: [ERROR] " + UTF8ToString(msg));
	}
});
