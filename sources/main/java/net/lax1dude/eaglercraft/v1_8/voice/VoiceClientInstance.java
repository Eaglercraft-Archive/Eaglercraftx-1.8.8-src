/*
 * Copyright (c) 2025 lax1dude, ayunami2000. All Rights Reserved.
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
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformVoiceClient;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketVoiceSignalConnectEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketVoiceSignalDescEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketVoiceSignalDisconnectPeerV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketVoiceSignalDisconnectV3EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketVoiceSignalDisconnectV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketVoiceSignalICEEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketVoiceSignalRequestEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketVoiceSignalGlobalEAG;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class VoiceClientInstance {

	private final int protocolVers;
	private final Consumer<GameMessagePacket> packetSendCallback;
	private final Map<EaglercraftUUID, VoicePlayerState> voicePlayers = new HashMap<>(32);
	private EnumVoiceChannelType voiceChannel = EnumVoiceChannelType.NONE;
	private long lastUpdate = 0l;

	protected VoiceClientInstance(int protocolVers, Consumer<GameMessagePacket> packetSendCallback) {
		this.protocolVers = protocolVers;
		this.packetSendCallback = packetSendCallback;
	}

	public void initialize(EnumVoiceChannelType voiceChannel) {
		setVoiceChannel(voiceChannel);
	}

	public EnumVoiceChannelType getVoiceChannel() {
		return voiceChannel;
	}

	public void setVoiceChannel(EnumVoiceChannelType channel) {
		VoiceClientController.lastVoiceChannel = channel;
		if (voiceChannel == channel) return;
		if (channel != EnumVoiceChannelType.NONE) {
			PlatformVoiceClient.initializeDevices();
		}
		if (channel == EnumVoiceChannelType.NONE) {
			release();
			packetSendCallback.accept(new CPacketVoiceSignalDisconnectV4EAG());
		} else {
			if (voiceChannel == EnumVoiceChannelType.PROXIMITY && channel == EnumVoiceChannelType.GLOBAL) {
				for (VoicePlayerState state : voicePlayers.values()) {
					if(!state.nearby) {
						state.tryRequest(EagRuntime.steadyTimeMillis());
					}
					PlatformVoiceClient.makePeerGlobal(state.uuid);
				}
			} else if (voiceChannel == EnumVoiceChannelType.GLOBAL && channel == EnumVoiceChannelType.PROXIMITY) {
				for (VoicePlayerState state : new ArrayList<>(voicePlayers.values())) {
					recheckNearby(state);
					if(!state.nearby) {
						PlatformVoiceClient.signalDisconnect(state.uuid, false);
					}
					PlatformVoiceClient.makePeerProximity(state.uuid);
				}
			} else if (voiceChannel == EnumVoiceChannelType.NONE) {
				packetSendCallback.accept(new CPacketVoiceSignalConnectEAG());
			}
		}
		voiceChannel = channel;
	}

	public void handleVoiceSignalPacketTypeGlobal(Collection<SPacketVoiceSignalGlobalEAG.UserData> voicePlayers) {
		if (voiceChannel != EnumVoiceChannelType.NONE) {
			EaglercraftUUID self = Minecraft.getMinecraft().getSession().getProfile().getId();
			Set<EaglercraftUUID> playersLost = new HashSet<>(this.voicePlayers.keySet());
			for (SPacketVoiceSignalGlobalEAG.UserData userData : voicePlayers) {
				EaglercraftUUID uuid = new EaglercraftUUID(userData.uuidMost, userData.uuidLeast);
				if (!uuid.equals(self)) {
					if (!playersLost.remove(uuid)) {
						announcePlayer(uuid);
					}
					this.voicePlayers.get(uuid).name = userData.username;
				}
			}
			if (!playersLost.isEmpty()) {
				for (EaglercraftUUID uuid : playersLost) {
					dropPlayer(uuid);
				}
			}
		}
	}

	private void announcePlayer(EaglercraftUUID uuid) {
		VoicePlayerState voiceState = new VoicePlayerState(this, uuid);
		voicePlayers.put(uuid, voiceState);
		if (voiceChannel == EnumVoiceChannelType.GLOBAL) {
			voiceState.tryRequest(EagRuntime.steadyTimeMillis());
		} else if (voiceChannel == EnumVoiceChannelType.PROXIMITY) {
			recheckNearby(voiceState);
			if(voiceState.nearby) {
				voiceState.tryRequest(EagRuntime.steadyTimeMillis());
			}
		}
	}

	private void recheckNearby(VoicePlayerState voiceState) {
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.theWorld != null) {
			EntityPlayer player = mc.theWorld.getPlayerEntityByUUID(voiceState.uuid);
			if(player != null) {
				voiceState.nearby = goddamnManhattanDistance(mc, player);
				if(voiceState.nearby) {
					PlatformVoiceClient.updateVoicePosition(voiceState.uuid, player.posX, player.posY, player.posZ);
				}
				return;
			}
		}
		voiceState.nearby = false;
	}

	// Must perform these ham-fisted manhattan distance calculations to work with old clients
	private boolean goddamnManhattanDistance(Minecraft mc, Entity player) {
		final int prox = 22;
		return Math.abs(mc.thePlayer.posX - player.posX) <= prox && Math.abs(mc.thePlayer.posY - player.posY) <= prox
				&& Math.abs(mc.thePlayer.posZ - player.posZ) <= prox;
	}

	private void dropPlayer(EaglercraftUUID uuid) {
		voicePlayers.remove(uuid);
		PlatformVoiceClient.signalDisconnect(uuid, true);
	}

	public void handleVoiceSignalPacketTypeConnectAnnounce(EaglercraftUUID user) {
		if (voiceChannel != EnumVoiceChannelType.NONE) {
			if (!voicePlayers.containsKey(user)) {
				// Backwards compat with old servers :(
				announcePlayer(user);
			}
		}
	}

	public void handleVoiceSignalPacketTypeConnect(EaglercraftUUID user, boolean offer) {
		if (voiceChannel != EnumVoiceChannelType.NONE) {
			PlatformVoiceClient.signalConnect(user, offer);
		}
	}

	public void handleVoiceSignalPacketTypeICECandidate(EaglercraftUUID user, String ice) {
		if (voiceChannel != EnumVoiceChannelType.NONE) {
			PlatformVoiceClient.signalICECandidate(user, ice);
		}
	}

	public void handleVoiceSignalPacketTypeDescription(EaglercraftUUID user, String desc) {
		if (voiceChannel != EnumVoiceChannelType.NONE) {
			PlatformVoiceClient.signalDescription(user, desc);
		}
	}

	public void handleVoiceSignalPacketTypeDisconnect(EaglercraftUUID user) {
		if (voiceChannel != EnumVoiceChannelType.NONE) {
			VoicePlayerState state = voicePlayers.get(user);
			if (state != null) {
				state.handleDisconnect();
			}
			PlatformVoiceClient.signalDisconnect(user, true);
		}
	}

	public void sendPacketRequest(VoicePlayerState voicePlayerState) {
		if (voiceChannel != EnumVoiceChannelType.NONE) {
			packetSendCallback.accept(new CPacketVoiceSignalRequestEAG(voicePlayerState.uuid.msb, voicePlayerState.uuid.lsb));
		}
	}

	public void sendPacketDesc(EaglercraftUUID peerId, String desc) {
		if (voiceChannel != EnumVoiceChannelType.NONE) {
			packetSendCallback.accept(new CPacketVoiceSignalDescEAG(peerId.msb, peerId.lsb, desc));
		}
	}

	public void sendPacketICE(EaglercraftUUID peerId, String candidate) {
		if (voiceChannel != EnumVoiceChannelType.NONE) {
			packetSendCallback.accept(new CPacketVoiceSignalICEEAG(peerId.msb, peerId.lsb, candidate));
		}
	}

	public void sendPacketDisconnectPeer(EaglercraftUUID peerId) {
		if (voiceChannel != EnumVoiceChannelType.NONE) {
			VoicePlayerState state = voicePlayers.get(peerId);
			if (state != null) {
				state.handleDisconnect();
			}
			if (protocolVers >= 4) {
				packetSendCallback.accept(new CPacketVoiceSignalDisconnectPeerV4EAG(peerId.msb, peerId.lsb));
			} else {
				packetSendCallback.accept(new CPacketVoiceSignalDisconnectV3EAG(true, peerId.msb, peerId.lsb));
			}
		}
	}

	public String getVoiceUsername(EaglercraftUUID uuid) {
		VoicePlayerState state = voicePlayers.get(uuid);
		if (state != null) {
			return state.name;
		} else {
			return uuid.toString();
		}
	}

	public EnumVoiceChannelStatus getVoiceStatus() {
		if (voiceChannel != EnumVoiceChannelType.NONE) {
			return PlatformVoiceClient.getReadyState() != EnumVoiceChannelReadyState.DEVICE_INITIALIZED ?
					EnumVoiceChannelStatus.CONNECTING : EnumVoiceChannelStatus.CONNECTED;
		} else {
			return EnumVoiceChannelStatus.DISCONNECTED;
		}
	}

	public void tickVoiceClient() {
		if (voiceChannel == EnumVoiceChannelType.GLOBAL) {
			long millis = EagRuntime.steadyTimeMillis();
			if (millis - lastUpdate > 1500l) {
				lastUpdate = millis;
				for (VoicePlayerState state : voicePlayers.values()) {
					state.tryRequest(millis);
				}
			}
		} else if (voiceChannel == EnumVoiceChannelType.PROXIMITY) {
			long millis = EagRuntime.steadyTimeMillis();
			if (millis - lastUpdate > 100l) {
				lastUpdate = millis;
				for (VoicePlayerState state : voicePlayers.values()) {
					boolean old = state.nearby;
					recheckNearby(state);
					if (state.nearby) {
						state.tryRequest(millis);
					} else if (old) {
						PlatformVoiceClient.signalDisconnect(state.uuid, false);
					}
				}
			}
		}
	}

	public void release() {
		for (VoicePlayerState state : new ArrayList<>(voicePlayers.values())) {
			dropPlayer(state.uuid);
		}
	}

}
