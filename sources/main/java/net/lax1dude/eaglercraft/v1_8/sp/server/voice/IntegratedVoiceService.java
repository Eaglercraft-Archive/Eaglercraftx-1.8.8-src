/*
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

package net.lax1dude.eaglercraft.v1_8.sp.server.voice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.*;
import net.lax1dude.eaglercraft.v1_8.voice.ExpiringSet;
import net.minecraft.entity.player.EntityPlayerMP;

//TODO: Rewrite to be more like EaglerXServer
public class IntegratedVoiceService {

	public static final Logger logger = LogManager.getLogger("IntegratedVoiceService");

	private GameMessagePacket iceServersPacket;

	private final Map<EaglercraftUUID, EntityPlayerMP> voicePlayers = new HashMap<>();
	private final Map<EaglercraftUUID, ExpiringSet<EaglercraftUUID>> voiceRequests = new HashMap<>();
	private final Set<VoicePair> voicePairs = new HashSet<>();

	public IntegratedVoiceService(String[] iceServers) {
		iceServersPacket = new SPacketVoiceSignalAllowedEAG(true, iceServers);
	}

	public void changeICEServers(String[] iceServers) {
		iceServersPacket = new SPacketVoiceSignalAllowedEAG(true, iceServers);
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
		player.playerNetServerHandler.sendEaglerMessage(iceServersPacket);
	}

	public void handlePlayerLoggedOut(EntityPlayerMP player) {
		removeUser(player.getUniqueID());
	}

	public void handleVoiceSignalPacketTypeRequest(EaglercraftUUID player, EntityPlayerMP sender) {
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
			if(targetPlayerCon.playerNetServerHandler.getEaglerMessageProtocol().ver <= 3) {
				targetPlayerCon.playerNetServerHandler
						.sendEaglerMessage(new SPacketVoiceSignalConnectV3EAG(senderUUID.msb, senderUUID.lsb, false, false));
			}else {
				targetPlayerCon.playerNetServerHandler
						.sendEaglerMessage(new SPacketVoiceSignalConnectV4EAG(senderUUID.msb, senderUUID.lsb, false));
			}
			if(sender.playerNetServerHandler.getEaglerMessageProtocol().ver <= 3) {
				sender.playerNetServerHandler
						.sendEaglerMessage(new SPacketVoiceSignalConnectV3EAG(player.msb, player.lsb, false, true));
			}else {
				sender.playerNetServerHandler
						.sendEaglerMessage(new SPacketVoiceSignalConnectV4EAG(player.msb, player.lsb, true));
			}
		}
	}

	public void handleVoiceSignalPacketTypeConnect(EntityPlayerMP sender) {
		EaglercraftUUID senderUuid = sender.getUniqueID();
		if (voicePlayers.containsKey(senderUuid)) {
			return;
		}
		boolean hasNoOtherPlayers = voicePlayers.isEmpty();
		voicePlayers.put(senderUuid, sender);
		if (hasNoOtherPlayers) {
			return;
		}
		GameMessagePacket v3p = null;
		GameMessagePacket v4p = null;
		Collection<SPacketVoiceSignalGlobalEAG.UserData> userDatas = new ArrayList<>(voicePlayers.size());
		for(EntityPlayerMP conn : voicePlayers.values()) {
			EaglercraftUUID otherUuid = conn.getUniqueID();
			if(conn != sender) {
				if(conn.playerNetServerHandler.getEaglerMessageProtocol().ver <= 3) {
					conn.playerNetServerHandler.sendEaglerMessage(v3p == null ? (v3p = new SPacketVoiceSignalConnectV3EAG(senderUuid.msb, senderUuid.lsb, true, false)) : v3p);
				} else {
					conn.playerNetServerHandler.sendEaglerMessage(v4p == null ? (v4p = new SPacketVoiceSignalConnectAnnounceV4EAG(senderUuid.msb, senderUuid.lsb)) : v4p);
				}
			}
			userDatas.add(new SPacketVoiceSignalGlobalEAG.UserData(otherUuid.msb, otherUuid.lsb, conn.getName()));
		}
		SPacketVoiceSignalGlobalEAG packetToBroadcast = new SPacketVoiceSignalGlobalEAG(userDatas);
		for (EntityPlayerMP userCon : voicePlayers.values()) {
			userCon.playerNetServerHandler.sendEaglerMessage(packetToBroadcast);
		}
		boolean selfV3 = sender.playerNetServerHandler.getEaglerMessageProtocol().ver <= 3;
		for(EntityPlayerMP conn : voicePlayers.values()) {
			EaglercraftUUID otherUuid = conn.getUniqueID();
			if(conn != sender) {
				if(selfV3) {
					sender.playerNetServerHandler.sendEaglerMessage(new SPacketVoiceSignalConnectV3EAG(otherUuid.msb, otherUuid.lsb, true, false));
				}else {
					sender.playerNetServerHandler.sendEaglerMessage(new SPacketVoiceSignalConnectAnnounceV4EAG(otherUuid.msb, otherUuid.lsb));
				}
			}
		}
	}

	public void handleVoiceSignalPacketTypeICE(EaglercraftUUID player, byte[] str, EntityPlayerMP sender) {
		EaglercraftUUID uuid = sender.getUniqueID();
		VoicePair pair = new VoicePair(player, uuid);
		EntityPlayerMP pass = voicePairs.contains(pair) ? voicePlayers.get(player) : null;
		if (pass != null) {
			pass.playerNetServerHandler.sendEaglerMessage(new SPacketVoiceSignalICEEAG(uuid.msb, uuid.lsb, str));
		}
	}

	public void handleVoiceSignalPacketTypeDesc(EaglercraftUUID player, byte[] str, EntityPlayerMP sender) {
		EaglercraftUUID uuid = sender.getUniqueID();
		VoicePair pair = new VoicePair(player, uuid);
		EntityPlayerMP pass = voicePairs.contains(pair) ? voicePlayers.get(player) : null;
		if (pass != null) {
			pass.playerNetServerHandler.sendEaglerMessage(new SPacketVoiceSignalDescEAG(uuid.msb, uuid.lsb, str));
		}
	}

	public void handleVoiceSignalPacketTypeDisconnect(EntityPlayerMP sender) {
		removeUser(sender.getUniqueID());
	}

	public void handleVoiceSignalPacketTypeDisconnectPeer(EaglercraftUUID player, EntityPlayerMP sender) {
		if (!voicePlayers.containsKey(player)) {
			return;
		}
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
					conn.playerNetServerHandler.sendEaglerMessage(new SPacketVoiceSignalDisconnectPeerEAG(player.msb, player.lsb));
				}
				sender.playerNetServerHandler.sendEaglerMessage(new SPacketVoiceSignalDisconnectPeerEAG(target.msb, target.lsb));
			}
		}
	}

	public void removeUser(EaglercraftUUID user) {
		if (voicePlayers.remove(user) == null) {
			return;
		}
		voiceRequests.remove(user);
		if (voicePlayers.size() > 0) {
			Collection<SPacketVoiceSignalGlobalEAG.UserData> userDatas = new ArrayList<>(voicePlayers.size());
			for(EntityPlayerMP player : voicePlayers.values()) {
				EaglercraftUUID uuid = player.getUniqueID();
				userDatas.add(new SPacketVoiceSignalGlobalEAG.UserData(uuid.msb, uuid.lsb, player.getName()));
			}
			SPacketVoiceSignalGlobalEAG packetToBroadcast = new SPacketVoiceSignalGlobalEAG(userDatas);
			for (EntityPlayerMP userCon : voicePlayers.values()) {
				userCon.playerNetServerHandler.sendEaglerMessage(packetToBroadcast);
			}
		}
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
						conn.playerNetServerHandler.sendEaglerMessage(new SPacketVoiceSignalDisconnectPeerEAG(user.msb, user.lsb));
					}
				}
			}
		}
	}

}