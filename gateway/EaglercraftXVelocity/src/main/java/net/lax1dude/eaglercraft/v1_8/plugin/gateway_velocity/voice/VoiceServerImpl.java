package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.voice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.velocitypowered.api.proxy.server.ServerInfo;

import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.server.SPacketRPCEventToggledVoice;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.EnumVoiceState;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.event.EaglercraftVoiceStatusChangeEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPlayerData;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.backend_rpc_protocol.EnumSubscribedEvent;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketVoiceSignalConnectV3EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketVoiceSignalConnectV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketVoiceSignalDescEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketVoiceSignalDisconnectPeerEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketVoiceSignalGlobalEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketVoiceSignalICEEAG;

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
public class VoiceServerImpl {

	private final ServerInfo server;
	private final GameMessagePacket iceServersPacket;

	private final Map<UUID, EaglerPlayerData> voicePlayers = new HashMap<>();
	private final Map<UUID, ExpiringSet<UUID>> voiceRequests = new HashMap<>();
	private final Set<VoicePair> voicePairs = new HashSet<>();

	private static final int VOICE_CONNECT_RATELIMIT = 15;

	private static class VoicePair {

		private final UUID uuid1;
		private final UUID uuid2;

		@Override
		public int hashCode() {
			return uuid1.hashCode() ^ uuid2.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			VoicePair other = (VoicePair) obj;
			return (uuid1.equals(other.uuid1) && uuid2.equals(other.uuid2))
					|| (uuid1.equals(other.uuid2) && uuid2.equals(other.uuid1));
		}

		private VoicePair(UUID uuid1, UUID uuid2) {
			this.uuid1 = uuid1;
			this.uuid2 = uuid2;
		}

		private boolean anyEquals(UUID uuid) {
			return uuid1.equals(uuid) || uuid2.equals(uuid);
		}
	}

	VoiceServerImpl(ServerInfo server, GameMessagePacket iceServersPacket) {
		this.server = server;
		this.iceServersPacket = iceServersPacket;
	}

	public void handlePlayerLoggedIn(EaglerPlayerData player) {
		player.sendEaglerMessage(iceServersPacket);
		player.fireVoiceStateChange(EaglercraftVoiceStatusChangeEvent.EnumVoiceState.DISABLED);
		if(player.getRPCEventSubscribed(EnumSubscribedEvent.TOGGLE_VOICE)) {
			player.getRPCSessionHandler().handleVoiceStateTransition(SPacketRPCEventToggledVoice.VOICE_STATE_DISABLED);
		}
	}

	public void handlePlayerLoggedOut(EaglerPlayerData player) {
		removeUser(player.getUniqueId());
	}

	void handleVoiceSignalPacketTypeRequest(UUID player, EaglerPlayerData sender) {
		synchronized (voicePlayers) {
			UUID senderUUID = sender.getUniqueId();
			if (senderUUID.equals(player))
				return; // prevent duplicates
			if (!voicePlayers.containsKey(senderUUID))
				return;
			EaglerPlayerData targetPlayerCon = voicePlayers.get(player);
			if (targetPlayerCon == null)
				return;
			VoicePair newPair = new VoicePair(player, senderUUID);
			if (voicePairs.contains(newPair))
				return; // already paired
			ExpiringSet<UUID> senderRequestSet = voiceRequests.get(senderUUID);
			if (senderRequestSet == null) {
				voiceRequests.put(senderUUID, senderRequestSet = new ExpiringSet<>(2000));
			}
			if (!senderRequestSet.add(player)) {
				return;
			}

			// check if other has requested earlier
			ExpiringSet<UUID> theSet;
			if ((theSet = voiceRequests.get(player)) != null && theSet.contains(senderUUID)) {
				theSet.remove(senderUUID);
				if (theSet.isEmpty())
					voiceRequests.remove(player);
				senderRequestSet.remove(player);
				if (senderRequestSet.isEmpty())
					voiceRequests.remove(senderUUID);
				// send each other add data
				voicePairs.add(newPair);
				if (targetPlayerCon.getEaglerProtocol().ver <= 3) {
					targetPlayerCon.sendEaglerMessage(new SPacketVoiceSignalConnectV3EAG(
							senderUUID.getMostSignificantBits(), senderUUID.getLeastSignificantBits(), false, false));
				} else {
					targetPlayerCon.sendEaglerMessage(new SPacketVoiceSignalConnectV4EAG(
							senderUUID.getMostSignificantBits(), senderUUID.getLeastSignificantBits(), false));
				}
				if (sender.getEaglerProtocol().ver <= 3) {
					sender.sendEaglerMessage(new SPacketVoiceSignalConnectV3EAG(
							player.getMostSignificantBits(), player.getLeastSignificantBits(), false, true));
				} else {
					sender.sendEaglerMessage(new SPacketVoiceSignalConnectV4EAG(
							player.getMostSignificantBits(), player.getLeastSignificantBits(), true));
				}
			}
		}
	}

	void handleVoiceSignalPacketTypeConnect(EaglerPlayerData sender) {
		if(!sender.voiceConnectRateLimiter.rateLimit(VOICE_CONNECT_RATELIMIT)) {
			return;
		}
		sender.fireVoiceStateChange(EaglercraftVoiceStatusChangeEvent.EnumVoiceState.ENABLED);
		if(sender.getRPCEventSubscribed(EnumSubscribedEvent.TOGGLE_VOICE)) {
			sender.getRPCSessionHandler().handleVoiceStateTransition(SPacketRPCEventToggledVoice.VOICE_STATE_ENABLED);
		}
		synchronized (voicePlayers) {
			if (voicePlayers.containsKey(sender.getUniqueId())) {
				return;
			}
			boolean hasNoOtherPlayers = voicePlayers.isEmpty();
			voicePlayers.put(sender.getUniqueId(), sender);
			if (hasNoOtherPlayers) {
				return;
			}
			Collection<SPacketVoiceSignalGlobalEAG.UserData> userDatas = new ArrayList<>(voicePlayers.size());
			for(EaglerPlayerData userCon : voicePlayers.values()) {
				UUID uuid = userCon.getUniqueId();
				userDatas.add(new SPacketVoiceSignalGlobalEAG.UserData(uuid.getMostSignificantBits(),
						uuid.getLeastSignificantBits(), userCon.getName()));
			}
			GameMessagePacket packetToBroadcast = new SPacketVoiceSignalGlobalEAG(userDatas);
			for (EaglerPlayerData userCon : voicePlayers.values()) {
				userCon.sendEaglerMessage(packetToBroadcast);
			}
		}
	}

	void handleVoiceSignalPacketTypeICE(UUID player, byte[] str, EaglerPlayerData sender) {
		EaglerPlayerData pass;
		VoicePair pair = new VoicePair(player, sender.getUniqueId());
		synchronized (voicePlayers) {
			pass = voicePairs.contains(pair) ? voicePlayers.get(player) : null;
		}
		if (pass != null) {
			UUID uuid = sender.getUniqueId();
			pass.sendEaglerMessage(
					new SPacketVoiceSignalICEEAG(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), str));
		}
	}

	void handleVoiceSignalPacketTypeDesc(UUID player, byte[] str, EaglerPlayerData sender) {
		EaglerPlayerData pass;
		VoicePair pair = new VoicePair(player, sender.getUniqueId());
		synchronized (voicePlayers) {
			pass = voicePairs.contains(pair) ? voicePlayers.get(player) : null;
		}
		if (pass != null) {
			UUID uuid = sender.getUniqueId();
			pass.sendEaglerMessage(
					new SPacketVoiceSignalDescEAG(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), str));
		}
	}

	void handleVoiceSignalPacketTypeDisconnect(EaglerPlayerData sender) {
		removeUser(sender.getUniqueId());
	}

	void handleVoiceSignalPacketTypeDisconnectPeer(UUID player, EaglerPlayerData sender) {
		synchronized (voicePlayers) {
			if (!voicePlayers.containsKey(player)) {
				return;
			}
			Iterator<VoicePair> pairsItr = voicePairs.iterator();
			while (pairsItr.hasNext()) {
				VoicePair voicePair = pairsItr.next();
				UUID target = null;
				if (voicePair.uuid1.equals(player)) {
					target = voicePair.uuid2;
				} else if (voicePair.uuid2.equals(player)) {
					target = voicePair.uuid1;
				}
				if (target != null) {
					pairsItr.remove();
					EaglerPlayerData conn = voicePlayers.get(target);
					if (conn != null) {
						conn.sendEaglerMessage(new SPacketVoiceSignalDisconnectPeerEAG(player.getMostSignificantBits(),
								player.getLeastSignificantBits()));
					}
					sender.sendEaglerMessage(new SPacketVoiceSignalDisconnectPeerEAG(target.getMostSignificantBits(),
							target.getLeastSignificantBits()));
				}
			}
		}
	}

	public void removeUser(UUID user) {
		synchronized (voicePlayers) {
			EaglerPlayerData connRemove;
			if ((connRemove = voicePlayers.remove(user)) == null) {
				return;
			}else {
				connRemove.fireVoiceStateChange(EaglercraftVoiceStatusChangeEvent.EnumVoiceState.DISABLED);
				if(connRemove.getRPCEventSubscribed(EnumSubscribedEvent.TOGGLE_VOICE)) {
					connRemove.getRPCSessionHandler().handleVoiceStateTransition(SPacketRPCEventToggledVoice.VOICE_STATE_DISABLED);
				}
			}
			voiceRequests.remove(user);
			if (voicePlayers.size() > 0) {
				Collection<SPacketVoiceSignalGlobalEAG.UserData> userDatas = new ArrayList<>(voicePlayers.size());
				for(EaglerPlayerData userCon : voicePlayers.values()) {
					UUID uuid = userCon.getUniqueId();
					userDatas.add(new SPacketVoiceSignalGlobalEAG.UserData(uuid.getMostSignificantBits(),
							uuid.getLeastSignificantBits(), userCon.getName()));
				}
				GameMessagePacket voicePlayersPkt = new SPacketVoiceSignalGlobalEAG(userDatas);
				for (EaglerPlayerData userCon : voicePlayers.values()) {
					if (!user.equals(userCon.getUniqueId())) {
						userCon.sendEaglerMessage(voicePlayersPkt);
					}
				}
			}
			Iterator<VoicePair> pairsItr = voicePairs.iterator();
			while (pairsItr.hasNext()) {
				VoicePair voicePair = pairsItr.next();
				UUID target = null;
				if (voicePair.uuid1.equals(user)) {
					target = voicePair.uuid2;
				} else if (voicePair.uuid2.equals(user)) {
					target = voicePair.uuid1;
				}
				if (target != null) {
					pairsItr.remove();
					if (voicePlayers.size() > 0) {
						EaglerPlayerData conn = voicePlayers.get(target);
						if (conn != null) {
							conn.sendEaglerMessage(new SPacketVoiceSignalDisconnectPeerEAG(
									user.getMostSignificantBits(), user.getLeastSignificantBits()));
						}
					}
				}
			}
		}
	}

	EnumVoiceState getPlayerVoiceState(UUID uniqueId) {
		synchronized (voicePlayers) {
			if(voicePlayers.containsKey(uniqueId)) {
				return EnumVoiceState.ENABLED;
			}
		}
		return EnumVoiceState.DISABLED;
	}

}
