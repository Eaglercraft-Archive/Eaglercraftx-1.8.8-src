package net.lax1dude.eaglercraft.v1_8.internal;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.lan.LANPeerEvent;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayManager;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayQuery;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayServerSocket;
import net.lax1dude.eaglercraft.v1_8.sp.relay.RelayWorldsQuery;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.*;
import net.lax1dude.eaglercraft.v1_8.update.UpdateService;

import org.json.JSONObject;
import org.json.JSONWriter;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSError;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.json.JSON;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.websocket.WebSocket;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Copyright (c) 2022-2024 ayunami2000. All Rights Reserved.
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
public class PlatformWebRTC {

	private static final Logger logger = LogManager.getLogger("PlatformWebRTC");

	@JSBody(script = "return typeof window.RTCPeerConnection !== \"undefined\";")
	public static native boolean supported();

	@JSBody(params = { "item" }, script = "return item.close();")
	static native void closeIt(JSObject item);

	@JSBody(params = { "item" }, script = "return item.readyState;")
	static native String getReadyState(JSObject item);

	@JSBody(params = { "item", "buffer" }, script = "return item.send(buffer);")
	static native void sendIt(JSObject item, ArrayBuffer buffer);

	@JSBody(params = { "item" }, script = "return !!item.candidate;")
	static native boolean hasCandidate(JSObject item);

	@JSBody(params = { "item" }, script = "return item.connectionState;")
	static native String getConnectionState(JSObject item);

	@JSBody(params = { "item" }, script = "return item.candidate.sdpMLineIndex;")
	static native int getSdpMLineIndex(JSObject item);

	@JSBody(params = { "item" }, script = "return item.candidate.candidate;")
	static native String getCandidate(JSObject item);

	@JSBody(params = { "iceServers" }, script = "return new RTCPeerConnection({ iceServers: JSON.parse(iceServers), optional: [ { DtlsSrtpKeyAgreement: true } ] });")
	static native JSObject createRTCPeerConnection(String iceServers);

	@JSBody(params = { "peerConnection", "name" }, script = "return peerConnection.createDataChannel(name);")
	static native JSObject createDataChannel(JSObject peerConnection, String name);

	@JSBody(params = { "item", "type" }, script = "return (item.binaryType = type);")
	static native void setBinaryType(JSObject item, String type);

	@JSBody(params = { "item" }, script = "return item.data;")
	static native ArrayBuffer getData(JSObject item);

	@JSBody(params = { "item" }, script = "return item.channel;")
	static native JSObject getChannel(JSObject item);

	@JSBody(params = { "peerConnection", "h1", "h2" }, script = "return peerConnection.createOffer(h1, h2);")
	static native void createOffer(JSObject peerConnection, DescHandler h1, ErrorHandler h2);

	@JSBody(params = { "peerConnection", "desc", "h1", "h2" }, script = "return peerConnection.setLocalDescription(desc, h1, h2);")
	static native void setLocalDescription(JSObject peerConnection, JSObject desc, EmptyHandler h1, ErrorHandler h2);

	@JSBody(params = { "peerConnection", "str" }, script = "return peerConnection.setRemoteDescription(JSON.parse(str));")
	static native void setRemoteDescription(JSObject peerConnection, String str);

	@JSBody(params = { "peerConnection", "str" }, script = "const candidateList = JSON.parse(str); for (let i = 0; i < candidateList.length; ++i) { peerConnection.addIceCandidate(candidateList[i]); }; return null;")
	static native void addIceCandidates(JSObject peerConnection, String str);

	@JSBody(params = { "peerConnection", "str" }, script = "const candidateList = JSON.parse(str); for (let i = 0; i < candidateList.length; ++i) { peerConnection.addIceCandidate(new RTCIceCandidate(candidateList[i])); }; return null;")
	static native void addIceCandidates2(JSObject peerConnection, String str);

	@JSBody(params = { "peerConnection", "str", "h1", "h2" }, script = "return peerConnection.setRemoteDescription(JSON.parse(str), h1, h2);")
	static native void setRemoteDescription2(JSObject peerConnection, String str, EmptyHandler h1, ErrorHandler h2);

	@JSBody(params = { "peerConnection", "h1", "h2" }, script = "return peerConnection.createAnswer(h1, h2);")
	static native void createAnswer(JSObject peerConnection, DescHandler h1, ErrorHandler h2);

	private static final Map<String, JSObject> fuckTeaVM = new HashMap<>();

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
					closeIt(dataChannel);
					dataChannel = null;
				}
				if (peerConnection != null) {
					closeIt(peerConnection);
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
						Window.setTimeout(() -> {
							if (peerConnection != null && !"disconnected".equals(getConnectionState(peerConnection))) {
								clientICECandidate = JSONWriter.valueToString(iceCandidates);
								iceCandidates.clear();
							}
						}, 3000);
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
					Window.setTimeout(() -> ((EventListener<Event>)evtHandler[0]).handleEvent(evt), 1);
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
				setRemoteDescription(peerConnection, json);
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

	public static final byte PEERSTATE_FAILED = 0;
	public static final byte PEERSTATE_SUCCESS = 1;
	public static final byte PEERSTATE_LOADING = 2;

	public static class LANPeer {
		public LANServer client;
		public String peerId;
		public JSObject peerConnection;

		public LANPeer(LANServer client, String peerId, JSObject peerConnection) {
			this.client = client;
			this.peerId = peerId;
			this.peerConnection = peerConnection;

			List<Map<String, String>> iceCandidates = new ArrayList<>();

			TeaVMUtils.addEventListener(peerConnection, "icecandidate", (EventListener<Event>) evt -> {
				if (hasCandidate(evt)) {
					if (iceCandidates.isEmpty()) {
						Window.setTimeout(() -> {
							if (peerConnection != null && !"disconnected".equals(getConnectionState(peerConnection))) {
								LANPeerEvent.LANPeerICECandidateEvent e = new LANPeerEvent.LANPeerICECandidateEvent(peerId, JSONWriter.valueToString(iceCandidates));
								synchronized(serverLANEventBuffer) {
									serverLANEventBuffer.put(peerId, e);
								}
								iceCandidates.clear();
							}
						}, 3000);
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
					Window.setTimeout(() -> ((EventListener<Event>)evtHandler[0]).handleEvent(evt), 1);
					return;
				}
				if (getChannel(evt) == null) return;
				JSObject dataChannel = getChannel(evt);
				synchronized(fuckTeaVM) {
					fuckTeaVM.put(peerId, dataChannel);
				}
				synchronized(serverLANEventBuffer) {
					serverLANEventBuffer.put(peerId, new LANPeerEvent.LANPeerDataChannelEvent(peerId));
				}
				TeaVMUtils.addEventListener(dataChannel, "message", (EventListener<Event>) evt2 -> {
					LANPeerEvent.LANPeerPacketEvent e = new LANPeerEvent.LANPeerPacketEvent(peerId, TeaVMUtils.wrapByteArrayBuffer(getData(evt2)));
					synchronized(serverLANEventBuffer) {
						serverLANEventBuffer.put(peerId, e);
					}
				});
			};
			
			TeaVMUtils.addEventListener(peerConnection, "datachannel", (EventListener<Event>)evtHandler[0]);

			TeaVMUtils.addEventListener(peerConnection, "connectionstatechange", (EventListener<Event>) evt -> {
				String connectionState = getConnectionState(peerConnection);
				if ("disconnected".equals(connectionState)) {
					client.signalRemoteDisconnect(peerId);
				} else if ("connected".equals(connectionState)) {
					if (client.peerState != PEERSTATE_SUCCESS) client.peerState = PEERSTATE_SUCCESS;
				} else if ("failed".equals(connectionState)) {
					if (client.peerState == PEERSTATE_LOADING) client.peerState = PEERSTATE_FAILED;
					client.signalRemoteDisconnect(peerId);
				}
			});
		}

		public void disconnect() {
			synchronized(fuckTeaVM) {
				if (fuckTeaVM.get(peerId) != null) {
					closeIt(fuckTeaVM.get(peerId));
					fuckTeaVM.remove(peerId);
				}
			}
			closeIt(peerConnection);
		}

		public void setRemoteDescription(String descJSON) {
			try {
				JSONObject remoteDesc = new JSONObject(descJSON);
				setRemoteDescription2(peerConnection, descJSON, () -> {
					if (remoteDesc.has("type") && "offer".equals(remoteDesc.getString("type"))) {
						createAnswer(peerConnection, desc -> {
							setLocalDescription(peerConnection, desc, () -> {
								LANPeerEvent.LANPeerDescriptionEvent e = new LANPeerEvent.LANPeerDescriptionEvent(peerId, JSON.stringify(desc));
								synchronized(serverLANEventBuffer) {
									serverLANEventBuffer.put(peerId, e);
								}
								if (client.peerStateDesc != PEERSTATE_SUCCESS) client.peerStateDesc = PEERSTATE_SUCCESS;
							}, err -> {
								logger.error("Failed to set local description for \"{}\"! {}", peerId, err.getMessage());
								if (client.peerStateDesc == PEERSTATE_LOADING) client.peerStateDesc = PEERSTATE_FAILED;
								client.signalRemoteDisconnect(peerId);
							});
						}, err -> {
							logger.error("Failed to create answer for \"{}\"! {}", peerId, err.getMessage());
							if (client.peerStateDesc == PEERSTATE_LOADING) client.peerStateDesc = PEERSTATE_FAILED;
							client.signalRemoteDisconnect(peerId);
						});
					}
				}, err -> {
					logger.error("Failed to set remote description for \"{}\"! {}", peerId, err.getMessage());
					if (client.peerStateDesc == PEERSTATE_LOADING) client.peerStateDesc = PEERSTATE_FAILED;
					client.signalRemoteDisconnect(peerId);
				});
			} catch (Throwable err) {
				logger.error("Failed to parse remote description for \"{}\"! {}", peerId, err.getMessage());
				if (client.peerStateDesc == PEERSTATE_LOADING) client.peerStateDesc = PEERSTATE_FAILED;
				client.signalRemoteDisconnect(peerId);
			}
		}

		public void addICECandidate(String candidates) {
			try {
				addIceCandidates2(peerConnection, candidates);
				if (client.peerStateIce != PEERSTATE_SUCCESS) client.peerStateIce = PEERSTATE_SUCCESS;
			} catch (Throwable err) {
				logger.error("Failed to parse ice candidate for \"{}\"! {}", peerId, err.getMessage());
				if (client.peerStateIce == PEERSTATE_LOADING) client.peerStateIce = PEERSTATE_FAILED;
				client.signalRemoteDisconnect(peerId);
			}
		}
	}

	public static class LANServer {
		public Set<Map<String, String>> iceServers = new HashSet<>();
		public Map<String, LANPeer> peerList = new HashMap<>();
		public byte peerState = PEERSTATE_LOADING;
		public byte peerStateConnect = PEERSTATE_LOADING;
		public byte peerStateInitial = PEERSTATE_LOADING;
		public byte peerStateDesc = PEERSTATE_LOADING;
		public byte peerStateIce = PEERSTATE_LOADING;

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
				boolean b = false;
				synchronized(fuckTeaVM) {
					if (fuckTeaVM.get(thePeer.peerId) != null && "open".equals(getReadyState(fuckTeaVM.get(thePeer.peerId)))) {
						try {
							sendIt(fuckTeaVM.get(thePeer.peerId), buffer);
						} catch (Throwable e) {
							b = true;
						}
					} else {
						b = true;
					}
				}
				if(b) {
					signalRemoteDisconnect(peerId);
				}
			}
		}

		public void resetPeerStates() {
			peerState = peerStateConnect = peerStateInitial = peerStateDesc = peerStateIce = PEERSTATE_LOADING;
		}

		public void signalRemoteConnect(String peerId) {
			try {
				JSObject peerConnection = createRTCPeerConnection(JSONWriter.valueToString(iceServers));
				LANPeer peerInstance = new LANPeer(this, peerId, peerConnection);
				peerList.put(peerId, peerInstance);
				if (peerStateConnect != PEERSTATE_SUCCESS) peerStateConnect = PEERSTATE_SUCCESS;
			} catch (Throwable e) {
				if (peerStateConnect == PEERSTATE_LOADING) peerStateConnect = PEERSTATE_FAILED;
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
				synchronized(fuckTeaVM) {
					fuckTeaVM.clear();
				}
				return;
			}
			LANPeer thePeer = peerList.get(peerId);
			if(thePeer != null) {
				peerList.remove(peerId);
				try {
					thePeer.disconnect();
				} catch (Throwable ignored) {}
				synchronized(fuckTeaVM) {
					fuckTeaVM.remove(peerId);
				}
				synchronized(serverLANEventBuffer) {
					serverLANEventBuffer.put(thePeer.peerId, new LANPeerEvent.LANPeerDisconnectEvent(peerId));
				}
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

	@JSBody(params = { "obj" }, script = "return typeof obj === \"string\";")
	private static native boolean isString(JSObject obj);

	private static final Map<String,Long> relayQueryLimited = new HashMap<>();
	private static final Map<String,Long> relayQueryBlocked = new HashMap<>();

	private static class RelayQueryImpl implements RelayQuery {

		private final WebSocket sock;
		private final String uri;

		private boolean open;
		private boolean failed;

		private boolean hasRecievedAnyData = false;

		private int vers = -1;
		private String comment = "<no comment>";
		private String brand = "<no brand>";

		private long connectionOpenedAt;
		private long connectionPingStart = -1;
		private long connectionPingTimer = -1;

		private RateLimit rateLimitStatus = RateLimit.NONE;

		private VersionMismatch versError = VersionMismatch.UNKNOWN;

		private RelayQueryImpl(String uri) {
			this.uri = uri;
			WebSocket s;
			try {
				connectionOpenedAt = System.currentTimeMillis();
				s = WebSocket.create(uri);
				s.setBinaryType("arraybuffer");
				open = true;
				failed = false;
			}catch(Throwable t) {
				connectionOpenedAt = 0l;
				sock = null;
				open = false;
				failed = true;
				return;
			}
			sock = s;
			sock.onOpen(evt -> {
				try {
					connectionPingStart = System.currentTimeMillis();
					PlatformNetworking.nativeBinarySend(sock, TeaVMUtils.unwrapArrayBuffer(
							IPacket.writePacket(new IPacket00Handshake(0x03, RelayManager.preferredRelayVersion, ""))
					));
				} catch (IOException e) {
					logger.error(e.toString());
					sock.close();
					failed = true;
				}
			});
			sock.onMessage(evt -> {
				if(evt.getData() != null && !isString(evt.getData())) {
					hasRecievedAnyData = true;
					byte[] arr = TeaVMUtils.wrapByteArrayBuffer(evt.getDataAsArray());
					if(arr.length == 2 && arr[0] == (byte)0xFC) {
						long millis = System.currentTimeMillis();
						if(arr[1] == (byte)0x00 || arr[1] == (byte)0x01) {
							rateLimitStatus = RateLimit.BLOCKED;
							relayQueryLimited.put(RelayQueryImpl.this.uri, millis);
						}else if(arr[1] == (byte)0x02) {
							rateLimitStatus = RateLimit.NOW_LOCKED;
							relayQueryLimited.put(RelayQueryImpl.this.uri, millis);
							relayQueryBlocked.put(RelayQueryImpl.this.uri, millis);
						}else {
							rateLimitStatus = RateLimit.LOCKED;
							relayQueryBlocked.put(RelayQueryImpl.this.uri, millis);
						}
						failed = true;
						open = false;
						sock.close();
					}else {
						if(open) {
							try {
								IPacket pkt = IPacket.readPacket(new DataInputStream(new EaglerInputStream(arr)));
								if(pkt instanceof IPacket69Pong) {
									IPacket69Pong ipkt = (IPacket69Pong)pkt;
									versError = VersionMismatch.COMPATIBLE;
									if(connectionPingTimer == -1) {
										connectionPingTimer = System.currentTimeMillis() - connectionPingStart;
									}
									vers = ipkt.protcolVersion;
									comment = ipkt.comment;
									brand = ipkt.brand;
									open = false;
									failed = false;
									sock.close();
								}else if(pkt instanceof IPacket70SpecialUpdate) {
									IPacket70SpecialUpdate ipkt = (IPacket70SpecialUpdate)pkt;
									if(ipkt.operation == IPacket70SpecialUpdate.OPERATION_UPDATE_CERTIFICATE) {
										UpdateService.addCertificateToSet(ipkt.updatePacket);
									}
								}else if(pkt instanceof IPacketFFErrorCode) {
									IPacketFFErrorCode ipkt = (IPacketFFErrorCode)pkt;
									if(ipkt.code == IPacketFFErrorCode.TYPE_PROTOCOL_VERSION) {
										String s1 = ipkt.desc.toLowerCase();
										if(s1.contains("outdated client") || s1.contains("client outdated")) {
											versError = VersionMismatch.CLIENT_OUTDATED;
										}else if(s1.contains("outdated server") || s1.contains("server outdated") ||
												s1.contains("outdated relay") || s1.contains("server relay")) {
											versError = VersionMismatch.RELAY_OUTDATED;
										}else {
											versError = VersionMismatch.UNKNOWN;
										}
									}
									logger.error("{}\": Recieved query error code {}: {}", uri, ipkt.code, ipkt.desc);
									open = false;
									failed = true;
									sock.close();
								}else {
									throw new IOException("Unexpected packet '" + pkt.getClass().getSimpleName() + "'");
								}
							} catch (IOException e) {
								logger.error("Relay Query Error: {}", e.toString());
								EagRuntime.debugPrintStackTrace(e);
								open = false;
								failed = true;
								sock.close();
							}
						}
					}
				}
			});
			sock.onClose(evt -> {
				open = false;
				if(!hasRecievedAnyData) {
					failed = true;
					Long l = relayQueryBlocked.get(uri);
					if(l != null) {
						if(System.currentTimeMillis() - l.longValue() < 400000l) {
							rateLimitStatus = RateLimit.LOCKED;
							return;
						}
					}
					l = relayQueryLimited.get(uri);
					if(l != null) {
						if(System.currentTimeMillis() - l.longValue() < 900000l) {
							rateLimitStatus = RateLimit.BLOCKED;
							return;
						}
					}
				}
			});
		}

		@Override
		public boolean isQueryOpen() {
			return open;
		}

		@Override
		public boolean isQueryFailed() {
			return failed;
		}

		@Override
		public RateLimit isQueryRateLimit() {
			return rateLimitStatus;
		}

		@Override
		public void close() {
			if(sock != null && open) {
				sock.close();
			}
			open = false;
		}

		@Override
		public int getVersion() {
			return vers;
		}

		@Override
		public String getComment() {
			return comment;
		}

		@Override
		public String getBrand() {
			return brand;
		}

		@Override
		public long getPing() {
			return connectionPingTimer < 1 ? 1 : connectionPingTimer;
		}

		@Override
		public VersionMismatch getCompatible() {
			return versError;
		}

	}

	private static class RelayQueryRatelimitDummy implements RelayQuery {

		private final RateLimit type;

		private RelayQueryRatelimitDummy(RateLimit type) {
			this.type = type;
		}

		@Override
		public boolean isQueryOpen() {
			return false;
		}

		@Override
		public boolean isQueryFailed() {
			return true;
		}

		@Override
		public RateLimit isQueryRateLimit() {
			return type;
		}

		@Override
		public void close() {
		}

		@Override
		public int getVersion() {
			return RelayManager.preferredRelayVersion;
		}

		@Override
		public String getComment() {
			return "this query was rate limited";
		}

		@Override
		public String getBrand() {
			return "lax1dude";
		}

		@Override
		public long getPing() {
			return 0l;
		}

		@Override
		public VersionMismatch getCompatible() {
			return VersionMismatch.COMPATIBLE;
		}

	}

	public static RelayQuery openRelayQuery(String addr) {
		long millis = System.currentTimeMillis();

		Long l = relayQueryBlocked.get(addr);
		if(l != null && millis - l.longValue() < 60000l) {
			return new RelayQueryRatelimitDummy(RelayQuery.RateLimit.LOCKED);
		}

		l = relayQueryLimited.get(addr);
		if(l != null && millis - l.longValue() < 10000l) {
			return new RelayQueryRatelimitDummy(RelayQuery.RateLimit.BLOCKED);
		}

		return new RelayQueryImpl(addr);
	}

	private static class RelayWorldsQueryImpl implements RelayWorldsQuery {

		private final WebSocket sock;
		private final String uri;

		private boolean open;
		private boolean failed;

		private boolean hasRecievedAnyData = false;
		private RelayQuery.RateLimit rateLimitStatus = RelayQuery.RateLimit.NONE;

		private RelayQuery.VersionMismatch versError = RelayQuery.VersionMismatch.UNKNOWN;

		private List<IPacket07LocalWorlds.LocalWorld> worlds = null;

		private RelayWorldsQueryImpl(String uri) {
			this.uri = uri;
			WebSocket s;
			try {
				s = WebSocket.create(uri);
				s.setBinaryType("arraybuffer");
				open = true;
				failed = false;
			}catch(Throwable t) {
				sock = null;
				open = false;
				failed = true;
				return;
			}
			sock = s;
			sock.onOpen(evt -> {
				try {
					PlatformNetworking.nativeBinarySend(sock, TeaVMUtils.unwrapArrayBuffer(
							IPacket.writePacket(new IPacket00Handshake(0x04, RelayManager.preferredRelayVersion, ""))
					));
				} catch (IOException e) {
					logger.error(e.toString());
					sock.close();
					open = false;
					failed = true;
				}
			});
			sock.onMessage(evt -> {
				if(evt.getData() != null && !isString(evt.getData())) {
					hasRecievedAnyData = true;
					byte[] arr = TeaVMUtils.wrapByteArrayBuffer(evt.getDataAsArray());
					if(arr.length == 2 && arr[0] == (byte)0xFC) {
						long millis = System.currentTimeMillis();
						if(arr[1] == (byte)0x00 || arr[1] == (byte)0x01) {
							rateLimitStatus = RelayQuery.RateLimit.BLOCKED;
							relayQueryLimited.put(RelayWorldsQueryImpl.this.uri, millis);
						}else if(arr[1] == (byte)0x02) {
							rateLimitStatus = RelayQuery.RateLimit.NOW_LOCKED;
							relayQueryLimited.put(RelayWorldsQueryImpl.this.uri, millis);
							relayQueryBlocked.put(RelayWorldsQueryImpl.this.uri, millis);
						}else {
							rateLimitStatus = RelayQuery.RateLimit.LOCKED;
							relayQueryBlocked.put(RelayWorldsQueryImpl.this.uri, millis);
						}
						open = false;
						failed = true;
						sock.close();
					}else {
						if(open) {
							try {
								IPacket pkt = IPacket.readPacket(new DataInputStream(new EaglerInputStream(arr)));
								if(pkt instanceof IPacket07LocalWorlds) {
									worlds = ((IPacket07LocalWorlds)pkt).worldsList;
									sock.close();
									open = false;
									failed = false;
								}else if(pkt instanceof IPacket70SpecialUpdate) {
									IPacket70SpecialUpdate ipkt = (IPacket70SpecialUpdate)pkt;
									if(ipkt.operation == IPacket70SpecialUpdate.OPERATION_UPDATE_CERTIFICATE) {
										UpdateService.addCertificateToSet(ipkt.updatePacket);
									}
								}else if(pkt instanceof IPacketFFErrorCode) {
									IPacketFFErrorCode ipkt = (IPacketFFErrorCode)pkt;
									if(ipkt.code == IPacketFFErrorCode.TYPE_PROTOCOL_VERSION) {
										String s1 = ipkt.desc.toLowerCase();
										if(s1.contains("outdated client") || s1.contains("client outdated")) {
											versError = RelayQuery.VersionMismatch.CLIENT_OUTDATED;
										}else if(s1.contains("outdated server") || s1.contains("server outdated") ||
												s1.contains("outdated relay") || s1.contains("server relay")) {
											versError = RelayQuery.VersionMismatch.RELAY_OUTDATED;
										}else {
											versError = RelayQuery.VersionMismatch.UNKNOWN;
										}
									}
									logger.error("{}: Recieved query error code {}: {}", uri, ipkt.code, ipkt.desc);
									open = false;
									failed = true;
									sock.close();
								}else {
									throw new IOException("Unexpected packet '" + pkt.getClass().getSimpleName() + "'");
								}
							} catch (IOException e) {
								logger.error("Relay World Query Error: {}", e.toString());
								EagRuntime.debugPrintStackTrace(e);
								open = false;
								failed = true;
								sock.close();
							}
						}
					}
				}
			});
			sock.onClose(evt -> {
				open = false;
				if(!hasRecievedAnyData) {
					failed = true;
					Long l = relayQueryBlocked.get(uri);
					if(l != null) {
						if(System.currentTimeMillis() - l.longValue() < 400000l) {
							rateLimitStatus = RelayQuery.RateLimit.LOCKED;
							return;
						}
					}
					l = relayQueryLimited.get(uri);
					if(l != null) {
						if(System.currentTimeMillis() - l.longValue() < 900000l) {
							rateLimitStatus = RelayQuery.RateLimit.BLOCKED;
							return;
						}
					}
				}
			});
		}

		@Override
		public boolean isQueryOpen() {
			return open;
		}

		@Override
		public boolean isQueryFailed() {
			return failed;
		}

		@Override
		public RelayQuery.RateLimit isQueryRateLimit() {
			return rateLimitStatus;
		}

		@Override
		public void close() {
			if(open && sock != null) {
				sock.close();
			}
			open = false;
		}

		@Override
		public List<IPacket07LocalWorlds.LocalWorld> getWorlds() {
			return worlds;
		}

		@Override
		public RelayQuery.VersionMismatch getCompatible() {
			return versError;
		}

	}

	private static class RelayWorldsQueryRatelimitDummy implements RelayWorldsQuery {

		private final RelayQuery.RateLimit rateLimit;

		private RelayWorldsQueryRatelimitDummy(RelayQuery.RateLimit rateLimit) {
			this.rateLimit = rateLimit;
		}

		@Override
		public boolean isQueryOpen() {
			return false;
		}

		@Override
		public boolean isQueryFailed() {
			return true;
		}

		@Override
		public RelayQuery.RateLimit isQueryRateLimit() {
			return rateLimit;
		}

		@Override
		public void close() {
		}

		@Override
		public List<IPacket07LocalWorlds.LocalWorld> getWorlds() {
			return new ArrayList(0);
		}

		@Override
		public RelayQuery.VersionMismatch getCompatible() {
			return RelayQuery.VersionMismatch.COMPATIBLE;
		}
	}

	public static RelayWorldsQuery openRelayWorldsQuery(String addr) {
		long millis = System.currentTimeMillis();

		Long l = relayQueryBlocked.get(addr);
		if(l != null && millis - l.longValue() < 60000l) {
			return new RelayWorldsQueryRatelimitDummy(RelayQuery.RateLimit.LOCKED);
		}

		l = relayQueryLimited.get(addr);
		if(l != null && millis - l.longValue() < 10000l) {
			return new RelayWorldsQueryRatelimitDummy(RelayQuery.RateLimit.BLOCKED);
		}

		return new RelayWorldsQueryImpl(addr);
	}

	private static class RelayServerSocketImpl implements RelayServerSocket {

		private final WebSocket sock;
		private final String uri;

		private boolean open;
		private boolean closed;
		private boolean failed;

		private boolean hasRecievedAnyData;

		private final List<Throwable> exceptions = new LinkedList();
		private final List<IPacket> packets = new LinkedList();

		private RelayServerSocketImpl(String uri, int timeout) {
			this.uri = uri;
			WebSocket s;
			try {
				s = WebSocket.create(uri);
				s.setBinaryType("arraybuffer");
				open = false;
				closed = false;
				failed = false;
			}catch(Throwable t) {
				exceptions.add(t);
				sock = null;
				open = false;
				closed = true;
				failed = true;
				return;
			}
			sock = s;
			sock.onOpen(evt -> open = true);
			sock.onMessage(evt -> {
				if(evt.getData() != null && !isString(evt.getData())) {
					hasRecievedAnyData = true;
					try {
						IPacket pkt = IPacket.readPacket(new DataInputStream(new EaglerInputStream(TeaVMUtils.wrapByteArrayBuffer(evt.getDataAsArray()))));
						if(pkt instanceof IPacket70SpecialUpdate) {
							IPacket70SpecialUpdate ipkt = (IPacket70SpecialUpdate)pkt;
							if(ipkt.operation == IPacket70SpecialUpdate.OPERATION_UPDATE_CERTIFICATE) {
								UpdateService.addCertificateToSet(ipkt.updatePacket);
							}
						}else {
							packets.add(pkt);
						}
					} catch (IOException e) {
						exceptions.add(e);
						logger.error("Relay Socket Error: {}", e.toString());
						EagRuntime.debugPrintStackTrace(e);
						open = false;
						failed = true;
						closed = true;
						sock.close();
					}
				}
			});
			sock.onClose(evt -> {
				if (!hasRecievedAnyData) {
					failed = true;
				}
				open = false;
				closed = true;
			});
			Window.setTimeout(() -> {
				if(!open && !closed) {
					closed = true;
					sock.close();
				}
			}, timeout);
		}

		@Override
		public boolean isOpen() {
			return open;
		}

		@Override
		public boolean isClosed() {
			return closed;
		}

		@Override
		public void close() {
			if(open && sock != null) {
				sock.close();
			}
			open = false;
			closed = true;
		}

		@Override
		public boolean isFailed() {
			return failed;
		}

		@Override
		public Throwable getException() {
			if(!exceptions.isEmpty()) {
				return exceptions.remove(0);
			}else {
				return null;
			}
		}

		@Override
		public void writePacket(IPacket pkt) {
			try {
				PlatformNetworking.nativeBinarySend(sock, TeaVMUtils.unwrapArrayBuffer(IPacket.writePacket(pkt)));
			} catch (Throwable e) {
				logger.error("Relay connection error: {}", e.toString());
				EagRuntime.debugPrintStackTrace(e);
				exceptions.add(e);
				failed = true;
				open = false;
				closed = true;
				sock.close();
			}
		}

		@Override
		public IPacket readPacket() {
			if(!packets.isEmpty()) {
				return packets.remove(0);
			}else {
				return null;
			}
		}

		@Override
		public IPacket nextPacket() {
			if(!packets.isEmpty()) {
				return packets.get(0);
			}else {
				return null;
			}
		}

		@Override
		public RelayQuery.RateLimit getRatelimitHistory() {
			if(relayQueryBlocked.containsKey(uri)) {
				return RelayQuery.RateLimit.LOCKED;
			}
			if(relayQueryLimited.containsKey(uri)) {
				return RelayQuery.RateLimit.BLOCKED;
			}
			return RelayQuery.RateLimit.NONE;
		}

		@Override
		public String getURI() {
			return uri;
		}

	}

	private static class RelayServerSocketRatelimitDummy implements RelayServerSocket {

		private final RelayQuery.RateLimit limit;

		private RelayServerSocketRatelimitDummy(RelayQuery.RateLimit limit) {
			this.limit = limit;
		}

		@Override
		public boolean isOpen() {
			return false;
		}

		@Override
		public boolean isClosed() {
			return true;
		}

		@Override
		public void close() {
		}

		@Override
		public boolean isFailed() {
			return true;
		}

		@Override
		public Throwable getException() {
			return null;
		}

		@Override
		public void writePacket(IPacket pkt) {
		}

		@Override
		public IPacket readPacket() {
			return null;
		}

		@Override
		public IPacket nextPacket() {
			return null;
		}

		@Override
		public RelayQuery.RateLimit getRatelimitHistory() {
			return limit;
		}

		@Override
		public String getURI() {
			return "<disconnected>";
		}

	}

	public static RelayServerSocket openRelayConnection(String addr, int timeout) {
		long millis = System.currentTimeMillis();

		Long l = relayQueryBlocked.get(addr);
		if(l != null && millis - l.longValue() < 60000l) {
			return new RelayServerSocketRatelimitDummy(RelayQuery.RateLimit.LOCKED);
		}

		l = relayQueryLimited.get(addr);
		if(l != null && millis - l.longValue() < 10000l) {
			return new RelayServerSocketRatelimitDummy(RelayQuery.RateLimit.BLOCKED);
		}

		return new RelayServerSocketImpl(addr, timeout);
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

	// todo: ArrayBuffer version
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
				List<byte[]> ret = new ArrayList(clientLANPacketBuffer);
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
		rtcLANServer.resetPeerStates();
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
