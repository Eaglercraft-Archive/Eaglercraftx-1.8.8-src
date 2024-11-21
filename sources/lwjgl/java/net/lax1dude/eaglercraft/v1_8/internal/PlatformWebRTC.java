package net.lax1dude.eaglercraft.v1_8.internal;

import dev.onvoid.webrtc.*;
import dev.onvoid.webrtc.internal.NativeLoader;
import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.internal.ClientPlatformSingleplayer;
import net.lax1dude.eaglercraft.v1_8.sp.lan.LANPeerEvent;

import org.apache.commons.lang3.SystemUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
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

	private static final Object lock1 = new Object();
	private static final Object lock2 = new Object();

	private static final List<ScheduledRunnable> scheduledRunnables = new LinkedList<>();

	private static class ScheduledRunnable {
		
		private final long runAt;
		private final Runnable runnable;
		
		private ScheduledRunnable(long runAt, Runnable runnable) {
			this.runAt = runAt;
			this.runnable = runnable;
		}
		
	}

	public static PeerConnectionFactory pcFactory;

	private static boolean supported = true;

	public static boolean supported() {
		if (supported && pcFactory == null) {
			try {
				System.load(Paths.get(SystemUtils.IS_OS_WINDOWS ? "webrtc-java.dll" : "libwebrtc-java.so").toAbsolutePath().toString());
				Field f = NativeLoader.class.getDeclaredField("LOADED_LIB_SET");
				f.setAccessible(true);
				((Set<String>) f.get(null)).add("webrtc-java");
				pcFactory = new PeerConnectionFactory();
				Runtime.getRuntime().addShutdownHook(new Thread(() -> pcFactory.dispose()));
				supported = true;
			} catch (Exception e) {
				logger.error("Failed to load WebRTC native library!");
				logger.error(e);
				supported = false;
			}
		}
		return supported;
	}

	private static final Comparator<ScheduledRunnable> sortTasks = (r1, r2) -> {
		return (int)(r1.runAt - r2.runAt);
	};

	public static void runScheduledTasks() {
		List<ScheduledRunnable> toRun = null;
		synchronized(scheduledRunnables) {
			if(scheduledRunnables.isEmpty()) return;
			long millis = PlatformRuntime.steadyTimeMillis();
			Iterator<ScheduledRunnable> itr = scheduledRunnables.iterator();
			while(itr.hasNext()) {
				ScheduledRunnable r = itr.next();
				if(r.runAt < millis) {
					itr.remove();
					if(toRun == null) {
						toRun = new ArrayList<>(1);
					}
					toRun.add(r);
				}
			}
		}
		if(toRun != null) {
			Collections.sort(toRun, sortTasks);
			for(int i = 0, l = toRun.size(); i < l; ++i) {
				try {
					toRun.get(i).runnable.run();
				}catch(Throwable t) {
					logger.error("Caught exception running scheduled WebRTC task!");
					logger.error(t);
				}
			}
		}
	}

	static void scheduleTask(long runAfter, Runnable runnable) {
		synchronized(scheduledRunnables) {
			scheduledRunnables.add(new ScheduledRunnable(PlatformRuntime.steadyTimeMillis() + runAfter, runnable));
		}
	}

	public static class LANClient {
		public static final byte READYSTATE_INIT_FAILED = -2;
		public static final byte READYSTATE_FAILED = -1;
		public static final byte READYSTATE_DISCONNECTED = 0;
		public static final byte READYSTATE_CONNECTING = 1;
		public static final byte READYSTATE_CONNECTED = 2;

		public Set<Map<String, String>> iceServers = new HashSet<>();
		public RTCPeerConnection peerConnection = null;
		public RTCDataChannel dataChannel = null;

		public byte readyState = READYSTATE_CONNECTING;

		private final List<Map<String, String>> iceCandidates = new ArrayList<>();

		public void initialize() {
			try {
				if (dataChannel != null) {
					dataChannel.close();
					dataChannel = null;
				}
				synchronized (lock1) {
					if (peerConnection != null) {
						peerConnection.close();
						peerConnection = null;
					}
				}
				RTCConfiguration rtcConfig = new RTCConfiguration();
				for (Map<String, String> server : iceServers) {
					RTCIceServer iceServer = new RTCIceServer();
					iceServer.urls.add(server.get("urls"));
					iceServer.username = server.getOrDefault("username", null);
					iceServer.password = server.getOrDefault("credential", null);
					rtcConfig.iceServers.add(iceServer);
				}
				synchronized (lock1) {
					this.peerConnection = pcFactory.createPeerConnection(rtcConfig, new PeerConnectionObserver() {
						@Override
						public void onIceCandidate(RTCIceCandidate iceCandidate) {
							synchronized (lock1) {
								if (iceCandidate.sdp != null && !iceCandidate.sdp.isEmpty()) {
									if (iceCandidates.isEmpty()) {
										final int[] candidateState = new int[2];
										final Runnable[] runnable = new Runnable[1];
										scheduleTask(2000l, runnable[0] = () -> {
											synchronized (lock1) {
												if (peerConnection != null && peerConnection.getConnectionState() != RTCPeerConnectionState.DISCONNECTED) {
													int trial = ++candidateState[1];
													if(candidateState[0] != iceCandidates.size() && trial < 3) {
														candidateState[0] = iceCandidates.size();
														scheduleTask(2000l, runnable[0]);
														return;
													}
													clientICECandidate = JSONWriter.valueToString(iceCandidates);
													iceCandidates.clear();
												}
											}
										});
									}
									Map<String, String> m = new HashMap<>();
									m.put("sdpMLineIndex", "" + iceCandidate.sdpMLineIndex);
									m.put("candidate", iceCandidate.sdp);
									iceCandidates.add(m);
								}
							}
						}

						@Override
						public void onConnectionChange(RTCPeerConnectionState connectionState) {
							if (connectionState == RTCPeerConnectionState.DISCONNECTED) {
								signalRemoteDisconnect(false);
							} else if (connectionState == RTCPeerConnectionState.CONNECTED) {
								readyState = READYSTATE_CONNECTED;
							} else if (connectionState == RTCPeerConnectionState.FAILED) {
								readyState = READYSTATE_FAILED;
								signalRemoteDisconnect(false);
							}
						}
					});
				}
				this.readyState = READYSTATE_CONNECTING;
			} catch (Throwable t) {
				readyState = READYSTATE_INIT_FAILED;
			}
		}

		public void setIceServers(String[] urls) {
			iceServers.clear();
			for (int i = 0; i < urls.length; ++i) {
				String url = urls[i];
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

		public void sendPacketToServer(RTCDataChannelBuffer buffer) {
			if (dataChannel != null && dataChannel.getState() == RTCDataChannelState.OPEN) {
				try {
					dataChannel.send(buffer);
				} catch (Throwable e) {
					signalRemoteDisconnect(false);
				}
			} else {
				signalRemoteDisconnect(false);
			}
		}

		public void signalRemoteConnect() {
			dataChannel = peerConnection.createDataChannel("lan", new RTCDataChannelInit());

			dataChannel.registerObserver(new RTCDataChannelObserver() {
				@Override
				public void onBufferedAmountChange(long l) {
					//
				}

				@Override
				public void onStateChange() {
					if (dataChannel != null && dataChannel.getState() == RTCDataChannelState.OPEN) {
						final Runnable[] retry = new Runnable[1];
						final int[] loopCount = new int[1];
						scheduleTask(-1l, retry[0] = () -> {
							f: {
								synchronized (lock1) {
									if (iceCandidates.isEmpty()) {
										break f;
									}
								}
								if(++loopCount[0] < 5) {
									scheduleTask(1000l, retry[0]);
								}
								return;
							}
							synchronized (lock2) {
								clientDataChannelClosed = false;
								clientDataChannelOpen = true;
							}
						});
					}
				}

				@Override
				public void onMessage(RTCDataChannelBuffer buffer) {
					if (!buffer.binary) return;
					byte[] data = new byte[buffer.data.remaining()];
					buffer.data.get(data);
					synchronized (clientLANPacketBuffer) {
						clientLANPacketBuffer.add(data);
					}
				}
			});

			peerConnection.createOffer(new RTCOfferOptions(), new CreateSessionDescriptionObserver() {
				@Override
				public void onSuccess(RTCSessionDescription desc) {
					peerConnection.setLocalDescription(desc, new SetSessionDescriptionObserver() {
						@Override
						public void onSuccess() {
							JSONObject descJson = new JSONObject();
							descJson.put("type", desc.sdpType.name().toLowerCase());
							descJson.put("sdp", desc.sdp);
							clientDescription = descJson.toString();
						}

						@Override
						public void onFailure(String s) {
							logger.error("Failed to set local description! {}", s);
							readyState = READYSTATE_FAILED;
							signalRemoteDisconnect(false);
						}
					});
				}

				@Override
				public void onFailure(String s) {
					logger.error("Failed to set create offer! {}", s);
					readyState = READYSTATE_FAILED;
					signalRemoteDisconnect(false);
				}
			});
		}

		public void signalRemoteDescription(String json) {
			try {
				JSONObject jsonObject = new JSONObject(json);
				peerConnection.setRemoteDescription(new RTCSessionDescription(RTCSdpType.valueOf(jsonObject.getString("type").toUpperCase()), jsonObject.getString("sdp")), new SetSessionDescriptionObserver() {
					@Override
					public void onSuccess() {
						//
					}

					@Override
					public void onFailure(String s) {
						logger.error(s);
						readyState = READYSTATE_FAILED;
						signalRemoteDisconnect(false);
					}
				});
			} catch (Throwable t) {
				EagRuntime.debugPrintStackTrace(t);
				readyState = READYSTATE_FAILED;
				signalRemoteDisconnect(false);
			}
		}

		public void signalRemoteICECandidate(String candidates) {
			try {
				JSONArray jsonArray = new JSONArray(candidates);
				for (int i = 0, l = jsonArray.length(); i < l; ++i) {
					JSONObject candidate = jsonArray.getJSONObject(i);
					peerConnection.addIceCandidate(new RTCIceCandidate(null, candidate.getInt("sdpMLineIndex"), candidate.getString("candidate")));
				}
			} catch (Throwable t) {
				EagRuntime.debugPrintStackTrace(t);
				readyState = READYSTATE_FAILED;
				signalRemoteDisconnect(false);
			}
		}

		public void signalRemoteDisconnect(boolean quiet) {
			if (dataChannel != null) {
				dataChannel.close();
				dataChannel = null;
			}
			synchronized (lock1) {
				if (peerConnection != null) {
					peerConnection.close();
					peerConnection = null;
				}
			}
			synchronized (lock2) {
				if (!quiet) clientDataChannelClosed = true;
			}
			readyState = READYSTATE_DISCONNECTED;
		}
	}

	public static final byte PEERSTATE_FAILED = 0;
	public static final byte PEERSTATE_SUCCESS = 1;
	public static final byte PEERSTATE_LOADING = 2;

	public static class LANPeer {
		public LANServer client;
		public String peerId;
		public RTCPeerConnection peerConnection;
		public RTCDataChannel dataChannel;
		public String ipcChannel;

		public LANPeer(LANServer client, String peerId, RTCPeerConnection peerConnection) {
			this.client = client;
			this.peerId = peerId;
			this.peerConnection = peerConnection;
		}

		public void disconnect() {
			if(dataChannel != null) {
				dataChannel.close();
				dataChannel = null;
			}
			peerConnection.close();
			peerConnection = null;
		}

		public void setRemoteDescription(String descJSON) {
			try {
				JSONObject remoteDesc = new JSONObject(descJSON);
				peerConnection.setRemoteDescription(new RTCSessionDescription(RTCSdpType.valueOf(remoteDesc.getString("type").toUpperCase()), remoteDesc.getString("sdp")), new SetSessionDescriptionObserver() {
					@Override
					public void onSuccess() {
						if (remoteDesc.has("type") && "offer".equals(remoteDesc.getString("type"))) {
							peerConnection.createAnswer(new RTCAnswerOptions(), new CreateSessionDescriptionObserver() {
								@Override
								public void onSuccess(RTCSessionDescription desc) {
									peerConnection.setLocalDescription(desc, new SetSessionDescriptionObserver() {
										@Override
										public void onSuccess() {
											JSONObject descJson = new JSONObject();
											descJson.put("type", desc.sdpType.name().toLowerCase());
											descJson.put("sdp", desc.sdp);
											LANPeerEvent.LANPeerDescriptionEvent e = new LANPeerEvent.LANPeerDescriptionEvent(peerId, descJson.toString());
											synchronized (serverLANEventBuffer) {
												serverLANEventBuffer.put(peerId, e);
											}
										}

										@Override
										public void onFailure(String s) {
											logger.error("Failed to set local description for \"{}\"! {}", peerId, s);
											client.signalRemoteDisconnect(peerId);
										}
									});
								}

								@Override
								public void onFailure(String s) {
									logger.error("Failed to create answer for \"{}\"! {}", peerId, s);
									client.signalRemoteDisconnect(peerId);
								}
							});
						}
					}

					@Override
					public void onFailure(String s) {
						logger.error("Failed to set remote description for \"{}\"! {}", peerId, s);
						client.signalRemoteDisconnect(peerId);
					}
				});
			} catch (Throwable err) {
				logger.error("Failed to parse remote description for \"{}\"! {}", peerId, err.getMessage());
				client.signalRemoteDisconnect(peerId);
			}
		}

		public void addICECandidate(String candidates) {
			try {
				JSONArray jsonArray = new JSONArray(candidates);
				for (int i = 0, l = jsonArray.length(); i < l; ++i) {
					JSONObject candidate = jsonArray.getJSONObject(i);
					peerConnection.addIceCandidate(new RTCIceCandidate(null, candidate.getInt("sdpMLineIndex"), candidate.getString("candidate")));
				}
			} catch (Throwable err) {
				logger.error("Failed to parse ice candidate for \"{}\"! {}", peerId, err.getMessage());
				client.signalRemoteDisconnect(peerId);
			}
		}

		public void mapIPC(String ipcChannel) {
			if(this.ipcChannel == null) {
				if(ipcChannel != null) {
					this.ipcChannel = ipcChannel;
					synchronized(this.client.ipcMapList) {
						this.client.ipcMapList.put(ipcChannel, this);
					}
				}
			}else {
				if(ipcChannel == null) {
					synchronized(this.client.ipcMapList) {
						this.client.ipcMapList.remove(this.ipcChannel);
					}
					this.ipcChannel = null;
				}
			}
		}
	}

	public static class LANServer {
		public Set<Map<String, String>> iceServers = new HashSet<>();
		public Map<String, LANPeer> peerList = new HashMap<>();
		public Map<String, LANPeer> ipcMapList = new HashMap<>();
		private final Object lock3 = new Object();

		public void setIceServers(String[] urls) {
			iceServers.clear();
			for (int i = 0; i < urls.length; ++i) {
				String[] etr = urls[i].split(";");
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

		public void sendPacketToRemoteClient(String peerId, RTCDataChannelBuffer buffer) {
			LANPeer thePeer;
			synchronized(peerList) {
				thePeer = peerList.get(peerId);
			}
			if (thePeer != null) {
				sendPacketToRemoteClient(thePeer, buffer);
			}
		}

		public void sendPacketToRemoteClient(LANPeer thePeer, RTCDataChannelBuffer buffer) {
			boolean b = false;
			if(thePeer.dataChannel != null && thePeer.dataChannel.getState() == RTCDataChannelState.OPEN) {
				try {
					thePeer.dataChannel.send(buffer);
				} catch (Exception e) {
					b = true;
				}
			} else {
				b = true;
			}
			if (b) {
				signalRemoteDisconnect(thePeer.peerId);
			}
		}

		public void signalRemoteConnect(String peerId) {
			try {
				List<Map<String, String>> iceCandidates = new ArrayList<>();
				RTCConfiguration rtcConfig = new RTCConfiguration();
				for (Map<String, String> server : iceServers) {
					RTCIceServer iceServer = new RTCIceServer();
					iceServer.urls.add(server.get("urls"));
					iceServer.username = server.getOrDefault("username", null);
					iceServer.password = server.getOrDefault("credential", null);
					rtcConfig.iceServers.add(iceServer);
				}
				final RTCPeerConnection[] peerConnection = new RTCPeerConnection[1];
				final LANPeer[] peerInstance = new LANPeer[1];
				peerConnection[0] = pcFactory.createPeerConnection(rtcConfig, new PeerConnectionObserver() {
					@Override
					public void onIceCandidate(RTCIceCandidate iceCandidate) {
						synchronized (lock3) {
							if (iceCandidate.sdp != null && !iceCandidate.sdp.isEmpty()) {
								if (iceCandidates.isEmpty()) {
									final int[] candidateState = new int[2];
									final Runnable[] runnable = new Runnable[1];
									scheduleTask(2000l, runnable[0] = () -> {
										synchronized (lock3) {
											if (peerConnection[0] != null && peerConnection[0].getConnectionState() != RTCPeerConnectionState.DISCONNECTED) {
												int trial = ++candidateState[1];
												if(candidateState[0] != iceCandidates.size() && trial < 3) {
													candidateState[0] = iceCandidates.size();
													scheduleTask(2000l, runnable[0]);
													return;
												}
												LANPeerEvent.LANPeerICECandidateEvent e = new LANPeerEvent.LANPeerICECandidateEvent(peerId, JSONWriter.valueToString(iceCandidates));
												synchronized (serverLANEventBuffer) {
													serverLANEventBuffer.put(peerId, e);
												}
												iceCandidates.clear();
											}
										}
									});
								}
								Map<String, String> m = new HashMap<>();
								m.put("sdpMLineIndex", "" + iceCandidate.sdpMLineIndex);
								m.put("candidate", iceCandidate.sdp);
								iceCandidates.add(m);
							}
						}
					}

					@Override
					public void onDataChannel(RTCDataChannel dataChannel) {
						final Runnable[] retry = new Runnable[1];
						final int[] loopCount = new int[1];
						scheduleTask(-1l, retry[0] = () -> {
							f: {
								synchronized (lock3) {
									if (iceCandidates.isEmpty()) {
										break f;
									}
								}
								if(++loopCount[0] < 5) {
									scheduleTask(1000l, retry[0]);
								}
								return;
							}
							if (dataChannel == null) return;
							if(peerInstance[0].dataChannel != null) {
								dataChannel.close();
								return;
							}
							peerInstance[0].dataChannel = dataChannel;
							synchronized (serverLANEventBuffer) {
								serverLANEventBuffer.put(peerId, new LANPeerEvent.LANPeerDataChannelEvent(peerId));
							}
							dataChannel.registerObserver(new RTCDataChannelObserver() {
								@Override
								public void onBufferedAmountChange(long l) {
									//
								}

								@Override
								public void onStateChange() {
									//
								}

								@Override
								public void onMessage(RTCDataChannelBuffer buffer) {
									if (!buffer.binary) return;
									byte[] data = new byte[buffer.data.remaining()];
									buffer.data.get(data);
									LANPeer peer = peerInstance[0];
									if(peer.ipcChannel != null) {
										ClientPlatformSingleplayer.sendPacket(new IPCPacketData(peer.ipcChannel, data));
									}else {
										LANPeerEvent.LANPeerPacketEvent e = new LANPeerEvent.LANPeerPacketEvent(peerId, data);
										synchronized (serverLANEventBuffer) {
											serverLANEventBuffer.put(peerId, e);
										}
									}
								}
							});
						});
					}

					@Override
					public void onConnectionChange(RTCPeerConnectionState connectionState) {
						if (connectionState == RTCPeerConnectionState.DISCONNECTED || connectionState == RTCPeerConnectionState.FAILED) {
							LANServer.this.signalRemoteDisconnect(peerId);
						}
					}
				});
				peerInstance[0] = new LANPeer(this, peerId, peerConnection[0]);
				synchronized(peerList) {
					peerList.put(peerId, peerInstance[0]);
				}
			} catch (Throwable e) {
				logger.error("Failed to create peer for \"{}\"", peerId);
				logger.error(e);
				signalRemoteDisconnect(peerId);
			}
		}

		public void signalRemoteDescription(String peerId, String descJSON) {
			LANPeer thePeer;
			synchronized(peerList) {
				thePeer = peerList.get(peerId);
			}
			if (thePeer != null) {
				thePeer.setRemoteDescription(descJSON);
			}
		}

		public void signalRemoteICECandidate(String peerId, String candidate) {
			LANPeer thePeer;
			synchronized(peerList) {
				thePeer = peerList.get(peerId);
			}
			if (thePeer != null) {
				thePeer.addICECandidate(candidate);
			}
		}

		public void signalRemoteDisconnect(String peerId) {
			if (peerId == null || peerId.isEmpty()) {
				List<LANPeer> cpy;
				synchronized(peerList) {
					cpy = new ArrayList<>(peerList.values());
					peerList.clear();
				}
				for (LANPeer thePeer : cpy) {
					if (thePeer != null) {
						try {
							thePeer.disconnect();
						} catch (Throwable ignored) {
						}
						synchronized (serverLANEventBuffer) {
							serverLANEventBuffer.put(thePeer.peerId, new LANPeerEvent.LANPeerDisconnectEvent(thePeer.peerId));
						}
					}
				}
				return;
			}
			LANPeer thePeer;
			synchronized(peerList) {
				thePeer = peerList.remove(peerId);
			}
			if (thePeer != null) {
				if(thePeer.ipcChannel != null) {
					synchronized(ipcMapList) {
						ipcMapList.remove(thePeer.ipcChannel);
					}
				}
				try {
					thePeer.disconnect();
				} catch (Throwable ignored) {
				}
				synchronized (serverLANEventBuffer) {
					serverLANEventBuffer.put(thePeer.peerId, new LANPeerEvent.LANPeerDisconnectEvent(peerId));
				}
			}
		}

		public void serverPeerMapIPC(String peer, String ipcChannel) {
			LANPeer peerr;
			synchronized(peerList) {
				peerr = peerList.get(peer);
			}
			if(peerr != null) {
				peerr.mapIPC(ipcChannel);
			}
		}

		public int countPeers() {
			synchronized(peerList) {
				return peerList.size();
			}
		}
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
		rtcLANClient.sendPacketToServer(new RTCDataChannelBuffer(ByteBuffer.wrap(pkt), true));
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
		synchronized (lock2) {
			clientDataChannelOpen = false;
			clientDataChannelClosed = true;
		}
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
		synchronized (lock2) {
			if (clientDataChannelOpen) {
				clientDataChannelOpen = false;
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean clientLANClosed() {
		synchronized (lock2) {
			return clientDataChannelClosed;
		}
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
		rtcLANServer.sendPacketToRemoteClient(peer, new RTCDataChannelBuffer(ByteBuffer.wrap(data), true));
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

	public static boolean serverLANPeerPassIPC(IPCPacketData ipcPacket) {
		if(rtcLANServer != null) {
			LANPeer peer;
			synchronized(rtcLANServer.ipcMapList) {
				peer = rtcLANServer.ipcMapList.get(ipcPacket.channel);
			}
			if(peer != null) {
				rtcLANServer.sendPacketToRemoteClient(peer, new RTCDataChannelBuffer(ByteBuffer.wrap(ipcPacket.contents), true));
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