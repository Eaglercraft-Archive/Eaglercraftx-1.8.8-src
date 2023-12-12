package net.lax1dude.eaglercraft.v1_8.sp.lan;

import java.util.Iterator;
import java.util.List;

import net.lax1dude.eaglercraft.v1_8.EagUtils;
import net.lax1dude.eaglercraft.v1_8.internal.IPCPacketData;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformWebRTC;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
import net.lax1dude.eaglercraft.v1_8.sp.internal.ClientPlatformSingleplayer;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.IPacket03ICECandidate;
import net.lax1dude.eaglercraft.v1_8.sp.relay.pkt.IPacket04Description;

/**
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
class LANClientPeer {

	private static final Logger logger = LogManager.getLogger("LANClientPeer");

	private static final int PRE = 0, SENT_ICE_CANDIDATE = 2, SENT_DESCRIPTION = 3, CONNECTED = 4, CLOSED = 5;

	protected final String clientId;

	protected int state = PRE;
	protected boolean dead = false;

	protected LANClientPeer(String clientId) {
		this.clientId = clientId;
		PlatformWebRTC.serverLANCreatePeer(clientId);
	}

	protected void handleICECandidates(String candidates) {
		if(state == SENT_DESCRIPTION) {
			PlatformWebRTC.serverLANPeerICECandidates(clientId, candidates);
			long millis = System.currentTimeMillis();
			do {
				LANPeerEvent evt;
				if((evt = PlatformWebRTC.serverLANGetEvent(clientId)) != null) {
					if(evt instanceof LANPeerEvent.LANPeerICECandidateEvent) {
						LANServerController.lanRelaySocket.writePacket(new IPacket03ICECandidate(clientId, ((LANPeerEvent.LANPeerICECandidateEvent)evt).candidates));
						state = SENT_ICE_CANDIDATE;
						return;
					}else if(evt instanceof LANPeerEvent.LANPeerDisconnectEvent) {
						logger.error("LAN client '{}' disconnected while waiting for server ICE candidates", clientId);
					}else {
						logger.error("LAN client '{}' had an accident: {}", clientId, evt.getClass().getSimpleName());
					}
					disconnect();
					return;
				}
				EagUtils.sleep(20l);
			}while(System.currentTimeMillis() - millis < 5000l);
			logger.error("Getting server ICE candidates for '{}' timed out!", clientId);
			disconnect();
		}else {
			logger.error("Relay [{}] unexpected IPacket03ICECandidate for '{}'", LANServerController.lanRelaySocket.getURI(), clientId);
		}
	}

	protected void handleDescription(String description) {
		if(state == PRE) {
			PlatformWebRTC.serverLANPeerDescription(clientId, description);
			long millis = System.currentTimeMillis();
			do {
				LANPeerEvent evt;
				if((evt = PlatformWebRTC.serverLANGetEvent(clientId)) != null) {
					if(evt instanceof LANPeerEvent.LANPeerDescriptionEvent) {
						LANServerController.lanRelaySocket.writePacket(new IPacket04Description(clientId, ((LANPeerEvent.LANPeerDescriptionEvent)evt).description));
						state = SENT_DESCRIPTION;
						return;
					}else if(evt instanceof LANPeerEvent.LANPeerDisconnectEvent) {
						logger.error("LAN client '{}' disconnected while waiting for server description", clientId);
					}else {
						logger.error("LAN client '{}' had an accident: {}", clientId, evt.getClass().getSimpleName());
					}
					disconnect();
					return;
				}
				EagUtils.sleep(20l);
			}while(System.currentTimeMillis() - millis < 5000l);
			logger.error("Getting server description for '{}' timed out!", clientId);
			disconnect();
		}else {
			logger.error("Relay [{}] unexpected IPacket04Description for '{}'", LANServerController.lanRelaySocket.getURI(), clientId);
		}
	}

	protected void handleSuccess() {
		if(state == SENT_ICE_CANDIDATE) {
			long millis = System.currentTimeMillis();
			do {
				LANPeerEvent evt;
				while((evt = PlatformWebRTC.serverLANGetEvent(clientId)) != null && evt instanceof LANPeerEvent.LANPeerICECandidateEvent) {
					// skip ice candidates
				}
				if(evt != null) {
					if(evt instanceof LANPeerEvent.LANPeerDataChannelEvent) {
						SingleplayerServerController.openPlayerChannel(clientId);
						state = CONNECTED;
						return;
					}else if(evt instanceof LANPeerEvent.LANPeerDisconnectEvent) {
						logger.error("LAN client '{}' disconnected while waiting for connection", clientId);
					}else {
						logger.error("LAN client '{}' had an accident: {}", clientId, evt.getClass().getSimpleName());
					}
					disconnect();
					return;
				}
				EagUtils.sleep(20l);
			}while(System.currentTimeMillis() - millis < 5000l);
			logger.error("Getting server description for '{}' timed out!", clientId);
			disconnect();
		}else {
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
		if(state == CONNECTED) {
			List<LANPeerEvent> l = PlatformWebRTC.serverLANGetAllEvent(clientId);
			if(l == null) {
				return;
			}
			Iterator<LANPeerEvent> itr = l.iterator();
			while(state == CONNECTED && itr.hasNext()) {
				LANPeerEvent evt = itr.next();
				if(evt instanceof LANPeerEvent.LANPeerPacketEvent) {
					ClientPlatformSingleplayer.sendPacket(new IPCPacketData(clientId, ((LANPeerEvent.LANPeerPacketEvent)evt).payload));
				}else if(evt instanceof LANPeerEvent.LANPeerDisconnectEvent) {
					logger.info("LAN client '{}' disconnected", clientId);
					disconnect();
				}else {
					logger.error("LAN client '{}' had an accident: {}", clientId, evt.getClass().getSimpleName());
					disconnect();
				}
			}
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