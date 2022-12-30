
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;

> CHANGE  2 : 4  @  6 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.HString;

> INSERT  3 : 16  @  2

+ 
+ import com.google.common.base.Predicate;
+ import com.google.common.base.Predicates;
+ 
+ import net.lax1dude.eaglercraft.v1_8.Display;
+ import net.lax1dude.eaglercraft.v1_8.Mouse;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.EffectPipelineFXAA;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  23  @  10 : 19

> DELETE  7  @  16 : 18

> DELETE  7  @  9 : 12

> DELETE  20  @  23 : 30

> CHANGE  9 : 10  @  16 : 17

~ 	private EaglercraftRandom random = new EaglercraftRandom();

> DELETE  44  @  44 : 45

> DELETE  30  @  31 : 32

> INSERT  1 : 10  @  2

+ 		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
+ 		GlStateManager.matrixMode(5890);
+ 		GlStateManager.loadIdentity();
+ 		float f3 = 0.00390625F;
+ 		GlStateManager.scale(f3, f3, f3);
+ 		GlStateManager.translate(8.0F, 8.0F, 8.0F);
+ 		GlStateManager.matrixMode(5888);
+ 		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
+ 

> CHANGE  22 : 23  @  13 : 14

~ 		return false;

> DELETE  4  @  4 : 10

> DELETE  7  @  13 : 28

> DELETE  3  @  18 : 33

> CHANGE  3 : 4  @  18 : 33

~ 		this.useShader = false;

> DELETE  4  @  18 : 29

> DELETE  3  @  14 : 18

> DELETE  44  @  48 : 52

> DELETE  1  @  5 : 12

> CHANGE  118 : 119  @  125 : 126

~ 				f = this.mc.gameSettings.keyBindZoomCamera.isKeyDown() ? 17.0f : this.mc.gameSettings.fovSetting;

> CHANGE  170 : 171  @  170 : 171

~ 		GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> CHANGE  2 : 3  @  2 : 3

~ 				this.farPlaneDistance * 2.0f * MathHelper.SQRT_2);

> CHANGE  58 : 59  @  58 : 59

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, false),

> DELETE  44  @  44 : 56

> CHANGE  121 : 122  @  133 : 134

~ 					this.lightmapColors[i] = short1 << 24 | j | k << 8 | l << 16;

> INSERT  4 : 18  @  4

+ 
+ 				GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
+ 				this.mc.getTextureManager().bindTexture(this.locationLightMap);
+ 				if (mc.gameSettings.fancyGraphics || mc.gameSettings.ambientOcclusion > 0) {
+ 					EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
+ 					EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
+ 				} else {
+ 					EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
+ 					EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
+ 				}
+ 				EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
+ 				EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
+ 				GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
+ 

> DELETE  37  @  23 : 28

> INSERT  4 : 7  @  9

+ 			if (this.mc.gameSettings.keyBindZoomCamera.isKeyDown()) {
+ 				f *= 0.7f;
+ 			}

> DELETE  42  @  39 : 52

> CHANGE  28 : 29  @  41 : 42

~ 							return EntityRenderer.this.mc.currentScreen.getClass().getName();

> CHANGE  5 : 6  @  5 : 6

~ 							return HString.format("Scaled: (%d, %d). Absolute: (%d, %d)",

> CHANGE  7 : 8  @  7 : 8

~ 							return HString.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d",

> DELETE  16  @  16 : 18

> CHANGE  32 : 33  @  34 : 35

~ 			EaglercraftGPU.glLineWidth(1.0F);

> INSERT  26 : 33  @  26

+ 
+ 		boolean fxaa = (this.mc.gameSettings.fxaa == 0 && this.mc.gameSettings.fancyGraphics)
+ 				|| this.mc.gameSettings.fxaa == 1;
+ 		if (fxaa) {
+ 			EffectPipelineFXAA.begin(this.mc.displayWidth, this.mc.displayHeight);
+ 		}
+ 

> INSERT  23 : 27  @  16

+ 		if (fxaa) {
+ 			EffectPipelineFXAA.end();
+ 		}
+ 

> CHANGE  33 : 35  @  29 : 31

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),
~ 					(float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * 4.0F);

> CHANGE  6 : 7  @  6 : 7

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> INSERT  28 : 29  @  28

+ 		GlStateManager.disableBlend();

> DELETE  7  @  6 : 7

> INSERT  1 : 2  @  2

+ 		GlStateManager.shadeModel(7424);

> CHANGE  92 : 93  @  91 : 92

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> CHANGE  10 : 11  @  10 : 11

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> CHANGE  88 : 89  @  88 : 89

~ 			EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);

> CHANGE  48 : 49  @  48 : 49

~ 							if (f2 >= 0.15F) {

> CHANGE  237 : 238  @  237 : 238

~ 		GlStateManager.clearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F);

> CHANGE  10 : 13  @  10 : 12

~ 		EaglercraftGPU.glFog(GL_FOG_COLOR,
~ 				this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
~ 		EaglercraftGPU.glNormal3f(0.0F, -1.0F, 0.0F);

> CHANGE  20 : 21  @  19 : 23

~ 			EaglercraftGPU.glFogi('\u855a', '\u855b');

> INSERT  15 : 18  @  18

+ 		} else if (!this.mc.gameSettings.fog) {
+ 			GlStateManager.setFog(2048);
+ 			GlStateManager.setFogDensity(0.0F);

> INSERT  4 : 5  @  1

+ 			GlStateManager.setFogDensity(0.001F);

> CHANGE  11 : 12  @  10 : 13

~ 			EaglercraftGPU.glFogi('\u855a', '\u855b');

> DELETE  10  @  12 : 13

> EOF
