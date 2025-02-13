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
import java.io.OutputStream;

public class IPCOutputStream extends OutputStream {

	private String className = null;
	private byte[] currentBuffer = null;
	private int idx = 0;
	private int originalSize = 0;
	
	public void feedBuffer(byte[] buf, String clazzName) {
		currentBuffer = buf;
		idx = 0;
		originalSize = buf.length;
		className = clazzName;
	}
	
	public byte[] returnBuffer() {
		if(className != null && currentBuffer.length != originalSize) {
			System.err.println("WARNING: Packet '" + className + "' was supposed to be " + originalSize + " bytes but buffer has grown by " + (currentBuffer.length - originalSize) + " to " + currentBuffer.length + " bytes");
		}
		return currentBuffer;
	}
	
	void growBuffer(int i) {
		int ii = currentBuffer.length;
		int iii = i - ii;
		if(iii > 0) {
			byte[] n = new byte[i];
			System.arraycopy(currentBuffer, 0, n, 0, ii);
			currentBuffer = n;
		}
	}

	@Override
	public void write(int b) throws IOException {
		if(idx >= currentBuffer.length) {
			growBuffer(idx + 1);
		}
		currentBuffer[idx++] = (byte) b;
	}
	
	@Override
	public void write(byte b[], int off, int len) throws IOException {
		if(idx + len > currentBuffer.length) {
			growBuffer(idx + len);
		}
		System.arraycopy(b, off, currentBuffer, idx, len);
		idx += len;
	}

}