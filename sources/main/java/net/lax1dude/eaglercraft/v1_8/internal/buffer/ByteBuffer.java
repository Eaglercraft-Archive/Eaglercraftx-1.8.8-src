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
public interface ByteBuffer extends Buffer {

	ByteBuffer slice();

	ByteBuffer duplicate();

	ByteBuffer asReadOnlyBuffer();

	byte get();

	ByteBuffer put(byte b);

	byte get(int index);

	ByteBuffer put(int index, byte b);

	ByteBuffer get(byte[] dst, int offset, int length);

	ByteBuffer get(byte[] dst);

	ByteBuffer put(ByteBuffer src);

	ByteBuffer put(byte[] src, int offset, int length);

	ByteBuffer put(byte[] src);

	int arrayOffset();

	ByteBuffer compact();

	char getChar();

	ByteBuffer putChar(char value);

	char getChar(int index);

	ByteBuffer putChar(int index, char value);

	public abstract short getShort();

	ByteBuffer putShort(short value);

	short getShort(int index);

	ByteBuffer putShort(int index, short value);

	ShortBuffer asShortBuffer();

	int getInt();

	ByteBuffer putInt(int value);

	int getInt(int index);

	ByteBuffer putInt(int index, int value);

	IntBuffer asIntBuffer();

	long getLong();

	ByteBuffer putLong(long value);

	long getLong(int index);

	ByteBuffer putLong(int index, long value);

	float getFloat();

	ByteBuffer putFloat(float value);

	float getFloat(int index);

	ByteBuffer putFloat(int index, float value);

	FloatBuffer asFloatBuffer();

	ByteBuffer mark();

	ByteBuffer reset();

	ByteBuffer clear();

	ByteBuffer flip();

	ByteBuffer rewind();

	ByteBuffer limit(int newLimit);

	ByteBuffer position(int newPosition);

}
