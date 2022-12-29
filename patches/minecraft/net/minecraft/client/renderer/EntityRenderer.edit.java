
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;

> CHANGE  4 : 6  @  8 : 9

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.HString;

> INSERT  7 : 20  @  10

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

> DELETE  30  @  20 : 29

> DELETE  37  @  36 : 38

> DELETE  44  @  45 : 48

> DELETE  64  @  68 : 75

> CHANGE  73 : 74  @  84 : 85

~ 	private EaglercraftRandom random = new EaglercraftRandom();

> DELETE  117  @  128 : 129

> DELETE  147  @  159 : 160

> INSERT  148 : 157  @  161

+ 		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
+ 		GlStateManager.matrixMode(5890);
+ 		GlStateManager.loadIdentity();
+ 		float f3 = 0.00390625F;
+ 		GlStateManager.scale(f3, f3, f3);
+ 		GlStateManager.translate(8.0F, 8.0F, 8.0F);
+ 		GlStateManager.matrixMode(5888);
+ 		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
+ 

> CHANGE  170 : 171  @  174 : 175

~ 		return false;

> DELETE  174  @  178 : 184

> DELETE  181  @  191 : 206

> DELETE  184  @  209 : 224

> CHANGE  187 : 188  @  227 : 242

~ 		this.useShader = false;

> DELETE  191  @  245 : 256

> DELETE  194  @  259 : 263

> DELETE  238  @  307 : 311

> DELETE  239  @  312 : 319

> CHANGE  357 : 358  @  437 : 438

~ 				f = this.mc.gameSettings.keyBindZoomCamera.isKeyDown() ? 17.0f : this.mc.gameSettings.fovSetting;

> CHANGE  527 : 528  @  607 : 608

~ 		GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> CHANGE  529 : 530  @  609 : 610

~ 				this.farPlaneDistance * 2.0f * MathHelper.SQRT_2);

> CHANGE  587 : 588  @  667 : 668

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, false),

> DELETE  631  @  711 : 723

> CHANGE  752 : 753  @  844 : 845

~ 					this.lightmapColors[i] = short1 << 24 | j | k << 8 | l << 16;

> INSERT  756 : 770  @  848

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

> DELETE  793  @  871 : 876

> INSERT  797 : 800  @  880

+ 			if (this.mc.gameSettings.keyBindZoomCamera.isKeyDown()) {
+ 				f *= 0.7f;
+ 			}

> DELETE  839  @  919 : 932

> CHANGE  867 : 868  @  960 : 961

~ 							return EntityRenderer.this.mc.currentScreen.getClass().getName();

> CHANGE  872 : 873  @  965 : 966

~ 							return HString.format("Scaled: (%d, %d). Absolute: (%d, %d)",

> CHANGE  879 : 880  @  972 : 973

~ 							return HString.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d",

> DELETE  895  @  988 : 990

> CHANGE  927 : 928  @  1022 : 1023

~ 			EaglercraftGPU.glLineWidth(1.0F);

> INSERT  953 : 958  @  1048

+ 
+ 		if (this.mc.gameSettings.fxaa) {
+ 			EffectPipelineFXAA.begin(this.mc.displayWidth, this.mc.displayHeight);
+ 		}
+ 

> INSERT  974 : 978  @  1064

+ 		if (this.mc.gameSettings.fxaa) {
+ 			EffectPipelineFXAA.end();
+ 		}
+ 

> CHANGE  1007 : 1009  @  1093 : 1095

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),
~ 					(float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * 4.0F);

> CHANGE  1013 : 1014  @  1099 : 1100

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> INSERT  1041 : 1042  @  1127

+ 		GlStateManager.disableBlend();

> DELETE  1048  @  1133 : 1134

> INSERT  1049 : 1050  @  1135

+ 		GlStateManager.shadeModel(7424);

> CHANGE  1141 : 1142  @  1226 : 1227

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> CHANGE  1151 : 1152  @  1236 : 1237

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> CHANGE  1239 : 1240  @  1324 : 1325

~ 			EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);

> CHANGE  1287 : 1288  @  1372 : 1373

~ 							if (f2 >= 0.15F) {

> CHANGE  1524 : 1525  @  1609 : 1610

~ 		GlStateManager.clearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F);

> CHANGE  1534 : 1537  @  1619 : 1621

~ 		EaglercraftGPU.glFog(GL_FOG_COLOR,
~ 				this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
~ 		EaglercraftGPU.glNormal3f(0.0F, -1.0F, 0.0F);

> CHANGE  1554 : 1555  @  1638 : 1642

~ 			EaglercraftGPU.glFogi('\u855a', '\u855b');

> INSERT  1569 : 1572  @  1656

+ 		} else if (!this.mc.gameSettings.fog) {
+ 			GlStateManager.setFog(2048);
+ 			GlStateManager.setFogDensity(0.0F);

> INSERT  1573 : 1574  @  1657

+ 			GlStateManager.setFogDensity(0.001F);

> CHANGE  1584 : 1585  @  1667 : 1670

~ 			EaglercraftGPU.glFogi('\u855a', '\u855b');

> DELETE  1594  @  1679 : 1680

> EOF
