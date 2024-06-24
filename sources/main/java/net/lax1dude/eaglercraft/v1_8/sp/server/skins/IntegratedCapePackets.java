package net.lax1dude.eaglercraft.v1_8.sp.server.skins;

import java.io.IOException;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.minecraft.entity.player.EntityPlayerMP;

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
public class IntegratedCapePackets {

	public static final int PACKET_MY_CAPE_PRESET = 0x01;
	public static final int PACKET_MY_CAPE_CUSTOM = 0x02;
	public static final int PACKET_GET_OTHER_CAPE = 0x03;
	public static final int PACKET_OTHER_CAPE_PRESET = 0x04;
	public static final int PACKET_OTHER_CAPE_CUSTOM = 0x05;

	public static void processPacket(byte[] data, EntityPlayerMP sender, IntegratedCapeService capeService) throws IOException {
		if(data.length == 0) {
			throw new IOException("Zero-length packet recieved");
		}
		int packetId = (int)data[0] & 0xFF;
		try {
			switch(packetId) {
			case PACKET_GET_OTHER_CAPE:
				processGetOtherCape(data, sender, capeService);
				break;
			default:
				throw new IOException("Unknown packet type " + packetId);
			}
		}catch(IOException ex) {
			throw ex;
		}catch(Throwable t) {
			throw new IOException("Unhandled exception handling packet type " + packetId, t);
		}
	}

	private static void processGetOtherCape(byte[] data, EntityPlayerMP sender, IntegratedCapeService capeService) throws IOException {
		if(data.length != 17) {
			throw new IOException("Invalid length " + data.length + " for skin request packet");
		}
		EaglercraftUUID searchUUID = IntegratedSkinPackets.bytesToUUID(data, 1);
		capeService.processGetOtherCape(searchUUID, sender);
	}

	public static void registerEaglerPlayer(EaglercraftUUID clientUUID, byte[] bs, IntegratedCapeService capeService) throws IOException {
		if(bs.length == 0) {
			throw new IOException("Zero-length packet recieved");
		}
		byte[] generatedPacket;
		int packetType = (int)bs[0] & 0xFF;
		switch(packetType) {
		case PACKET_MY_CAPE_PRESET:
			if(bs.length != 5) {
				throw new IOException("Invalid length " + bs.length + " for preset cape packet");
			}
			generatedPacket = IntegratedCapePackets.makePresetResponse(clientUUID, (bs[1] << 24) | (bs[2] << 16) | (bs[3] << 8) | (bs[4] & 0xFF));
			break;
		case PACKET_MY_CAPE_CUSTOM:
			if(bs.length != 1174) {
				throw new IOException("Invalid length " + bs.length + " for custom cape packet");
			}
			generatedPacket = IntegratedCapePackets.makeCustomResponse(clientUUID, bs, 1, 1173);
			break;
		default:
			throw new IOException("Unknown skin packet type: " + packetType);
		}
		capeService.registerEaglercraftPlayer(clientUUID, generatedPacket);
	}

	public static void registerEaglerPlayerFallback(EaglercraftUUID clientUUID, IntegratedCapeService capeService) {
		capeService.registerEaglercraftPlayer(clientUUID, IntegratedCapePackets.makePresetResponse(clientUUID, 0));
	}

	public static byte[] makePresetResponse(EaglercraftUUID uuid, int presetId) {
		byte[] ret = new byte[1 + 16 + 4];
		ret[0] = (byte)PACKET_OTHER_CAPE_PRESET;
		IntegratedSkinPackets.UUIDToBytes(uuid, ret, 1);
		ret[17] = (byte)(presetId >>> 24);
		ret[18] = (byte)(presetId >>> 16);
		ret[19] = (byte)(presetId >>> 8);
		ret[20] = (byte)(presetId & 0xFF);
		return ret;
	}

	public static byte[] makeCustomResponse(EaglercraftUUID uuid, byte[] pixels) {
		return makeCustomResponse(uuid, pixels, 0, pixels.length);
	}

	public static byte[] makeCustomResponse(EaglercraftUUID uuid, byte[] pixels, int offset, int length) {
		byte[] ret = new byte[1 + 16 + length];
		ret[0] = (byte)PACKET_OTHER_CAPE_CUSTOM;
		IntegratedSkinPackets.UUIDToBytes(uuid, ret, 1);
		System.arraycopy(pixels, offset, ret, 17, length);
		return ret;
	}
}
