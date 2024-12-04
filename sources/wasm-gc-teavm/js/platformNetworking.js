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

const platfNetworkingName = "platformNetworking";

(function() {

	const WS_CLOSED = 0;
	const WS_CONNECTING = 1;
	const WS_CONNECTED = 2;
	const WS_FAILED = 3;

	function closeSocketImpl() {
		this["_socket"].close();
	}

	/**
	 * @param {string} str
	 */
	function sendStringFrameImpl(str) {
		this["_socket"].send(str);
	}

	/**
	 * @param {Uint8Array} bin
	 */
	function sendBinaryFrameImpl(bin) {
		this["_socket"].send(bin);
	}

	/**
	 * @return {number}
	 */
	function availableFramesImpl() {
		return this["_frameCountStr"] + this["_frameCountBin"];
	}

	/**
	 * @return {Object}
	 */
	function getNextFrameImpl() {
		const f = this["_queue"];
		if(f) {
			if(f["_next"] === f && f["_prev"] === f) {
				this["_queue"] = null;
			}else {
				this["_queue"] = f["_next"];
				f["_prev"]["_next"] = f["_next"];
				f["_next"]["_prev"] = f["_prev"];
			}
			f["_next"] = null;
			f["_prev"] = null;
			if(f["type"] === 0) {
				--this["_frameCountStr"];
			}else {
				--this["_frameCountBin"];
			}
			return f;
		}else {
			return null;
		}
	}

	/**
	 * @return {Array}
	 */
	function getAllFramesImpl() {
		const len = this["_frameCountStr"] + this["_frameCountBin"];
		if(len === 0) {
			return null;
		}
		const ret = new Array(len);
		var idx = 0;
		var f = this["_queue"];
		var g;
		const ff = f;
		do {
			ret[idx++] = f;
			g = f["_next"];
			f["_next"] = null;
			f["_prev"] = null;
			f = g;
		}while(f !== ff);
		this["_queue"] = null;
		this["_frameCountStr"] = 0;
		this["_frameCountBin"] = 0;
		return ret;
	}

	function clearFramesImpl() {
		this["_queue"] = null;
		this["_frameCountStr"] = 0;
		this["_frameCountBin"] = 0;
	}

	/**
	 * @param {Object} thisObj
	 * @param {number} type
	 * @return {Object}
	 */
	function getNextTypedFrameImpl(thisObj, type) {
		var f = thisObj["_queue"];
		if(!f) {
			return null;
		}
		var g, h;
		const ff = f;
		do {
			g = f["_next"];
			if(f["type"] === type) {
				h = f["_prev"];
				if(g === f && h === f) {
					thisObj["_queue"] = null;
				}else {
					if(f === ff) {
						thisObj["_queue"] = g;
					}
					h["_next"] = g;
					g["_prev"] = h;
				}
				f["_next"] = null;
				f["_prev"] = null;
				return f;
			}
			f = g;
		}while(f !== ff);
		return null;
	}

	/**
	 * @param {Object} thisObj
	 * @param {number} type
	 * @param {Array} ret
	 */
	function getAllTypedFrameImpl(thisObj, type, ret) {
		var idx = 0;
		var f = thisObj["_queue"];
		var g, h;
		const ff = f;
		do {
			g = f["_next"];
			if(f["type"] === type) {
				ret[idx++] = f;
			}
			f = g;
		}while(f !== ff);
		ret.length = idx;
		for(var i = 0; i < idx; ++i) {
			f = ret[i];
			g = f["_next"];
			h = f["_prev"];
			if(g === f && h === f) {
				thisObj["_queue"] = null;
			}else {
				if(f === thisObj["_queue"]) {
					thisObj["_queue"] = g;
				}
				h["_next"] = g;
				g["_prev"] = h;
			}
		}
	}

	/**
	 * @return {number}
	 */
	function availableStringFramesImpl() {
		return this["_frameCountStr"];
	}

	/**
	 * @return {Object}
	 */
	function getNextStringFrameImpl() {
		const len = this["_frameCountStr"];
		if(len === 0) {
			return null;
		}
		const ret = getNextTypedFrameImpl(this, 0);
		if(!ret) {
			this["_frameCountStr"] = 0;
		}else {
			--this["_frameCountStr"];
		}
		return ret;
	}

	/**
	 * @return {Array}
	 */
	function getAllStringFramesImpl() {
		const len = this["_frameCountStr"];
		if(len === 0) {
			return null;
		}
		const ret = new Array(len);
		getAllTypedFrameImpl(this, 0, ret);
		this["_frameCountStr"] = 0;
		return ret;
	}

	function clearStringFramesImpl() {
		const len = this["_frameCountStr"];
		if(len === 0) {
			return null;
		}
		const ret = new Array(len);
		getAllTypedFrameImpl(this, 0, ret);
		this["_frameCountStr"] = 0;
	}

	/**
	 * @return {number}
	 */
	function availableBinaryFramesImpl() {
		return this["_frameCountBin"];
	}

	/**
	 * @return {Object}
	 */
	function getNextBinaryFrameImpl() {
		const len = this["_frameCountBin"];
		if(len === 0) {
			return null;
		}
		const ret = getNextTypedFrameImpl(this, 1);
		if(!ret) {
			this["_frameCountBin"] = 0;
		}else {
			--this["_frameCountBin"];
		}
		return ret;
	}

	/**
	 * @return {Array}
	 */
	function getAllBinaryFramesImpl() {
		const len = this["_frameCountBin"];
		if(len === 0) {
			return null;
		}
		const ret = new Array(len);
		getAllTypedFrameImpl(this, 1, ret);
		this["_frameCountBin"] = 0;
		return ret;
	}

	function clearBinaryFramesImpl() {
		const len = this["_frameCountBin"];
		if(len === 0) {
			return null;
		}
		const ret = new Array(len);
		getAllTypedFrameImpl(this, 1, ret);
		this["_frameCountBin"] = 0;
	}

	function addRecievedFrameImpl(dat) {
		const isStr = (typeof dat === "string");
		const itm = {
			"type": (isStr ? 0 : 1),
			"data": dat,
			"timestamp": performance.now(),
			"_next": null,
			"_prev": null
		};
		const first = this["_queue"];
		if(!first) {
			this["_queue"] = itm;
			itm["_next"] = itm;
			itm["_prev"] = itm;
		}else {
			const last = first["_prev"];
			last["_next"] = itm;
			itm["_prev"] = last;
			itm["_next"] = first;
			first["_prev"] = itm;
		}
		if(isStr) {
			++this["_frameCountStr"];
		}else {
			++this["_frameCountBin"];
		}
	}

	/**
	 * @param {string} socketURI
	 * @return {Object}
	 */
	eagruntimeImpl.platformNetworking["createWebSocketHandle"] = function(socketURI) {
		let sock;
		
		try {
			sock = new WebSocket(socketURI);
		}catch(ex) {
			eagError("Failed to create WebSocket: {}", socketURI);
			eagStackTrace(ERROR, "Exception Caught", ex);
			return null;
		}
		
		sock.binaryType = "arraybuffer";
		
		const ret = {
			"state": WS_CONNECTING,
			"_socket": sock,
			"_queue": null,
			"_frameCountStr": 0,
			"_frameCountBin": 0,
			"_addRecievedFrame": addRecievedFrameImpl,
			"closeSocket": closeSocketImpl,
			"sendStringFrame": sendStringFrameImpl,
			"sendBinaryFrame": sendBinaryFrameImpl,
			"availableFrames": availableFramesImpl,
			"getNextFrame": getNextFrameImpl,
			"getAllFrames": getAllFramesImpl,
			"clearFrames": clearFramesImpl,
			"availableStringFrames": availableStringFramesImpl,
			"getNextStringFrame": getNextStringFrameImpl,
			"getAllStringFrames": getAllStringFramesImpl,
			"clearStringFrames": clearStringFramesImpl,
			"availableBinaryFrames": availableBinaryFramesImpl,
			"getNextBinaryFrame": getNextBinaryFrameImpl,
			"getAllBinaryFrames": getAllBinaryFramesImpl,
			"clearBinaryFrames": clearBinaryFramesImpl
		};
		
		sock.addEventListener("open", function(evt) {
			ret["state"] = WS_CONNECTED;
		});
		
		sock.addEventListener("message", function(evt) {
			ret["_addRecievedFrame"](evt.data);
		});
		
		sock.addEventListener("close", function(evt) {
			if(ret["state"] !== WS_FAILED) {
				ret["state"] = WS_CLOSED;
			}
		});
		
		sock.addEventListener("error", function(evt) {
			if(ret["state"] === WS_CONNECTING) {
				ret["state"] = WS_FAILED;
			}
		});
		
		return ret;
	};

})();
