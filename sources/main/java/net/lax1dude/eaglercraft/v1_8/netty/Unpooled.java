package net.lax1dude.eaglercraft.v1_8.netty;

import java.nio.ByteBuffer;

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
public class Unpooled {

	public static final ByteBuf EMPTY_BUFFER = ByteBuf.allocate(0, 0);

	public static ByteBuf buffer() {
		return ByteBuf.allocate(256, Integer.MAX_VALUE);
	}

	public static ByteBuf buffer(int length) {
		return ByteBuf.allocate(length, Integer.MAX_VALUE);
	}

	public static ByteBuf buffer(int length, int maxLength) {
		return ByteBuf.allocate(length, maxLength);
	}

	public static ByteBuf buffer(ByteBuffer data, int maxLength) {
		return ByteBuf.allocate(data, maxLength);
	}

	public static ByteBuf buffer(byte[] data, int maxLength) {
		return ByteBuf.allocate(ByteBuffer.wrap(data), maxLength);
	}
	
}
