/*
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

package net.lax1dude.eaglercraft.v1_8.socket.protocol.client;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import net.lax1dude.eaglercraft.v1_8.DecoderException;
import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePacketInputBuffer;
import net.minecraft.network.PacketBuffer;

public class PacketBufferInputWrapper implements GamePacketInputBuffer {

	protected PacketBuffer buffer;

	public PacketBufferInputWrapper(PacketBuffer buffer) {
		this.buffer = buffer;
	}

	public PacketBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(PacketBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		try {
			buffer.readBytes(b);
		}catch(IndexOutOfBoundsException ex) {
			throw new EOFException();
		}
	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		try {
			buffer.readBytes(b, off, len);
		}catch(IndexOutOfBoundsException ex) {
			throw new EOFException();
		}
	}

	@Override
	public int skipBytes(int n) throws IOException {
		int r = buffer.readableBytes();
		if(n > r) {
			n = r;
		}
		buffer.readerIndex(buffer.readerIndex() + n);
		return n;
	}

	@Override
	public boolean readBoolean() throws IOException {
		try {
			return buffer.readBoolean();
		}catch(IndexOutOfBoundsException ex) {
			throw new EOFException();
		}
	}

	@Override
	public byte readByte() throws IOException {
		try {
			return buffer.readByte();
		}catch(IndexOutOfBoundsException ex) {
			throw new EOFException();
		}
	}

	@Override
	public int readUnsignedByte() throws IOException {
		try {
			return buffer.readUnsignedByte();
		}catch(IndexOutOfBoundsException ex) {
			throw new EOFException();
		}
	}

	@Override
	public short readShort() throws IOException {
		try {
			return buffer.readShort();
		}catch(IndexOutOfBoundsException ex) {
			throw new EOFException();
		}
	}

	@Override
	public int readUnsignedShort() throws IOException {
		try {
			return buffer.readUnsignedShort();
		}catch(IndexOutOfBoundsException ex) {
			throw new EOFException();
		}
	}

	@Override
	public char readChar() throws IOException {
		try {
			return buffer.readChar();
		}catch(IndexOutOfBoundsException ex) {
			throw new EOFException();
		}
	}

	@Override
	public int readInt() throws IOException {
		try {
			return buffer.readInt();
		}catch(IndexOutOfBoundsException ex) {
			throw new EOFException();
		}
	}

	@Override
	public long readLong() throws IOException {
		try {
			return buffer.readLong();
		}catch(IndexOutOfBoundsException ex) {
			throw new EOFException();
		}
	}

	@Override
	public float readFloat() throws IOException {
		try {
			return buffer.readFloat();
		}catch(IndexOutOfBoundsException ex) {
			throw new EOFException();
		}
	}

	@Override
	public double readDouble() throws IOException {
		try {
			return buffer.readDouble();
		}catch(IndexOutOfBoundsException ex) {
			throw new EOFException();
		}
	}

	@Override
	public String readLine() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String readUTF() throws IOException {
		return DataInputStream.readUTF(this);
	}

	@Override
	public void skipAllBytes(int n) throws IOException {
		if(buffer.readableBytes() < n) {
			throw new EOFException();
		}
		buffer.readerIndex(buffer.readerIndex() + n);
	}

	@Override
	public int readVarInt() throws IOException {
		try {
			return buffer.readVarIntFromBuffer();
		}catch(IndexOutOfBoundsException ex) {
			throw new EOFException();
		}
	}

	@Override
	public long readVarLong() throws IOException {
		try {
			return buffer.readVarLong();
		}catch(IndexOutOfBoundsException ex) {
			throw new EOFException();
		}
	}

	@Override
	public String readStringMC(int maxLen) throws IOException {
		try {
			return buffer.readStringFromBuffer(maxLen);
		}catch(DecoderException ex) {
			throw new IOException(ex.getMessage());
		}catch(IndexOutOfBoundsException ex) {
			throw new EOFException();
		}
	}

	@Override
	public String readStringEaglerASCII8() throws IOException {
		int len = readUnsignedByte();
		char[] ret = new char[len];
		for(int i = 0; i < len; ++i) {
			ret[i] = (char)readByte();
		}
		return new String(ret);
	}

	@Override
	public String readStringEaglerASCII16() throws IOException {
		int len = readUnsignedShort();
		char[] ret = new char[len];
		for(int i = 0; i < len; ++i) {
			ret[i] = (char)readByte();
		}
		return new String(ret);
	}

	@Override
	public byte[] readByteArrayMC() throws IOException {
		try {
			return buffer.readByteArray();
		}catch(IndexOutOfBoundsException ex) {
			throw new EOFException();
		}
	}

	@Override
	public int available() throws IOException {
		return buffer.readableBytes();
	}

	@Override
	public InputStream stream() {
		return new InputStream() {

			@Override
			public int read() throws IOException {
				if(buffer.readableBytes() > 0) {
					return buffer.readUnsignedShort();
				}else {
					return -1;
				}
			}

			@Override
			public int read(byte b[], int off, int len) throws IOException {
				int avail = buffer.readableBytes();
				if(avail == 0) return -1;
				len = avail > len ? avail : len;
				buffer.readBytes(b, off, len);
				return len;
			}

			@Override
			public long skip(long n) throws IOException {
				return PacketBufferInputWrapper.this.skipBytes((int)n);
			}

			@Override
			public int available() throws IOException {
				return buffer.readableBytes();
			}

			@Override
		    public boolean markSupported() {
		        return true;
		    }

			@Override
			public synchronized void mark(int readlimit) {
				buffer.markReaderIndex();
			}

			@Override
		    public synchronized void reset() throws IOException {
				try {
					buffer.resetReaderIndex();
				}catch(IndexOutOfBoundsException ex) {
					throw new EOFException();
				}
			}

		};
	}

	@Override
	public byte[] toByteArray() throws IOException {
		byte[] ret = new byte[buffer.readableBytes()];
		buffer.readBytes(ret);
		return ret;
	}

}