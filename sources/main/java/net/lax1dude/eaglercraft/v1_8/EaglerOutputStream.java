package net.lax1dude.eaglercraft.v1_8;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Copyright (c) 2024 lax1dude. All Rights Reserved.
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
public class EaglerOutputStream extends OutputStream {

	protected byte buf[];
	protected int count;

	public EaglerOutputStream() {
		this(32);
	}

	public EaglerOutputStream(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Negative initial size: " + size);
		}
		buf = new byte[size];
	}

	private void ensureCapacity(int minCapacity) {
		if (buf.length < minCapacity) {
			minCapacity = Math.max(minCapacity, buf.length * 3 / 2);
			buf = Arrays.copyOf(buf, minCapacity);
		}
	}

	public void write(int b) {
		ensureCapacity(count + 1);
		buf[count] = (byte) b;
		count += 1;
	}

	public void write(byte b[], int off, int len) {
		ensureCapacity(count + len);
		System.arraycopy(b, off, buf, count, len);
		count += len;
	}

	public void writeBytes(byte b[]) {
		write(b, 0, b.length);
	}

	public void writeTo(OutputStream out) throws IOException {
		out.write(buf, 0, count);
	}

	public void reset() {
		count = 0;
	}

	public byte[] toByteArray() {
		return Arrays.copyOf(buf, count);
	}

	public int size() {
		return count;
	}

	public void skipBytes(int num) {
		ensureCapacity(count + num);
		count += num;
	}

	public void close() throws IOException {
	}
}
