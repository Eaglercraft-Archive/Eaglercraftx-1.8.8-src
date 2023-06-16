package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.DataView;
import org.teavm.jso.typedarrays.Uint8Array;

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
public class EaglerArrayIntBuffer implements IntBuffer {

	final DataView dataView;

	final int capacity;
	int position;
	int limit;
	int mark;
	
	private static final int SHIFT = 2;
	
	EaglerArrayIntBuffer(DataView dataView) {
		this.dataView = dataView;
		this.capacity = dataView.getByteLength() >> SHIFT;
		this.position = 0;
		this.limit = this.capacity;
		this.mark = -1;
	}
	
	EaglerArrayIntBuffer(DataView dataView, int position, int limit, int mark) {
		this.dataView = dataView;
		this.capacity = dataView.getByteLength() >> SHIFT;
		this.position = position;
		this.limit = limit;
		this.mark = mark;
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
	public IntBuffer slice() {
		int o = dataView.getByteOffset();
		return new EaglerArrayIntBuffer(DataView.create(dataView.getBuffer(), o + (position << SHIFT), (limit - position) << SHIFT));
	}

	@Override
	public IntBuffer duplicate() {
		return new EaglerArrayIntBuffer(dataView, position, limit, mark);
	}

	@Override
	public IntBuffer asReadOnlyBuffer() {
		return new EaglerArrayIntBuffer(dataView, position, limit, mark);
	}

	@Override
	public int get() {
		if(position >= limit) throw new ArrayIndexOutOfBoundsException(position);
		return dataView.getInt32((position++) << SHIFT, true);
	}

	@Override
	public IntBuffer put(int b) {
		if(position >= limit) throw new ArrayIndexOutOfBoundsException(position);
		dataView.setInt32((position++) << SHIFT, b, true);
		return this;
	}

	@Override
	public int get(int index) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		return dataView.getInt32(index << SHIFT, true);
	}

	@Override
	public IntBuffer put(int index, int b) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		dataView.setInt32(index << SHIFT, b, true);
		return this;
	}

	@Override
	public int getElement(int index) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		return dataView.getInt32(index << SHIFT, true);
	}

	@Override
	public void putElement(int index, int value) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		dataView.setInt32(index << SHIFT, value, true);
	}

	@Override
	public IntBuffer get(int[] dst, int offset, int length) {
		if(position + length > limit) throw new ArrayIndexOutOfBoundsException(position + length - 1);
		for(int i = 0; i < length; ++i) {
			dst[offset + i] = dataView.getInt32((position + i) << SHIFT, true);
		}
		position += length;
		return this;
	}

	@Override
	public IntBuffer get(int[] dst) {
		if(position + dst.length > limit) throw new ArrayIndexOutOfBoundsException(position + dst.length - 1);
		for(int i = 0; i < dst.length; ++i) {
			dst[i] = dataView.getInt32((position + i) << SHIFT, true);
		}
		position += dst.length;
		return this;
	}

	@Override
	public IntBuffer put(IntBuffer src) {
		if(src instanceof EaglerArrayIntBuffer) {
			EaglerArrayIntBuffer c = (EaglerArrayIntBuffer)src;
			int l = c.limit - c.position;
			if(position + l > limit) throw new ArrayIndexOutOfBoundsException(position + l - 1);
			int o = c.dataView.getByteOffset();
			Uint8Array.create(dataView.getBuffer()).set(
					Uint8Array.create(c.dataView.getBuffer(), o + (c.position << SHIFT), (c.limit - c.position) << SHIFT),
					dataView.getByteOffset() + (position << SHIFT));
			position += l;
			c.position += l;
		}else {
			int l = src.remaining();
			if(position + l > limit) throw new ArrayIndexOutOfBoundsException(position + l - 1);
			for(int i = 0; i < l; ++i) {
				dataView.setInt32((position + l) << SHIFT, src.get(), true);
			}
			position += l;
		}
		return this;
	}

	@Override
	public IntBuffer put(int[] src, int offset, int length) {
		if(position + length > limit) throw new ArrayIndexOutOfBoundsException(position + length - 1);
		for(int i = 0; i < length; ++i) {
			dataView.setInt32((position + i) << SHIFT, src[offset + i], true);
		}
		position += length;
		return this;
	}

	@Override
	public IntBuffer put(int[] src) {
		if(position + src.length > limit) throw new ArrayIndexOutOfBoundsException(position + src.length - 1);
		for(int i = 0; i < src.length; ++i) {
			dataView.setInt32((position + i) << SHIFT, src[i], true);
		}
		position += src.length;
		return this;
	}

	@Override
	public int getArrayOffset() {
		return position;
	}

	@Override
	public IntBuffer compact() {
		if(limit > capacity) throw new ArrayIndexOutOfBoundsException(limit);
		if(position > limit) throw new ArrayIndexOutOfBoundsException(position);
		
		if(position == limit) {
			return new EaglerArrayIntBuffer(EaglerArrayByteBuffer.ZERO_LENGTH_BUFFER);
		}
		
		int o = dataView.getByteOffset();
		
		Uint8Array dst = Uint8Array.create(ArrayBuffer.create((limit - position) << SHIFT));
		dst.set(Uint8Array.create(dataView.getBuffer(), o + (position << SHIFT), (limit - position) << SHIFT));
		
		return new EaglerArrayIntBuffer(DataView.create(dst.getBuffer()));
	}

	@Override
	public boolean isDirect() {
		return true;
	}

	@Override
	public IntBuffer mark() {
		mark = position;
		return this;
	}

	@Override
	public IntBuffer reset() {
		int m = mark;
		if(m < 0) throw new ArrayIndexOutOfBoundsException("Invalid mark: " + m);
		position = m;
		return this;
	}

	@Override
	public IntBuffer clear() {
		position = 0;
		limit = capacity;
		mark = -1;
		return this;
	}

	@Override
	public IntBuffer flip() {
		limit = position;
		position = 0;
		mark = -1;
		return this;
	}

	@Override
	public IntBuffer rewind() {
		position = 0;
		mark = -1;
		return this;
	}

	@Override
	public IntBuffer limit(int newLimit) {
		if(newLimit < 0 || newLimit > capacity) throw new ArrayIndexOutOfBoundsException(newLimit);
		limit = newLimit;
		return this;
	}

	@Override
	public IntBuffer position(int newPosition) {
		if(newPosition < 0 || newPosition > limit) throw new ArrayIndexOutOfBoundsException(newPosition);
		position = newPosition;
		return this;
	}

}
