package net.lax1dude.eaglercraft.v1_8.json.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;

import net.lax1dude.eaglercraft.v1_8.IOUtils;
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
public class JSONDataParserStream implements JSONDataParserImpl {

	public boolean accepts(Object type) {
		return type instanceof InputStream;
	}

	@Override
	public Object parse(Object data) {
		try {
			InputStream s = (InputStream)data;
			try {
				return JSONTypeProvider.parse(IOUtils.inputStreamToString(s, StandardCharsets.UTF_8));
			}finally {
				s.close();
			}
		} catch (IOException e) {
			throw new JSONException("Could not deserialize from " + data.getClass().getSimpleName());
		}
	}

}
