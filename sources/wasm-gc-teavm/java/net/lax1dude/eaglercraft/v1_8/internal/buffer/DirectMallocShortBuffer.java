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

public class DirectMallocShortBuffer extends ShortBuffer {

	final Address address;
	final boolean original;

	private final int capacity;
	private int position;
	private int limit;
	private int mark;

	private static final int SHIFT = 1;

	DirectMallocShortBuffer(Address address, int capacity, boolean original) {
		this(address, capacity, 0, capacity, -1, original);
	}

	DirectMallocShortBuffer(Address address, int capacity, int position, int limit, int mark, boolean original) {
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
	public short[] array() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ShortBuffer duplicate() {
		return new DirectMallocShortBuffer(address, capacity, position, limit, mark, false);
	}

	@Override
	public short get() {
		if(position >= limit) throw Buffer.makeIOOBE(position);
		return address.add((position++) << SHIFT).getShort();
	}

	@Override
	public ShortBuffer put(short b) {
		if(position >= limit) throw Buffer.makeIOOBE(position);
		address.add((position++) << SHIFT).putShort(b);
		return this;
	}

	@Override
	public short get(int index) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		return address.add(index << SHIFT).getShort();
	}

	@Override
	public ShortBuffer put(int index, short b) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		address.add(index << SHIFT).putShort(b);
		return this;
	}

	@Override
	public short getElement(int index) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		return address.add(index << SHIFT).getShort();
	}

	@Override
	public void putElement(int index, short value) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		address.add(index << SHIFT).putShort(value);
	}

	@Override
	public ShortBuffer get(short[] dst, int offset, int length) {
		if(position + length > limit) throw Buffer.makeIOOBE(position + length - 1);
		if(offset < 0) throw Buffer.makeIOOBE(offset);
		if(offset + length > dst.length) throw Buffer.makeIOOBE(offset + length - 1);
		WASMGCDirectArrayCopy.memcpy(dst, offset, address.add(position << SHIFT), length);
		position += length;
		return this;
	}

	@Override
	public ShortBuffer get(short[] dst) {
		int dstLen = dst.length;
		if(position + dstLen > limit) throw Buffer.makeIOOBE(position + dstLen - 1);
		WASMGCDirectArrayCopy.memcpy(dst, 0, address.add(position << SHIFT), dstLen);
		position += dstLen;
		return this;
	}

	@Override
	public ShortBuffer put(ShortBuffer src) {
		if(src instanceof DirectMallocShortBuffer) {
			DirectMallocShortBuffer c = (DirectMallocShortBuffer)src;
			int l = c.limit - c.position;
			if(position + l > limit) throw Buffer.makeIOOBE(position + l - 1);
			DirectMalloc.memcpy(address.add(position << SHIFT), c.address.add(c.position << SHIFT), l << SHIFT);
			position += l;
			c.position += l;
		}else {
			int l = src.remaining();
			if(position + l > limit) throw Buffer.makeIOOBE(position + l - 1);
			Address addrBase = address.add(position << SHIFT);
			for(int i = 0, ll = l << SHIFT; i < ll; i += 2) {
				addrBase.add(i).putShort(src.get());
			}
			position += l;
		}
		return this;
	}

	@Override
	public ShortBuffer put(short[] src, int offset, int length) {
		if(position + length > limit) throw Buffer.makeIOOBE(position + length - 1);
		if(offset < 0) throw Buffer.makeIOOBE(offset);
		if(offset + length > src.length) throw Buffer.makeIOOBE(offset + length - 1);
		WASMGCDirectArrayCopy.memcpy(address.add(position << SHIFT), src, offset, length);
		position += length;
		return this;
	}

	@Override
	public ShortBuffer put(short[] src) {
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