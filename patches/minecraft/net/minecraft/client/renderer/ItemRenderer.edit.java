
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

> DELETE  11  @  6 : 11

> DELETE  5  @  10 : 11

> DELETE  12  @  13 : 14

> CHANGE  26 : 29  @  27 : 30

~ //				if (this.isBlockTranslucent(block)) { //TODO: figure out why this code exists, it breaks slime blocks
~ //					GlStateManager.depthMask(false);
~ //				}

> CHANGE  6 : 9  @  6 : 9

~ //			if (this.isBlockTranslucent(block)) {
~ //				GlStateManager.depthMask(true);
~ //			}

> CHANGE  104 : 105  @  104 : 105

~ 		EaglercraftGPU.glNormal3f(0.0F, 0.0F, -1.0F);

> CHANGE  195 : 196  @  195 : 196

~ 	private void func_178108_a(float parFloat1, EaglerTextureAtlasSprite parTextureAtlasSprite) {

> CHANGE  66 : 67  @  66 : 67

~ 			EaglerTextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks()

> EOF
