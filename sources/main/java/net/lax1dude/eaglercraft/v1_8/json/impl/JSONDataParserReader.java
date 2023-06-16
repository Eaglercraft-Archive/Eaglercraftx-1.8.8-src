package net.lax1dude.eaglercraft.v1_8.json.impl;

import java.io.IOException;
import java.io.Reader;

import org.json.JSONException;

import net.lax1dude.eaglercraft.v1_8.json.JSONDataParserImpl;
import net.lax1dude.eaglercraft.v1_8.json.JSONTypeProvider;

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
public class JSONDataParserReader implements JSONDataParserImpl {
	
	public boolean accepts(Object type) {
		return type instanceof Reader;
	}

	@Override
	public Object parse(Object data) {
		Reader r = (Reader)data;
		StringBuilder builder = new StringBuilder();
		char[] copyBuffer = new char[2048];
		int i;
		try {
			try {
				while((i = r.read(copyBuffer)) != -1) {
					builder.append(copyBuffer, 0, i);
				}
			}finally {
				r.close();
			}
		}catch(IOException ex) {
			throw new JSONException("Could not deserialize from " + data.getClass().getSimpleName());
		}
		return JSONTypeProvider.parse(builder.toString());
	}

}
