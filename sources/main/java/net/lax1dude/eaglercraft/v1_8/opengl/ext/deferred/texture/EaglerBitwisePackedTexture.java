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

package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import net.lax1dude.eaglercraft.v1_8.IOUtils;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

public class EaglerBitwisePackedTexture {

	private static int getFromBits(int idxx, int bits, byte[] bytes) {
		int startByte = idxx >> 3;
		int endByte = (idxx + bits - 1) >> 3;
		if(startByte == endByte) {
			return (((int)bytes[startByte] & 0xff) >> (8 - (idxx & 7) - bits)) & ((1 << bits) - 1);
		}else {
			return (((((int)bytes[startByte] & 0xff) << 8) | ((int)bytes[endByte] & 0xff)) >> (16 - (idxx & 7) - bits)) & ((1 << bits) - 1);
		}
	}

	private static int readByte(InputStream is) throws IOException {
		int i = is.read();
		if (i < 0) {
			throw new EOFException();
		}
		return i;
	}

	public static ImageData loadTexture(InputStream is) throws IOException {
		if(readByte(is) != '%' || readByte(is) != 'E' || readByte(is) != 'B' || readByte(is) != 'P') {
			throw new IOException("Not an EBP file!");
		}
		int v = readByte(is);
		if(v != 1) {
			throw new IOException("Unknown EBP version: " + v);
		}
		int c = readByte(is);
		if(c != 3 && c != 4) {
			throw new IOException("Invalid component count: " + c);
		}
		int w = readByte(is) | (readByte(is) << 8);
		int h = readByte(is) | (readByte(is) << 8);
		ImageData img = new ImageData(w, h, true);
		v = readByte(is);
		if(v == 0) {
			if(c == 3) {
				for(int i = 0, l = w * h; i < l; ++i) {
					img.pixels[i] = readByte(is) | (readByte(is) << 8) | (readByte(is) << 16) | 0xFF000000;
				}
			}else {
				for(int i = 0, l = w * h; i < l; ++i) {
					img.pixels[i] = readByte(is) | (readByte(is) << 8) | (readByte(is) << 16) | (readByte(is) << 24);
				}
			}
		}else if(v == 1) {
			int paletteSize = readByte(is) + 1;
			int[] palette = new int[paletteSize];
			palette[0] = 0xFF000000;
			if(c == 3) {
				for(int i = 1; i < paletteSize; ++i) {
					palette[i] = readByte(is) | (readByte(is) << 8) | (readByte(is) << 16) | 0xFF000000;
				}
			}else {
				for(int i = 1; i < paletteSize; ++i) {
					palette[i] = readByte(is) | (readByte(is) << 8) | (readByte(is) << 16) | (readByte(is) << 24);
				}
			}
			int bpp = readByte(is);
			byte[] readSet = new byte[readByte(is) | (readByte(is) << 8) | (readByte(is) << 16)];
			IOUtils.readFully(is, readSet);
			for(int i = 0, l = w * h; i < l; ++i) {
				img.pixels[i] = palette[getFromBits(i * bpp, bpp, readSet)];
			}
		}else {
			throw new IOException("Unknown EBP storage type: " + v);
		}
		if(readByte(is) != ':' || readByte(is) != '>') {
			throw new IOException("Invalid footer! (:>)");
		}
		return img;
	}

	public static ImageData loadTextureSafe(InputStream is) throws IOException {
		ImageData bufferedimage;
		try {
			bufferedimage = loadTexture(is);
		} finally {
			IOUtils.closeQuietly(is);
		}

		return bufferedimage;
	}

}