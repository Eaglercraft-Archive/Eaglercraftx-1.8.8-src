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
public interface ShortBuffer extends Buffer {

	ShortBuffer slice();

	ShortBuffer duplicate();

	ShortBuffer asReadOnlyBuffer();

	short get();

	ShortBuffer put(short b);

	short get(int index);

	ShortBuffer put(int index, short b);

	short getElement(int index);

	void putElement(int index, short value);

	ShortBuffer get(short[] dst, int offset, int length);

	ShortBuffer get(short[] dst);

	ShortBuffer put(ShortBuffer src);

	ShortBuffer put(short[] src, int offset, int length);

	ShortBuffer put(short[] src);

	int getArrayOffset();

	ShortBuffer compact();

	boolean isDirect();

	ShortBuffer mark();

	ShortBuffer reset();

	ShortBuffer clear();

	ShortBuffer flip();

	ShortBuffer rewind();

	ShortBuffer limit(int newLimit);

	ShortBuffer position(int newPosition);
	
}
