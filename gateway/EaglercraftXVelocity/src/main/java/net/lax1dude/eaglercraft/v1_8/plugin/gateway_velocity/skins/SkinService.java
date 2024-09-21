package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.scheduler.ScheduledTask;
import com.velocitypowered.api.util.GameProfile;
import com.velocitypowered.api.util.GameProfile.Property;
import com.velocitypowered.proxy.connection.client.ConnectedPlayer;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.EaglerXVelocity;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.api.EaglerXVelocityAPIHelper;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.config.EaglerVelocityConfig;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.server.EaglerPlayerData;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.AsyncSkinProvider.CacheFetchedProfile;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins.AsyncSkinProvider.CancelException;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketForceClientSkinPresetV4EAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketOtherSkinPresetEAG;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SkinPacketVersionCache;

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
public class SkinService implements ISkinService {

	public static final int masterRateLimitPerPlayer = 250;

	private final Map<UUID, CachedPlayerSkin> onlinePlayersCache = new HashMap<>();
	private final Multimap<UUID, UUID> onlinePlayersFromTexturesMap = MultimapBuilder.hashKeys().hashSetValues().build();
	private final Map<UUID, UUID> onlinePlayersToTexturesMap = new HashMap<>();
	private final Map<UUID, CachedForeignSkin> foreignSkinCache = new HashMap<>();

	private final Map<UUID, PendingTextureDownload> pendingTextures = new HashMap<>();
	private final Map<UUID, PendingProfileUUIDLookup> pendingUUIDs = new HashMap<>();
	private final Map<String, PendingProfileNameLookup> pendingNameLookups = new HashMap<>();

	private final Object2IntMap<UUID> antagonists = new Object2IntOpenHashMap<>();
	private long antagonistCooldown = EaglerXVelocityAPIHelper.steadyTimeMillis();

	private final Consumer<Set<UUID>> antagonistLogger = new Consumer<Set<UUID>>() {

		@Override
		public void accept(Set<UUID> t) {
			if(t.size() == 1) {
				int limit = EaglerXVelocity.getEagler().getConfig().getAntagonistsRateLimit() << 1;
				UUID offender = t.iterator().next();
				synchronized(antagonists) {
					int v = antagonists.getInt(offender);
					if(v == antagonists.defaultReturnValue()) {
						antagonists.put(offender, 1);
					}else {
						if(v <= limit) {
							antagonists.put(offender, v + 1);
						}
					}
				}
			}
		}

	};

	private ICacheProvider cacheProvider = null;

	protected static class CachedForeignSkin {

		protected final UUID uuid;
		protected final SkinPacketVersionCache data;
		protected final int modelKnown;
		protected long lastHit;

		protected CachedForeignSkin(UUID uuid, SkinPacketVersionCache data, int modelKnown) {
			this.uuid = uuid;
			this.data = data;
			this.modelKnown = modelKnown;
			this.lastHit = EaglerXVelocityAPIHelper.steadyTimeMillis();
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
		protected boolean finalized;

		protected PendingTextureDownload(UUID textureUUID, String textureURL, UUID caller, Consumer<byte[]> callback,
				Consumer<Set<UUID>> antagonistsCallback) {
			this.textureUUID = textureUUID;
			this.textureURL = textureURL;
			this.antagonists = new LinkedHashSet<>();
			this.antagonists.add(caller);
			this.callbacks = new LinkedList<>();
			this.callbacks.add(callback);
			this.antagonistsCallback = antagonistsCallback;
			this.initializedTime = EaglerXVelocityAPIHelper.steadyTimeMillis();
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
		protected boolean finalized;

		protected PendingProfileUUIDLookup(UUID profileUUID, UUID caller, Consumer<CacheFetchedProfile> callback,
				Consumer<Set<UUID>> antagonistsCallback) {
			this.profileUUID = profileUUID;
			this.antagonists = new LinkedHashSet<>();
			this.antagonists.add(caller);
			this.callbacks = new LinkedList<>();
			this.callbacks.add(callback);
			this.antagonistsCallback = antagonistsCallback;
			this.initializedTime = EaglerXVelocityAPIHelper.steadyTimeMillis();
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
		protected boolean finalized;

		protected PendingProfileNameLookup(String profileName, UUID caller, Consumer<CacheFetchedProfile> callback,
				Consumer<Set<UUID>> antagonistsCallback) {
			this.profileName = profileName;
			this.antagonists = new LinkedHashSet<>();
			this.antagonists.add(caller);
			this.callbacks = new LinkedList<>();
			this.callbacks.add(callback);
			this.antagonistsCallback = antagonistsCallback;
			this.initializedTime = EaglerXVelocityAPIHelper.steadyTimeMillis();
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
		antagonistCooldown = EaglerXVelocityAPIHelper.steadyTimeMillis();
		if(cacheProvider == null) {
			cacheProvider = JDBCCacheProvider.initialize(uri, driverClass, driverPath, keepObjectsDays,
					keepProfilesDays, maxObjects, maxProfiles);
		}
		resetMaps();
	}
	
	public void processGetOtherSkin(final UUID searchUUID, final EaglerPlayerData sender) {
		if(!sender.skinLookupRateLimiter.rateLimit(masterRateLimitPerPlayer)) {
			return;
		}
		
		CachedPlayerSkin maybeCachedPacket;
		synchronized(onlinePlayersCache) {
			maybeCachedPacket = onlinePlayersCache.get(searchUUID);
		}
		
		if(maybeCachedPacket != null) {
			sender.sendEaglerMessage(maybeCachedPacket.data.get(sender.getEaglerProtocol()));
		}else {
			Player player = EaglerXVelocity.proxy().getPlayer(searchUUID).orElse(null);
			UUID playerTexture;
			synchronized(onlinePlayersToTexturesMap) {
				playerTexture = onlinePlayersToTexturesMap.get(searchUUID);
			}
			if(playerTexture != null) {
				Collection<UUID> possiblePlayers;
				synchronized(onlinePlayersFromTexturesMap) {
					possiblePlayers = onlinePlayersFromTexturesMap.get(playerTexture);
				}
				boolean playersExist = possiblePlayers.size() > 0;
				if(playersExist) {
					for(UUID uuid : possiblePlayers) {
						synchronized(onlinePlayersCache) {
							maybeCachedPacket = onlinePlayersCache.get(uuid);
						}
						if(maybeCachedPacket != null) {
							SkinPacketVersionCache rewritten = SkinPacketVersionCache.rewriteUUID(
									maybeCachedPacket.data, searchUUID.getMostSignificantBits(),
									searchUUID.getLeastSignificantBits());
							if(player != null) {
								synchronized(onlinePlayersCache) {
									onlinePlayersCache.put(searchUUID, new CachedPlayerSkin(rewritten,
											maybeCachedPacket.textureUUID, maybeCachedPacket.modelId));
								}
							}
							sender.sendEaglerMessage(rewritten.get(sender.getEaglerProtocol()));
							return;
						}
					}
				}
				CachedForeignSkin foreignSkin;
				synchronized(foreignSkinCache) {
					foreignSkin = foreignSkinCache.get(playerTexture);
				}
				if(foreignSkin != null && foreignSkin.modelKnown != -1) {
					if(player != null) {
						synchronized(onlinePlayersCache) {
							onlinePlayersCache.put(searchUUID,
									new CachedPlayerSkin(SkinPacketVersionCache.rewriteUUID(foreignSkin.data,
											searchUUID.getMostSignificantBits(), searchUUID.getLeastSignificantBits()),
											playerTexture, foreignSkin.modelKnown));
						}
						synchronized(foreignSkinCache) {
							foreignSkinCache.remove(playerTexture);
						}
					}else {
						foreignSkin.lastHit = EaglerXVelocityAPIHelper.steadyTimeMillis();
					}
					sender.sendEaglerMessage(foreignSkin.data.get(sender.getEaglerProtocol()));
					return;
				}
			}
			if(player != null && (player instanceof ConnectedPlayer)) {
				GameProfile loginProfile = player.getGameProfile();
				if(loginProfile != null) {
					List<Property> props = loginProfile.getProperties();
					if(props.size() > 0) {
						for(int i = 0, l = props.size(); i < l; ++i) {
							Property pp = props.get(i);
							if(pp.getName().equals("textures")) {
								try {
									String jsonStr = SkinPackets.bytesToAscii(Base64.decodeBase64(pp.getValue()));
									JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
									JsonObject skinObj = json.getAsJsonObject("SKIN");
									if(skinObj != null) {
										JsonElement url = json.get("url");
										if(url != null) {
											String urlStr = SkinService.sanitizeTextureURL(url.getAsString());
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
											
											CachedForeignSkin foreignSkin;
											synchronized(foreignSkinCache) {
												foreignSkin = foreignSkinCache.remove(skinUUID);
											}
											if(foreignSkin != null) {
												registerTextureToPlayerAssociation(skinUUID, searchUUID);
												SkinPacketVersionCache rewrite = SkinPacketVersionCache
														.rewriteUUIDModel(foreignSkin.data,
																searchUUID.getMostSignificantBits(),
																searchUUID.getLeastSignificantBits(), model);
												synchronized(onlinePlayersCache) {
													onlinePlayersCache.put(searchUUID, new CachedPlayerSkin(rewrite, skinUUID, model));
												}
												sender.sendEaglerMessage(rewrite.get(sender.getEaglerProtocol()));
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
						if(player.isOnlineMode()) {
							processResolveProfileTextureByUUIDForOnline(sender, searchUUID);
						}else {
							processResolveProfileTextureByNameForOnline(sender, player.getUsername(), searchUUID);
						}
					});
				}
			}else {
				CachedForeignSkin foreignSkin;
				synchronized(foreignSkinCache) {
					foreignSkin = foreignSkinCache.get(searchUUID);
				}
				if(foreignSkin != null) {
					foreignSkin.lastHit = EaglerXVelocityAPIHelper.steadyTimeMillis();
					sender.sendEaglerMessage(foreignSkin.data.get(sender.getEaglerProtocol()));
				}else {
					if (sender.skinUUIDLookupRateLimiter
							.rateLimit(EaglerXVelocity.getEagler().getConfig().getUuidRateLimitPlayer())
							&& !isLimitedAsAntagonist(sender.getUniqueId())) {
						if(sender.isOnlineMode()) {
							doAsync(() -> {
								processResolveProfileTextureByUUIDForeign(sender, searchUUID);
							});
						}else {
							sender.sendEaglerMessage(
									new SPacketOtherSkinPresetEAG(searchUUID.getMostSignificantBits(),
											searchUUID.getLeastSignificantBits(), isAlex(searchUUID) ? 1 : 0));
						}
					}
				}
			}
		}
	}
	
	public void processGetOtherSkin(UUID searchUUID, String skinURL, EaglerPlayerData sender) {
		EaglerVelocityConfig config = EaglerXVelocity.getEagler().getConfig();
		if(!sender.skinLookupRateLimiter.rateLimit(masterRateLimitPerPlayer)) {
			return;
		}
		CachedForeignSkin foreignSkin;
		synchronized(foreignSkinCache) {
			foreignSkin = foreignSkinCache.get(searchUUID);
		}
		if(foreignSkin != null) {
			foreignSkin.lastHit = EaglerXVelocityAPIHelper.steadyTimeMillis();
			sender.sendEaglerMessage(foreignSkin.data.get(sender.getEaglerProtocol()));
		}else {
			Collection<UUID> possiblePlayers;
			synchronized(onlinePlayersFromTexturesMap) {
				possiblePlayers = onlinePlayersFromTexturesMap.get(searchUUID);
			}
			boolean playersExist = possiblePlayers.size() > 0;
			if(playersExist) {
				for(UUID uuid : possiblePlayers) {
					CachedPlayerSkin maybeCachedPacket;
					synchronized(onlinePlayersCache) {
						maybeCachedPacket = onlinePlayersCache.get(uuid);
					}
					if(maybeCachedPacket != null) {
						sender.sendEaglerMessage(maybeCachedPacket.data.get(sender.getEaglerProtocol(),
								searchUUID.getMostSignificantBits(), searchUUID.getLeastSignificantBits()));
						return;
					}
				}
			}
			if(skinURL.startsWith("eagler://")) { // customs skulls from exported singleplayer worlds
				sender.sendEaglerMessage(new SPacketOtherSkinPresetEAG(searchUUID.getMostSignificantBits(),
						searchUUID.getLeastSignificantBits(), 0));
				return;
			}
			if(sender.skinTextureDownloadRateLimiter.rateLimit(config.getSkinRateLimitPlayer()) && !isLimitedAsAntagonist(sender.getUniqueId())) {
				doAsync(() -> {
					processResolveURLTextureForForeign(sender, searchUUID, searchUUID, skinURL, -1);
				});
			}
		}
	}
	
	private void processResolveURLTextureForOnline(final EaglerPlayerData initiator, final UUID onlineCacheUUID,
			final UUID skinUUID, final String urlStr, final int modelId) {
		synchronized(pendingTextures) {
			PendingTextureDownload alreadyPending = pendingTextures.get(skinUUID);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<byte[]>() {

						@Override
						public void accept(byte[] t) {
							CachedPlayerSkin skin;
							synchronized(onlinePlayersCache) {
								skin = onlinePlayersCache.get(onlineCacheUUID);
							}
							if(skin != null) {
								initiator.sendEaglerMessage(skin.data.get(initiator.getEaglerProtocol()));
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
							synchronized (onlinePlayersCache) {
								onlinePlayersCache.put(onlineCacheUUID, skin);
							}
							initiator.sendEaglerMessage(skin.data.get(initiator.getEaglerProtocol()));
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
	
	private void processResolveURLTextureForForeign(final EaglerPlayerData initiator, final UUID foreignCacheUUID,
			final UUID skinUUID, final String urlStr, final int modelId) {
		synchronized(pendingTextures) {
			PendingTextureDownload alreadyPending = pendingTextures.get(skinUUID);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<byte[]>() {

						@Override
						public void accept(byte[] t) {
							CachedForeignSkin skin;
							synchronized(foreignSkinCache) {
								skin = foreignSkinCache.get(foreignCacheUUID);
							}
							if(skin != null) {
								initiator.sendEaglerMessage(skin.data.get(initiator.getEaglerProtocol()));
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
							synchronized (foreignSkinCache) {
								foreignSkinCache.put(foreignCacheUUID, skin);
							}
							initiator.sendEaglerMessage(skin.data.get(initiator.getEaglerProtocol()));
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
	
	private void processResolveProfileTextureByUUIDForOnline(final EaglerPlayerData initiator, final UUID playerUUID) {
		synchronized(pendingUUIDs) {
			PendingProfileUUIDLookup alreadyPending = pendingUUIDs.get(playerUUID);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<CacheFetchedProfile>() {

						@Override
						public void accept(CacheFetchedProfile t) {
							if(t == null || t.texture == null) {
								CachedPlayerSkin skin;
								synchronized(onlinePlayersCache) {
									skin = onlinePlayersCache.get(playerUUID);
								}
								if(skin != null) {
									initiator.sendEaglerMessage(skin.data.get(initiator.getEaglerProtocol()));
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
							synchronized(onlinePlayersCache) {
								onlinePlayersCache.put(playerUUID, skin);
							}
							initiator.sendEaglerMessage(skin.data.get(initiator.getEaglerProtocol()));
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
	
	private void processResolveProfileTextureByNameForOnline(final EaglerPlayerData initiator, final String playerName, final UUID mapUUID) {
		synchronized(pendingNameLookups) {
			PendingProfileNameLookup alreadyPending = pendingNameLookups.get(playerName);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<CacheFetchedProfile>() {

						@Override
						public void accept(CacheFetchedProfile t) {
							if(t == null || t.texture == null) {
								CachedPlayerSkin skin;
								synchronized(onlinePlayersCache) {
									skin = onlinePlayersCache.get(t.uuid);
								}
								if(skin != null) {
									initiator.sendEaglerMessage(skin.data.get(initiator.getEaglerProtocol()));
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
							synchronized(onlinePlayersCache) {
								onlinePlayersCache.put(mapUUID, skin);
							}
							initiator.sendEaglerMessage(skin.data.get(initiator.getEaglerProtocol()));
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
	
	private void processResolveProfileTextureByUUIDForeign(final EaglerPlayerData initiator, final UUID playerUUID) {
		synchronized(pendingUUIDs) {
			PendingProfileUUIDLookup alreadyPending = pendingUUIDs.get(playerUUID);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<CacheFetchedProfile>() {

						@Override
						public void accept(CacheFetchedProfile t) {
							if(t == null || t.texture == null) {
								CachedForeignSkin skin;
								synchronized(foreignSkinCache) {
									skin = foreignSkinCache.get(playerUUID);
								}
								if(skin != null) {
									initiator.sendEaglerMessage(skin.data.get(initiator.getEaglerProtocol()));
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
							synchronized(foreignSkinCache) {
								foreignSkinCache.put(playerUUID, skin);
							}
							initiator.sendEaglerMessage(skin.data.get(initiator.getEaglerProtocol()));
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
		synchronized(foreignSkinCache) {
			foreignSkinCache.remove(clientUUID);
		}
		synchronized(onlinePlayersCache) {
			onlinePlayersCache.put(clientUUID, new CachedPlayerSkin(generatedPacket, null, modelId));
		}
	}
	
	public void unregisterPlayer(UUID clientUUID) {
		CachedPlayerSkin data;
		synchronized(onlinePlayersCache) {
			data = onlinePlayersCache.remove(clientUUID);
		}
		if(data != null) {
			synchronized(foreignSkinCache) {
				foreignSkinCache.put(clientUUID, new CachedForeignSkin(clientUUID, data.data, data.modelId));
			}
			if(data.textureUUID != null) {
				synchronized(foreignSkinCache) {
					foreignSkinCache.put(data.textureUUID, new CachedForeignSkin(data.textureUUID, data.data, data.modelId));
				}
			}
			deletePlayerTextureAssociation(clientUUID, data.textureUUID);
		}else {
			deletePlayerTextureAssociation(clientUUID, null);
		}
	}
	
	private void deletePlayerTextureAssociation(UUID clientUUID, UUID textureUUID) {
		if(textureUUID != null) {
			synchronized(onlinePlayersToTexturesMap) {
				onlinePlayersToTexturesMap.remove(clientUUID);
			}
			synchronized(onlinePlayersFromTexturesMap) {
				onlinePlayersFromTexturesMap.remove(textureUUID, clientUUID);
			}
		}else {
			UUID removedUUID;
			synchronized(onlinePlayersToTexturesMap) {
				removedUUID = onlinePlayersToTexturesMap.remove(clientUUID);
			}
			if(removedUUID != null) {
				synchronized(onlinePlayersFromTexturesMap) {
					onlinePlayersFromTexturesMap.remove(removedUUID, clientUUID);
				}
			}
		}
	}
	
	public void registerTextureToPlayerAssociation(UUID textureUUID, UUID playerUUID) {
		synchronized(onlinePlayersFromTexturesMap) {
			onlinePlayersFromTexturesMap.put(textureUUID, playerUUID);
		}
		synchronized(onlinePlayersToTexturesMap) {
			onlinePlayersToTexturesMap.put(playerUUID, textureUUID);
		}
		CachedForeignSkin foreign;
		synchronized(foreignSkinCache) {
			foreign = foreignSkinCache.remove(textureUUID);
		}
		if(foreign != null) {
			synchronized(onlinePlayersCache) {
				onlinePlayersCache.put(playerUUID, new CachedPlayerSkin(foreign.data, textureUUID, foreign.modelKnown));
			}
		}
	}

	public void processForceSkin(UUID playerUUID, EaglerPlayerData eaglerHandler) {
		CachedPlayerSkin maybeCachedPacket;
		synchronized(onlinePlayersCache) {
			maybeCachedPacket = onlinePlayersCache.get(playerUUID);
		}
		
		if(maybeCachedPacket != null) {
			eaglerHandler.sendEaglerMessage(maybeCachedPacket.data.getForceClientV4());
		}else {
			UUID playerTexture;
			synchronized(onlinePlayersToTexturesMap) {
				playerTexture = onlinePlayersToTexturesMap.get(playerUUID);
			}
			if(playerTexture != null) {
				Collection<UUID> possiblePlayers;
				synchronized(onlinePlayersFromTexturesMap) {
					possiblePlayers = onlinePlayersFromTexturesMap.get(playerTexture);
				}
				boolean playersExist = possiblePlayers.size() > 0;
				if(playersExist) {
					for(UUID uuid : possiblePlayers) {
						synchronized(onlinePlayersCache) {
							maybeCachedPacket = onlinePlayersCache.get(uuid);
						}
						if(maybeCachedPacket != null) {
							SkinPacketVersionCache rewritten = SkinPacketVersionCache.rewriteUUID(
									maybeCachedPacket.data, playerUUID.getMostSignificantBits(),
									playerUUID.getLeastSignificantBits());
							synchronized(onlinePlayersCache) {
								onlinePlayersCache.put(playerUUID, new CachedPlayerSkin(rewritten,
										maybeCachedPacket.textureUUID, maybeCachedPacket.modelId));
							}
							eaglerHandler.sendEaglerMessage(rewritten.getForceClientV4());
							return;
						}
					}
				}
				CachedForeignSkin foreignSkin;
				synchronized(foreignSkinCache) {
					foreignSkin = foreignSkinCache.get(playerTexture);
				}
				if(foreignSkin != null && foreignSkin.modelKnown != -1) {
					synchronized(onlinePlayersCache) {
						onlinePlayersCache.put(playerUUID,
								new CachedPlayerSkin(SkinPacketVersionCache.rewriteUUID(foreignSkin.data,
										playerUUID.getMostSignificantBits(), playerUUID.getLeastSignificantBits()),
										playerTexture, foreignSkin.modelKnown));
					}
					synchronized(foreignSkinCache) {
						foreignSkinCache.remove(playerTexture);
					}
					eaglerHandler.sendEaglerMessage(foreignSkin.data.getForceClientV4());
					return;
				}
			}
			GameProfile loginProfile = eaglerHandler.getGameProfile();
			if(loginProfile != null) {
				List<Property> props = loginProfile.getProperties();
				if(props.size() > 0) {
					for(int i = 0, l = props.size(); i < l; ++i) {
						Property pp = props.get(i);
						if(pp.getName().equals("textures")) {
							try {
								String jsonStr = SkinPackets.bytesToAscii(Base64.decodeBase64(pp.getValue()));
								JsonObject json = JsonParser.parseString(jsonStr).getAsJsonObject();
								JsonObject skinObj = json.getAsJsonObject("SKIN");
								if(skinObj != null) {
									JsonElement url = json.get("url");
									if(url != null) {
										String urlStr = SkinService.sanitizeTextureURL(url.getAsString());
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
										
										CachedForeignSkin foreignSkin;
										synchronized(foreignSkinCache) {
											foreignSkin = foreignSkinCache.remove(skinUUID);
										}
										if(foreignSkin != null) {
											registerTextureToPlayerAssociation(skinUUID, playerUUID);
											SkinPacketVersionCache rewrite = SkinPacketVersionCache
													.rewriteUUIDModel(foreignSkin.data,
															playerUUID.getMostSignificantBits(),
															playerUUID.getLeastSignificantBits(), model);
											synchronized(onlinePlayersCache) {
												onlinePlayersCache.put(playerUUID, new CachedPlayerSkin(rewrite, skinUUID, model));
											}
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
				CachedForeignSkin foreignSkin;
				synchronized(foreignSkinCache) {
					foreignSkin = foreignSkinCache.get(playerUUID);
				}
				if(foreignSkin != null) {
					foreignSkin.lastHit = EaglerXVelocityAPIHelper.steadyTimeMillis();
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
	
	private void processResolveURLTextureForOnlineToForce(final EaglerPlayerData initiator, final UUID onlineCacheUUID,
			final UUID skinUUID, final String urlStr, final int modelId) {
		synchronized(pendingTextures) {
			PendingTextureDownload alreadyPending = pendingTextures.get(skinUUID);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<byte[]>() {

						@Override
						public void accept(byte[] t) {
							CachedPlayerSkin skin;
							synchronized(onlinePlayersCache) {
								skin = onlinePlayersCache.get(onlineCacheUUID);
							}
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
							synchronized (onlinePlayersCache) {
								onlinePlayersCache.put(onlineCacheUUID, skin);
							}
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
	
	private void processResolveURLTextureForForeignToForce(final EaglerPlayerData initiator, final UUID foreignCacheUUID,
			final UUID skinUUID, final String urlStr, final int modelId) {
		synchronized(pendingTextures) {
			PendingTextureDownload alreadyPending = pendingTextures.get(skinUUID);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<byte[]>() {

						@Override
						public void accept(byte[] t) {
							CachedForeignSkin skin;
							synchronized(foreignSkinCache) {
								skin = foreignSkinCache.get(foreignCacheUUID);
							}
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
							synchronized (foreignSkinCache) {
								foreignSkinCache.put(foreignCacheUUID, skin);
							}
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
	
	private void processResolveProfileTextureByUUIDForOnlineToForce(final EaglerPlayerData initiator, final UUID playerUUID) {
		synchronized(pendingUUIDs) {
			PendingProfileUUIDLookup alreadyPending = pendingUUIDs.get(playerUUID);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<CacheFetchedProfile>() {

						@Override
						public void accept(CacheFetchedProfile t) {
							if(t == null || t.texture == null) {
								CachedPlayerSkin skin;
								synchronized(onlinePlayersCache) {
									skin = onlinePlayersCache.get(playerUUID);
								}
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
							synchronized(onlinePlayersCache) {
								onlinePlayersCache.put(playerUUID, skin);
							}
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
	
	private void processResolveProfileTextureByNameForOnlineToForce(final EaglerPlayerData initiator, final String playerName, final UUID mapUUID) {
		synchronized(pendingNameLookups) {
			PendingProfileNameLookup alreadyPending = pendingNameLookups.get(playerName);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<CacheFetchedProfile>() {

						@Override
						public void accept(CacheFetchedProfile t) {
							if(t == null || t.texture == null) {
								CachedPlayerSkin skin;
								synchronized(onlinePlayersCache) {
									skin = onlinePlayersCache.get(t.uuid);
								}
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
							synchronized(onlinePlayersCache) {
								onlinePlayersCache.put(mapUUID, skin);
							}
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
	
	private void processResolveProfileTextureByUUIDForeignToForce(final EaglerPlayerData initiator, final UUID playerUUID) {
		synchronized(pendingUUIDs) {
			PendingProfileUUIDLookup alreadyPending = pendingUUIDs.get(playerUUID);
			if(alreadyPending != null) {
				if(alreadyPending.antagonists.add(initiator.getUniqueId())) {
					alreadyPending.callbacks.add(new Consumer<CacheFetchedProfile>() {

						@Override
						public void accept(CacheFetchedProfile t) {
							if(t == null || t.texture == null) {
								CachedForeignSkin skin;
								synchronized(foreignSkinCache) {
									skin = foreignSkinCache.get(playerUUID);
								}
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
							synchronized(foreignSkinCache) {
								foreignSkinCache.put(playerUUID, skin);
							}
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
		long millis = EaglerXVelocityAPIHelper.steadyTimeMillis();
		
		synchronized(foreignSkinCache) {
			Iterator<CachedForeignSkin> itr = foreignSkinCache.values().iterator();
			while(itr.hasNext()) {
				if(millis - itr.next().lastHit > 900000l) { // 15 minutes
					itr.remove();
				}
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

		int cooldownPeriod = 60000 / EaglerXVelocity.getEagler().getConfig().getAntagonistsRateLimit();
		int elapsedCooldown = (int)(millis - antagonistCooldown);
		elapsedCooldown /= cooldownPeriod;
		if(elapsedCooldown > 0) {
			antagonistCooldown += elapsedCooldown * cooldownPeriod;
			synchronized(antagonists) {
				Iterator<UUID> itr = antagonists.keySet().iterator();
				while(itr.hasNext()) {
					UUID key = itr.next();
					int i = antagonists.getInt(key) - elapsedCooldown;
					if(i <= 0) {
						itr.remove();
					}else {
						antagonists.put(key, i);
					}
				}
			}
		}
		
		cacheProvider.flush();
	}

	public SkinPacketVersionCache getSkin(UUID playerUUID) {
		CachedPlayerSkin skin;
		synchronized(onlinePlayersCache) {
			skin = onlinePlayersCache.get(playerUUID);
		}
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
		int limit = EaglerXVelocity.getEagler().getConfig().getAntagonistsRateLimit();
		limit += limit >> 1;
		synchronized(antagonists) {
			int i = antagonists.getInt(uuid);
			return i != antagonists.defaultReturnValue() && i > limit;
		}
	}
	
	private void resetMaps() {
		synchronized(onlinePlayersCache) {
			onlinePlayersCache.clear();
		}
		synchronized(onlinePlayersFromTexturesMap) {
			onlinePlayersFromTexturesMap.clear();
		}
		synchronized(onlinePlayersToTexturesMap) {
			onlinePlayersToTexturesMap.clear();
		}
		synchronized(foreignSkinCache) {
			foreignSkinCache.clear();
		}
		synchronized(pendingTextures) {
			pendingTextures.clear();
		}
		synchronized(pendingUUIDs) {
			pendingUUIDs.clear();
		}
		synchronized(pendingNameLookups) {
			pendingNameLookups.clear();
		}
		synchronized(antagonists) {
			antagonists.clear();
		}
	}
	
	private ScheduledTask doAsync(Runnable handler) {
		return EaglerXVelocity.proxy().getScheduler().buildTask(EaglerXVelocity.getEagler(), handler).schedule();
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
			if(host == null) {
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
