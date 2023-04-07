package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.HString;
import net.lax1dude.eaglercraft.v1_8.internal.IFramebufferGL;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
import net.lax1dude.eaglercraft.v1_8.minecraft.TextureAnimationCache;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
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
public class EaglerTextureAtlasSpritePBR extends EaglerTextureAtlasSprite {

	private static final Logger logger = LogManager.getLogger("EaglerTextureAtlasSpritePBR");

	protected List<int[][]>[] frameTextureDataPBR = new List[] { Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList() };
	protected TextureAnimationCache[] animationCachePBR = new TextureAnimationCache[3];

	public boolean dontAnimateNormals = true;
	public boolean dontAnimateMaterial = true;

	public static EaglerTextureAtlasSpritePBR makeAtlasSprite(ResourceLocation spriteResourceLocation) {
		String s = spriteResourceLocation.toString();
		return (EaglerTextureAtlasSpritePBR) (locationNameClock.equals(s) ? new TextureClockPBRImpl(s)
				: (locationNameCompass.equals(s) ? new TextureCompassPBRImpl(s) : new EaglerTextureAtlasSpritePBR(s)));
	}

	public EaglerTextureAtlasSpritePBR(String spriteName) {
		super(spriteName);
	}

	public void loadSpritePBR(ImageData[][] imageDatas, AnimationMetadataSection meta,
			boolean dontAnimateNormals, boolean dontAnimateMaterial) {
		this.resetSprite();
		if(imageDatas.length != 3) {
			throw new IllegalArgumentException("loadSpritePBR required an array of 3 different textures (" + imageDatas.length + " given)");
		}
		this.dontAnimateNormals = dontAnimateNormals;
		this.dontAnimateMaterial = dontAnimateMaterial;
		int i = imageDatas[0][0].width;
		int j = imageDatas[0][0].height;
		this.width = i;
		this.height = j;
		int[][][] aint = new int[3][imageDatas[0].length][];

		for (int l = 0; l < imageDatas.length; ++l) {
			ImageData[] images = imageDatas[l];
			for (int k = 0; k < images.length; ++k) {
				ImageData bufferedimage = images[k];
				if (bufferedimage != null) {
					if (k > 0 && (bufferedimage.width) != i >> k || bufferedimage.height != j >> k) {
						throw new RuntimeException(
								HString.format("Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d",
										new Object[] { Integer.valueOf(k), Integer.valueOf(bufferedimage.width),
												Integer.valueOf(bufferedimage.height), Integer.valueOf(i >> k),
												Integer.valueOf(j >> k) }));
					}
	
					aint[l][k] = new int[bufferedimage.width * bufferedimage.height];
					bufferedimage.getRGB(0, 0, bufferedimage.width, bufferedimage.height, aint[l][k], 0, bufferedimage.width);
				}
			}
		}

		if (meta == null) {
			if (j != i) {
				throw new RuntimeException("broken aspect ratio and not an animation");
			}

			this.frameTextureDataPBR[0].add(aint[0]);
			this.frameTextureDataPBR[1].add(aint[1]);
			this.frameTextureDataPBR[2].add(aint[2]);
		} else {
			int j1 = j / i;
			int k1 = i;
			int l = i;
			this.height = this.width;
			if (meta.getFrameCount() > 0) {
				Iterator iterator = meta.getFrameIndexSet().iterator();

				while (iterator.hasNext()) {
					int i1 = ((Integer) iterator.next()).intValue();
					if (i1 >= j1) {
						throw new RuntimeException("invalid frameindex " + i1);
					}

					this.allocateFrameTextureData(i1);
					this.frameTextureDataPBR[0].set(i1, getFrameTextureData(aint[0], k1, l, i1));
					this.frameTextureDataPBR[1].set(i1, getFrameTextureData(aint[1], k1, l, i1));
					this.frameTextureDataPBR[2].set(i1, getFrameTextureData(aint[2], k1, l, i1));
				}

				this.animationMetadata = meta;
			} else {
				ArrayList arraylist = Lists.newArrayList();

				for (int l1 = 0; l1 < j1; ++l1) {
					this.frameTextureDataPBR[0].add(getFrameTextureData(aint[0], k1, l, l1));
					this.frameTextureDataPBR[1].add(getFrameTextureData(aint[1], k1, l, l1));
					this.frameTextureDataPBR[2].add(getFrameTextureData(aint[2], k1, l, l1));
					arraylist.add(new AnimationFrame(l1, -1));
				}

				this.animationMetadata = new AnimationMetadataSection(arraylist, this.width, this.height,
						meta.getFrameTime(), meta.isInterpolate());
			}
		}
	}

	public int[][][] getFramePBRTextureData(int index) {
		return new int[][][] { frameTextureDataPBR[0].get(index),
				frameTextureDataPBR[1].get(index),
				frameTextureDataPBR[2].get(index) };
	}

	public int[][] getFrameTextureData(int index) {
		return frameTextureDataPBR[0].get(index);
	}

	public int getFrameCount() {
		return frameTextureDataPBR[0].size();
	}

	public void setFramesTextureDataPBR(List<int[][]>[] newFramesTextureData) {
		frameTextureDataPBR = newFramesTextureData;
	}

	protected void allocateFrameTextureData(int index) {
		for(int j = 0; j < 3; ++j) {
			if (this.frameTextureDataPBR[j].size() <= index) {
				for (int i = this.frameTextureDataPBR[j].size(); i <= index; ++i) {
					this.frameTextureDataPBR[j].add((int[][]) null);
				}
			}
		}
	}

	public void generateMipmaps(int level) {
		List[] arraylist = new List[] { Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList() };

		for(int j = 0; j < 3; ++j) {
			for (int i = 0; i < this.frameTextureDataPBR[j].size(); ++i) {
				final int[][] aint = (int[][]) this.frameTextureDataPBR[j].get(i);
				if (aint != null) {
					try {
						if(j == 0) {
							arraylist[j].add(TextureUtil.generateMipmapData(level, this.width, aint));
						}else {
							arraylist[j].add(PBRTextureMapUtils.generateMipmapDataIgnoreAlpha(level, this.width, aint));
						}
					} catch (Throwable throwable) {
						CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Generating mipmaps for frame (pbr)");
						CrashReportCategory crashreportcategory = crashreport.makeCategory("Frame being iterated");
						crashreportcategory.addCrashSection("PBR Layer", Integer.valueOf(j));
						crashreportcategory.addCrashSection("Frame index", Integer.valueOf(i));
						crashreportcategory.addCrashSectionCallable("Frame sizes", new Callable<String>() {
							public String call() throws Exception {
								StringBuilder stringbuilder = new StringBuilder();
	
								for (int[] aint1 : aint) {
									if (stringbuilder.length() > 0) {
										stringbuilder.append(", ");
									}
	
									stringbuilder.append(aint1 == null ? "null" : Integer.valueOf(aint1.length));
								}
	
								return stringbuilder.toString();
							}
						});
						throw new ReportedException(crashreport);
					}
				}
			}
		}

		this.setFramesTextureDataPBR(arraylist);
		this.bakeAnimationCache();
	}

	public void bakeAnimationCache() {
		if(animationMetadata != null) {
			for(int i = 0; i < 3; ++i) {
				if(dontAnimateNormals && i == 1) continue;
				if(dontAnimateMaterial && i == 2) continue;
				int mipLevels = frameTextureDataPBR[i].get(0).length;
				if(animationCachePBR[i] == null) {
					animationCachePBR[i] = new TextureAnimationCache(width, height, mipLevels);
				}
				animationCachePBR[i].initialize(frameTextureDataPBR[i]);
			}
		}
	}

	public void updateAnimationPBR(IFramebufferGL[] copyColorFramebuffer, IFramebufferGL[] copyMaterialFramebuffer, int materialTexOffset) {
		if(animationCachePBR[0] == null || (!dontAnimateNormals && animationCachePBR[1] == null)
				|| (!dontAnimateMaterial && animationCachePBR[2] == null)) {
			throw new IllegalStateException("Animation cache for '" + this.iconName + "' was never baked!");
		}
		++this.tickCounter;
		if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter)) {
			int i = this.animationMetadata.getFrameIndex(this.frameCounter);
			int j = this.animationMetadata.getFrameCount() == 0 ? this.frameTextureDataPBR[0].size()
					: this.animationMetadata.getFrameCount();
			this.frameCounter = (this.frameCounter + 1) % j;
			this.tickCounter = 0;
			int k = this.animationMetadata.getFrameIndex(this.frameCounter);
			if (i != k && k >= 0 && k < this.frameTextureDataPBR[0].size()) {
				animationCachePBR[0].copyFrameLevelsToTex2D(k, this.originX, this.originY, this.width, this.height, copyColorFramebuffer);
				if(!dontAnimateNormals) animationCachePBR[1].copyFrameLevelsToTex2D(k, this.originX, this.originY, this.width, this.height, copyMaterialFramebuffer);
				if(!dontAnimateMaterial) animationCachePBR[2].copyFrameLevelsToTex2D(k, this.originX, this.originY + materialTexOffset, this.width, this.height, copyMaterialFramebuffer);
			}
		} else if (this.animationMetadata.isInterpolate()) {
			float f = 1.0f - (float) this.tickCounter / (float) this.animationMetadata.getFrameTimeSingle(this.frameCounter);
			int i = this.animationMetadata.getFrameIndex(this.frameCounter);
			int j = this.animationMetadata.getFrameCount() == 0 ? this.frameTextureDataPBR[0].size()
					: this.animationMetadata.getFrameCount();
			int k = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % j);
			if (i != k && k >= 0 && k < this.frameTextureDataPBR[0].size()) {
				animationCachePBR[0].copyInterpolatedFrameLevelsToTex2D(i, k, f, this.originX, this.originY, this.width, this.height, copyColorFramebuffer);
				if(!dontAnimateNormals) animationCachePBR[1].copyInterpolatedFrameLevelsToTex2D(i, k, f, this.originX, this.originY, this.width, this.height, copyMaterialFramebuffer);
				if(!dontAnimateMaterial) animationCachePBR[2].copyInterpolatedFrameLevelsToTex2D(i, k, f, this.originX, this.originY + materialTexOffset, this.width, this.height, copyMaterialFramebuffer);
			}
		}
	}

	public void clearFramesTextureData() {
		for(int i = 0; i < 3; ++i) {
			this.frameTextureDataPBR[i].clear();
			if(this.animationCachePBR[i] != null) {
				this.animationCachePBR[i].free();
				this.animationCachePBR[i] = null;
			}
		}
	}

	public void loadSprite(ImageData[] images, AnimationMetadataSection meta) throws IOException {
		Throwable t = new UnsupportedOperationException("Cannot call regular loadSprite in PBR mode, use loadSpritePBR");
		try {
			throw t;
		}catch(Throwable tt) {
			logger.error(t);
		}
	}

	public void setFramesTextureData(List<int[][]> newFramesTextureData) {
		Throwable t = new UnsupportedOperationException("Cannot call regular setFramesTextureData in PBR mode, use setFramesTextureDataPBR");
		try {
			throw t;
		}catch(Throwable tt) {
			logger.error(t);
		}
	}

	public void updateAnimation(IFramebufferGL[] fb) {
		Throwable t = new UnsupportedOperationException("Cannot call regular updateAnimation in PBR mode, use updateAnimationPBR");
		try {
			throw t;
		}catch(Throwable tt) {
			logger.error(t);
		}
	}

	protected void resetSprite() {
		this.animationMetadata = null;
		this.setFramesTextureDataPBR(new List[] { Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList() });
		this.frameCounter = 0;
		this.tickCounter = 0;
		for(int i = 0; i < 3; ++i) {
			if(this.animationCachePBR[i] != null) {
				this.animationCachePBR[i].free();
				this.animationCachePBR[i] = null;
			}
		}
	}

	public String toString() {
		return "EaglerTextureAtlasSpritePBR{name=\'" + this.iconName + '\'' + ", frameCount=" + this.framesTextureData.size()
				+ ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height="
				+ this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0="
				+ this.minV + ", v1=" + this.maxV + '}';
	}

}
