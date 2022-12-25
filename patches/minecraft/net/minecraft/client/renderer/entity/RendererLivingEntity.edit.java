
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;

> INSERT  4 : 13  @  5

+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  18  @  10 : 12

> DELETE  19  @  13 : 16

> DELETE  29  @  26 : 29

> CHANGE  45 : 46  @  45 : 46

~ 		return this.layerRenderers.add((LayerRenderer<T>) layer);

> CHANGE  240 : 247  @  240 : 296

~ 			GlStateManager.enableShaderBlendAdd();
~ 			float f1 = 1.0F - (float) (i >> 24 & 255) / 255.0F;
~ 			float f2 = (float) (i >> 16 & 255) / 255.0F;
~ 			float f3 = (float) (i >> 8 & 255) / 255.0F;
~ 			float f4 = (float) (i & 255) / 255.0F;
~ 			GlStateManager.setShaderBlendSrc(f1, f1, f1, 1.0F);
~ 			GlStateManager.setShaderBlendAdd(f2 * f1 + 0.4F, f3 * f1, f4 * f1, 0.0f);

> CHANGE  252 : 253  @  301 : 338

~ 		GlStateManager.disableShaderBlendAdd();

> CHANGE  326 : 327  @  411 : 412

~ 					EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);

> EOF
