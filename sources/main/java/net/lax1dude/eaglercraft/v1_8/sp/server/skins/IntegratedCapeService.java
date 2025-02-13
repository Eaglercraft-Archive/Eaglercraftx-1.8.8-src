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

package net.lax1dude.eaglercraft.v1_8.sp.server.skins;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherCapePresetEAG;
import net.minecraft.entity.player.EntityPlayerMP;

public class IntegratedCapeService {

	public static final Logger logger = LogManager.getLogger("IntegratedCapeService");

	public static final int masterRateLimitPerPlayer = 250;

	private final Map<EaglercraftUUID, GameMessagePacket> capesCache = new HashMap<>();

	public void processLoginPacket(byte[] packetData, EntityPlayerMP sender) {
		try {
			IntegratedCapePackets.registerEaglerPlayer(sender.getUniqueID(), packetData, this);
		} catch (IOException e) {
			logger.error("Invalid skin data packet recieved from player {}!", sender.getName());
			logger.error(e);
			sender.playerNetServerHandler.kickPlayerFromServer("Invalid skin data packet recieved!");
		}
	}

	public void registerEaglercraftPlayer(EaglercraftUUID playerUUID, GameMessagePacket capePacket) {
		capesCache.put(playerUUID, capePacket);
	}

	public void processGetOtherCape(EaglercraftUUID searchUUID, EntityPlayerMP sender) {
		GameMessagePacket maybeCape = capesCache.get(searchUUID);
		if(maybeCape == null) {
			maybeCape = new SPacketOtherCapePresetEAG(searchUUID.msb, searchUUID.lsb, 0);
		}
		sender.playerNetServerHandler.sendEaglerMessage(maybeCape);
	}

	public void unregisterPlayer(EaglercraftUUID playerUUID) {
		synchronized(capesCache) {
			capesCache.remove(playerUUID);
		}
	}
}