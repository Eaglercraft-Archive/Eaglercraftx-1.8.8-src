package net.lax1dude.eaglercraft.v1_8.plugin.gateway_velocity.skins;

import java.util.UUID;

/**
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
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
