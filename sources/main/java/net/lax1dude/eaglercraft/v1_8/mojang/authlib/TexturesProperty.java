package net.lax1dude.eaglercraft.v1_8.mojang.authlib;

import java.util.Collection;

import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.ArrayUtils;
import net.lax1dude.eaglercraft.v1_8.Base64;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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
public class TexturesProperty {

	public final String skin;
	public final String model;
	public final String cape;
	public final boolean eaglerPlayer;

	public static final TexturesProperty defaultNull = new TexturesProperty(null, "default", null, false);

	private TexturesProperty(String skin, String model, String cape, boolean eaglerPlayer) {
		this.skin = skin;
		this.model = model;
		this.cape = cape;
		this.eaglerPlayer = eaglerPlayer;
	}

	public static TexturesProperty parseProfile(GameProfile profile) {
		Collection<Property> etr = profile.getProperties().get("textures");
		if(!etr.isEmpty()) {
			Property prop = etr.iterator().next();
			String str;
			try {
				str = ArrayUtils.asciiString(Base64.decodeBase64(prop.getValue()));
			}catch(Throwable t) {
				return defaultNull;
			}
			boolean isEagler = false;
			etr = profile.getProperties().get("isEaglerPlayer");
			if(!etr.isEmpty()) {
				prop = etr.iterator().next();
				isEagler = prop.getValue().equalsIgnoreCase("true");
			}
			return parseTextures(str, isEagler);
		}else {
			return defaultNull;
		}
	}

	public static TexturesProperty parseTextures(String string, boolean isEagler) {
		String skin = null;
		String model = "default";
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
						model = meta.optString("model", model);
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
