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
public interface IntBuffer extends Buffer {

	IntBuffer slice();

	IntBuffer duplicate();

	IntBuffer asReadOnlyBuffer();

	int get();

	IntBuffer put(int b);

	int get(int index);

	IntBuffer put(int index, int b);

	int getElement(int index);

	void putElement(int index, int value);

	IntBuffer get(int[] dst, int offset, int length);

	IntBuffer get(int[] dst);

	IntBuffer put(IntBuffer src);

	IntBuffer put(int[] src, int offset, int length);

	IntBuffer put(int[] src);

	int getArrayOffset();

	IntBuffer compact();

	boolean isDirect();

	IntBuffer mark();

	IntBuffer reset();

	IntBuffer clear();

	IntBuffer flip();

	IntBuffer rewind();

	IntBuffer limit(int newLimit);

	IntBuffer position(int newPosition);
	
}

