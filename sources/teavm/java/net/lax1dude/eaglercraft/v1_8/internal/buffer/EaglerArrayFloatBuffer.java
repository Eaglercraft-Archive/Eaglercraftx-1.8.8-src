package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.DataView;
import org.teavm.jso.typedarrays.Uint8Array;

/**
 * Copyright (c) 2022-2023 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class EaglerArrayFloatBuffer implements FloatBuffer {

	final DataView dataView;

	final int capacity;
	int position;
	int limit;
	int mark;
	
	private static final int SHIFT = 2;
	
	EaglerArrayFloatBuffer(DataView dataView) {
		this.dataView = dataView;
		this.capacity = dataView.getByteLength() >> SHIFT;
		this.position = 0;
		this.limit = this.capacity;
		this.mark = -1;
	}
	
	EaglerArrayFloatBuffer(DataView dataView, int position, int limit, int mark) {
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
	public FloatBuffer slice() {
		int o = dataView.getByteOffset();
		return new EaglerArrayFloatBuffer(DataView.create(dataView.getBuffer(), o + (position << SHIFT), (limit - position) << SHIFT));
	}

	@Override
	public FloatBuffer duplicate() {
		return new EaglerArrayFloatBuffer(dataView, position, limit, mark);
	}

	@Override
	public FloatBuffer asReadOnlyBuffer() {
		return new EaglerArrayFloatBuffer(dataView, position, limit, mark);
	}

	@Override
	public float get() {
		if(position >= limit) throw new ArrayIndexOutOfBoundsException(position);
		return dataView.getFloat32((position++) << SHIFT, true);
	}

	@Override
	public FloatBuffer put(float b) {
		if(position >= limit) throw new ArrayIndexOutOfBoundsException(position);
		dataView.setFloat32((position++) << SHIFT, b, true);
		return this;
	}

	@Override
	public float get(int index) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		return dataView.getFloat32(index << SHIFT, true);
	}

	@Override
	public FloatBuffer put(int index, float b) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		dataView.setFloat32(index << SHIFT, b, true);
		return this;
	}

	@Override
	public float getElement(int index) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		return dataView.getFloat32(index << SHIFT, true);
	}

	@Override
	public void putElement(int index, float value) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		dataView.setFloat32(index << SHIFT, value, true);
	}

	@Override
	public FloatBuffer get(float[] dst, int offset, int length) {
		if(position + length > limit) throw new ArrayIndexOutOfBoundsException(position + length - 1);
		for(int i = 0; i < length; ++i) {
			dst[offset + i] = dataView.getFloat32((position + i) << SHIFT, true);
		}
		position += length;
		return this;
	}

	@Override
	public FloatBuffer get(float[] dst) {
		if(position + dst.length > limit) throw new ArrayIndexOutOfBoundsException(position + dst.length - 1);
		for(int i = 0; i < dst.length; ++i) {
			dst[i] = dataView.getFloat32((position + i) << SHIFT, true);
		}
		position += dst.length;
		return this;
	}

	@Override
	public FloatBuffer put(FloatBuffer src) {
		if(src instanceof EaglerArrayFloatBuffer) {
			EaglerArrayFloatBuffer c = (EaglerArrayFloatBuffer)src;
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
				dataView.setFloat32((position + l) << SHIFT, src.get(), true);
			}
			position += l;
		}
		return this;
	}

	@Override
	public FloatBuffer put(float[] src, int offset, int length) {
		if(position + length > limit) throw new ArrayIndexOutOfBoundsException(position + length - 1);
		for(int i = 0; i < length; ++i) {
			dataView.setFloat32((position + i) << SHIFT, src[offset + i], true);
		}
		position += length;
		return this;
	}

	@Override
	public FloatBuffer put(float[] src) {
		if(position + src.length > limit) throw new ArrayIndexOutOfBoundsException(position + src.length - 1);
		for(int i = 0; i < src.length; ++i) {
			dataView.setFloat32((position + i) << SHIFT, src[i], true);
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
			return new EaglerArrayFloatBuffer(EaglerArrayByteBuffer.ZERO_LENGTH_BUFFER);
		}
		
		int o = dataView.getByteOffset();
		
		Uint8Array dst = Uint8Array.create(ArrayBuffer.create((limit - position) << SHIFT));
		dst.set(Uint8Array.create(dataView.getBuffer(), o + (position << SHIFT), (limit - position) << SHIFT));
		
		return new EaglerArrayFloatBuffer(DataView.create(dst.getBuffer()));
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
