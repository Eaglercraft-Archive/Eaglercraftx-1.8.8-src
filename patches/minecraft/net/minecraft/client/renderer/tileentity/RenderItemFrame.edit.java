
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  3  @  3 : 4

> DELETE  2  @  2 : 3

> DELETE  4  @  4 : 5

> DELETE  16  @  16 : 17

> INSERT  36 : 38  @  36

+ 		GlStateManager.enableLighting();
+ 		GlStateManager.enableColorMaterial();

> INSERT  7 : 9  @  7

+ 		GlStateManager.enableLighting();
+ 		GlStateManager.enableColorMaterial();

> DELETE  33  @  33 : 54

> CHANGE  5 : 6  @  5 : 6

~ 				GlStateManager.pushLightCoords();

> CHANGE  3 : 4  @  3 : 7

~ 				GlStateManager.popLightCoords();

> CHANGE  21 : 22  @  21 : 22

~ 					EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);

> EOF
