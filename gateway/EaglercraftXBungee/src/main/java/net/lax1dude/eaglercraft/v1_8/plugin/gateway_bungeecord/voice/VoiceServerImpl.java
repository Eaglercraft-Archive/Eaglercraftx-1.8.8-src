package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.voice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.lax1dude.eaglercraft.v1_8.plugin.backend_rpc_protocol.pkt.server.SPacketRPCEventToggledVoice;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.EnumVoiceState;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.event.EaglercraftVoiceStatusChangeEvent;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.backend_rpc_protocol.EnumSubscribedEvent;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.*;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.config.ServerInfo;

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

	private final Map<UUID, UserConnection> voicePlayers = new HashMap<>();
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

	public void handlePlayerLoggedIn(UserConnection player) {
		EaglerInitialHandler eaglerHandler = (EaglerInitialHandler)player.getPendingConnection();
		eaglerHandler.sendEaglerMessage(iceServersPacket);
		eaglerHandler.fireVoiceStateChange(EaglercraftVoiceStatusChangeEvent.EnumVoiceState.DISABLED);
		if(eaglerHandler.getRPCEventSubscribed(EnumSubscribedEvent.TOGGLE_VOICE)) {
			eaglerHandler.getRPCSessionHandler().handleVoiceStateTransition(SPacketRPCEventToggledVoice.VOICE_STATE_DISABLED);
		}
	}

	public void handlePlayerLoggedOut(UserConnection player) {
		EaglerInitialHandler eaglerHandler = (EaglerInitialHandler)player.getPendingConnection();
		removeUser(eaglerHandler.getUniqueId());
	}

	void handleVoiceSignalPacketTypeRequest(UUID player, UserConnection sender) {
		UUID senderUUID;
		EaglerInitialHandler targetInitialHandler;
		EaglerInitialHandler senderInitialHandler;
		synchronized (voicePlayers) {
			senderUUID = sender.getUniqueId();
			if (senderUUID.equals(player))
				return; // prevent duplicates
			if (!voicePlayers.containsKey(senderUUID))
				return;
			UserConnection targetPlayerCon = voicePlayers.get(player);
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
				targetInitialHandler = (EaglerInitialHandler) targetPlayerCon.getPendingConnection();
				senderInitialHandler = (EaglerInitialHandler) sender.getPendingConnection();
			}else {
				return;
			}
		}
		if (targetInitialHandler.getEaglerProtocol().ver <= 3) {
			targetInitialHandler.sendEaglerMessage(new SPacketVoiceSignalConnectV3EAG(
					senderUUID.getMostSignificantBits(), senderUUID.getLeastSignificantBits(), false, false));
		} else {
			targetInitialHandler.sendEaglerMessage(new SPacketVoiceSignalConnectV4EAG(
					senderUUID.getMostSignificantBits(), senderUUID.getLeastSignificantBits(), false));
		}
		if (senderInitialHandler.getEaglerProtocol().ver <= 3) {
			senderInitialHandler.sendEaglerMessage(new SPacketVoiceSignalConnectV3EAG(
					player.getMostSignificantBits(), player.getLeastSignificantBits(), false, true));
		} else {
			senderInitialHandler.sendEaglerMessage(new SPacketVoiceSignalConnectV4EAG(
					player.getMostSignificantBits(), player.getLeastSignificantBits(), true));
		}
	}

	void handleVoiceSignalPacketTypeConnect(UserConnection sender) {
		EaglerInitialHandler eaglerHandler = (EaglerInitialHandler)sender.getPendingConnection();
		if(!eaglerHandler.voiceConnectRateLimiter.rateLimit(VOICE_CONNECT_RATELIMIT)) {
			return;
		}
		eaglerHandler.fireVoiceStateChange(EaglercraftVoiceStatusChangeEvent.EnumVoiceState.ENABLED);
		if(eaglerHandler.getRPCEventSubscribed(EnumSubscribedEvent.TOGGLE_VOICE)) {
			eaglerHandler.getRPCSessionHandler().handleVoiceStateTransition(SPacketRPCEventToggledVoice.VOICE_STATE_ENABLED);
		}
		UUID senderUuid = eaglerHandler.getUniqueId();
		List<UserConnection> lst;
		synchronized (voicePlayers) {
			if (voicePlayers.containsKey(senderUuid)) {
				return;
			}
			boolean hasNoOtherPlayers = voicePlayers.isEmpty();
			voicePlayers.put(senderUuid, sender);
			if (hasNoOtherPlayers) {
				return;
			}
			lst = new ArrayList<>(voicePlayers.values());
		}
		GameMessagePacket v3p = null;
		GameMessagePacket v4p = null;
		for (UserConnection conn : lst) {
			EaglerInitialHandler handler = (EaglerInitialHandler) conn.getPendingConnection();
			if (handler.getEaglerProtocol().ver <= 3) {
				handler.sendEaglerMessage(
						v3p == null
								? (v3p = new SPacketVoiceSignalConnectV3EAG(senderUuid.getMostSignificantBits(),
										senderUuid.getLeastSignificantBits(), true, false))
								: v3p);
			} else {
				handler.sendEaglerMessage(
						v4p == null
								? (v4p = new SPacketVoiceSignalConnectAnnounceV4EAG(senderUuid.getMostSignificantBits(),
										senderUuid.getLeastSignificantBits()))
								: v4p);
			}
		}
		Collection<SPacketVoiceSignalGlobalEAG.UserData> userDatas = new ArrayList<>(lst.size());
		for(UserConnection userCon : lst) {
			UUID uuid = userCon.getUniqueId();
			userDatas.add(new SPacketVoiceSignalGlobalEAG.UserData(uuid.getMostSignificantBits(),
					uuid.getLeastSignificantBits(), userCon.getDisplayName()));
		}
		GameMessagePacket packetToBroadcast = new SPacketVoiceSignalGlobalEAG(userDatas);
		for (UserConnection userCon : lst) {
			((EaglerInitialHandler)userCon.getPendingConnection()).sendEaglerMessage(packetToBroadcast);
		}
	}

	void handleVoiceSignalPacketTypeICE(UUID player, byte[] str, UserConnection sender) {
		UserConnection pass;
		VoicePair pair = new VoicePair(player, sender.getUniqueId());
		synchronized (voicePlayers) {
			pass = voicePairs.contains(pair) ? voicePlayers.get(player) : null;
		}
		if (pass != null) {
			UUID uuid = sender.getUniqueId();
			((EaglerInitialHandler) pass.getPendingConnection()).sendEaglerMessage(
					new SPacketVoiceSignalICEEAG(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), str));
		}
	}

	void handleVoiceSignalPacketTypeDesc(UUID player, byte[] str, UserConnection sender) {
		UserConnection pass;
		VoicePair pair = new VoicePair(player, sender.getUniqueId());
		synchronized (voicePlayers) {
			pass = voicePairs.contains(pair) ? voicePlayers.get(player) : null;
		}
		if (pass != null) {
			UUID uuid = sender.getUniqueId();
			((EaglerInitialHandler) pass.getPendingConnection()).sendEaglerMessage(
					new SPacketVoiceSignalDescEAG(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), str));
		}
	}

	void handleVoiceSignalPacketTypeDisconnect(UserConnection sender) {
		removeUser(sender.getUniqueId());
	}

	void handleVoiceSignalPacketTypeDisconnectPeer(UUID player, UserConnection sender) {
		List<Runnable> peersToDisconnect = new ArrayList<>();
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
					final UserConnection conn = voicePlayers.get(target);
					final UUID target2 = target;
					peersToDisconnect.add(() -> {
						if (conn != null) {
							((EaglerInitialHandler) conn.getPendingConnection()).sendEaglerMessage(
									new SPacketVoiceSignalDisconnectPeerEAG(player.getMostSignificantBits(),
											player.getLeastSignificantBits()));
						}
						((EaglerInitialHandler) sender.getPendingConnection())
								.sendEaglerMessage(new SPacketVoiceSignalDisconnectPeerEAG(target2.getMostSignificantBits(),
										target2.getLeastSignificantBits()));
					});
				}
			}
		}
		for(Runnable r : peersToDisconnect) {
			r.run();
		}
	}

	void removeUser(final UUID user) {
		List<Runnable> peersToDisconnect;
		synchronized (voicePlayers) {
			final UserConnection connRemove;
			if ((connRemove = voicePlayers.remove(user)) == null) {
				return;
			}
			peersToDisconnect = new ArrayList<>();
			if(connRemove != null) {
				peersToDisconnect.add(() -> {
					EaglerInitialHandler eaglerHandler = (EaglerInitialHandler)connRemove.getPendingConnection();
					eaglerHandler.fireVoiceStateChange(EaglercraftVoiceStatusChangeEvent.EnumVoiceState.DISABLED);
					if(eaglerHandler.getRPCEventSubscribed(EnumSubscribedEvent.TOGGLE_VOICE)) {
						eaglerHandler.getRPCSessionHandler().handleVoiceStateTransition(SPacketRPCEventToggledVoice.VOICE_STATE_DISABLED);
					}
				});
			}
			voiceRequests.remove(user);
			if (voicePlayers.size() > 0) {
				Collection<SPacketVoiceSignalGlobalEAG.UserData> userDatas = new ArrayList<>(voicePlayers.size());
				for(UserConnection userCon : voicePlayers.values()) {
					UUID uuid = userCon.getUniqueId();
					userDatas.add(new SPacketVoiceSignalGlobalEAG.UserData(uuid.getMostSignificantBits(),
							uuid.getLeastSignificantBits(), userCon.getDisplayName()));
				}
				final GameMessagePacket voicePlayersPkt = new SPacketVoiceSignalGlobalEAG(userDatas);
				for (final UserConnection userCon : voicePlayers.values()) {
					if (!user.equals(userCon.getUniqueId())) {
						peersToDisconnect.add(() -> {
							((EaglerInitialHandler)userCon.getPendingConnection()).sendEaglerMessage(voicePlayersPkt);
						});
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
						final UserConnection conn = voicePlayers.get(target);
						if (conn != null) {
							peersToDisconnect.add(() -> {
								((EaglerInitialHandler) conn.getPendingConnection()).sendEaglerMessage(
										new SPacketVoiceSignalDisconnectPeerEAG(user.getMostSignificantBits(),
												user.getLeastSignificantBits()));
							});
						}
					}
				}
			}
		}
		for(Runnable r : peersToDisconnect) {
			r.run();
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
