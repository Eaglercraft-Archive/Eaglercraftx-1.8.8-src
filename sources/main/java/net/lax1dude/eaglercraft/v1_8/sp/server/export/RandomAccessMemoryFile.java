package net.lax1dude.eaglercraft.v1_8.sp.server.export;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.IOException;

/**
 * Copyright (c) 2023-2024 lax1dude. All Rights Reserved.
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
public class RandomAccessMemoryFile implements DataInput, DataOutput {

	private byte[] buffer;
	private int length;
	private int pos;

	public RandomAccessMemoryFile(byte[] initialBuffer, int initialLength) {
		this.buffer = initialBuffer;
		this.length = initialLength;
		this.pos = 0;
	}

	private void grow(int newMaxSize) {
		if (length < newMaxSize) {
			if (buffer.length < newMaxSize) {
				byte[] newBuffer = new byte[newMaxSize | 0x7FFFF];
				System.arraycopy(buffer, 0, newBuffer, 0, length);
				buffer = newBuffer;
			}
			length = newMaxSize;
		}
	}

	public byte[] getByteArray() {
		byte[] b = new byte[length];
		System.arraycopy(buffer, 0, b, 0, length);
		return b;
	}

	public int read() throws IOException {
		return (pos < length) ? (buffer[pos++] & 0xff) : -1;
	}

	private int readBytes(byte b[], int off, int len) throws IOException {
		if (pos >= length) {
			return -1;
		}

		int avail = length - pos;
		if (len > avail) {
			len = avail;
		}
		if (len <= 0) {
			return 0;
		}
		System.arraycopy(buffer, pos, b, off, len);
		pos += len;
		return len;
	}

	public int read(byte b[], int off, int len) throws IOException {
		return readBytes(b, off, len);
	}

	public int read(byte b[]) throws IOException {
		return readBytes(b, 0, b.length);
	}

	public final void readFully(byte b[]) throws IOException {
		readFully(b, 0, b.length);
	}

	public final void readFully(byte b[], int off, int len) throws IOException {
		int n = 0;
		do {
			int count = this.read(b, off + n, len - n);
			if (count < 0)
				throw new EOFException();
			n += count;
		} while (n < len);
	}

	public int skipBytes(int n) throws IOException {
		int newpos;

		if (n <= 0) {
			return 0;
		}
		newpos = pos + n;
		if (newpos > length) {
			newpos = length;
		}
		seek(newpos);

		return (int) (newpos - pos);
	}

	public void write(int b) throws IOException {
		grow(pos + 1);
		buffer[pos] = (byte) b;
		pos += 1;
	}

	private void writeBytes(byte b[], int off, int len) throws IOException {
		grow(pos + len);
		System.arraycopy(b, off, buffer, pos, len);
		pos += len;
	}

	public void write(byte b[]) throws IOException {
		writeBytes(b, 0, b.length);
	}

	public void write(byte b[], int off, int len) throws IOException {
		writeBytes(b, off, len);
	}

	public void seek(int pos) {
		this.pos = pos;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int newLength) {
		grow(newLength);
	}

	public final boolean readBoolean() throws IOException {
		int ch = this.read();
		if (ch < 0)
			throw new EOFException();
		return (ch != 0);
	}

	public final byte readByte() throws IOException {
		int ch = this.read();
		if (ch < 0)
			throw new EOFException();
		return (byte) (ch);
	}

	public final int readUnsignedByte() throws IOException {
		int ch = this.read();
		if (ch < 0)
			throw new EOFException();
		return ch;
	}

	public final short readShort() throws IOException {
		int ch1 = this.read();
		int ch2 = this.read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (short) ((ch1 << 8) + (ch2 << 0));
	}

	public final int readUnsignedShort() throws IOException {
		int ch1 = this.read();
		int ch2 = this.read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (ch1 << 8) + (ch2 << 0);
	}

	public final char readChar() throws IOException {
		int ch1 = this.read();
		int ch2 = this.read();
		if ((ch1 | ch2) < 0)
			throw new EOFException();
		return (char) ((ch1 << 8) + (ch2 << 0));
	}

	public final int readInt() throws IOException {
		int ch1 = this.read();
		int ch2 = this.read();
		int ch3 = this.read();
		int ch4 = this.read();
		if ((ch1 | ch2 | ch3 | ch4) < 0)
			throw new EOFException();
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
	}

	public final long readLong() throws IOException {
		return ((long) (readInt()) << 32) + (readInt() & 0xFFFFFFFFL);
	}

	public final float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	public final double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	public final String readLine() throws IOException {
		StringBuilder input = new StringBuilder();
		int c = -1;
		boolean eol = false;

		while (!eol) {
			switch (c = read()) {
			case -1:
			case '\n':
				eol = true;
				break;
			case '\r':
				eol = true;
				int cur = pos;
				if ((read()) != '\n') {
					seek(cur);
				}
				break;
			default:
				input.append((char) c);
				break;
			}
		}

		if ((c == -1) && (input.length() == 0)) {
			return null;
		}
		return input.toString();
	}

	public final String readUTF() throws IOException {
		throw new IOException("TODO");
	}

	public final void writeBoolean(boolean v) throws IOException {
		write(v ? 1 : 0);
	}

	public final void writeByte(int v) throws IOException {
		write(v);
	}

	public final void writeShort(int v) throws IOException {
		write((v >>> 8) & 0xFF);
		write((v >>> 0) & 0xFF);
	}

	public final void writeChar(int v) throws IOException {
		write((v >>> 8) & 0xFF);
		write((v >>> 0) & 0xFF);
	}

	public final void writeInt(int v) throws IOException {
		write((v >>> 24) & 0xFF);
		write((v >>> 16) & 0xFF);
		write((v >>> 8) & 0xFF);
		write((v >>> 0) & 0xFF);
	}

	public final void writeLong(long v) throws IOException {
		write((int) (v >>> 56) & 0xFF);
		write((int) (v >>> 48) & 0xFF);
		write((int) (v >>> 40) & 0xFF);
		write((int) (v >>> 32) & 0xFF);
		write((int) (v >>> 24) & 0xFF);
		write((int) (v >>> 16) & 0xFF);
		write((int) (v >>> 8) & 0xFF);
		write((int) (v >>> 0) & 0xFF);
	}

	public final void writeFloat(float v) throws IOException {
		writeInt(Float.floatToIntBits(v));
	}

	public final void writeDouble(double v) throws IOException {
		writeLong(Double.doubleToLongBits(v));
	}

	public final void writeBytes(String s) throws IOException {
		int len = s.length();
		byte[] b = new byte[len];
		s.getBytes(0, len, b, 0);
		writeBytes(b, 0, len);
	}

	public final void writeChars(String s) throws IOException {
		int clen = s.length();
		int blen = 2 * clen;
		byte[] b = new byte[blen];
		char[] c = new char[clen];
		s.getChars(0, clen, c, 0);
		for (int i = 0, j = 0; i < clen; i++) {
			b[j++] = (byte) (c[i] >>> 8);
			b[j++] = (byte) (c[i] >>> 0);
		}
		writeBytes(b, 0, blen);
	}

	public final void writeUTF(String str) throws IOException {
		throw new IOException("TODO");
	}
}
