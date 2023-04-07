
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.internal.IFramebufferGL;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;

> DELETE  1  @  1 : 3

> CHANGE  4 : 5  @  4 : 5

~ public class TextureCompass extends EaglerTextureAtlasSprite {

> CHANGE  9 : 10  @  9 : 10

~ 	public void updateAnimation(IFramebufferGL[] copyColorFramebuffer) {

> CHANGE  3 : 4  @  3 : 4

~ 					(double) minecraft.thePlayer.rotationYaw, false, false, copyColorFramebuffer);

> CHANGE  1 : 2  @  1 : 2

~ 			this.updateCompass((World) null, 0.0D, 0.0D, 0.0D, true, false, copyColorFramebuffer);

> CHANGE  5 : 6  @  5 : 6

~ 			boolean parFlag2, IFramebufferGL[] copyColorFramebuffer) {

> CHANGE  40 : 42  @  40 : 42

~ 				animationCache.copyFrameLevelsToTex2D(this.frameCounter, this.originX, this.originY, this.width,
~ 						this.height, copyColorFramebuffer);

> INSERT  4 : 5  @  4

+ 

> EOF
