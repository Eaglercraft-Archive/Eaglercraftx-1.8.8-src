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

package net.lax1dude.eaglercraft.v1_8.voice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformVoiceClient;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.*;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketVoiceSignalGlobalEAG;
import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerController;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class VoiceClientController {

	public static final String SIGNAL_CHANNEL = "EAG|Voice-1.8";

	static final Logger logger = LogManager.getLogger("VoiceClientController");

	private static boolean clientSupport = false;
	private static boolean serverSupport = false;
	private static Consumer<GameMessagePacket> packetSendCallback = null;
	private static int protocolVersion = -1;
	private static EnumVoiceChannelType voiceChannel = EnumVoiceChannelType.NONE;
	private static final HashSet<EaglercraftUUID> nearbyPlayers = new HashSet<>();
	private static final ExpiringSet<EaglercraftUUID> recentlyNearbyPlayers = new ExpiringSet<>(5000, uuid -> {
		if (!nearbyPlayers.contains(uuid)) {
			PlatformVoiceClient.signalDisconnect(uuid, false);
		}
	});
	private static final Map<EaglercraftUUID, String> uuidToNameLookup = new HashMap<>(256);

	public static boolean isSupported() {
		return isClientSupported() && isServerSupported();
	}

	private static boolean checked = false;

	public static boolean isClientSupported() {
		if (!checked) {
			checked = true;
			clientSupport = EagRuntime.getConfiguration().isAllowVoiceClient() && PlatformVoiceClient.isSupported();
		}
		return clientSupport;
	}

	public static boolean isServerSupported() {
		return serverSupport;
	}

	public static void initializeVoiceClient(Consumer<GameMessagePacket> signalSendCallbackIn, int proto) {
		packetSendCallback = signalSendCallbackIn;
		protocolVersion = proto;
		uuidToNameLookup.clear();
		if (getVoiceChannel() != EnumVoiceChannelType.NONE) sendInitialVoice();
	}

	public static void handleVoiceSignalPacketTypeGlobal(EaglercraftUUID[] voicePlayers, String[] voiceNames) {
		uuidToNameLookup.clear();
		for (int i = 0; i < voicePlayers.length; i++) {
			if(voiceNames != null) {
				uuidToNameLookup.put(voicePlayers[i], voiceNames[i]);
			}
			sendPacketRequestIfNeeded(voicePlayers[i]);
		}
	}

	public static void handleVoiceSignalPacketTypeGlobalNew(Collection<SPacketVoiceSignalGlobalEAG.UserData> voicePlayers) {
		boolean isGlobal = voiceChannel == EnumVoiceChannelType.GLOBAL;
		uuidToNameLookup.clear();
		for (SPacketVoiceSignalGlobalEAG.UserData player : voicePlayers) {
			EaglercraftUUID uuid = new EaglercraftUUID(player.uuidMost, player.uuidLeast);
			if(player.username != null) {
				uuidToNameLookup.put(uuid, player.username);
			}
			if (isGlobal) {
				sendPacketRequestIfNeeded(uuid);
			}
		}
	}

	public static void handleServerDisconnect() {
		if(!isClientSupported()) return;
		serverSupport = false;
		uuidToNameLookup.clear();
		for (EaglercraftUUID uuid : nearbyPlayers) {
			PlatformVoiceClient.signalDisconnect(uuid, false);
		}
		for (EaglercraftUUID uuid : recentlyNearbyPlayers) {
			PlatformVoiceClient.signalDisconnect(uuid, false);
		}
		nearbyPlayers.clear();
		recentlyNearbyPlayers.clear();
		Set<EaglercraftUUID> antiConcurrentModificationUUIDs = new HashSet<>(listeningSet);
		for (EaglercraftUUID uuid : antiConcurrentModificationUUIDs) {
			PlatformVoiceClient.signalDisconnect(uuid, false);
		}
		activateVoice(false);
	}

	public static void handleVoiceSignalPacketTypeAllowed(boolean voiceAvailableStat, String[] servs) {
		serverSupport = voiceAvailableStat;
		PlatformVoiceClient.setICEServers(servs);
		if(isSupported()) {
			EnumVoiceChannelType ch = getVoiceChannel();
	        setVoiceChannel(EnumVoiceChannelType.NONE);
	        setVoiceChannel(ch);
		}
	}

	public static void handleVoiceSignalPacketTypeConnect(EaglercraftUUID user, boolean offer) {
		if (voiceChannel != EnumVoiceChannelType.NONE) PlatformVoiceClient.signalConnect(user, offer);
	}

	public static void handleVoiceSignalPacketTypeConnectAnnounce(EaglercraftUUID user) {
		if (voiceChannel != EnumVoiceChannelType.NONE && (voiceChannel == EnumVoiceChannelType.GLOBAL || listeningSet.contains(user))) sendPacketRequest(user);
	}

	public static void handleVoiceSignalPacketTypeDisconnect(EaglercraftUUID user) {
		if (voiceChannel != EnumVoiceChannelType.NONE) PlatformVoiceClient.signalDisconnect(user, true);
	}

	public static void handleVoiceSignalPacketTypeICECandidate(EaglercraftUUID user, String ice) {
		if (voiceChannel != EnumVoiceChannelType.NONE) PlatformVoiceClient.signalICECandidate(user, ice);
	}

	public static void handleVoiceSignalPacketTypeDescription(EaglercraftUUID user, String desc) {
		if (voiceChannel != EnumVoiceChannelType.NONE) PlatformVoiceClient.signalDescription(user, desc);
	}

	public static void tickVoiceClient(Minecraft mc) {
		if(!isClientSupported()) return;
		recentlyNearbyPlayers.checkForExpirations();
		speakingSet.clear();
		PlatformVoiceClient.tickVoiceClient();

		if (getVoiceChannel() != EnumVoiceChannelType.NONE && (getVoiceStatus() == EnumVoiceChannelStatus.CONNECTING || getVoiceStatus() == EnumVoiceChannelStatus.CONNECTED)) {
			activateVoice((mc.currentScreen == null || !mc.currentScreen.blockPTTKey()) && Keyboard.isKeyDown(mc.gameSettings.voicePTTKey));

			if(mc.isSingleplayer() && !LANServerController.isHostingLAN()) {
				setVoiceChannel(EnumVoiceChannelType.NONE);
				return;
			}

			if (mc.theWorld != null && mc.thePlayer != null) {
				HashSet<EaglercraftUUID> seenPlayers = new HashSet<>();
				for (EntityPlayer player : mc.theWorld.playerEntities) {
					if (player == mc.thePlayer) continue;
					if (getVoiceChannel() == EnumVoiceChannelType.PROXIMITY) updateVoicePosition(player.getUniqueID(), player.posX, player.posY + player.getEyeHeight(), player.posZ);
					int prox = 22;
					// cube
					if (Math.abs(mc.thePlayer.posX - player.posX) <= prox && Math.abs(mc.thePlayer.posY - player.posY) <= prox && Math.abs(mc.thePlayer.posZ - player.posZ) <= prox) {
						if (!uuidToNameLookup.containsKey(player.getUniqueID())) {
							uuidToNameLookup.put(player.getUniqueID(), player.getName());
						}
						if (addNearbyPlayer(player.getUniqueID())) {
							seenPlayers.add(player.getUniqueID());
						}
					}
				}
				cleanupNearbyPlayers(seenPlayers);
			}
		}
	}

	public static final boolean addNearbyPlayer(EaglercraftUUID uuid) {
		recentlyNearbyPlayers.remove(uuid);
		if (nearbyPlayers.add(uuid)) {
			sendPacketRequestIfNeeded(uuid);
			return true;
		}
		return false;
	}

	public static final void removeNearbyPlayer(EaglercraftUUID uuid) {
		if (nearbyPlayers.remove(uuid)) {
			if (getVoiceStatus() == EnumVoiceChannelStatus.DISCONNECTED || getVoiceStatus() == EnumVoiceChannelStatus.UNAVAILABLE) return;
			if (voiceChannel == EnumVoiceChannelType.PROXIMITY) recentlyNearbyPlayers.add(uuid);
		}
	}

	public static final void cleanupNearbyPlayers(HashSet<EaglercraftUUID> existingPlayers) {
		nearbyPlayers.stream().filter(ud -> !existingPlayers.contains(ud)).collect(Collectors.toSet()).forEach(VoiceClientController::removeNearbyPlayer);
	}

	public static final void updateVoicePosition(EaglercraftUUID uuid, double x, double y, double z) {
		PlatformVoiceClient.updateVoicePosition(uuid, x, y, z);
	}

	public static void setVoiceChannel(EnumVoiceChannelType channel) {
		if (voiceChannel == channel) return;
		if (channel != EnumVoiceChannelType.NONE) PlatformVoiceClient.initializeDevices();
		if (channel == EnumVoiceChannelType.NONE) {
			for (EaglercraftUUID uuid : nearbyPlayers) {
				PlatformVoiceClient.signalDisconnect(uuid, false);
			}
			for (EaglercraftUUID uuid : recentlyNearbyPlayers) {
				PlatformVoiceClient.signalDisconnect(uuid, false);
			}
			nearbyPlayers.clear();
			recentlyNearbyPlayers.clear();
			Set<EaglercraftUUID> antiConcurrentModificationUUIDs = new HashSet<>(listeningSet);
			for (EaglercraftUUID uuid : antiConcurrentModificationUUIDs) {
				PlatformVoiceClient.signalDisconnect(uuid, false);
			}
			sendPacketDisconnect();
			activateVoice(false);
		} else if (voiceChannel == EnumVoiceChannelType.PROXIMITY) {
			for (EaglercraftUUID uuid : nearbyPlayers) {
				PlatformVoiceClient.signalDisconnect(uuid, false);
			}
			for (EaglercraftUUID uuid : recentlyNearbyPlayers) {
				PlatformVoiceClient.signalDisconnect(uuid, false);
			}
			nearbyPlayers.clear();
			recentlyNearbyPlayers.clear();
			sendPacketDisconnect();
		} else if(voiceChannel == EnumVoiceChannelType.GLOBAL) {
			Set<EaglercraftUUID> antiConcurrentModificationUUIDs = new HashSet<>(listeningSet);
			antiConcurrentModificationUUIDs.removeAll(nearbyPlayers);
			antiConcurrentModificationUUIDs.removeAll(recentlyNearbyPlayers);
			for (EaglercraftUUID uuid : antiConcurrentModificationUUIDs) {
				PlatformVoiceClient.signalDisconnect(uuid, false);
			}
			sendPacketDisconnect();
		}
		voiceChannel = channel;
		if (channel != EnumVoiceChannelType.NONE) {
			sendInitialVoice();
		}
	}

	public static void sendInitialVoice() {
		sendPacketConnect();
		for (EaglercraftUUID uuid : nearbyPlayers) {
			sendPacketRequest(uuid);
		}
	}

	public static EnumVoiceChannelType getVoiceChannel() {
		return voiceChannel;
	}

	public static EnumVoiceChannelStatus getVoiceStatus() {
		return (!isClientSupported() || !isServerSupported()) ? EnumVoiceChannelStatus.UNAVAILABLE :
				(PlatformVoiceClient.getReadyState() != EnumVoiceChannelReadyState.DEVICE_INITIALIZED ?
						EnumVoiceChannelStatus.CONNECTING : EnumVoiceChannelStatus.CONNECTED);
	}

	private static boolean talkStatus = false;

	public static void activateVoice(boolean talk) {
		if (talkStatus != talk) {
			PlatformVoiceClient.activateVoice(talk);
			talkStatus = talk;
		}
	}

	private static int proximity = 16;

	public static void setVoiceProximity(int prox) {
		PlatformVoiceClient.setVoiceProximity(prox);
		proximity = prox;
	}

	public static int getVoiceProximity() {
		return proximity;
	}

	private static float volumeListen = 0.5f;

	public static void setVoiceListenVolume(float f) {
		PlatformVoiceClient.setVoiceListenVolume(f);
		volumeListen = f;
	}

	public static float getVoiceListenVolume() {
		return volumeListen;
	}

	private static float volumeSpeak = 0.5f;

	public static void setVoiceSpeakVolume(float f) {
		if (volumeSpeak != f) {
			PlatformVoiceClient.setMicVolume(f);
		}
		volumeSpeak = f;
	}

	public static float getVoiceSpeakVolume() {
		return volumeSpeak;
	}

	private static final Set<EaglercraftUUID> listeningSet = new HashSet<>();
	private static final Set<EaglercraftUUID> speakingSet = new HashSet<>();
	private static final Set<EaglercraftUUID> mutedSet = new HashSet<>();

	public static Set<EaglercraftUUID> getVoiceListening() {
		return listeningSet;
	}

	public static Set<EaglercraftUUID> getVoiceSpeaking() {
		return speakingSet;
	}

	public static void setVoiceMuted(EaglercraftUUID uuid, boolean mute) {
		PlatformVoiceClient.mutePeer(uuid, mute);
		if (mute) {
			mutedSet.add(uuid);
		} else {
			mutedSet.remove(uuid);
		}
	}

	public static Set<EaglercraftUUID> getVoiceMuted() {
		return mutedSet;
	}

	public static List<EaglercraftUUID> getVoiceRecent() {
		return new ArrayList<>(listeningSet);
	}

	public static String getVoiceUsername(EaglercraftUUID uuid) {
		if(uuid == null) {
			return "null";
		}
		String ret = uuidToNameLookup.get(uuid);
		return ret == null ? uuid.toString() : ret;
	}

	public static void sendPacketICE(EaglercraftUUID peerId, String candidate) {
		if(packetSendCallback != null) {
			packetSendCallback.accept(new CPacketVoiceSignalICEEAG(peerId.msb, peerId.lsb, candidate));
		}
	}

	public static void sendPacketDesc(EaglercraftUUID peerId, String desc) {
		if(packetSendCallback != null) {
			packetSendCallback.accept(new CPacketVoiceSignalDescEAG(peerId.msb, peerId.lsb, desc));
		}
	}

	public static void sendPacketDisconnect() {
		if(packetSendCallback != null) {
			if(protocolVersion <= 3) {
				packetSendCallback.accept(new CPacketVoiceSignalDisconnectV3EAG());
			}else {
				packetSendCallback.accept(new CPacketVoiceSignalDisconnectV4EAG());
			}
		}
	}

	public static void sendPacketDisconnectPeer(EaglercraftUUID peerId) {
		if(packetSendCallback != null) {
			if(protocolVersion <= 3) {
				packetSendCallback.accept(new CPacketVoiceSignalDisconnectV3EAG(true, peerId.msb, peerId.lsb));
			}else {
				packetSendCallback.accept(new CPacketVoiceSignalDisconnectPeerV4EAG(peerId.msb, peerId.lsb));
			}
		}
	}

	public static void sendPacketConnect() {
		if(packetSendCallback != null) {
			packetSendCallback.accept(new CPacketVoiceSignalConnectEAG());
		}
	}

	public static void sendPacketRequest(EaglercraftUUID peerId) {
		if(packetSendCallback != null) {
			packetSendCallback.accept(new CPacketVoiceSignalRequestEAG(peerId.msb, peerId.lsb));
		}
	}

	private static void sendPacketRequestIfNeeded(EaglercraftUUID uuid) {
		if (getVoiceStatus() == EnumVoiceChannelStatus.DISCONNECTED || getVoiceStatus() == EnumVoiceChannelStatus.UNAVAILABLE) return;
		if(uuid.equals(EaglerProfile.getPlayerUUID())) return;
		if (!getVoiceListening().contains(uuid)) sendPacketRequest(uuid);
	}
}