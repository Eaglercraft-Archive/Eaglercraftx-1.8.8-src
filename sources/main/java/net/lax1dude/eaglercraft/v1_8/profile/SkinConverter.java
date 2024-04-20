package net.lax1dude.eaglercraft.v1_8.profile;

import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

/**
 * Copyright (c) 2022-2024 lax1dude, ayunami2000. All Rights Reserved.
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
public class SkinConverter {

	public static void convert64x32to64x64(ImageData skinIn, ImageData skinOut) {
		copyRawPixels(skinIn.pixels, skinOut.pixels, 0, 0, 0, 0, 64, 32, 64, 64, false);
		copyRawPixels(skinIn.pixels, skinOut.pixels, 24, 48, 20, 52, 4, 16, 8, 20, 64, 64);
		copyRawPixels(skinIn.pixels, skinOut.pixels, 28, 48, 24, 52, 8, 16, 12, 20, 64, 64);
		copyRawPixels(skinIn.pixels, skinOut.pixels, 20, 52, 16, 64, 8, 20, 12, 32, 64, 64);
		copyRawPixels(skinIn.pixels, skinOut.pixels, 24, 52, 20, 64, 4, 20, 8, 32, 64, 64);
		copyRawPixels(skinIn.pixels, skinOut.pixels, 28, 52, 24, 64, 0, 20, 4, 32, 64, 64);
		copyRawPixels(skinIn.pixels, skinOut.pixels, 32, 52, 28, 64, 12, 20, 16, 32, 64, 64);
		copyRawPixels(skinIn.pixels, skinOut.pixels, 40, 48, 36, 52, 44, 16, 48, 20, 64, 64);
		copyRawPixels(skinIn.pixels, skinOut.pixels, 44, 48, 40, 52, 48, 16, 52, 20, 64, 64);
		copyRawPixels(skinIn.pixels, skinOut.pixels, 36, 52, 32, 64, 48, 20, 52, 32, 64, 64);
		copyRawPixels(skinIn.pixels, skinOut.pixels, 40, 52, 36, 64, 44, 20, 48, 32, 64, 64);
		copyRawPixels(skinIn.pixels, skinOut.pixels, 44, 52, 40, 64, 40, 20, 44, 32, 64, 64);
		copyRawPixels(skinIn.pixels, skinOut.pixels, 48, 52, 44, 64, 52, 20, 56, 32, 64, 64);
	}

	public static void convertCape32x32RGBAto23x17RGB(ImageData skinIn, byte[] skinOut) {
		int i, j;
		for(int y = 0; y < 17; ++y) {
			for(int x = 0; x < 22; ++x) {
				i = (y * 23 + x) * 3;
				j = skinIn.pixels[y * skinIn.width + x];
				if((j & 0xFF000000) != 0) {
					skinOut[i] = (byte)(j >> 16);
					skinOut[i + 1] = (byte)(j >> 8);
					skinOut[i + 2] = (byte)(j & 0xFF);
				}else {
					skinOut[i] = skinOut[i + 1] = skinOut[i + 2] = 0;
				}
			}
		}
		for(int y = 0; y < 11; ++y) {
			i = ((y + 6) * 23 + 22) * 3;
			j = skinIn.pixels[(y + 11) * skinIn.width + 22];
			if((j & 0xFF000000) != 0) {
				skinOut[i] = (byte)(j >> 16);
				skinOut[i + 1] = (byte)(j >> 8);
				skinOut[i + 2] = (byte)(j & 0xFF);
			}else {
				skinOut[i] = skinOut[i + 1] = skinOut[i + 2] = 0;
			}
		}
	}

	public static void convertCape23x17RGBto32x32RGBA(byte[] skinIn, byte[] skinOut) {
		int i, j;
		for(int y = 0; y < 17; ++y) {
			for(int x = 0; x < 22; ++x) {
				i = (y * 32 + x) << 2;
				j = (y * 23 + x) * 3;
				skinOut[i] = (byte)0xFF;
				skinOut[i + 1] = skinIn[j];
				skinOut[i + 2] = skinIn[j + 1];
				skinOut[i + 3] = skinIn[j + 2];
			}
		}
		for(int y = 0; y < 11; ++y) {
			i = ((y + 11) * 32 + 22) << 2;
			j = ((y + 6) * 23 + 22) * 3;
			skinOut[i] = (byte)0xFF;
			skinOut[i + 1] = skinIn[j];
			skinOut[i + 2] = skinIn[j + 1];
			skinOut[i + 3] = skinIn[j + 2];
		}
	}

	private static void copyRawPixels(int[] imageIn, int[] imageOut, int dx1, int dy1, int dx2, int dy2, int sx1,
			int sy1, int sx2, int sy2, int imgSrcWidth, int imgDstWidth) {
		if(dx1 > dx2) {
			copyRawPixels(imageIn, imageOut, sx1, sy1, dx2, dy1, sx2 - sx1, sy2 - sy1, imgSrcWidth, imgDstWidth, true);
		} else {
			copyRawPixels(imageIn, imageOut, sx1, sy1, dx1, dy1, sx2 - sx1, sy2 - sy1, imgSrcWidth, imgDstWidth, false);
		}
	}

	private static void copyRawPixels(int[] imageIn, int[] imageOut, int srcX, int srcY, int dstX, int dstY, int width,
			int height, int imgSrcWidth, int imgDstWidth, boolean flip) {
		int i, j;
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				i = imageIn[(srcY + y) * imgSrcWidth + srcX + x];
				if(flip) {
					j = (dstY + y) * imgDstWidth + dstX + width - x - 1;
				}else {
					j = (dstY + y) * imgDstWidth + dstX + x;
				}
				imageOut[j] = i;
			}
		}
	}

}
