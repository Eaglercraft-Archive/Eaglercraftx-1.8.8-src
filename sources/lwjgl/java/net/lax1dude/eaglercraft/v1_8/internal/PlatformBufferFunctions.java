package net.lax1dude.eaglercraft.v1_8.internal;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.IntBuffer;

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
public class PlatformBufferFunctions {

	public static void put(ByteBuffer newBuffer, ByteBuffer flip) {
		int len = flip.remaining();
		for(int i = 0; i < len; ++i) {
			newBuffer.put(flip.get());
		}
	}

	public static void put(IntBuffer intBuffer, int index, int[] data) {
		int p = intBuffer.position();
		intBuffer.position(index);
		intBuffer.put(data);
		intBuffer.position(p);
	}
	
}
