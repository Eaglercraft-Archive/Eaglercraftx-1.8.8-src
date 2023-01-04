package net.lax1dude.eaglercraft.v1_8.plugin.gateway_bungeecord.skins;

/**
 * Copyright (c) 2022-2023 LAX1DUDE. All Rights Reserved.
 * 
 * WITH THE EXCEPTION OF PATCH FILES, MINIFIED JAVASCRIPT, AND ALL FILES
 * NORMALLY FOUND IN AN UNMODIFIED MINECRAFT RESOURCE PACK, YOU ARE NOT ALLOWED
 * TO SHARE, DISTRIBUTE, OR REPURPOSE ANY FILE USED BY OR PRODUCED BY THE
 * SOFTWARE IN THIS REPOSITORY WITHOUT PRIOR PERMISSION FROM THE PROJECT AUTHOR.
 * 
 * NOT FOR COMMERCIAL OR MALICIOUS USE
 * 
 * (please read the 'LICENSE' file this repo's root directory for more info)
 * 
 */
public class SkinRescaler {

	public static void convertToBytes(int[] imageIn, byte[] imageOut) {
		for(int i = 0, j, k; i < imageIn.length; ++i) {
			j = i << 2;
			k = imageIn[i];
			imageOut[j] = (byte)(k >> 24);
			imageOut[j + 1] = (byte)(k & 0xFF);
			imageOut[j + 2] = (byte)(k >> 8);
			imageOut[j + 3] = (byte)(k >> 16);
		}
	}

	public static void convert64x32To64x64(int[] imageIn, byte[] imageOut) {
		copyRawPixels(imageIn, imageOut, 0, 0, 0, 0, 64, 32, 64, 64, false);
		copyRawPixels(imageIn, imageOut, 24, 48, 20, 52, 4, 16, 8, 20, 64, 64);
		copyRawPixels(imageIn, imageOut, 28, 48, 24, 52, 8, 16, 12, 20, 64, 64);
		copyRawPixels(imageIn, imageOut, 20, 52, 16, 64, 8, 20, 12, 32, 64, 64);
		copyRawPixels(imageIn, imageOut, 24, 52, 20, 64, 4, 20, 8, 32, 64, 64);
		copyRawPixels(imageIn, imageOut, 28, 52, 24, 64, 0, 20, 4, 32, 64, 64);
		copyRawPixels(imageIn, imageOut, 32, 52, 28, 64, 12, 20, 16, 32, 64, 64);
		copyRawPixels(imageIn, imageOut, 40, 48, 36, 52, 44, 16, 48, 20, 64, 64);
		copyRawPixels(imageIn, imageOut, 44, 48, 40, 52, 48, 16, 52, 20, 64, 64);
		copyRawPixels(imageIn, imageOut, 36, 52, 32, 64, 48, 20, 52, 32, 64, 64);
		copyRawPixels(imageIn, imageOut, 40, 52, 36, 64, 44, 20, 48, 32, 64, 64);
		copyRawPixels(imageIn, imageOut, 44, 52, 40, 64, 40, 20, 44, 32, 64, 64);
		copyRawPixels(imageIn, imageOut, 48, 52, 44, 64, 52, 20, 56, 32, 64, 64);
	}

	private static void copyRawPixels(int[] imageIn, byte[] imageOut, int dx1, int dy1, int dx2, int dy2, int sx1,
			int sy1, int sx2, int sy2, int imgSrcWidth, int imgDstWidth) {
		if(dx1 > dx2) {
			copyRawPixels(imageIn, imageOut, sx1, sy1, dx2, dy1, sx2 - sx1, sy2 - sy1, imgSrcWidth, imgDstWidth, true);
		} else {
			copyRawPixels(imageIn, imageOut, sx1, sy1, dx1, dy1, sx2 - sx1, sy2 - sy1, imgSrcWidth, imgDstWidth, false);
		}
	}

	private static void copyRawPixels(int[] imageIn, byte[] imageOut, int srcX, int srcY, int dstX, int dstY, int width,
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
				j = j << 2;
				imageOut[j] = (byte)(i >> 24);
				imageOut[j + 1] = (byte)(i & 0xFF);
				imageOut[j + 2] = (byte)(i >> 8);
				imageOut[j + 3] = (byte)(i >> 16);
			}
		}
	}

}
