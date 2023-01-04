package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.EaglerXBungee;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.BinaryHttpClient.Response;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.ICacheProvider.CacheLoadedProfile;
import net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins.ICacheProvider.CacheLoadedSkin;

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
public class AsyncSkinProvider {

	private static class SkinConsumerImpl implements Consumer<Response> {

		protected final Consumer<byte[]> responseConsumer;

		protected SkinConsumerImpl(Consumer<byte[]> consumer) {
			this.responseConsumer = consumer;
		}

		protected void doAccept(byte[] v) {
			try {
				responseConsumer.accept(v);
			}catch(Throwable t) {
				EaglerXBungee.logger().log(Level.SEVERE, "Exception thrown caching new skin!", t);
			}
		}

		@Override
		public void accept(Response response) {
			if(response == null || response.exception != null || response.code != 200 || response.data == null) {
				doAccept(null);
			}else {
				BufferedImage image;
				try {
					image = ImageIO.read(new ByteArrayInputStream(response.data));
				}catch(IOException ex) {
					doAccept(null);
					return;
				}
				try {
					int srcWidth = image.getWidth();
					int srcHeight = image.getHeight();
					if(srcWidth < 64 || srcWidth > 512 || srcHeight < 32 || srcHeight > 512) {
						doAccept(null);
						return;
					}
					if(srcWidth != 64 || srcHeight != 64) {
						if(srcWidth % 64 == 0) {
							if(srcWidth == srcHeight * 2) {
								BufferedImage scaled = new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB);
								Graphics2D graphics = scaled.createGraphics();
								graphics.drawImage(image, 0, 0, 64, 32, 0, 0, srcWidth, srcHeight, null);
								graphics.dispose();
								image = scaled;
								srcWidth = 64;
								srcHeight = 32;
							}else if(srcWidth == srcHeight) {
								BufferedImage scaled = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
								Graphics2D graphics = scaled.createGraphics();
								graphics.drawImage(image, 0, 0, 64, 64, 0, 0, srcWidth, srcHeight, null);
								graphics.dispose();
								image = scaled;
								srcWidth = 64;
								srcHeight = 64;
							}else {
								doAccept(null);
								return;
							}
						}else {
							doAccept(null);
							return;
						}
					}
					if(srcWidth == 64 && srcHeight == 64) {
						int[] tmp = new int[4096];
						byte[] loadedPixels = new byte[16384];
						image.getRGB(0, 0, 64, 64, tmp, 0, 64);
						SkinRescaler.convertToBytes(tmp, loadedPixels);
						SkinPackets.setAlphaForChest(loadedPixels, (byte)255);
						doAccept(loadedPixels);
						return;
					}else if(srcWidth == 64 && srcHeight == 32) {
						int[] tmp1 = new int[2048];
						byte[] loadedPixels = new byte[16384];
						image.getRGB(0, 0, 64, 32, tmp1, 0, 64);
						SkinRescaler.convert64x32To64x64(tmp1, loadedPixels);
						SkinPackets.setAlphaForChest(loadedPixels, (byte)255);
						doAccept(loadedPixels);
						return;
					}else {
						doAccept(null);
						return;
					}
				}catch(Throwable t) {
					
				}
			}
		}

	}

	private static class SkinCachingConsumer implements Consumer<byte[]> {

		protected final UUID skinUUID;
		protected final String skinTexture;
		protected final ICacheProvider cacheProvider;
		protected final Consumer<byte[]> responseConsumer;

		protected SkinCachingConsumer(UUID skinUUID, String skinTexture, ICacheProvider cacheProvider,
				Consumer<byte[]> responseConsumer) {
			this.skinUUID = skinUUID;
			this.skinTexture = skinTexture;
			this.cacheProvider = cacheProvider;
			this.responseConsumer = responseConsumer;
		}

		@Override
		public void accept(byte[] skin) {
			if(skin != null) {
				try {
					cacheProvider.cacheSkinByUUID(skinUUID, skinTexture, skin);
				}catch(Throwable t) {
					EaglerXBungee.logger().log(Level.SEVERE, "Exception thrown writing new skin to database!", t);
				}
				responseConsumer.accept(skin);
			}else {
				responseConsumer.accept(null);
			}
		}

	}

	public static class CacheFetchedProfile {

		public final UUID uuid;
		public final String username;
		public final String texture;
		public final UUID textureUUID;
		public final String model;

		protected CacheFetchedProfile(UUID uuid, String username, String texture, String model) {
			this.uuid = uuid;
			this.username = username;
			this.texture = texture;
			this.textureUUID = SkinPackets.createEaglerURLSkinUUID(texture);
			this.model = model;
		}

		protected CacheFetchedProfile(CacheLoadedProfile profile) {
			this.uuid = profile.uuid;
			this.username = profile.username;
			this.texture = profile.texture;
			this.textureUUID = SkinPackets.createEaglerURLSkinUUID(profile.texture);
			this.model = profile.model;
		}

	}

	private static class ProfileConsumerImpl implements Consumer<Response> {

		protected final UUID uuid;
		protected final Consumer<CacheFetchedProfile> responseConsumer;

		protected ProfileConsumerImpl(UUID uuid, Consumer<CacheFetchedProfile> responseConsumer) {
			this.uuid = uuid;
			this.responseConsumer = responseConsumer;
		}

		protected void doAccept(CacheFetchedProfile v) {
			try {
				responseConsumer.accept(v);
			}catch(Throwable t) {
				EaglerXBungee.logger().log(Level.SEVERE, "Exception thrown caching new profile!", t);
			}
		}

		@Override
		public void accept(Response response) {
			if(response == null || response.exception != null || response.code != 200 || response.data == null) {
				doAccept(null);
			}else {
				try {
					JsonObject json = (new JsonParser()).parse(new String(response.data, StandardCharsets.UTF_8)).getAsJsonObject();
					String username = json.get("name").getAsString().toLowerCase();
					String texture = null;
					String model = null;
					JsonElement propsElement = json.get("properties");
					if(propsElement != null) {
						try {
							JsonArray properties = propsElement.getAsJsonArray();
							if(properties.size() > 0) {
								for(int i = 0, l = properties.size(); i < l; ++i) {
									JsonElement prop = properties.get(i);
									if(prop.isJsonObject()) {
										JsonObject propObj = prop.getAsJsonObject();
										if(propObj.get("name").getAsString().equals("textures")) {
											String value = new String(Base64.decodeBase64(propObj.get("value").getAsString()), StandardCharsets.UTF_8);
											JsonObject texturesJson = (new JsonParser()).parse(value).getAsJsonObject();
											if(texturesJson != null && texturesJson.has("textures")) {
												texturesJson = texturesJson.getAsJsonObject("textures");
												JsonElement skin = texturesJson.get("SKIN");
												if(skin != null) {
													model = "default";
													JsonObject skinObj = skin.getAsJsonObject();
													JsonElement urlElement = skinObj.get("url");
													if(urlElement != null && !urlElement.isJsonNull()) {
														texture = urlElement.getAsString();
													}
													JsonElement metaElement = skinObj.get("metadata");
													if(metaElement != null) {
														JsonObject metaObj = metaElement.getAsJsonObject();
														JsonElement modelElement = metaObj.get("model");
														if(modelElement != null) {
															model = modelElement.getAsString();
														}
													}
												}
											}
											break;
										}
									}
								}
							}
						}catch(Throwable t2) {
						}
					}
					if(texture == null && model == null) {
						model = SkinService.isAlex(uuid) ? "slim" : "default";
					}
					doAccept(new CacheFetchedProfile(uuid, username, texture, model));
				}catch(Throwable ex) {
					doAccept(null);
				}
			}
		}

	}

	private static class ProfileCachingConsumer implements Consumer<CacheFetchedProfile> {

		protected final UUID uuid;
		protected final ICacheProvider cacheProvider;
		protected final Consumer<CacheFetchedProfile> responseConsumer;

		protected ProfileCachingConsumer(UUID uuid, ICacheProvider cacheProvider, Consumer<CacheFetchedProfile> responseConsumer) {
			this.uuid = uuid;
			this.cacheProvider = cacheProvider;
			this.responseConsumer = responseConsumer;
		}

		@Override
		public void accept(CacheFetchedProfile profile) {
			if(profile != null) {
				try {
					cacheProvider.cacheProfileByUUID(uuid, profile.username, profile.texture, profile.model);
				}catch(Throwable t) {
					EaglerXBungee.logger().log(Level.SEVERE, "Exception thrown writing new profile to database!", t);
				}
				responseConsumer.accept(profile);
			}else {
				responseConsumer.accept(null);
			}
		}

	}

	private static class UsernameToUUIDConsumerImpl implements Consumer<Response> {

		protected final String username;
		protected final ICacheProvider cacheProvider;
		protected final Consumer<CacheFetchedProfile> responseConsumer;

		protected UsernameToUUIDConsumerImpl(String username, ICacheProvider cacheProvider, Consumer<CacheFetchedProfile> responseConsumer) {
			this.username = username;
			this.cacheProvider = cacheProvider;
			this.responseConsumer = responseConsumer;
		}

		protected void doAccept(CacheFetchedProfile v) {
			try {
				responseConsumer.accept(v);
			}catch(Throwable t) {
				EaglerXBungee.logger().log(Level.SEVERE, "Exception thrown caching new profile!", t);
			}
		}

		@Override
		public void accept(Response response) {
			if(response == null || response.exception != null || response.code != 200 || response.data == null) {
				doAccept(null);
			}else {
				try {
					JsonObject json = (new JsonParser()).parse(new String(response.data, StandardCharsets.UTF_8)).getAsJsonObject();
					String loadUsername = json.get("name").getAsString().toLowerCase();
					if(!username.equals(loadUsername)) {
						doAccept(null);
					}
					UUID mojangUUID = SkinService.parseMojangUUID(json.get("id").getAsString());
					lookupProfileByUUID(mojangUUID, cacheProvider, responseConsumer, false);
				}catch(Throwable t) {
					doAccept(null);
				}
			}
		}

	}

	private static final SimpleRateLimiter rateLimitDownload = new SimpleRateLimiter();
	private static final SimpleRateLimiter rateLimitLookup = new SimpleRateLimiter();
 
	public static void downloadSkin(String skinTexture, ICacheProvider cacheProvider, Consumer<byte[]> responseConsumer) {
		downloadSkin(SkinPackets.createEaglerURLSkinUUID(skinTexture), skinTexture, cacheProvider, responseConsumer);
	}

	public static void downloadSkin(UUID skinUUID, String skinTexture, ICacheProvider cacheProvider, Consumer<byte[]> responseConsumer) {
		CacheLoadedSkin loadedSkin = cacheProvider.loadSkinByUUID(skinUUID);
		if(loadedSkin == null) {
			URI uri;
			try {
				uri = URI.create(skinTexture);
			}catch(IllegalArgumentException ex) {
				try {
					responseConsumer.accept(null);
				}catch(Throwable t) {
					EaglerXBungee.logger().log(Level.SEVERE, "Exception thrown handling invalid skin!", t);
				}
				throw new CancelException();
			}
			int globalRatelimit = EaglerXBungee.getEagler().getConfig().getSkinRateLimitGlobal();
			boolean isRateLimit;
			synchronized(rateLimitDownload) {
				isRateLimit = !rateLimitDownload.rateLimit(globalRatelimit);
			}
			if(!isRateLimit) {
				BinaryHttpClient.asyncRequest("GET", uri, new SkinConsumerImpl(
						new SkinCachingConsumer(skinUUID, skinTexture, cacheProvider, responseConsumer)));
			}else {
				EaglerXBungee.logger().warning("skin system reached the global texture download ratelimit of " + globalRatelimit + " while downloading up \"" + skinTexture + "\"");
				throw new CancelException();
			}
		}else {
			try {
				responseConsumer.accept(loadedSkin.texture);
			}catch(Throwable t) {
				EaglerXBungee.logger().log(Level.SEVERE, "Exception thrown processing cached skin!", t);
			}
			throw new CancelException();
		}
	}

	public static void lookupProfileByUUID(UUID playerUUID, ICacheProvider cacheProvider, Consumer<CacheFetchedProfile> responseConsumer) {
		lookupProfileByUUID(playerUUID, cacheProvider, responseConsumer, true);
	}

	private static void lookupProfileByUUID(UUID playerUUID, ICacheProvider cacheProvider, Consumer<CacheFetchedProfile> responseConsumer, boolean rateLimit) {
		CacheLoadedProfile profile = cacheProvider.loadProfileByUUID(playerUUID);
		if(profile == null) {
			URI requestURI = URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + SkinService.getMojangUUID(playerUUID));
			int globalRatelimit = EaglerXBungee.getEagler().getConfig().getUuidRateLimitGlobal();
			boolean isRateLimit;
			if(rateLimit) {
				synchronized(rateLimitLookup) {
					isRateLimit = !rateLimitLookup.rateLimit(globalRatelimit);
				}
			}else {
				isRateLimit = false;
			}
			if(!isRateLimit) {
				BinaryHttpClient.asyncRequest("GET", requestURI, new ProfileConsumerImpl(playerUUID,
						new ProfileCachingConsumer(playerUUID, cacheProvider, responseConsumer)));
			}else {
				EaglerXBungee.logger().warning("skin system reached the global UUID lookup ratelimit of " + globalRatelimit + " while looking up \"" + playerUUID + "\"");
				throw new CancelException();
			}
		}else {
			try {
				responseConsumer.accept(new CacheFetchedProfile(profile));
			}catch(Throwable t) {
				EaglerXBungee.logger().log(Level.SEVERE, "Exception thrown processing cached profile!", t);
			}
			throw new CancelException();
		}
	}

	public static void lookupProfileByUsername(String playerUsername, ICacheProvider cacheProvider, Consumer<CacheFetchedProfile> responseConsumer) {
		String playerUsernameLower = playerUsername.toLowerCase();
		CacheLoadedProfile profile = cacheProvider.loadProfileByUsername(playerUsernameLower);
		if(profile == null) {
			if(!playerUsernameLower.equals(playerUsernameLower.replaceAll("[^a-z0-9_]", "_").trim())) {
				try {
					responseConsumer.accept(null);
				}catch(Throwable t) {
					EaglerXBungee.logger().log(Level.SEVERE, "Exception thrown processing invalid profile!", t);
				}
				throw new CancelException();
			}
			URI requestURI = URI.create("https://api.mojang.com/users/profiles/minecraft/" + playerUsername);
			int globalRatelimit = EaglerXBungee.getEagler().getConfig().getUuidRateLimitGlobal();
			boolean isRateLimit;
			synchronized(rateLimitLookup) {
				isRateLimit = !rateLimitLookup.rateLimit(globalRatelimit);
			}
			if(!isRateLimit) {
				BinaryHttpClient.asyncRequest("GET", requestURI, new UsernameToUUIDConsumerImpl(playerUsername, cacheProvider, responseConsumer));
			}else {
				EaglerXBungee.logger().warning("skin system reached the global UUID lookup ratelimit of " + globalRatelimit + " while looking up \"" + playerUsername + "\"");
				throw new CancelException();
			}
		}else {
			try {
				responseConsumer.accept(new CacheFetchedProfile(profile));
			}catch(Throwable t) {
				EaglerXBungee.logger().log(Level.SEVERE, "Exception thrown processing cached profile!", t);
			}
			throw new CancelException();
		}
	}

	public static class CancelException extends RuntimeException {

	}

}
