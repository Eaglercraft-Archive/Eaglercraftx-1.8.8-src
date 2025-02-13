/*
 * Copyright (c) 2022-2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import org.teavm.jso.typedarrays.Int16Array;

import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;

public class EaglerArrayShortBuffer extends ShortBuffer {

	final Int16Array typedArray;

	final int capacity;
	int position;
	int limit;
	int mark;

	private static final int SHIFT = 1;

	EaglerArrayShortBuffer(Int16Array typedArray) {
		this.typedArray = typedArray;
		this.capacity = typedArray.getLength();
		this.position = 0;
		this.limit = this.capacity;
		this.mark = -1;
	}

	EaglerArrayShortBuffer(Int16Array typedArray, int position, int limit, int mark) {
		this.typedArray = typedArray;
		this.capacity = typedArray.getLength();
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
	public boolean hasArray() {
		return false;
	}

	@Override
	public short[] array() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ShortBuffer duplicate() {
		return new EaglerArrayShortBuffer(typedArray, position, limit, mark);
	}

	@Override
	public short get() {
		if(position >= limit) throw Buffer.makeIOOBE(position);
		return typedArray.get(position++);
	}

	@Override
	public ShortBuffer put(short b) {
		if(position >= limit) throw Buffer.makeIOOBE(position);
		typedArray.set(position++, b);
		return this;
	}

	@Override
	public short get(int index) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		return typedArray.get(index);
	}

	@Override
	public ShortBuffer put(int index, short b) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		typedArray.set(index, b);
		return this;
	}

	@Override
	public short getElement(int index) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		return typedArray.get(index);
	}

	@Override
	public void putElement(int index, short value) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		typedArray.set(index, value);
	}

	@Override
	public ShortBuffer get(short[] dst, int offset, int length) {
		if(position + length > limit) throw Buffer.makeIOOBE(position + length - 1);
		TeaVMUtils.unwrapArrayBufferView(dst).set(Int16Array.create(typedArray.getBuffer(), typedArray.getByteOffset() + (position << SHIFT), length), offset);
		position += length;
		return this;
	}

	@Override
	public ShortBuffer get(short[] dst) {
		if(position + dst.length > limit) throw Buffer.makeIOOBE(position + dst.length - 1);
		TeaVMUtils.unwrapArrayBufferView(dst).set(Int16Array.create(typedArray.getBuffer(), typedArray.getByteOffset() + (position << SHIFT), dst.length));
		position += dst.length;
		return this;
	}

	@Override
	public ShortBuffer put(ShortBuffer src) {
		if(src instanceof EaglerArrayShortBuffer) {
			EaglerArrayShortBuffer c = (EaglerArrayShortBuffer)src;
			int l = c.limit - c.position;
			if(position + l > limit) throw Buffer.makeIOOBE(position + l - 1);
			typedArray.set(Int16Array.create(c.typedArray.getBuffer(), c.typedArray.getByteOffset() + (c.position << SHIFT), l), position);
			position += l;
			c.position += l;
		}else {
			int l = src.remaining();
			if(position + l > limit) throw Buffer.makeIOOBE(position + l - 1);
			for(int i = 0; i < l; ++i) {
				typedArray.set(position + i, src.get());
			}
			position += l;
		}
		return this;
	}

	@Override
	public ShortBuffer put(short[] src, int offset, int length) {
		if(position + length > limit) throw Buffer.makeIOOBE(position + length - 1);
		if(offset == 0 && length == src.length) {
			typedArray.set(TeaVMUtils.unwrapArrayBufferView(src), position);
		}else {
			typedArray.set(Int16Array.create(TeaVMUtils.unwrapArrayBuffer(src), offset << SHIFT, length), position);
		}
		position += length;
		return this;
	}

	@Override
	public ShortBuffer put(short[] src) {
		if(position + src.length > limit) throw Buffer.makeIOOBE(position + src.length - 1);
		typedArray.set(TeaVMUtils.unwrapArrayBufferView(src), position);
		position += src.length;
		return this;
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
		if(m < 0) throw new IndexOutOfBoundsException("Invalid mark: " + m);
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
		if(newLimit < 0 || newLimit > capacity) throw Buffer.makeIOOBE(newLimit);
		limit = newLimit;
		return this;
	}

	@Override
	public ShortBuffer position(int newPosition) {
		if(newPosition < 0 || newPosition > limit) throw Buffer.makeIOOBE(newPosition);
		position = newPosition;
		return this;
	}

}