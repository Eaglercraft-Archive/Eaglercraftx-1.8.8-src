
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  7  @  3 : 4

> DELETE  2  @  3 : 4

> DELETE  4  @  5 : 6

> DELETE  16  @  17 : 18

> INSERT  36 : 38  @  37

+ 		GlStateManager.enableLighting();
+ 		GlStateManager.enableColorMaterial();

> INSERT  9 : 11  @  7

+ 		GlStateManager.enableLighting();
+ 		GlStateManager.enableColorMaterial();

> DELETE  35  @  33 : 54

> CHANGE  5 : 6  @  26 : 27

~ 				GlStateManager.pushLightCoords();

> CHANGE  4 : 5  @  4 : 8

~ 				GlStateManager.popLightCoords();

> CHANGE  22 : 23  @  25 : 26

~ 					EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);

> EOF
