package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import org.teavm.jso.typedarrays.DataView;
import org.teavm.jso.typedarrays.Float32Array;
import org.teavm.jso.typedarrays.Int16Array;
import org.teavm.jso.typedarrays.Int32Array;
import org.teavm.jso.typedarrays.Int8Array;

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
public class EaglerArrayByteBuffer implements ByteBuffer {

	final DataView dataView;
	final Int8Array typedArray;

	final int capacity;
	int position;
	int limit;
	int mark;

	EaglerArrayByteBuffer(DataView dataView) {
		this.dataView = dataView;
		this.typedArray = Int8Array.create(dataView.getBuffer(), dataView.getByteOffset(), dataView.getByteLength());
		this.capacity = dataView.getByteLength();
		this.position = 0;
		this.limit = this.capacity;
		this.mark = -1;
	}

	EaglerArrayByteBuffer(DataView dataView, int position, int limit, int mark) {
		this.dataView = dataView;
		this.typedArray = Int8Array.create(dataView.getBuffer(), dataView.getByteOffset(), dataView.getByteLength());
		this.capacity = dataView.getByteLength();
		this.position = position;
		this.limit = limit;
		this.mark = mark;
	}

	EaglerArrayByteBuffer(Int8Array typedArray) {
		this.typedArray = typedArray;
		this.dataView = DataView.create(typedArray.getBuffer(), typedArray.getByteOffset(), typedArray.getByteLength());
		this.capacity = typedArray.getByteLength();
		this.position = 0;
		this.limit = this.capacity;
		this.mark = -1;
	}

	EaglerArrayByteBuffer(Int8Array typedArray, int position, int limit, int mark) {
		this.typedArray = typedArray;
		this.dataView = DataView.create(typedArray.getBuffer(), typedArray.getByteOffset(), typedArray.getByteLength());
		this.capacity = typedArray.getByteLength();
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
		return limit > position;
	}

	@Override
	public boolean hasArray() {
		return false;
	}

	@Override
	public byte[] array() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDirect() {
		return true;
	}

	@Override
	public ByteBuffer duplicate() {
		return new EaglerArrayByteBuffer(dataView, position, limit, mark);
	}

	@Override
	public byte get() {
		if(position >= limit) throw Buffer.makeIOOBE(position);
		return typedArray.get(position++);
	}

	@Override
	public ByteBuffer put(byte b) {
		if(position >= limit) throw Buffer.makeIOOBE(position);
		typedArray.set(position++, b);
		return this;
	}

	@Override
	public byte get(int index) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		return typedArray.get(index);
	}

	@Override
	public ByteBuffer put(int index, byte b) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		typedArray.set(index, b);
		return this;
	}

	@Override
	public ByteBuffer get(byte[] dst, int offset, int length) {
		if(position + length > limit) throw Buffer.makeIOOBE(position + length - 1);
		TeaVMUtils.unwrapArrayBufferView(dst).set(Int8Array.create(typedArray.getBuffer(), typedArray.getByteOffset() + position, length), offset);
		position += length;
		return this;
	}

	@Override
	public ByteBuffer get(byte[] dst) {
		if(position + dst.length > limit) throw Buffer.makeIOOBE(position + dst.length - 1);
		TeaVMUtils.unwrapArrayBufferView(dst).set(Int8Array.create(typedArray.getBuffer(), typedArray.getByteOffset() + position, dst.length));
		position += dst.length;
		return this;
	}

	@Override
	public ByteBuffer put(ByteBuffer src) {
		if(src instanceof EaglerArrayByteBuffer) {
			EaglerArrayByteBuffer c = (EaglerArrayByteBuffer)src;
			int l = c.limit - c.position;
			if(position + l > limit) throw Buffer.makeIOOBE(position + l - 1);
			typedArray.set(Int8Array.create(c.typedArray.getBuffer(), c.typedArray.getByteOffset() + c.position, l), position);
			position += l;
			c.position += l;
		}else {
			int l = src.remaining();
			if(position + l > limit) throw Buffer.makeIOOBE(position + l - 1);
			for(int i = 0; i < l; ++i) {
				dataView.setInt8(position + l, src.get());
			}
			position += l;
		}
		return this;
	}

	@Override
	public ByteBuffer put(byte[] src, int offset, int length) {
		if(position + length > limit) throw Buffer.makeIOOBE(position + length - 1);
		if(offset == 0 && length == src.length) {
			typedArray.set(TeaVMUtils.unwrapArrayBufferView(src), position);
		}else {
			typedArray.set(Int8Array.create(TeaVMUtils.unwrapArrayBuffer(src), offset, length), position);
		}
		position += length;
		return this;
	}

	@Override
	public ByteBuffer put(byte[] src) {
		if(position + src.length > limit) throw Buffer.makeIOOBE(position + src.length - 1);
		typedArray.set(TeaVMUtils.unwrapArrayBufferView(src), position);
		position += src.length;
		return this;
	}

	@Override
	public char getChar() {
		if(position + 2 > limit) throw Buffer.makeIOOBE(position);
		char c = (char)dataView.getUint16(position, true);
		position += 2;
		return c;
	}

	@Override
	public ByteBuffer putChar(char value) {
		if(position + 2 > limit) throw Buffer.makeIOOBE(position);
		dataView.setUint16(position, (short)value, true);
		position += 2;
		return this;
	}

	@Override
	public char getChar(int index) {
		if(index < 0 || index + 2 > limit) throw Buffer.makeIOOBE(index);
		return (char)dataView.getUint16(index, true);
	}

	@Override
	public ByteBuffer putChar(int index, char value) {
		if(index < 0 || index + 2 > limit) throw Buffer.makeIOOBE(index);
		dataView.setUint16(index, value, true);
		return this;
	}

	@Override
	public short getShort() {
		if(position + 2 > limit) throw Buffer.makeIOOBE(position);
		short s = dataView.getInt16(position, true);
		position += 2;
		return s;
	}

	@Override
	public ByteBuffer putShort(short value) {
		if(position + 2 > limit) throw Buffer.makeIOOBE(position);
		dataView.setInt16(position, value, true);
		position += 2;
		return this;
	}

	@Override
	public short getShort(int index) {
		if(index < 0 || index + 2 > limit) throw Buffer.makeIOOBE(index);
		return dataView.getInt16(index, true);
	}

	@Override
	public ByteBuffer putShort(int index, short value) {
		if(index < 0 || index + 2 > limit) throw Buffer.makeIOOBE(index);
		dataView.setInt16(index, value, true);
		return this;
	}

	@Override
	public ShortBuffer asShortBuffer() {
		return new EaglerArrayShortBuffer(Int16Array.create(typedArray.getBuffer(), typedArray.getByteOffset(), typedArray.getLength() >> 1));
	}

	@Override
	public int getInt() {
		if(position + 4 > limit) throw Buffer.makeIOOBE(position);
		int i = dataView.getInt32(position, true);
		position += 4;
		return i;
	}

	@Override
	public ByteBuffer putInt(int value) {
		if(position + 4 > limit) throw Buffer.makeIOOBE(position);
		dataView.setInt32(position, value, true);
		position += 4;
		return this;
	}

	@Override
	public int getInt(int index) {
		if(index < 0 || index + 4 > limit) throw Buffer.makeIOOBE(index);
		return dataView.getInt32(index, true);
	}

	@Override
	public ByteBuffer putInt(int index, int value) {
		if(index < 0 || index + 4 > limit) throw Buffer.makeIOOBE(index);
		dataView.setInt32(index, value, true);
		return this;
	}

	@Override
	public IntBuffer asIntBuffer() {
		return new EaglerArrayIntBuffer(Int32Array.create(typedArray.getBuffer(), typedArray.getByteOffset(), typedArray.getLength() >> 2));
	}

	@Override
	public long getLong() {
		if(position + 8 > limit) throw Buffer.makeIOOBE(position);
		long l = (long)dataView.getUint32(position, true) | ((long) dataView.getUint32(position + 4) << 32l);
		position += 8;
		return l;
	}

	@Override
	public ByteBuffer putLong(long value) {
		if(position + 8 > limit) throw Buffer.makeIOOBE(position);
		dataView.setUint32(position, (int) (value & 0xFFFFFFFFl), true);
		dataView.setUint32(position + 4, (int) (value >>> 32l), true);
		position += 8;
		return this;
	}

	@Override
	public long getLong(int index) {
		if(index < 0 || index + 8 > limit) throw Buffer.makeIOOBE(index);
		return (long)dataView.getUint32(index, true) | ((long) dataView.getUint32(index + 4) << 32l);
	}

	@Override
	public ByteBuffer putLong(int index, long value) {
		if(index < 0 || index + 8 > limit) throw Buffer.makeIOOBE(index);
		dataView.setUint32(index, (int) (value & 0xFFFFFFFFl), true);
		dataView.setUint32(index + 4, (int) (value >>> 32l), true);
		return this;
	}

	@Override
	public float getFloat() {
		if(position + 4 > limit) throw Buffer.makeIOOBE(position);
		float f = dataView.getFloat32(position, true);
		position += 4;
		return f;
	}

	@Override
	public ByteBuffer putFloat(float value) {
		if(position + 4 > limit) throw Buffer.makeIOOBE(position);
		dataView.setFloat32(position, value, true);
		position += 4;
		return this;
	}

	@Override
	public float getFloat(int index) {
		if(index < 0 || index + 4 > limit) throw Buffer.makeIOOBE(index);
		return dataView.getFloat32(index, true);
	}

	@Override
	public ByteBuffer putFloat(int index, float value) {
		if(index < 0 || index + 4 > limit) throw Buffer.makeIOOBE(index);
		dataView.setFloat32(index, value, true);
		return this;
	}

	@Override
	public FloatBuffer asFloatBuffer() {
		return new EaglerArrayFloatBuffer(Float32Array.create(typedArray.getBuffer(), typedArray.getByteOffset(), typedArray.getLength() >> 2));
	}

	@Override
	public ByteBuffer mark() {
		mark = position;
		return this;
	}

	@Override
	public ByteBuffer reset() {
		int m = mark;
		if(m < 0) throw new IndexOutOfBoundsException("Invalid mark: " + m);
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
		if(newLimit < 0 || newLimit > capacity) throw Buffer.makeIOOBE(newLimit);
		limit = newLimit;
		return this;
	}

	@Override
	public ByteBuffer position(int newPosition) {
		if(newPosition < 0 || newPosition > limit) throw Buffer.makeIOOBE(newPosition);
		position = newPosition;
		return this;
	}
}
