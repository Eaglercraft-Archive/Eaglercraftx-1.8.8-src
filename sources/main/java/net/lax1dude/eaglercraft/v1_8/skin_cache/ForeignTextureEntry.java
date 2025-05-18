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
import net.lax1dude.eaglercraft.v1_8.profile.DefaultSkins;
import net.lax1dude.eaglercraft.v1_8.profile.EaglerSkinTexture;
import net.lax1dude.eaglercraft.v1_8.profile.SkinModel;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.util.SkinPacketVersionCache;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

class ForeignTextureEntry extends SkinData {

	protected final EaglercraftUUID uuid;
	protected final String url;

	protected int state;

	protected long lastHit;

	protected ResourceLocation skinLocation;
	protected EaglerSkinTexture skinTexture;
	protected SkinModel skinModel;

	protected SkinData inverseModel;

	public ForeignTextureEntry(EaglercraftUUID uuid, String url, SkinModel skinModel) {
		this.uuid = uuid;
		this.url = url;
		this.skinModel = skinModel;
	}

	@Override
	public ResourceLocation getLocation() {
		return skinLocation;
	}

	@Override
	public SkinModel getModel() {
		return skinModel;
	}

	protected SkinData withModel(SkinModel model) {
		if(model != null && skinModel != model) {
			if(inverseModel == null) {
				return inverseModel = new SkinData() {
					@Override
					public ResourceLocation getLocation() {
						return skinLocation;
					}
					@Override
					public SkinModel getModel() {
						return model;
					}
				};
			}
			return inverseModel;
		}else {
			return this;
		}
	}

	protected void handleSkinResultPreset(int skinID) {
		DefaultSkins skin = DefaultSkins.getSkinFromId(skinID);
		skinLocation = skin.location;
		skinModel = skin.model;
		state |= ServerTextureCacheOld.STATE_S_LOADED;
	}

	protected void handleSkinResultCustomV4(byte[] customSkin, int modelID) {
		handleSkinResultCustomV3(SkinPacketVersionCache.convertToV3Raw(customSkin), modelID);
	}

	protected void handleSkinResultCustomV3(byte[] customSkin, int modelID) {
		if(modelID != 0xFF) {
			skinModel = SkinModel.getSanitizedModelFromId(modelID);
		}else if(skinModel == null) {
			skinModel = SkinModel.STEVE;
		}
		skinTexture = new EaglerSkinTexture(customSkin, skinModel.width, skinModel.height);
		state |= ServerTextureCacheOld.STATE_S_COMPLETE;
	}

	protected void loadSkin(TextureManager textureManager) {
		skinLocation = new ResourceLocation("eagler:multiplayer/tex_" + ServerTextureCacheOld.texId++);
		textureManager.loadTexture(skinLocation, skinTexture);
		state |= ServerTextureCacheOld.STATE_S_LOADED;
	}

	protected void release(TextureManager textureManager) {
		if(skinTexture != null && (state & ServerTextureCacheOld.STATE_S_LOADED) != 0) {
			textureManager.deleteTexture(skinLocation);
		}
	}

}