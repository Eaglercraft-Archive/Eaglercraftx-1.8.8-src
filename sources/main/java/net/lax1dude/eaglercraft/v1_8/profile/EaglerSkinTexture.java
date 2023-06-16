package net.lax1dude.eaglercraft.v1_8.profile;

import java.io.IOException;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

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
public class EaglerSkinTexture implements ITextureObject {

	private final int[] pixels;
	private final int width;
	private final int height;

	private int textureId = -1;

	public EaglerSkinTexture(int[] pixels, int width, int height) {
		if(pixels.length != width * height) {
			throw new IllegalArgumentException("Wrong data length " + pixels.length * 4 + "  for " + width + "x" + height + " texture");
		}
		this.pixels = pixels;
		this.width = width;
		this.height = height;
	}

	public EaglerSkinTexture(byte[] pixels, int width, int height) {
		if(pixels.length != width * height * 4) {
			throw new IllegalArgumentException("Wrong data length " + pixels.length + "  for " + width + "x" + height + " texture");
		}
		int[] p = new int[pixels.length >> 2];
		for(int i = 0, j; i < p.length; ++i) {
			j = i << 2;
			p[i] = (((int) pixels[j] & 0xFF) << 24) | (((int) pixels[j + 1] & 0xFF) << 16)
					| (((int) pixels[j + 2] & 0xFF) << 8) | ((int) pixels[j + 3] & 0xFF);
		}
		this.pixels = p;
		this.width = width;
		this.height = height;
	}

	public void copyPixelsIn(int[] pixels) {
		if(this.pixels.length != pixels.length) {
			throw new IllegalArgumentException("Tried to copy " + pixels.length + " pixels into a " + this.pixels.length + " pixel texture");
		}
		System.arraycopy(pixels, 0, this.pixels, 0, pixels.length);
		if(textureId != -1) {
			TextureUtil.uploadTextureImageAllocate(textureId, new ImageData(width, height, pixels, true), false, false);
		}
	}

	@Override
	public void loadTexture(IResourceManager var1) throws IOException {
		if(textureId == -1) {
			textureId = GlStateManager.generateTexture();
			TextureUtil.uploadTextureImageAllocate(textureId, new ImageData(width, height, pixels, true), false, false);
		}
	}

	@Override
	public int getGlTextureId() {
		return textureId;
	}

	@Override
	public void setBlurMipmap(boolean var1, boolean var2) {
		// no
	}

	@Override
	public void restoreLastBlurMipmap() {
		// no
	}
	
	public void free() {
		GlStateManager.deleteTexture(textureId);
		textureId = -1;
	}

}
