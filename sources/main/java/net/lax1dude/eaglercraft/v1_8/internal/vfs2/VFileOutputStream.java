/*
 * Copyright (c) 2023-2025 lax1dude. All Rights Reserved.
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

package net.lax1dude.eaglercraft.v1_8.internal.vfs2;

import java.io.IOException;
import java.io.OutputStream;

import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;

class VFileOutputStream extends OutputStream {

	private final VFile2 vfsFile;
	private ByteBuffer buffer;

	VFileOutputStream(VFile2 vfsFile) {
		this.buffer = PlatformRuntime.allocateByteBuffer(256);
		this.vfsFile = vfsFile;
	}

	@Override
	public void write(int b) throws IOException {
		if(buffer == null) throw new IOException("File is closed!");
		if(buffer.remaining() < 1) {
			buffer.flip();
			ByteBuffer buf = PlatformRuntime.allocateByteBuffer(buffer.limit() << 1);
			buf.put(buffer);
			PlatformRuntime.freeByteBuffer(buffer);
			buffer = buf;
		}
		buffer.put((byte)(b & 0xFF));
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if(buffer == null) throw new IOException("File is closed!");
		if(buffer.remaining() < len) {
			buffer.flip();
			int oldLen = buffer.limit();
			int newLen = oldLen;
			do {
				newLen <<= 1;
			}while(newLen < oldLen + len);
			ByteBuffer buf = PlatformRuntime.allocateByteBuffer(newLen);
			buf.put(buffer);
			PlatformRuntime.freeByteBuffer(buffer);
			buffer = buf;
		}
		buffer.put(b, off, len);
	}

	@Override
	public void close() throws IOException {
		if(buffer != null) {
			buffer.flip();
			try {
				vfsFile.getFS().eaglerWrite(vfsFile.path, buffer);
			}catch(Throwable t) {
				throw new IOException("Could not write stream contents to file!", t);
			}finally {
				PlatformRuntime.freeByteBuffer(buffer);
				buffer = null;
			}
		}
	}

}