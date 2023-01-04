package net.lax1dude.eaglercraft.v1_8.json.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.json.JSONTypeDeserializer;
import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;
import net.minecraft.client.audio.SoundHandler.SoundMap;
import net.minecraft.client.audio.SoundList;

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
public class SoundMapDeserializer implements JSONTypeDeserializer<JSONObject, SoundMap> {

	@Override
	public SoundMap deserialize(JSONObject json) throws JSONException {
		Map<String, SoundList> soundsMap = new HashMap();
		for(String str : json.keySet()) {
			soundsMap.put(str, JSONTypeProvider.deserialize(json.getJSONObject(str), SoundList.class));
		}
		return new SoundMap(soundsMap);
	}

}
