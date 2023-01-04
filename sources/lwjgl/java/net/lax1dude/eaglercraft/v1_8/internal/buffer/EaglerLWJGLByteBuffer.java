package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.jemalloc.JEmalloc;

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
public class EaglerLWJGLByteBuffer implements ByteBuffer {

	final long address;
	final boolean original;

	private final int capacity;
	private int position;
	private int limit;
	private int mark;
	
	EaglerLWJGLByteBuffer(long address, int capacity, boolean original) {
		this(address, capacity, 0, capacity, -1, original);
	}

	EaglerLWJGLByteBuffer(long address, int capacity, int position, int limit, int mark, boolean original) {
		this.address = address;
		this.capacity = capacity;
		this.position = position;
		this.limit = limit;
		this.mark = mark;
		this.original = original;
	}

	@Override
	public int capacity() {
		return capacity;
	}

	@Override
	public int position() {
		return position;
	}

	@Override
	public int limit() {
		return limit;
	}

	@Override
	public int remaining() {
		return limit - position;
	}

	@Override
	public boolean hasRemaining() {
		return position < limit;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public boolean hasArray() {
		return false;
	}

	@Override
	public Object array() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDirect() {
		return true;
	}

	@Override
	public ByteBuffer slice() {
		return new EaglerLWJGLByteBuffer(address + position, limit - position, false);
	}

	@Override
	public ByteBuffer duplicate() {
		return new EaglerLWJGLByteBuffer(address, capacity, position, limit, mark, false);
	}

	@Override
	public ByteBuffer asReadOnlyBuffer() {
		return new EaglerLWJGLByteBuffer(address, capacity, position, limit, mark, false);
	}

	@Override
	public byte get() {
		if(position >= limit) throw new ArrayIndexOutOfBoundsException(position);
		return MemoryUtil.memGetByte(address + position++);
	}

	@Override
	public ByteBuffer put(byte b) {
		if(position >= limit) throw new ArrayIndexOutOfBoundsException(position);
		MemoryUtil.memPutByte(address + position++, b);
		return this;
	}

	@Override
	public byte get(int index) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		return MemoryUtil.memGetByte(address + index);
	}

	@Override
	public ByteBuffer put(int index, byte b) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		MemoryUtil.memPutByte(address + index, b);
		return this;
	}

	@Override
	public ByteBuffer get(byte[] dst, int offset, int length) {
		if(position + length > limit) throw new ArrayIndexOutOfBoundsException(position + length - 1);
		for(int i = 0; i < length; ++i) {
			dst[offset + i] = MemoryUtil.memGetByte(address + position + i);
		}
		position += length;
		return this;
	}

	@Override
	public ByteBuffer get(byte[] dst) {
		if(position + dst.length > limit) throw new ArrayIndexOutOfBoundsException(position + dst.length - 1);
		for(int i = 0; i < dst.length; ++i) {
			dst[position + i] = MemoryUtil.memGetByte(address + position + i);
		}
		position += dst.length;
		return this;
	}

	@Override
	public ByteBuffer put(ByteBuffer src) {
		if(src instanceof EaglerLWJGLByteBuffer) {
			EaglerLWJGLByteBuffer c = (EaglerLWJGLByteBuffer)src;
			int l = c.limit - c.position;
			if(position + l > limit) throw new ArrayIndexOutOfBoundsException(position + l - 1);
			MemoryUtil.memCopy(c.address + c.position, address + position, l);
			position += l;
			c.position += l;
		}else {
			int l = src.remaining();
			if(position + l > limit) throw new ArrayIndexOutOfBoundsException(position + l - 1);
			for(int i = 0; i < l; ++i) {
				MemoryUtil.memPutByte(address + position + l, src.get());
			}
			position += l;
		}
		return this;
	}

	@Override
	public ByteBuffer put(byte[] src, int offset, int length) {
		if(position + length > limit) throw new ArrayIndexOutOfBoundsException(position + length - 1);
		for(int i = 0; i < length; ++i) {
			MemoryUtil.memPutByte(address + position + i, src[offset + i]);
		}
		position += length;
		return this;
	}

	@Override
	public ByteBuffer put(byte[] src) {
		if(position + src.length > limit) throw new ArrayIndexOutOfBoundsException(position + src.length - 1);
		for(int i = 0; i < src.length; ++i) {
			MemoryUtil.memPutByte(address + position + i, src[i]);
		}
		position += src.length;
		return this;
	}

	@Override
	public int arrayOffset() {
		return position;
	}

	@Override
	public ByteBuffer compact() {
		if(limit > capacity) throw new ArrayIndexOutOfBoundsException(limit);
		if(position > limit) throw new ArrayIndexOutOfBoundsException(position);
		
		if(position == limit) {
			return new EaglerLWJGLByteBuffer(0l, 0, false);
		}
		
		int newLen = limit - position;
		long newAlloc = JEmalloc.nje_malloc(newLen);
		MemoryUtil.memCopy(address + position, newAlloc, newLen);
		
		return new EaglerLWJGLByteBuffer(newAlloc, newLen, true);
	}

	@Override
	public char getChar() {
		if(position + 2 > limit) throw new ArrayIndexOutOfBoundsException(position);
		char c = (char)MemoryUtil.memGetShort(address + position);
		position += 2;
		return c;
	}

	@Override
	public ByteBuffer putChar(char value) {
		if(position + 2 > limit) throw new ArrayIndexOutOfBoundsException(position);
		MemoryUtil.memPutShort(address + position, (short)value);
		position += 2;
		return this;
	}

	@Override
	public char getChar(int index) {
		if(index + 2 > limit) throw new ArrayIndexOutOfBoundsException(index);
		return (char)MemoryUtil.memGetShort(address + index);
	}

	@Override
	public ByteBuffer putChar(int index, char value) {
		if(index + 2 > limit) throw new ArrayIndexOutOfBoundsException(index);
		MemoryUtil.memPutShort(address + index, (short)value);
		return this;
	}

	@Override
	public short getShort() {
		if(position + 2 > limit) throw new ArrayIndexOutOfBoundsException(position);
		short s = MemoryUtil.memGetShort(address + position);
		position += 2;
		return s;
	}

	@Override
	public ByteBuffer putShort(short value) {
		if(position + 2 > limit) throw new ArrayIndexOutOfBoundsException(position);
		MemoryUtil.memPutShort(address + position, value);
		position += 2;
		return this;
	}

	@Override
	public short getShort(int index) {
		if(index + 2 > limit) throw new ArrayIndexOutOfBoundsException(index);
		return MemoryUtil.memGetShort(address + index);
	}

	@Override
	public ByteBuffer putShort(int index, short value) {
		if(index + 2 > limit) throw new ArrayIndexOutOfBoundsException(index);
		MemoryUtil.memPutShort(address + index, value);
		return this;
	}

	@Override
	public ShortBuffer asShortBuffer() {
		return new EaglerLWJGLShortBuffer(address, capacity >> 1, false);
	}

	@Override
	public int getInt() {
		if(position + 4 > limit) throw new ArrayIndexOutOfBoundsException(position);
		int i = MemoryUtil.memGetInt(address + position);
		position += 4;
		return i;
	}

	@Override
	public ByteBuffer putInt(int value) {
		if(position + 4 > limit) throw new ArrayIndexOutOfBoundsException(position);
		MemoryUtil.memPutInt(address + position, value);
		position += 4;
		return this;
	}

	@Override
	public int getInt(int index) {
		if(index + 4 > limit) throw new ArrayIndexOutOfBoundsException(index);
		return MemoryUtil.memGetInt(address + index);
	}

	@Override
	public ByteBuffer putInt(int index, int value) {
		if(index + 4 > limit) throw new ArrayIndexOutOfBoundsException(index);
		MemoryUtil.memPutInt(address + index, value);
		return this;
	}

	@Override
	public IntBuffer asIntBuffer() {
		return new EaglerLWJGLIntBuffer(address, capacity >> 2, false);
	}

	@Override
	public long getLong() {
		if(position + 8 > limit) throw new ArrayIndexOutOfBoundsException(position);
		long l = MemoryUtil.memGetLong(address + position);
		position += 8;
		return l;
	}

	@Override
	public ByteBuffer putLong(long value) {
		if(position + 8 > limit) throw new ArrayIndexOutOfBoundsException(position);
		MemoryUtil.memPutLong(address + position, value);
		position += 8;
		return this;
	}

	@Override
	public long getLong(int index) {
		if(index + 8 > limit) throw new ArrayIndexOutOfBoundsException(index);
		return MemoryUtil.memGetLong(address + index);
	}

	@Override
	public ByteBuffer putLong(int index, long value) {
		if(index + 8 > limit) throw new ArrayIndexOutOfBoundsException(index);
		MemoryUtil.memPutLong(address + index, value);
		return this;
	}

	@Override
	public float getFloat() {
		if(position + 4 > limit) throw new ArrayIndexOutOfBoundsException(position);
		float f = MemoryUtil.memGetFloat(address + position);
		position += 4;
		return f;
	}

	@Override
	public ByteBuffer putFloat(float value) {
		if(position + 4 > limit) throw new ArrayIndexOutOfBoundsException(position);
		MemoryUtil.memPutFloat(address + position, value);
		position += 4;
		return this;
	}

	@Override
	public float getFloat(int index) {
		if(index + 4 > limit) throw new ArrayIndexOutOfBoundsException(index);
		return MemoryUtil.memGetFloat(address + index);
	}

	@Override
	public ByteBuffer putFloat(int index, float value) {
		if(index + 4 > limit) throw new ArrayIndexOutOfBoundsException(index);
		MemoryUtil.memPutFloat(address + index, value);
		return this;
	}

	@Override
	public FloatBuffer asFloatBuffer() {
		return new EaglerLWJGLFloatBuffer(address, capacity >> 2, false);
	}

	@Override
	public ByteBuffer mark() {
		mark = position;
		return this;
	}

	@Override
	public ByteBuffer reset() {
		int m = mark;
		if(m < 0) throw new ArrayIndexOutOfBoundsException("Invalid mark: " + m);
		position = m;
		return this;
	}

	@Override
	public ByteBuffer clear() {
		position = 0;
		limit = capacity;
		mark = -1;
		return this;
	}

	@Override
	public ByteBuffer flip() {
		limit = position;
		position = 0;
		mark = -1;
		return this;
	}

	@Override
	public ByteBuffer rewind() {
		position = 0;
		mark = -1;
		return this;
	}

	@Override
	public ByteBuffer limit(int newLimit) {
		if(newLimit < 0 || newLimit > capacity) throw new ArrayIndexOutOfBoundsException(newLimit);
		limit = newLimit;
		return this;
	}

	@Override
	public ByteBuffer position(int newPosition) {
		if(newPosition < 0 || newPosition > limit) throw new ArrayIndexOutOfBoundsException(newPosition);
		position = newPosition;
		return this;
	}

}
