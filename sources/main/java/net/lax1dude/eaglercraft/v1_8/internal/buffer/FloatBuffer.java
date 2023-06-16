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
public interface FloatBuffer extends Buffer {

	FloatBuffer slice();

	FloatBuffer duplicate();

	FloatBuffer asReadOnlyBuffer();

	float get();

	FloatBuffer put(float b);

	float get(int index);

	FloatBuffer put(int index, float b);

	float getElement(int index);

	void putElement(int index, float value);

	FloatBuffer get(float[] dst, int offset, int length);

	FloatBuffer get(float[] dst);

	FloatBuffer put(FloatBuffer src);

	FloatBuffer put(float[] src, int offset, int length);

	FloatBuffer put(float[] src);

	int getArrayOffset();

	FloatBuffer compact();

	boolean isDirect();

	FloatBuffer mark();

	FloatBuffer reset();

	FloatBuffer clear();

	FloatBuffer flip();

	FloatBuffer rewind();

	FloatBuffer limit(int newLimit);

	FloatBuffer position(int newPosition);
	
}

