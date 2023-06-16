
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.IAcceleratedParticleEngine;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  1  @  1 : 5

> INSERT  64 : 73  @  64

+ 
+ 		public boolean renderAccelerated(IAcceleratedParticleEngine accelerator, Entity var2, float f, float f1,
+ 				float f2, float f3, float f4, float f5) {
+ 			accelerator.drawParticle(this, 64, 32, getBrightnessForRender(f), 64,
+ 					7.1F * MathHelper.sin(((float) this.particleAge + f - 1.0F) * 0.25F * 3.1415927F) * 0.0625f * 0.25f,
+ 					this.particleRed, this.particleGreen, this.particleBlue,
+ 					0.6F - ((float) this.particleAge + f - 1.0F) * 0.25F * 0.5F);
+ 			return true;
+ 		}

> INSERT  222 : 223  @  222

+ 				entityfirework$overlayfx.particleAlpha = 0.99f;

> EOF
