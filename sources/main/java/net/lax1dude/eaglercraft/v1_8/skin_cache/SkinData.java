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
import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;
import net.lax1dude.eaglercraft.v1_8.profile.SkinModel;
import net.minecraft.util.ResourceLocation;

public abstract class SkinData {

	public abstract ResourceLocation getLocation();

	public abstract SkinModel getModel();

	static SkinData getDefaultSkin(EaglercraftUUID uuid) {
		return (uuid != null && (uuid.hashCode() & 1) != 0) ? defaultSkinDataAlex : defaultSkinDataSteve;
	}

	static SkinData getDefaultSkin(SkinModel model) {
		return (model == SkinModel.ALEX) ? defaultSkinDataAlex : defaultSkinDataSteve;
	}

	static final SkinData defaultSkinDataSteve = new SkinData() {
		@Override
		public ResourceLocation getLocation() {
			return DefaultSkins.DEFAULT_STEVE.location;
		}
		@Override
		public SkinModel getModel() {
			return SkinModel.STEVE;
		}
	};

	static final SkinData defaultSkinDataAlex = new SkinData() {
		@Override
		public ResourceLocation getLocation() {
			return DefaultSkins.DEFAULT_ALEX.location;
		}
		@Override
		public SkinModel getModel() {
			return SkinModel.ALEX;
		}
	};

	static final SkinData defaultSkinSelf = new SkinData() {
		@Override
		public ResourceLocation getLocation() {
			return EaglerProfile.getActiveSkinResourceLocation();
		}
		@Override
		public SkinModel getModel() {
			return EaglerProfile.getActiveSkinModel();
		}
	};

}