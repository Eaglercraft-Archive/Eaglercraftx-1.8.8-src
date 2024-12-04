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

const READYSTATE_INIT_FAILED = -2;
const READYSTATE_FAILED = -1;
const READYSTATE_DISCONNECTED = 0;
const READYSTATE_CONNECTING = 1;
const READYSTATE_CONNECTED = 2;

const EVENT_WEBRTC_ICE = 0;
const EVENT_WEBRTC_DESC = 1;
const EVENT_WEBRTC_OPEN = 2;
const EVENT_WEBRTC_PACKET = 3;
const EVENT_WEBRTC_CLOSE = 4;

const platfWebRTCName = "platformWebRTC";

/** 
 * @typedef {{
 *     peerId:string,
 *     peerConnection:!RTCPeerConnection,
 *     dataChannel:RTCDataChannel,
 *     ipcChannel:(string|null),
 *     pushEvent:function(number,*),
 *     disconnect:function()
 * }}
 */
let LANPeerInternal;

function initializePlatfWebRTC(webrtcImports) {

	const clientLANPacketBuffer = new EaglerLinkedQueue();
	
	let lanClient;
	lanClient = {
		iceServers: [],
		/** @type {RTCPeerConnection|null} */
		peerConnection: null,
		/** @type {RTCDataChannel|null} */
		dataChannel: null,
		readyState: READYSTATE_CONNECTING,
		/** @type {string|null} */
		iceCandidate: null,
		/** @type {string|null} */
		description: null,
		dataChannelOpen: false,
		dataChannelClosed: true,
		disconnect: function(quiet) {
			if (lanClient.dataChannel) {
				try {
					lanClient.dataChannel.close();
				} catch (t) {
				}
				lanClient.dataChannel = null;
			}
			if (lanClient.peerConnection) {
				try {
					lanClient.peerConnection.close();
				} catch (t) {
				}
				lanClient.peerConnection = null;
			}
			if (!quiet) lanClient.dataChannelClosed = true;
			lanClient.readyState = READYSTATE_DISCONNECTED;
		}
	};

	/**
	 * @return {boolean}
	 */
	webrtcImports["supported"] = function() {
		return typeof RTCPeerConnection !== "undefined";
	};

	/**
	 * @return {number}
	 */
	webrtcImports["clientLANReadyState"] = function() {
		return lanClient.readyState;
	};

	webrtcImports["clientLANCloseConnection"] = function() {
		lanClient.disconnect(false);
	};

	/**
	 * @param {!Uint8Array} pkt
	 */
	webrtcImports["clientLANSendPacket"] = function(pkt) {
		if (lanClient.dataChannel !== null && "open" === lanClient.dataChannel.readyState) {
			try {
				lanClient.dataChannel.send(pkt);
			} catch (e) {
				lanClient.disconnect(false);
			}
		}else {
			lanClient.disconnect(false);
		}
	};

	/**
	 * @return {Uint8Array}
	 */
	webrtcImports["clientLANReadPacket"] = function() {
		const ret = clientLANPacketBuffer.shift();
		return ret ? new Uint8Array(ret["data"]) : null;
	};

	/**
	 * @return {number}
	 */
	webrtcImports["clientLANAvailable"] = function() {
		return clientLANPacketBuffer.getLength();
	};

	/**
	 * @param {!Array<string>} servers
	 */
	webrtcImports["clientLANSetICEServersAndConnect"] = function(servers) {
		lanClient.iceServers.length = 0;
		for (let url of servers) {
			let etr = url.split(";");
			if(etr.length === 1) {
				lanClient.iceServers.push({
					urls: etr[0]
				});
			}else if(etr.length === 3) {
				lanClient.iceServers.push({
					urls: etr[0],
					username: etr[1],
					credential: etr[2]
				});
			}
		}
		if(lanClient.readyState === READYSTATE_CONNECTED || lanClient.readyState === READYSTATE_CONNECTING) {
			lanClient.disconnect(true);
		}
		try {
			if (lanClient.dataChannel) {
				try {
					lanClient.dataChannel.close();
				} catch (t) {
				}
				lanClient.dataChannel = null;
			}
			if (lanClient.peerConnection) {
				try {
					lanClient.peerConnection.close();
				} catch (t) {
				}
			}
			lanClient.peerConnection = new RTCPeerConnection({
				iceServers: lanClient.iceServers,
				optional: [
					{
						DtlsSrtpKeyAgreement: true
					}
				]
			});
			lanClient.readyState = READYSTATE_CONNECTING;
		} catch (/** Error */ t) {
			eagStackTrace(ERROR, "Could not create LAN client RTCPeerConnection!", t);
			lanClient.readyState = READYSTATE_INIT_FAILED;
			return;
		}

		try {
			const iceCandidates = [];

			lanClient.peerConnection.addEventListener("icecandidate", /** @type {function(Event)} */ ((/** !RTCPeerConnectionIceEvent */ evt) => {
				if(evt.candidate) {
					if(iceCandidates.length === 0) {
						const candidateState = [0, 0];
						const runnable = () => {
							if(lanClient.peerConnection !== null && lanClient.peerConnection.connectionState !== "disconnected") {
								const trial = ++candidateState[1];
								if(candidateState[0] !== iceCandidates.length && trial < 3) {
									candidateState[0] = iceCandidates.length;
									setTimeout(runnable, 2000);
									return;
								}
								lanClient.iceCandidate = JSON.stringify(iceCandidates);
								iceCandidates.length = 0;
							}
						};
						setTimeout(runnable, 2000);
					}
					iceCandidates.push({
						"sdpMLineIndex": evt.candidate.sdpMLineIndex,
						"candidate": evt.candidate.candidate
					});
				}
			}));

			lanClient.dataChannel = lanClient.peerConnection.createDataChannel("lan");
			lanClient.dataChannel.binaryType = "arraybuffer";

			let evtHandler;
			evtHandler = () => {
				if (iceCandidates.length > 0) {
					setTimeout(evtHandler, 10);
					return;
				}
				lanClient.dataChannelClosed = false;
				lanClient.dataChannelOpen = true;
			};

			lanClient.dataChannel.addEventListener("open", evtHandler);

			lanClient.dataChannel.addEventListener("message", /** @type {function(Event)} */ ((/** MessageEvent */ evt) => {
				clientLANPacketBuffer.push({ "data": evt.data, "_next": null });
			}));

			lanClient.peerConnection.createOffer().then((/** !RTCSessionDescription */ desc) => {
				lanClient.peerConnection.setLocalDescription(desc).then(() => {
					lanClient.description = JSON.stringify(desc);
				}).catch((err) => {
					eagError("Failed to set local description! {}", /** @type {string} */ (err.message));
					lanClient.readyState = READYSTATE_FAILED;
					lanClient.disconnect(false);
				});
			}).catch((err) => {
				eagError("Failed to set create offer! {}", /** @type {string} */ (err.message));
				lanClient.readyState = READYSTATE_FAILED;
				lanClient.disconnect(false);
			});

			lanClient.peerConnection.addEventListener("connectionstatechange", /** @type {function(Event)} */ ((evt) => {
				var connectionState = lanClient.peerConnection.connectionState;
				if ("disconnected" === connectionState) {
					lanClient.disconnect(false);
				} else if ("connected" === connectionState) {
					lanClient.readyState = READYSTATE_CONNECTED;
				} else if ("failed" === connectionState) {
					lanClient.readyState = READYSTATE_FAILED;
					lanClient.disconnect(false);
				}
			}));
		} catch (t) {
			if (lanClient.dataChannel) {
				try {
					lanClient.dataChannel.close();
				} catch (tt) {
				}
				lanClient.dataChannel = null;
			}
			if (lanClient.peerConnection) {
				try {
					lanClient.peerConnection.close();
				} catch (tt) {
				}
				lanClient.peerConnection = null;
			}
			eagStackTrace(ERROR, "Could not create LAN client RTCDataChannel!", t);
			lanClient.readyState = READYSTATE_INIT_FAILED;
		}
	};

	webrtcImports["clearLANClientState"] = function() {
		lanClient.iceCandidate = lanClient.description = null;
		lanClient.dataChannelOpen = false;
		lanClient.dataChannelClosed = true;
	};

	/**
	 * @return {string|null}
	 */
	webrtcImports["clientLANAwaitICECandidate"] = function() {
		if (lanClient.iceCandidate === null) {
			return null;
		}
		const ret = lanClient.iceCandidate;
		lanClient.iceCandidate = null;
		return ret;
	};

	/**
	 * @return {string|null}
	 */
	webrtcImports["clientLANAwaitDescription"] = function() {
		if (lanClient.description === null) {
			return null;
		}
		const ret = lanClient.description;
		lanClient.description = null;
		return ret;
	};

	/**
	 * @return {boolean}
	 */
	webrtcImports["clientLANAwaitChannel"] = function() {
		if (lanClient.dataChannelOpen) {
			lanClient.dataChannelOpen = false;
			return true;
		}
		return false;
	};

	/**
	 * @return {boolean}
	 */
	webrtcImports["clientLANClosed"] = function() {
		return lanClient.dataChannelClosed;
	};

	/**
	 * @param {string} candidate
	 */
	webrtcImports["clientLANSetICECandidate"] = function(candidate) {
		try {
			const lst = /** @type {Array<!Object>} */ (JSON.parse(candidate));
			for (var i = 0; i < lst.length; ++i) {
				lanClient.peerConnection.addIceCandidate(new RTCIceCandidate(lst[i]));
			}
		}catch(/** Error */ ex) {
			eagStackTrace(ERROR, "Uncaught exception setting remote ICE candidates", ex);
			lanClient.readyState = READYSTATE_FAILED;
			lanClient.disconnect(false);
		}
	};

	/**
	 * @param {string} description
	 */
	webrtcImports["clientLANSetDescription"] = function(description) {
		try {
			lanClient.peerConnection.setRemoteDescription(/** @type {!RTCSessionDescription} */ (JSON.parse(description)));
		}catch(/** Error */ ex) {
			eagStackTrace(ERROR, "Uncaught exception setting remote description", ex);
			lanClient.readyState = READYSTATE_FAILED;
			lanClient.disconnect(false);
		}
	};

	let lanServer;
	lanServer = {
		/** @type {!Array<Object>} */
		iceServers: [],
		/** @type {!Map<string, !LANPeerInternal>} */
		peerList: new Map(),
		/** @type {!Map<string, !LANPeerInternal>} */
		ipcMapList: new Map(),
		disconnect: function(/** string */ peerId) {
			const thePeer = lanServer.peerList.get(peerId);
			if(thePeer) {
				lanServer.peerList.delete(peerId);
				if(thePeer.ipcChannel) {
					lanServer.ipcMapList.delete(thePeer.ipcChannel);
				}
				try {
					thePeer.disconnect();
				} catch (ignored) {}
				thePeer.pushEvent(EVENT_WEBRTC_CLOSE, null);
			}
		}
	};

	/**
	 * @param {!Array<string>} servers
	 */
	webrtcImports["serverLANInitializeServer"] = function(servers) {
		lanServer.iceServers.length = 0;
		for (let url of servers) {
			let etr = url.split(";");
			if(etr.length === 1) {
				lanServer.iceServers.push({
					"urls": etr[0]
				});
			}else if(etr.length === 3) {
				lanServer.iceServers.push({
					"urls": etr[0],
					"username": etr[1],
					"credential": etr[2]
				});
			}
		}
	};

	webrtcImports["serverLANCloseServer"] = function() {
		for (let thePeer of Object.values(lanServer.peerList)) {
			if (thePeer) {
				try {
					thePeer.disconnect();
				} catch (e) {}
				thePeer.pushEvent(EVENT_WEBRTC_CLOSE, null);
			}
		}
		lanServer.peerList.clear();
	};

	/**
	 * @param {string} peer
	 */
	webrtcImports["serverLANCreatePeer"] = function(peer) {
		try {
			const events = new EaglerLinkedQueue();

			/** @type {!LANPeerInternal} */
			let peerInstance;
			peerInstance = {
				peerId: peer,
				peerConnection: new RTCPeerConnection(/** @type {RTCConfiguration} */ ({
					"iceServers": lanServer.iceServers,
					"optional": [
						{
							"DtlsSrtpKeyAgreement": true
						}
					]
				})),
				/** @type {RTCDataChannel} */
				dataChannel: null,
				/** @type {string|null} */
				ipcChannel: null,
				pushEvent: function(type, data) {
					events.push({
						"type": type,
						"data": data,
						"_next": null
					});
				},
				disconnect: function() {
					if (peerInstance.dataChannel) peerInstance.dataChannel.close();
					peerInstance.peerConnection.close();
				}
			};

			lanServer.peerList.set(peerInstance.peerId, peerInstance);

			const iceCandidates = [];

			peerInstance.peerConnection.addEventListener("icecandidate", /** @type {function(Event)} */ ((/** RTCPeerConnectionIceEvent */ evt) => {
				if(evt.candidate) {
					if(iceCandidates.length === 0) {
						const candidateState = [0, 0];
						const runnable = () => {
							if(peerInstance.peerConnection !== null && peerInstance.peerConnection.connectionState !== "disconnected") {
								const trial = ++candidateState[1];
								if(candidateState[0] !== iceCandidates.length && trial < 3) {
									candidateState[0] = iceCandidates.length;
									setTimeout(runnable, 2000);
									return;
								}
								peerInstance.pushEvent(EVENT_WEBRTC_ICE, JSON.stringify(iceCandidates));
								iceCandidates.length = 0;
							}
						};
						setTimeout(runnable, 2000);
					}
					iceCandidates.push({
						"sdpMLineIndex": evt.candidate.sdpMLineIndex,
						"candidate": evt.candidate.candidate
					});
				}
			}));

			let evtHandler;
			evtHandler = (/** RTCDataChannelEvent */ evt) => {
				if (iceCandidates.length > 0) {
					setTimeout(evtHandler, 10, evt);
					return;
				}
				if (!evt.channel) return;
				const newDataChannel = evt.channel;
				if(peerInstance.dataChannel !== null) {
					newDataChannel.close();
					return;
				}
				peerInstance.dataChannel = newDataChannel;
				peerInstance.pushEvent(EVENT_WEBRTC_OPEN, null);
				peerInstance.dataChannel.addEventListener("message", (evt2) => {
					const data = evt2.data;
					if(peerInstance.ipcChannel) {
						sendIPCPacketFunc(peerInstance.ipcChannel, data);
					}else {
						peerInstance.pushEvent(EVENT_WEBRTC_PACKET, new Uint8Array(data));
					}
				});
			};

			peerInstance.peerConnection.addEventListener("datachannel", /** @type {function(Event)} */ (evtHandler));

			peerInstance.peerConnection.addEventListener("connectionstatechange", (evt) => {
				const connectionState = peerInstance.peerConnection.connectionState;
				if ("disconnected" === connectionState || "failed" === connectionState) {
					lanServer.disconnect(peerInstance.peerId);
				}
			});

			return {
				"peerId": peerInstance.peerId,
				/**
				 * @return {number}
				 */
				"countAvailableEvents": function() {
					return events.getLength();
				},
				/**
				 * @return {Object}
				 */
				"nextEvent": function() {
					return events.shift();
				},
				/**
				 * @param {!Uint8Array} dat
				 */
				"writePacket": function(dat) {
					let b = false;
					if (peerInstance.dataChannel !== null && "open" === peerInstance.dataChannel.readyState) {
						try {
							peerInstance.dataChannel.send(dat);
						} catch (e) {
							b = true;
						}
					} else {
						b = true;
					}
					if(b) {
						lanServer.disconnect(peerInstance.peerId);
					}
				},
				/**
				 * @param {string} iceCandidates
				 */
				"handleRemoteICECandidates": function(iceCandidates) {
					try {
						const candidateList = /** @type {!Array<!RTCIceCandidateInit>} */ (JSON.parse(iceCandidates));
						for (let candidate of candidateList) {
							peerInstance.peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
						}
					} catch (err) {
						eagError("Failed to parse ice candidate for \"{}\"! {}", peerInstance.peerId, err.message);
						lanServer.disconnect(peerInstance.peerId);
					}
				},
				/**
				 * @param {string} desc
				 */
				"handleRemoteDescription": function(desc) {
					try {
						const remoteDesc = /** @type {!RTCSessionDescription} */ (JSON.parse(desc));
						peerInstance.peerConnection.setRemoteDescription(remoteDesc).then(() => {
							if (remoteDesc.hasOwnProperty("type") && "offer" === remoteDesc["type"]) {
								peerInstance.peerConnection.createAnswer().then((desc) => {
									peerInstance.peerConnection.setLocalDescription(desc).then(() => {
										peerInstance.pushEvent(EVENT_WEBRTC_DESC, JSON.stringify(desc));
									}).catch((err) => {
										eagError("Failed to set local description for \"{}\"! {}", peerInstance.peerId, err.message);
										lanServer.disconnect(peerInstance.peerId);
									});
								}).catch((err) => {
									eagError("Failed to create answer for \"{}\"! {}", peerInstance.peerId, err.message);
									lanServer.disconnect(peerInstance.peerId);
								});
							}
						}).catch((err) => {
							eagError("Failed to set remote description for \"{}\"! {}", peerInstance.peerId, err.message);
							lanServer.disconnect(peerInstance.peerId);
						});
					} catch (err) {
						eagError("Failed to parse remote description for \"{}\"! {}", peerInstance.peerId, err.message);
						lanServer.disconnect(peerInstance.peerId);
					}
				},
				/**
				 * @param {string|null} ipcChannel
				 */
				"mapIPC": function(ipcChannel) {
					if(!peerInstance.ipcChannel) {
						if(ipcChannel) {
							peerInstance.ipcChannel = ipcChannel;
							lanServer.ipcMapList.set(ipcChannel, peerInstance);
						}
					}else {
						if(!ipcChannel) {
							lanServer.ipcMapList.delete(peerInstance.ipcChannel);
							peerInstance.ipcChannel = null;
						}
					}
				},
				"disconnect": function() {
					lanServer.disconnect(peerInstance.peerId);
				}
			};
		}catch(/** Error */ tt) {
			eagStackTrace(ERROR, "Failed to create WebRTC LAN peer!", tt);
			return null;
		}
	};

	/**
	 * @param {string} channel
	 * @param {!ArrayBuffer} arr
	 */
	serverLANPeerPassIPCFunc = function(channel, arr) {
		const peer = lanServer.ipcMapList.get(channel);
		if(peer) {
			let b = false;
			if (peer.dataChannel && "open" === peer.dataChannel.readyState) {
				try {
					peer.dataChannel.send(arr);
				} catch (e) {
					b = true;
				}
			} else {
				b = true;
			}
			if(b) {
				lanServer.disconnect(peer.peerId);
			}
			return true;
		}else {
			return false;
		}
	};

}

function initializeNoPlatfWebRTC(webrtcImports) {
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "supported");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "clientLANReadyState");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "clientLANCloseConnection");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "clientLANSendPacket");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "clientLANReadPacket");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "clientLANAvailable");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "clientLANSetICEServersAndConnect");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "clearLANClientState");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "clientLANAwaitICECandidate");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "clientLANAwaitDescription");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "clientLANAwaitChannel");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "clientLANClosed");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "clientLANSetICECandidate");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "clientLANSetDescription");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "clientLANClosed");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "serverLANInitializeServer");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "serverLANCloseServer");
	setUnsupportedFunc(webrtcImports, platfWebRTCName, "serverLANCreatePeer");
}
