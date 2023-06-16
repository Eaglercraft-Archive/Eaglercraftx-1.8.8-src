package net.lax1dude.eaglercraft.v1_8.internal.buffer;

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
public interface Buffer {

	int capacity();

	int position();

	Buffer position(int newPosition);

	int limit();

	Buffer limit(int newLimit);

	Buffer mark();

	Buffer reset();

	Buffer clear();

	Buffer flip();

	Buffer rewind();

	int remaining();

	boolean hasRemaining();

	boolean isReadOnly();

	boolean hasArray();

	Object array();

	int arrayOffset();

	boolean isDirect();

}
