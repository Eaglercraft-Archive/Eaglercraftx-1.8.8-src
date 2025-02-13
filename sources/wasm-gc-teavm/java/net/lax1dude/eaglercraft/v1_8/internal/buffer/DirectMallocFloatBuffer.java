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
import org.teavm.interop.DirectMalloc;

public class DirectMallocFloatBuffer extends FloatBuffer {

	final Address address;
	final boolean original;

	private final int capacity;
	private int position;
	private int limit;
	private int mark;

	private static final int SHIFT = 2;

	DirectMallocFloatBuffer(Address address, int capacity, boolean original) {
		this(address, capacity, 0, capacity, -1, original);
	}

	DirectMallocFloatBuffer(Address address, int capacity, int position, int limit, int mark, boolean original) {
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
	public float[] array() {
		throw new UnsupportedOperationException();
	}

	@Override
	public FloatBuffer duplicate() {
		return new DirectMallocFloatBuffer(address, capacity, position, limit, mark, false);
	}

	@Override
	public float get() {
		if(position >= limit) throw Buffer.makeIOOBE(position);
		return address.add((position++) << SHIFT).getFloat();
	}

	@Override
	public FloatBuffer put(float b) {
		if(position >= limit) throw Buffer.makeIOOBE(position);
		address.add((position++) << SHIFT).putFloat(b);
		return this;
	}

	@Override
	public float get(int index) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		return address.add(index << SHIFT).getFloat();
	}

	@Override
	public FloatBuffer put(int index, float b) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		address.add(index << SHIFT).putFloat(b);
		return this;
	}

	@Override
	public float getElement(int index) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		return address.add(index << SHIFT).getFloat();
	}

	@Override
	public void putElement(int index, float value) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		address.add(index << SHIFT).putFloat(value);
	}

	@Override
	public FloatBuffer get(float[] dst, int offset, int length) {
		if(position + length > limit) throw Buffer.makeIOOBE(position + length - 1);
		if(offset < 0) throw Buffer.makeIOOBE(offset);
		if(offset + length > dst.length) throw Buffer.makeIOOBE(offset + length - 1);
		WASMGCDirectArrayCopy.memcpy(dst, offset, address.add(position << SHIFT), length);
		position += length;
		return this;
	}

	@Override
	public FloatBuffer get(float[] dst) {
		int dstLen = dst.length;
		if(position + dstLen > limit) throw Buffer.makeIOOBE(position + dstLen - 1);
		WASMGCDirectArrayCopy.memcpy(dst, 0, address.add(position << SHIFT), dstLen);
		position += dstLen;
		return this;
	}

	@Override
	public FloatBuffer put(FloatBuffer src) {
		if(src instanceof DirectMallocFloatBuffer) {
			DirectMallocFloatBuffer c = (DirectMallocFloatBuffer)src;
			int l = c.limit - c.position;
			if(position + l > limit) throw Buffer.makeIOOBE(position + l - 1);
			DirectMalloc.memcpy(address.add(position << SHIFT), c.address.add(c.position << SHIFT), l << SHIFT);
			position += l;
			c.position += l;
		}else {
			int l = src.remaining();
			if(position + l > limit) throw Buffer.makeIOOBE(position + l - 1);
			Address addrBase = address.add(position << SHIFT);
			for(int i = 0, ll = l << SHIFT; i < ll; i += 4) {
				addrBase.add(i).putFloat(src.get());
			}
			position += l;
		}
		return this;
	}

	@Override
	public FloatBuffer put(float[] src, int offset, int length) {
		if(position + length > limit) throw Buffer.makeIOOBE(position + length - 1);
		if(offset < 0) throw Buffer.makeIOOBE(offset);
		if(offset + length > src.length) throw Buffer.makeIOOBE(offset + length - 1);
		WASMGCDirectArrayCopy.memcpy(address.add(position << SHIFT), src, offset, length);
		position += length;
		return this;
	}

	@Override
	public FloatBuffer put(float[] src) {
		if(position + src.length > limit) throw Buffer.makeIOOBE(position + src.length - 1);
		WASMGCDirectArrayCopy.memcpy(address.add(position << SHIFT), src, 0, src.length);
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