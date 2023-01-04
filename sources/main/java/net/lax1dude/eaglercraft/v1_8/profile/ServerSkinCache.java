package net.lax1dude.eaglercraft.v1_8.profile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.TexturesProperty;
import net.lax1dude.eaglercraft.v1_8.socket.EaglercraftNetworkManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ResourceLocation;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info) 
 * 
 */
public class ServerSkinCache {

	private static final Logger logger = LogManager.getLogger("ServerSkinCache");

	public class SkinCacheEntry {
		
		protected final boolean isPresetSkin;
		protected final int presetSkinId;
		protected final CacheCustomSkin customSkin;
		
		protected long lastCacheHit = System.currentTimeMillis();
		
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
	private final Map<EaglercraftUUID, SkinCacheEntry> skinsCache = new HashMap();
	private final Map<EaglercraftUUID, WaitingSkin> waitingSkins = new HashMap();
	private final Map<EaglercraftUUID, Long> evictedSkins = new HashMap();

	private final EaglercraftNetworkManager networkManager;
	protected final TextureManager textureManager;
	
	private final EaglercraftUUID clientPlayerId;
	private final SkinCacheEntry clientPlayerCacheEntry;

	private long lastFlush = System.currentTimeMillis();
	private long lastFlushReq = System.currentTimeMillis();
	private long lastFlushEvict = System.currentTimeMillis();

	private static int texId = 0;

	public ServerSkinCache(EaglercraftNetworkManager networkManager, TextureManager textureManager) {
		this.networkManager = networkManager;
		this.textureManager = textureManager;
		this.clientPlayerId = EaglerProfile.getPlayerUUID();
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
				waitingSkins.put(player, new WaitingSkin(System.currentTimeMillis(), null));
				PacketBuffer buffer;
				try {
					buffer = SkinPackets.writeGetOtherSkin(player);
				}catch(IOException ex) {
					logger.error("Could not write skin request packet!");
					logger.error(ex);
					return defaultCacheEntry;
				}
				networkManager.sendPacket(new C17PacketCustomPayload("EAG|Skins-1.8", buffer));
			}
			return defaultCacheEntry;
		}else {
			etr.lastCacheHit = System.currentTimeMillis();
			return etr;
		}
	}

	public SkinCacheEntry getSkin(String url, SkinModel skinModelResponse) {
		if(url.length() > 0xFFFF) {
			return skinModelResponse == SkinModel.ALEX ? defaultSlimCacheEntry : defaultCacheEntry;
		}
		EaglercraftUUID generatedUUID = SkinPackets.createEaglerURLSkinUUID(url);
		SkinCacheEntry etr = skinsCache.get(generatedUUID);
		if(etr != null) {
			etr.lastCacheHit = System.currentTimeMillis();
			return etr;
		}else {
			if(!waitingSkins.containsKey(generatedUUID) && !evictedSkins.containsKey(generatedUUID)) {
				waitingSkins.put(generatedUUID, new WaitingSkin(System.currentTimeMillis(), skinModelResponse));
				PacketBuffer buffer;
				try {
					buffer = SkinPackets.writeGetSkinByURL(generatedUUID, url);
				}catch(IOException ex) {
					logger.error("Could not write skin request packet!");
					logger.error(ex);
					return skinModelResponse == SkinModel.ALEX ? defaultSlimCacheEntry : defaultCacheEntry;
				}
				networkManager.sendPacket(new C17PacketCustomPayload("EAG|Skins-1.8", buffer));
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
		long millis = System.currentTimeMillis();
		if(millis - lastFlushReq > 5000l) {
			lastFlushReq = millis;
			if(!waitingSkins.isEmpty()) {
				Iterator<WaitingSkin> waitingItr = waitingSkins.values().iterator();
				while(waitingItr.hasNext()) {
					if(millis - waitingItr.next().timeout > 30000l) {
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
	}
	
	public void destroy() {
		Iterator<SkinCacheEntry> entryItr = skinsCache.values().iterator();
		while(entryItr.hasNext()) {
			entryItr.next().free();
		}
		skinsCache.clear();
		waitingSkins.clear();
	}
	
	public void evictSkin(EaglercraftUUID uuid) {
		evictedSkins.put(uuid, Long.valueOf(System.currentTimeMillis()));
		SkinCacheEntry etr = skinsCache.remove(uuid);
		if(etr != null) {
			etr.free();
		}
	}

}
