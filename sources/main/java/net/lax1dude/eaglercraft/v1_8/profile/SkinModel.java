package net.lax1dude.eaglercraft.v1_8.profile;

import java.util.HashMap;
import java.util.Map;

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
public enum SkinModel {
	STEVE(0, 64, 64, "default", false), ALEX(1, 64, 64, "slim", false), ZOMBIE(2, 64, 64, "zombie", true);
	
	public final int id;	
	public final int width;
	public final int height;
	public final String profileSkinType;
	public final boolean sanitize;
	
	public static final SkinModel[] skinModels = new SkinModel[3];
	private static final Map<String, SkinModel> skinModelsByName = new HashMap();
	
	private SkinModel(int id, int w, int h, String profileSkinType, boolean sanitize) {
		this.id = id;
		this.width = w;
		this.height = h;
		this.profileSkinType = profileSkinType;
		this.sanitize = sanitize;
	}

	public static SkinModel getModelFromId(String str) {
		SkinModel mdl = skinModelsByName.get(str.toLowerCase());
		if(mdl == null) {
			return skinModels[0];
		}
		return mdl;
	}

	public static SkinModel getModelFromId(int id) {
		SkinModel s = null;
		if(id >= 0 && id < skinModels.length) {
			s = skinModels[id];
		}
		if(s != null) {
			return s;
		}else {
			return STEVE;
		}
	}
	
	static {
		SkinModel[] arr = values();
		for(int i = 0; i < arr.length; ++i) {
			skinModels[arr[i].id] = arr[i];
			skinModelsByName.put(arr[i].profileSkinType, arr[i]);
		}
	}

}