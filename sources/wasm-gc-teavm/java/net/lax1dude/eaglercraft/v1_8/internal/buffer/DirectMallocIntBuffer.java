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

public class DirectMallocIntBuffer extends IntBuffer {

	final Address address;
	final boolean original;

	private final int capacity;
	private int position;
	private int limit;
	private int mark;

	private static final int SHIFT = 2;

	DirectMallocIntBuffer(Address address, int capacity, boolean original) {
		this(address, capacity, 0, capacity, -1, original);
	}

	DirectMallocIntBuffer(Address address, int capacity, int position, int limit, int mark, boolean original) {
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
	public int[] array() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IntBuffer duplicate() {
		return new DirectMallocIntBuffer(address, capacity, position, limit, mark, false);
	}

	@Override
	public int get() {
		if(position >= limit) throw Buffer.makeIOOBE(position);
		return address.add((position++) << SHIFT).getInt();
	}

	@Override
	public IntBuffer put(int b) {
		if(position >= limit) throw Buffer.makeIOOBE(position);
		address.add((position++) << SHIFT).putInt(b);
		return this;
	}

	@Override
	public int get(int index) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		return address.add(index << SHIFT).getInt();
	}

	@Override
	public IntBuffer put(int index, int b) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		address.add(index << SHIFT).putInt(b);
		return this;
	}

	@Override
	public int getElement(int index) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		return address.add(index << SHIFT).getInt();
	}

	@Override
	public void putElement(int index, int value) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		address.add(index << SHIFT).putInt(value);
	}

	@Override
	public IntBuffer get(int[] dst, int offset, int length) {
		if(position + length > limit) throw Buffer.makeIOOBE(position + length - 1);
		if(offset < 0) throw Buffer.makeIOOBE(offset);
		if(offset + length > dst.length) throw Buffer.makeIOOBE(offset + length - 1);
		WASMGCDirectArrayCopy.memcpy(dst, offset, address.add(position << SHIFT), length);
		position += length;
		return this;
	}

	@Override
	public IntBuffer get(int[] dst) {
		int dstLen = dst.length;
		if(position + dstLen > limit) throw Buffer.makeIOOBE(position + dstLen - 1);
		WASMGCDirectArrayCopy.memcpy(dst, 0, address.add(position << SHIFT), dstLen);
		position += dstLen;
		return this;
	}

	@Override
	public IntBuffer put(IntBuffer src) {
		if(src instanceof DirectMallocIntBuffer) {
			DirectMallocIntBuffer c = (DirectMallocIntBuffer)src;
			int l = c.limit - c.position;
			if(position + l > limit) throw Buffer.makeIOOBE(position + l - 1);
			Address.moveMemoryBlock(c.address.add(c.position << SHIFT), address.add(position << SHIFT), l << SHIFT);
			position += l;
			c.position += l;
		}else {
			int l = src.remaining();
			if(position + l > limit) throw Buffer.makeIOOBE(position + l - 1);
			Address addrBase = address.add(position << SHIFT);
			for(int i = 0, ll = l << SHIFT; i < ll; i += 4) {
				addrBase.add(i).putInt(src.get());
			}
			position += l;
		}
		return this;
	}

	@Override
	public IntBuffer put(int[] src, int offset, int length) {
		if(position + length > limit) throw Buffer.makeIOOBE(position + length - 1);
		if(offset < 0) throw Buffer.makeIOOBE(offset);
		if(offset + length > src.length) throw Buffer.makeIOOBE(offset + length - 1);
		WASMGCDirectArrayCopy.memcpy(address.add(position << SHIFT), src, offset, length);
		position += length;
		return this;
	}

	@Override
	public IntBuffer put(int[] src) {
		int srcLen = src.length;
		if(position + srcLen > limit) throw Buffer.makeIOOBE(position + srcLen - 1);
		WASMGCDirectArrayCopy.memcpy(address.add(position << SHIFT), src, 0, srcLen);
		position += src.length;
		return this;
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
		if(m < 0) throw new IndexOutOfBoundsException("Invalid mark: " + m);
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
		if(newLimit < 0 || newLimit > capacity) throw Buffer.makeIOOBE(newLimit);
		limit = newLimit;
		return this;
	}

	@Override
	public IntBuffer position(int newPosition) {
		if(newPosition < 0 || newPosition > limit) throw Buffer.makeIOOBE(newPosition);
		position = newPosition;
		return this;
	}

}