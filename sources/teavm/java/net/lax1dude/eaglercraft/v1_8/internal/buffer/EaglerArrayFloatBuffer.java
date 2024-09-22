package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import org.teavm.jso.typedarrays.Float32Array;

import net.lax1dude.eaglercraft.v1_8.internal.teavm.TeaVMUtils;

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

	final Float32Array typedArray;

	final int capacity;
	int position;
	int limit;
	int mark;

	private static final int SHIFT = 2;

	EaglerArrayFloatBuffer(Float32Array typedArray) {
		this.typedArray = typedArray;
		this.capacity = typedArray.getLength();
		this.position = 0;
		this.limit = this.capacity;
		this.mark = -1;
	}

	EaglerArrayFloatBuffer(Float32Array typedArray, int position, int limit, int mark) {
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
	public float[] array() {
		throw new UnsupportedOperationException();
	}

	@Override
	public FloatBuffer duplicate() {
		return new EaglerArrayFloatBuffer(typedArray, position, limit, mark);
	}

	@Override
	public float get() {
		if(position >= limit) throw Buffer.makeIOOBE(position);
		return typedArray.get(position++);
	}

	@Override
	public FloatBuffer put(float b) {
		if(position >= limit) throw Buffer.makeIOOBE(position);
		typedArray.set(position++, b);
		return this;
	}

	@Override
	public float get(int index) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		return typedArray.get(index);
	}

	@Override
	public FloatBuffer put(int index, float b) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		typedArray.set(index, b);
		return this;
	}

	@Override
	public float getElement(int index) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		return typedArray.get(index);
	}

	@Override
	public void putElement(int index, float value) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		typedArray.set(index, value);
	}

	@Override
	public FloatBuffer get(float[] dst, int offset, int length) {
		if(position + length > limit) Buffer.makeIOOBE(position + length - 1);
		TeaVMUtils.unwrapArrayBufferView(dst).set(Float32Array.create(typedArray.getBuffer(), typedArray.getByteOffset() + (position << SHIFT), length), offset);
		position += length;
		return this;
	}

	@Override
	public FloatBuffer get(float[] dst) {
		if(position + dst.length > limit) Buffer.makeIOOBE(position + dst.length - 1);
		TeaVMUtils.unwrapArrayBufferView(dst).set(Float32Array.create(typedArray.getBuffer(), typedArray.getByteOffset() + (position << SHIFT), dst.length));
		position += dst.length;
		return this;
	}

	@Override
	public FloatBuffer put(FloatBuffer src) {
		if(src instanceof EaglerArrayFloatBuffer) {
			EaglerArrayFloatBuffer c = (EaglerArrayFloatBuffer)src;
			int l = c.limit - c.position;
			if(position + l > limit) throw Buffer.makeIOOBE(position + l - 1);
			typedArray.set(Float32Array.create(c.typedArray.getBuffer(), c.typedArray.getByteOffset() + (c.position << SHIFT), l), position);
			position += l;
			c.position += l;
		}else {
			int l = src.remaining();
			if(position + l > limit) throw Buffer.makeIOOBE(position + l - 1);
			for(int i = 0; i < l; ++i) {
				typedArray.set(position + l, src.get());
			}
			position += l;
		}
		return this;
	}

	@Override
	public FloatBuffer put(float[] src, int offset, int length) {
		if(position + length > limit) throw Buffer.makeIOOBE(position + length - 1);
		if(offset == 0 && length == src.length) {
			typedArray.set(TeaVMUtils.unwrapArrayBufferView(src), position);
		}else {
			typedArray.set(Float32Array.create(TeaVMUtils.unwrapArrayBuffer(src), offset << SHIFT, length), position);
		}
		position += length;
		return this;
	}

	@Override
	public FloatBuffer put(float[] src) {
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
	public FloatBuffer mark() {
		mark = position;
		return this;
	}

	@Override
	public FloatBuffer reset() {
		int m = mark;
		if(m < 0) throw new IndexOutOfBoundsException("Invalid mark: " + m);
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
		if(newLimit < 0 || newLimit > capacity) throw Buffer.makeIOOBE(newLimit);
		limit = newLimit;
		return this;
	}

	@Override
	public FloatBuffer position(int newPosition) {
		if(newPosition < 0 || newPosition > limit) throw Buffer.makeIOOBE(newPosition);
		position = newPosition;
		return this;
	}

}
