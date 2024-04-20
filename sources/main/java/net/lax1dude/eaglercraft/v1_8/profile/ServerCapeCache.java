package net.lax1dude.eaglercraft.v1_8.profile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.socket.EaglercraftNetworkManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;

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
public class ServerCapeCache {

	private static final Logger logger = LogManager.getLogger("ServerCapeCache");

	public class CapeCacheEntry {
		
		protected final boolean isPresetCape;
		protected final int presetCapeId;
		protected final CacheCustomCape customCape;
		
		protected long lastCacheHit = System.currentTimeMillis();
		
		protected CapeCacheEntry(EaglerSkinTexture textureInstance, ResourceLocation resourceLocation) {
			this.isPresetCape = false;
			this.presetCapeId = -1;
			this.customCape = new CacheCustomCape(textureInstance, resourceLocation);
			ServerCapeCache.this.textureManager.loadTexture(resourceLocation, textureInstance);
		}
		
		/**
		 * Use only for the constant for the client player
		 */
		protected CapeCacheEntry(ResourceLocation resourceLocation) {
			this.isPresetCape = false;
			this.presetCapeId = -1;
			this.customCape = new CacheCustomCape(null, resourceLocation);
		}
		
		protected CapeCacheEntry(int presetSkinId) {
			this.isPresetCape = true;
			this.presetCapeId = presetSkinId;
			this.customCape = null;
		}
		
		public ResourceLocation getResourceLocation() {
			if(isPresetCape) {
				return DefaultCapes.getCapeFromId(presetCapeId).location;
			}else {
				if(customCape != null) {
					return customCape.resourceLocation;
				}else {
					return null;
				}
			}
		}
		
		protected void free() {
			if(!isPresetCape && customCape.resourceLocation != null) {
				ServerCapeCache.this.textureManager.deleteTexture(customCape.resourceLocation);
			}
		}

	}

	protected static class CacheCustomCape {
		
		protected final EaglerSkinTexture textureInstance;
		protected final ResourceLocation resourceLocation;
		
		protected CacheCustomCape(EaglerSkinTexture textureInstance, ResourceLocation resourceLocation) {
			this.textureInstance = textureInstance;
			this.resourceLocation = resourceLocation;
		}

	}

	private final CapeCacheEntry defaultCacheEntry = new CapeCacheEntry(0);
	private final Map<EaglercraftUUID, CapeCacheEntry> capesCache = new HashMap();
	private final Map<EaglercraftUUID, Long> waitingCapes = new HashMap();
	private final Map<EaglercraftUUID, Long> evictedCapes = new HashMap();

	private final EaglercraftNetworkManager networkManager;
	protected final TextureManager textureManager;
	
	private final EaglercraftUUID clientPlayerId;
	private final CapeCacheEntry clientPlayerCacheEntry;

	private long lastFlush = System.currentTimeMillis();
	private long lastFlushReq = System.currentTimeMillis();
	private long lastFlushEvict = System.currentTimeMillis();

	private static int texId = 0;

	public ServerCapeCache(EaglercraftNetworkManager networkManager, TextureManager textureManager) {
		this.networkManager = networkManager;
		this.textureManager = textureManager;
		this.clientPlayerId = EaglerProfile.getPlayerUUID();
		this.clientPlayerCacheEntry = new CapeCacheEntry(EaglerProfile.getActiveCapeResourceLocation());
	}

	public CapeCacheEntry getClientPlayerCape() {
		return clientPlayerCacheEntry;
	}

	public CapeCacheEntry getCape(EaglercraftUUID player) {
		if(player.equals(clientPlayerId)) {
			return clientPlayerCacheEntry;
		}
		CapeCacheEntry etr = capesCache.get(player);
		if(etr == null) {
			if(!waitingCapes.containsKey(player) && !evictedCapes.containsKey(player)) {
				waitingCapes.put(player, System.currentTimeMillis());
				PacketBuffer buffer;
				try {
					buffer = CapePackets.writeGetOtherCape(player);
				}catch(IOException ex) {
					logger.error("Could not write cape request packet!");
					logger.error(ex);
					return defaultCacheEntry;
				}
				networkManager.sendPacket(new C17PacketCustomPayload("EAG|Capes-1.8", buffer));
			}
			return defaultCacheEntry;
		}else {
			etr.lastCacheHit = System.currentTimeMillis();
			return etr;
		}
	}

	public void cacheCapePreset(EaglercraftUUID player, int presetId) {
		if(waitingCapes.remove(player) != null) {
			CapeCacheEntry etr = capesCache.remove(player);
			if(etr != null) {
				etr.free();
			}
			capesCache.put(player, new CapeCacheEntry(presetId));
		}else {
			logger.error("Unsolicited cape response recieved for \"{}\"! (preset {})", player, presetId);
		}
	}

	public void cacheCapeCustom(EaglercraftUUID player, byte[] pixels) {
		if(waitingCapes.remove(player) != null) {
			CapeCacheEntry etr = capesCache.remove(player);
			if(etr != null) {
				etr.free();
			}
			byte[] pixels32x32 = new byte[4096];
			SkinConverter.convertCape23x17RGBto32x32RGBA(pixels, pixels32x32);
			try {
				etr = new CapeCacheEntry(new EaglerSkinTexture(pixels32x32, 32, 32),
						new ResourceLocation("eagler:capes/multiplayer/tex_" + texId++));
			}catch(Throwable t) {
				etr = new CapeCacheEntry(0);
				logger.error("Could not process custom skin packet for \"{}\"!", player);
				logger.error(t);
			}
			capesCache.put(player, etr);
		}else {
			logger.error("Unsolicited skin response recieved for \"{}\"!", player);
		}
	}

	public void flush() {
		long millis = System.currentTimeMillis();
		if(millis - lastFlushReq > 5000l) {
			lastFlushReq = millis;
			if(!waitingCapes.isEmpty()) {
				Iterator<Long> waitingItr = waitingCapes.values().iterator();
				while(waitingItr.hasNext()) {
					if(millis - waitingItr.next().longValue() > 30000l) {
						waitingItr.remove();
					}
				}
			}
		}
		if(millis - lastFlushEvict > 1000l) {
			lastFlushEvict = millis;
			if(!evictedCapes.isEmpty()) {
				Iterator<Long> evictItr = evictedCapes.values().iterator();
				while(evictItr.hasNext()) {
					if(millis - evictItr.next().longValue() > 3000l) {
						evictItr.remove();
					}
				}
			}
		}
		if(millis - lastFlush > 60000l) {
			lastFlush = millis;
			if(!capesCache.isEmpty()) {
				Iterator<CapeCacheEntry> entryItr = capesCache.values().iterator();
				while(entryItr.hasNext()) {
					CapeCacheEntry etr = entryItr.next();
					if(millis - etr.lastCacheHit > 900000l) { // 15 minutes
						entryItr.remove();
						etr.free();
					}
				}
			}
		}
	}

	public void destroy() {
		Iterator<CapeCacheEntry> entryItr = capesCache.values().iterator();
		while(entryItr.hasNext()) {
			entryItr.next().free();
		}
		capesCache.clear();
		waitingCapes.clear();
		evictedCapes.clear();
	}

	public void evictCape(EaglercraftUUID uuid) {
		evictedCapes.put(uuid, Long.valueOf(System.currentTimeMillis()));
		CapeCacheEntry etr = capesCache.remove(uuid);
		if(etr != null) {
			etr.free();
		}
	}

}
