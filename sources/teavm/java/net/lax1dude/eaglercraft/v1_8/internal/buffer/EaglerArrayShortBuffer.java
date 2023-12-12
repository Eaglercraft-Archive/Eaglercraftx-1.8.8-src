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
public class EaglerArrayShortBuffer implements ShortBuffer {

	final DataView dataView;

	final int capacity;
	int position;
	int limit;
	int mark;
	
	private static final int SHIFT = 1;
	
	EaglerArrayShortBuffer(DataView dataView) {
		this.dataView = dataView;
		this.capacity = dataView.getByteLength() >> SHIFT;
		this.position = 0;
		this.limit = this.capacity;
		this.mark = -1;
	}
	
	EaglerArrayShortBuffer(DataView dataView, int position, int limit, int mark) {
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
	public ShortBuffer slice() {
		int o = dataView.getByteOffset();
		return new EaglerArrayShortBuffer(DataView.create(dataView.getBuffer(), o + (position << SHIFT), (limit - position) << SHIFT));
	}

	@Override
	public ShortBuffer duplicate() {
		return new EaglerArrayShortBuffer(dataView, position, limit, mark);
	}

	@Override
	public ShortBuffer asReadOnlyBuffer() {
		return new EaglerArrayShortBuffer(dataView, position, limit, mark);
	}

	@Override
	public short get() {
		if(position >= limit) throw new ArrayIndexOutOfBoundsException(position);
		return dataView.getInt16((position++) << SHIFT, true);
	}

	@Override
	public ShortBuffer put(short b) {
		if(position >= limit) throw new ArrayIndexOutOfBoundsException(position);
		dataView.setInt16((position++) << SHIFT, b, true);
		return this;
	}

	@Override
	public short get(int index) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		return dataView.getInt16(index << SHIFT, true);
	}

	@Override
	public ShortBuffer put(int index, short b) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		dataView.setInt16(index << SHIFT, b, true);
		return this;
	}

	@Override
	public short getElement(int index) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		return dataView.getInt16(index << SHIFT, true);
	}

	@Override
	public void putElement(int index, short value) {
		if(index >= limit) throw new ArrayIndexOutOfBoundsException(index);
		dataView.setInt16(index << SHIFT, value, true);
	}

	@Override
	public ShortBuffer get(short[] dst, int offset, int length) {
		if(position + length > limit) throw new ArrayIndexOutOfBoundsException(position + length - 1);
		for(int i = 0; i < length; ++i) {
			dst[offset + i] = dataView.getInt16((position + i) << SHIFT, true);
		}
		position += length;
		return this;
	}

	@Override
	public ShortBuffer get(short[] dst) {
		if(position + dst.length > limit) throw new ArrayIndexOutOfBoundsException(position + dst.length - 1);
		for(int i = 0; i < dst.length; ++i) {
			dst[i] = dataView.getInt16((position + i) << SHIFT, true);
		}
		position += dst.length;
		return this;
	}

	@Override
	public ShortBuffer put(ShortBuffer src) {
		if(src instanceof EaglerArrayShortBuffer) {
			EaglerArrayShortBuffer c = (EaglerArrayShortBuffer)src;
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
				dataView.setInt16((position + l) << SHIFT, src.get(), true);
			}
			position += l;
		}
		return this;
	}

	@Override
	public ShortBuffer put(short[] src, int offset, int length) {
		if(position + length > limit) throw new ArrayIndexOutOfBoundsException(position + length - 1);
		for(int i = 0; i < length; ++i) {
			dataView.setInt16((position + i) << SHIFT, src[offset + i], true);
		}
		position += length;
		return this;
	}

	@Override
	public ShortBuffer put(short[] src) {
		if(position + src.length > limit) throw new ArrayIndexOutOfBoundsException(position + src.length - 1);
		for(int i = 0; i < src.length; ++i) {
			dataView.setInt16((position + i) << SHIFT, src[i], true);
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
			return new EaglerArrayShortBuffer(EaglerArrayByteBuffer.ZERO_LENGTH_BUFFER);
		}
		
		int o = dataView.getByteOffset();
		
		Uint8Array dst = Uint8Array.create(ArrayBuffer.create((limit - position) << SHIFT));
		dst.set(Uint8Array.create(dataView.getBuffer(), o + (position << SHIFT), (limit - position) << SHIFT));
		
		return new EaglerArrayShortBuffer(DataView.create(dst.getBuffer()));
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
