
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  9  @  5 : 6

> DELETE  11  @  8 : 9

> DELETE  15  @  13 : 14

> DELETE  31  @  30 : 31

> INSERT  67 : 69  @  67

+ 		GlStateManager.enableLighting();
+ 		GlStateManager.enableColorMaterial();

> INSERT  76 : 78  @  74

+ 		GlStateManager.enableLighting();
+ 		GlStateManager.enableColorMaterial();

> DELETE  111  @  107 : 128

> CHANGE  116 : 117  @  133 : 134

~ 				GlStateManager.pushLightCoords();

> CHANGE  120 : 121  @  137 : 141

~ 				GlStateManager.popLightCoords();

> CHANGE  142 : 143  @  162 : 163

~ 					EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);

> EOF
