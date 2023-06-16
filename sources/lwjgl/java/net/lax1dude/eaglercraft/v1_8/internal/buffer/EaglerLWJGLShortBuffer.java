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
public class EaglerLWJGLShortBuffer implements ShortBuffer {
	
	final long address;
	final boolean original;

	private final int capacity;
	private int position;
	private int limit;
	private int mark;
	
	private static final int SHIFT = 1;
	
	EaglerLWJGLShortBuffer(long address, int capacity, boolean original) {
		this(address, capacity, 0, capacity, -1, original);
	}

	EaglerLWJGLShortBuffer(long address, int capacity, int position, int limit, int mark, boolean original) {
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
	public ShortBuffer slice() {
		return new EaglerLWJGLShortBuffer(address + (position << SHIFT), limit - position, false);
	}

	@Override
	public ShortBuffer duplicate() {
		return new EaglerLWJGLShortBuffer(address, capacity, position, limit, mark, false);
	}

	@Override
	public ShortBuffer asReadOnlyBuffer() {
		return new EaglerLWJGLShortBuffer(address, capacity, position, limit, mark, false);
	}

	@Override
	public short get() {
		if(position >= limit) throw new ArrayIndexOutOfBoundsException(position);
		return MemoryUtil.memGetShort(address + ((position++) << SHIFT));
	}

	@Override
	public ShortBuffer put(short b) {
		if(position >= limit) throw new ArrayIndexOutOfBoundsException(position);
		MemoryUtil.memPutShort(address + ((position++) << SHIFT), b);
		return this;
	}

	@Override
	public short get(int index) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		return MemoryUtil.memGetShort(address + (index << SHIFT));
	}

	@Override
	public ShortBuffer put(int index, short b) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		MemoryUtil.memPutShort(address + (index << SHIFT), b);
		return this;
	}

	@Override
	public short getElement(int index) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		return MemoryUtil.memGetShort(address + (index << SHIFT));
	}

	@Override
	public void putElement(int index, short value) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		MemoryUtil.memPutShort(address + (index << SHIFT), value);
	}

	@Override
	public ShortBuffer get(short[] dst, int offset, int length) {
		if(position + length > limit) throw new ArrayIndexOutOfBoundsException(position + length - 1);
		for(int i = 0; i < length; ++i) {
			dst[offset + i] = MemoryUtil.memGetShort(address + ((position + i) << SHIFT));
		}
		position += length;
		return this;
	}

	@Override
	public ShortBuffer get(short[] dst) {
		if(position + dst.length > limit) throw new ArrayIndexOutOfBoundsException(position + dst.length - 1);
		for(int i = 0; i < dst.length; ++i) {
			dst[i] = MemoryUtil.memGetShort(address + ((position + i) << SHIFT));
		}
		position += dst.length;
		return this;
	}

	@Override
	public ShortBuffer put(ShortBuffer src) {
		if(src instanceof EaglerLWJGLShortBuffer) {
			EaglerLWJGLShortBuffer c = (EaglerLWJGLShortBuffer)src;
			int l = c.limit - c.position;
			if(position + l > limit) throw new ArrayIndexOutOfBoundsException(position + l - 1);
			MemoryUtil.memCopy(c.address + (c.position << SHIFT), address + (position << SHIFT), l << SHIFT);
			position += l;
			c.position += l;
		}else {
			int l = src.remaining();
			if(position + l > limit) throw new ArrayIndexOutOfBoundsException(position + l - 1);
			for(int i = 0; i < l; ++i) {
				MemoryUtil.memPutShort(address + ((position + l) << SHIFT), src.get());
			}
			position += l;
		}
		return this;
	}

	@Override
	public ShortBuffer put(short[] src, int offset, int length) {
		if(position + length > limit) throw new ArrayIndexOutOfBoundsException(position + length - 1);
		for(int i = 0; i < length; ++i) {
			MemoryUtil.memPutShort(address + ((position + i) << SHIFT), src[offset + i]);
		}
		position += length;
		return this;
	}

	@Override
	public ShortBuffer put(short[] src) {
		if(position + src.length > limit) throw new ArrayIndexOutOfBoundsException(position + src.length - 1);
		for(int i = 0; i < src.length; ++i) {
			MemoryUtil.memPutShort(address + ((position + i) << SHIFT), src[i]);
		}
		position += src.length;
		return this;
	}

	@Override
	public int getArrayOffset() {
		return position;
	}

	@Override
	public ShortBuffer compact() {
		if(limit > capacity) throw new ArrayIndexOutOfBoundsException(limit);
		if(position > limit) throw new ArrayIndexOutOfBoundsException(position);
		
		if(position == limit) {
			return new EaglerLWJGLShortBuffer(0l, 0, false);
		}
		
		int newLen = limit - position;
		long newAlloc = JEmalloc.nje_malloc(newLen);
		MemoryUtil.memCopy(address + (position << SHIFT), newAlloc, newLen << SHIFT);
		
		return new EaglerLWJGLShortBuffer(newAlloc, newLen, true);
	}

	@Override
	public boolean isDirect() {
		return true;
	}

	@Override
	public ShortBuffer mark() {
		mark = position;
		return this;
	}

	@Override
	public ShortBuffer reset() {
		int m = mark;
		if(m < 0) throw new ArrayIndexOutOfBoundsException("Invalid mark: " + m);
		position = m;
		return this;
	}

	@Override
	public ShortBuffer clear() {
		position = 0;
		limit = capacity;
		mark = -1;
		return this;
	}

	@Override
	public ShortBuffer flip() {
		limit = position;
		position = 0;
		mark = -1;
		return this;
	}

	@Override
	public ShortBuffer rewind() {
		position = 0;
		mark = -1;
		return this;
	}

	@Override
	public ShortBuffer limit(int newLimit) {
		if(newLimit < 0 || newLimit > capacity) throw new ArrayIndexOutOfBoundsException(newLimit);
		limit = newLimit;
		return this;
	}

	@Override
	public ShortBuffer position(int newPosition) {
		if(newPosition < 0 || newPosition > limit) throw new ArrayIndexOutOfBoundsException(newPosition);
		position = newPosition;
		return this;
	}

}
