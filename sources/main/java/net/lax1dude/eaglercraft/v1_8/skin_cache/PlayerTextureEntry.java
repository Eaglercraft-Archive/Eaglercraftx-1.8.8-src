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

import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.profile.DefaultCapes;
import net.lax1dude.eaglercraft.v1_8.profile.DefaultSkins;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerSkinTexture;
import net.lax1dude.eaglercraft.v1_8.profile.SkinConverter;
import net.lax1dude.eaglercraft.v1_8.profile.SkinModel;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SkinPacketVersionCache;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

class PlayerTextureEntry extends SkinData {

	protected final EaglercraftUUID uuid;

	protected int state;

	protected long lastHit;

	protected ResourceLocation skinLocation;
	protected EaglerSkinTexture skinTexture;
	protected SkinModel skinModel;

	protected ResourceLocation capeLocation;
	protected EaglerSkinTexture capeTexture;

	public PlayerTextureEntry(EaglercraftUUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public ResourceLocation getLocation() {
		return skinLocation;
	}

	@Override
	public SkinModel getModel() {
		return skinModel;
	}

	protected void handleSkinResultPreset(int skinID) {
		DefaultSkins skin = DefaultSkins.getSkinFromId(skinID);
		skinLocation = skin.location;
		skinModel = skin.model;
		state |= ServerTextureCache.STATE_S_LOADED;
	}

	protected void handleSkinResultCustomV4(byte[] customSkin, int modelID) {
		handleSkinResultCustomV3(SkinPacketVersionCache.convertToV3Raw(customSkin), modelID);
	}

	protected void handleSkinResultCustomV3(byte[] customSkin, int modelID) {
		skinModel = SkinModel.getSanitizedModelFromId(modelID);
		skinTexture = new EaglerSkinTexture(customSkin, skinModel.width, skinModel.height);
		state |= ServerTextureCache.STATE_S_COMPLETE;
	}

	protected void loadSkin(TextureManager textureManager) {
		skinLocation = new ResourceLocation("eagler:multiplayer/tex_" + ServerTextureCache.texId++);
		textureManager.loadTexture(skinLocation, skinTexture);
		state |= ServerTextureCache.STATE_S_LOADED;
	}

	protected void handleCapeResultPreset(int presetCape) {
		DefaultCapes cape = DefaultCapes.getCapeFromId(presetCape);
		capeLocation = cape.location;
		state |= ServerTextureCache.STATE_C_LOADED;
	}

	protected void handleCapeResultCustom(byte[] customCape) {
		byte[] pixels32x32 = new byte[4096];
		SkinConverter.convertCape23x17RGBto32x32RGBA(customCape, pixels32x32);
		capeTexture = new EaglerSkinTexture(pixels32x32, 32, 32);
		state |= ServerTextureCache.STATE_C_COMPLETE;
	}

	protected void loadCape(TextureManager textureManager) {
		capeLocation = new ResourceLocation("eagler:multiplayer/tex_" + ServerTextureCache.texId++);
		textureManager.loadTexture(capeLocation, capeTexture);
		state |= ServerTextureCache.STATE_C_LOADED;
	}

	protected void release(TextureManager textureManager) {
		if(skinTexture != null && (state & ServerTextureCache.STATE_S_LOADED) != 0) {
			textureManager.deleteTexture(skinLocation);
		}
		if(capeTexture != null && (state & ServerTextureCache.STATE_C_LOADED) != 0) {
			textureManager.deleteTexture(capeLocation);
		}
	}

	protected void drop(TextureManager textureManager, boolean skin, boolean cape) {
		if(skin) {
			if(skinTexture != null && (state & ServerTextureCache.STATE_S_LOADED) != 0) {
				textureManager.deleteTexture(skinLocation);
			}
			skinTexture = null;
			skinLocation = null;
			state &= ~(ServerTextureCache.STATE_S_PENDING | ServerTextureCache.STATE_S_LOADED
					| ServerTextureCache.STATE_S_COMPLETE);
		}
		if(cape) {
			if(capeTexture != null && (state & ServerTextureCache.STATE_C_LOADED) != 0) {
				textureManager.deleteTexture(capeLocation);
			}
			capeTexture = null;
			capeLocation = null;
			state &= ~(ServerTextureCache.STATE_C_PENDING | ServerTextureCache.STATE_C_LOADED
					| ServerTextureCache.STATE_C_COMPLETE);
		}
	}

}