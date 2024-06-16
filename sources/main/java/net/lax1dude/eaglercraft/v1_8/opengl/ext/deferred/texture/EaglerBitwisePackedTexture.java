package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture;

import java.io.IOException;
import java.io.InputStream;

import net.lax1dude.eaglercraft.v1_8.IOUtils;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

/**
 * Copyright (c) 2023 lax1dude. All Rights Reserved.
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

	public static ImageData loadTexture(InputStream is, int alpha) throws IOException {
		if(is.read() != '%' || is.read() != 'E' || is.read() != 'B' || is.read() != 'P') {
			throw new IOException("Not an EBP file!");
		}
		int v = is.read();
		if(v != 1) {
			throw new IOException("Unknown EBP version: " + v);
		}
		v = is.read();
		if(v != 3) {
			throw new IOException("Invalid component count: " + v);
		}
		int w = is.read() | (is.read() << 8);
		int h = is.read() | (is.read() << 8);
		ImageData img = new ImageData(w, h, true);
		alpha <<= 24;
		v = is.read();
		if(v == 0) {
			for(int i = 0, l = w * h; i < l; ++i) {
				img.pixels[i] = is.read() | (is.read() << 8) | (is.read() << 16) | alpha;
			}
		}else if(v == 1) {
			int paletteSize = is.read();
			int[] palette = new int[paletteSize + 1];
			palette[0] = alpha;
			for(int i = 0; i < paletteSize; ++i) {
				palette[i + 1] = is.read() | (is.read() << 8) | (is.read() << 16) | alpha;
			}
			int bpp = is.read();
			byte[] readSet = new byte[is.read() | (is.read() << 8) | (is.read() << 16)];
			is.read(readSet);
			for(int i = 0, l = w * h; i < l; ++i) {
				img.pixels[i] = palette[getFromBits(i * bpp, bpp, readSet)];
			}
		}else {
			throw new IOException("Unknown EBP storage type: " + v);
		}
		if(is.read() != ':' || is.read() != '>') {
			throw new IOException("Invalid footer! (:>)");
		}
		return img;
	}

	public static ImageData loadTextureSafe(InputStream is, int alpha) throws IOException {
		ImageData bufferedimage;
		try {
			bufferedimage = loadTexture(is, alpha);
		} finally {
			IOUtils.closeQuietly(is);
		}

		return bufferedimage;
	}

}
