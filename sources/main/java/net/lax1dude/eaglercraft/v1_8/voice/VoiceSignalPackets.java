package net.lax1dude.eaglercraft.v1_8.voice;

import java.nio.charset.StandardCharsets;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
import net.minecraft.network.PacketBuffer;

/**
 * Copyright (c) 2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class VoiceSignalPackets {

	static final int VOICE_SIGNAL_ALLOWED = 0;
	static final int VOICE_SIGNAL_REQUEST = 0;
	static final int VOICE_SIGNAL_CONNECT = 1;
	static final int VOICE_SIGNAL_DISCONNECT = 2;
	static final int VOICE_SIGNAL_ICE = 3;
	static final int VOICE_SIGNAL_DESC = 4;
	static final int VOICE_SIGNAL_GLOBAL = 5;

	static void handleVoiceSignal(PacketBuffer streamIn) {
		try {
			int sig = streamIn.readUnsignedByte();
			switch(sig) {
				case VOICE_SIGNAL_ALLOWED: {
					boolean voiceAvailableStat = streamIn.readUnsignedByte() == 1;
					String[] servs = null;
					if(voiceAvailableStat) {
						servs = new String[streamIn.readVarIntFromBuffer()];
						for(int i = 0; i < servs.length; i++) {
							servs[i] = streamIn.readStringFromBuffer(1024);
						}
					}
					VoiceClientController.handleVoiceSignalPacketTypeAllowed(voiceAvailableStat, servs);
					break;
				}
				case VOICE_SIGNAL_GLOBAL: {
					if (VoiceClientController.getVoiceChannel() != EnumVoiceChannelType.GLOBAL) return;
					EaglercraftUUID[] voiceIds = new EaglercraftUUID[streamIn.readVarIntFromBuffer()];
					for(int i = 0; i < voiceIds.length; i++) {
						voiceIds[i] = streamIn.readUuid();
					}
					String[] voiceNames = null;
					if (streamIn.isReadable()) {
						voiceNames = new String[voiceIds.length];
						for(int i = 0; i < voiceNames.length; i++) {
							voiceNames[i] = streamIn.readStringFromBuffer(16);
						}
					}
					VoiceClientController.handleVoiceSignalPacketTypeGlobal(voiceIds, voiceNames);
					break;
				}
				case VOICE_SIGNAL_CONNECT: {
					EaglercraftUUID uuid = streamIn.readUuid();
					if (streamIn.isReadable()) {
						VoiceClientController.handleVoiceSignalPacketTypeConnect(uuid, streamIn.readBoolean());
					} else if (VoiceClientController.getVoiceChannel() != EnumVoiceChannelType.PROXIMITY || VoiceClientController.getVoiceListening().contains(uuid)) {
						VoiceClientController.handleVoiceSignalPacketTypeConnectAnnounce(uuid);
					}
					break;
				}
				case VOICE_SIGNAL_DISCONNECT: {
					VoiceClientController.handleVoiceSignalPacketTypeDisconnect(streamIn.readableBytes() > 0 ? streamIn.readUuid() : null);
					break;
				}
				case VOICE_SIGNAL_ICE: {
					VoiceClientController.handleVoiceSignalPacketTypeICECandidate(streamIn.readUuid(), streamIn.readStringFromBuffer(32767));
					break;
				}
				case VOICE_SIGNAL_DESC: {
					VoiceClientController.handleVoiceSignalPacketTypeDescription(streamIn.readUuid(), streamIn.readStringFromBuffer(32767));
					break;
				}
				default: {
					VoiceClientController.logger.error("Unknown voice signal packet '{}'!", sig);
					break;
				}
			}
		}catch(Throwable ex) {
			VoiceClientController.logger.error("Failed to handle signal packet!");
			VoiceClientController.logger.error(ex);
		}
	}

	static PacketBuffer makeVoiceSignalPacketRequest(EaglercraftUUID user) {
		PacketBuffer ret = new PacketBuffer(Unpooled.buffer(17, 17));
		ret.writeByte(VOICE_SIGNAL_REQUEST);
		ret.writeUuid(user);
		return ret;
	}

	static PacketBuffer makeVoiceSignalPacketICE(EaglercraftUUID user, String icePacket) {
		byte[] str = icePacket.getBytes(StandardCharsets.UTF_8);
		int estLen = 17 + PacketBuffer.getVarIntSize(str.length) + str.length;
		PacketBuffer ret = new PacketBuffer(Unpooled.buffer(estLen, estLen));
		ret.writeByte(VOICE_SIGNAL_ICE);
		ret.writeUuid(user);
		ret.writeByteArray(str);
		return ret;
	}

	static PacketBuffer makeVoiceSignalPacketDesc(EaglercraftUUID user, String descPacket) {
		byte[] str = descPacket.getBytes(StandardCharsets.UTF_8);
		int estLen = 17 + PacketBuffer.getVarIntSize(str.length) + str.length;
		PacketBuffer ret = new PacketBuffer(Unpooled.buffer(estLen, estLen));
		ret.writeByte(VOICE_SIGNAL_DESC);
		ret.writeUuid(user);
		ret.writeByteArray(str);
		return ret;
	}

	static PacketBuffer makeVoiceSignalPacketDisconnect(EaglercraftUUID user) {
		if (user == null) {
			PacketBuffer ret = new PacketBuffer(Unpooled.buffer(1, 1));
			ret.writeByte(VOICE_SIGNAL_DISCONNECT);
			return ret;
		}
		PacketBuffer ret = new PacketBuffer(Unpooled.buffer(17, 17));
		ret.writeByte(VOICE_SIGNAL_DISCONNECT);
		ret.writeUuid(user);
		return ret;
	}

	public static PacketBuffer makeVoiceSignalPacketConnect() {
		PacketBuffer ret = new PacketBuffer(Unpooled.buffer(1, 1));
		ret.writeByte(VOICE_SIGNAL_CONNECT);
		return ret;
	}
}
