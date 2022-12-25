
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 5  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.IAcceleratedParticleEngine;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  6  @  3 : 5

> CHANGE  24 : 25  @  23 : 24

~ 	protected EaglerTextureAtlasSprite particleIcon;

> INSERT  177 : 189  @  176

+ 	public boolean renderAccelerated(IAcceleratedParticleEngine accelerator, Entity var2, float f, float f1, float f2,
+ 			float f3, float f4, float f5) {
+ 		if (getFXLayer() == 3) {
+ 			return false;
+ 		} else {
+ 			accelerator.drawParticle(this, particleTextureIndexX * 16, particleTextureIndexY * 16,
+ 					getBrightnessForRender(f), 16, particleScale * 0.1f, this.particleRed, this.particleGreen,
+ 					this.particleBlue, this.particleAlpha);
+ 			return true;
+ 		}
+ 	}
+ 

> CHANGE  199 : 200  @  186 : 187

~ 	public void setParticleIcon(EaglerTextureAtlasSprite icon) {

> EOF
