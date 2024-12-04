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

const platfScreenRecordName = "platformScreenRecord";

var canMic = (typeof window !== "undefined");
var mic = null;

/**
 * @return {Promise<MediaStream|null>}
 */
function getMic0() {
	return new Promise(function(resolve) {
		if ("navigator" in window && "mediaDevices" in window.navigator && "getUserMedia" in window.navigator.mediaDevices) {
			try {
				window.navigator.mediaDevices.getUserMedia({
					audio: true,
					video: false
				}).then(function(stream) {
					resolve(stream);
				}).catch(function(err) {
					eagError("getUserMedia Error! (async)");
					eagStackTrace(ERROR, "Exception Caught", /** @type {Error} */ (err));
					resolve(null);
				});
			} catch(e) {
				eagError("getUserMedia Error!");
				resolve(null);
			}
		} else {
			eagError("No getUserMedia!");
			resolve(null);
		}
	});
}

/**
 * @return {Promise<MediaStream|null>}
 */
async function getMicImpl() {
	if (canMic) {
		if (mic === null) {
			mic = await getMic0();
			if (mic === null) {
				canMic = false;
				return null;
			}
			return mic;
		}
		return mic;
	}
	return null;
}

function initializePlatfScreenRecord(screenRecImports) {

	eagruntimeImpl.platformScreenRecord["getMic"] = new WebAssembly.Suspending(getMicImpl);

	/**
	 * @param {string} nameStr
	 * @return {string}
	 */
	function formatScreenRecDate(nameStr) {
		const d = new Date();
		const fmt = d.getFullYear()
			+ "-" + ("0" + (d.getMonth() + 1)).slice(-2)
			+ "-" + ("0" + d.getDate()).slice(-2)
			+ " " + ("0" + d.getHours()).slice(-2)
			+ "-" + ("0" + d.getMinutes()).slice(-2)
			+ "-" + ("0" + d.getSeconds()).slice(-2);
		return nameStr.replace("${date}", fmt);
	}

	/**
	 * @param {MediaRecorder} mediaRec
	 * @param {boolean} isWebM
	 * @param {string} nameStr
	 */
	eagruntimeImpl.platformScreenRecord["setDataAvailableHandler"] = function(mediaRec, isWebM, nameStr) {
		const startTime = performance.now();
		mediaRec.addEventListener("dataavailable", function(evt) {
			if(isWebM) {
				fixWebMDuration(/** @type {!Blob} */ (evt.data), (performance.now() - startTime) | 0, function(/** !Blob */ b) {
					const blobUrl = URL.createObjectURL(b);
					downloadFileImpl(formatScreenRecDate(nameStr), blobUrl, function() {
						URL.revokeObjectURL(blobUrl);
					});
				}, {
					/**
					 * @param {string} str
					 */
					logger: function(str) {
						eagInfo(str);
					}
				});
			}else {
				const blobUrl = URL.createObjectURL(/** @type {!Blob} */ (evt.data));
				downloadFileImpl(formatScreenRecDate(nameStr), blobUrl, function() {
					URL.revokeObjectURL(blobUrl);
				});
			}
		});
	};

}

function initializeNoPlatfScreenRecord(screenRecImports) {
	setUnsupportedFunc(screenRecImports, platfScreenRecordName, "getMic");
	setUnsupportedFunc(screenRecImports, platfScreenRecordName, "setDataAvailableHandler");
}
