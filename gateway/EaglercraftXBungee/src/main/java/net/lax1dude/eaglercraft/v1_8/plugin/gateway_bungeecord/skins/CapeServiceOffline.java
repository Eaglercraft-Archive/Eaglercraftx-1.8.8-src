package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
import net.md_5.bungee.UserConnection;

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
public class CapeServiceOffline {

	public static final int masterRateLimitPerPlayer = 250;

	public static final String CHANNEL = "EAG|Capes-1.8";

	private final Map<UUID, byte[]> capesCache = new HashMap();

	public void registerEaglercraftPlayer(UUID playerUUID, byte[] capePacket) {
		synchronized(capesCache) {
			capesCache.put(playerUUID, capePacket);
		}
	}

	public void processGetOtherCape(UUID searchUUID, UserConnection sender) {
		if(((EaglerInitialHandler)sender.getPendingConnection()).skinLookupRateLimiter.rateLimit(masterRateLimitPerPlayer)) {
			byte[] maybeCape;
			synchronized(capesCache) {
				maybeCape = capesCache.get(searchUUID);
			}
			if(maybeCape != null) {
				sender.sendData(CapeServiceOffline.CHANNEL, maybeCape);
			}else {
				sender.sendData(CapeServiceOffline.CHANNEL, CapePackets.makePresetResponse(searchUUID, 0));
			}
		}
	}

	public void unregisterPlayer(UUID playerUUID) {
		synchronized(capesCache) {
			capesCache.remove(playerUUID);
		}
	}

	public void shutdown() {
		synchronized(capesCache) {
			capesCache.clear();
		}
	}
}
