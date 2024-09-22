package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.io.IOException;
import java.io.InputStream;

import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Int8Array;
import org.teavm.jso.typedarrays.Uint8Array;

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
public class ArrayBufferInputStream extends InputStream {

	private int mark = -1;
	private int position;
	private int limit;
	private final ArrayBuffer buffer;
	private final Uint8Array typed;

	public ArrayBufferInputStream(ArrayBuffer bufferIn) {
		this(bufferIn, 0, bufferIn.getByteLength());
	}

	public ArrayBufferInputStream(ArrayBuffer bufferIn, int off, int len) {
		if(off + len > bufferIn.getByteLength()) {
			throw new IllegalArgumentException("offset " + off + " and length " + len + " are out of bounds for a "
					+ bufferIn.getByteLength() + " long arraybuffer");
		}
		buffer = bufferIn;
		typed = Uint8Array.create(bufferIn);
		position = off;
		limit = off + len;
	}

	@Override
	public int read() {
		if(position >= limit) {
			return -1;
		}
		return typed.get(position++);
	}

	@Override
	public int read(byte b[], int off, int len) {
		if(off + len > b.length) {
			throw new ArrayIndexOutOfBoundsException("offset " + off + " and length " + len
					+ " are out of bounds for a " + b.length + " array");
		}
		
		int avail = limit - position;
		if(len > avail) {
			len = avail;
		}
		
		if(len <= 0) {
			return -1;
		}
		
		TeaVMUtils.unwrapArrayBufferView(b).set(Int8Array.create(buffer, position, len), off);
		
		position += len;
		
		return len;
	}

	@Override
	public long skip(long n) {
		int avail = limit - position;
		if(n > avail) {
			n = avail;
		}
		position += (int)n;
		return n;
	}

	@Override
	public int available() {
		return limit - position;
	}

	public int getPosition() {
		return position;
	}

	public int getLimit() {
		return limit;
	}

	public ArrayBuffer getBuffer() {
		return buffer;
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public synchronized void mark(int readlimit) {
		mark = position;
	}

	@Override
	public synchronized void reset() throws IOException {
		if(mark == -1) {
			throw new IOException("Cannot reset, stream has no mark!");
		}
		position = mark;
	}
}
