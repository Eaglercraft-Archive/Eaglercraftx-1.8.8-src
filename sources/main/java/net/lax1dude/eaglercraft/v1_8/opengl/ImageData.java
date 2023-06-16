package net.lax1dude.eaglercraft.v1_8.opengl;

import java.io.InputStream;

import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.lax1dude.eaglercraft.v1_8.internal.PlatformAssets;

public class ImageData {
	
	public final int width;
	public final int height;
	public final int[] pixels;
	public final boolean alpha;
	
	public ImageData(int width, int height, int[] pixels, boolean alpha) {
		this.width = width;
		this.height = height;
		this.pixels = pixels;
		this.alpha = alpha;
	}
	
	public ImageData(int width, int height, boolean alpha) {
		this.width = width;
		this.height = height;
		this.pixels = new int[width * height];
		this.alpha = alpha;
	}
	
	public ImageData fillAlpha() {
		for(int i = 0; i < pixels.length; ++i) {
			pixels[i] = pixels[i] | 0xFF000000;
		}
		return this;
	}
	
	public ImageData getSubImage(int x, int y, int pw, int ph) {
		int[] img = new int[pw * ph];
		for(int i = 0; i < ph; ++i) {
			System.arraycopy(pixels, (i + y) * this.width + x, img, i * pw, pw);
		}
		return new ImageData(pw, ph, img, alpha);
	}

	public static final ImageData loadImageFile(String path) {
		byte[] fileData = EagRuntime.getResourceBytes(path);
		if(fileData != null) {
			return loadImageFile(fileData);
		}else {
			return null;
		}
	}

	public static final ImageData loadImageFile(InputStream data) {
		return PlatformAssets.loadImageFile(data);
	}

	public static final ImageData loadImageFile(byte[] data) {
		return PlatformAssets.loadImageFile(data);
	}

	public void getRGB(int startX, int startY, int w, int h,
            int[] rgbArray, int offset, int scansize) {
		for(int y = 0; y < h; ++y) {
			System.arraycopy(pixels, offset + (y + startY) * scansize + startX, rgbArray, y * w, w);
		}
	}
	
	public void copyPixelsFrom(ImageData input, int dx1, int dy1, int dx2, int dy2,
                                      int sx1, int sy1, int sx2, int sy2) {
		if(sx2 - sx1 != dx2 - dx1) {
			throw new IllegalArgumentException("Width of the copied region must match the"
					+ "width of the pasted region");
		}
		int cw = sx2 - sx1;
		if(sy2 - sy1 != dy2 - dy1) {
			throw new IllegalArgumentException("Height of the copied region must match the"
					+ "height of the pasted region");
		}
		int ch = sy2 - sy1;
		for(int y = 0; y < ch; ++y) {
			System.arraycopy(input.pixels, sx1 + (sy1 + y) * cw, pixels, dx1 + (dy1 + y) * cw, cw);
		}
	}
	
	public void drawLayer(ImageData input, int dx1, int dy1, int dx2, int dy2,
                                      int sx1, int sy1, int sx2, int sy2) {
		if(sx2 - sx1 != dx2 - dx1) {
			throw new IllegalArgumentException("Width of the copied region must match the"
					+ "width of the pasted region");
		}
		int cw = sx2 - sx1;
		if(sy2 - sy1 != dy2 - dy1) {
			throw new IllegalArgumentException("Height of the copied region must match the"
					+ "height of the pasted region");
		}
		int ch = sy2 - sy1;
		for(int y = 0; y < ch; ++y) {
			for(int x = 0; x < cw; ++x) {
				int si = (sy1 + y) * cw + sx1 + x;
				int di = (dy1 + y) * cw + dx1 + x;
				int spx = input.pixels[si];
				int dpx = pixels[di];
				if((spx & 0xFF000000) == 0xFF000000 || (dpx & 0xFF000000) == 0) {
					pixels[di] = spx;
				}else {
					int sa = (spx >> 24) & 0xFF;
					int da = (dpx >> 24) & 0xFF;
					int r = ((spx >> 16) & 0xFF) * sa / 255;
					int g = ((spx >> 8) & 0xFF) * sa / 255;
					int b = (spx & 0xFF) * sa / 255;
					int aa = (255 - sa) * da;
					r += ((dpx >> 16) & 0xFF) * aa / 65025;
					g += ((dpx >> 8) & 0xFF) * aa / 65025;
					b += (dpx & 0xFF) * aa / 65025;
					sa += da;
					if(sa > 0xFF) sa = 0xFF;
					pixels[di] = ((sa) << 24) | (r << 16) | (g << 8) | b;
				}
			}
		}
	}
	
	public ImageData swapRB() {
		for(int i = 0; i < pixels.length; ++i) {
			int j = pixels[i];
			pixels[i] = (j & 0xFF00FF00) | ((j & 0x00FF0000) >> 16) |
					((j & 0x000000FF) << 16);
		}
		return this;
	}
	
	public static int swapRB(int c) {
		return (c & 0xFF00FF00) | ((c & 0x00FF0000) >> 16) | ((c & 0x000000FF) << 16);
	}

}
