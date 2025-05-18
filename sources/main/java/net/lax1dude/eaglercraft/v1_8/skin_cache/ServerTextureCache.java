/*
 * Copyright (c) 2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.skin_cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.carrotsearch.hppc.ObjectLongHashMap;
import com.carrotsearch.hppc.ObjectLongMap;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
import net.lax1dude.eaglercraft.v1_8.mojang.authlib.TexturesProperty;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;
import net.lax1dude.eaglercraft.v1_8.profile.SkinModel;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.client.StateFlags;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.*;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public abstract class ServerTextureCache {

	static final int STATE_S_PENDING = 1;

	static final int STATE_C_PENDING = 2;

	static final int STATE_SC_PENDING = STATE_S_PENDING | STATE_C_PENDING;

	static final int STATE_S_COMPLETE = 4;

	static final int STATE_C_COMPLETE = 8;

	static final int STATE_S_LOADED = 16;

	static final int STATE_C_LOADED = 32;

	static int texId = 0;

	protected final int protocolVers;
	protected final NetHandlerPlayClient netHandler;
	protected final TextureManager textureManager;
	protected final EaglercraftUUID self;

	private EaglercraftUUID mruPlayerKey = null;
	private PlayerTextureEntry mruPlayerTexture = null;
	private final Map<EaglercraftUUID, PlayerTextureEntry> playerTextures = new HashMap<>(256);

	private EaglercraftUUID mruForeignKey = null;
	private ForeignTextureEntry mruForeignTexture = null;
	private final Map<EaglercraftUUID, ForeignTextureEntry> foreignTextures = new HashMap<>(256);

	private final Set<PlayerTextureEntry> pendingPlayerToLookup = new HashSet<>(32);
	private final Set<ForeignTextureEntry> pendingForeignToLookup = new HashSet<>(32);
	private final ObjectLongMap<EaglercraftUUID> evictedPlayers = new ObjectLongHashMap<>(32);

	private int nextFlush = 200;
	private int nextEvictFlush = 20;
	private int flush = 0;

	public static ServerTextureCache create(NetHandlerPlayClient netHandler, TextureManager textureManager) {
		if (netHandler.getEaglerMessageProtocol().ver >= 5) {
			return new ServerTextureCacheV5(netHandler, textureManager);
		} else {
			return new ServerTextureCacheOld(netHandler, textureManager);
		}
	}

	public ServerTextureCache(NetHandlerPlayClient netHandler, TextureManager textureManager) {
		this.protocolVers = netHandler.getEaglerMessageProtocol().ver;
		this.netHandler = netHandler;
		this.textureManager = textureManager;
		this.self = netHandler.getGameProfile().getId();
	}

	private PlayerTextureEntry loadPlayer(EaglercraftUUID uuid) {
		if (mruPlayerKey != null && uuid.equals(mruPlayerKey)) {
			return mruPlayerTexture;
		}
		PlayerTextureEntry ret = playerTextures.get(uuid);
		if (ret == null) {
			playerTextures.put(uuid, ret = new PlayerTextureEntry(uuid));
		}
		ret.lastHit = EagRuntime.steadyTimeMillis();
		mruPlayerKey = uuid;
		mruPlayerTexture = ret;
		return ret;
	}

	private ForeignTextureEntry loadForeign(EaglercraftUUID uuid, String url, SkinModel model) {
		if (mruForeignKey != null && uuid.equals(mruForeignKey)) {
			return mruForeignTexture;
		}
		ForeignTextureEntry ret = foreignTextures.get(uuid);
		if (ret == null) {
			foreignTextures.put(uuid, ret = new ForeignTextureEntry(uuid, url, model));
		}
		ret.lastHit = EagRuntime.steadyTimeMillis();
		mruForeignKey = uuid;
		mruForeignTexture = ret;
		return ret;
	}

	public SkinData getPlayerSkin(GameProfile profile) {
		if (self.equals(profile.getId())) {
			return SkinData.defaultSkinSelf;
		}
		TexturesProperty prop = profile.getTextures();
		if (prop.eaglerPlayer != (byte) 0) {
			return getPlayerSkinImpl(profile, prop);
		} else {
			SkinData ret = getForeignSkinImpl(profile, prop);
			if (ret != null) {
				return ret;
			} else if (StateFlags.eaglerPlayerFlag) {
				return SkinData.getDefaultSkin(profile.getId());
			} else {
				return getPlayerSkinImpl(profile, prop);
			}
		}
	}

	private SkinData getPlayerSkinImpl(GameProfile profile, TexturesProperty prop) {
		EaglercraftUUID uuid = profile.getId();
		if (checkEvicted(uuid)) {
			return SkinData.getDefaultSkin(uuid);
		}
		PlayerTextureEntry etr = loadPlayer(uuid);
		int state = etr.state;
		if ((state & STATE_S_LOADED) != 0) {
			return etr;
		} else if ((state & STATE_S_COMPLETE) != 0) {
			etr.loadSkin(textureManager);
			return etr;
		} else {
			if ((state & STATE_S_PENDING) == 0) {
				etr.state = (state | STATE_S_PENDING);
				pendingPlayerToLookup.add(etr);
			}
			return SkinData.getDefaultSkin(profile.getId());
		}
	}

	private SkinData getForeignSkinImpl(GameProfile profile, TexturesProperty prop) {
		if(StateFlags.disableSkinURLLookup) {
			return null;
		}
		String url = prop.skin;
		if (url != null) {
			ForeignTextureEntry etr = loadForeign(prop.loadSkinTextureUUID(), url, prop.model);
			int state = etr.state;
			if ((state & STATE_S_LOADED) != 0) {
				return etr.withModel(prop.model);
			} else if ((state & STATE_S_COMPLETE) != 0) {
				etr.loadSkin(textureManager);
				return etr.withModel(prop.model);
			} else {
				if ((state & STATE_S_PENDING) == 0) {
					etr.state = (state | STATE_S_PENDING);
					pendingForeignToLookup.add(etr);
				}
				return SkinData.getDefaultSkin(prop.model);
			}
		}
		return null;
	}

	public ResourceLocation getPlayerCape(GameProfile profile) {
		EaglercraftUUID uuid = profile.getId();
		if (uuid.equals(self)) {
			return EaglerProfile.getActiveCapeResourceLocation();
		}
		if (!StateFlags.eaglerPlayerFlag || profile.getTextures().eaglerPlayer != (byte) 0) {
			if (checkEvicted(uuid)) {
				return null;
			}
			PlayerTextureEntry etr = loadPlayer(uuid);
			int state = etr.state;
			if ((state & STATE_C_LOADED) != 0) {
				return etr.capeLocation;
			} else if ((state & STATE_C_COMPLETE) != 0) {
				etr.loadCape(textureManager);
				return etr.capeLocation;
			} else {
				if ((state & STATE_C_PENDING) == 0) {
					etr.state = (state | STATE_C_PENDING);
					pendingPlayerToLookup.add(etr);
				}
				return null;
			}
		}
		// Loading capes by URL is not currently supported
		// (Should only affect NPCs on a working setup)
		return null;
	}

	public void dropPlayer(EaglercraftUUID playerUUID, boolean skin, boolean cape) {
		if (skin || cape) {
			PlayerTextureEntry etr = playerTextures.get(playerUUID);
			if (etr != null) {
				etr.drop(textureManager, skin, cape);
				_dropPlayer(playerUUID, skin, cape);
			}
		}
	}

	protected abstract void _dropPlayer(EaglercraftUUID playerUUID, boolean skin, boolean cape);

	public void evictPlayer(EaglercraftUUID playerUUID) {
		PlayerTextureEntry etr = playerTextures.remove(playerUUID);
		if (etr != null) {
			etr.release(textureManager);
		}
		if (mruPlayerKey != null && playerUUID.equals(mruPlayerKey)) {
			mruPlayerKey = null;
			mruPlayerTexture = null;
		}
		evictedPlayers.put(playerUUID, EagRuntime.steadyTimeMillis() + 2000l);
	}

	private boolean checkEvicted(EaglercraftUUID playerUUID) {
		if (!evictedPlayers.isEmpty()) {
			long l = evictedPlayers.getOrDefault(playerUUID, -1l);
			return l != -1l && EagRuntime.steadyTimeMillis() < l;
		}
		return false;
	}

	public void runTick() {
		if (!pendingPlayerToLookup.isEmpty()) {
			for (PlayerTextureEntry etr : pendingPlayerToLookup) {
				int state = etr.state;
				if (protocolVers >= 5 && (state & STATE_SC_PENDING) == STATE_SC_PENDING) {
					sendTexturesRequest(etr);
				} else {
					if ((state & STATE_S_PENDING) != 0) {
						sendSkinRequest(etr);
					}
					if ((state & STATE_C_PENDING) != 0) {
						sendCapeRequest(etr);
					}
				}
			}
			pendingPlayerToLookup.clear();
		}
		if (!pendingForeignToLookup.isEmpty()) {
			for (ForeignTextureEntry etr : pendingForeignToLookup) {
				if ((etr.state & STATE_S_PENDING) != 0) {
					sendSkinRequest(etr);
				}
			}
			pendingForeignToLookup.clear();
		}
		if (--nextEvictFlush <= 0) {
			nextEvictFlush = 20;
			long now = EagRuntime.steadyTimeMillis();
			evictedPlayers.removeAll((obj, millis) -> {
				return now > millis;
			});
		}
		if (--nextFlush <= 0) {
			nextFlush = 200;
			long now = EagRuntime.steadyTimeMillis();
			lookupFlush(now);
			if ((++flush & 3) == 0) {
				if (!playerTextures.isEmpty()) {
					Iterator<PlayerTextureEntry> itr1 = playerTextures.values().iterator();
					while (itr1.hasNext()) {
						PlayerTextureEntry etr = itr1.next();
						if (now - etr.lastHit > (900l * 1000l)) {
							itr1.remove();
							etr.release(textureManager);
						}
					}
					mruPlayerKey = null;
					mruPlayerTexture = null;
				}
				if (!foreignTextures.isEmpty()) {
					Iterator<ForeignTextureEntry> itr2 = foreignTextures.values().iterator();
					while (itr2.hasNext()) {
						ForeignTextureEntry etr = itr2.next();
						if (now - etr.lastHit > (900l * 1000l)) {
							itr2.remove();
							etr.release(textureManager);
						}
					}
					mruForeignKey = null;
					mruForeignTexture = null;
				}
			}
		}
	}

	public void destroy() {
		if (!playerTextures.isEmpty()) {
			for (PlayerTextureEntry etr : playerTextures.values()) {
				etr.release(textureManager);
			}
			playerTextures.clear();
			mruPlayerKey = null;
			mruPlayerTexture = null;
		}
		if (!foreignTextures.isEmpty()) {
			for (ForeignTextureEntry etr : foreignTextures.values()) {
				etr.release(textureManager);
			}
			foreignTextures.clear();
			mruForeignKey = null;
			mruForeignTexture = null;
		}
		pendingPlayerToLookup.clear();
		pendingForeignToLookup.clear();
		evictedPlayers.clear();
	}

	protected abstract void lookupFlush(long now);

	protected abstract void sendTexturesRequest(PlayerTextureEntry etr);

	protected abstract void sendSkinRequest(PlayerTextureEntry etr);

	protected abstract void sendSkinRequest(ForeignTextureEntry etr);

	protected abstract void sendCapeRequest(PlayerTextureEntry etr);

	public abstract void handlePacket(SPacketOtherSkinPresetEAG packet);

	public abstract void handlePacket(SPacketOtherSkinPresetV5EAG packet);

	public abstract void handlePacket(SPacketOtherSkinCustomV3EAG packet);

	public abstract void handlePacket(SPacketOtherSkinCustomV4EAG packet);

	public abstract void handlePacket(SPacketOtherSkinCustomV5EAG packet);

	public abstract void handlePacket(SPacketOtherCapePresetEAG packet);

	public abstract void handlePacket(SPacketOtherCapePresetV5EAG packet);

	public abstract void handlePacket(SPacketOtherCapeCustomEAG packet);

	public abstract void handlePacket(SPacketOtherCapeCustomV5EAG packet);

	public abstract void handlePacket(SPacketOtherTexturesV5EAG packet);

}
