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

package net.lax1dude.eaglercraft.v1_8.internal.teavm;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.internal.buffer.ByteBuffer;

public class PCMToWAVLoader {

	public static int getWAVLen(List<float[][]> data, boolean floating) {
		int i = 44;
		int j = floating ? 4 : 2;
		int k;
		for(float[][] f : data) {
			k = f.length;
			if(k == 0) continue;
			i += k * f[0].length * j;
		}
		return i;
	}

	public static void createWAV16(List<float[][]> data, int chCount, int sampleRate, ByteBuffer bufferOut) {
		if(chCount == 0 || data.isEmpty()) return;
		int finalSize = bufferOut.remaining();
		
		// header
		bufferOut.putInt(0x46464952); // magic
		bufferOut.putInt(finalSize - 8); // file len
		bufferOut.putInt(0x45564157); // magic
		
		// format chunk
		bufferOut.putInt(0x20746D66); // magic
		bufferOut.putInt(16); // format chunk len - 8
		bufferOut.putShort((short)1); // audio format = int
		bufferOut.putShort((short)chCount); // channels
		bufferOut.putInt(sampleRate); // sample rate
		bufferOut.putInt(sampleRate * chCount * 2); // bytes per second
		bufferOut.putShort((short)(chCount * 2)); // bytes per sample
		bufferOut.putShort((short)16); // bits per sample
		
		// data chunk
		bufferOut.putInt(0x61746164); // magic
		bufferOut.putInt(finalSize - 44);

		for(float[][] f : data) {
			for(int i = 0, l = f[0].length; i < l; ++i) {
				for(int c = 0; c < chCount; ++c) {
					int val = (int)(f[c][i] * 32767.0f);
					if (val > 32767) {
						val = 32767;
					}
					if (val < -32768) {
						val = -32768;
					}
					bufferOut.putShort((short)val);
				}
			}
		}
		
		if(bufferOut.hasRemaining()) {
			throw new IllegalStateException("Buffer was the wrong size! " + bufferOut.remaining() + " remaining");
		}
	}

	public static void createWAV32F(List<float[][]> data, int chCount, int sampleRate, ByteBuffer bufferOut) {
		if(chCount == 0 || data.isEmpty()) return;
		int finalSize = bufferOut.remaining();
		
		// header
		bufferOut.putInt(0x46464952); // magic
		bufferOut.putInt(finalSize - 8); // file len
		bufferOut.putInt(0x45564157); // magic
		
		// format chunk
		bufferOut.putInt(0x20746D66); // magic
		bufferOut.putInt(16); // format chunk len - 8
		bufferOut.putShort((short)3); // audio format = float
		bufferOut.putShort((short)chCount); // channels
		bufferOut.putInt(sampleRate); // sample rate
		bufferOut.putInt(sampleRate * chCount * 4); // bytes per second
		bufferOut.putShort((short)(chCount * 4)); // bytes per sample
		bufferOut.putShort((short)32); // bits per sample
		
		// data chunk
		bufferOut.putInt(0x61746164); // magic
		bufferOut.putInt(finalSize - 44);
		
		for(float[][] f : data) {
			for(int i = 0, l = f[0].length; i < l; ++i) {
				for(int c = 0; c < chCount; ++c) {
					bufferOut.putFloat(f[c][i]);
				}
			}
		}
		
		if(bufferOut.hasRemaining()) {
			throw new IllegalStateException("Buffer was the wrong size! " + finalSize + " " + bufferOut.remaining() + " remaining");
		}
	}

}