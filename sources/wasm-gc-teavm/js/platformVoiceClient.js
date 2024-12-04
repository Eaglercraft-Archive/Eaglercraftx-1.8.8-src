/*
 * Copyright (c) 2024 lax1dude, ayunami2000. All Rights Reserved.
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

const EVENT_VOICE_ICE = 0;
const EVENT_VOICE_DESC = 1;
const EVENT_VOICE_OPEN = 2;
const EVENT_VOICE_CLOSE = 3;

const platfVoiceClientName = "platformVoiceClient";

function initializePlatfVoiceClient(voiceClientImports) {

	/**
	 * @return {boolean}
	 */
	voiceClientImports["isSupported"] = function() {
		return typeof navigator.mediaDevices !== "undefined" && typeof navigator.mediaDevices.getUserMedia !== "undefined" && "srcObject" in HTMLAudioElement.prototype;
	};

	/**
	 * @param {string} desc
	 * @suppress {globalThis}
	 */
	function setRemoteDescriptionImpl(desc) {
		try {
			const remoteDesc = JSON.parse(desc);
			this["_peerConnection"].setRemoteDescription(remoteDesc).then(() => {
				if (remoteDesc.hasOwnProperty("type") && "offer" === remoteDesc["type"]) {
					this["_peerConnection"].createAnswer().then((desc) => {
						this["_peerConnection"].setLocalDescription(desc).then(() => {
							pushEvent(EVENT_TYPE_VOICE, EVENT_VOICE_DESC, {
								"objId": this["objId"],
								"data": JSON.stringify(desc)
							});
						}).catch((err) => {
							eagError("Failed to set local description for \"{}\"! {}", this["objId"], err.message);
							pushEvent(EVENT_TYPE_VOICE, EVENT_VOICE_CLOSE, {
								"objId": this["objId"]
							});
						});
					}).catch((err) => {
						eagError("Failed to create answer for \"{}\"! {}", this["objId"], err.message);
						pushEvent(EVENT_TYPE_VOICE, EVENT_VOICE_CLOSE, {
							"objId": this["objId"]
						});
					});
				}
			}).catch((err) => {
				eagError("Failed to set remote description for \"{}\"! {}", this["objId"], err.message);
				pushEvent(EVENT_TYPE_VOICE, EVENT_VOICE_CLOSE, {
					"objId": this["objId"]
				});
			});
		} catch (e) {
			eagError(e.message);
			pushEvent(EVENT_TYPE_VOICE, EVENT_VOICE_CLOSE, {
				"objId": this["objId"]
			});
		}
	}

	/**
	 * @param {string} ice
	 * @suppress {globalThis}
	 */
	function addRemoteICECandidateImpl(ice) {
		try {
			this["_peerConnection"].addIceCandidate(new RTCIceCandidate(/** @type {!RTCIceCandidateInit} */ (JSON.parse(ice)))).catch((err) => {
				eagError("Failed to parse ice candidate for \"{}\"! {}", this["objId"], err.message);
				pushEvent(EVENT_TYPE_VOICE, EVENT_VOICE_CLOSE, {
					"objId": this["objId"]
				});
			});
		} catch (e) {
			eagError(e.message);
			pushEvent(EVENT_TYPE_VOICE, EVENT_VOICE_CLOSE, {
				"objId": this["objId"]
			});
		}
	}

	/**
	 * @suppress {globalThis}
	 */
	function closeImpl() {
		this["_peerConnection"].close();
	}

	let idCounter = 0;

	/**
	 * @param {string} iceServers
	 * @param {number} offer
	 * @param {!MediaStream} localStream
	 * @return {Object}
	 */
	voiceClientImports["createRTCPeerConnection"] = function(iceServers, offer, localStream) {
		try {
			const peerId = idCounter++;
			var ret;
			const peerConnection = new RTCPeerConnection(/** @type {!RTCConfiguration} */ ({
				"iceServers": JSON.parse(iceServers),
				"optional": [
					{
						"DtlsSrtpKeyAgreement": true
					}
				]
			}));
			
			peerConnection.addEventListener("icecandidate", /** @type {function(Event)} */ ((/** RTCPeerConnectionIceEvent */ evt) => {
				if (evt.candidate) {
					pushEvent(EVENT_TYPE_VOICE, EVENT_VOICE_ICE, {
						"objId": peerId,
						"data": JSON.stringify({
							"sdpMLineIndex": "" + evt.candidate.sdpMLineIndex,
							"candidate": evt.candidate.candidate
						})
					});
				}
			}));
			peerConnection.addEventListener("track", /** @type {function(Event)} */ ((/** RTCTrackEvent */ evt) => {
				const rawStream = evt.streams[0];
				ret["_aud"] = document.createElement("audio");
				ret["_aud"]["autoplay"] = true;
				ret["_aud"]["muted"] = true;
				ret["_aud"]["srcObject"] = rawStream;
				pushEvent(EVENT_TYPE_VOICE, EVENT_VOICE_OPEN, {
					"objId": peerId,
					"stream": rawStream
				});
			}));

			localStream.getTracks().forEach(function(track) {
				peerConnection.addTrack(track, localStream);
			});
			if (offer) {
				peerConnection.createOffer().then((desc) => {
					peerConnection.setLocalDescription(desc).then(() => {
						pushEvent(EVENT_TYPE_VOICE, EVENT_VOICE_DESC, {
							"objId": peerId,
							"data": JSON.stringify(desc)
						});
					}).catch((err) => {
						eagError("Failed to set local description for \"{}\"! {}", peerId, err.message);
						pushEvent(EVENT_TYPE_VOICE, EVENT_VOICE_CLOSE, {
							"objId": peerId
						});
					});
				}).catch((err) => {
					eagError("Failed to set create offer for \"{}\"! {}", peerId, err.message);
					pushEvent(EVENT_TYPE_VOICE, EVENT_VOICE_CLOSE, {
						"objId": peerId
					});
				});
			}

			peerConnection.addEventListener("connectionstatechange", () => {
				const cs = peerConnection.connectionState;
				if ("disconnected" === cs || "failed" === cs) {
					pushEvent(EVENT_TYPE_VOICE, EVENT_VOICE_CLOSE, {
						"objId": peerId
					});
				}
			});

			return ret = {
				"objId": peerId,
				"_peerConnection": peerConnection,
				"setRemoteDescription": setRemoteDescriptionImpl,
				"addRemoteICECandidate": addRemoteICECandidateImpl,
				"closeHandle": closeImpl
			};		
		} catch (e) {
			eagError(e.message);
			return null;
		}
	};

}

function initializeNoPlatfVoiceClient(voiceClientImports) {
	setUnsupportedFunc(voiceClientImports, platfVoiceClientName, "isSupported");
	setUnsupportedFunc(voiceClientImports, platfVoiceClientName, "createRTCPeerConnection");
}
