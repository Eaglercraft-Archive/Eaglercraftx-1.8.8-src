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

const platfRuntimeName = "platformRuntime";

var allowImmediateContinue = false;
const immediateContinueChannel = new MessageChannel();
var immediateContinueHandler = null;

immediateContinueChannel.port2.addEventListener("message", function(evt) {
	immediateContinueHandler();
});

async function initializePlatfRuntime() {
	immediateContinueChannel.port1.start();
	immediateContinueChannel.port2.start();

	immediateContinueHandler = function() {
		immediateContinueHandler = null;
	};

	immediateContinueChannel.port1.postMessage(0);

	if(immediateContinueHandler) {
		await new Promise(function(resolve) {
			setTimeout(function() {
				if(!immediateContinueHandler) {
					allowImmediateContinue = true;
				}else {
					eagError("Immediate continue hack is not supported");
				}
				resolve();
			}, 25);
		});
	}else {
		eagError("Immediate continue hack is not supported");
	}
}

/**
 * @return {HTMLElement}
 */
eagruntimeImpl.platformRuntime["getRootElement"] = function() {
	return rootElement;
};

/**
 * @return {HTMLElement}
 */
eagruntimeImpl.platformRuntime["getParentElement"] = function() {
	return parentElement;
};

/**
 * @return {HTMLCanvasElement}
 */
eagruntimeImpl.platformRuntime["getCanvasElement"] = function() {
	return canvasElement;
};

/**
 * @return {Object}
 */
eagruntimeImpl.platformRuntime["getEaglercraftXOpts"] = function() {
	return eaglercraftXOpts;
};

eagruntimeImpl.platformRuntime["getEventCount"] = mainEventQueue.getLength.bind(mainEventQueue);

eagruntimeImpl.platformRuntime["getNextEvent"] = mainEventQueue.shift.bind(mainEventQueue);

const EVENT_RUNTIME_ASYNC_DOWNLOAD = 0;

/**
 * @param {string} uri
 * @param {number} forceCache
 * @param {number} id
 */
eagruntimeImpl.platformRuntime["queueAsyncDownload"] = function(uri, forceCache, id) {
	try {
		fetch(uri, /** @type {!RequestInit} */ ({
			"cache": forceCache ? "force-cache" : "no-store",
			"mode": "cors"
		})).then(function(res) {
			return res.arrayBuffer();
		}).then(function(arr) {
			pushEvent(EVENT_TYPE_RUNTIME, EVENT_RUNTIME_ASYNC_DOWNLOAD, {
				"requestId": id,
				"arrayBuffer": arr
			});
		}).catch(function(err) {
			eagError("Failed to complete async download: {}", uri);
			eagStackTrace(ERROR, "Exception Caught", /** @type {Error} */ (err));
			pushEvent(EVENT_TYPE_RUNTIME, EVENT_RUNTIME_ASYNC_DOWNLOAD, {
				"requestId": id,
				"arrayBuffer": null
			});
		});
	}catch(/** Error */ ex) {
		eagError("Failed to fetch: {}", uri);
		eagStackTrace(ERROR, "Exception Caught", ex);
		pushEvent(EVENT_TYPE_RUNTIME, EVENT_RUNTIME_ASYNC_DOWNLOAD, {
			"requestId": id,
			"arrayBuffer": null
		});
	}
};

/**
 * @param {string} uri
 * @param {number} forceCache
 * @return {Promise}
 */
function downloadImpl(uri, forceCache) {
	return new Promise(function(resolve) {
		try {
			fetch(uri, /** @type {!RequestInit} */ ({
				"cache": forceCache ? "force-cache" : "no-store",
				"mode": "cors"
			})).then(function(res) {
				return res.arrayBuffer();
			}).then(function(arr) {
				resolve(arr);
			}).catch(function(err) {
				eagError("Failed to complete download: {}", uri);
				eagStackTrace(ERROR, "Exception Caught", /** @type {Error} */ (err));
				resolve(null);
			});
		}catch(/** Error */ ex) {
			eagError("Failed to fetch: {}", uri);
			eagStackTrace(ERROR, "Exception Caught", ex);
			resolve(null);
		}
	});
}

eagruntimeImpl.platformRuntime["download"] = new WebAssembly.Suspending(downloadImpl);

/**
 * @param {string} crashDump
 */
eagruntimeImpl.platformRuntime["writeCrashReport"] = function(crashDump) {
	displayCrashReport(crashDump, false);
};

eagruntimeImpl.platformRuntime["steadyTimeMillis"] = performance.now.bind(performance);

/**
 * @param {number} millis
 * @return {Promise}
 */
function sleepImpl(millis) {
	return new Promise(function(resolve) {
		setTimeout(resolve, millis);
	});
}

eagruntimeImpl.platformRuntime["sleep"] = new WebAssembly.Suspending(sleepImpl);

function immediateContinueResolver(resolve) {
	if(allowImmediateContinue) {
		immediateContinueHandler = resolve;
		immediateContinueChannel.port1.postMessage(0);
	}else {
		setTimeout(resolve, 0);
	}
}

/**
 * @return {Promise}
 */
function immediateContinueImpl() {
	return new Promise(immediateContinueResolver);
}

/**
 * @return {Promise}
 */
function swapDelayImpl() {
	if(!runtimeOpts.useDelayOnSwap) {
		return immediateContinueImpl();
	}else {
		return sleepImpl(0);
	}
}

eagruntimeImpl.platformRuntime["immediateContinue"] = new WebAssembly.Suspending(immediateContinueImpl);

/**
 * @param {number} id
 * @param {string} str
 */
eagruntimeImpl.platformRuntime["setCrashReportString"] = function(id, str) {
	crashReportStrings[id] = str;
};
