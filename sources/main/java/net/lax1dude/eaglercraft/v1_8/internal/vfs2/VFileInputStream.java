package net.lax1dude.eaglercraft.v1_8.internal.vfs2;

import java.io.IOException;
import java.io.InputStream;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;

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
class VFileInputStream extends InputStream {

	private ByteBuffer fileBuffer;
	
	VFileInputStream(ByteBuffer buffer) {
		this.fileBuffer = buffer;
	}

	@Override
	public int read() throws IOException {
		if(fileBuffer == null) {
			throw new IOException("Stream is closed");
		}
		if(fileBuffer.remaining() <= 0) {
			return -1;
		}
		return (int)fileBuffer.get() & 0xFF;
	}

	@Override
	public int read(byte b[], int off, int len) throws IOException {
		if(fileBuffer == null) {
			throw new IOException("Stream is closed");
		}
		int p = fileBuffer.position();
		int l = fileBuffer.limit();
		int r = l - p;
		if(r < len) {
			len = r;
		}
		if(len > 0) {
			fileBuffer.get(b, off, len);
		}
		return len;
	}

	@Override
	public long skip(long n) throws IOException {
		if(fileBuffer == null) {
			throw new IOException("Stream is closed");
		}
		int p = fileBuffer.position();
		int l = fileBuffer.limit();
		int r = l - p;
		if(r < n) {
			n = r;
		}
		if(n > 0) {
			fileBuffer.position(p + (int)n);
		}
		return n;
	}

	@Override
	public int available() throws IOException {
		return fileBuffer == null ? -1 : fileBuffer.remaining();
	}

	@Override
	public void close() {
		if(fileBuffer != null) {
			PlatformRuntime.freeByteBuffer(fileBuffer);
			fileBuffer = null;
		}
	}
}
