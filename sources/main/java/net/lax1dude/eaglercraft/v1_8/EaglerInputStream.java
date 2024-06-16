package net.lax1dude.eaglercraft.v1_8;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

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
public class EaglerInputStream extends InputStream {

	protected byte buf[];
	protected int pos;
	protected int mark = 0;
	protected int count;

	public EaglerInputStream(byte[] buf) {
		this.buf = buf;
		this.pos = 0;
		this.count = buf.length;
	}

	public EaglerInputStream(byte buf[], int offset, int length) {
		this.buf = buf;
		this.pos = offset;
		this.count = Math.min(offset + length, buf.length);
		this.mark = offset;
	}

	public int read() {
		return (pos < count) ? (buf[pos++] & 0xff) : -1;
	}

	public int read(byte b[], int off, int len) {
		if (pos >= count) {
			return -1;
		}

		int avail = count - pos;
		if (len > avail) {
			len = avail;
		}
		if (len <= 0) {
			return 0;
		}
		System.arraycopy(buf, pos, b, off, len);
		pos += len;
		return len;
	}

	public byte[] readAllBytes() {
		byte[] result = Arrays.copyOfRange(buf, pos, count);
		pos = count;
		return result;
	}

	public int readNBytes(byte[] b, int off, int len) {
		int n = read(b, off, len);
		return n == -1 ? 0 : n;
	}

	public long transferTo(OutputStream out) throws IOException {
		int len = count - pos;
		out.write(buf, pos, len);
		pos = count;
		return len;
	}

	public static byte[] inputStreamToBytesQuiet(InputStream is) {
		if (is == null) {
			return null;
		}
		try {
			return inputStreamToBytes(is);
		} catch (IOException ex) {
			return null;
		}
	}

	public long skip(long n) {
		long k = count - pos;
		if (n < k) {
			k = n < 0 ? 0 : n;
		}

		pos += k;
		return k;
	}

	public int available() {
		return count - pos;
	}

	public boolean markSupported() {
		return true;
	}

	public void mark(int readAheadLimit) {
		mark = pos;
	}

	public void reset() {
		pos = mark;
	}

	public void close() throws IOException {
	}

	public static byte[] inputStreamToBytes(InputStream is) throws IOException {
		try {
			if (is instanceof EaglerInputStream) {
				return ((EaglerInputStream) is).getAsArray();
			} else if (is instanceof ByteArrayInputStream) {
				byte[] ret = new byte[is.available()];
				is.read(ret);
				return ret;
			} else {
				EaglerOutputStream os = new EaglerOutputStream(1024);
				byte[] buf = new byte[1024];
				int i;
				while ((i = is.read(buf)) != -1) {
					os.write(buf, 0, i);
				}
				return os.toByteArray();
			}
		}finally {
			is.close();
		}
	}

	public byte[] getAsArray() {
		if (pos == 0 && count == buf.length) {
			return buf;
		} else {
			byte[] ret = new byte[count];
			System.arraycopy(buf, pos, ret, 0, count);
			return ret;
		}
	}

	public boolean canUseArrayDirectly() {
		return pos == 0 && count == buf.length;
	}

	public int getPosition() {
		return pos;
	}

	public int getMark() {
		return mark;
	}

	public int getCount() {
		return count;
	}

}
