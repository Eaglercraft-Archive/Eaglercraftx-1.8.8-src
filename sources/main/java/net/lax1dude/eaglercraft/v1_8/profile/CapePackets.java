package net.lax1dude.eaglercraft.v1_8.profile;

import java.io.IOException;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
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
public class CapePackets {

	public static final int PACKET_MY_CAPE_PRESET = 0x01;
	public static final int PACKET_MY_CAPE_CUSTOM = 0x02;
	public static final int PACKET_GET_OTHER_CAPE = 0x03;
	public static final int PACKET_OTHER_CAPE_PRESET = 0x04;
	public static final int PACKET_OTHER_CAPE_CUSTOM = 0x05;

	public static void readPluginMessage(PacketBuffer buffer, ServerCapeCache capeCache) throws IOException {
		try {
			int type = (int)buffer.readByte() & 0xFF;
			switch(type) {
			case PACKET_OTHER_CAPE_PRESET: {
				EaglercraftUUID responseUUID = buffer.readUuid();
				int responsePreset = buffer.readInt();
				if(buffer.isReadable()) {
					throw new IOException("PACKET_OTHER_CAPE_PRESET had " + buffer.readableBytes() + " remaining bytes!");
				}
				capeCache.cacheCapePreset(responseUUID, responsePreset);
				break;
			}
			case PACKET_OTHER_CAPE_CUSTOM: {
				EaglercraftUUID responseUUID = buffer.readUuid();
				byte[] readCape = new byte[1173];
				buffer.readBytes(readCape);
				if(buffer.isReadable()) {
					throw new IOException("PACKET_OTHER_CAPE_CUSTOM had " + buffer.readableBytes() + " remaining bytes!");
				}
				capeCache.cacheCapeCustom(responseUUID, readCape);
				break;
			}
			default:
				throw new IOException("Unknown skin packet type: " + type);
			}
		}catch(IOException ex) {
			throw ex;
		}catch(Throwable t) {
			throw new IOException("Failed to parse cape packet!", t);
		}
	}

	public static byte[] writeMyCapePreset(int capeId) {
		return new byte[] { (byte) PACKET_MY_CAPE_PRESET, (byte) (capeId >> 24), (byte) (capeId >> 16),
				(byte) (capeId >> 8), (byte) (capeId & 0xFF) };
	}

	public static byte[] writeMyCapeCustom(CustomCape customCape) {
		byte[] packet = new byte[1 + customCape.texture.length];
		packet[0] = (byte) PACKET_MY_CAPE_CUSTOM;
		System.arraycopy(customCape.texture, 0, packet, 1, customCape.texture.length);
		return packet;
	}

	public static PacketBuffer writeGetOtherCape(EaglercraftUUID playerId) throws IOException {
		PacketBuffer ret = new PacketBuffer(Unpooled.buffer(17, 17));
		ret.writeByte(PACKET_GET_OTHER_CAPE);
		ret.writeUuid(playerId);
		return ret;
	}
}
