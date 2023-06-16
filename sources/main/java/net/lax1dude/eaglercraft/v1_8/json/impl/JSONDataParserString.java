package net.lax1dude.eaglercraft.v1_8.json.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.lax1dude.eaglercraft.v1_8.json.JSONDataParserImpl;

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
public class JSONDataParserString implements JSONDataParserImpl {

	public boolean accepts(Object type) {
		return type instanceof String;
	}

	@Override
	public Object parse(Object data) {
		String s = ((String)data).trim();
		try {
			if(s.indexOf('{') == 0 && s.lastIndexOf('}') == s.length() - 1) {
				return new JSONObject(s);
			}else if(s.indexOf('[') == 0 && s.lastIndexOf(']') == s.length() - 1) {
				return new JSONArray(s);
			}else if ((s.indexOf('\"') == 0 && s.lastIndexOf('\"') == s.length() - 1)
					|| (s.indexOf('\'') == 0 && s.lastIndexOf('\'') == s.length() - 1)) {
				return (new JSONObject("{\"E\":" + s + "}")).getString("E");
			}else {
				return (String)data;
			}
		}catch(JSONException ex) {
			return (String)data;
		}
	}

}
