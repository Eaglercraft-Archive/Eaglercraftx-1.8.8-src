/*
 * Copyright (c) 2022-2025 lax1dude, ayunami2000. All Rights Reserved.
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformVoiceClient;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketVoiceSignalGlobalEAG;
import net.minecraft.client.Minecraft;

public class VoiceClientController {

	static final Logger logger = LogManager.getLogger("VoiceClientController");

	private static boolean clientSupport = false;
	private static boolean serverSupport = false;
	private static Consumer<GameMessagePacket> packetSendCallback = null;
	private static int protocolVersion = -1;
	private static VoiceClientInstance voiceClient = null;

	static EnumVoiceChannelType lastVoiceChannel = EnumVoiceChannelType.NONE;

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
		if (!isClientSupported()) return;
		packetSendCallback = signalSendCallbackIn;
		protocolVersion = proto;
		handleRelease();
		if (signalSendCallbackIn != null && serverSupport) {
			voiceClient = new VoiceClientInstance(proto, signalSendCallbackIn);
			voiceClient.initialize(lastVoiceChannel);
		}
	}

	private static void handleRelease() {
		if (voiceClient != null) {
			voiceClient.release();
			voiceClient = null;
		}
		speakingSet.clear();
		activateVoice(false);
	}

	public static void handleVoiceSignalPacketTypeGlobalNew(Collection<SPacketVoiceSignalGlobalEAG.UserData> voicePlayers) {
		if (voiceClient != null) {
			voiceClient.handleVoiceSignalPacketTypeGlobal(voicePlayers);
		}
	}

	public static void handleServerDisconnect() {
		if (!isClientSupported()) return;
		serverSupport = false;
		packetSendCallback = null;
		protocolVersion = -1;
		handleRelease();
	}

	public static void handleVoiceSignalPacketTypeAllowed(boolean voiceAvailableStat, String[] servs) {
		if(packetSendCallback != null) {
			handleRelease();
			PlatformVoiceClient.setICEServers(servs);
			if (serverSupport != voiceAvailableStat) {
				serverSupport = voiceAvailableStat;
				if (voiceAvailableStat) {
					voiceClient = new VoiceClientInstance(protocolVersion, packetSendCallback);
					voiceClient.initialize(lastVoiceChannel);
				}
			}
		}
	}

	public static void handleVoiceSignalPacketTypeConnect(EaglercraftUUID user, boolean offer) {
		if (voiceClient != null) {
			voiceClient.handleVoiceSignalPacketTypeConnect(user, offer);
		}
	}

	public static void handleVoiceSignalPacketTypeConnectAnnounce(EaglercraftUUID user) {
		if (voiceClient != null) {
			voiceClient.handleVoiceSignalPacketTypeConnectAnnounce(user);
		}
	}

	public static void handleVoiceSignalPacketTypeDisconnect(EaglercraftUUID user) {
		if (voiceClient != null) {
			voiceClient.handleVoiceSignalPacketTypeDisconnect(user);
		}
	}

	public static void handleVoiceSignalPacketTypeICECandidate(EaglercraftUUID user, String ice) {
		if (voiceClient != null) {
			voiceClient.handleVoiceSignalPacketTypeICECandidate(user, ice);
		}
	}

	public static void handleVoiceSignalPacketTypeDescription(EaglercraftUUID user, String desc) {
		if (voiceClient != null) {
			voiceClient.handleVoiceSignalPacketTypeDescription(user, desc);
		}
	}

	public static void tickVoiceClient() {
		if (voiceClient != null) {
			voiceClient.tickVoiceClient();
			Minecraft mc = Minecraft.getMinecraft();
			if (voiceClient.getVoiceChannel() != EnumVoiceChannelType.NONE) {
				activateVoice((mc.currentScreen == null || !mc.currentScreen.blockPTTKey())
						&& Keyboard.isKeyDown(mc.gameSettings.voicePTTKey));
			} else {
				activateVoice(false);
			}
			speakingSet.clear();
			PlatformVoiceClient.tickVoiceClient();
		}
	}

	public static void setVoiceChannel(EnumVoiceChannelType channel) {
		if (voiceClient != null) {
			voiceClient.setVoiceChannel(channel);
		}
	}

	public static EnumVoiceChannelType getVoiceChannel() {
		if (voiceClient != null) {
			return voiceClient.getVoiceChannel();
		} else {
			return EnumVoiceChannelType.NONE;
		}
	}

	public static EnumVoiceChannelStatus getVoiceStatus() {
		if (voiceClient != null) {
			return voiceClient.getVoiceStatus();
		} else {
			return EnumVoiceChannelStatus.UNAVAILABLE;
		}
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
		if (voiceClient != null) {
			return voiceClient.getVoiceUsername(uuid);
		} else {
			return uuid.toString();
		}
	}

	public static void sendPacketICE(EaglercraftUUID peerId, String candidate) {
		if (voiceClient != null) {
			voiceClient.sendPacketICE(peerId, candidate);
		}
	}

	public static void sendPacketDesc(EaglercraftUUID peerId, String desc) {
		if (voiceClient != null) {
			voiceClient.sendPacketDesc(peerId, desc);
		}
	}

	public static void sendPacketDisconnectPeer(EaglercraftUUID peerId) {
		if (voiceClient != null) {
			voiceClient.sendPacketDisconnectPeer(peerId);
		}
	}

}