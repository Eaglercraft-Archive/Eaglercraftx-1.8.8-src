
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 7  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  6  @  6 : 11

> DELETE  5  @  5 : 6

> DELETE  12  @  12 : 13

> CHANGE  26 : 29  @  26 : 29

~ //				if (this.isBlockTranslucent(block)) { //TODO: figure out why this code exists, it breaks slime blocks
~ //					GlStateManager.depthMask(false);
~ //				}

> CHANGE  3 : 6  @  3 : 6

~ //			if (this.isBlockTranslucent(block)) {
~ //				GlStateManager.depthMask(true);
~ //			}

> CHANGE  101 : 102  @  101 : 102

~ 		EaglercraftGPU.glNormal3f(0.0F, 0.0F, -1.0F);

> CHANGE  194 : 195  @  194 : 195

~ 	private void func_178108_a(float parFloat1, EaglerTextureAtlasSprite parTextureAtlasSprite) {

> CHANGE  65 : 66  @  65 : 66

~ 			EaglerTextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks()

> EOF
