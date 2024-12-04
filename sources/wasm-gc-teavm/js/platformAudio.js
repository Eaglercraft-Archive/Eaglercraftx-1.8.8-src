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

function setCurrentAudioContext(audioContext, audioImports) {

	/**
	 * @return {AudioContext}
	 */
	audioImports["getContext"] = function() {
		return audioContext;
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
	setUnsupportedFunc(audioImports, platfAudioName, "registerIsEndedHandler");
	setUnsupportedFunc(audioImports, platfAudioName, "releaseIsEndedHandler");
	setUnsupportedFunc(audioImports, platfAudioName, "decodeAudioBrowser");
}
