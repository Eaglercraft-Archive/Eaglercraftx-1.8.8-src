package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins;

import java.io.IOException;
import java.util.UUID;

import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinCustomV3EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinCustomV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinPresetEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SkinPacketVersionCache;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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
public class SkinPackets {

	public static final int PACKET_MY_SKIN_PRESET = 0x01;
	public static final int PACKET_MY_SKIN_CUSTOM = 0x02;
	
	public static void registerEaglerPlayer(UUID clientUUID, byte[] bs, ISkinService skinService, int protocolVers) throws IOException {
		if(bs.length == 0) {
			throw new IOException("Zero-length packet recieved");
		}
		GameMessagePacket generatedPacketV3 = null;
		GameMessagePacket generatedPacketV4 = null;
		int skinModel = -1;
		int packetType = (int)bs[0] & 0xFF;
		switch(packetType) {
		case PACKET_MY_SKIN_PRESET:
			if(bs.length != 5) {
				throw new IOException("Invalid length " + bs.length + " for preset skin packet");
			}
			generatedPacketV3 = generatedPacketV4 = new SPacketOtherSkinPresetEAG(clientUUID.getMostSignificantBits(),
					clientUUID.getLeastSignificantBits(), (bs[1] << 24) | (bs[2] << 16) | (bs[3] << 8) | (bs[4] & 0xFF));
			break;
		case PACKET_MY_SKIN_CUSTOM:
			if(protocolVers <= 3) {
				byte[] pixels = new byte[16384];
				if(bs.length != 2 + pixels.length) {
					throw new IOException("Invalid length " + bs.length + " for custom skin packet");
				}
				setAlphaForChestV3(pixels);
				System.arraycopy(bs, 2, pixels, 0, pixels.length);
				generatedPacketV3 = new SPacketOtherSkinCustomV3EAG(clientUUID.getMostSignificantBits(), clientUUID.getLeastSignificantBits(), (skinModel = (int)bs[1] & 0xFF), pixels);
			}else {
				byte[] pixels = new byte[12288];
				if(bs.length != 2 + pixels.length) {
					throw new IOException("Invalid length " + bs.length + " for custom skin packet");
				}
				setAlphaForChestV4(pixels);
				System.arraycopy(bs, 2, pixels, 0, pixels.length);
				generatedPacketV4 = new SPacketOtherSkinCustomV4EAG(clientUUID.getMostSignificantBits(), clientUUID.getLeastSignificantBits(), (skinModel = (int)bs[1] & 0xFF), pixels);
			}
			break;
		default:
			throw new IOException("Unknown skin packet type: " + packetType);
		}
		skinService.registerEaglercraftPlayer(clientUUID, new SkinPacketVersionCache(generatedPacketV3, generatedPacketV4), skinModel);
	}
	
	public static void registerEaglerPlayerFallback(UUID clientUUID, ISkinService skinService) {
		int skinModel = (clientUUID.hashCode() & 1) != 0 ? 1 : 0;
		skinService.registerEaglercraftPlayer(clientUUID, SkinPacketVersionCache.createPreset(
				clientUUID.getMostSignificantBits(), clientUUID.getLeastSignificantBits(), skinModel), skinModel);
	}
	
	public static void setAlphaForChestV3(byte[] skin64x64) {
		if(skin64x64.length != 16384) {
			throw new IllegalArgumentException("Skin is not 64x64!");
		}
		for(int y = 20; y < 32; ++y) {
			for(int x = 16; x < 40; ++x) {
				skin64x64[(y << 8) | (x << 2)] = (byte)0xFF;
			}
		}
	}
	
	public static void setAlphaForChestV4(byte[] skin64x64) {
		if(skin64x64.length != 12288) {
			throw new IllegalArgumentException("Skin is not 64x64!");
		}
		for(int y = 20; y < 32; ++y) {
			for(int x = 16; x < 40; ++x) {
				skin64x64[((y << 6) | x) * 3] |= 0x80;
			}
		}
	}
	
	public static String bytesToAscii(byte[] bytes, int off, int len) {
		char[] ret = new char[len];
		for(int i = 0; i < len; ++i) {
			ret[i] = (char)((int)bytes[off + i] & 0xFF);
		}
		return new String(ret);
	}

	public static String bytesToAscii(byte[] bytes) {
		return bytesToAscii(bytes, 0, bytes.length);
	}
	
	public static byte[] asciiString(String string) {
		byte[] str = new byte[string.length()];
		for(int i = 0; i < str.length; ++i) {
			str[i] = (byte)string.charAt(i);
		}
		return str;
	}
	
	public static UUID createEaglerURLSkinUUID(String skinUrl) {
		return UUID.nameUUIDFromBytes(asciiString("EaglercraftSkinURL:" + skinUrl));
	}

	public static int getModelId(String modelName) {
		return "slim".equalsIgnoreCase(modelName) ? 1 : 0;
	}

}
