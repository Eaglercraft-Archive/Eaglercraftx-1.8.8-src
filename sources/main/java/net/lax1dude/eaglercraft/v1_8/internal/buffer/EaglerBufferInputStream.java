package net.lax1dude.eaglercraft.v1_8.internal.buffer;

import java.io.IOException;
import java.io.InputStream;


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
public class EaglerBufferInputStream extends InputStream {
	
	private final ByteBuffer buffer;
	
	public EaglerBufferInputStream(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public int read() throws IOException {
		if(buffer.remaining() <= 0) {
			return -1;
		}
		return (int)buffer.get() & 0xFF;
	}

	@Override
	public int read(byte b[], int off, int len) throws IOException {
		int p = buffer.position();
		int l = buffer.limit();
		int r = l - p;
		if(r < len) {
			len = r;
		}
		if(len > 0) {
			buffer.get(b, off, len);
		}
		return len;
	}

	@Override
	public long skip(long n) throws IOException {
		int p = buffer.position();
		int l = buffer.limit();
		int r = l - p;
		if(r < n) {
			n = r;
		}
		if(n > 0) {
			buffer.position(p + (int)n);
		}
		return n;
	}

	@Override
	public int available() throws IOException {
		return buffer.remaining();
	}

}
