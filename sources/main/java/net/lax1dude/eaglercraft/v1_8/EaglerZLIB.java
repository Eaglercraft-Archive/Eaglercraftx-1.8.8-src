package net.lax1dude.eaglercraft.v1_8;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;

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
public class EaglerZLIB {

	public static OutputStream newDeflaterOutputStream(OutputStream os) throws IOException {
		return PlatformRuntime.newDeflaterOutputStream(os);
	}
	
	public static OutputStream newGZIPOutputStream(OutputStream os) throws IOException {
		return PlatformRuntime.newGZIPOutputStream(os);
	}
	
	public static InputStream newInflaterInputStream(InputStream is) throws IOException {
		return PlatformRuntime.newInflaterInputStream(is);
	}
	
	public static InputStream newGZIPInputStream(InputStream is) throws IOException {
		return PlatformRuntime.newGZIPInputStream(is);
	}

}
