package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins;

import java.util.UUID;

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
public interface ICacheProvider {

	public static class CacheException extends RuntimeException {

		public CacheException() {
			super();
		}

		public CacheException(String message, Throwable cause) {
			super(message, cause);
		}

		public CacheException(String message) {
			super(message);
		}

		public CacheException(Throwable cause) {
			super(cause);
		}

	}

	public static class CacheLoadedSkin {

		public final UUID uuid;
		public final String url;
		public final byte[] texture;

		public CacheLoadedSkin(UUID uuid, String url, byte[] texture) {
			this.uuid = uuid;
			this.url = url;
			this.texture = texture;
		}

	}

	public static class CacheLoadedProfile {

		public final UUID uuid;
		public final String username;
		public final String texture;
		public final String model;

		public CacheLoadedProfile(UUID uuid, String username, String texture, String model) {
			this.uuid = uuid;
			this.username = username;
			this.texture = texture;
			this.model = model;
		}

		public UUID getSkinUUID() {
			return SkinPackets.createEaglerURLSkinUUID(texture);
		}

	}

	CacheLoadedSkin loadSkinByUUID(UUID uuid) throws CacheException;

	void cacheSkinByUUID(UUID uuid, String url, byte[] textureBlob) throws CacheException;

	CacheLoadedProfile loadProfileByUUID(UUID uuid) throws CacheException;

	CacheLoadedProfile loadProfileByUsername(String username) throws CacheException;

	void cacheProfileByUUID(UUID uuid, String username, String texture, String model) throws CacheException;

	void flush() throws CacheException;

	void destroy();

}
