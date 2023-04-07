package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture;

import java.io.IOException;
import java.io.InputStream;

import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

/**
 * Copyright (c) 2023 LAX1DUDE. All Rights Reserved.
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

}
