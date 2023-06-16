package net.lax1dude.eaglercraft.v1_8.mojang.authlib;

import java.util.Collection;

import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.ArrayUtils;
import net.lax1dude.eaglercraft.v1_8.Base64;

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
				JSONObject capeObj = json.optJSONObject("SKIN");
				if(capeObj != null) {
					cape = capeObj.optString("url");
				}
			}
		}catch(Throwable t) {
		}
		return new TexturesProperty(skin, model, cape, isEagler);
	}

}
