/*
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.internal;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.internal.ClientPlatformSingleplayer;
import net.lax1dude.eaglercraft.v1_8.sp.lan.LANPeerEvent;

import org.json.JSONObject;
import org.json.JSONWriter;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.TimerHandler;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSError;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.json.JSON;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.websocket.WebSocket;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.*;

public class PlatformWebRTC {

	private static final Logger logger = LogManager.getLogger("PlatformWebRTC");

	static final int WEBRTC_SUPPORT_NONE = 0;
	static final int WEBRTC_SUPPORT_CORE = 1;
	static final int WEBRTC_SUPPORT_WEBKIT = 2;
	static final int WEBRTC_SUPPORT_MOZ = 3;
	static final int WEBRTC_SUPPORT_CORE_NON_PROMISING = 4;

	@JSBody(script = "var checkPromising = function() { try {"
			+ "return (typeof (new RTCPeerConnection({iceServers:[{urls:\"stun:127.69.0.1:6969\"}]})).createOffer() === \"object\") ? 1 : 4;"
			+ "} catch(ex) {"
			+ "return (ex.name === \"TypeError\") ? 4 : 1;"
			+ "}};"
			+ "return (typeof RTCPeerConnection !== \"undefined\")"
			+ " ? checkPromising()"
			+ " : ((typeof webkitRTCPeerConnection !== \"undefined\") ? 2"
			+ " : ((typeof mozRTCPeerConnection !== \"undefined\") ? 3"
			+ " : 0));")
	private static native int checkSupportedImpl();

	static boolean hasCheckedSupport = false;
	static int supportedImpl = WEBRTC_SUPPORT_NONE;
	static boolean useSessionDescConstructor = false;
	static boolean useOldConnStateCheck = false;
	static boolean belowChrome71Fix = false;

	public static boolean supported() {
		if(!hasCheckedSupport) {
			supportedImpl = checkSupportedImpl();
			hasCheckedSupport = true;
			if(supportedImpl == WEBRTC_SUPPORT_NONE) {
				logger.error("WebRTC is not supported on this browser!");
			}else if(supportedImpl == WEBRTC_SUPPORT_WEBKIT) {
				logger.info("Using webkit- prefix for RTCPeerConnection");
			}else if(supportedImpl == WEBRTC_SUPPORT_MOZ) {
				logger.info("Using moz- prefix for RTCPeerConnection");
			}else if(supportedImpl == WEBRTC_SUPPORT_CORE_NON_PROMISING) {
				logger.info("Using non-promising RTCPeerConnection");
			}
			if(supportedImpl != WEBRTC_SUPPORT_NONE) {
				belowChrome71Fix = isChromeBelow71();
				if(belowChrome71Fix) {
					logger.info("Note: Detected Chrome below version 71, stripping \"a=extmap-allow-mixed\" from the description SDP field");
				}
			}else {
				belowChrome71Fix = false;
			}
		}
		return supportedImpl != WEBRTC_SUPPORT_NONE;
	}

	@JSBody(params = { "item" }, script = "item.close();")
	static native void closeIt(JSObject item);

	@JSBody(params = { "item" }, script = "return item.readyState;")
	static native String getReadyState(JSObject item);

	@JSBody(params = { "item", "buffer" }, script = "item.send(buffer);")
	static native void sendIt(JSObject item, ArrayBuffer buffer);

	@JSBody(params = { "item" }, script = "return !!item.candidate;")
	static native boolean hasCandidate(JSObject item);

	@JSBody(params = { "item" }, script = "return item.connectionState || \"\";")
	private static native String getModernConnectionState(JSObject item);

	@JSBody(params = { "item" }, script = "return item.iceConnectionState;")
	private static native String getICEConnectionState(JSObject item);

	@JSBody(params = { "item" }, script = "return item.signalingState;")
	private static native String getSignalingState(JSObject item);

	static String getConnectionState(JSObject item) {
		if(useOldConnStateCheck) {
			return getConnectionStateLegacy(item);
		}else {
			String str = getModernConnectionState(item);
			if(str.length() == 0) {
				useOldConnStateCheck = true;
				logger.info("Note: Using legacy connection state check using iceConnectionState+signalingState");
				return getConnectionStateLegacy(item);
			}else {
				return str;
			}
		}
	}

	private static String getConnectionStateLegacy(JSObject item) {
		String connState = getICEConnectionState(item);
		switch(connState) {
		case "new":
			return "new";
		case "checking":
			return "connecting";
		case "failed":
			return "failed";
		case "disconnected":
			return "disconnected";
		case "connected":
		case "completed":
		case "closed":
			String signalState = getSignalingState(item);
			switch(signalState) {
			case "stable":
				return "connected";
			case "have-local-offer":
			case "have-remote-offer":
			case "have-local-pranswer":
			case "have-remote-pranswer":
				return "connecting";
			case "closed":
			default:
				return "closed";
			}
		default:
			return "closed";
		}
	}

	@JSBody(params = { "item" }, script = "return item.candidate.sdpMLineIndex;")
	static native int getSdpMLineIndex(JSObject item);

	@JSBody(params = { "item" }, script = "return item.candidate.candidate;")
	static native String getCandidate(JSObject item);

	static JSObject createRTCPeerConnection(String iceServers) {
		if(!hasCheckedSupport) supported();
		switch(supportedImpl) {
		case WEBRTC_SUPPORT_CORE:
		case WEBRTC_SUPPORT_CORE_NON_PROMISING:
			return createCoreRTCPeerConnection(iceServers);
		case WEBRTC_SUPPORT_WEBKIT:
			return createWebkitRTCPeerConnection(iceServers);
		case WEBRTC_SUPPORT_MOZ:
			return createMozRTCPeerConnection(iceServers);
		default:
			throw new UnsupportedOperationException();
		}
	}

	@JSBody(params = { "iceServers" }, script = "return new RTCPeerConnection({ iceServers: JSON.parse(iceServers), optional: [ { DtlsSrtpKeyAgreement: true } ] });")
	static native JSObject createCoreRTCPeerConnection(String iceServers);

	@JSBody(params = { "iceServers" }, script = "return new webkitRTCPeerConnection({ iceServers: JSON.parse(iceServers), optional: [ { DtlsSrtpKeyAgreement: true } ] });")
	static native JSObject createWebkitRTCPeerConnection(String iceServers);

	@JSBody(params = { "iceServers" }, script = "return new mozRTCPeerConnection({ iceServers: JSON.parse(iceServers), optional: [ { DtlsSrtpKeyAgreement: true } ] });")
	static native JSObject createMozRTCPeerConnection(String iceServers);

	@JSBody(params = { "peerConnection", "name" }, script = "return peerConnection.createDataChannel(name);")
	static native JSObject createDataChannel(JSObject peerConnection, String name);

	@JSBody(params = { "item", "type" }, script = "item.binaryType = type;")
	static native void setBinaryType(JSObject item, String type);

	@JSBody(params = { "item" }, script = "return item.data;")
	static native ArrayBuffer getData(JSObject item);

	@JSBody(params = { "item" }, script = "return item.channel;")
	static native JSObject getChannel(JSObject item);

	@JSBody(params = { "peerConnection", "h1", "h2" }, script = "peerConnection.createOffer().then(h1).catch(h2);")
	private static native void createOfferPromising(JSObject peerConnection, DescHandler h1, ErrorHandler h2);

	@JSBody(params = { "peerConnection", "h1", "h2" }, script = "peerConnection.createOffer(h1, h2);")
	private static native void createOfferLegacy(JSObject peerConnection, DescHandler h1, ErrorHandler h2);

	static void createOffer(JSObject peerConnection, DescHandler h1, ErrorHandler h2) {
		if(!hasCheckedSupport) supported();
		switch(supportedImpl) {
		case WEBRTC_SUPPORT_CORE:
			createOfferPromising(peerConnection, h1, h2);
			break;
		case WEBRTC_SUPPORT_WEBKIT:
		case WEBRTC_SUPPORT_MOZ:
		case WEBRTC_SUPPORT_CORE_NON_PROMISING:
			createOfferLegacy(peerConnection, h1, h2);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	@JSBody(params = { "peerConnection", "desc", "h1", "h2" }, script = "peerConnection.setLocalDescription(desc).then(h1).catch(h2);")
	private static native void setLocalDescriptionPromising(JSObject peerConnection, JSObject desc, EmptyHandler h1, ErrorHandler h2);

	@JSBody(params = { "peerConnection", "desc", "h1", "h2" }, script = "peerConnection.setLocalDescription(desc, h1, h2);")
	private static native void setLocalDescriptionLegacy(JSObject peerConnection, JSObject desc, EmptyHandler h1, ErrorHandler h2);

	static void setLocalDescription(JSObject peerConnection, JSObject desc, EmptyHandler h1, ErrorHandler h2) {
		if(!hasCheckedSupport) supported();
		switch(supportedImpl) {
		case WEBRTC_SUPPORT_CORE:
			setLocalDescriptionPromising(peerConnection, desc, h1, h2);
			break;
		case WEBRTC_SUPPORT_WEBKIT:
		case WEBRTC_SUPPORT_MOZ:
		case WEBRTC_SUPPORT_CORE_NON_PROMISING:
			setLocalDescriptionLegacy(peerConnection, desc, h1, h2);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	@JSBody(params = { "peerConnection", "str" }, script = "var candidateList = JSON.parse(str); for (var i = 0; i < candidateList.length; ++i) { peerConnection.addIceCandidate(new RTCIceCandidate(candidateList[i])); }; return null;")
	private static native void addCoreIceCandidates(JSObject peerConnection, String str);

	@JSBody(params = { "peerConnection", "str" }, script = "var candidateList = JSON.parse(str); for (var i = 0; i < candidateList.length; ++i) { peerConnection.addIceCandidate(new mozRTCIceCandidate(candidateList[i])); }; return null;")
	private static native void addMozIceCandidates(JSObject peerConnection, String str);

	@JSBody(params = { }, script = "if(!navigator || !navigator.userAgent) return false;"
			+ "var ua = navigator.userAgent.toLowerCase();"
			+ "var i = ua.indexOf(\"chrome/\");"
			+ "if(i === -1) return false;"
			+ "i += 7;"
			+ "var j = ua.indexOf(\".\", i);"
			+ "if(j === -1 || j < i) j = ua.length;"
			+ "var versStr = ua.substring(i, j);"
			+ "versStr = parseInt(versStr);"
			+ "return !isNaN(versStr) && versStr < 71;")
	private static native boolean isChromeBelow71();

	static void addIceCandidates(JSObject peerConnection, String str) {
		if(!hasCheckedSupport) supported();
		switch(supportedImpl) {
		case WEBRTC_SUPPORT_CORE:
		case WEBRTC_SUPPORT_CORE_NON_PROMISING:
		case WEBRTC_SUPPORT_WEBKIT:
			addCoreIceCandidates(peerConnection, str);
			break;
		case WEBRTC_SUPPORT_MOZ:
			addMozIceCandidates(peerConnection, str);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	@JSBody(params = { "peerConnection", "str" }, script = "try { peerConnection.setRemoteDescription(str); return true; } catch(ex) { if(ex.name === \"TypeError\") return false; else throw ex; }")
	private static native boolean setCoreRemoteDescription(JSObject peerConnection, JSObject str);

	@JSBody(params = { "peerConnection", "str" }, script = "peerConnection.setRemoteDescription(new RTCSessionDescription(str));")
	private static native void setCoreRemoteDescriptionLegacy(JSObject peerConnection, JSObject str);

	@JSBody(params = { "peerConnection", "str" }, script = "peerConnection.setRemoteDescription(new mozRTCSessionDescription(str));")
	private static native void setMozRemoteDescriptionLegacy(JSObject peerConnection, JSObject str);

	static void setRemoteDescription(JSObject peerConnection, JSObject str) {
		if(!hasCheckedSupport) supported();
		if(belowChrome71Fix) {
			removeExtmapAllowMixed(str);
		}
		switch(supportedImpl) {
		case WEBRTC_SUPPORT_CORE:
		case WEBRTC_SUPPORT_CORE_NON_PROMISING:
			if(useSessionDescConstructor) {
				setCoreRemoteDescriptionLegacy(peerConnection, str);
			}else {
				if(!setCoreRemoteDescription(peerConnection, str)) {
					useSessionDescConstructor = true;
					logger.info("Note: Caught suspicious exception, using legacy RTCSessionDescription method");
					setCoreRemoteDescriptionLegacy(peerConnection, str);
				}
			}
			break;
		case WEBRTC_SUPPORT_WEBKIT:
			setCoreRemoteDescriptionLegacy(peerConnection, str);
			break;
		case WEBRTC_SUPPORT_MOZ:
			setMozRemoteDescriptionLegacy(peerConnection, str);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	@JSBody(params = { "peerConnection", "str", "h1", "h2" }, script = "try { peerConnection.setRemoteDescription(str).then(h1).catch(h2); return true; } catch(ex) { if(ex.name === \"TypeError\") return false; else throw ex; }")
	private static native boolean setCoreRemoteDescription2Promising(JSObject peerConnection, JSObject str, EmptyHandler h1, ErrorHandler h2);

	@JSBody(params = { "peerConnection", "str", "h1", "h2" }, script = "try { peerConnection.setRemoteDescription(str, h1, h2); return true; } catch(ex) { if(ex.name === \"TypeError\") return false; else throw ex; }")
	private static native boolean setCoreRemoteDescription2Legacy(JSObject peerConnection, JSObject str, EmptyHandler h1, ErrorHandler h2);

	@JSBody(params = { "peerConnection", "str", "h1", "h2" }, script = "peerConnection.setRemoteDescription(new RTCSessionDescription(str)).then(h1).catch(h2);")
	private static native void setCoreRemoteDescription2PromisingLegacy(JSObject peerConnection, JSObject str, EmptyHandler h1, ErrorHandler h2);

	@JSBody(params = { "peerConnection", "str", "h1", "h2" }, script = "peerConnection.setRemoteDescription(new RTCSessionDescription(str), h1, h2);")
	private static native void setCoreRemoteDescription2LegacyLegacy(JSObject peerConnection, JSObject str, EmptyHandler h1, ErrorHandler h2);

	@JSBody(params = { "peerConnection", "str", "h1", "h2" }, script = "peerConnection.setRemoteDescription(new mozRTCSessionDescription(str), h1, h2);")
	private static native void setMozRemoteDescription2LegacyLegacy(JSObject peerConnection, JSObject str, EmptyHandler h1, ErrorHandler h2);

	static void setRemoteDescription2(JSObject peerConnection, JSObject str, EmptyHandler h1, ErrorHandler h2) {
		if(!hasCheckedSupport) supported();
		if(belowChrome71Fix) {
			removeExtmapAllowMixed(str);
		}
		switch(supportedImpl) {
		case WEBRTC_SUPPORT_CORE:
			if(useSessionDescConstructor) {
				setCoreRemoteDescription2PromisingLegacy(peerConnection, str, h1, h2);
			}else {
				if(!setCoreRemoteDescription2Promising(peerConnection, str, h1, h2)) {
					useSessionDescConstructor = true;
					logger.info("Note: Caught suspicious exception, using legacy RTCSessionDescription method");
					setCoreRemoteDescription2PromisingLegacy(peerConnection, str, h1, h2);
				}
			}
			break;
		case WEBRTC_SUPPORT_WEBKIT:
			setCoreRemoteDescription2LegacyLegacy(peerConnection, str, h1, h2);
			break;
		case WEBRTC_SUPPORT_MOZ:
			setMozRemoteDescription2LegacyLegacy(peerConnection, str, h1, h2);
			break;
		case WEBRTC_SUPPORT_CORE_NON_PROMISING:
			if(useSessionDescConstructor) {
				setCoreRemoteDescription2LegacyLegacy(peerConnection, str, h1, h2);
			}else {
				if(!setCoreRemoteDescription2Legacy(peerConnection, str, h1, h2)) {
					useSessionDescConstructor = true;
					logger.info("Note: Caught suspicious exception, using legacy RTCSessionDescription method");
					setCoreRemoteDescription2LegacyLegacy(peerConnection, str, h1, h2);
				}
			}
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	@JSBody(params = { "objIn" }, script = "if(typeof objIn.sdp === \"string\""
			+ "&& objIn.sdp.indexOf(\"a=extmap-allow-mixed\") !== -1) {"
			+ "objIn.sdp = objIn.sdp.split(\"\\n\").filter(function(line) {"
			+ "return line.trim() !== \"a=extmap-allow-mixed\";"
			+ "}).join(\"\\n\");"
			+ "}")
	private static native void removeExtmapAllowMixed(JSObject objIn);

	@JSBody(params = { "peerConnection", "h1", "h2" }, script = "peerConnection.createAnswer().then(h1).catch(h2);")
	private static native void createAnswerPromising(JSObject peerConnection, DescHandler h1, ErrorHandler h2);

	@JSBody(params = { "peerConnection", "h1", "h2" }, script = "peerConnection.createAnswer(h1, h2);")
	private static native void createAnswerLegacy(JSObject peerConnection, DescHandler h1, ErrorHandler h2);

	static void createAnswer(JSObject peerConnection, DescHandler h1, ErrorHandler h2) {
		if(!hasCheckedSupport) supported();
		switch(supportedImpl) {
		case WEBRTC_SUPPORT_CORE:
			createAnswerPromising(peerConnection, h1, h2);
			break;
		case WEBRTC_SUPPORT_WEBKIT:
		case WEBRTC_SUPPORT_MOZ:
		case WEBRTC_SUPPORT_CORE_NON_PROMISING:
			createAnswerLegacy(peerConnection, h1, h2);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	@JSBody(params = { "sock", "buffer" }, script = "sock.send(buffer);")
	static native void nativeBinarySend(WebSocket sock, ArrayBuffer buffer);

	public static void runScheduledTasks() {
		
	}

	public static class LANClient {
		public static final byte READYSTATE_INIT_FAILED = -2;
		public static final byte READYSTATE_FAILED = -1;
		public static final byte READYSTATE_DISCONNECTED = 0;
		public static final byte READYSTATE_CONNECTING = 1;
		public static final byte READYSTATE_CONNECTED = 2;

		public Set<Map<String, String>> iceServers = new HashSet<>();
		public JSObject peerConnection = null;
		public JSObject dataChannel = null;

		public byte readyState = READYSTATE_CONNECTING;

		public void initialize() {
			try {
				if (dataChannel != null) {
					try {
						closeIt(dataChannel);
					} catch (Throwable t) {
					}
					dataChannel = null;
				}
				if (peerConnection != null) {
					try {
						closeIt(peerConnection);
					} catch (Throwable t) {
					}
				}
				this.peerConnection = createRTCPeerConnection(JSONWriter.valueToString(iceServers));
				this.readyState = READYSTATE_CONNECTING;
			} catch (Throwable t) {
				readyState = READYSTATE_INIT_FAILED;
			}
		}

		public void setIceServers(String[] urls) {
			iceServers.clear();
			for (String url : urls) {
				String[] etr = url.split(";");
				if (etr.length == 1) {
					Map<String, String> m = new HashMap<>();
					m.put("urls", etr[0]);
					iceServers.add(m);
				} else if (etr.length == 3) {
					Map<String, String> m = new HashMap<>();
					m.put("urls", etr[0]);
					m.put("username", etr[1]);
					m.put("credential", etr[2]);
					iceServers.add(m);
				}
			}
		}

		public void sendPacketToServer(ArrayBuffer buffer) {
			if (dataChannel != null && "open".equals(getReadyState(dataChannel))) {
				try {
					sendIt(dataChannel, buffer);
				} catch (Throwable e) {
					signalRemoteDisconnect(false);
				}
			}else {
				signalRemoteDisconnect(false);
			}
		}

		public void signalRemoteConnect() {
			List<Map<String, String>> iceCandidates = new ArrayList<>();

			TeaVMUtils.addEventListener(peerConnection, "icecandidate", (EventListener<Event>) evt -> {
				if (hasCandidate(evt)) {
					if (iceCandidates.isEmpty()) {
						final int[] candidateState = new int[2];
						final TimerHandler[] runnable = new TimerHandler[1];
						Window.setTimeout(runnable[0] = () -> {
							if (peerConnection != null && !"disconnected".equals(getConnectionState(peerConnection))) {
								int trial = ++candidateState[1];
								if(candidateState[0] != iceCandidates.size() && trial < 3) {
									candidateState[0] = iceCandidates.size();
									Window.setTimeout(runnable[0], 2000);
									return;
								}
								clientICECandidate = JSONWriter.valueToString(iceCandidates);
								iceCandidates.clear();
							}
						}, 2000);
					}
					Map<String, String> m = new HashMap<>();
					m.put("sdpMLineIndex", "" + getSdpMLineIndex(evt));
					m.put("candidate", getCandidate(evt));
					iceCandidates.add(m);
				}
			});

			dataChannel = createDataChannel(peerConnection, "lan");
			setBinaryType(dataChannel, "arraybuffer");

			final Object[] evtHandler = new Object[1];
			evtHandler[0] = (EventListener<Event>) evt -> {
				if (!iceCandidates.isEmpty()) {
					Window.setTimeout(() -> ((EventListener<Event>)evtHandler[0]).handleEvent(evt), 10);
					return;
				}
				clientDataChannelClosed = false;
				clientDataChannelOpen = true;
			};

			TeaVMUtils.addEventListener(dataChannel, "open", (EventListener<Event>)evtHandler[0]);

			TeaVMUtils.addEventListener(dataChannel, "message", (EventListener<Event>) evt -> {
				synchronized(clientLANPacketBuffer) {
					clientLANPacketBuffer.add(TeaVMUtils.wrapByteArrayBuffer(getData(evt)));
				}
			});

			createOffer(peerConnection, desc -> {
				setLocalDescription(peerConnection, desc, () -> {
					clientDescription = JSON.stringify(desc);
				}, err -> {
					logger.error("Failed to set local description! {}", err.getMessage());
					readyState = READYSTATE_FAILED;
					signalRemoteDisconnect(false);
				});
			}, err -> {
				logger.error("Failed to set create offer! {}", err.getMessage());
				readyState = READYSTATE_FAILED;
				signalRemoteDisconnect(false);
			});

			TeaVMUtils.addEventListener(peerConnection, "connectionstatechange", (EventListener<Event>) evt -> {
				String connectionState = getConnectionState(peerConnection);
				if ("disconnected".equals(connectionState)) {
					signalRemoteDisconnect(false);
				} else if ("connected".equals(connectionState)) {
					readyState = READYSTATE_CONNECTED;
				} else if ("failed".equals(connectionState)) {
					readyState = READYSTATE_FAILED;
					signalRemoteDisconnect(false);
				}
			});
		}

		public void signalRemoteDescription(String json) {
			try {
				setRemoteDescription(peerConnection, JSON.parse(json));
			} catch (Throwable t) {
				EagRuntime.debugPrintStackTrace(t);
				readyState = READYSTATE_FAILED;
				signalRemoteDisconnect(false);
			}
		}

		public void signalRemoteICECandidate(String candidates) {
			try {
				addIceCandidates(peerConnection, candidates);
			} catch (Throwable t) {
				EagRuntime.debugPrintStackTrace(t);
				readyState = READYSTATE_FAILED;
				signalRemoteDisconnect(false);
			}
		}

		public void signalRemoteDisconnect(boolean quiet) {
			if (dataChannel != null) {
				closeIt(dataChannel);
				dataChannel = null;
			}
			if (peerConnection != null) {
				closeIt(peerConnection);
			}
			if (!quiet) clientDataChannelClosed = true;
			readyState = READYSTATE_DISCONNECTED;
		}
	}

	public static class LANPeer {
		public LANServer client;
		public String peerId;
		public JSObject peerConnection;
		public JSObject dataChannel;
		public String ipcChannel;

		public LANPeer(LANServer client, String peerId, JSObject peerConnection) {
			this.client = client;
			this.peerId = peerId;
			this.peerConnection = peerConnection;

			List<Map<String, String>> iceCandidates = new ArrayList<>();

			TeaVMUtils.addEventListener(peerConnection, "icecandidate", (EventListener<Event>) evt -> {
				if (hasCandidate(evt)) {
					if (iceCandidates.isEmpty()) {
						final int[] candidateState = new int[2];
						final TimerHandler[] runnable = new TimerHandler[1];
						Window.setTimeout(runnable[0] = () -> {
							if (peerConnection != null && !"disconnected".equals(getConnectionState(peerConnection))) {
								int trial = ++candidateState[1];
								if(candidateState[0] != iceCandidates.size() && trial < 3) {
									candidateState[0] = iceCandidates.size();
									Window.setTimeout(runnable[0], 2000);
									return;
								}
								LANPeerEvent.LANPeerICECandidateEvent e = new LANPeerEvent.LANPeerICECandidateEvent(peerId, JSONWriter.valueToString(iceCandidates));
								synchronized(serverLANEventBuffer) {
									serverLANEventBuffer.put(peerId, e);
								}
								iceCandidates.clear();
							}
						}, 2000);
					}
					Map<String, String> m = new HashMap<>();
					m.put("sdpMLineIndex", "" + getSdpMLineIndex(evt));
					m.put("candidate", getCandidate(evt));
					iceCandidates.add(m);
				}
			});

			final Object[] evtHandler = new Object[1];
			evtHandler[0] = (EventListener<Event>) evt -> {
				if (!iceCandidates.isEmpty()) {
					Window.setTimeout(() -> ((EventListener<Event>)evtHandler[0]).handleEvent(evt), 10);
					return;
				}
				if (getChannel(evt) == null) return;
				JSObject dataChannel = getChannel(evt);
				if(this.dataChannel != null) {
					closeIt(dataChannel);
					return;
				}
				this.dataChannel = dataChannel;
				synchronized(serverLANEventBuffer) {
					serverLANEventBuffer.put(peerId, new LANPeerEvent.LANPeerDataChannelEvent(peerId));
				}
				TeaVMUtils.addEventListener(dataChannel, "message", (EventListener<Event>) evt2 -> {
					ArrayBuffer data = getData(evt2);
					if(ipcChannel != null) {
						ClientPlatformSingleplayer.sendPacketTeaVM(ipcChannel, data);
					}else {
						LANPeerEvent.LANPeerPacketEvent e = new LANPeerEvent.LANPeerPacketEvent(peerId, TeaVMUtils.wrapByteArrayBuffer(data));
						synchronized(serverLANEventBuffer) {
							serverLANEventBuffer.put(peerId, e);
						}
					}
				});
			};
			
			TeaVMUtils.addEventListener(peerConnection, "datachannel", (EventListener<Event>)evtHandler[0]);

			TeaVMUtils.addEventListener(peerConnection, "connectionstatechange", (EventListener<Event>) evt -> {
				String connectionState = getConnectionState(peerConnection);
				if ("disconnected".equals(connectionState) || "failed".equals(connectionState)) {
					client.signalRemoteDisconnect(peerId);
				}
			});
		}

		public void disconnect() {
			if(dataChannel != null) {
				closeIt(dataChannel);
			}
			closeIt(peerConnection);
		}

		public void setRemoteDescription(String descJSON) {
			try {
				JSONObject remoteDesc = new JSONObject(descJSON);
				setRemoteDescription2(peerConnection, JSON.parse(descJSON), () -> {
					if (remoteDesc.has("type") && "offer".equals(remoteDesc.getString("type"))) {
						createAnswer(peerConnection, desc -> {
							setLocalDescription(peerConnection, desc, () -> {
								LANPeerEvent.LANPeerDescriptionEvent e = new LANPeerEvent.LANPeerDescriptionEvent(peerId, JSON.stringify(desc));
								synchronized(serverLANEventBuffer) {
									serverLANEventBuffer.put(peerId, e);
								}
							}, err -> {
								logger.error("Failed to set local description for \"{}\"! {}", peerId, TeaVMUtils.safeErrorMsgToString(err));
								client.signalRemoteDisconnect(peerId);
							});
						}, err -> {
							logger.error("Failed to create answer for \"{}\"! {}", peerId, TeaVMUtils.safeErrorMsgToString(err));
							client.signalRemoteDisconnect(peerId);
						});
					}
				}, err -> {
					logger.error("Failed to set remote description for \"{}\"! {}", peerId, TeaVMUtils.safeErrorMsgToString(err));
					client.signalRemoteDisconnect(peerId);
				});
			} catch (Throwable err) {
				logger.error("Failed to parse remote description for \"{}\"! {}", peerId, err.getMessage());
				logger.error(err);
				client.signalRemoteDisconnect(peerId);
			}
		}

		public void addICECandidate(String candidates) {
			try {
				addIceCandidates(peerConnection, candidates);
			} catch (Throwable err) {
				logger.error("Failed to parse ice candidate for \"{}\"! {}", peerId, err.getMessage());
				client.signalRemoteDisconnect(peerId);
			}
		}

		public void mapIPC(String ipcChannel) {
			if(this.ipcChannel == null) {
				if(ipcChannel != null) {
					this.ipcChannel = ipcChannel;
					this.client.ipcMapList.put(ipcChannel, this);
				}
			}else {
				if(ipcChannel == null) {
					this.client.ipcMapList.remove(this.ipcChannel);
					this.ipcChannel = null;
				}
			}
		}
	}

	public static class LANServer {
		public Set<Map<String, String>> iceServers = new HashSet<>();
		public Map<String, LANPeer> peerList = new HashMap<>();
		public Map<String, LANPeer> ipcMapList = new HashMap<>();

		public void setIceServers(String[] urls) {
			iceServers.clear();
			for (String url : urls) {
				String[] etr = url.split(";");
				if (etr.length == 1) {
					Map<String, String> m = new HashMap<>();
					m.put("urls", etr[0]);
					iceServers.add(m);
				} else if (etr.length == 3) {
					Map<String, String> m = new HashMap<>();
					m.put("urls", etr[0]);
					m.put("username", etr[1]);
					m.put("credential", etr[2]);
					iceServers.add(m);
				}
			}
		}

		public void sendPacketToRemoteClient(String peerId, ArrayBuffer buffer) {
			LANPeer thePeer = this.peerList.get(peerId);
			if (thePeer != null) {
				sendPacketToRemoteClient(thePeer, buffer);
			}
		}

		public void sendPacketToRemoteClient(LANPeer thePeer, ArrayBuffer buffer) {
			boolean b = false;
			if (thePeer.dataChannel != null && "open".equals(getReadyState(thePeer.dataChannel))) {
				try {
					sendIt(thePeer.dataChannel, buffer);
				} catch (Throwable e) {
					b = true;
				}
			} else {
				b = true;
			}
			if(b) {
				signalRemoteDisconnect(thePeer.peerId);
			}
		}

		public void signalRemoteConnect(String peerId) {
			try {
				JSObject peerConnection = createRTCPeerConnection(JSONWriter.valueToString(iceServers));
				LANPeer peerInstance = new LANPeer(this, peerId, peerConnection);
				peerList.put(peerId, peerInstance);
			} catch (Throwable e) {
				logger.error("Failed to create peer for \"{}\"", peerId);
				logger.error(e);
				signalRemoteDisconnect(peerId);
			}
		}

		public void signalRemoteDescription(String peerId, String descJSON) {
			LANPeer thePeer = peerList.get(peerId);
			if (thePeer != null) {
				thePeer.setRemoteDescription(descJSON);
			}
		}

		public void signalRemoteICECandidate(String peerId, String candidate) {
			LANPeer thePeer = peerList.get(peerId);
			if (thePeer != null) {
				thePeer.addICECandidate(candidate);
			}
		}

		public void signalRemoteDisconnect(String peerId) {
			if (peerId == null || peerId.isEmpty()) {
				for (LANPeer thePeer : peerList.values()) {
					if (thePeer != null) {
						try {
							thePeer.disconnect();
						} catch (Throwable ignored) {}
						synchronized(serverLANEventBuffer) {
							serverLANEventBuffer.put(thePeer.peerId, new LANPeerEvent.LANPeerDisconnectEvent(thePeer.peerId));
						}
					}
				}
				peerList.clear();
				return;
			}
			LANPeer thePeer = peerList.remove(peerId);
			if(thePeer != null) {
				if(thePeer.ipcChannel != null) {
					ipcMapList.remove(thePeer.ipcChannel);
				}
				try {
					thePeer.disconnect();
				} catch (Throwable ignored) {}
				synchronized(serverLANEventBuffer) {
					serverLANEventBuffer.put(thePeer.peerId, new LANPeerEvent.LANPeerDisconnectEvent(peerId));
				}
			}
		}

		public void serverPeerMapIPC(String peer, String ipcChannel) {
			LANPeer peerr = peerList.get(peer);
			if(peerr != null) {
				peerr.mapIPC(ipcChannel);
			}
		}

		public int countPeers() {
			return peerList.size();
		}
	}

	@JSFunctor
	public interface EmptyHandler extends JSObject {
		void call();
	}

	@JSFunctor
	public interface DescHandler extends JSObject {
		void call(JSObject desc);
	}

	@JSFunctor
	public interface ErrorHandler extends JSObject {
		void call(JSError err);
	}

	private static LANClient rtcLANClient = null;

	public static void startRTCLANClient() {
		if (rtcLANClient == null) {
			rtcLANClient = new LANClient();
		}
	}

	private static final List<byte[]> clientLANPacketBuffer = new ArrayList<>();

	private static String clientICECandidate = null;
	private static String clientDescription = null;
	private static boolean clientDataChannelOpen = false;
	private static boolean clientDataChannelClosed = true;

	public static int clientLANReadyState() {
		return rtcLANClient.readyState;
	}

	public static void clientLANCloseConnection() {
		rtcLANClient.signalRemoteDisconnect(false);
	}

	public static void clientLANSendPacket(byte[] pkt) {
		rtcLANClient.sendPacketToServer(TeaVMUtils.unwrapArrayBuffer(pkt));
	}

	public static byte[] clientLANReadPacket() {
		synchronized(clientLANPacketBuffer) {
			return !clientLANPacketBuffer.isEmpty() ? clientLANPacketBuffer.remove(0) : null;
		}
	}

	public static List<byte[]> clientLANReadAllPacket() {
		synchronized(clientLANPacketBuffer) {
			if(!clientLANPacketBuffer.isEmpty()) {
				List<byte[]> ret = new ArrayList<>(clientLANPacketBuffer);
				clientLANPacketBuffer.clear();
				return ret;
			}else {
				return null;
			}
		}
	}

	public static void clientLANSetICEServersAndConnect(String[] servers) {
		rtcLANClient.setIceServers(servers);
		if(clientLANReadyState() == LANClient.READYSTATE_CONNECTED || clientLANReadyState() == LANClient.READYSTATE_CONNECTING) {
			rtcLANClient.signalRemoteDisconnect(true);
		}
		rtcLANClient.initialize();
		rtcLANClient.signalRemoteConnect();
	}

	public static void clearLANClientState() {
		clientICECandidate = null;
		clientDescription = null;
		clientDataChannelOpen = false;
		clientDataChannelClosed = true;
	}

	public static String clientLANAwaitICECandidate() {
		if(clientICECandidate != null) {
			String ret = clientICECandidate;
			clientICECandidate = null;
			return ret;
		}else {
			return null;
		}
	}

	public static String clientLANAwaitDescription() {
		if(clientDescription != null) {
			String ret = clientDescription;
			clientDescription = null;
			return ret;
		}else {
			return null;
		}
	}

	public static boolean clientLANAwaitChannel() {
		if(clientDataChannelOpen) {
			clientDataChannelOpen = false;
			return true;
		}else {
			return false;
		}
	}

	public static boolean clientLANClosed() {
		return clientDataChannelClosed;
	}

	public static void clientLANSetICECandidate(String candidate) {
		rtcLANClient.signalRemoteICECandidate(candidate);
	}

	public static void clientLANSetDescription(String description) {
		rtcLANClient.signalRemoteDescription(description);
	}

	private static LANServer rtcLANServer = null;

	public static void startRTCLANServer() {
		if (rtcLANServer == null) {
			rtcLANServer = new LANServer();
		}
	}

	private static final ListMultimap<String, LANPeerEvent> serverLANEventBuffer = LinkedListMultimap.create();

	public static void serverLANInitializeServer(String[] servers) {
		synchronized(serverLANEventBuffer) {
			serverLANEventBuffer.clear();
		}
		rtcLANServer.setIceServers(servers);
	}

	public static void serverLANCloseServer() {
		rtcLANServer.signalRemoteDisconnect("");
	}

	public static LANPeerEvent serverLANGetEvent(String clientId) {
		synchronized(serverLANEventBuffer) {
			if(!serverLANEventBuffer.isEmpty()) {
				List<LANPeerEvent> l = serverLANEventBuffer.get(clientId);
				if(!l.isEmpty()) {
					return l.remove(0);
				}
			}
			return null;
		}
	}

	public static List<LANPeerEvent> serverLANGetAllEvent(String clientId) {
		synchronized(serverLANEventBuffer) {
			if(!serverLANEventBuffer.isEmpty()) {
				List<LANPeerEvent> l = serverLANEventBuffer.removeAll(clientId);
				if(l.isEmpty()) {
					return null;
				}
				return l;
			}
			return null;
		}
	}

	public static void serverLANWritePacket(String peer, byte[] data) {
		rtcLANServer.sendPacketToRemoteClient(peer, TeaVMUtils.unwrapArrayBuffer(data));
	}

	public static void serverLANCreatePeer(String peer) {
		rtcLANServer.signalRemoteConnect(peer);
	}

	public static void serverLANPeerICECandidates(String peer, String iceCandidates) {
		rtcLANServer.signalRemoteICECandidate(peer, iceCandidates);
	}

	public static void serverLANPeerDescription(String peer, String description) {
		rtcLANServer.signalRemoteDescription(peer, description);
	}

	public static void serverLANPeerMapIPC(String peer, String ipcChannel) {
		rtcLANServer.serverPeerMapIPC(peer, ipcChannel);
	}

	public static boolean serverLANPeerPassIPC(String channelName, ArrayBuffer data) {
		if(rtcLANServer != null) {
			LANPeer peer = rtcLANServer.ipcMapList.get(channelName);
			if(peer != null) {
				rtcLANServer.sendPacketToRemoteClient(peer, data);
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}

	public static void serverLANDisconnectPeer(String peer) {
		rtcLANServer.signalRemoteDisconnect(peer);
	}

	public static int countPeers() {
		if (rtcLANServer == null) {
			return 0;
		}
		return rtcLANServer.countPeers();
	}

}