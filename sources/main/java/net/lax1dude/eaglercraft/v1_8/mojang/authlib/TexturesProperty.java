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

package net.lax1dude.eaglercraft.v1_8.mojang.authlib;

import java.util.Collection;

import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.ArrayUtils;
import net.lax1dude.eaglercraft.v1_8.Base64;
import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
import net.lax1dude.eaglercraft.v1_8.profile.SkinModel;
import net.lax1dude.eaglercraft.v1_8.profile.SkinPackets;

public class TexturesProperty {

	public final String skin;
	public final SkinModel model;
	public final String cape;
	public final byte eaglerPlayer;

	private EaglercraftUUID skinTextureUUID;

	public static final TexturesProperty[] defaultNull = new TexturesProperty[] {
			new TexturesProperty(null, SkinModel.STEVE, null, (byte) 0),
			new TexturesProperty(null, SkinModel.STEVE, null, (byte) 1),
			new TexturesProperty(null, SkinModel.STEVE, null, (byte) 2)
	};

	private TexturesProperty(String skin, SkinModel model, String cape, byte eaglerPlayer) {
		this.skin = skin;
		this.model = model;
		this.cape = cape;
		this.eaglerPlayer = eaglerPlayer;
	}

	public EaglercraftUUID loadSkinTextureUUID() {
		if(skinTextureUUID == null && skin != null) {
			skinTextureUUID = SkinPackets.createEaglerURLSkinUUID(skin);
		}
		return skinTextureUUID;
	}

	public static TexturesProperty parseProfile(GameProfile profile) {
		String str = null;
		byte isEagler = 0;
		Property prop;
		Collection<Property> etr = profile.getProperties().get("textures");
		if(!etr.isEmpty()) {
			prop = etr.iterator().next();
			try {
				str = ArrayUtils.asciiString(Base64.decodeBase64(prop.getValue()));
			}catch(Throwable t) {
			}
		}
		etr = profile.getProperties().get("isEaglerPlayer");
		if(!etr.isEmpty()) {
			prop = etr.iterator().next();
			isEagler = prop.getValue().equalsIgnoreCase("true") ? (byte) 2 : (byte) 1;
		}
		if(str != null) {
			return parseTextures(str, isEagler);
		}else {
			return defaultNull[isEagler];
		}
	}

	public static TexturesProperty parseTextures(String string, byte isEagler) {
		String skin = null;
		SkinModel model = SkinModel.STEVE;
		String cape = null;
		try {
			JSONObject json = new JSONObject(string);
			json = json.optJSONObject("textures");
			if(json != null) {
				JSONObject skinObj = json.optJSONObject("SKIN");
				if(skinObj != null) {
					skin = skinObj.optString("url");
					JSONObject meta = skinObj.optJSONObject("metadata");
					if(meta != null) {
						String modelStr = meta.optString("model");
						if(modelStr != null && modelStr.equalsIgnoreCase("slim")) {
							model = SkinModel.STEVE;
						}
					}
				}
				JSONObject capeObj = json.optJSONObject("CAPE");
				if(capeObj != null) {
					cape = capeObj.optString("url");
				}
			}
		}catch(Throwable t) {
		}
		return new TexturesProperty(skin, model, cape, isEagler);
	}

}