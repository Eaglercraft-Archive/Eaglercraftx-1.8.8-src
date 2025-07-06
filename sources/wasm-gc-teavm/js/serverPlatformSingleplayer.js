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

const serverPlatfSPName = "serverPlatformSingleplayer";

/** @type {function(string, boolean)|null} */
var sendIntegratedServerCrash = null;

/** @type {boolean} */
var isTabClosingFlag = false;

function initializeServerPlatfSP(spImports) {

	const serverMessageQueue = new EaglerLinkedQueue();

	/**
	 * @param {Object} o
	 */
	self.__eaglerXOnMessage = function(o) {
		const channel = o["ch"];
		const buf = o["dat"];
		
		if(!channel) {
			eagError("Recieved IPC packet with null channel");
			return;
		}
		
		if(channel === "~!WASM_AUTOSAVE") {
			isTabClosingFlag = true;
			return;
		}
		
		if(!buf) {
			eagError("Recieved IPC packet with null buffer");
			return;
		}
		
		serverMessageQueue.push({
			"ch": channel,
			"data": new Uint8Array(buf),
			"_next": null
		});
	};

	/**
	 * @param {string} channel
	 * @param {number} addr
	 * @param {number} length
	 */
	spImports["sendPacket"] = function(channel, addr, length) {
		const copiedArray = heapArrayBuffer.slice(addr, addr + length);
		postMessage({
			"ch": channel,
			"dat": copiedArray
		}, [copiedArray]);
	};

	spImports["getAvailablePackets"] = serverMessageQueue.getLength.bind(serverMessageQueue);

	spImports["getNextPacket"] = serverMessageQueue.shift.bind(serverMessageQueue);

	spImports["setCrashCallback"] = function() {
		return {
			"call": function(functor) {
				sendIntegratedServerCrash = functor;
			}
		};
	};

	/**
	 * @return {boolean}
	 */
	spImports["isTabAboutToClose"] = function() {
		const ret = isTabClosingFlag;
		isTabClosingFlag = false;
		return ret;
	};
}

function initializeNoServerPlatfSP(spImports) {
	setUnsupportedFunc(spImports, serverPlatfSPName, "sendPacket");
	setUnsupportedFunc(spImports, serverPlatfSPName, "getAvailablePackets");
	setUnsupportedFunc(spImports, serverPlatfSPName, "getNextPacket");
	setUnsupportedFunc(spImports, serverPlatfSPName, "setCrashCallback");
	setUnsupportedFunc(spImports, serverPlatfSPName, "isTabAboutToClose");
}
