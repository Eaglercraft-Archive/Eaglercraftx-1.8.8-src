package net.lax1dude.eaglercraft.v1_8.internal;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import net.lax1dude.eaglercraft.v1_8.EaglerInputStream;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;

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
public class PlatformAssets {
	
	static URL getDesktopResourceURL(String path) {
		File f = new File("resources", path);
		if(f.isFile()) {
			try {
				return f.toURI().toURL();
			} catch (MalformedURLException e) {
				return null;
			}
		}else {
			return null;
		}
	}
	
	public static final byte[] getResourceBytes(String path) {
		File loadFile = new File("resources", path);
		byte[] ret = new byte[(int) loadFile.length()];
		try(FileInputStream is = new FileInputStream(loadFile)) {
			int i, j = 0;
			while(j < ret.length && (i = is.read(ret, j, ret.length - j)) != -1) {
				j += i;
			}
			return ret;
		}catch(IOException ex) {
			return null;
		}
	}
	
	public static final ImageData loadImageFile(InputStream data) {
		try {
			BufferedImage img = ImageIO.read(data);
			int w = img.getWidth();
			int h = img.getHeight();
			boolean a = img.getColorModel().hasAlpha();
			int[] pixels = new int[w * h];
			img.getRGB(0, 0, w, h, pixels, 0, w);
			for(int i = 0; i < pixels.length; ++i) {
				int j = pixels[i];
				if(!a) {
					j = j | 0xFF000000;
				}
				pixels[i] = (j & 0xFF00FF00) | ((j & 0x00FF0000) >> 16) |
						((j & 0x000000FF) << 16);
			}
			return new ImageData(w, h, pixels, a);
		}catch(IOException ex) {
			return null;
		}
	}
	
	public static final ImageData loadImageFile(byte[] data) {
		return loadImageFile(new EaglerInputStream(data));
	}
	
}
