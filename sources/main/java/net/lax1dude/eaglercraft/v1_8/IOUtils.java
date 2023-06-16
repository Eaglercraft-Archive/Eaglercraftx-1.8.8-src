package net.lax1dude.eaglercraft.v1_8;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class IOUtils {

	public static List<String> readLines(InputStream parInputStream, Charset charset) {
		if(parInputStream instanceof EaglerInputStream) {
			return Arrays.asList(
					new String(((EaglerInputStream) parInputStream).getAsArray(), charset).split("(\\r\\n|\\n|\\r)"));
		}else {
			List<String> ret = new ArrayList();
			try(InputStream is = parInputStream) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(is, charset));
				String s;
				while((s = rd.readLine()) != null) {
					ret.add(s);
				}
			}catch(IOException ex) {
				return null;
			}
			return ret;
		}
	}

	public static void closeQuietly(Closeable reResourcePack) {
		try {
			reResourcePack.close();
		}catch(Throwable t) {
		}
	}
	
	public static String inputStreamToString(InputStream is, Charset c) throws IOException {
		if(is instanceof EaglerInputStream) {
			return new String(((EaglerInputStream)is).getAsArray(), c);
		}else {
			try {
				StringBuilder b = new StringBuilder();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is, c));
				String s;
				while((s = rd.readLine()) != null) {
					b.append(s).append('\n');
				}
				return b.toString();
			}finally {
				is.close();
			}
		}
	}

}
