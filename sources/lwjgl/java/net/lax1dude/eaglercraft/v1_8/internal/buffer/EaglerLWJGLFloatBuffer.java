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
public class EaglerLWJGLFloatBuffer implements FloatBuffer {

	final long address;
	final boolean original;

	private final int capacity;
	private int position;
	private int limit;
	private int mark;
	
	private static final int SHIFT = 2;
	
	EaglerLWJGLFloatBuffer(long address, int capacity, boolean original) {
		this(address, capacity, 0, capacity, -1, original);
	}

	EaglerLWJGLFloatBuffer(long address, int capacity, int position, int limit, int mark, boolean original) {
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
	public int arrayOffset() {
		return position;
	}

	@Override
	public FloatBuffer slice() {
		return new EaglerLWJGLFloatBuffer(address + (position << SHIFT), limit - position, false);
	}

	@Override
	public FloatBuffer duplicate() {
		return new EaglerLWJGLFloatBuffer(address, capacity, position, limit, mark, false);
	}

	@Override
	public FloatBuffer asReadOnlyBuffer() {
		return new EaglerLWJGLFloatBuffer(address, capacity, position, limit, mark, false);
	}

	@Override
	public float get() {
		if(position >= limit) throw new ArrayIndexOutOfBoundsException(position);
		return MemoryUtil.memGetFloat(address + ((position++) << SHIFT));
	}

	@Override
	public FloatBuffer put(float b) {
		if(position >= limit) throw new ArrayIndexOutOfBoundsException(position);
		MemoryUtil.memPutFloat(address + ((position++) << SHIFT), b);
		return this;
	}

	@Override
	public float get(int index) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		return MemoryUtil.memGetFloat(address + (index << SHIFT));
	}

	@Override
	public FloatBuffer put(int index, float b) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		MemoryUtil.memPutFloat(address + (index << SHIFT), b);
		return this;
	}

	@Override
	public float getElement(int index) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		return MemoryUtil.memGetFloat(address + (index << SHIFT));
	}

	@Override
	public void putElement(int index, float value) {
		if(position >= limit) throw new ArrayIndexOutOfBoundsException(position);
		MemoryUtil.memPutFloat(address + ((position++) << SHIFT), value);
	}

	@Override
	public FloatBuffer get(float[] dst, int offset, int length) {
		if(position + length > limit) throw new ArrayIndexOutOfBoundsException(position + length - 1);
		for(int i = 0; i < length; ++i) {
			dst[offset + i] = MemoryUtil.memGetFloat(address + ((position + i) << SHIFT));
		}
		position += length;
		return this;
	}

	@Override
	public FloatBuffer get(float[] dst) {
		if(position + dst.length > limit) throw new ArrayIndexOutOfBoundsException(position + dst.length - 1);
		for(int i = 0; i < dst.length; ++i) {
			dst[i] = MemoryUtil.memGetFloat(address + ((position + i) << SHIFT));
		}
		position += dst.length;
		return this;
	}

	@Override
	public FloatBuffer put(FloatBuffer src) {
		if(src instanceof EaglerLWJGLFloatBuffer) {
			EaglerLWJGLFloatBuffer c = (EaglerLWJGLFloatBuffer)src;
			int l = c.limit - c.position;
			if(position + l > limit) throw new ArrayIndexOutOfBoundsException(position + l - 1);
			MemoryUtil.memCopy(c.address + (c.position << SHIFT), address + (position << SHIFT), l << SHIFT);
			position += l;
			c.position += l;
		}else {
			int l = src.remaining();
			if(position + l > limit) throw new ArrayIndexOutOfBoundsException(position + l - 1);
			for(int i = 0; i < l; ++i) {
				MemoryUtil.memPutFloat(address + ((position + l) << SHIFT), src.get());
			}
			position += l;
		}
		return this;
	}

	@Override
	public FloatBuffer put(float[] src, int offset, int length) {
		if(position + length > limit) throw new ArrayIndexOutOfBoundsException(position + length - 1);
		for(int i = 0; i < length; ++i) {
			MemoryUtil.memPutFloat(address + ((position + i) << SHIFT), src[offset + i]);
		}
		position += length;
		return this;
	}

	@Override
	public FloatBuffer put(float[] src) {
		if(position + src.length > limit) throw new ArrayIndexOutOfBoundsException(position + src.length - 1);
		for(int i = 0; i < src.length; ++i) {
			MemoryUtil.memPutFloat(address + ((position + i) << SHIFT), src[i]);
		}
		position += src.length;
		return this;
	}

	@Override
	public int getArrayOffset() {
		return position;
	}

	@Override
	public FloatBuffer compact() {
		if(limit > capacity) throw new ArrayIndexOutOfBoundsException(limit);
		if(position > limit) throw new ArrayIndexOutOfBoundsException(position);
		
		if(position == limit) {
			return new EaglerLWJGLFloatBuffer(0l, 0, false);
		}
		
		int newLen = limit - position;
		long newAlloc = JEmalloc.nje_malloc(newLen);
		MemoryUtil.memCopy(address + (position << SHIFT), newAlloc, newLen << SHIFT);
		
		return new EaglerLWJGLFloatBuffer(newAlloc, newLen, true);
	}

	@Override
	public boolean isDirect() {
		return true;
	}

	@Override
	public FloatBuffer mark() {
		mark = position;
		return this;
	}

	@Override
	public FloatBuffer reset() {
		int m = mark;
		if(m < 0) throw new ArrayIndexOutOfBoundsException("Invalid mark: " + m);
		position = m;
		return this;
	}

	@Override
	public FloatBuffer clear() {
		position = 0;
		limit = capacity;
		mark = -1;
		return this;
	}

	@Override
	public FloatBuffer flip() {
		limit = position;
		position = 0;
		mark = -1;
		return this;
	}

	@Override
	public FloatBuffer rewind() {
		position = 0;
		mark = -1;
		return this;
	}

	@Override
	public FloatBuffer limit(int newLimit) {
		if(newLimit < 0 || newLimit > capacity) throw new ArrayIndexOutOfBoundsException(newLimit);
		limit = newLimit;
		return this;
	}

	@Override
	public FloatBuffer position(int newPosition) {
		if(newPosition < 0 || newPosition > limit) throw new ArrayIndexOutOfBoundsException(newPosition);
		position = newPosition;
		return this;
	}

}
