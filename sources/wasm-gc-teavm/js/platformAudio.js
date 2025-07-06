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

const platfAudioName = "platformAudio";

/** @type {HTMLAudioElement|null} */
var silenceElement = null;

function setCurrentAudioContext(audioContext, audioImports) {

	/**
	 * @return {AudioContext}
	 */
	audioImports["getContext"] = function() {
		return audioContext;
	};

	/**
	 * @param {number} addr
	 * @param {number} length
	 */
	audioImports["initKeepAliveHack"] = function(addr, length) {
		const copiedData = heapArrayBuffer.slice(addr, addr + length);
		const copiedDataURI = URL.createObjectURL(new Blob([copiedData], {type: "audio/wav"}));
		const audioElement = /** @type {HTMLAudioElement} */ (document.createElement("audio"));
		audioElement.classList.add("_eaglercraftX_keepalive_hack");
		audioElement.setAttribute("style", "display:none;");
		audioElement.autoplay = false;
		audioElement.loop = true;
		audioElement.addEventListener("seeked", function() {
			// NOP, wakes up the browser's event loop
		});
		audioElement.addEventListener("canplay", function() {
			if (silenceElement && document.visibilityState === "hidden") {
				silenceElement.play();
			}
		});
		const sourceElement = /** @type {HTMLSourceElement} */ (document.createElement("source"));
		sourceElement.type = "audio/wav";
		sourceElement.src = copiedDataURI;
		audioElement.appendChild(sourceElement);
		parentElement.appendChild(audioElement);
		silenceElement = audioElement;
	};

	handleVisibilityChange = function() {
		if (silenceElement) {
			if (document.visibilityState === "hidden") {
				silenceElement.play();
			} else {
				silenceElement.pause();
			}
		}
	};

	/**
	 * @param {PannerNode} node
	 * @param {number} maxDist
	 * @param {number} x
	 * @param {number} y
	 * @param {number} z
	 */
	audioImports["setupPanner"] = function(node, maxDist, x, y, z) {
		node.maxDistance = maxDist;
		node.rolloffFactor = 1.0;
		node.panningModel = "HRTF";
		node.distanceModel = "linear";
		node.coneInnerAngle = 360.0;
		node.coneOuterAngle = 0.0;
		node.coneOuterGain = 0.0;
		node.setOrientation(0.0, 1.0, 0.0);
		node.setPosition(x, y, z);
	};

	/**
	 * @param {AudioBufferSourceNode} sourceNode
	 * @param {Object} isEnded
	 */
	audioImports["registerIsEndedHandler"] = function(sourceNode, isEnded) {
		if(!isEnded["selfEndHandler"]) {
			isEnded["selfEndHandler"] = function(evt) {
				isEnded["isEnded"] = true;
			}
		}
		sourceNode.addEventListener("ended", isEnded["selfEndHandler"]);
	};

	/**
	 * @param {AudioBufferSourceNode} sourceNode
	 * @param {Object} isEnded
	 */
	audioImports["releaseIsEndedHandler"] = function(sourceNode, isEnded) {
		if(isEnded["selfEndHandler"]) {
			sourceNode.removeEventListener("ended", isEnded["selfEndHandler"]);
		}
	};

	/**
	 * @param {Uint8Array} fileData
	 * @param {string} errorFileName
	 * @return {Promise}
	 */
	function decodeAudioBrowserImpl(fileData, errorFileName) {
		return new Promise(function(resolve) {
			const copiedData = new Uint8Array(fileData.length);
			copiedData.set(fileData, 0);
			audioContext.decodeAudioData(copiedData.buffer, resolve, function(err) {
				eagError("Failed to load audio: {}", errorFileName);
				resolve(null);
			});
		});
	}

	audioImports["decodeAudioBrowser"] = new WebAssembly.Suspending(decodeAudioBrowserImpl);

}

function setNoAudioContext(audioImports) {
	audioImports["getContext"] = function() {
		return null;
	};
	setUnsupportedFunc(audioImports, platfAudioName, "setupPanner");
	setUnsupportedFunc(audioImports, platfAudioName, "initKeepAliveHack");
	setUnsupportedFunc(audioImports, platfAudioName, "registerIsEndedHandler");
	setUnsupportedFunc(audioImports, platfAudioName, "releaseIsEndedHandler");
	setUnsupportedFunc(audioImports, platfAudioName, "decodeAudioBrowser");
}
