package net.lax1dude.eaglercraft.v1_8.sp.relay.server;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Copyright (c) 2022 lax1dude. All Rights Reserved.
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
public class ByteBufferInputStream extends InputStream {
	
	private final ByteBuffer buffer;
	
	public ByteBufferInputStream(ByteBuffer buf) {
		buffer = buf;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int max = buffer.remaining();
		if(len > max) {
			len = max;
		}
		buffer.get(b, off, len);
		return len;
	}
	
	@Override
	public int read() throws IOException {
		if(buffer.remaining() == 0) {
			return -1;
		}else {
			return (int)buffer.get() & 0xFF;
		}
	}

	@Override
	public long skip(long n) throws IOException {
		int max = buffer.remaining();
		if(n > max) {
			n = (int)max;
		}
		return max;
	}

	@Override
	public int available() throws IOException {
		return buffer.remaining();
	}

	@Override
	public synchronized void mark(int readlimit) {
		buffer.mark();
	}

	@Override
	public synchronized void reset() throws IOException {
		buffer.reset();
	}

	@Override
	public boolean markSupported() {
		return true;
	}

}
