package net.lax1dude.eaglercraft.v1_8.json;

import org.json.JSONException;

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
public interface JSONTypeSerializer<O, J> {
	
	default J serializeToJson(O object) throws JSONException {
		try {
			return serialize(object);
		}catch(JSONException obj) {
			throw obj;
		}catch(Throwable t) {
			throw new JSONException("Exception serializing JSON object", t);
		}
	}
	
	J serialize(O object) throws JSONException;
	
}
