package net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture;

import net.lax1dude.eaglercraft.v1_8.internal.IFramebufferGL;
import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

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
public class TextureClockPBRImpl extends EaglerTextureAtlasSpritePBR {
	private double smoothParam1;
	private double smoothParam2;

	public TextureClockPBRImpl(String spriteName) {
		super(spriteName);
	}

	public void updateAnimationPBR(IFramebufferGL[] copyColorFramebuffer, IFramebufferGL[] copyMaterialFramebuffer, int materialTexOffset) {
		if (!this.frameTextureDataPBR[0].isEmpty()) {
			Minecraft minecraft = Minecraft.getMinecraft();
			double d0 = 0.0;
			if (minecraft.theWorld != null && minecraft.thePlayer != null) {
				d0 = (double) minecraft.theWorld.getCelestialAngle(1.0f);
				if (!minecraft.theWorld.provider.isSurfaceWorld()) {
					d0 = Math.random();
				}
			}

			double d1;
			for (d1 = d0 - this.smoothParam1; d1 < -0.5; ++d1) {
				;
			}

			while (d1 >= 0.5) {
				--d1;
			}

			d1 = MathHelper.clamp_double(d1, -1.0, 1.0);
			this.smoothParam2 += d1 * 0.1;
			this.smoothParam2 *= 0.8;
			this.smoothParam1 += this.smoothParam2;

			int i, frameCount = this.frameTextureDataPBR[0].size();
			for (i = (int) ((this.smoothParam1 + 1.0) * frameCount) % frameCount; i < 0; i = (i + frameCount) % frameCount) {
				;
			}

			if (i != this.frameCounter) {
				this.frameCounter = i;
				animationCachePBR[0].copyFrameLevelsToTex2D(this.frameCounter, this.originX, this.originY, this.width,
						this.height, copyColorFramebuffer);
				if (!dontAnimateNormals)
					animationCachePBR[1].copyFrameLevelsToTex2D(this.frameCounter, this.originX, this.originY,
							this.width, this.height, copyMaterialFramebuffer);
				if (!dontAnimateMaterial)
					animationCachePBR[2].copyFrameLevelsToTex2D(this.frameCounter, this.originX,
							this.originY + materialTexOffset, this.width, this.height, copyMaterialFramebuffer);
			}

		}
	}

}
