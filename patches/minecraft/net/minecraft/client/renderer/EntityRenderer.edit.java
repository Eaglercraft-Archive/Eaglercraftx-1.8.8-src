
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;

> CHANGE  1 : 3  @  1 : 2

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.HString;

> INSERT  1 : 15  @  1

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
+ import net.lax1dude.eaglercraft.v1_8.opengl.GameOverlayFramebuffer;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  10  @  10 : 19

> DELETE  7  @  7 : 9

> DELETE  7  @  7 : 10

> DELETE  20  @  20 : 27

> CHANGE  9 : 10  @  9 : 10

~ 	private EaglercraftRandom random = new EaglercraftRandom();

> DELETE  43  @  43 : 61

> INSERT  1 : 2  @  1

+ 	private GameOverlayFramebuffer overlayFramebuffer;

> DELETE  2  @  2 : 4

> CHANGE  8 : 9  @  8 : 9

~ 		this.overlayFramebuffer = new GameOverlayFramebuffer();

> INSERT  1 : 10  @  1

+ 		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
+ 		GlStateManager.matrixMode(5890);
+ 		GlStateManager.loadIdentity();
+ 		float f3 = 0.00390625F;
+ 		GlStateManager.scale(f3, f3, f3);
+ 		GlStateManager.translate(8.0F, 8.0F, 8.0F);
+ 		GlStateManager.matrixMode(5888);
+ 		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
+ 

> CHANGE  13 : 14  @  13 : 14

~ 		return false;

> DELETE  3  @  3 : 9

> DELETE  3  @  3 : 4

> DELETE  3  @  3 : 18

> DELETE  3  @  3 : 18

> DELETE  3  @  3 : 18

> DELETE  3  @  3 : 14

> DELETE  3  @  3 : 7

> DELETE  44  @  44 : 48

> DELETE  1  @  1 : 8

> CHANGE  118 : 119  @  118 : 119

~ 				f = this.mc.gameSettings.keyBindZoomCamera.isKeyDown() ? 17.0f : this.mc.gameSettings.fovSetting;

> CHANGE  169 : 170  @  169 : 170

~ 		GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> CHANGE  1 : 2  @  1 : 2

~ 				this.farPlaneDistance * 2.0f * MathHelper.SQRT_2);

> CHANGE  57 : 58  @  57 : 58

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, false),

> DELETE  43  @  43 : 55

> CHANGE  121 : 122  @  121 : 122

~ 					this.lightmapColors[i] = short1 << 24 | j | k << 8 | l << 16;

> INSERT  3 : 17  @  3

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

> DELETE  23  @  23 : 28

> INSERT  4 : 7  @  4

+ 			if (this.mc.gameSettings.keyBindZoomCamera.isKeyDown()) {
+ 				f *= 0.7f;
+ 			}

> DELETE  39  @  39 : 52

> CHANGE  4 : 43  @  4 : 5

~ 					long framebufferAge = this.overlayFramebuffer.getAge();
~ 					if (framebufferAge == -1l || framebufferAge > (Minecraft.getDebugFPS() < 25 ? 125l : 75l)) {
~ 						this.overlayFramebuffer.beginRender(mc.displayWidth, mc.displayHeight);
~ 						GlStateManager.colorMask(true, true, true, true);
~ 						GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
~ 						GlStateManager.clear(16640);
~ 						GlStateManager.enableOverlayFramebufferBlending();
~ 						this.mc.ingameGUI.renderGameOverlay(parFloat1);
~ 						GlStateManager.disableOverlayFramebufferBlending();
~ 						this.overlayFramebuffer.endRender();
~ 					}
~ 					this.setupOverlayRendering();
~ 					GlStateManager.enableBlend();
~ 					if (Minecraft.isFancyGraphicsEnabled()) {
~ 						this.mc.ingameGUI.renderVignette(parFloat1, l, i1);
~ 					}
~ 					this.mc.ingameGUI.renderGameOverlayCrosshairs(l, i1);
~ 					GlStateManager.bindTexture(this.overlayFramebuffer.getTexture());
~ 					GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
~ 					GlStateManager.enableBlend();
~ 					GlStateManager.blendFunc(1, 771);
~ 					GlStateManager.disableAlpha();
~ 					GlStateManager.disableDepth();
~ 					GlStateManager.depthMask(false);
~ 					Tessellator tessellator = Tessellator.getInstance();
~ 					WorldRenderer worldrenderer = tessellator.getWorldRenderer();
~ 					worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
~ 					worldrenderer.pos(0.0D, (double) i1, -90.0D).tex(0.0D, 0.0D).endVertex();
~ 					worldrenderer.pos((double) l, (double) i1, -90.0D).tex(1.0D, 0.0D).endVertex();
~ 					worldrenderer.pos((double) l, 0.0D, -90.0D).tex(1.0D, 1.0D).endVertex();
~ 					worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 1.0D).endVertex();
~ 					tessellator.draw();
~ 					GlStateManager.depthMask(true);
~ 					GlStateManager.enableDepth();
~ 					GlStateManager.enableAlpha();
~ 					GlStateManager.disableBlend();
~ 					if (this.mc.gameSettings.hudPlayer) { // give the player model HUD good fps
~ 						this.mc.ingameGUI.drawEaglerPlayerOverlay(l - 3, 3, parFloat1);
~ 					}

> CHANGE  23 : 24  @  23 : 24

~ 							return EntityRenderer.this.mc.currentScreen.getClass().getName();

> CHANGE  4 : 5  @  4 : 5

~ 							return HString.format("Scaled: (%d, %d). Absolute: (%d, %d)",

> CHANGE  6 : 7  @  6 : 7

~ 							return HString.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d",

> DELETE  15  @  15 : 17

> CHANGE  32 : 33  @  32 : 33

~ 			EaglercraftGPU.glLineWidth(1.0F);

> INSERT  25 : 32  @  25

+ 
+ 		boolean fxaa = (this.mc.gameSettings.fxaa == 0 && this.mc.gameSettings.fancyGraphics)
+ 				|| this.mc.gameSettings.fxaa == 1;
+ 		if (fxaa) {
+ 			EffectPipelineFXAA.begin(this.mc.displayWidth, this.mc.displayHeight);
+ 		}
+ 

> INSERT  16 : 20  @  16

+ 		if (fxaa) {
+ 			EffectPipelineFXAA.end();
+ 		}
+ 

> CHANGE  29 : 31  @  29 : 31

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),
~ 					(float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * 4.0F);

> CHANGE  4 : 5  @  4 : 5

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> INSERT  27 : 28  @  27

+ 		GlStateManager.disableBlend();

> DELETE  6  @  6 : 7

> INSERT  1 : 2  @  1

+ 		GlStateManager.shadeModel(7424);

> CHANGE  91 : 92  @  91 : 92

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> CHANGE  9 : 10  @  9 : 10

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> CHANGE  87 : 88  @  87 : 88

~ 			EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);

> CHANGE  47 : 48  @  47 : 48

~ 							if (f2 >= 0.15F) {

> CHANGE  236 : 237  @  236 : 237

~ 		GlStateManager.clearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F);

> CHANGE  9 : 12  @  9 : 11

~ 		EaglercraftGPU.glFog(GL_FOG_COLOR,
~ 				this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
~ 		EaglercraftGPU.glNormal3f(0.0F, -1.0F, 0.0F);

> CHANGE  17 : 18  @  17 : 21

~ 			EaglercraftGPU.glFogi('\u855a', '\u855b');

> INSERT  14 : 17  @  14

+ 		} else if (!this.mc.gameSettings.fog) {
+ 			GlStateManager.setFog(2048);
+ 			GlStateManager.setFogDensity(0.0F);

> INSERT  1 : 2  @  1

+ 			GlStateManager.setFogDensity(0.001F);

> CHANGE  10 : 11  @  10 : 13

~ 			EaglercraftGPU.glFogi('\u855a', '\u855b');

> DELETE  9  @  9 : 10

> EOF
