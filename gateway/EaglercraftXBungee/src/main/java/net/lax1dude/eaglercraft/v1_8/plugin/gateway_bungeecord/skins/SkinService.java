/*
 * Copyright (c) 2022-2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import org.apache.commons.codec.binary.Base64;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.api.EaglerXBungeeAPIHelper;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.config.EaglerBungeeConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.server.EaglerInitialHandler;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.AsyncSkinProvider.CacheFetchedProfile;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.AsyncSkinProvider.CancelException;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketForceClientSkinPresetV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinPresetEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SkinPacketVersionCache;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.protocol.Property;

public class SkinService implements ISkinService {

	public static final int masterRateLimitPerPlayer = 250;

	private final ConcurrentMap<UUID, CachedPlayerSkin> onlinePlayersCache = new ConcurrentHashMap<>();
	private final ConcurrentMap<UUID, UUID> onlinePlayersToTexturesMap = new ConcurrentHashMap<>();
	private final ConcurrentMap<UUID, CachedForeignSkin> foreignSkinCache = new ConcurrentHashMap<>();

	private final ReadWriteLock onlinePlayersFromTexturesMapLock = new ReentrantReadWriteLock();
	private final Multimap<UUID, UUID> onlinePlayersFromTexturesMap = MultimapBuilder.hashKeys().hashSetValues().build();

	private final Map<UUID, PendingTextureDownload> pendingTextures = new HashMap<>();
	private final Map<UUID, PendingProfileUUIDLookup> pendingUUIDs = new HashMap<>();
	private final Map<String, PendingProfileNameLookup> pendingNameLookups = new HashMap<>();

	private final ReadWriteLock antagonistsLock = new ReentrantReadWriteLock();
	private final TObjectIntMap<UUID> antagonists = new TObjectIntHashMap<>();

	private long antagonistCooldown = EaglerXBungeeAPIHelper.steadyTimeMillis();

	private final Consumer<Set<UUID>> antagonistLogger = new Consumer<Set<UUID>>() {

		@Override
		public void accept(Set<UUID> t) {
			if(t.size() == 1) {
				int limit = EaglerXBungee.getEagler().getConfig().getAntagonistsRateLimit() << 1;
				UUID offender = t.iterator().next();
				antagonistsLock.writeLock().lock();
				try {
					int v = antagonists.get(offender);
					if(v == antagonists.getNoEntryValue()) {
						antagonists.put(offender, 1);
					}else {
						if(v <= limit) {
							antagonists.put(offender, v + 1);
						}
					}
				}finally {
					antagonistsLock.writeLock().unlock();
				}
			}
		}

	};

	private ICacheProvider cacheProvider = null;

	protected static class CachedForeignSkin {

		protected final UUID uuid;
		protected final SkinPacketVersionCache data;
		protected final int modelKnown;
		protected volatile long lastHit;

		protected CachedForeignSkin(UUID uuid, SkinPacketVersionCache data, int modelKnown) {
			this.uuid = uuid;
			this.data = data;
			this.modelKnown = modelKnown;
			this.lastHit = EaglerXBungeeAPIHelper.steadyTimeMillis();
		}

	}

	protected static class CachedPlayerSkin {

		protected final SkinPacketVersionCache data;
		protected final UUID textureUUID;
		protected final int modelId;

		protected CachedPlayerSkin(SkinPacketVersionCache data, UUID textureUUID, int modelId) {
			this.data = data;
			this.textureUUID = textureUUID;
			this.modelId = modelId;
		}

	}
	
	protected class PendingTextureDownload implements Consumer<byte[]> {

		protected final UUID textureUUID;
		protected final String textureURL;

		protected final Set<UUID> antagonists;
		protected final List<Consumer<byte[]>> callbacks;
		protected final Consumer<Set<UUID>> antagonistsCallback;

		protected final long initializedTime;
		protected volatile boolean finalized;

		protected PendingTextureDownload(UUID textureUUID, String textureURL, UUID caller, Consumer<byte[]> callback,
				Consumer<Set<UUID>> antagonistsCallback) {
			this.textureUUID = textureUUID;
			this.textureURL = textureURL;
			this.antagonists = new LinkedHashSet<>();
			this.antagonists.add(caller);
			this.callbacks = new LinkedList<>();
			this.callbacks.add(callback);
			this.antagonistsCallback = antagonistsCallback;
			this.initializedTime = EaglerXBungeeAPIHelper.steadyTimeMillis();
			this.finalized = false;
		}

		@Override
		public void accept(byte[] t) {
			for(int i = 0, l = callbacks.size(); i < l; ++i) {
				try {
					callbacks.get(i).accept(t);
				}catch(Throwable t2) {
				}
			}
			if(t != null) {
				synchronized(pendingTextures) {
					finalized = true;
					pendingTextures.remove(textureUUID);
				}
			}
		}

	}
	
	protected class PendingProfileUUIDLookup implements Consumer<CacheFetchedProfile> {

		protected final UUID profileUUID;

		protected final Set<UUID> antagonists;
		protected final List<Consumer<CacheFetchedProfile>> callbacks;
		protected final Consumer<Set<UUID>> antagonistsCallback;

		protected final long initializedTime;
		protected volatile boolean finalized;

		protected PendingProfileUUIDLookup(UUID profileUUID, UUID caller, Consumer<CacheFetchedProfile> callback,
				Consumer<Set<UUID>> antagonistsCallback) {
			this.profileUUID = profileUUID;
			this.antagonists = new LinkedHashSet<>();
			this.antagonists.add(caller);
			this.callbacks = new LinkedList<>();
			this.callbacks.add(callback);
			this.antagonistsCallback = antagonistsCallback;
			this.initializedTime = EaglerXBungeeAPIHelper.steadyTimeMillis();
			this.finalized = false;
		}

		@Override
		public void accept(CacheFetchedProfile t) {
			for(int i = 0, l = callbacks.size(); i < l; ++i) {
				try {
					callbacks.get(i).accept(t);
				}catch(Throwable t2) {
				}
			}
			if(t != null) {
				synchronized(pendingUUIDs) {
					finalized = true;
					pendingUUIDs.remove(profileUUID);
				}
			}
		}

	}
	
	protected class PendingProfileNameLookup implements Consumer<CacheFetchedProfile> {

		protected final String profileName;

		protected final Set<UUID> antagonists;
		protected final List<Consumer<CacheFetchedProfile>> callbacks;
		protected final Consumer<Set<UUID>> antagonistsCallback;

		protected final long initializedTime;
		protected volatile boolean finalized;

		protected PendingProfileNameLookup(String profileName, UUID caller, Consumer<CacheFetchedProfile> callback,
				Consumer<Set<UUID>> antagonistsCallback) {
			this.profileName = profileName;
			this.antagonists = new LinkedHashSet<>();
			this.antagonists.add(caller);
			this.callbacks = new LinkedList<>();
			this.callbacks.add(callback);
			this.antagonistsCallback = antagonistsCallback;
			this.initializedTime = EaglerXBungeeAPIHelper.steadyTimeMillis();
			this.finalized = false;
		}

		@Override
		public void accept(CacheFetchedProfile t) {
			for(int i = 0, l = callbacks.size(); i < l; ++i) {
				try {
					callbacks.get(i).accept(t);
				}catch(Throwable t2) {
				}
			}
			if(t != null) {
				synchronized(pendingNameLookups) {
					finalized = true;
					pendingNameLookups.remove(profileName);
				}
			}
		}

	}
	
	public void init(String uri, String driverClass, String driverPath, int keepObjectsDays, int keepProfilesDays,
			int maxObjects, int maxProfiles) {
		antagonistCooldown = EaglerXBungeeAPIHelper.steadyTimeMillis();
		if(cacheProvider == null) {
			cacheProvider = JDBCCacheProvider.initialize(uri, driverClass, driverPath, keepObjectsDays,
					keepProfilesDays, maxObjects, maxProfiles);
		}
		resetMaps();
	}
	
	public void processGetOtherSkin(final UUID searchUUID, final UserConnection sender) {
		EaglerInitialHandler eaglerHandler = (EaglerInitialHandler)sender.getPendingConnection();
		if(!eaglerHandler.skinLookupRateLimiter.rateLimit(masterRateLimitPerPlayer)) {
			return;
		}
		
		CachedPlayerSkin maybeCachedPacket = onlinePlayersCache.get(searchUUID);
		
		if(maybeCachedPacket != null) {
			eaglerHandler.sendEaglerMessage(maybeCachedPacket.data.get(eaglerHandler.getEaglerProtocol()));
		}else {
			ProxiedPlayer player = BungeeCord.getInstance().getPlayer(searchUUID);
			UUID playerTexture = onlinePlayersToTexturesMap.get(searchUUID);
			if(playerTexture != null) {
				Collection<UUID> possiblePlayers;
				onlinePlayersFromTexturesMapLock.readLock().lock();
				try {
					possiblePlayers = new ArrayList<>(onlinePlayersFromTexturesMap.get(playerTexture));
				}finally {
					onlinePlayersFromTexturesMapLock.readLock().unlock();
				}
				boolean playersExist = possiblePlayers.size() > 0;
				if(playersExist) {
					for(UUID uuid : possiblePlayers) {
						maybeCachedPacket = onlinePlayersCache.get(uuid);
						if(maybeCachedPacket != null) {
							SkinPacketVersionCache rewritten = SkinPacketVersionCache.rewriteUUID(
									maybeCachedPacket.data, searchUUID.getMostSignificantBits(),
									searchUUID.getLeastSignificantBits());
							if(player != null) {
								onlinePlayersCache.put(searchUUID, new CachedPlayerSkin(rewritten,
										maybeCachedPacket.textureUUID, maybeCachedPacket.modelId));
							}
							eaglerHandler.sendEaglerMessage(rewritten.get(eaglerHandler.getEaglerProtocol()));
							return;
						}
					}
				}
				CachedForeignSkin foreignSkin = foreignSkinCache.get(playerTexture);
				if(foreignSkin != null && foreignSkin.modelKnown != -1) {
					if(player != null) {
						onlinePlayersCache.put(searchUUID,
								new CachedPlayerSkin(SkinPacketVersionCache.rewriteUUID(foreignSkin.data,
										searchUUID.getMostSignificantBits(), searchUUID.getLeastSignificantBits()),
										playerTexture, foreignSkin.modelKnown));
						foreignSkinCache.remove(playerTexture);
					}else {
						foreignSkin.lastHit = EaglerXBungeeAPIHelper.steadyTimeMillis();
					}
					eaglerHandler.sendEaglerMessage(foreignSkin.data.get(eaglerHandler.getEaglerProtocol()));
					return;
				}
			}
			if(player != null && (player instanceof UserConnection)) {
				LoginResult loginProfile = ((UserConnection)player).getPendingConnection().getLoginProfile();
				if(loginProfile != null) {
					Property[] props = loginProfile.getProperties();
					if(props.length > 0) {
						for(int i = 0; i < props.length; ++i) {
							Property pp = props[i];
							if(pp.getName().equals("textures")) {
								try {
									String jsonStr = SkinPackets.bytesToAscii(Base64.decodeBase64(pp.getValue()));
									JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
									JsonObject skinObj = json.getAsJsonObject("SKIN");
									if(skinObj != null) {
										JsonElement url = json.get("url");
										if(url != null) {
											String urlStr = sanitizeTextureURL(url.getAsString());
											if(urlStr == null) {
												break;
											}
											int model = 0;
											JsonElement el = skinObj.get("metadata");
											if(el != null && el.isJsonObject()) {
												el = el.getAsJsonObject().get("model");
												if(el != null) {
													model = SkinPackets.getModelId(el.getAsString());
												}
											}
											UUID skinUUID = SkinPackets.createEaglerURLSkinUUID(urlStr);
											
											CachedForeignSkin foreignSkin = foreignSkinCache.remove(skinUUID);
											if(foreignSkin != null) {
												registerTextureToPlayerAssociation(skinUUID, searchUUID);
												SkinPacketVersionCache rewrite = SkinPacketVersionCache
														.rewriteUUIDModel(foreignSkin.data,
																searchUUID.getMostSignificantBits(),
																searchUUID.getLeastSignificantBits(), model);
												onlinePlayersCache.put(searchUUID, new CachedPlayerSkin(rewrite, skinUUID, model));
												eaglerHandler.sendEaglerMessage(rewrite.get(eaglerHandler.getEaglerProtocol()));
												return;
											}
											
											// download player skin, put in onlinePlayersCache, no limit
											
											if(!isLimitedAsAntagonist(player.getUniqueId())) {
												final int modelf = model;
												doAsync(() -> {
													processResolveURLTextureForOnline(sender, searchUUID, skinUUID, urlStr, modelf);
												});
											}
											
											return;
										}
									}
								}catch(Throwable t) {
								}
							}
						}
					}
				}
				if(!isLimitedAsAntagonist(player.getUniqueId())) {
					doAsync(() -> {
						if(player.getPendingConnection().isOnlineMode()) {
							processResolveProfileTextureByUUIDForOnline(sender, searchUUID);
						}else {
							processResolveProfileTextureByNameForOnline(sender, player.getPendingConnection().getName(), searchUUID);
						}
					});
				}
			}else {
				CachedForeignSkin foreignSkin = foreignSkinCache.get(searchUUID);
				if(foreignSkin != null) {
					foreignSkin.lastHit = EaglerXBungeeAPIHelper.steadyTimeMillis();
					eaglerHandler.sendEaglerMessage(foreignSkin.data.get(eaglerHandler.getEaglerProtocol()));
				}else {
					if (eaglerHandler.skinUUIDLookupRateLimiter
							.rateLimit(EaglerXBungee.getEagler().getConfig().getUuidRateLimitPlayer())
							&& !isLimitedAsAntagonist(sender.getUniqueId())) {
						if(eaglerHandler.isOnlineMode()) {
							doAsync(() -> {
								processResolveProfileTextureByUUIDForeign(sender, searchUUID);
							});
						}else {
							eaglerHandler.sendEaglerMessage(
									new SPacketOtherSkinPresetEAG(searchUUID.getMostSignificantBits(),
											searchUUID.getLeastSignificantBits(), isAlex(searchUUID) ? 1 : 0));
						}
					}
				}
			}
		}
	}
	
	public void processGetOtherSkin(UUID searchUUID, String skinURL, UserConnection sender) {
		EaglerBungeeConfig config = EaglerXBungee.getEagler().getConfig();
		EaglerInitialHandler eaglerHandler = (EaglerInitialHandler)sender.getPendingConnection();
		if(!eaglerHandler.skinLookupRateLimiter.rateLimit(masterRateLimitPerPlayer)) {
			return;
		}
		CachedForeignSkin foreignSkin = foreignSkinCache.get(searchUUID);
		if(foreignSkin != null) {
			foreignSkin.lastHit = EaglerXBungeeAPIHelper.steadyTimeMillis();
			eaglerHandler.sendEaglerMessage(foreignSkin.data.get(eaglerHandler.getEaglerProtocol()));
		}else {
			Collection<UUID> possiblePlayers;
			onlinePlayersFromTexturesMapLock.readLock().lock();
			try {
				possiblePlayers = new ArrayList<>(onlinePlayersFromTexturesMap.get(searchUUID));
			}finally {
				onlinePlayersFromTexturesMapLock.readLock().unlock();
			}
			boolean playersExist = possiblePlayers.size() > 0;
			if(playersExist) {
				for(UUID uuid : possiblePlayers) {
					CachedPlayerSkin maybeCachedPacket = onlinePlayersCache.get(uuid);
					if(maybeCachedPacket != null) {
						eaglerHandler.sendEaglerMessage(maybeCachedPacket.data.get(eaglerHandler.getEaglerProtocol(),
								searchUUID.getMostSignificantBits(), searchUUID.getLeastSignificantBits()));
						return;
					}
				}
			}
			if(skinURL.startsWith("eagler://")) { // customs skulls from exported singleplayer worlds
				eaglerHandler.sendEaglerMessage(new SPacketOtherSkinPresetEAG(searchUUID.getMostSignificantBits(),
						searchUUID.getLeastSignificantBits(), 0));
				return;
			}
			skinURL = sanitizeTextureURL(skinURL);
			if(skinURL != null) {
				final String skinURL_ = skinURL;
				if(eaglerHandler.skinTextureDownloadRateLimiter.rateLimit(config.getSkinRateLimitPlayer()) && !isLimitedAsAntagonist(sender.getUniqueId())) {
					doAsync(() -> {
						processResolveURLTextureForForeign(sender, searchUUID, searchUUID, skinURL_, -1);
					});
				}
			}else {
				eaglerHandler.sendEaglerMessage(new SPacketOtherSkinPresetEAG(searchUUID.getMostSignificantBits(),
						searchUUID.getLeastSignificantBits(), 0));
			}
		}
	}
	
	private void processResolveURLTextureForOnline(final UserConnection initiator, final UUID onlineCacheUUID,
			final UUID skinUUID, final String urlStr, final int modelId) {
		synchronized(pendingTextures) {
			PendingTextureDownload alreadyPending = pendingTextures.get(skinUUID);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<byte[]>() {

						@Override
						public void accept(byte[] t) {
							CachedPlayerSkin skin = onlinePlayersCache.get(onlineCacheUUID);
							if(skin != null) {
								EaglerInitialHandler initialHandler = (EaglerInitialHandler)initiator.getPendingConnection();
								initialHandler.sendEaglerMessage(skin.data.get(initialHandler.getEaglerProtocol()));
							}
						}

					});
				}
			}else {
				PendingTextureDownload newTask = new PendingTextureDownload(skinUUID, urlStr, initiator.getUniqueId(),
						new Consumer<byte[]>() {

						@Override
						public void accept(byte[] t) {
							CachedPlayerSkin skin;
							if (t != null) {
								registerTextureToPlayerAssociation(skinUUID, onlineCacheUUID);
								skin = new CachedPlayerSkin(
										SkinPacketVersionCache.createCustomV3(
												onlineCacheUUID.getMostSignificantBits(),
												onlineCacheUUID.getLeastSignificantBits(), modelId, t),
										skinUUID, modelId);
							} else {
								skin = new CachedPlayerSkin(SkinPacketVersionCache.createPreset(
										onlineCacheUUID.getMostSignificantBits(),
										onlineCacheUUID.getLeastSignificantBits()), null, -1);
							}
							onlinePlayersCache.put(onlineCacheUUID, skin);
							EaglerInitialHandler initialHandler = (EaglerInitialHandler) initiator.getPendingConnection();
							initialHandler.sendEaglerMessage(skin.data.get(initialHandler.getEaglerProtocol()));
						}

					}, antagonistLogger);
				try {
					AsyncSkinProvider.downloadSkin(skinUUID, urlStr, cacheProvider, newTask);
				}catch(CancelException ex) {
					return;
				}
				pendingTextures.put(skinUUID, newTask);
			}
		}
	}
	
	private void processResolveURLTextureForForeign(final UserConnection initiator, final UUID foreignCacheUUID,
			final UUID skinUUID, final String urlStr, final int modelId) {
		synchronized(pendingTextures) {
			PendingTextureDownload alreadyPending = pendingTextures.get(skinUUID);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<byte[]>() {

						@Override
						public void accept(byte[] t) {
							CachedForeignSkin skin = foreignSkinCache.get(foreignCacheUUID);
							if(skin != null) {
								EaglerInitialHandler initialHandler = (EaglerInitialHandler) initiator.getPendingConnection();
								initialHandler.sendEaglerMessage(skin.data.get(initialHandler.getEaglerProtocol()));
							}
						}

					});
				}
			}else {
				PendingTextureDownload newTask = new PendingTextureDownload(skinUUID, urlStr, initiator.getUniqueId(),
						new Consumer<byte[]>() {

						@Override
						public void accept(byte[] t) {
							CachedForeignSkin skin;
							if (t != null) {
								skin = new CachedForeignSkin(foreignCacheUUID,
										SkinPacketVersionCache.createCustomV3(
												foreignCacheUUID.getMostSignificantBits(),
												foreignCacheUUID.getLeastSignificantBits(), modelId, t),
										modelId);
							} else {
								skin = new CachedForeignSkin(foreignCacheUUID,
										SkinPacketVersionCache.createPreset(
												foreignCacheUUID.getMostSignificantBits(),
												foreignCacheUUID.getLeastSignificantBits()),
										-1);
							}
							foreignSkinCache.put(foreignCacheUUID, skin);
							EaglerInitialHandler initialHandler = (EaglerInitialHandler) initiator.getPendingConnection();
							initialHandler.sendEaglerMessage(skin.data.get(initialHandler.getEaglerProtocol()));
						}

					}, antagonistLogger);
				try {
					AsyncSkinProvider.downloadSkin(skinUUID, urlStr, cacheProvider, newTask);
				}catch(CancelException ex) {
					return;
				}
				pendingTextures.put(skinUUID, newTask);
			}
		}
	}
	
	private void processResolveProfileTextureByUUIDForOnline(final UserConnection initiator, final UUID playerUUID) {
		synchronized(pendingUUIDs) {
			PendingProfileUUIDLookup alreadyPending = pendingUUIDs.get(playerUUID);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<CacheFetchedProfile>() {

						@Override
						public void accept(CacheFetchedProfile t) {
							if(t == null || t.texture == null) {
								CachedPlayerSkin skin = onlinePlayersCache.get(playerUUID);
								if(skin != null) {
									EaglerInitialHandler initialHandler = (EaglerInitialHandler) initiator.getPendingConnection();
									initialHandler.sendEaglerMessage(skin.data.get(initialHandler.getEaglerProtocol()));
								}
							}else {
								processResolveURLTextureForOnline(initiator, playerUUID, t.textureUUID, t.texture,
										SkinPackets.getModelId(t.model));
							}
						}

					});
				}
			}else {
				PendingProfileUUIDLookup newTask = new PendingProfileUUIDLookup(
						playerUUID, initiator.getUniqueId(), new Consumer<CacheFetchedProfile>() {

					@Override
					public void accept(CacheFetchedProfile t) {
						if(t == null || t.texture == null) {
							CachedPlayerSkin skin;
							if (t == null) {
								skin = new CachedPlayerSkin(
										SkinPacketVersionCache.createPreset(playerUUID.getMostSignificantBits(),
												playerUUID.getLeastSignificantBits()),
										null, -1);
							} else {
								skin = new CachedPlayerSkin(
										SkinPacketVersionCache.createPreset(playerUUID.getMostSignificantBits(),
												playerUUID.getLeastSignificantBits(),
												SkinPackets.getModelId(t.model) == 1 ? 1 : 0),
										null, -1);
							}
							onlinePlayersCache.put(playerUUID, skin);
							EaglerInitialHandler initialHandler = (EaglerInitialHandler) initiator.getPendingConnection();
							initialHandler.sendEaglerMessage(skin.data.get(initialHandler.getEaglerProtocol()));
						}else {
							processResolveURLTextureForOnline(initiator, playerUUID, t.textureUUID, t.texture,
									SkinPackets.getModelId(t.model));
						}
					}
					
				}, antagonistLogger);
				try {
					AsyncSkinProvider.lookupProfileByUUID(playerUUID, cacheProvider, newTask);
				}catch(CancelException ex) {
					return;
				}
				pendingUUIDs.put(playerUUID, newTask);
			}
		}
	}
	
	private void processResolveProfileTextureByNameForOnline(final UserConnection initiator, final String playerName, final UUID mapUUID) {
		synchronized(pendingNameLookups) {
			PendingProfileNameLookup alreadyPending = pendingNameLookups.get(playerName);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<CacheFetchedProfile>() {

						@Override
						public void accept(CacheFetchedProfile t) {
							if(t == null || t.texture == null) {
								CachedPlayerSkin skin = onlinePlayersCache.get(t.uuid);
								if(skin != null) {
									EaglerInitialHandler initialHandler = (EaglerInitialHandler) initiator.getPendingConnection();
									initialHandler.sendEaglerMessage(skin.data.get(initialHandler.getEaglerProtocol()));
								}
							}else {
								processResolveURLTextureForOnline(initiator, mapUUID, t.textureUUID, t.texture,
										SkinPackets.getModelId(t.model));
							}
						}

					});
				}
			}else {
				PendingProfileNameLookup newTask = new PendingProfileNameLookup(
						playerName, initiator.getUniqueId(), new Consumer<CacheFetchedProfile>() {

					@Override
					public void accept(CacheFetchedProfile t) {
						if(t == null || t.texture == null) {
							CachedPlayerSkin skin;
							if (t == null) {
								skin = new CachedPlayerSkin(
										SkinPacketVersionCache.createPreset(mapUUID.getMostSignificantBits(),
												mapUUID.getLeastSignificantBits()),
										null, -1);
							} else {
								skin = new CachedPlayerSkin(SkinPacketVersionCache.createPreset(
										mapUUID.getMostSignificantBits(), mapUUID.getLeastSignificantBits(),
										SkinPackets.getModelId(t.model) == 1 ? 1 : 0), null, -1);
							}
							onlinePlayersCache.put(mapUUID, skin);
							EaglerInitialHandler initialHandler = (EaglerInitialHandler) initiator.getPendingConnection();
							initialHandler.sendEaglerMessage(skin.data.get(initialHandler.getEaglerProtocol()));
						}else {
							processResolveURLTextureForOnline(initiator, mapUUID, t.textureUUID, t.texture,
									SkinPackets.getModelId(t.model));
						}
					}
					
				}, antagonistLogger);
				try {
					AsyncSkinProvider.lookupProfileByUsername(playerName, cacheProvider, newTask);
				}catch(CancelException ex) {
					return;
				}
				pendingNameLookups.put(playerName, newTask);
			}
		}
	}
	
	private void processResolveProfileTextureByUUIDForeign(final UserConnection initiator, final UUID playerUUID) {
		synchronized(pendingUUIDs) {
			PendingProfileUUIDLookup alreadyPending = pendingUUIDs.get(playerUUID);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<CacheFetchedProfile>() {

						@Override
						public void accept(CacheFetchedProfile t) {
							if(t == null || t.texture == null) {
								CachedForeignSkin skin = foreignSkinCache.get(playerUUID);
								if(skin != null) {
									EaglerInitialHandler initialHandler = (EaglerInitialHandler) initiator.getPendingConnection();
									initialHandler.sendEaglerMessage(skin.data.get(initialHandler.getEaglerProtocol()));
								}
							}else {
								processResolveURLTextureForForeign(initiator, playerUUID, t.textureUUID, t.texture,
										SkinPackets.getModelId(t.model));
							}
						}

					});
				}
			}else {
				PendingProfileUUIDLookup newTask = new PendingProfileUUIDLookup(
						playerUUID, initiator.getUniqueId(), new Consumer<CacheFetchedProfile>() {

					@Override
					public void accept(CacheFetchedProfile t) {
						if(t == null || t.texture == null) {
							CachedForeignSkin skin;
							if (t == null) {
								skin = new CachedForeignSkin(playerUUID,
										SkinPacketVersionCache.createPreset(playerUUID.getMostSignificantBits(),
												playerUUID.getLeastSignificantBits()),
										-1);
							} else {
								skin = new CachedForeignSkin(playerUUID,
										SkinPacketVersionCache.createPreset(playerUUID.getMostSignificantBits(),
												playerUUID.getLeastSignificantBits(),
												SkinPackets.getModelId(t.model) == 1 ? 1 : 0),
										-1);
							}
							foreignSkinCache.put(playerUUID, skin);
							EaglerInitialHandler initialHandler = (EaglerInitialHandler) initiator.getPendingConnection();
							initialHandler.sendEaglerMessage(skin.data.get(initialHandler.getEaglerProtocol()));
						}else {
							processResolveURLTextureForForeign(initiator, playerUUID, t.textureUUID, t.texture,
									SkinPackets.getModelId(t.model));
						}
					}
					
				}, antagonistLogger);
				try {
					AsyncSkinProvider.lookupProfileByUUID(playerUUID, cacheProvider, newTask);
				}catch(CancelException ex) {
					return;
				}
				pendingUUIDs.put(playerUUID, newTask);
			}
		}
	}
	
	public void registerEaglercraftPlayer(UUID clientUUID, SkinPacketVersionCache generatedPacket, int modelId) {
		foreignSkinCache.remove(clientUUID);
		onlinePlayersCache.put(clientUUID, new CachedPlayerSkin(generatedPacket, null, modelId));
	}
	
	public void unregisterPlayer(UUID clientUUID) {
		CachedPlayerSkin data = onlinePlayersCache.remove(clientUUID);
		if(data != null) {
			foreignSkinCache.put(clientUUID, new CachedForeignSkin(clientUUID, data.data, data.modelId));
			if(data.textureUUID != null) {
				foreignSkinCache.put(data.textureUUID, new CachedForeignSkin(data.textureUUID, data.data, data.modelId));
			}
			deletePlayerTextureAssociation(clientUUID, data.textureUUID);
		}else {
			deletePlayerTextureAssociation(clientUUID, null);
		}
	}
	
	private void deletePlayerTextureAssociation(UUID clientUUID, UUID textureUUID) {
		if(textureUUID != null) {
			onlinePlayersToTexturesMap.remove(clientUUID);
			onlinePlayersFromTexturesMapLock.writeLock().lock();
			try {
				onlinePlayersFromTexturesMap.remove(textureUUID, clientUUID);
			}finally {
				onlinePlayersFromTexturesMapLock.writeLock().unlock();
			}
		}else {
			UUID removedUUID = onlinePlayersToTexturesMap.remove(clientUUID);
			if(removedUUID != null) {
				onlinePlayersFromTexturesMapLock.writeLock().lock();
				try {
					onlinePlayersFromTexturesMap.remove(removedUUID, clientUUID);
				}finally {
					onlinePlayersFromTexturesMapLock.writeLock().unlock();
				}
			}
		}
	}
	
	public void registerTextureToPlayerAssociation(UUID textureUUID, UUID playerUUID) {
		onlinePlayersFromTexturesMapLock.writeLock().lock();
		try {
			onlinePlayersFromTexturesMap.put(textureUUID, playerUUID);
		}finally {
			onlinePlayersFromTexturesMapLock.writeLock().unlock();
		}
		onlinePlayersToTexturesMap.put(playerUUID, textureUUID);
		CachedForeignSkin foreign = foreignSkinCache.remove(textureUUID);
		if(foreign != null) {
			onlinePlayersCache.put(playerUUID, new CachedPlayerSkin(foreign.data, textureUUID, foreign.modelKnown));
		}
	}

	public void processForceSkin(UUID playerUUID, EaglerInitialHandler eaglerHandler) {
		CachedPlayerSkin maybeCachedPacket = onlinePlayersCache.get(playerUUID);
		
		if(maybeCachedPacket != null) {
			eaglerHandler.sendEaglerMessage(maybeCachedPacket.data.getForceClientV4());
		}else {
			UUID playerTexture = onlinePlayersToTexturesMap.get(playerUUID);
			if(playerTexture != null) {
				Collection<UUID> possiblePlayers;
				onlinePlayersFromTexturesMapLock.readLock().lock();
				try {
					possiblePlayers = new ArrayList<>(onlinePlayersFromTexturesMap.get(playerTexture));
				}finally {
					onlinePlayersFromTexturesMapLock.readLock().unlock();
				}
				boolean playersExist = possiblePlayers.size() > 0;
				if(playersExist) {
					for(UUID uuid : possiblePlayers) {
						maybeCachedPacket = onlinePlayersCache.get(uuid);
						if(maybeCachedPacket != null) {
							SkinPacketVersionCache rewritten = SkinPacketVersionCache.rewriteUUID(
									maybeCachedPacket.data, playerUUID.getMostSignificantBits(),
									playerUUID.getLeastSignificantBits());
							onlinePlayersCache.put(playerUUID, new CachedPlayerSkin(rewritten,
									maybeCachedPacket.textureUUID, maybeCachedPacket.modelId));
							eaglerHandler.sendEaglerMessage(rewritten.getForceClientV4());
							return;
						}
					}
				}
				CachedForeignSkin foreignSkin = foreignSkinCache.get(playerTexture);
				if(foreignSkin != null && foreignSkin.modelKnown != -1) {
					onlinePlayersCache.put(playerUUID,
							new CachedPlayerSkin(SkinPacketVersionCache.rewriteUUID(foreignSkin.data,
									playerUUID.getMostSignificantBits(), playerUUID.getLeastSignificantBits()),
									playerTexture, foreignSkin.modelKnown));
					foreignSkinCache.remove(playerTexture);
					eaglerHandler.sendEaglerMessage(foreignSkin.data.getForceClientV4());
					return;
				}
			}
			LoginResult loginProfile = eaglerHandler.getLoginProfile();
			if(loginProfile != null) {
				Property[] props = loginProfile.getProperties();
				if(props.length > 0) {
					for(int i = 0; i < props.length; ++i) {
						Property pp = props[i];
						if(pp.getName().equals("textures")) {
							try {
								String jsonStr = SkinPackets.bytesToAscii(Base64.decodeBase64(pp.getValue()));
								JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
								JsonObject skinObj = json.getAsJsonObject("SKIN");
								if(skinObj != null) {
									JsonElement url = json.get("url");
									if(url != null) {
										String urlStr = sanitizeTextureURL(url.getAsString());
										if(urlStr == null) {
											break;
										}
										int model = 0;
										JsonElement el = skinObj.get("metadata");
										if(el != null && el.isJsonObject()) {
											el = el.getAsJsonObject().get("model");
											if(el != null) {
												model = SkinPackets.getModelId(el.getAsString());
											}
										}
										UUID skinUUID = SkinPackets.createEaglerURLSkinUUID(urlStr);
										
										CachedForeignSkin foreignSkin = foreignSkinCache.remove(skinUUID);
										if(foreignSkin != null) {
											registerTextureToPlayerAssociation(skinUUID, playerUUID);
											SkinPacketVersionCache rewrite = SkinPacketVersionCache
													.rewriteUUIDModel(foreignSkin.data,
															playerUUID.getMostSignificantBits(),
															playerUUID.getLeastSignificantBits(), model);
											onlinePlayersCache.put(playerUUID, new CachedPlayerSkin(rewrite, skinUUID, model));
											eaglerHandler.sendEaglerMessage(rewrite.getForceClientV4());
											return;
										}
										
										// download player skin, put in onlinePlayersCache, no limit
										
										final int modelf = model;
										doAsync(() -> {
											processResolveURLTextureForOnlineToForce(eaglerHandler, playerUUID, skinUUID, urlStr, modelf);
										});
										
										return;
									}
								}
							}catch(Throwable t) {
							}
						}
					}
				}
				doAsync(() -> {
					if(eaglerHandler.isOnlineMode()) {
						processResolveProfileTextureByUUIDForOnlineToForce(eaglerHandler, playerUUID);
					}else {
						processResolveProfileTextureByNameForOnlineToForce(eaglerHandler, eaglerHandler.getName(), playerUUID);
					}
				});
			}else {
				CachedForeignSkin foreignSkin = foreignSkinCache.get(playerUUID);
				if(foreignSkin != null) {
					foreignSkin.lastHit = EaglerXBungeeAPIHelper.steadyTimeMillis();
					eaglerHandler.sendEaglerMessage(foreignSkin.data.getForceClientV4());
				}else {
					if(eaglerHandler.isOnlineMode()) {
						doAsync(() -> {
							processResolveProfileTextureByUUIDForeignToForce(eaglerHandler, playerUUID);
						});
					}else {
						eaglerHandler.sendEaglerMessage(new SPacketForceClientSkinPresetV4EAG(isAlex(playerUUID) ? 1 : 0));
					}
				}
			}
		}
	}
	
	private void processResolveURLTextureForOnlineToForce(final EaglerInitialHandler initiator, final UUID onlineCacheUUID,
			final UUID skinUUID, final String urlStr, final int modelId) {
		synchronized(pendingTextures) {
			PendingTextureDownload alreadyPending = pendingTextures.get(skinUUID);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<byte[]>() {

						@Override
						public void accept(byte[] t) {
							CachedPlayerSkin skin = onlinePlayersCache.get(onlineCacheUUID);
							if(skin != null) {
								initiator.sendEaglerMessage(skin.data.getForceClientV4());
							}
						}

					});
				}
			}else {
				PendingTextureDownload newTask = new PendingTextureDownload(skinUUID, urlStr, initiator.getUniqueId(),
						new Consumer<byte[]>() {

						@Override
						public void accept(byte[] t) {
							CachedPlayerSkin skin;
							if (t != null) {
								registerTextureToPlayerAssociation(skinUUID, onlineCacheUUID);
								skin = new CachedPlayerSkin(
										SkinPacketVersionCache.createCustomV3(
												onlineCacheUUID.getMostSignificantBits(),
												onlineCacheUUID.getLeastSignificantBits(), modelId, t),
										skinUUID, modelId);
							} else {
								skin = new CachedPlayerSkin(SkinPacketVersionCache.createPreset(
										onlineCacheUUID.getMostSignificantBits(),
										onlineCacheUUID.getLeastSignificantBits()), null, -1);
							}
							onlinePlayersCache.put(onlineCacheUUID, skin);
							initiator.sendEaglerMessage(skin.data.getForceClientV4());
						}

					}, antagonistLogger);
				try {
					AsyncSkinProvider.downloadSkin(skinUUID, urlStr, cacheProvider, newTask);
				}catch(CancelException ex) {
					return;
				}
				pendingTextures.put(skinUUID, newTask);
			}
		}
	}
	
	private void processResolveURLTextureForForeignToForce(final EaglerInitialHandler initiator, final UUID foreignCacheUUID,
			final UUID skinUUID, final String urlStr, final int modelId) {
		synchronized(pendingTextures) {
			PendingTextureDownload alreadyPending = pendingTextures.get(skinUUID);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<byte[]>() {

						@Override
						public void accept(byte[] t) {
							CachedForeignSkin skin = foreignSkinCache.get(foreignCacheUUID);
							if(skin != null) {
								initiator.sendEaglerMessage(skin.data.getForceClientV4());
							}
						}

					});
				}
			}else {
				PendingTextureDownload newTask = new PendingTextureDownload(skinUUID, urlStr, initiator.getUniqueId(),
						new Consumer<byte[]>() {

						@Override
						public void accept(byte[] t) {
							CachedForeignSkin skin;
							if (t != null) {
								skin = new CachedForeignSkin(foreignCacheUUID,
										SkinPacketVersionCache.createCustomV3(
												foreignCacheUUID.getMostSignificantBits(),
												foreignCacheUUID.getLeastSignificantBits(), modelId, t),
										modelId);
							} else {
								skin = new CachedForeignSkin(foreignCacheUUID,
										SkinPacketVersionCache.createPreset(
												foreignCacheUUID.getMostSignificantBits(),
												foreignCacheUUID.getLeastSignificantBits()),
										-1);
							}
							foreignSkinCache.put(foreignCacheUUID, skin);
							initiator.sendEaglerMessage(skin.data.getForceClientV4());
						}

					}, antagonistLogger);
				try {
					AsyncSkinProvider.downloadSkin(skinUUID, urlStr, cacheProvider, newTask);
				}catch(CancelException ex) {
					return;
				}
				pendingTextures.put(skinUUID, newTask);
			}
		}
	}
	
	private void processResolveProfileTextureByUUIDForOnlineToForce(final EaglerInitialHandler initiator, final UUID playerUUID) {
		synchronized(pendingUUIDs) {
			PendingProfileUUIDLookup alreadyPending = pendingUUIDs.get(playerUUID);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<CacheFetchedProfile>() {

						@Override
						public void accept(CacheFetchedProfile t) {
							if(t == null || t.texture == null) {
								CachedPlayerSkin skin = onlinePlayersCache.get(playerUUID);
								if(skin != null) {
									initiator.sendEaglerMessage(skin.data.getForceClientV4());
								}
							}else {
								processResolveURLTextureForOnlineToForce(initiator, playerUUID, t.textureUUID, t.texture,
										SkinPackets.getModelId(t.model));
							}
						}

					});
				}
			}else {
				PendingProfileUUIDLookup newTask = new PendingProfileUUIDLookup(
						playerUUID, initiator.getUniqueId(), new Consumer<CacheFetchedProfile>() {

					@Override
					public void accept(CacheFetchedProfile t) {
						if(t == null || t.texture == null) {
							CachedPlayerSkin skin;
							if (t == null) {
								skin = new CachedPlayerSkin(
										SkinPacketVersionCache.createPreset(playerUUID.getMostSignificantBits(),
												playerUUID.getLeastSignificantBits()),
										null, -1);
							} else {
								skin = new CachedPlayerSkin(
										SkinPacketVersionCache.createPreset(playerUUID.getMostSignificantBits(),
												playerUUID.getLeastSignificantBits(),
												SkinPackets.getModelId(t.model) == 1 ? 1 : 0),
										null, -1);
							}
							onlinePlayersCache.put(playerUUID, skin);
							initiator.sendEaglerMessage(skin.data.getForceClientV4());
						}else {
							processResolveURLTextureForOnlineToForce(initiator, playerUUID, t.textureUUID, t.texture,
									SkinPackets.getModelId(t.model));
						}
					}
					
				}, antagonistLogger);
				try {
					AsyncSkinProvider.lookupProfileByUUID(playerUUID, cacheProvider, newTask);
				}catch(CancelException ex) {
					return;
				}
				pendingUUIDs.put(playerUUID, newTask);
			}
		}
	}
	
	private void processResolveProfileTextureByNameForOnlineToForce(final EaglerInitialHandler initiator, final String playerName, final UUID mapUUID) {
		synchronized(pendingNameLookups) {
			PendingProfileNameLookup alreadyPending = pendingNameLookups.get(playerName);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<CacheFetchedProfile>() {

						@Override
						public void accept(CacheFetchedProfile t) {
							if(t == null || t.texture == null) {
								CachedPlayerSkin skin = onlinePlayersCache.get(t.uuid);
								if(skin != null) {
									initiator.sendEaglerMessage(skin.data.getForceClientV4());
								}
							}else {
								processResolveURLTextureForOnlineToForce(initiator, mapUUID, t.textureUUID, t.texture,
										SkinPackets.getModelId(t.model));
							}
						}

					});
				}
			}else {
				PendingProfileNameLookup newTask = new PendingProfileNameLookup(
						playerName, initiator.getUniqueId(), new Consumer<CacheFetchedProfile>() {

					@Override
					public void accept(CacheFetchedProfile t) {
						if(t == null || t.texture == null) {
							CachedPlayerSkin skin;
							if (t == null) {
								skin = new CachedPlayerSkin(
										SkinPacketVersionCache.createPreset(mapUUID.getMostSignificantBits(),
												mapUUID.getLeastSignificantBits()),
										null, -1);
							} else {
								skin = new CachedPlayerSkin(SkinPacketVersionCache.createPreset(
										mapUUID.getMostSignificantBits(), mapUUID.getLeastSignificantBits(),
										SkinPackets.getModelId(t.model) == 1 ? 1 : 0), null, -1);
							}
							onlinePlayersCache.put(mapUUID, skin);
							initiator.sendEaglerMessage(skin.data.getForceClientV4());
						}else {
							processResolveURLTextureForOnlineToForce(initiator, mapUUID, t.textureUUID, t.texture,
									SkinPackets.getModelId(t.model));
						}
					}
					
				}, antagonistLogger);
				try {
					AsyncSkinProvider.lookupProfileByUsername(playerName, cacheProvider, newTask);
				}catch(CancelException ex) {
					return;
				}
				pendingNameLookups.put(playerName, newTask);
			}
		}
	}
	
	private void processResolveProfileTextureByUUIDForeignToForce(final EaglerInitialHandler initiator, final UUID playerUUID) {
		synchronized(pendingUUIDs) {
			PendingProfileUUIDLookup alreadyPending = pendingUUIDs.get(playerUUID);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<CacheFetchedProfile>() {

						@Override
						public void accept(CacheFetchedProfile t) {
							if(t == null || t.texture == null) {
								CachedForeignSkin skin = foreignSkinCache.get(playerUUID);
								if(skin != null) {
									initiator.sendEaglerMessage(skin.data.getForceClientV4());
								}
							}else {
								processResolveURLTextureForForeignToForce(initiator, playerUUID, t.textureUUID, t.texture,
										SkinPackets.getModelId(t.model));
							}
						}

					});
				}
			}else {
				PendingProfileUUIDLookup newTask = new PendingProfileUUIDLookup(
						playerUUID, initiator.getUniqueId(), new Consumer<CacheFetchedProfile>() {

					@Override
					public void accept(CacheFetchedProfile t) {
						if(t == null || t.texture == null) {
							CachedForeignSkin skin;
							if (t == null) {
								skin = new CachedForeignSkin(playerUUID,
										SkinPacketVersionCache.createPreset(playerUUID.getMostSignificantBits(),
												playerUUID.getLeastSignificantBits()),
										-1);
							} else {
								skin = new CachedForeignSkin(playerUUID,
										SkinPacketVersionCache.createPreset(playerUUID.getMostSignificantBits(),
												playerUUID.getLeastSignificantBits(),
												SkinPackets.getModelId(t.model) == 1 ? 1 : 0),
										-1);
							}
							foreignSkinCache.put(playerUUID, skin);
							initiator.sendEaglerMessage(skin.data.getForceClientV4());
						}else {
							processResolveURLTextureForForeignToForce(initiator, playerUUID, t.textureUUID, t.texture,
									SkinPackets.getModelId(t.model));
						}
					}
					
				}, antagonistLogger);
				try {
					AsyncSkinProvider.lookupProfileByUUID(playerUUID, cacheProvider, newTask);
				}catch(CancelException ex) {
					return;
				}
				pendingUUIDs.put(playerUUID, newTask);
			}
		}
	}
	
	public void flush() {
		long millis = EaglerXBungeeAPIHelper.steadyTimeMillis();
		
		final List<UUID> foreignSkinCleanup = new ArrayList<>(4);
		foreignSkinCache.entrySet().forEach((etr) -> {
			if(millis - etr.getValue().lastHit > 900000l) { // 15 minutes
				foreignSkinCleanup.add(etr.getKey());
			}
		});
		
		if(!foreignSkinCleanup.isEmpty()) {
			for(UUID uuid : foreignSkinCleanup) {
				foreignSkinCache.remove(uuid);
			}
		}
		
		synchronized(pendingTextures) {
			Iterator<PendingTextureDownload> itr = pendingTextures.values().iterator();
			while(itr.hasNext()) {
				PendingTextureDownload etr = itr.next();
				if(millis - etr.initializedTime > (etr.finalized ? 5000l : 10000l)) {
					itr.remove();
					try {
						etr.antagonistsCallback.accept(etr.antagonists);
					}catch(Throwable t) {
					}
				}
			}
		}
		
		synchronized(pendingUUIDs) {
			Iterator<PendingProfileUUIDLookup> itr = pendingUUIDs.values().iterator();
			while(itr.hasNext()) {
				PendingProfileUUIDLookup etr = itr.next();
				if(millis - etr.initializedTime > (etr.finalized ? 5000l : 10000l)) {
					itr.remove();
					try {
						etr.antagonistsCallback.accept(etr.antagonists);
					}catch(Throwable t) {
					}
				}
			}
		}
		
		synchronized(pendingNameLookups) {
			Iterator<PendingProfileNameLookup> itr = pendingNameLookups.values().iterator();
			while(itr.hasNext()) {
				PendingProfileNameLookup etr = itr.next();
				if(millis - etr.initializedTime > (etr.finalized ? 5000l : 10000l)) {
					itr.remove();
					try {
						etr.antagonistsCallback.accept(etr.antagonists);
					}catch(Throwable t) {
					}
				}
			}
		}

		int cooldownPeriod = 60000 / EaglerXBungee.getEagler().getConfig().getAntagonistsRateLimit();
		int elapsedCooldown = (int)(millis - antagonistCooldown);
		elapsedCooldown /= cooldownPeriod;
		if(elapsedCooldown > 0) {
			antagonistCooldown += elapsedCooldown * cooldownPeriod;
			antagonistsLock.writeLock().lock();
			try {
				Iterator<UUID> itr = antagonists.keySet().iterator();
				while(itr.hasNext()) {
					UUID key = itr.next();
					int i = antagonists.get(key) - elapsedCooldown;
					if(i <= 0) {
						itr.remove();
					}else {
						antagonists.put(key, i);
					}
				}
			}finally {
				antagonistsLock.writeLock().unlock();
			}
		}
		
		cacheProvider.flush();
	}

	public SkinPacketVersionCache getSkin(UUID playerUUID) {
		CachedPlayerSkin skin = onlinePlayersCache.get(playerUUID);
		return skin != null ? skin.data : null;
	}

	public void shutdown() {
		resetMaps();
		if(cacheProvider != null) {
			cacheProvider.destroy();
		}
		cacheProvider = null;
	}
	
	private boolean isLimitedAsAntagonist(UUID uuid) {
		int limit = EaglerXBungee.getEagler().getConfig().getAntagonistsRateLimit();
		limit += limit >> 1;
		int i;
		antagonistsLock.readLock().lock();
		try {
			i = antagonists.get(uuid);
		}finally {
			antagonistsLock.readLock().unlock();
		}
		return i != antagonists.getNoEntryValue() && i > limit;
	}
	
	private void resetMaps() {
		onlinePlayersCache.clear();
		onlinePlayersFromTexturesMapLock.writeLock().lock();
		try {
			onlinePlayersFromTexturesMap.clear();
		}finally {
			onlinePlayersFromTexturesMapLock.writeLock().unlock();
		}
		onlinePlayersToTexturesMap.clear();
		foreignSkinCache.clear();
		synchronized(pendingTextures) {
			pendingTextures.clear();
		}
		synchronized(pendingUUIDs) {
			pendingUUIDs.clear();
		}
		synchronized(pendingNameLookups) {
			pendingNameLookups.clear();
		}
		antagonistsLock.writeLock().lock();
		try {
			antagonists.clear();
		}finally {
			antagonistsLock.writeLock().unlock();
		}
	}
	
	private void doAsync(Runnable handler) {
		ProxyServer.getInstance().getScheduler().runAsync(EaglerXBungee.getEagler(), handler);
	}
	
	public static String sanitizeTextureURL(String url) {
		try {
			URI uri = URI.create(url);
			StringBuilder builder = new StringBuilder();
			String scheme = uri.getScheme();
			if(scheme == null) {
				return null;
			}
			String host = uri.getHost();
			if(host == null || !EaglerXBungee.getEagler().getConfig().isValidSkinHost(host)) {
				return null;
			}
			scheme = scheme.toLowerCase();
			builder.append(scheme).append("://");
			builder.append(host);
			int port = uri.getPort();
			if(port != -1) {
				switch(scheme) {
				case "http":
					if(port == 80) {
						port = -1;
					}
					break;
				case "https":
					if(port == 443) {
						port = -1;
					}
					break;
				default:
					return null;
				}
				if(port != -1) {
					builder.append(":").append(port);
				}
			}
			String path = uri.getRawPath();
			if(path != null) {
				if(path.contains("//")) {
					path = String.join("/", path.split("[\\/]+"));
				}
				int len = path.length();
				if(len > 1 && path.charAt(len - 1) == '/') {
					path = path.substring(0, len - 1);
				}
				builder.append(path);
			}
			return builder.toString();
		}catch(Throwable t) {
			return null;
		}
	}

	private static final String hexString = "0123456789abcdef";

	private static final char[] HEX = new char[] {
		'0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
	};

	public static String getMojangUUID(UUID uuid) {
		char[] ret = new char[32];
		long msb = uuid.getMostSignificantBits();
		long lsb = uuid.getLeastSignificantBits();
		for(int i = 0, j; i < 16; ++i) {
			j = (15 - i) << 2;
			ret[i] = HEX[(int)((msb >> j) & 15l)];
			ret[i + 16] = HEX[(int)((lsb >> j) & 15l)];
		}
		return new String(ret);
	}

	public static UUID parseMojangUUID(String uuid) {
		long msb = 0l;
		long lsb = 0l;
		for(int i = 0, j; i < 16; ++i) {
			j = (15 - i) << 2;
			msb |= ((long)hexString.indexOf(uuid.charAt(i)) << j);
			lsb |= ((long)hexString.indexOf(uuid.charAt(i + 16)) << j);
		}
		return new UUID(msb, lsb);
	}

	public static boolean isAlex(UUID skinUUID) {
		return (skinUUID.hashCode() & 1) != 0;
	}

}