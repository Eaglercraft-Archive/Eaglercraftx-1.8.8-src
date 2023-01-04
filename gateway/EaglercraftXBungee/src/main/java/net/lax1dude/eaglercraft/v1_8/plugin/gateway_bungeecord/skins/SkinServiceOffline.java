package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
import net.md_5.bungee.UserConnection;

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
public class SkinServiceOffline implements ISkinService {

	public static final int masterRateLimitPerPlayer = 250;

	private static class CachedSkin {

		protected final UUID uuid;
		protected final byte[] packet;

		protected CachedSkin(UUID uuid, byte[] packet) {
			this.uuid = uuid;
			this.packet = packet;
		}

	}

	private final Map<UUID, CachedSkin> skinCache = new HashMap();

	private final Multimap<UUID, UUID> onlinePlayersFromTexturesMap = MultimapBuilder.hashKeys().hashSetValues().build();

	public void init(String uri, String driverClass, String driverPath, int keepObjectsDays, int keepProfilesDays,
			int maxObjects, int maxProfiles) {
		synchronized(skinCache) {
			skinCache.clear();
		}
	}

	public void processGetOtherSkin(UUID searchUUID, UserConnection sender) {
		if(((EaglerInitialHandler)sender.getPendingConnection()).skinLookupRateLimiter.rateLimit(masterRateLimitPerPlayer)) {
			CachedSkin cached;
			synchronized(skinCache) {
				cached = skinCache.get(searchUUID);
			}
			if(cached != null) {
				sender.sendData(SkinService.CHANNEL, cached.packet);
			}else {
				sender.sendData(SkinService.CHANNEL, SkinPackets.makePresetResponse(searchUUID));
			}
		}
	}

	public void processGetOtherSkin(UUID searchUUID, String skinURL, UserConnection sender) {
		Collection<UUID> uuids;
		synchronized(onlinePlayersFromTexturesMap) {
			uuids = onlinePlayersFromTexturesMap.get(searchUUID);
		}
		if(uuids.size() > 0) {
			CachedSkin cached;
			synchronized(skinCache) {
				Iterator<UUID> uuidItr = uuids.iterator();
				while(uuidItr.hasNext()) {
					cached = skinCache.get(uuidItr.next());
					if(cached != null) {
						sender.sendData(SkinService.CHANNEL, SkinPackets.rewriteUUID(searchUUID, cached.packet));
					}
				}
			}
		}
		sender.sendData(SkinService.CHANNEL, SkinPackets.makePresetResponse(searchUUID));
	}

	public void registerEaglercraftPlayer(UUID clientUUID, byte[] generatedPacket, int modelId) throws IOException {
		synchronized(skinCache) {
			skinCache.put(clientUUID, new CachedSkin(clientUUID, generatedPacket));
		}
	}

	public void unregisterPlayer(UUID clientUUID) {
		synchronized(skinCache) {
			skinCache.remove(clientUUID);
		}
	}

	public void registerTextureToPlayerAssociation(UUID textureUUID, UUID playerUUID) {
		synchronized(onlinePlayersFromTexturesMap) {
			onlinePlayersFromTexturesMap.put(textureUUID, playerUUID);
		}
	}

	public void flush() {
		// no
	}

	public void shutdown() {
		synchronized(skinCache) {
			skinCache.clear();
		}
	}

}
