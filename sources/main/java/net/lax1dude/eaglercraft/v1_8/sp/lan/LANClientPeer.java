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

package net.lax1dude.eaglercraft.v1_8.sp.lan;

import java.util.LinkedList;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.IPCPacketData;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformWebRTC;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
import net.lax1dude.eaglercraft.v1_8.sp.internal.ClientPlatformSingleplayer;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket03ICECandidate;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.RelayPacket04Description;

class LANClientPeer {

	private static final Logger logger = LogManager.getLogger("LANClientPeer");

	private static final int PRE = 0, RECEIVED_ICE_CANDIDATE = 1, SENT_ICE_CANDIDATE = 2, RECEIVED_DESCRIPTION = 3,
			SENT_DESCRIPTION = 4, RECEIVED_SUCCESS = 5, CONNECTED = 6, CLOSED = 7;

	protected final String clientId;

	protected int state = PRE;
	protected boolean dead = false;

	protected long startTime;

	protected String localICECandidate = null;
	protected boolean localChannel = false;
	protected List<byte[]> packetPreBuffer = null;

	protected LANClientPeer(String clientId) {
		this.clientId = clientId;
		this.startTime = EagRuntime.steadyTimeMillis();
		PlatformWebRTC.serverLANCreatePeer(clientId);
	}

	protected void handleICECandidates(String candidates) {
		if(state == SENT_DESCRIPTION) {
			PlatformWebRTC.serverLANPeerICECandidates(clientId, candidates);
			if(localICECandidate != null) {
				LANServerController.lanRelaySocket.writePacket(new RelayPacket03ICECandidate(clientId, localICECandidate));
				localICECandidate = null;
				state = SENT_ICE_CANDIDATE;
			}else {
				state = RECEIVED_ICE_CANDIDATE;
			}
		}else {
			logger.error("Relay [{}] unexpected IPacket03ICECandidate for '{}'", LANServerController.lanRelaySocket.getURI(), clientId);
		}
	}

	protected void handleDescription(String description) {
		if(state == PRE) {
			PlatformWebRTC.serverLANPeerDescription(clientId, description);
			state = RECEIVED_DESCRIPTION;
		}else {
			logger.error("Relay [{}] unexpected IPacket04Description for '{}'", LANServerController.lanRelaySocket.getURI(), clientId);
		}
	}

	protected void handleSuccess() {
		if(state == SENT_ICE_CANDIDATE) {
			if(localChannel) {
				SingleplayerServerController.openPlayerChannel(clientId);
				PlatformWebRTC.serverLANPeerMapIPC(clientId, clientId);
				if(packetPreBuffer != null) {
					for(byte[] b : packetPreBuffer) {
						ClientPlatformSingleplayer.sendPacket(new IPCPacketData(clientId, b));
					}
					packetPreBuffer = null;
				}
				state = CONNECTED;
			}else {
				state = RECEIVED_SUCCESS;
			}
		}else if(state != CONNECTED) {
			logger.error("Relay [{}] unexpected IPacket05ClientSuccess for '{}'", LANServerController.lanRelaySocket.getURI(), clientId);
		}
	}

	protected void handleFailure() {
		if(state == SENT_ICE_CANDIDATE) {
			logger.error("Client '{}' failed to connect", clientId);
			disconnect();
		}else {
			logger.error("Relay [{}] unexpected IPacket06ClientFailure for '{}'", LANServerController.lanRelaySocket.getURI(), clientId);
		}
	}

	protected void update() {
		if(state != CLOSED) {
			if(state != CONNECTED && EagRuntime.steadyTimeMillis() - startTime > 13000l) {
				logger.info("LAN client '{}' handshake timed out", clientId);
				disconnect();
				return;
			}
			List<LANPeerEvent> l = PlatformWebRTC.serverLANGetAllEvent(clientId);
			if(l == null) {
				return;
			}
			read_loop: for(int i = 0, s = l.size(); i < s; ++i) {
				LANPeerEvent evt = l.get(i);
				if(evt instanceof LANPeerEvent.LANPeerDisconnectEvent) {
					logger.info("LAN client '{}' disconnected", clientId);
					disconnect();
				}else {
					switch(state) {
						case SENT_DESCRIPTION:{
							if(evt instanceof LANPeerEvent.LANPeerICECandidateEvent) {
								localICECandidate = ((LANPeerEvent.LANPeerICECandidateEvent)evt).candidates;
								continue read_loop;
							}
							break;
						}
						case RECEIVED_DESCRIPTION: {
							if(evt instanceof LANPeerEvent.LANPeerDescriptionEvent) {
								LANServerController.lanRelaySocket.writePacket(new RelayPacket04Description(clientId, ((LANPeerEvent.LANPeerDescriptionEvent)evt).description));
								state = SENT_DESCRIPTION;
								continue read_loop;
							}
							break;
						}
						case RECEIVED_ICE_CANDIDATE: {
							if(evt instanceof LANPeerEvent.LANPeerICECandidateEvent) {
								LANServerController.lanRelaySocket.writePacket(new RelayPacket03ICECandidate(clientId, ((LANPeerEvent.LANPeerICECandidateEvent)evt).candidates));
								state = SENT_ICE_CANDIDATE;
								continue read_loop;
							}else if(evt instanceof LANPeerEvent.LANPeerDataChannelEvent) {
								localChannel = true;
								continue read_loop;
							}else if(evt instanceof LANPeerEvent.LANPeerPacketEvent) {
								if(packetPreBuffer == null) packetPreBuffer = new LinkedList<>();
								packetPreBuffer.add(((LANPeerEvent.LANPeerPacketEvent)evt).payload);
								continue read_loop;
							}
							break;
						}
						case SENT_ICE_CANDIDATE: {
							if(evt instanceof LANPeerEvent.LANPeerDataChannelEvent) {
								localChannel = true;
								continue read_loop;
							}else if(evt instanceof LANPeerEvent.LANPeerPacketEvent) {
								if(packetPreBuffer == null) packetPreBuffer = new LinkedList<>();
								packetPreBuffer.add(((LANPeerEvent.LANPeerPacketEvent)evt).payload);
								continue read_loop;
							}
							break;
						}
						case RECEIVED_SUCCESS: {
							if(evt instanceof LANPeerEvent.LANPeerDataChannelEvent) {
								SingleplayerServerController.openPlayerChannel(clientId);
								PlatformWebRTC.serverLANPeerMapIPC(clientId, clientId);
								if(packetPreBuffer != null) {
									for(byte[] b : packetPreBuffer) {
										ClientPlatformSingleplayer.sendPacket(new IPCPacketData(clientId, b));
									}
									packetPreBuffer = null;
								}
								state = CONNECTED;
								continue read_loop;
							}else if(evt instanceof LANPeerEvent.LANPeerICECandidateEvent) {
								continue read_loop;
							}
							break;
						}
						case CONNECTED: {
							if(evt instanceof LANPeerEvent.LANPeerPacketEvent) {
								//logger.warn("Forwarding packet for '{}' to IPC channel manually, even though the channel should be mapped", clientId);
								// just to be safe
								ClientPlatformSingleplayer.sendPacket(new IPCPacketData(clientId, ((LANPeerEvent.LANPeerPacketEvent)evt).payload));
								continue read_loop;
							}
							break;
						}
						default: {
							break;
						}
					}
					if(state != CLOSED) {
						logger.error("LAN client '{}' had an accident: {} (state {})", clientId, evt.getClass().getSimpleName(), state);
					}
					disconnect();
					return;
				}
			}
		}else {
			disconnect();
		}
	}

	protected void disconnect() {
		if(!dead) {
			if(state == CONNECTED) {
				SingleplayerServerController.closePlayerChannel(clientId);
			}
			state = CLOSED;
			PlatformWebRTC.serverLANDisconnectPeer(clientId);
			dead = true;
		}
	}

}