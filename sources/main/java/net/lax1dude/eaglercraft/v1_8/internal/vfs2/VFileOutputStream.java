package net.lax1dude.eaglercraft.v1_8.internal.vfs2;

import java.io.IOException;

import net.lax1dude.eaglercraft.v1_8.EaglerOutputStream;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformFilesystem;
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
class VFileOutputStream extends EaglerOutputStream {

	private final VFile2 vfsFile;
	private boolean closed = false;

	VFileOutputStream(VFile2 vfsFile) {
		super(256);
		this.vfsFile = vfsFile;
	}

	@Override
	public void close() throws IOException {
		if(!closed) {
			closed = true;
			ByteBuffer copyBuffer = PlatformRuntime.allocateByteBuffer(count);
			try {
				copyBuffer.put(buf, 0, count);
				copyBuffer.flip();
				try {
					PlatformFilesystem.eaglerWrite(vfsFile.path, copyBuffer);
				}catch(Throwable t) {
					throw new IOException("Could not write stream contents to file!", t);
				}
			}finally {
				PlatformRuntime.freeByteBuffer(copyBuffer);
			}
		}
	}

}
