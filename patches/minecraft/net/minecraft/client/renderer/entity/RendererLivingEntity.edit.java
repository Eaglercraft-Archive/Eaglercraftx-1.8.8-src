
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;

> INSERT  1 : 10  @  1

+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  5  @  5 : 7

> DELETE  1  @  1 : 4

> DELETE  10  @  10 : 13

> CHANGE  16 : 17  @  16 : 17

~ 		return this.layerRenderers.add((LayerRenderer<T>) layer);

> CHANGE  194 : 201  @  194 : 250

~ 			GlStateManager.enableShaderBlendAdd();
~ 			float f1 = 1.0F - (float) (i >> 24 & 255) / 255.0F;
~ 			float f2 = (float) (i >> 16 & 255) / 255.0F;
~ 			float f3 = (float) (i >> 8 & 255) / 255.0F;
~ 			float f4 = (float) (i & 255) / 255.0F;
~ 			GlStateManager.setShaderBlendSrc(f1, f1, f1, 1.0F);
~ 			GlStateManager.setShaderBlendAdd(f2 * f1 + 0.4F, f3 * f1, f4 * f1, 0.0f);

> CHANGE  5 : 6  @  5 : 42

~ 		GlStateManager.disableShaderBlendAdd();

> CHANGE  73 : 74  @  73 : 74

~ 					EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);

> EOF
