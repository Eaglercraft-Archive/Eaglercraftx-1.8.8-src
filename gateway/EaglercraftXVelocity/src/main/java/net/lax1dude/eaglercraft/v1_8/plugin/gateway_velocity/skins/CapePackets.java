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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins;

import java.io.IOException;
import java.util.UUID;

import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherCapeCustomEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherCapePresetEAG;

public class CapePackets {

	public static final int PACKET_MY_CAPE_PRESET = 0x01;
	public static final int PACKET_MY_CAPE_CUSTOM = 0x02;

	public static void registerEaglerPlayer(UUID clientUUID, byte[] bs, CapeServiceOffline capeService) throws IOException {
		if(bs.length == 0) {
			throw new IOException("Zero-length packet recieved");
		}
		GameMessagePacket generatedPacket;
		int packetType = (int)bs[0] & 0xFF;
		switch(packetType) {
		case PACKET_MY_CAPE_PRESET:
			if(bs.length != 5) {
				throw new IOException("Invalid length " + bs.length + " for preset cape packet");
			}
			generatedPacket = new SPacketOtherCapePresetEAG(clientUUID.getMostSignificantBits(),
					clientUUID.getLeastSignificantBits(), (bs[1] << 24) | (bs[2] << 16) | (bs[3] << 8) | (bs[4] & 0xFF));
			break;
		case PACKET_MY_CAPE_CUSTOM:
			if(bs.length != 1174) {
				throw new IOException("Invalid length " + bs.length + " for custom cape packet");
			}
			byte[] capePixels = new byte[bs.length - 1];
			System.arraycopy(bs, 1, capePixels, 0, capePixels.length);
			generatedPacket = new SPacketOtherCapeCustomEAG(clientUUID.getMostSignificantBits(),
					clientUUID.getLeastSignificantBits(), capePixels);
			break;
		default:
			throw new IOException("Unknown skin packet type: " + packetType);
		}
		capeService.registerEaglercraftPlayer(clientUUID, generatedPacket);
	}

	public static void registerEaglerPlayerFallback(UUID clientUUID, CapeServiceOffline capeService) {
		capeService.registerEaglercraftPlayer(clientUUID, new SPacketOtherCapePresetEAG(
				clientUUID.getMostSignificantBits(), clientUUID.getLeastSignificantBits(), 0));
	}

}