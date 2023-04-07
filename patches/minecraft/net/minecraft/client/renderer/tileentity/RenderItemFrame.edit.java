
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 7  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.NameTagRenderer;

> DELETE  3  @  3 : 4

> DELETE  2  @  2 : 3

> DELETE  4  @  4 : 6

> DELETE  12  @  12 : 13

> DELETE  2  @  2 : 3

> INSERT  36 : 38  @  36

+ 		GlStateManager.enableLighting();
+ 		GlStateManager.enableColorMaterial();

> INSERT  7 : 9  @  7

+ 		GlStateManager.enableLighting();
+ 		GlStateManager.enableColorMaterial();

> CHANGE  33 : 37  @  33 : 52

~ 				boolean emissive = itemFrame.eaglerEmissiveFlag;
~ 				itemFrame.eaglerEmissiveFlag = false;
~ 				if (emissive) {
~ 					DeferredStateManager.setEmissionConstant(1.0f);

> DELETE  1  @  1 : 2

> CHANGE  5 : 6  @  5 : 6

~ 				GlStateManager.pushLightCoords();

> CHANGE  3 : 6  @  3 : 6

~ 				GlStateManager.popLightCoords();
~ 				if (emissive) {
~ 					DeferredStateManager.setEmissionConstant(0.0f);

> INSERT  19 : 23  @  19

+ 					if (DeferredStateManager.isInDeferredPass()) {
+ 						NameTagRenderer.renderNameTag(entityitemframe, null, d0, d1, d2, -69);
+ 						return;
+ 					}

> CHANGE  3 : 4  @  3 : 4

~ 					EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);

> EOF
