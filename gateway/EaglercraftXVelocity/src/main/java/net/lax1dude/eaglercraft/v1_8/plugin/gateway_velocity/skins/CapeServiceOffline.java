package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPlayerData;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketForceClientCapeCustomV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketForceClientCapePresetV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherCapeCustomEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherCapePresetEAG;

/**
 * Copyright (c) 2024-2025 lax1dude. All Rights Reserved.
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
public class CapeServiceOffline {

	public static final int masterRateLimitPerPlayer = 250;

	private final ConcurrentMap<UUID, GameMessagePacket> capesCache = new ConcurrentHashMap<>();

	public void registerEaglercraftPlayer(UUID playerUUID, GameMessagePacket capePacket) {
		capesCache.put(playerUUID, capePacket);
	}

	public void processGetOtherCape(UUID searchUUID, EaglerPlayerData sender) {
		if(sender.skinLookupRateLimiter.rateLimit(masterRateLimitPerPlayer)) {
			GameMessagePacket maybeCape = capesCache.get(searchUUID);
			if(maybeCape != null) {
				sender.sendEaglerMessage(maybeCape);
			}else {
				sender.sendEaglerMessage(new SPacketOtherCapePresetEAG(searchUUID.getMostSignificantBits(),
						searchUUID.getLeastSignificantBits(), 0));
			}
		}
	}

	public void processForceCape(UUID clientUUID, EaglerPlayerData initialHandler) {
		GameMessagePacket maybeCape = capesCache.get(clientUUID);
		if(maybeCape != null) {
			if (maybeCape instanceof SPacketOtherCapePresetEAG) {
				initialHandler.sendEaglerMessage(
						new SPacketForceClientCapePresetV4EAG(((SPacketOtherCapePresetEAG) maybeCape).presetCape));
			} else if (maybeCape instanceof SPacketOtherCapeCustomEAG) {
				initialHandler.sendEaglerMessage(
						new SPacketForceClientCapeCustomV4EAG(((SPacketOtherCapeCustomEAG) maybeCape).customCape));
			}
		}
	}

	public void unregisterPlayer(UUID playerUUID) {
		capesCache.remove(playerUUID);
	}

	public GameMessagePacket getCape(UUID clientUUID) {
		return capesCache.get(clientUUID);
	}

	public byte[] getCapeHandshakeData(UUID clientUUID) {
		GameMessagePacket capePacket = getCape(clientUUID);
		if(capePacket != null) {
			if(capePacket instanceof SPacketOtherCapeCustomEAG) {
				SPacketOtherCapeCustomEAG pkt = (SPacketOtherCapeCustomEAG)capePacket;
				byte[] ret = new byte[1174];
				ret[0] = (byte)2;
				System.arraycopy(pkt.customCape, 0, ret, 1, 1173);
				return ret;
			}else {
				SPacketOtherCapePresetEAG pkt = (SPacketOtherCapePresetEAG)capePacket;
				int p = pkt.presetCape;
				byte[] ret = new byte[5];
				ret[0] = (byte)1;
				ret[1] = (byte)(p >>> 24);
				ret[2] = (byte)(p >>> 16);
				ret[3] = (byte)(p >>> 8);
				ret[4] = (byte)(p & 0xFF);
				return ret;
			}
		}else {
			return null;
		}
	}

	public void shutdown() {
		capesCache.clear();
	}
}
