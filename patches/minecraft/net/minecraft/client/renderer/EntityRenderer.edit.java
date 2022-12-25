
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;

> CHANGE  4 : 6  @  8 : 9

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.HString;

> INSERT  7 : 19  @  10

+ 
+ import com.google.common.base.Predicate;
+ import com.google.common.base.Predicates;
+ 
+ import net.lax1dude.eaglercraft.v1_8.Display;
+ import net.lax1dude.eaglercraft.v1_8.Mouse;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  29  @  20 : 29

> DELETE  36  @  36 : 38

> DELETE  43  @  45 : 48

> DELETE  63  @  68 : 75

> CHANGE  72 : 73  @  84 : 85

~ 	private EaglercraftRandom random = new EaglercraftRandom();

> DELETE  116  @  128 : 129

> DELETE  146  @  159 : 160

> INSERT  147 : 156  @  161

+ 		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
+ 		GlStateManager.matrixMode(5890);
+ 		GlStateManager.loadIdentity();
+ 		float f3 = 0.00390625F;
+ 		GlStateManager.scale(f3, f3, f3);
+ 		GlStateManager.translate(8.0F, 8.0F, 8.0F);
+ 		GlStateManager.matrixMode(5888);
+ 		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
+ 

> CHANGE  169 : 170  @  174 : 175

~ 		return false;

> DELETE  173  @  178 : 184

> DELETE  180  @  191 : 206

> DELETE  183  @  209 : 224

> CHANGE  186 : 187  @  227 : 242

~ 		this.useShader = false;

> DELETE  190  @  245 : 256

> DELETE  193  @  259 : 263

> DELETE  237  @  307 : 311

> DELETE  238  @  312 : 319

> CHANGE  356 : 357  @  437 : 438

~ 				f = this.mc.gameSettings.keyBindZoomCamera.isKeyDown() ? 17.0f : this.mc.gameSettings.fovSetting;

> CHANGE  526 : 527  @  607 : 608

~ 		GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> CHANGE  528 : 529  @  609 : 610

~ 				this.farPlaneDistance * 2.0f * MathHelper.SQRT_2);

> CHANGE  586 : 587  @  667 : 668

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, false),

> DELETE  630  @  711 : 723

> CHANGE  751 : 752  @  844 : 845

~ 					this.lightmapColors[i] = short1 << 24 | j | k << 8 | l << 16;

> INSERT  755 : 769  @  848

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

> DELETE  792  @  871 : 876

> INSERT  796 : 799  @  880

+ 			if (this.mc.gameSettings.keyBindZoomCamera.isKeyDown()) {
+ 				f *= 0.7f;
+ 			}

> DELETE  838  @  919 : 932

> CHANGE  866 : 867  @  960 : 961

~ 							return EntityRenderer.this.mc.currentScreen.getClass().getName();

> CHANGE  871 : 872  @  965 : 966

~ 							return HString.format("Scaled: (%d, %d). Absolute: (%d, %d)",

> CHANGE  878 : 879  @  972 : 973

~ 							return HString.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d",

> DELETE  894  @  988 : 990

> CHANGE  926 : 927  @  1022 : 1023

~ 			EaglercraftGPU.glLineWidth(1.0F);

> CHANGE  997 : 999  @  1093 : 1095

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),
~ 					(float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * 4.0F);

> CHANGE  1003 : 1004  @  1099 : 1100

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> INSERT  1031 : 1032  @  1127

+ 		GlStateManager.disableBlend();

> DELETE  1038  @  1133 : 1134

> INSERT  1039 : 1040  @  1135

+ 		GlStateManager.shadeModel(7424);

> CHANGE  1131 : 1132  @  1226 : 1227

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> CHANGE  1141 : 1142  @  1236 : 1237

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> CHANGE  1229 : 1230  @  1324 : 1325

~ 			EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);

> CHANGE  1277 : 1278  @  1372 : 1373

~ 							if (f2 >= 0.15F) {

> CHANGE  1514 : 1515  @  1609 : 1610

~ 		GlStateManager.clearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F);

> CHANGE  1524 : 1527  @  1619 : 1621

~ 		EaglercraftGPU.glFog(GL_FOG_COLOR,
~ 				this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
~ 		EaglercraftGPU.glNormal3f(0.0F, -1.0F, 0.0F);

> CHANGE  1544 : 1545  @  1638 : 1642

~ 			EaglercraftGPU.glFogi('\u855a', '\u855b');

> INSERT  1559 : 1562  @  1656

+ 		} else if (!this.mc.gameSettings.fog) {
+ 			GlStateManager.setFog(2048);
+ 			GlStateManager.setFogDensity(0.0F);

> INSERT  1563 : 1564  @  1657

+ 			GlStateManager.setFogDensity(0.001F);

> CHANGE  1574 : 1575  @  1667 : 1670

~ 			EaglercraftGPU.glFogi('\u855a', '\u855b');

> DELETE  1584  @  1679 : 1680

> EOF
