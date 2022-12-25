
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 7  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  13  @  8 : 13

> DELETE  18  @  18 : 19

> DELETE  30  @  31 : 32

> CHANGE  56 : 59  @  58 : 61

~ //				if (this.isBlockTranslucent(block)) { //TODO: figure out why this code exists, it breaks slime blocks
~ //					GlStateManager.depthMask(false);
~ //				}

> CHANGE  62 : 65  @  64 : 67

~ //			if (this.isBlockTranslucent(block)) {
~ //				GlStateManager.depthMask(true);
~ //			}

> CHANGE  166 : 167  @  168 : 169

~ 		EaglercraftGPU.glNormal3f(0.0F, 0.0F, -1.0F);

> CHANGE  361 : 362  @  363 : 364

~ 	private void func_178108_a(float parFloat1, EaglerTextureAtlasSprite parTextureAtlasSprite) {

> CHANGE  427 : 428  @  429 : 430

~ 			EaglerTextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks()

> EOF
