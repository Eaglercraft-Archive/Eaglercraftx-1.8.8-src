package net.lax1dude.eaglercraft.v1_8.minecraft;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import com.carrotsearch.hppc.cursors.IntCursor;
import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.v1_8.HString;
import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureClock;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.optifine.util.CounterInt;

/**
 * Copyright (c) 2022-2025 lax1dude. All Rights Reserved.
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
public class EaglerTextureAtlasSprite {

	private static final Logger logger = LogManager.getLogger("EaglerTextureAtlasSprite");

	protected final String iconName;
	protected List<int[][]> framesTextureData = Lists.newArrayList();
	protected int[][] interpolatedFrameData;
	protected AnimationMetadataSection animationMetadata;
	protected boolean rotated;
	protected int originX;
	protected int originY;
	protected int width;
	protected int height;
	protected float minU;
	protected float maxU;
	protected float minV;
	protected float maxV;
	protected int frameCounter;
	protected int tickCounter;
    private int indexInMap = -1;
	protected static String locationNameClock = "builtin/clock";
	protected static String locationNameCompass = "builtin/compass";

	protected TextureAnimationCache animationCache = null;

	public String optifineBaseTextureName = null;

	public EaglerTextureAtlasSprite(String spriteName) {
		this.iconName = spriteName;
	}

	public static EaglerTextureAtlasSprite makeAtlasSprite(ResourceLocation spriteResourceLocation) {
		String s = spriteResourceLocation.toString();
		return (EaglerTextureAtlasSprite) (locationNameClock.equals(s) ? new TextureClock(s)
				: (locationNameCompass.equals(s) ? new TextureCompass(s) : new EaglerTextureAtlasSprite(s)));
	}

	public static void setLocationNameClock(String clockName) {
		locationNameClock = clockName;
	}

	public static void setLocationNameCompass(String compassName) {
		locationNameCompass = compassName;
	}

	public void initSprite(int inX, int inY, int originInX, int originInY, boolean rotatedIn) {
		this.originX = originInX;
		this.originY = originInY;
		this.rotated = rotatedIn;
		float f = (float) (0.009999999776482582D / (double) inX);
		float f1 = (float) (0.009999999776482582D / (double) inY);
		this.minU = (float) originInX / (float) ((double) inX) + f;
		this.maxU = (float) (originInX + this.width) / (float) ((double) inX) - f;
		this.minV = (float) originInY / (float) inY + f1;
		this.maxV = (float) (originInY + this.height) / (float) inY - f1;
	}

	public void copyFrom(EaglerTextureAtlasSprite atlasSpirit) {
		this.originX = atlasSpirit.originX;
		this.originY = atlasSpirit.originY;
		this.width = atlasSpirit.width;
		this.height = atlasSpirit.height;
		this.rotated = atlasSpirit.rotated;
		this.minU = atlasSpirit.minU;
		this.maxU = atlasSpirit.maxU;
		this.minV = atlasSpirit.minV;
		this.maxV = atlasSpirit.maxV;
		if (atlasSpirit != Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite()) {
			this.indexInMap = atlasSpirit.indexInMap;
		}
	}

	public int getOriginX() {
		return this.originX;
	}

	public int getOriginY() {
		return this.originY;
	}

	public int getIconWidth() {
		return this.width;
	}

	public int getIconHeight() {
		return this.height;
	}

	public float getMinU() {
		return this.minU;
	}

	public float getMaxU() {
		return this.maxU;
	}

	public float getInterpolatedU(double u) {
		float f = this.maxU - this.minU;
		return this.minU + f * (float) u / 16.0F;
	}

	public float getMinV() {
		return this.minV;
	}

	public float getMaxV() {
		return this.maxV;
	}

	public float getInterpolatedV(double v) {
		float f = this.maxV - this.minV;
		return this.minV + f * ((float) v / 16.0F);
	}

	public String getIconName() {
		return this.iconName;
	}

	protected static interface IAnimCopyFunction {
		void updateAnimation(int mapWidth, int mapHeight, int mapLevel);
	}

	protected IAnimCopyFunction currentAnimUpdater = null;

	public void updateAnimation() {
		if(animationCache == null) {
			throw new IllegalStateException("Animation cache for '" + this.iconName + "' was never baked!");
		}
		++this.tickCounter;
		if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter)) {
			int i = this.animationMetadata.getFrameIndex(this.frameCounter);
			int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size()
					: this.animationMetadata.getFrameCount();
			this.frameCounter = (this.frameCounter + 1) % j;
			this.tickCounter = 0;
			int k = this.animationMetadata.getFrameIndex(this.frameCounter);
			if (i != k && k >= 0 && k < this.framesTextureData.size()) {
				currentAnimUpdater = (mapWidth, mapHeight, mapLevel) -> {
					animationCache.copyFrameToTex2D(k, mapLevel, this.originX >> mapLevel, this.originY >> mapLevel,
							this.width >> mapLevel, this.height >> mapLevel, mapWidth, mapHeight);
				};
			}else {
				currentAnimUpdater = null;
			}
		} else if (this.animationMetadata.isInterpolate()) {
			float f = 1.0f - (float) this.tickCounter / (float) this.animationMetadata.getFrameTimeSingle(this.frameCounter);
			int i = this.animationMetadata.getFrameIndex(this.frameCounter);
			int j = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size()
					: this.animationMetadata.getFrameCount();
			int k = this.animationMetadata.getFrameIndex((this.frameCounter + 1) % j);
			if (i != k && k >= 0 && k < this.framesTextureData.size()) {
				currentAnimUpdater = (mapWidth, mapHeight, mapLevel) -> {
					animationCache.copyInterpolatedFrameToTex2D(i, k, f, mapLevel, this.originX >> mapLevel,
							this.originY >> mapLevel, this.width >> mapLevel, this.height >> mapLevel, mapWidth,
							mapHeight);
				};
			}else {
				currentAnimUpdater = null;
			}
		} else {
			currentAnimUpdater = null;
		}
	}

	public void copyAnimationFrame(int mapWidth, int mapHeight, int mapLevel) {
		if(currentAnimUpdater != null) {
			currentAnimUpdater.updateAnimation(mapWidth, mapHeight, mapLevel);
		}
	}

	public int[][] getFrameTextureData(int index) {
		return (int[][]) this.framesTextureData.get(index);
	}

	public int getFrameCount() {
		return this.framesTextureData.size();
	}

	public void setIconWidth(int newWidth) {
		this.width = newWidth;
	}

	public void setIconHeight(int newHeight) {
		this.height = newHeight;
	}

	public void loadSprite(ImageData[] images, AnimationMetadataSection meta) throws IOException {
		this.resetSprite();
		int i = images[0].width;
		int j = images[0].height;
		this.width = i;
		this.height = j;
		int[][] aint = new int[images.length][];

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

				aint[k] = new int[bufferedimage.width * bufferedimage.height];
				bufferedimage.getRGB(0, 0, bufferedimage.width, bufferedimage.height, aint[k], 0, bufferedimage.width);
			}
		}

		if (meta == null) {
			if (j != i) {
				throw new RuntimeException("broken aspect ratio and not an animation");
			}

			this.framesTextureData.add(aint);
		} else {
			int j1 = j / i;
			int k1 = i;
			int l = i;
			this.height = this.width;
			if (meta.getFrameCount() > 0) {
				for (IntCursor cur : meta.getFrameIndexSet()) {
					int i1 = cur.value;
					if (i1 >= j1) {
						throw new RuntimeException("invalid frameindex " + i1);
					}

					this.allocateFrameTextureData(i1);
					this.framesTextureData.set(i1, getFrameTextureData(aint, k1, l, i1));
				}

				this.animationMetadata = meta;
			} else {
				List<AnimationFrame> arraylist = Lists.newArrayList();

				for (int l1 = 0; l1 < j1; ++l1) {
					this.framesTextureData.add(getFrameTextureData(aint, k1, l, l1));
					arraylist.add(new AnimationFrame(l1, -1));
				}

				this.animationMetadata = new AnimationMetadataSection(arraylist, this.width, this.height,
						meta.getFrameTime(), meta.isInterpolate());
			}
		}

	}

	public void generateMipmaps(int level) {
		List<int[][]> arraylist = Lists.newArrayList();

		for (int i = 0; i < this.framesTextureData.size(); ++i) {
			final int[][] aint = this.framesTextureData.get(i);
			if (aint != null) {
				try {
					arraylist.add(TextureUtil.generateMipmapData(level, this.width, aint));
				} catch (Throwable throwable) {
					CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Generating mipmaps for frame");
					CrashReportCategory crashreportcategory = crashreport.makeCategory("Frame being iterated");
					crashreportcategory.addCrashSection("Frame index", Integer.valueOf(i));
					crashreportcategory.addCrashSectionCallable("Frame sizes", new Callable<String>() {
						public String call() throws Exception {
							StringBuilder stringbuilder = new StringBuilder();

							for (int j = 0; j < aint.length; ++j) {
								int[] aint1 = aint[j];
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

		this.setFramesTextureData(arraylist);
		this.bakeAnimationCache();
	}

	public void bakeAnimationCache() {
		if(animationMetadata != null) {
			int mipLevels = framesTextureData.get(0).length;
			if(animationCache == null) {
				animationCache = new TextureAnimationCache(width, height, mipLevels);
			}
			animationCache.initialize(framesTextureData);
		}
	}

	protected void allocateFrameTextureData(int index) {
		if (this.framesTextureData.size() <= index) {
			for (int i = this.framesTextureData.size(); i <= index; ++i) {
				this.framesTextureData.add((int[][]) null);
			}
		}
	}

	protected static int[][] getFrameTextureData(int[][] data, int rows, int columns, int parInt3) {
		int[][] aint = new int[data.length][];

		for (int i = 0; i < data.length; ++i) {
			int[] aint1 = data[i];
			if (aint1 != null) {
				aint[i] = new int[(rows >> i) * (columns >> i)];
				System.arraycopy(aint1, parInt3 * aint[i].length, aint[i], 0, aint[i].length);
			}
		}

		return aint;
	}

	public void clearFramesTextureData() {
		this.framesTextureData.clear();
		if(this.animationCache != null) {
			this.animationCache.free();
			this.animationCache = null;
		}
	}

	public boolean hasAnimationMetadata() {
		return this.animationMetadata != null;
	}

	public void setFramesTextureData(List<int[][]> newFramesTextureData) {
		this.framesTextureData = newFramesTextureData;
	}

	protected void resetSprite() {
		this.animationMetadata = null;
		this.setFramesTextureData(Lists.newArrayList());
		this.frameCounter = 0;
		this.tickCounter = 0;
		if(this.animationCache != null) {
			this.animationCache.free();
			this.animationCache = null;
		}
	}

	public String toString() {
		return "TextureAtlasSprite{name=\'" + this.iconName + '\'' + ", frameCount=" + this.framesTextureData.size()
				+ ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height="
				+ this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0="
				+ this.minV + ", v1=" + this.maxV + '}';
	}

	public void loadSpritePBR(ImageData[][] imageDatas, AnimationMetadataSection animationmetadatasection,
			boolean dontAnimateNormals, boolean dontAnimateMaterial) {
		Throwable t = new UnsupportedOperationException("PBR is not enabled");
		try {
			throw t;
		}catch(Throwable tt) {
			logger.error(t);
		}
	}

	public void updateAnimationPBR() {
		Throwable t = new UnsupportedOperationException("PBR is not enabled");
		try {
			throw t;
		}catch(Throwable tt) {
			logger.error(t);
		}
	}

	public void copyAnimationFramePBR(int pass, int width, int height, int level) {
		Throwable t = new UnsupportedOperationException("PBR is not enabled");
		try {
			throw t;
		}catch(Throwable tt) {
			logger.error(t);
		}
	}

	public int getIndexInMap() {
		return this.indexInMap;
	}

	public void setIndexInMap(int p_setIndexInMap_1_) {
		this.indexInMap = p_setIndexInMap_1_;
	}

	public void updateIndexInMap(CounterInt p_updateIndexInMap_1_) {
		if (this.indexInMap < 0) {
			this.indexInMap = p_updateIndexInMap_1_.nextValue();
		}
	}

	public double getSpriteU16(float p_getSpriteU16_1_) {
		float f = this.maxU - this.minU;
		return (double) ((p_getSpriteU16_1_ - this.minU) / f * 16.0F);
	}

	public double getSpriteV16(float p_getSpriteV16_1_) {
		float f = this.maxV - this.minV;
		return (double) ((p_getSpriteV16_1_ - this.minV) / f * 16.0F);
	}
}