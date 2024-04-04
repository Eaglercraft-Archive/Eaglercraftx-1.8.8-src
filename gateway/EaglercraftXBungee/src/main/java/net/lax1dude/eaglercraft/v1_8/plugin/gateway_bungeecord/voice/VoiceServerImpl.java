package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.voice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
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
	private final byte[] iceServersPacket;

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

	VoiceServerImpl(ServerInfo server, byte[] iceServersPacket) {
		this.server = server;
		this.iceServersPacket = iceServersPacket;
	}

	public void handlePlayerLoggedIn(UserConnection player) {
		player.sendData(VoiceService.CHANNEL, iceServersPacket);
	}

	public void handlePlayerLoggedOut(UserConnection player) {
		removeUser(player.getUniqueId());
	}

	void handleVoiceSignalPacketTypeRequest(UUID player, UserConnection sender) {
		synchronized (voicePlayers) {
			UUID senderUUID = sender.getUniqueId();
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
				targetPlayerCon.sendData(VoiceService.CHANNEL,
						VoiceSignalPackets.makeVoiceSignalPacketConnect(senderUUID, false));
				sender.sendData(VoiceService.CHANNEL, VoiceSignalPackets.makeVoiceSignalPacketConnect(player, true));
			}
		}
	}

	void handleVoiceSignalPacketTypeConnect(UserConnection sender) {
		if(!((EaglerInitialHandler)sender.getPendingConnection()).voiceConnectRateLimiter.rateLimit(VOICE_CONNECT_RATELIMIT)) {
			return;
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
			byte[] packetToBroadcast = VoiceSignalPackets.makeVoiceSignalPacketGlobal(voicePlayers.values());
			for (UserConnection userCon : voicePlayers.values()) {
				userCon.sendData(VoiceService.CHANNEL, packetToBroadcast);
			}
		}
	}

	void handleVoiceSignalPacketTypeICE(UUID player, String str, UserConnection sender) {
		UserConnection pass;
		VoicePair pair = new VoicePair(player, sender.getUniqueId());
		synchronized (voicePlayers) {
			pass = voicePairs.contains(pair) ? voicePlayers.get(player) : null;
		}
		if (pass != null) {
			pass.sendData(VoiceService.CHANNEL, VoiceSignalPackets.makeVoiceSignalPacketICE(sender.getUniqueId(), str));
		}
	}

	void handleVoiceSignalPacketTypeDesc(UUID player, String str, UserConnection sender) {
		UserConnection pass;
		VoicePair pair = new VoicePair(player, sender.getUniqueId());
		synchronized (voicePlayers) {
			pass = voicePairs.contains(pair) ? voicePlayers.get(player) : null;
		}
		if (pass != null) {
			pass.sendData(VoiceService.CHANNEL,
					VoiceSignalPackets.makeVoiceSignalPacketDesc(sender.getUniqueId(), str));
		}
	}

	void handleVoiceSignalPacketTypeDisconnect(UUID player, UserConnection sender) {
		if (player != null) {
			synchronized (voicePlayers) {
				if (!voicePlayers.containsKey(player)) {
					return;
				}
				byte[] userDisconnectPacket = null;
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
						UserConnection conn = voicePlayers.get(target);
						if (conn != null) {
							if (userDisconnectPacket == null) {
								userDisconnectPacket = VoiceSignalPackets.makeVoiceSignalPacketDisconnect(player);
							}
							conn.sendData(VoiceService.CHANNEL, userDisconnectPacket);
						}
						sender.sendData(VoiceService.CHANNEL,
								VoiceSignalPackets.makeVoiceSignalPacketDisconnect(target));
					}
				}
			}
		} else {
			removeUser(sender.getUniqueId());
		}
	}

	public void removeUser(UUID user) {
		synchronized (voicePlayers) {
			if (voicePlayers.remove(user) == null) {
				return;
			}
			voiceRequests.remove(user);
			if (voicePlayers.size() > 0) {
				byte[] voicePlayersPkt = VoiceSignalPackets.makeVoiceSignalPacketGlobal(voicePlayers.values());
				for (UserConnection userCon : voicePlayers.values()) {
					if (!user.equals(userCon.getUniqueId())) {
						userCon.sendData(VoiceService.CHANNEL, voicePlayersPkt);
					}
				}
			}
			byte[] userDisconnectPacket = null;
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
						UserConnection conn = voicePlayers.get(target);
						if (conn != null) {
							if (userDisconnectPacket == null) {
								userDisconnectPacket = VoiceSignalPackets.makeVoiceSignalPacketDisconnect(user);
							}
							conn.sendData(VoiceService.CHANNEL, userDisconnectPacket);
						}
					}
				}
			}
		}
	}

}
