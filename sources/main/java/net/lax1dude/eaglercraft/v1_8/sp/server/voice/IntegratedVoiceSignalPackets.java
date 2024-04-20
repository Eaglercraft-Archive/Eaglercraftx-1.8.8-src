package net.lax1dude.eaglercraft.v1_8.sp.server.voice;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.netty.ByteBuf;
import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;

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
public class IntegratedVoiceSignalPackets {

	static final int VOICE_SIGNAL_ALLOWED = 0;
	static final int VOICE_SIGNAL_REQUEST = 0;
	static final int VOICE_SIGNAL_CONNECT = 1;
	static final int VOICE_SIGNAL_DISCONNECT = 2;
	static final int VOICE_SIGNAL_ICE = 3;
	static final int VOICE_SIGNAL_DESC = 4;
	static final int VOICE_SIGNAL_GLOBAL = 5;

	public static void processPacket(PacketBuffer buffer, EntityPlayerMP sender, IntegratedVoiceService voiceService) throws IOException {
		int packetId = -1;
		if(buffer.readableBytes() == 0) {
			throw new IOException("Zero-length packet recieved");
		}
		try {
			packetId = buffer.readUnsignedByte();
			switch(packetId) {
				case VOICE_SIGNAL_REQUEST: {
					voiceService.handleVoiceSignalPacketTypeRequest(buffer.readUuid(), sender);
					break;
				}
				case VOICE_SIGNAL_CONNECT: {
					voiceService.handleVoiceSignalPacketTypeConnect(sender);
					break;
				}
				case VOICE_SIGNAL_ICE: {
					voiceService.handleVoiceSignalPacketTypeICE(buffer.readUuid(), buffer.readStringFromBuffer(32767), sender);
					break;
				}
				case VOICE_SIGNAL_DESC: {
					voiceService.handleVoiceSignalPacketTypeDesc(buffer.readUuid(), buffer.readStringFromBuffer(32767), sender);
					break;
				}
				case VOICE_SIGNAL_DISCONNECT: {
					voiceService.handleVoiceSignalPacketTypeDisconnect(buffer.readableBytes() > 0 ? buffer.readUuid() : null, sender);
					break;
				}
				default: {
					throw new IOException("Unknown packet type " + packetId);
				}
			}
			if(buffer.readableBytes() > 0) {
				throw new IOException("Voice packet is too long!");
			}
		}catch(IOException ex) {
			throw ex;
		}catch(Throwable t) {
			throw new IOException("Unhandled exception handling voice packet type " + packetId, t);
		}
	}

	static byte[] makeVoiceSignalPacketAllowed(boolean allowed, String[] iceServers) {
		if (iceServers == null) {
			byte[] ret = new byte[2];
			ByteBuf wrappedBuffer = Unpooled.buffer(ret, ret.length);
			wrappedBuffer.writeByte(VOICE_SIGNAL_ALLOWED);
			wrappedBuffer.writeBoolean(allowed);
			return ret;
		}
		byte[][] iceServersBytes = new byte[iceServers.length][];
		int totalLen = 2 + PacketBuffer.getVarIntSize(iceServers.length);
		for(int i = 0; i < iceServers.length; ++i) {
			byte[] b = iceServersBytes[i] = iceServers[i].getBytes(StandardCharsets.UTF_8);
			totalLen += PacketBuffer.getVarIntSize(b.length) + b.length;
		}
		byte[] ret = new byte[totalLen];
		PacketBuffer wrappedBuffer = new PacketBuffer(Unpooled.buffer(ret, ret.length));
		wrappedBuffer.writeByte(VOICE_SIGNAL_ALLOWED);
		wrappedBuffer.writeBoolean(allowed);
		wrappedBuffer.writeVarIntToBuffer(iceServersBytes.length);
		for(int i = 0; i < iceServersBytes.length; ++i) {
			byte[] b = iceServersBytes[i];
			wrappedBuffer.writeVarIntToBuffer(b.length);
			wrappedBuffer.writeBytes(b);
		}
		return ret;
	}

	static byte[] makeVoiceSignalPacketGlobal(Collection<EntityPlayerMP> users) {
		int cnt = users.size();
		byte[][] displayNames = new byte[cnt][];
		int i = 0;
		for(EntityPlayerMP user : users) {
			String name = user.getName();
			if(name.length() > 16) name = name.substring(0, 16);
			displayNames[i++] = name.getBytes(StandardCharsets.UTF_8);
		}
		int totalLength = 1 + PacketBuffer.getVarIntSize(cnt) + (cnt << 4);
		for(i = 0; i < cnt; ++i) {
			totalLength += PacketBuffer.getVarIntSize(displayNames[i].length) + displayNames[i].length;
		}
		byte[] ret = new byte[totalLength];
		PacketBuffer wrappedBuffer = new PacketBuffer(Unpooled.buffer(ret, ret.length));
		wrappedBuffer.writeByte(VOICE_SIGNAL_GLOBAL);
		wrappedBuffer.writeVarIntToBuffer(cnt);
		for(EntityPlayerMP user : users) {
			wrappedBuffer.writeUuid(user.getUniqueID());
		}
		for(i = 0; i < cnt; ++i) {
			wrappedBuffer.writeVarIntToBuffer(displayNames[i].length);
			wrappedBuffer.writeBytes(displayNames[i]);
		}
		return ret;
	}

	static PacketBuffer makeVoiceSignalPacketConnect(EaglercraftUUID player, boolean offer) {
		byte[] ret = new byte[18];
		PacketBuffer wrappedBuffer = new PacketBuffer(Unpooled.buffer(ret, ret.length));
		wrappedBuffer.writeByte(VOICE_SIGNAL_CONNECT);
		wrappedBuffer.writeUuid(player);
		wrappedBuffer.writeBoolean(offer);
		return wrappedBuffer;
	}

	static byte[] makeVoiceSignalPacketConnectAnnounce(EaglercraftUUID player) {
		byte[] ret = new byte[17];
		PacketBuffer wrappedBuffer = new PacketBuffer(Unpooled.buffer(ret, ret.length));
		wrappedBuffer.writeByte(VOICE_SIGNAL_CONNECT);
		wrappedBuffer.writeUuid(player);
		return ret;
	}

	static byte[] makeVoiceSignalPacketDisconnect(EaglercraftUUID player) {
		if(player == null) {
			return new byte[] { (byte)VOICE_SIGNAL_DISCONNECT };
		}
		byte[] ret = new byte[17];
		PacketBuffer wrappedBuffer = new PacketBuffer(Unpooled.buffer(ret, ret.length));
		wrappedBuffer.writeByte(VOICE_SIGNAL_DISCONNECT);
		wrappedBuffer.writeUuid(player);
		return ret;
	}

	static PacketBuffer makeVoiceSignalPacketDisconnectPB(EaglercraftUUID player) {
		if(player == null) {
			byte[] ret = new byte[1];
			PacketBuffer wrappedBuffer = new PacketBuffer(Unpooled.buffer(ret, ret.length));
			wrappedBuffer.writeByte(VOICE_SIGNAL_DISCONNECT);
			return wrappedBuffer;
		}
		byte[] ret = new byte[17];
		PacketBuffer wrappedBuffer = new PacketBuffer(Unpooled.buffer(ret, ret.length));
		wrappedBuffer.writeByte(VOICE_SIGNAL_DISCONNECT);
		wrappedBuffer.writeUuid(player);
		return wrappedBuffer;
	}

	static PacketBuffer makeVoiceSignalPacketICE(EaglercraftUUID player, String str) {
		byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
		byte[] ret = new byte[17 + PacketBuffer.getVarIntSize(strBytes.length) + strBytes.length];
		PacketBuffer wrappedBuffer = new PacketBuffer(Unpooled.buffer(ret, ret.length));
		wrappedBuffer.writeByte(VOICE_SIGNAL_ICE);
		wrappedBuffer.writeUuid(player);
		wrappedBuffer.writeVarIntToBuffer(strBytes.length);
		wrappedBuffer.writeBytes(strBytes);
		return wrappedBuffer;
	}

	static PacketBuffer makeVoiceSignalPacketDesc(EaglercraftUUID player, String str) {
		byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
		byte[] ret = new byte[17 + PacketBuffer.getVarIntSize(strBytes.length) + strBytes.length];
		PacketBuffer wrappedBuffer = new PacketBuffer(Unpooled.buffer(ret, ret.length));
		wrappedBuffer.writeByte(VOICE_SIGNAL_DESC);
		wrappedBuffer.writeUuid(player);
		wrappedBuffer.writeVarIntToBuffer(strBytes.length);
		wrappedBuffer.writeBytes(strBytes);
		return wrappedBuffer;
	}

}
