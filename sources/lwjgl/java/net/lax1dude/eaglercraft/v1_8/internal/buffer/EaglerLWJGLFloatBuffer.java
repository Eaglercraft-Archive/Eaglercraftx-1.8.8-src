package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import net.lax1dude.unsafememcpy.UnsafeMemcpy;
import net.lax1dude.unsafememcpy.UnsafeUtils;

/**
 * Copyright (c) 2022-2024 lax1dude. All Rights Reserved.
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
public class EaglerLWJGLFloatBuffer implements FloatBuffer {

	final long address;
	final boolean original;

	private final int capacity;
	private int position;
	private int limit;
	private int mark;

	private static final int SHIFT = 2;

	EaglerLWJGLFloatBuffer(long address, int capacity, boolean original) {
		this(address, capacity, 0, capacity, -1, original);
	}

	EaglerLWJGLFloatBuffer(long address, int capacity, int position, int limit, int mark, boolean original) {
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
		return new EaglerLWJGLFloatBuffer(address, capacity, position, limit, mark, false);
	}

	@Override
	public float get() {
		if(position >= limit) throw Buffer.makeIOOBE(position);
		return UnsafeUtils.getMemFloat(address + ((position++) << SHIFT));
	}

	@Override
	public FloatBuffer put(float b) {
		if(position >= limit) throw Buffer.makeIOOBE(position);
		UnsafeUtils.setMemFloat(address + ((position++) << SHIFT), b);
		return this;
	}

	@Override
	public float get(int index) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		return UnsafeUtils.getMemFloat(address + (index << SHIFT));
	}

	@Override
	public FloatBuffer put(int index, float b) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		UnsafeUtils.setMemFloat(address + (index << SHIFT), b);
		return this;
	}

	@Override
	public float getElement(int index) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		return UnsafeUtils.getMemFloat(address + (index << SHIFT));
	}

	@Override
	public void putElement(int index, float value) {
		if(index < 0 || index >= limit) throw Buffer.makeIOOBE(index);
		UnsafeUtils.setMemFloat(address + (index << SHIFT), value);
	}

	@Override
	public FloatBuffer get(float[] dst, int offset, int length) {
		if(position + length > limit) throw Buffer.makeIOOBE(position + length - 1);
		UnsafeMemcpy.memcpyAlignDst(dst, offset << SHIFT, address + (position << SHIFT), length);
		position += length;
		return this;
	}

	@Override
	public FloatBuffer get(float[] dst) {
		if(position + dst.length > limit) throw Buffer.makeIOOBE(position + dst.length - 1);
		UnsafeMemcpy.memcpyAlignDst(dst, 0, address + (position << SHIFT), dst.length);
		position += dst.length;
		return this;
	}

	@Override
	public FloatBuffer put(FloatBuffer src) {
		if(src instanceof EaglerLWJGLFloatBuffer) {
			EaglerLWJGLFloatBuffer c = (EaglerLWJGLFloatBuffer)src;
			int l = c.limit - c.position;
			if(position + l > limit) throw Buffer.makeIOOBE(position + l - 1);
			UnsafeMemcpy.memcpy(address + (position << SHIFT), c.address + (c.position << SHIFT), l << SHIFT);
			position += l;
			c.position += l;
		}else {
			int l = src.remaining();
			if(position + l > limit) throw Buffer.makeIOOBE(position + l - 1);
			for(int i = 0; i < l; ++i) {
				UnsafeUtils.setMemFloat(address + ((position + i) << SHIFT), src.get());
			}
			position += l;
		}
		return this;
	}

	@Override
	public FloatBuffer put(float[] src, int offset, int length) {
		if(position + length > limit) throw Buffer.makeIOOBE(position + length - 1);
		UnsafeMemcpy.memcpyAlignSrc(address + (position << SHIFT), src, offset << SHIFT, length);
		position += length;
		return this;
	}

	@Override
	public FloatBuffer put(float[] src) {
		if(position + src.length > limit) throw Buffer.makeIOOBE(position + src.length - 1);
		UnsafeMemcpy.memcpyAlignSrc(address + (position << SHIFT), src, 0, src.length);
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
