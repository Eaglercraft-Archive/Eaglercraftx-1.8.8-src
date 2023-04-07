package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Copyright (c) 2023 LAX1DUDE. All Rights Reserved.
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
public class ShaderPackInfo {

	public final String name;
	public final String desc;
	public final String vers;
	public final String author;
	public final int apiVers;
	public final Set<String> supportedFeatures;

	public final boolean WAVING_BLOCKS;
	public final boolean DYNAMIC_LIGHTS;
	public final boolean GLOBAL_AMBIENT_OCCLUSION;
	public final boolean SHADOWS_SUN;
	public final boolean SHADOWS_COLORED;
	public final boolean SHADOWS_SMOOTHED;
	public final boolean REFLECTIONS_PARABOLOID;
	public final boolean REALISTIC_WATER;
	public final boolean LIGHT_SHAFTS;
	public final boolean SCREEN_SPACE_REFLECTIONS;
	public final boolean POST_LENS_DISTORION;
	public final boolean POST_LENS_FLARES;
	public final boolean POST_BLOOM;
	public final boolean POST_FXAA;

	public ShaderPackInfo(JSONObject json) {
		name = json.optString("name", "Untitled");
		desc = json.optString("desc", "No Description");
		vers = json.optString("vers", "Unknown");
		author = json.optString("author", "Unknown");
		apiVers = json.optInt("api_vers", -1);
		supportedFeatures = new HashSet();
		JSONArray features = json.getJSONArray("features");
		if(features.length() == 0) {
			throw new JSONException("No supported features list has been defined for this shader pack!");
		}
		for(int i = 0, l = features.length(); i < l; ++i) {
			supportedFeatures.add(features.getString(i));
		}
		WAVING_BLOCKS = supportedFeatures.contains("WAVING_BLOCKS");
		DYNAMIC_LIGHTS = supportedFeatures.contains("DYNAMIC_LIGHTS");
		GLOBAL_AMBIENT_OCCLUSION = supportedFeatures.contains("GLOBAL_AMBIENT_OCCLUSION");
		SHADOWS_SUN = supportedFeatures.contains("SHADOWS_SUN");
		SHADOWS_COLORED = supportedFeatures.contains("SHADOWS_COLORED");
		SHADOWS_SMOOTHED = supportedFeatures.contains("SHADOWS_SMOOTHED");
		REFLECTIONS_PARABOLOID = supportedFeatures.contains("REFLECTIONS_PARABOLOID");
		REALISTIC_WATER = supportedFeatures.contains("REALISTIC_WATER");
		LIGHT_SHAFTS = supportedFeatures.contains("LIGHT_SHAFTS");
		SCREEN_SPACE_REFLECTIONS = supportedFeatures.contains("SCREEN_SPACE_REFLECTIONS");
		POST_LENS_DISTORION = supportedFeatures.contains("POST_LENS_DISTORION");
		POST_LENS_FLARES = supportedFeatures.contains("POST_LENS_FLARES");
		POST_BLOOM = supportedFeatures.contains("POST_BLOOM");
		POST_FXAA = supportedFeatures.contains("POST_FXAA");
	}

}
