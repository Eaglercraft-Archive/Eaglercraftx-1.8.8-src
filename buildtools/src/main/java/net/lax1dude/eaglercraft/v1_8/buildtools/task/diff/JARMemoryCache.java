package net.lax1dude.eaglercraft.v1_8.buildtools.task.diff;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

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
public class JARMemoryCache {
	
	public static Map<String,byte[]> loadJAR(InputStream is) throws IOException {
		Map<String,byte[]> ret = new HashMap();
		ZipInputStream isz = new ZipInputStream(is);
		ZipEntry et;
		while((et = isz.getNextEntry()) != null) {
			if(!et.isDirectory()) {
				String n = et.getName();
				if(n.startsWith("/")) {
					n = n.substring(1);
				}
				if(!n.startsWith("META-INF")) {
					byte[] data = IOUtils.toByteArray(isz);
					ret.put(n, data);
				}
			}
		}
		return ret;
	}

}
