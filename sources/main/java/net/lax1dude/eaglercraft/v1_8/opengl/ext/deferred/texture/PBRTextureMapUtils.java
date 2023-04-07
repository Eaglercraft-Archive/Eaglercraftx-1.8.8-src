package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture;

import java.util.List;

import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

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
public class PBRTextureMapUtils {

	public static final ImageData defaultNormalsTexture = new ImageData(1, 1, new int[] { 0 }, true);

	public static final PBRMaterialConstants blockMaterialConstants = new PBRMaterialConstants(new ResourceLocation("eagler:glsl/deferred/material_block_constants.csv"));

	public static final Logger logger = LogManager.getLogger("PBRTextureMap");

	public static ImageData locateCompanionTexture(IResourceManager resMgr, IResource mainImage, String ext) {
		ResourceLocation baseLocation = mainImage.getResourceLocation();
		String domain = baseLocation.getResourceDomain();
		String resourcePack = mainImage.getResourcePackName();
		String fname = baseLocation.getResourcePath();
		int idx = fname.lastIndexOf('.');
		if(idx != -1) {
			fname = fname.substring(0, idx) + ext + fname.substring(idx);
		}else {
			fname += ext;
		}
		try {
			List<IResource> ress = resMgr.getAllResources(new ResourceLocation(domain, fname));
			for(IResource res : ress) {
				if(res.getResourcePackName().equals(resourcePack)) {
					ImageData toRet = TextureUtil.readBufferedImage(res.getInputStream());
					if(ext.equals("_s")) {
						for(int i = 0, j; i < toRet.pixels.length; ++i) {
							// swap B and A, because labPBR support
							int a = (toRet.pixels[i] >>> 24) & 0xFF;
							if(a == 0xFF) a = 0;
							toRet.pixels[i] = (toRet.pixels[i] & 0x0000FFFF) | Math.min(a << 18, 0xFF0000) | 0xFF000000;
						}
					}
					return toRet;
				}
			}
		}catch(Throwable t) {
		}
		if("Default".equals(resourcePack)) {
			idx = fname.lastIndexOf('.');
			if(idx != -1) {
				fname = fname.substring(0, idx);
			}
			try {
				return TextureUtil.readBufferedImage(resMgr.getResource(new ResourceLocation("eagler:glsl/deferred/assets_pbr/" + fname + ".png")).getInputStream());
			}catch(Throwable t) {
			}
			try {
				return EaglerBitwisePackedTexture.loadTexture(resMgr.getResource(new ResourceLocation("eagler:glsl/deferred/assets_pbr/" + fname + ".ebp")).getInputStream(), 255);
			}catch(Throwable t) {
				// dead code because teavm
				t.toString();
			}
		}
		return null;
	}

	public static void unifySizes(int lvl, ImageData[]... imageSets) {
		int resX = -1;
		int resY = -1;
		int iw, ih;
		for(int i = 0; i < imageSets.length; ++i) {
			iw = imageSets[i][lvl].width;
			ih = imageSets[i][lvl].height;
			if(iw != ih) {
			}
			if(iw > resX) {
				resX = iw;
			}
			if(ih > resY) {
				resY = ih;
			}
		}
		if(resX == -1 || resY == -1) {
			throw new IllegalArgumentException("No images were provided!");
		}
		for(int i = 0; i < imageSets.length; ++i) {
			ImageData in = imageSets[i][lvl];
			ImageData out = null;
			if(in.width != resX || in.height != resY) {
				out = new ImageData(resX, resY, true);
				if(in.width == 1 && in.height == 1) {
					int px = in.pixels[0];
					for(int j = 0; j < out.pixels.length; ++j) {
						out.pixels[j] = px;
					}
				}else {
					for(int y = 0; y < resY; ++y) {
						for(int x = 0; x < resX; ++x) {
							out.pixels[y * resX + x] = in.pixels[((y * in.height / resY)) * in.width + (x * in.width / resX)];
						}
					}
				}
			}
			if(out != null) {
				imageSets[i][lvl] = out;
			}
		}
	}

	public static ImageData generateMaterialTextureFor(String iconName) {
		if(iconName.startsWith("minecraft:")) {
			iconName = iconName.substring(10);
		}
		Integer in = blockMaterialConstants.spriteNameToMaterialConstants.get(iconName);
		if(in == null) {
			return new ImageData(1, 1, new int[] { blockMaterialConstants.defaultMaterial }, true);
		}else {
			return new ImageData(1, 1, new int[] { in.intValue() }, true);
		}
	}

	public static int[][] generateMipmapDataIgnoreAlpha(int level, int width, int[][] aint) {
		int[][] ret = new int[level + 1][];
		ret[0] = aint[0];
		if (level > 0) {
			for(int i = 1; i <= level; ++i) {
				if(aint[i] != null) {
					ret[i] = aint[i];
				}else {
					int lvlW = width >> i, lvl2W = lvlW << 1;
					int len = lvlW * lvlW;
					ret[i] = new int[len];
					int x, y, s1, s2, s3, s4, c1, c2, c3, c4;
					for(int j = 0; j < len; ++j) {
						x = (j % len) << 1;
						y = (j / len) << 1;
						s1 = ret[i - 1][x + y * lvl2W];
						s2 = ret[i - 1][x + y * lvl2W + 1];
						s3 = ret[i - 1][x + y * lvl2W + lvl2W];
						s4 = ret[i - 1][x + y * lvl2W + lvl2W + 1];
						c1 = ((s1 >> 24) & 0xFF) + ((s2 >> 24) & 0xFF) + ((s3 >> 24) & 0xFF) + ((s4 >> 24) & 0xFF);
						c2 = ((s1 >> 16) & 0xFF) + ((s2 >> 16) & 0xFF) + ((s3 >> 16) & 0xFF) + ((s4 >> 16) & 0xFF);
						c3 = ((s1 >> 8) & 0xFF) + ((s2 >> 8) & 0xFF) + ((s3 >> 8) & 0xFF) + ((s4 >> 8) & 0xFF);
						c4 = (s1 & 0xFF) + (s2 & 0xFF) + (s3 & 0xFF) + (s4 & 0xFF);
						ret[i][j] = ((c1 >> 2) << 24) | ((c2 >> 2) << 16) | ((c3 >> 2) << 8) | (c4 >> 2);
					}
				}
			}
		}
		return ret;
	}

}
