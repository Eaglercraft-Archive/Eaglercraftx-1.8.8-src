package net.lax1dude.eaglercraft.v1_8.sp.server.voice;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
import net.lax1dude.eaglercraft.v1_8.voice.ExpiringSet;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;

/**
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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
public class IntegratedVoiceService {

	public static final Logger logger = LogManager.getLogger("IntegratedVoiceService");

	public static final String CHANNEL = "EAG|Voice-1.8";

	private byte[] iceServersPacket;

	private final Map<EaglercraftUUID, EntityPlayerMP> voicePlayers = new HashMap<>();
	private final Map<EaglercraftUUID, ExpiringSet<EaglercraftUUID>> voiceRequests = new HashMap<>();
	private final Set<VoicePair> voicePairs = new HashSet<>();

	public IntegratedVoiceService(String[] iceServers) {
		iceServersPacket = IntegratedVoiceSignalPackets.makeVoiceSignalPacketAllowed(true, iceServers);
	}

	public void changeICEServers(String[] iceServers) {
		iceServersPacket = IntegratedVoiceSignalPackets.makeVoiceSignalPacketAllowed(true, iceServers);
	}

	private static class VoicePair {

		private final EaglercraftUUID uuid1;
		private final EaglercraftUUID uuid2;

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

		private VoicePair(EaglercraftUUID uuid1, EaglercraftUUID uuid2) {
			this.uuid1 = uuid1;
			this.uuid2 = uuid2;
		}

		private boolean anyEquals(EaglercraftUUID uuid) {
			return uuid1.equals(uuid) || uuid2.equals(uuid);
		}
	}

	public void handlePlayerLoggedIn(EntityPlayerMP player) {
		player.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload(CHANNEL, new PacketBuffer(
				Unpooled.buffer(iceServersPacket, iceServersPacket.length).writerIndex(iceServersPacket.length))));
	}

	public void handlePlayerLoggedOut(EntityPlayerMP player) {
		removeUser(player.getUniqueID());
	}

	public void processPacket(PacketBuffer packetData, EntityPlayerMP sender) {
		try {
			IntegratedVoiceSignalPackets.processPacket(packetData, sender, this);
		} catch (IOException e) {
			logger.error("Invalid voice signal packet recieved from player {}!", sender.getName());
			logger.error(e);
			sender.playerNetServerHandler.kickPlayerFromServer("Invalid voice signal packet recieved!");
		}
	}

	void handleVoiceSignalPacketTypeRequest(EaglercraftUUID player, EntityPlayerMP sender) {
		EaglercraftUUID senderUUID = sender.getUniqueID();
		if (senderUUID.equals(player))
			return; // prevent duplicates
		if (!voicePlayers.containsKey(senderUUID))
			return;
		EntityPlayerMP targetPlayerCon = voicePlayers.get(player);
		if (targetPlayerCon == null)
			return;
		VoicePair newPair = new VoicePair(player, senderUUID);
		if (voicePairs.contains(newPair))
			return; // already paired
		ExpiringSet<EaglercraftUUID> senderRequestSet = voiceRequests.get(senderUUID);
		if (senderRequestSet == null) {
			voiceRequests.put(senderUUID, senderRequestSet = new ExpiringSet<>(2000));
		}
		if (!senderRequestSet.add(player)) {
			return;
		}

		// check if other has requested earlier
		ExpiringSet<EaglercraftUUID> theSet;
		if ((theSet = voiceRequests.get(player)) != null && theSet.contains(senderUUID)) {
			theSet.remove(senderUUID);
			if (theSet.isEmpty())
				voiceRequests.remove(player);
			senderRequestSet.remove(player);
			if (senderRequestSet.isEmpty())
				voiceRequests.remove(senderUUID);
			// send each other add data
			voicePairs.add(newPair);
			targetPlayerCon.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload(CHANNEL,
					IntegratedVoiceSignalPackets.makeVoiceSignalPacketConnect(senderUUID, false)));
			sender.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload(CHANNEL,
					IntegratedVoiceSignalPackets.makeVoiceSignalPacketConnect(player, true)));
		}
	}

	void handleVoiceSignalPacketTypeConnect(EntityPlayerMP sender) {
		if (voicePlayers.containsKey(sender.getUniqueID())) {
			return;
		}
		boolean hasNoOtherPlayers = voicePlayers.isEmpty();
		voicePlayers.put(sender.getUniqueID(), sender);
		if (hasNoOtherPlayers) {
			return;
		}
		byte[] packetToBroadcast = IntegratedVoiceSignalPackets.makeVoiceSignalPacketGlobal(voicePlayers.values());
		for (EntityPlayerMP userCon : voicePlayers.values()) {
			userCon.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload(CHANNEL, new PacketBuffer(Unpooled
					.buffer(packetToBroadcast, packetToBroadcast.length).writerIndex(packetToBroadcast.length))));
		}
	}

	void handleVoiceSignalPacketTypeICE(EaglercraftUUID player, String str, EntityPlayerMP sender) {
		VoicePair pair = new VoicePair(player, sender.getUniqueID());
		EntityPlayerMP pass = voicePairs.contains(pair) ? voicePlayers.get(player) : null;
		if (pass != null) {
			pass.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload(CHANNEL,
					IntegratedVoiceSignalPackets.makeVoiceSignalPacketICE(sender.getUniqueID(), str)));
		}
	}

	void handleVoiceSignalPacketTypeDesc(EaglercraftUUID player, String str, EntityPlayerMP sender) {
		VoicePair pair = new VoicePair(player, sender.getUniqueID());
		EntityPlayerMP pass = voicePairs.contains(pair) ? voicePlayers.get(player) : null;
		if (pass != null) {
			pass.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload(CHANNEL,
					IntegratedVoiceSignalPackets.makeVoiceSignalPacketDesc(sender.getUniqueID(), str)));
		}
	}

	void handleVoiceSignalPacketTypeDisconnect(EaglercraftUUID player, EntityPlayerMP sender) {
		if (player != null) {
			if (!voicePlayers.containsKey(player)) {
				return;
			}
			byte[] userDisconnectPacket = null;
			Iterator<VoicePair> pairsItr = voicePairs.iterator();
			while (pairsItr.hasNext()) {
				VoicePair voicePair = pairsItr.next();
				EaglercraftUUID target = null;
				if (voicePair.uuid1.equals(player)) {
					target = voicePair.uuid2;
				} else if (voicePair.uuid2.equals(player)) {
					target = voicePair.uuid1;
				}
				if (target != null) {
					pairsItr.remove();
					EntityPlayerMP conn = voicePlayers.get(target);
					if (conn != null) {
						if (userDisconnectPacket == null) {
							userDisconnectPacket = IntegratedVoiceSignalPackets.makeVoiceSignalPacketDisconnect(player);
						}
						conn.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload(CHANNEL,
								new PacketBuffer(Unpooled.buffer(userDisconnectPacket, userDisconnectPacket.length)
										.writerIndex(userDisconnectPacket.length))));
					}
					sender.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload(CHANNEL,
							IntegratedVoiceSignalPackets.makeVoiceSignalPacketDisconnectPB(target)));
				}
			}
		} else {
			removeUser(sender.getUniqueID());
		}
	}

	public void removeUser(EaglercraftUUID user) {
		if (voicePlayers.remove(user) == null) {
			return;
		}
		voiceRequests.remove(user);
		if (voicePlayers.size() > 0) {
			byte[] voicePlayersPkt = IntegratedVoiceSignalPackets.makeVoiceSignalPacketGlobal(voicePlayers.values());
			for (EntityPlayerMP userCon : voicePlayers.values()) {
				if (!user.equals(userCon.getUniqueID())) {
					userCon.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload(CHANNEL,
							new PacketBuffer(Unpooled.buffer(voicePlayersPkt, voicePlayersPkt.length)
									.writerIndex(voicePlayersPkt.length))));
				}
			}
		}
		byte[] userDisconnectPacket = null;
		Iterator<VoicePair> pairsItr = voicePairs.iterator();
		while (pairsItr.hasNext()) {
			VoicePair voicePair = pairsItr.next();
			EaglercraftUUID target = null;
			if (voicePair.uuid1.equals(user)) {
				target = voicePair.uuid2;
			} else if (voicePair.uuid2.equals(user)) {
				target = voicePair.uuid1;
			}
			if (target != null) {
				pairsItr.remove();
				if (voicePlayers.size() > 0) {
					EntityPlayerMP conn = voicePlayers.get(target);
					if (conn != null) {
						if (userDisconnectPacket == null) {
							userDisconnectPacket = IntegratedVoiceSignalPackets.makeVoiceSignalPacketDisconnect(user);
						}
						conn.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload(CHANNEL,
								new PacketBuffer(Unpooled.buffer(userDisconnectPacket, userDisconnectPacket.length)
										.writerIndex(userDisconnectPacket.length))));
					}
				}
			}
		}
	}

}
