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

const clientPlatfSPName = "clientPlatformSingleplayer";

/** @type {HTMLElement} */
var integratedServerCrashPanel = null;

var integratedServerCrashPanelShowing = false;

function initializeClientPlatfSP(spImports) {

	/** @type {Worker|null} */
	var workerObj = null;

	const clientMessageQueue = new EaglerLinkedQueue();

	const workerBootstrapSource = "\"use strict\"; (function(ctx, globals) {" +
			"globals.__eaglerXOnMessage = function(o) {" +
			"globals.__eaglerXOnMessage = function(oo) { console.error(\"Dropped IPC packet that was sent too early!\"); };" +
			"const eagRuntimeJSURL = URL.createObjectURL(new Blob([ o.eagruntimeJS ], { type: \"text/javascript;charset=utf-8\" }));" +
			"ctx.getEaglercraftXOpts = function() { return o.eaglercraftXOpts; };" +
			"ctx.getEagRuntimeJSURL = function() { return eagRuntimeJSURL; };" +
			"ctx.getClassesWASMURL = function() { return o.classesWASM; };" +
			"ctx.getClassesDeobfWASMURL = function() { return o.classesDeobfWASM; };" +
			"ctx.getClassesTEADBGURL = function() { return o.classesTEADBG; };" +
			"ctx.getEPKFiles = function() { return null; };" +
			"ctx.getRootElement = function() { return null; };" +
			"ctx.getMainArgs = function() { return [\"_worker_process_\"]; };" +
			"ctx.getImageURL = function(idx) { return null; };" +
			"ctx.runMain = function(mainFunc) { mainFunc(); };" +
			"importScripts(eagRuntimeJSURL);" +
			"};" +
			"addEventListener(\"message\", function(evt) { globals.__eaglerXOnMessage(evt.data); });" +
			"})(self.__eaglercraftXLoaderContext = {}, self);";

	/** @type {string|null} */
	var workerURL = null;

	/**
	 * @return {Promise<boolean>}
	 */
	async function startIntegratedServerImpl() {
		if(!workerURL) {
			workerURL = URL.createObjectURL(new Blob([ workerBootstrapSource ], { "type": "text/javascript;charset=utf8" }));
		}

		try {
			workerObj = new Worker(workerURL);
		}catch(ex) {
			eagStackTrace(ERROR, "Failed to create worker", ex);
			return false;
		}

		workerObj.addEventListener("error", /** @type {function(Event)} */ (function(/** ErrorEvent */ evt) {
			eagStackTrace(ERROR, "Worker Error", /** @type {Error} */ (evt.error));
		}));

		workerObj.addEventListener("message", /** @type {function(Event)} */ (function(/** MessageEvent */ evt) {
			const channel = evt.data["ch"];
			
			if(!channel) {
				eagError("Recieved IPC packet with null channel");
				return;
			}
			
			if(channel === "~!LOGGER") {
				addLogMessageImpl(evt.data["txt"], evt.data["err"]);
				return;
			}
			
			const buf = evt.data["dat"];
			
			if(!buf) {
				eagError("Recieved IPC packet with null buffer");
				return;
			}
			
			if(serverLANPeerPassIPCFunc(channel, buf)) {
				return;
			}
			
			clientMessageQueue.push({
				"ch": channel,
				"data": new Uint8Array(buf),
				"_next": null
			});
		}));
		
		const classesTEADBGCopy = new Int8Array(classesTEADBG.length);
		classesTEADBGCopy.set(classesTEADBG, 0);
		
		var eagRuntimeJS;
		try {
			eagRuntimeJS = await fetch(/** @type {string} */ (eagRuntimeJSURL), { "cache": "force-cache" })
					.then((resp) => resp.arrayBuffer());
		}catch(ex) {
			eagStackTrace(ERROR, "Failed to fetch eagruntime.js contents", ex);
			try {
				workerObj.terminate();
			}catch(exx) {
			}
			return false;
		}
		
		workerObj.postMessage({
			"eaglercraftXOpts": eaglercraftXOpts,
			"eagruntimeJS": eagRuntimeJS,
			"classesWASM": classesWASMModule,
			"classesDeobfWASM": classesDeobfWASMModule,
			"classesTEADBG": classesTEADBGCopy.buffer
		});

		return true;
	};

	spImports["startIntegratedServer"] = new WebAssembly.Suspending(startIntegratedServerImpl);

	/**
	 * @param {string} channel
	 * @param {Uint8Array} arr
	 */
	spImports["sendPacket"] = function(channel, arr) {
		if(workerObj) {
			const copiedArray = new Uint8Array(arr.length);
			copiedArray.set(arr, 0);
			workerObj.postMessage({
				"ch": channel,
				"dat": copiedArray.buffer
			});
		}
	};

	/**
	 * @param {string} channel
	 * @param {!ArrayBuffer} arr
	 */
	sendIPCPacketFunc = function(channel, arr) {
		if(workerObj) {
			workerObj.postMessage({
				"ch": channel,
				"dat": arr
			});
		}
	};

	spImports["getAvailablePackets"] = clientMessageQueue.getLength.bind(clientMessageQueue);

	spImports["getNextPacket"] = clientMessageQueue.shift.bind(clientMessageQueue);

	spImports["killWorker"] = function() {
		if(workerObj) {
			workerObj.terminate();
			workerObj = null;
		}
	};

	/**
	 * @param {string} report
	 * @param {number} x
	 * @param {number} y
	 * @param {number} w
	 * @param {number} h
	 */
	spImports["showCrashReportOverlay"] = function(report, x, y, w, h) {
		if(!integratedServerCrashPanel) {
			integratedServerCrashPanel = /** @type {HTMLElement} */ (document.createElement("div"));
			integratedServerCrashPanel.setAttribute("style", "z-index:99;position:absolute;background-color:black;color:white;overflow-x:hidden;overflow-y:scroll;overflow-wrap:break-word;white-space:pre-wrap;font:18px sans-serif;padding:20px;display:none;");
			integratedServerCrashPanel.classList.add("_eaglercraftX_integratedserver_crash_element");
			parentElement.appendChild(integratedServerCrashPanel);
		}
		integratedServerCrashPanel.innerText = "";
		integratedServerCrashPanel.innerText = "CURRENT DATE: " + (new Date()).toLocaleString() + "\n\n" + report;
		const s = window.devicePixelRatio;
		integratedServerCrashPanel.style.top = "" + (y / s) + "px";
		integratedServerCrashPanel.style.left = "" + (x / s) + "px";
		integratedServerCrashPanel.style.width = "" + ((w / s) - 20) + "px";
		integratedServerCrashPanel.style.height = "" + ((h / s) - 20) + "px";
		integratedServerCrashPanel.style.display = "block";
		integratedServerCrashPanelShowing = true;
	};

	spImports["hideCrashReportOverlay"] = function() {
		if(integratedServerCrashPanel) {
			integratedServerCrashPanel.style.display = "none";
		}
		integratedServerCrashPanelShowing = false;
	};

	window.__curEaglerX188UnloadListenerCB = function() {
		if(workerObj) {
			workerObj.postMessage({
				"ch": "~!WASM_AUTOSAVE"
			});
		}
	};
}

function initializeNoClientPlatfSP(spImports) {
	setUnsupportedFunc(spImports, clientPlatfSPName, "startIntegratedServer");
	setUnsupportedFunc(spImports, clientPlatfSPName, "sendPacket");
	setUnsupportedFunc(spImports, clientPlatfSPName, "getAvailablePackets");
	setUnsupportedFunc(spImports, clientPlatfSPName, "getNextPacket");
	setUnsupportedFunc(spImports, clientPlatfSPName, "killWorker");
	setUnsupportedFunc(spImports, clientPlatfSPName, "showCrashReportOverlay");
	setUnsupportedFunc(spImports, clientPlatfSPName, "hideCrashReportOverlay");
}
