package net.lax1dude.eaglercraft.v1_8.profile;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.TexturesProperty;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketGetOtherSkinEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.client.CPacketGetSkinByURLEAG;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

/**
 * Copyright (c) 2022-2023 lax1dude, ayunami2000. All Rights Reserved.
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
public class ServerSkinCache {

	private static final Logger logger = LogManager.getLogger("ServerSkinCache");

	public class SkinCacheEntry {
		
		protected final boolean isPresetSkin;
		protected final int presetSkinId;
		protected final CacheCustomSkin customSkin;
		
		protected long lastCacheHit = EagRuntime.steadyTimeMillis();
		
		protected SkinCacheEntry(EaglerSkinTexture textureInstance, ResourceLocation resourceLocation, SkinModel model) {
			this.isPresetSkin = false;
			this.presetSkinId = -1;
			this.customSkin = new CacheCustomSkin(textureInstance, resourceLocation, model);
			ServerSkinCache.this.textureManager.loadTexture(resourceLocation, textureInstance);
		}
		
		/**
		 * Use only for the constant for the client player
		 */
		protected SkinCacheEntry(ResourceLocation resourceLocation, SkinModel model) {
			this.isPresetSkin = false;
			this.presetSkinId = -1;
			this.customSkin = new CacheCustomSkin(null, resourceLocation, model);
		}
		
		protected SkinCacheEntry(int presetSkinId) {
			this.isPresetSkin = true;
			this.presetSkinId = presetSkinId;
			this.customSkin = null;
		}
		
		public ResourceLocation getResourceLocation() {
			if(isPresetSkin) {
				return DefaultSkins.getSkinFromId(presetSkinId).location;
			}else {
				if(customSkin != null) {
					return customSkin.resourceLocation;
				}else {
					return DefaultSkins.DEFAULT_STEVE.location;
				}
			}
		}
		
		public SkinModel getSkinModel() {
			if(isPresetSkin) {
				return DefaultSkins.getSkinFromId(presetSkinId).model;
			}else {
				if(customSkin != null) {
					return customSkin.model;
				}else {
					return DefaultSkins.DEFAULT_STEVE.model;
				}
			}
		}
		
		protected void free() {
			if(!isPresetSkin) {
				ServerSkinCache.this.textureManager.deleteTexture(customSkin.resourceLocation);
			}
		}

	}

	protected static class CacheCustomSkin {
		
		protected final EaglerSkinTexture textureInstance;
		protected final ResourceLocation resourceLocation;
		protected final SkinModel model;
		
		protected CacheCustomSkin(EaglerSkinTexture textureInstance, ResourceLocation resourceLocation, SkinModel model) {
			this.textureInstance = textureInstance;
			this.resourceLocation = resourceLocation;
			this.model = model;
		}

	}

	protected static class WaitingSkin {
		
		protected final long timeout;
		protected final SkinModel model;
		
		protected WaitingSkin(long timeout, SkinModel model) {
			this.timeout = timeout;
			this.model = model;
		}

	}

	private final SkinCacheEntry defaultCacheEntry = new SkinCacheEntry(0);
	private final SkinCacheEntry defaultSlimCacheEntry = new SkinCacheEntry(1);
	private final Map<EaglercraftUUID, SkinCacheEntry> skinsCache = new HashMap<>();
	private final Map<EaglercraftUUID, WaitingSkin> waitingSkins = new HashMap<>();
	private final Map<EaglercraftUUID, Long> evictedSkins = new HashMap<>();

	private final NetHandlerPlayClient netHandler;
	protected final TextureManager textureManager;
	
	private final EaglercraftUUID clientPlayerId;
	private SkinCacheEntry clientPlayerCacheEntry;

	private long lastFlush = EagRuntime.steadyTimeMillis();
	private long lastFlushReq = EagRuntime.steadyTimeMillis();
	private long lastFlushEvict = EagRuntime.steadyTimeMillis();

	private static int texId = 0;
	public static boolean needReloadClientSkin = false;

	public ServerSkinCache(NetHandlerPlayClient netHandler, TextureManager textureManager) {
		this.netHandler = netHandler;
		this.textureManager = textureManager;
		this.clientPlayerId = EaglerProfile.getPlayerUUID();
		reloadClientPlayerSkin();
	}

	public void reloadClientPlayerSkin() {
		needReloadClientSkin = false;
		this.clientPlayerCacheEntry = new SkinCacheEntry(EaglerProfile.getActiveSkinResourceLocation(), EaglerProfile.getActiveSkinModel());
	}

	public SkinCacheEntry getClientPlayerSkin() {
		return clientPlayerCacheEntry;
	}

	public SkinCacheEntry getSkin(GameProfile player) {
		EaglercraftUUID uuid = player.getId();
		if(uuid != null && uuid.equals(clientPlayerId)) {
			return clientPlayerCacheEntry;
		}
		TexturesProperty props = player.getTextures();
		if(props.eaglerPlayer || props.skin == null) {
			if(uuid != null) {
				return _getSkin(uuid);
			}else {
				if("slim".equalsIgnoreCase(props.model)) {
					return defaultSlimCacheEntry;
				}else {
					return defaultCacheEntry;
				}
			}
		}else {
			return getSkin(props.skin, SkinModel.getModelFromId(props.model));
		}
	}

	public SkinCacheEntry getSkin(EaglercraftUUID player) {
		if(player.equals(clientPlayerId)) {
			return clientPlayerCacheEntry;
		}
		return _getSkin(player);
	}

	private SkinCacheEntry _getSkin(EaglercraftUUID player) {
		SkinCacheEntry etr = skinsCache.get(player);
		if(etr == null) {
			if(!waitingSkins.containsKey(player) && !evictedSkins.containsKey(player)) {
				waitingSkins.put(player, new WaitingSkin(EagRuntime.steadyTimeMillis(), null));
				netHandler.sendEaglerMessage(new CPacketGetOtherSkinEAG(player.msb, player.lsb));
			}
			return defaultCacheEntry;
		}else {
			etr.lastCacheHit = EagRuntime.steadyTimeMillis();
			return etr;
		}
	}

	public SkinCacheEntry getSkin(String url, SkinModel skinModelResponse) {
		if(url.length() > 0x7F00) {
			return skinModelResponse == SkinModel.ALEX ? defaultSlimCacheEntry : defaultCacheEntry;
		}
		EaglercraftUUID generatedUUID = SkinPackets.createEaglerURLSkinUUID(url);
		SkinCacheEntry etr = skinsCache.get(generatedUUID);
		if(etr != null) {
			etr.lastCacheHit = EagRuntime.steadyTimeMillis();
			return etr;
		}else {
			if(!waitingSkins.containsKey(generatedUUID) && !evictedSkins.containsKey(generatedUUID)) {
				waitingSkins.put(generatedUUID, new WaitingSkin(EagRuntime.steadyTimeMillis(), skinModelResponse));
				netHandler.sendEaglerMessage(new CPacketGetSkinByURLEAG(generatedUUID.msb, generatedUUID.lsb, url));
			}
		}
		return skinModelResponse == SkinModel.ALEX ? defaultSlimCacheEntry : defaultCacheEntry;
	}

	public void cacheSkinPreset(EaglercraftUUID player, int presetId) {
		if(waitingSkins.remove(player) != null) {
			SkinCacheEntry etr = skinsCache.remove(player);
			if(etr != null) {
				etr.free();
			}
			skinsCache.put(player, new SkinCacheEntry(presetId));
		}else {
			logger.error("Unsolicited skin response recieved for \"{}\"! (preset {})", player, presetId);
		}
	}
	
	public void cacheSkinCustom(EaglercraftUUID player, byte[] pixels, SkinModel model) {
		WaitingSkin waitingSkin;
		if((waitingSkin = waitingSkins.remove(player)) != null) {
			SkinCacheEntry etr = skinsCache.remove(player);
			if(etr != null) {
				etr.free();
			}
			if(waitingSkin.model != null) {
				model = waitingSkin.model;
			}else if(model == null) {
				model = (player.hashCode() & 1) != 0 ? SkinModel.ALEX : SkinModel.STEVE;
			}
			try {
				etr = new SkinCacheEntry(new EaglerSkinTexture(pixels, model.width, model.height),
						new ResourceLocation("eagler:skins/multiplayer/tex_" + texId++), model);
			}catch(Throwable t) {
				etr = new SkinCacheEntry(0);
				logger.error("Could not process custom skin packet for \"{}\"!", player);
				logger.error(t);
			}
			skinsCache.put(player, etr);
		}else {
			logger.error("Unsolicited skin response recieved for \"{}\"! (custom {}x{})", player, model.width, model.height);
		}
	}
	
	public SkinModel getRequestedSkinType(EaglercraftUUID waiting) {
		WaitingSkin waitingSkin;
		if((waitingSkin = waitingSkins.get(waiting)) != null) {
			return waitingSkin.model;
		}else {
			return null;
		}
	}
	
	public void flush() {
		long millis = EagRuntime.steadyTimeMillis();
		if(millis - lastFlushReq > 5000l) {
			lastFlushReq = millis;
			if(!waitingSkins.isEmpty()) {
				Iterator<WaitingSkin> waitingItr = waitingSkins.values().iterator();
				while(waitingItr.hasNext()) {
					if(millis - waitingItr.next().timeout > 20000l) {
						waitingItr.remove();
					}
				}
			}
		}
		if(millis - lastFlushEvict > 1000l) {
			lastFlushEvict = millis;
			if(!evictedSkins.isEmpty()) {
				Iterator<Long> evictItr = evictedSkins.values().iterator();
				while(evictItr.hasNext()) {
					if(millis - evictItr.next().longValue() > 3000l) {
						evictItr.remove();
					}
				}
			}
		}
		if(millis - lastFlush > 60000l) {
			lastFlush = millis;
			if(!skinsCache.isEmpty()) {
				Iterator<SkinCacheEntry> entryItr = skinsCache.values().iterator();
				while(entryItr.hasNext()) {
					SkinCacheEntry etr = entryItr.next();
					if(millis - etr.lastCacheHit > 900000l) { // 15 minutes
						entryItr.remove();
						etr.free();
					}
				}
			}
		}
		if(needReloadClientSkin) {
			reloadClientPlayerSkin();
		}
	}
	
	public void destroy() {
		Iterator<SkinCacheEntry> entryItr = skinsCache.values().iterator();
		while(entryItr.hasNext()) {
			entryItr.next().free();
		}
		skinsCache.clear();
		waitingSkins.clear();
		evictedSkins.clear();
	}
	
	public void evictSkin(EaglercraftUUID uuid) {
		evictedSkins.put(uuid, Long.valueOf(EagRuntime.steadyTimeMillis()));
		SkinCacheEntry etr = skinsCache.remove(uuid);
		if(etr != null) {
			etr.free();
		}
	}

	public void handleInvalidate(EaglercraftUUID uuid) {
		SkinCacheEntry etr = skinsCache.remove(uuid);
		if(etr != null) {
			etr.free();
		}
	}

}
