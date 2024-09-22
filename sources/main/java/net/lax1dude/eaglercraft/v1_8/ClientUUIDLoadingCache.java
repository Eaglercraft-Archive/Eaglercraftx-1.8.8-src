package net.lax1dude.eaglercraft.v1_8;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketGetOtherClientUUIDV4EAG;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
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
public class ClientUUIDLoadingCache {

	private static final Logger logger = LogManager.getLogger("ClientUUIDLoadingCache");

	public static final EaglercraftUUID NULL_UUID = new EaglercraftUUID(0l, 0l);
	public static final EaglercraftUUID PENDING_UUID = new EaglercraftUUID(0x6969696969696969l, 0x6969696969696969l);
	public static final EaglercraftUUID VANILLA_UUID = new EaglercraftUUID(0x1DCE015CD384374El, 0x85030A4DE95E5736l);

	/**
	 * For client devs, allows you to get EaglercraftVersion.clientBrandUUID of
	 * other players on a server, to detect other players who also use your client.
	 * 
	 * Requires EaglerXBungee 1.3.0 or EaglerXVelocity 1.1.0
	 * 
	 * @return NULL_UUID if not found, PENDING_UUID if pending,
	 *         VANILLA_UUID if vanilla, or the remote player's
	 *         client's EaglercraftVersion.clientBrandUUID
	 */
	public static EaglercraftUUID getPlayerClientBrandUUID(EntityPlayer player) {
		EaglercraftUUID ret = null;
		if(player instanceof AbstractClientPlayer) {
			ret = ((AbstractClientPlayer)player).clientBrandUUIDCache;
			if(ret == null) {
				Minecraft mc = Minecraft.getMinecraft();
				if(mc != null && mc.thePlayer != null && mc.thePlayer.sendQueue.getEaglerMessageProtocol().ver >= 4) {
					ret = PENDING_UUID;
					EaglercraftUUID playerUUID = player.getUniqueID();
					if(!waitingUUIDs.containsKey(playerUUID) && !evictedUUIDs.containsKey(playerUUID)) {
						int reqID = ++requestId & 0x3FFF;
						WaitingLookup newLookup = new WaitingLookup(reqID, playerUUID, EagRuntime.steadyTimeMillis(),
								(AbstractClientPlayer) player);
						waitingIDs.put(reqID, newLookup);
						waitingUUIDs.put(playerUUID, newLookup);
						mc.thePlayer.sendQueue.sendEaglerMessage(
								new CPacketGetOtherClientUUIDV4EAG(reqID, newLookup.uuid.msb, newLookup.uuid.lsb));
					}
				}
			}
		}else if(player instanceof EntityPlayerMP) {
			ret = ((EntityPlayerMP)player).clientBrandUUID;
		}
		if(ret == null) {
			ret = NULL_UUID;
		}
		return ret;
	}

	private static final Map<Integer,WaitingLookup> waitingIDs = new HashMap<>();
	private static final Map<EaglercraftUUID,WaitingLookup> waitingUUIDs = new HashMap<>();
	private static final Map<EaglercraftUUID,Long> evictedUUIDs = new HashMap<>();

	private static int requestId = 0;
	private static long lastFlushReq = EagRuntime.steadyTimeMillis();
	private static long lastFlushEvict = EagRuntime.steadyTimeMillis();

	public static void update() {
		long timestamp = EagRuntime.steadyTimeMillis();
		if(timestamp - lastFlushReq > 5000l) {
			lastFlushReq = timestamp;
			if(!waitingIDs.isEmpty()) {
				Iterator<WaitingLookup> itr = waitingIDs.values().iterator();
				while(itr.hasNext()) {
					WaitingLookup lookup = itr.next();
					if(timestamp - lookup.timestamp > 15000l) {
						itr.remove();
						waitingUUIDs.remove(lookup.uuid);
					}
				}
			}
		}
		if(timestamp - lastFlushEvict > 1000l) {
			lastFlushEvict = timestamp;
			if(!evictedUUIDs.isEmpty()) {
				Iterator<Long> evictItr = evictedUUIDs.values().iterator();
				while(evictItr.hasNext()) {
					if(timestamp - evictItr.next().longValue() > 3000l) {
						evictItr.remove();
					}
				}
			}
		}
	}

	public static void flushRequestCache() {
		waitingIDs.clear();
		waitingUUIDs.clear();
		evictedUUIDs.clear();
	}

	public static void handleResponse(int requestId, EaglercraftUUID clientId) {
		WaitingLookup lookup = waitingIDs.remove(requestId);
		if(lookup != null) {
			lookup.player.clientBrandUUIDCache = clientId;
			waitingUUIDs.remove(lookup.uuid);
		}else {
			logger.warn("Unsolicited client brand UUID lookup response #{} recieved! (Brand UUID: {})", requestId, clientId);
		}
	}

	public static void evict(EaglercraftUUID clientId) {
		evictedUUIDs.put(clientId, Long.valueOf(EagRuntime.steadyTimeMillis()));
		WaitingLookup lk = waitingUUIDs.remove(clientId);
		if(lk != null) {
			waitingIDs.remove(lk.reqID);
		}
	}

	private static class WaitingLookup {

		private final int reqID;
		private final EaglercraftUUID uuid;
		private final long timestamp;
		private final AbstractClientPlayer player;

		public WaitingLookup(int reqID, EaglercraftUUID uuid, long timestamp, AbstractClientPlayer player) {
			this.reqID = reqID;
			this.uuid = uuid;
			this.timestamp = timestamp;
			this.player = player;
		}

	}
}
