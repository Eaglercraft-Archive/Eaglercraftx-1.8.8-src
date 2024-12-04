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

const platfApplicationName = "platformApplication";

/**
 * @param {string} str
 * @param {string} blobUrl
 * @param {function()|null} blobRevoke
 */
function downloadFileImpl(str, blobUrl, blobRevoke) {
	const el = document.createElement("a");
	el.style.position = "absolute";
	el.style.left = "0px";
	el.style.top = "0px";
	el.style.zIndex = "-100";
	el.style.color = "transparent";
	el.innerText = "Download File";
	el.href = blobUrl;
	el.target = "_blank";
	el.download = str;
	parentElement.appendChild(el);
	setTimeout(function() {
		el.click();
		setTimeout(function() {
			parentElement.removeChild(el);
		}, 50);
		if(blobRevoke) {
			setTimeout(blobRevoke, 60000);
		}
	}, 50);
}

/** @type {number} */
const bufferSpoolSize = 256;
/** @type {number} */
const windowMaxMessages = 2048;

/** @type {number} */
var messageBufferLength = 0;
/** @type {Object|null} */
var messageBufferStart = null;
/** @type {Object|null} */
var messageBufferEnd = null;
/** @type {Window|null} */
var loggerWindow = null;
/** @type {function(string, boolean)|null} */
var loggerWindowMsgHandler = null;

/**
 * @param {string} text
 * @param {boolean} isErr
 */
function addLogMessageImpl(text, isErr) {
	if(!loggerWindow) {
		var newEl = {
			"msg": text,
			"err": isErr,
			"next": null
		};
		if(messageBufferEnd) {
			messageBufferEnd["next"] = newEl;
		}
		if(!messageBufferStart) {
			messageBufferStart = newEl;
		}
		messageBufferEnd = newEl;
		++messageBufferLength;
		while(messageBufferLength > bufferSpoolSize) {
			--messageBufferLength;
			if(messageBufferStart) {
				messageBufferStart = messageBufferStart["next"];
			}
		}
	}else if(loggerWindowMsgHandler) {
		loggerWindowMsgHandler(text, isErr);
	}
}

/**
 * @param {Object} applicationImports
 */
function initializePlatfApplication(applicationImports) {

	/**
	 * @param {string} str
	 * @return {boolean}
	 */
	applicationImports["setClipboard"] = function setClipboardImpl(str) {
		try {
			if(window.navigator.clipboard) {
				window.navigator.clipboard.writeText(str);
				return true;
			}
		}catch(ex) {
			eagError("Failed to set clipboard data!");
		}
		return false;
	};

	/**
	 * @return {Promise<string|null>}
	 */
	async function getClipboardImpl() {
		var txt = null;
		try {
			if(window.navigator.clipboard) {
				txt = await navigator.clipboard.readText();
			}
		}catch(ex) {
			eagError("Failed to read clipboard data!");
		}
		return txt;
	}

	applicationImports["getClipboard"] = new WebAssembly.Suspending(getClipboardImpl);

	/** @type {boolean} */
	var fileChooserHasResult = false;
	/** @type {Object|null} */
	var fileChooserResultObject = null;
	/** @type {HTMLInputElement|null} */
	var fileChooserElement = null;
	/** @type {HTMLElement|null} */
	var fileChooserMobileElement = null;

	/**
	 * @param {string} mime
	 * @param {string} ext
	 */
	applicationImports["displayFileChooser"] = function(mime, ext) {
		clearFileChooserResultImpl();
		if(isLikelyMobileBrowser) {
			const el = fileChooserMobileElement = /** @type {HTMLElement} */ (document.createElement("div"));
			el.classList.add("_eaglercraftX_mobile_file_chooser_popup");
			el.style.position = "absolute";
			el.style.backgroundColor = "white";
			el.style.fontFamily = "sans-serif";
			el.style.top = "10%";
			el.style.left = "10%";
			el.style.right = "10%";
			el.style.border = "5px double black";
			el.style.padding = "15px";
			el.style.textAlign = "left";
			el.style.fontSize = "20px";
			el.style.userSelect = "none";
			el.style.zIndex = "150";
			const fileChooserTitle = document.createElement("h3");
			fileChooserTitle.appendChild(document.createTextNode("File Chooser"));
			el.appendChild(fileChooserTitle);
			const inputElementContainer = document.createElement("p");
			const inputElement = fileChooserElement = /** @type {HTMLInputElement} */ (document.createElement("input"));
			inputElement.type = "file";
			if(mime === null) {
				inputElement.accept = "." + ext;
			}else {
				inputElement.accept = mime;
			}
			inputElement.multiple = false;
			inputElementContainer.appendChild(inputElement);
			el.appendChild(inputElementContainer);
			const fileChooserButtons = document.createElement("p");
			const fileChooserButtonCancel = document.createElement("button");
			fileChooserButtonCancel.classList.add("_eaglercraftX_mobile_file_chooser_btn_cancel");
			fileChooserButtonCancel.style.fontSize = "1.0em";
			fileChooserButtonCancel.addEventListener("click", function(/** Event */ evt) {
				if(fileChooserMobileElement === el) {
					parentElement.removeChild(el);
					fileChooserMobileElement = null;
					fileChooserElement = null;
				}
			});
			fileChooserButtonCancel.appendChild(document.createTextNode("Cancel"));
			fileChooserButtons.appendChild(fileChooserButtonCancel);
			fileChooserButtons.appendChild(document.createTextNode(" "));
			const fileChooserButtonDone = document.createElement("button");
			fileChooserButtonDone.classList.add("_eaglercraftX_mobile_file_chooser_btn_done");
			fileChooserButtonDone.style.fontSize = "1.0em";
			fileChooserButtonDone.style.fontWeight = "bold";
			fileChooserButtonDone.addEventListener("click", function(/** Event */ evt) {
				if(fileChooserMobileElement === el) {
					if(inputElement.files.length > 0) {
						const val = inputElement.files[0];
						val.arrayBuffer().then(function(arr){
							fileChooserHasResult = true;
							fileChooserResultObject = {
								"fileName": val.name,
								"fileData": arr
							};
						}).catch(function(){
							fileChooserHasResult = true;
							fileChooserResultObject = null;
						});
					}else {
						fileChooserHasResult = true;
						fileChooserResultObject = null;
					}
					parentElement.removeChild(el);
					fileChooserMobileElement = null;
					fileChooserElement = null;
				}
			});
			fileChooserButtonDone.appendChild(document.createTextNode("Done"));
			fileChooserButtons.appendChild(fileChooserButtonDone);
			el.appendChild(fileChooserButtons);
			parentElement.appendChild(el);
		}else {
			const inputElement = fileChooserElement = /** @type {HTMLInputElement} */ (document.createElement("input"));
			inputElement.type = "file";
			inputElement.style.position = "absolute";
			inputElement.style.left = "0px";
			inputElement.style.top = "0px";
			inputElement.style.zIndex = "-100";
			if(mime === null) {
				inputElement.accept = "." + ext;
			}else {
				inputElement.accept = mime;
			}
			inputElement.multiple = false;
			inputElement.addEventListener("change", function(/** Event */ evt) {
				if(fileChooserElement === inputElement) {
					if(inputElement.files.length > 0) {
						const val = inputElement.files[0];
						val.arrayBuffer().then(function(arr){
							fileChooserHasResult = true;
							fileChooserResultObject = {
								"fileName": val.name,
								"fileData": arr
							};
						}).catch(function(){
							fileChooserHasResult = true;
							fileChooserResultObject = null;
						});
					}else {
						fileChooserHasResult = true;
						fileChooserResultObject = null;
					}
					parentElement.removeChild(inputElement);
					fileChooserElement = null;
				}
			});
			parentElement.appendChild(inputElement);
			window.setTimeout(function() {
				inputElement.click();
			}, 50);
		}
	};

	/**
	 * @return {boolean}
	 */
	applicationImports["fileChooserHasResult"] = function() {
		return fileChooserHasResult;
	};

	/**
	 * @return {Object}
	 */
	applicationImports["getFileChooserResult"] = function() {
		fileChooserHasResult = false;
		const res = fileChooserResultObject;
		fileChooserResultObject = null;
		return res;
	};

	function clearFileChooserResultImpl() {
		fileChooserHasResult = false;
		fileChooserResultObject = null;
		if(fileChooserMobileElement !== null) {
			parentElement.removeChild(fileChooserMobileElement);
			fileChooserMobileElement = null;
			fileChooserElement = null;
		}else if(fileChooserElement !== null) {
			parentElement.removeChild(fileChooserElement);
			fileChooserElement = null;
		}
	}

	applicationImports["clearFileChooserResult"] = clearFileChooserResultImpl;

	/**
	 * @param {string} str
	 * @param {Uint8Array} dat
	 */
	applicationImports["downloadFileWithNameU8"] = function(str, dat) {
		const blobUrl = URL.createObjectURL(new Blob([dat], { "type": "application/octet-stream" }));
		downloadFileImpl(str, blobUrl, function() {
			URL.revokeObjectURL(blobUrl);
		});
	};

	/**
	 * @param {string} str
	 * @param {ArrayBuffer} dat
	 */
	applicationImports["downloadFileWithNameA"] = function(str, dat) {
		const blobUrl = URL.createObjectURL(new Blob([dat], { "type": "application/octet-stream" }));
		downloadFileImpl(str, blobUrl, function() {
			URL.revokeObjectURL(blobUrl);
		});
	};

	/**
	 * @param {string} str
	 * @param {HTMLCanvasElement} cvs
	 */
	applicationImports["downloadScreenshot"] = function(str, cvs) {
		downloadFileImpl(str, cvs.toDataURL("image/png"), null);
	};

	/** @type {HTMLDocument} */
	var loggerDoc = null;
	/** @type {HTMLElement} */
	var loggerBody = null;
	/** @type {HTMLElement} */
	var loggerMessageContainer = null;
	/** @type {string} */
	const loggerLocalStorageKey = runtimeOpts.localStorageNamespace + "showDebugConsole";
	/** @type {string} */
	const loggerWinUnloadEvent = runtimeOpts.fixDebugConsoleUnloadListener ? "beforeunload" : "unload";

	function debugConsoleLocalStorageSet(val) {
		try {
			if(window.localStorage) {
				window.localStorage.setItem(loggerLocalStorageKey, val ? "true" : "false");
			}
		}catch(t) {
		}
	}

	function debugConsoleLocalStorageGet() {
		try {
			if(window.localStorage) {
				const val = window.localStorage.getItem(loggerLocalStorageKey);
				return val && "true" === val.toLowerCase();
			}else {
				return false;
			}
		}catch(t) {
			return false;
		}
	}
	
	try {
		window.addEventListener(loggerWinUnloadEvent, (evt) => {
			destroyWindow();
		});
	}catch(ex) {
	}

	if(runtimeOpts.openDebugConsoleOnLaunch || debugConsoleLocalStorageGet()) {
		showDebugConsole0();
	}

	function showDebugConsole() {
		debugConsoleLocalStorageSet(true);
		showDebugConsole0();
	}

	function showDebugConsole0() {
		if(!loggerWindow) {
			const w = Math.round(1000 * window.devicePixelRatio);
			const h = Math.round(400 * window.devicePixelRatio);
			const x = Math.round((window.screen.width - w) / 2.0);
			const y = Math.round((window.screen.height - h) / 2.0);
			loggerWindow = window.open("", "_blank", "top=" + y + ",left=" + x + ",width=" + w + ",height=" + h + ",menubar=0,status=0,titlebar=0,toolbar=0");
			if(!loggerWindow) {
				eagError("Logger popup was blocked!");
				window.alert("ERROR: Popup blocked!\n\nPlease make sure you have popups enabled for this site!");
				return;
			}
			loggerWindow.focus();
			loggerDoc = loggerWindow.document;
			loggerDoc.write("<!DOCTYPE html><html><head><meta charset=\"UTF-8\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />"
					+ "<title>Debug Console</title><link type=\"image/png\" rel=\"shortcut icon\" href=\"" + faviconURL + "\" />"
					+ "</head><body style=\"overflow-x:hidden;overflow-y:scroll;padding:0px;\"><p id=\"loggerMessageContainer\" style=\"overflow-wrap:break-word;white-space:pre-wrap;font:14px monospace;padding:10px;\"></p></body></html>");
			loggerDoc.close();
			loggerBody = loggerDoc.body;
			loggerMessageContainer = /** @type {HTMLElement} */ (loggerDoc.getElementById("loggerMessageContainer"));
			var linkedListEl = messageBufferStart;
			while(linkedListEl) {
				appendLogMessage(linkedListEl["msg"] + "\n", linkedListEl["err"] ? "#DD0000" : "#000000");
				linkedListEl = linkedListEl["next"];
			}
			messageBufferStart = null;
			messageBufferEnd = null;
			messageBufferLength = 0;
			scrollToEnd0();
			const unloadListener = (evt) => {
				if(loggerWindow != null) {
					loggerWindow = null;
					debugConsoleLocalStorageSet(false);
				}
			};
			loggerWindow.addEventListener("beforeunload", unloadListener);
			loggerWindow.addEventListener("unload", unloadListener);
		}else {
			loggerWindow.focus();
		}
	}

	/**
	 * @param {string} text
	 * @param {boolean} isErr
	 */
	loggerWindowMsgHandler = function(text, isErr) {
		appendLogMessageAndScroll(text + "\n", isErr ? "#DD0000" : "#000000");
	}

	function appendLogMessageAndScroll(text, color) {
		var b = isScrollToEnd();
		appendLogMessage(text, color);
		if(b) {
			scrollToEnd0();
		}
	}

	function appendLogMessage(text, color) {
		var el = loggerDoc.createElement("span");
		el.innerText = text;
		el.style.color = color;
		loggerMessageContainer.appendChild(el);
		var children = loggerMessageContainer.children;
		while(children.length > windowMaxMessages) {
			children[0].remove();
		}
	}

	/**
	 * @return {boolean}
	 */
	function isShowingDebugConsole() {
		return !!loggerWindow;
	}

	function destroyWindow() {
		if(loggerWindow) {
			var w = loggerWindow;
			loggerWindow = null;
			loggerDoc = null;
			loggerBody = null;
			loggerMessageContainer = null;
			w.close();
		}
	}

	function isScrollToEnd() {
		return (loggerWindow.innerHeight + loggerWindow.pageYOffset) >= loggerBody.offsetHeight;
	}

	function scrollToEnd0() {
		setTimeout(() => {
			loggerWindow.scrollTo(0, loggerBody.scrollHeight || loggerBody.clientHeight);
		}, 1);
	}

	applicationImports["showDebugConsole"] = showDebugConsole;

	applicationImports["addLogMessage"] = addLogMessageImpl;

	applicationImports["isShowingDebugConsole"] = isShowingDebugConsole;

	/**
	 * @return {string|null}
	 */
	applicationImports["getFaviconURL"] = function() {
		return faviconURL;
	};

}

/**
 * @param {Object} applicationImports
 */
function initializeNoPlatfApplication(applicationImports) {
	setUnsupportedFunc(applicationImports, platfApplicationName, "setClipboard");
	setUnsupportedFunc(applicationImports, platfApplicationName, "getClipboard");
	setUnsupportedFunc(applicationImports, platfApplicationName, "displayFileChooser");
	setUnsupportedFunc(applicationImports, platfApplicationName, "fileChooserHasResult");
	setUnsupportedFunc(applicationImports, platfApplicationName, "getFileChooserResult");
	setUnsupportedFunc(applicationImports, platfApplicationName, "clearFileChooserResult");
	setUnsupportedFunc(applicationImports, platfApplicationName, "downloadFileWithNameU8");
	setUnsupportedFunc(applicationImports, platfApplicationName, "downloadFileWithNameA");
	setUnsupportedFunc(applicationImports, platfApplicationName, "downloadScreenshot");
	setUnsupportedFunc(applicationImports, platfApplicationName, "showDebugConsole");
	setUnsupportedFunc(applicationImports, platfApplicationName, "addLogMessage");
	setUnsupportedFunc(applicationImports, platfApplicationName, "isShowingDebugConsole");
	setUnsupportedFunc(applicationImports, platfApplicationName, "getFaviconURL");
}
