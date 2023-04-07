
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.IAcceleratedParticleEngine;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  3  @  3 : 6

> INSERT  3 : 5  @  3

+ import net.minecraft.util.EnumWorldBlockLayer;
+ import net.minecraft.util.MathHelper;

> INSERT  15 : 16  @  15

+ 		this.particleAlpha = state.getBlock().getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT ? 0.999f : 1.0f;

> INSERT  75 : 88  @  75

+ 	public boolean renderAccelerated(IAcceleratedParticleEngine accelerator, Entity var2, float f, float f1, float f2,
+ 			float f3, float f4, float f5) {
+ 		int w = this.particleIcon.getIconWidth();
+ 		int h = this.particleIcon.getIconHeight();
+ 		int xOffset = MathHelper.floor_float(w * this.particleTextureJitterX * 4.0f * 0.0625f);
+ 		int yOffset = MathHelper.floor_float(h * this.particleTextureJitterY * 4.0f * 0.0625f);
+ 		int texSize = Math.min(w, h) / 4;
+ 		accelerator.drawParticle(this, this.particleIcon.getOriginX() + xOffset,
+ 				this.particleIcon.getOriginY() + yOffset, getBrightnessForRender(f), texSize, particleScale * 0.1f,
+ 				this.particleRed, this.particleGreen, this.particleBlue, 1.0f);
+ 		return true;
+ 	}
+ 

> EOF
