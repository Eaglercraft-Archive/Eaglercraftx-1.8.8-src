/*
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

package net.lax1dude.eaglercraft.v1_8.sp.ipc;

import java.io.IOException;
import java.io.InputStream;

public class IPCInputStream extends InputStream {
	
	private byte[] currentBuffer = null;
	private int idx = 0;
	private int markIDX = 0;
	private String errorName = null;
	
	public void feedBuffer(byte[] b) {
		currentBuffer = b;
		idx = 0;
		errorName = null;
		markIDX = 0;
	}
	
	public void nameBuffer(String str) {
		errorName = str;
	}

	@Override
	public int read() throws IOException {
		try {
			return ((int)currentBuffer[idx++]) & 0xFF;
		}catch(ArrayIndexOutOfBoundsException a) {
			throw new IOException("IPCInputStream buffer underflow" + (errorName == null ? "," : (" (while deserializing '" + errorName + "')")) + " no bytes remaining", a);
		}
	}
	
	@Override
	public int read(byte b[], int off, int len) throws IOException {
		if(idx + len > currentBuffer.length) {
			throw new IOException("IPCInputStream buffer underflow" + (errorName == null ? "," : (" (while deserializing '" + errorName + "')")) + " tried to read " + len + " when there are only " + (currentBuffer.length - idx) + " bytes remaining", new ArrayIndexOutOfBoundsException(idx + len - 1));
		}
		if(off + len > b.length) {
			throw new ArrayIndexOutOfBoundsException(off + len - 1);
		}
		System.arraycopy(currentBuffer, idx, b, off, len);
		idx += len;
		return len;
	}
	
	public void markIndex() {
		markIDX = idx;
	}
	
	public void rewindIndex() {
		idx = markIDX;
	}
	
	public byte[] getLeftover() {
		if(currentBuffer.length - idx <= 0) {
			return null;
		}
		
		byte[] buf = new byte[currentBuffer.length - idx];
		System.arraycopy(currentBuffer, idx, buf, 0, currentBuffer.length - idx);
		
		return buf;
	}
	
	public int getLeftoverCount() {
		return currentBuffer.length - idx;
	}

}