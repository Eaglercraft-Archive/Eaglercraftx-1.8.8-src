/*
 * Copyright (c) 2024-2025 lax1dude. All Rights Reserved.
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

import org.teavm.interop.Address;

public class DirectMallocByteBuffer extends ByteBuffer {

	final Address address;
	final boolean original;

	private final int capacity;
	private int position;
	private int limit;
	private int mark;

	DirectMallocByteBuffer(Address address, int capacity, boolean original) {
		this(address, capacity, 0, capacity, -1, original);
	}

	DirectMallocByteBuffer(Address address, int capacity, int position, int limit, int mark, boolean original) {
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
		return new DirectMallocByteBuffer(address, capacity, position, limit, mark, false);
	}

	@Override
	public byte get() {
		if(position >= limit) throw Buffer.makeIOOBE(position);
		return address.add(position++).getByte();
	}

	@Override
	public ByteBuffer put(byte b) {
		if(position >= limit) throw Buffer.makeIOOBE(position);
		address.add(position++).putByte(b);
		return this;
	}

	@Override
	public byte get(int index) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		return address.add(index).getByte();
	}

	@Override
	public ByteBuffer put(int index, byte b) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		address.add(index).putByte(b);
		return this;
	}

	@Override
	public ByteBuffer get(byte[] dst, int offset, int length) {
		if(position + length > limit) throw Buffer.makeIOOBE(position + length - 1);
		if(offset < 0) throw Buffer.makeIOOBE(offset);
		if(offset + length > dst.length) throw Buffer.makeIOOBE(offset + length - 1);
		WASMGCDirectArrayCopy.memcpy(dst, offset, address.add(position), length);
		position += length;
		return this;
	}

	@Override
	public ByteBuffer get(byte[] dst) {
		int dstLen = dst.length;
		if(position + dstLen > limit) throw Buffer.makeIOOBE(position + dstLen - 1);
		WASMGCDirectArrayCopy.memcpy(dst, 0, address.add(position), dstLen);
		position += dstLen;
		return this;
	}

	@Override
	public ByteBuffer put(ByteBuffer src) {
		if(src instanceof DirectMallocByteBuffer) {
			DirectMallocByteBuffer c = (DirectMallocByteBuffer)src;
			int l = c.limit - c.position;
			if(position + l > limit) throw Buffer.makeIOOBE(position + l - 1);
			Address.moveMemoryBlock(c.address.add(c.position), address.add(position), l);
			position += l;
			c.position += l;
		}else {
			int l = src.remaining();
			if(position + l > limit) throw Buffer.makeIOOBE(position + l - 1);
			Address addrBase = address.add(position);
			for(int i = 0; i < l; ++i) {
				addrBase.add(i).putByte(src.get());
			}
			position += l;
		}
		return this;
	}

	@Override
	public ByteBuffer put(byte[] src, int offset, int length) {
		if(position + length > limit) throw Buffer.makeIOOBE(position + length - 1);
		if(offset < 0) throw Buffer.makeIOOBE(offset);
		if(offset + length > src.length) throw Buffer.makeIOOBE(offset + length - 1);
		WASMGCDirectArrayCopy.memcpy(address.add(position), src, offset, length);
		position += length;
		return this;
	}

	@Override
	public ByteBuffer put(byte[] src) {
		int srcLen = src.length;
		if(position + srcLen > limit) throw Buffer.makeIOOBE(position + srcLen - 1);
		WASMGCDirectArrayCopy.memcpy(address.add(position), src, 0, srcLen);
		position += srcLen;
		return this;
	}

	@Override
	public char getChar() {
		if(position + 2 > limit) throw Buffer.makeIOOBE(position);
		char c = address.add(position).getChar();
		position += 2;
		return c;
	}

	@Override
	public ByteBuffer putChar(char value) {
		if(position + 2 > limit) throw Buffer.makeIOOBE(position);
		address.add(position).putChar(value);
		position += 2;
		return this;
	}

	@Override
	public char getChar(int index) {
		if(index < 0 || index + 2 > limit) throw Buffer.makeIOOBE(index);
		return address.add(index).getChar();
	}

	@Override
	public ByteBuffer putChar(int index, char value) {
		if(index < 0 || index + 2 > limit) throw Buffer.makeIOOBE(index);
		address.add(index).putChar(value);
		return this;
	}

	@Override
	public short getShort() {
		if(position + 2 > limit) throw Buffer.makeIOOBE(position);
		short s = address.add(position).getShort();
		position += 2;
		return s;
	}

	@Override
	public ByteBuffer putShort(short value) {
		if(position + 2 > limit) throw Buffer.makeIOOBE(position);
		address.add(position).putShort(value);
		position += 2;
		return this;
	}

	@Override
	public short getShort(int index) {
		if(index < 0 || index + 2 > limit) throw Buffer.makeIOOBE(index);
		return address.add(index).getShort();
	}

	@Override
	public ByteBuffer putShort(int index, short value) {
		if(index < 0 || index + 2 > limit) throw Buffer.makeIOOBE(index);
		address.add(index).putShort(value);
		return this;
	}

	@Override
	public ShortBuffer asShortBuffer() {
		return new DirectMallocShortBuffer(address, capacity >> 1, false);
	}

	@Override
	public int getInt() {
		if(position + 4 > limit) throw Buffer.makeIOOBE(position);
		int i = address.add(position).getInt();
		position += 4;
		return i;
	}

	@Override
	public ByteBuffer putInt(int value) {
		if(position + 4 > limit) throw Buffer.makeIOOBE(position);
		address.add(position).putInt(value);
		position += 4;
		return this;
	}

	@Override
	public int getInt(int index) {
		if(index < 0 || index + 4 > limit) throw Buffer.makeIOOBE(index);
		return address.add(index).getInt();
	}

	@Override
	public ByteBuffer putInt(int index, int value) {
		if(index < 0 || index + 4 > limit) throw Buffer.makeIOOBE(index);
		address.add(index).putInt(value);
		return this;
	}

	@Override
	public IntBuffer asIntBuffer() {
		return new DirectMallocIntBuffer(address, capacity >> 2, false);
	}

	@Override
	public long getLong() {
		if(position + 8 > limit) throw Buffer.makeIOOBE(position);
		long l = address.add(position).getLong();
		position += 8;
		return l;
	}

	@Override
	public ByteBuffer putLong(long value) {
		if(position + 8 > limit) throw Buffer.makeIOOBE(position);
		address.add(position).putLong(value);
		position += 8;
		return this;
	}

	@Override
	public long getLong(int index) {
		if(index < 0 || index + 8 > limit) throw Buffer.makeIOOBE(index);
		return address.add(index).getLong();
	}

	@Override
	public ByteBuffer putLong(int index, long value) {
		if(index < 0 || index + 8 > limit) throw Buffer.makeIOOBE(index);
		address.add(index).putLong(value);
		return this;
	}

	@Override
	public float getFloat() {
		if(position + 4 > limit) throw Buffer.makeIOOBE(position);
		float f = address.add(position).getFloat();
		position += 4;
		return f;
	}

	@Override
	public ByteBuffer putFloat(float value) {
		if(position + 4 > limit) throw Buffer.makeIOOBE(position);
		address.add(position).putFloat(value);
		position += 4;
		return this;
	}

	@Override
	public float getFloat(int index) {
		if(index < 0 || index + 4 > limit) throw Buffer.makeIOOBE(index);
		return address.add(index).getFloat();
	}

	@Override
	public ByteBuffer putFloat(int index, float value) {
		if(index < 0 || index + 4 > limit) throw Buffer.makeIOOBE(index);
		address.add(index).putFloat(value);
		return this;
	}

	@Override
	public FloatBuffer asFloatBuffer() {
		return new DirectMallocFloatBuffer(address, capacity >> 2, false);
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