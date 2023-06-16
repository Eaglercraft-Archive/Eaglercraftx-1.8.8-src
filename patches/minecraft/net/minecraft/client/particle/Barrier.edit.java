
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.IAcceleratedParticleEngine;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  1  @  1 : 4

> INSERT  54 : 62  @  54

+ 	public boolean renderAccelerated(IAcceleratedParticleEngine accelerator, Entity var2, float f, float f1, float f2,
+ 			float f3, float f4, float f5) {
+ 		accelerator.drawParticle(this, particleIcon.getOriginX(), particleIcon.getOriginY(), getBrightnessForRender(f),
+ 				Math.min(particleIcon.getIconWidth(), particleIcon.getIconHeight()), 0.5f, this.particleRed,
+ 				this.particleGreen, this.particleBlue, this.particleAlpha);
+ 		return true;
+ 	}
+ 

> EOF
