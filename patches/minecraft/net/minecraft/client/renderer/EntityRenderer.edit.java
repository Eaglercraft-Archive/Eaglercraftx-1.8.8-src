
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
~ 
~ import java.util.Arrays;

> CHANGE  1 : 3  @  1 : 2

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.HString;

> INSERT  1 : 27  @  1

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
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.BetterFrustum;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DebugFramebufferView;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DynamicLightManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredConfig;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.NameTagRenderer;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.gui.GuiShaderConfig;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture.EmissiveItems;
+ import net.lax1dude.eaglercraft.v1_8.vector.Vector4f;
+ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;

> CHANGE  10 : 13  @  10 : 20

~ import net.minecraft.client.particle.EntityFX;
~ import net.minecraft.client.renderer.RenderGlobal.ChunkCullAdapter;
~ import net.minecraft.client.renderer.chunk.RenderChunk;

> INSERT  1 : 2  @  1

+ import net.minecraft.client.renderer.entity.Render;

> INSERT  2 : 3  @  2

+ import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

> DELETE  3  @  3 : 5

> INSERT  6 : 7  @  6

+ import net.minecraft.entity.item.EntityItem;

> DELETE  1  @  1 : 4

> INSERT  18 : 19  @  18

+ import net.minecraft.util.Vec3i;

> DELETE  2  @  2 : 9

> CHANGE  9 : 10  @  9 : 10

~ 	private EaglercraftRandom random = new EaglercraftRandom();

> DELETE  43  @  43 : 59

> INSERT  3 : 5  @  3

+ 	private GameOverlayFramebuffer overlayFramebuffer;
+ 	private float eagPartialTicks = 0.0f;

> DELETE  2  @  2 : 3

> CHANGE  9 : 10  @  9 : 10

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

> DELETE  7  @  7 : 22

> DELETE  3  @  3 : 18

> CHANGE  3 : 4  @  3 : 18

~ 		this.useShader = false;

> DELETE  3  @  3 : 14

> DELETE  3  @  3 : 7

> CHANGE  23 : 26  @  23 : 24

~ 		float f3 = this.mc.theWorld.getLightBrightness(
~ 				DeferredStateManager.isDeferredRenderer() ? new BlockPos(this.mc.getRenderViewEntity()).up()
~ 						: new BlockPos(this.mc.getRenderViewEntity()));

> DELETE  20  @  20 : 24

> DELETE  1  @  1 : 8

> CHANGE  111 : 112  @  111 : 112

~ 	public float getFOVModifier(float partialTicks, boolean parFlag) {

> CHANGE  6 : 7  @  6 : 7

~ 				f = this.mc.gameSettings.keyBindZoomCamera.isKeyDown() ? 17.0f : this.mc.gameSettings.fovSetting;

> CHANGE  169 : 173  @  169 : 172

~ 		float farPlane = this.farPlaneDistance * 2.0f * MathHelper.SQRT_2;
~ 		GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),
~ 				(float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, farPlane);
~ 		DeferredStateManager.setGBufferNearFarPlanes(0.05f, farPlane);

> CHANGE  57 : 58  @  57 : 58

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, false),

> DELETE  43  @  43 : 55

> INSERT  4 : 16  @  4

+ 	public static void disableLightmapStatic() {
+ 		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
+ 		GlStateManager.disableTexture2D();
+ 		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
+ 	}
+ 
+ 	public static void enableLightmapStatic() {
+ 		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
+ 		GlStateManager.enableTexture2D();
+ 		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
+ 	}
+ 

> CHANGE  117 : 118  @  117 : 118

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

> CHANGE  4 : 44  @  4 : 5

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
~ 					GlStateManager.disableLighting();
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

> INSERT  25 : 33  @  25

+ 
+ 		boolean fxaa = !this.mc.gameSettings.shaders
+ 				&& ((this.mc.gameSettings.fxaa == 0 && this.mc.gameSettings.fancyGraphics)
+ 						|| this.mc.gameSettings.fxaa == 1);
+ 		if (fxaa) {
+ 			EffectPipelineFXAA.begin(this.mc.displayWidth, this.mc.displayHeight);
+ 		}
+ 

> CHANGE  4 : 5  @  4 : 5

~ 		if (this.mc.gameSettings.anaglyph && !this.mc.gameSettings.shaders) {

> CHANGE  8 : 24  @  8 : 9

~ 			if (this.mc.gameSettings.shaders) {
~ 				try {
~ 					this.eaglercraftShaders(partialTicks, finishTimeNano);
~ 				} catch (Throwable t) {
~ 					logger.error("Exception caught running deferred render!");
~ 					logger.error(t);
~ 					EaglerDeferredPipeline.instance.resetContextStateAfterException();
~ 					logger.error("Suspending shaders...");
~ 					EaglerDeferredPipeline.isSuspended = true;
~ 
~ 				}
~ 				mc.effectRenderer.acceleratedParticleRenderer = EffectRenderer.vanillaAcceleratedParticleRenderer;
~ 			} else {
~ 				mc.effectRenderer.acceleratedParticleRenderer = EffectRenderer.vanillaAcceleratedParticleRenderer;
~ 				this.renderWorldPass(2, partialTicks, finishTimeNano);
~ 			}

> INSERT  2 : 6  @  2

+ 		if (fxaa) {
+ 			EffectPipelineFXAA.end();
+ 		}
+ 

> DELETE  15  @  15 : 17

> CHANGE  12 : 14  @  12 : 14

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),
~ 					(float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * 4.0F);

> CHANGE  4 : 5  @  4 : 5

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> INSERT  27 : 28  @  27

+ 		GlStateManager.disableBlend();

> DELETE  6  @  6 : 7

> INSERT  1 : 2  @  1

+ 		GlStateManager.shadeModel(7424);

> CHANGE  46 : 47  @  46 : 47

~ 			effectrenderer.renderParticles(entity, partialTicks, 2);

> CHANGE  44 : 45  @  44 : 45

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> CHANGE  9 : 10  @  9 : 10

~ 			GlStateManager.gluPerspective(this.getFOVModifier(partialTicks, true),

> INSERT  8 : 10  @  8

+ 		if (DeferredStateManager.isDeferredRenderer())
+ 			return;

> INSERT  70 : 71  @  70

+ 			boolean df = DeferredStateManager.isInDeferredPass();

> CHANGE  9 : 24  @  9 : 13

~ 			if (!df) {
~ 				GlStateManager.enableBlend();
~ 				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
~ 				GlStateManager.alphaFunc(516, 0.1F);
~ 			} else {
~ 				GlStateManager.enableAlpha();
~ 				DeferredStateManager.setHDRTranslucentPassBlendFunc();
~ 				DeferredStateManager.reportForwardRenderObjectPosition2(0.0f, 0.0f, 0.0f);
~ 				GlStateManager.alphaFunc(516, 0.01F);
~ 				GlStateManager.depthMask(false);
~ 				EaglerDeferredPipeline.instance.setForwardRenderLightFactors(0.65f,
~ 						4.75f - MathHelper.clamp_float(DeferredStateManager.getSunHeight() * 8.0f - 3.0f, 0.0f, 4.0f),
~ 						1.0f, 0.03f);
~ 			}
~ 			EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);

> CHANGE  5 : 8  @  5 : 6

~ 			if (df) {
~ 				b0 = 8;
~ 			} else if (this.mc.gameSettings.fancyGraphics) {

> CHANGE  38 : 39  @  38 : 39

~ 							if (f2 >= 0.15F) {

> CHANGE  6 : 15  @  6 : 7

~ 									this.mc.getTextureManager()
~ 											.bindTexture(df ? new ResourceLocation("eagler:glsl/deferred/rain.png")
~ 													: locationRainPng);
~ 									if (df) {
~ 										DeferredStateManager.setRoughnessConstant(0.5f);
~ 										DeferredStateManager.setMetalnessConstant(0.05f);
~ 										DeferredStateManager.setEmissionConstant(1.0f);
~ 										GlStateManager.color(0.8F, 0.8F, 1.0F, 0.25F);
~ 									}

> INSERT  34 : 40  @  34

+ 									if (df) {
+ 										DeferredStateManager.setRoughnessConstant(0.7f);
+ 										DeferredStateManager.setMetalnessConstant(0.05f);
+ 										DeferredStateManager.setEmissionConstant(1.0f);
+ 										GlStateManager.color(1.3F, 1.3F, 1.3F, 0.5F);
+ 									}

> CHANGE  41 : 50  @  41 : 42

~ 			if (!df) {
~ 				GlStateManager.disableBlend();
~ 			} else {
~ 				GlStateManager.disableAlpha();
~ 				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
~ 				DeferredStateManager.setDefaultMaterialConstants();
~ 				GlStateManager.depthMask(true);
~ 				EaglerDeferredPipeline.instance.setForwardRenderLightFactors(1.0f, 1.0f, 1.0f, 1.0f);
~ 			}

> CHANGE  153 : 154  @  153 : 154

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

> INSERT  12 : 988  @  12

+ 
+ 	private static final Vector4f tmpVec4f_1 = new Vector4f();
+ 	private static final Matrix4f tmpMat4f_1 = new Matrix4f();
+ 	private int shadowFrameIndex = 0;
+ 
+ 	private double blockWaveOffsetX = 0.0;
+ 	private double blockWaveOffsetY = 0.0;
+ 	private double blockWaveOffsetZ = 0.0;
+ 
+ 	private void eaglercraftShaders(float partialTicks, long finishTimeNano) {
+ 		if ((EaglerDeferredPipeline.isSuspended || EaglerDeferredPipeline.instance == null)
+ 				|| (mc.currentScreen != null && mc.currentScreen instanceof GuiShaderConfig)) {
+ 			EaglerDeferredPipeline.renderSuspended();
+ 			return;
+ 		}
+ 		mc.mcProfiler.endStartSection("eaglercraftShaders");
+ 		EaglerDeferredPipeline.instance.setPartialTicks(partialTicks);
+ 		eagPartialTicks = partialTicks;
+ 		EaglerDeferredConfig conf = mc.gameSettings.deferredShaderConf;
+ 		boolean flag = isDrawBlockOutline();
+ 		GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
+ 		mc.mcProfiler.startSection("camera");
+ 		setupCameraTransform(partialTicks, 2);
+ 		EaglerDeferredPipeline.instance.loadViewMatrix();
+ 		ActiveRenderInfo.updateRenderInfo(mc.thePlayer, mc.gameSettings.thirdPersonView == 2);
+ 		mc.mcProfiler.endStartSection("culling");
+ 		Frustum frustum = new Frustum();
+ 		Entity entity = mc.getRenderViewEntity();
+ 		if (entity == null) {
+ 			entity = mc.thePlayer;
+ 		}
+ 		double d0 = EaglerDeferredPipeline.instance.currentRenderX = entity.lastTickPosX
+ 				+ (entity.posX - entity.lastTickPosX) * (double) partialTicks;
+ 		double d1 = EaglerDeferredPipeline.instance.currentRenderY = entity.lastTickPosY
+ 				+ (entity.posY - entity.lastTickPosY) * (double) partialTicks;
+ 		double d2 = EaglerDeferredPipeline.instance.currentRenderZ = entity.lastTickPosZ
+ 				+ (entity.posZ - entity.lastTickPosZ) * (double) partialTicks;
+ 		EaglerDeferredPipeline.instance.updateReprojectionCoordinates(d0, d1, d2);
+ 		float eyeHeight = entity.getEyeHeight();
+ 		frustum.setPosition(d0, d1, d2);
+ 
+ //		StringBuilder builder = new StringBuilder();
+ //		long ll = Double.doubleToLongBits(d0);
+ //		for(int i = 63; i >= 0; --i) {
+ //			builder.append((ll >>> i) & 1);
+ //		}
+ //		System.out.println(builder.toString());
+ 
+ 		float waveTimer = (float) ((System.currentTimeMillis() % 600000l) * 0.001);
+ 		DeferredStateManager.setWaterWindOffset(0.0f, 0.0f, waveTimer, waveTimer);
+ 
+ 		float blockWaveDistX = (float) (d0 - blockWaveOffsetX);
+ 		float blockWaveDistY = (float) (d1 - blockWaveOffsetY);
+ 		float blockWaveDistZ = (float) (d2 - blockWaveOffsetZ);
+ 		if (blockWaveDistX * blockWaveDistX + blockWaveDistY * blockWaveDistY + blockWaveDistZ * blockWaveDistZ > 128.0f
+ 				* 128.0f) {
+ 			blockWaveOffsetX = MathHelper.floor_double(d0);
+ 			blockWaveOffsetY = MathHelper.floor_double(d1);
+ 			blockWaveOffsetZ = MathHelper.floor_double(d2);
+ 			blockWaveDistX = (float) (d0 - blockWaveOffsetX);
+ 			blockWaveDistY = (float) (d1 - blockWaveOffsetY);
+ 			blockWaveDistZ = (float) (d2 - blockWaveOffsetZ);
+ 		}
+ 
+ 		boolean wavingBlocks = conf.is_rendering_wavingBlocks;
+ 
+ 		DeferredStateManager.setWavingBlockOffset(blockWaveDistX, blockWaveDistY, blockWaveDistZ);
+ 		if (wavingBlocks) {
+ 			DeferredStateManager.setWavingBlockParams(1.0f * waveTimer,
+ 					200.0f * waveTimer
+ 							+ MathHelper.sin(waveTimer * 0.125f + MathHelper.sin(waveTimer * 1.5f) * 0.2f) * 125.0f,
+ 					0.0f, 0.0f);
+ 		}
+ 
+ 		// if (mc.gameSettings.renderDistanceChunks >= 4) vanilla shows sky not fog
+ 
+ 		mc.mcProfiler.endStartSection("terrain_setup");
+ 		mc.renderGlobal.setupTerrain(entity, (double) partialTicks, frustum, frameCount++, mc.thePlayer.isSpectator());
+ 
+ 		// clear some state:
+ 
+ 		GlStateManager.enableCull();
+ 		GlStateManager.matrixMode(5888);
+ 		GlStateManager.pushMatrix();
+ 		GlStateManager.disableAlpha();
+ 		GlStateManager.disableBlend();
+ 
+ 		// vanilla solid chunks pass:
+ 
+ 		EaglerDeferredPipeline.instance.beginDrawDeferred();
+ 		EaglerDeferredPipeline.instance.beginDrawMainGBuffer();
+ 
+ 		EaglerDeferredPipeline.instance.beginDrawMainGBufferTerrain();
+ 
+ 		mc.mcProfiler.endStartSection("updatechunks");
+ 		mc.renderGlobal.updateChunks(finishTimeNano);
+ 
+ 		mc.mcProfiler.endStartSection("terrain");
+ 
+ 		mc.renderGlobal.renderBlockLayer(EnumWorldBlockLayer.SOLID, (double) partialTicks, 2, entity);
+ 		GlStateManager.enableAlpha();
+ 		GlStateManager.alphaFunc(516, 0.5F);
+ 		if (wavingBlocks)
+ 			DeferredStateManager.enableDrawWavingBlocks();
+ 		mc.renderGlobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, (double) partialTicks, 2, entity);
+ 		mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
+ 		mc.renderGlobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, (double) partialTicks, 2, entity);
+ 		mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
+ 		GlStateManager.alphaFunc(516, 0.1F);
+ 		GlStateManager.matrixMode(5888);
+ 		GlStateManager.popMatrix();
+ 		if (wavingBlocks)
+ 			DeferredStateManager.disableDrawWavingBlocks();
+ 
+ 		// vanilla solid entities:
+ 
+ 		EaglerDeferredPipeline.instance.beginDrawMainGBufferEntities();
+ 		if (conf.is_rendering_dynamicLights) {
+ 			DynamicLightManager.setIsRenderingLights(true);
+ 		}
+ 
+ 		DeferredStateManager.forwardCallbackHandler = DeferredStateManager.forwardCallbackGBuffer;
+ 		DeferredStateManager.forwardCallbackHandler.reset();
+ 
+ 		NameTagRenderer.doRenderNameTags = true;
+ 		NameTagRenderer.nameTagsCount = 0;
+ 		GlStateManager.pushMatrix();
+ 		mc.mcProfiler.endStartSection("entities");
+ 		DeferredStateManager.setDefaultMaterialConstants();
+ 		DeferredStateManager.startUsingEnvMap();
+ 		mc.renderGlobal.renderEntities(entity, frustum, partialTicks);
+ 		GlStateManager.matrixMode(5888);
+ 		GlStateManager.popMatrix();
+ 		mc.mcProfiler.endStartSection("litParticles");
+ 		EntityFX.interpPosX = d0;
+ 		EntityFX.interpPosY = d1;
+ 		EntityFX.interpPosZ = d2;
+ 		enableLightmap();
+ 		GlStateManager.pushMatrix();
+ 		mc.effectRenderer.renderLitParticles(entity, partialTicks);
+ 		mc.mcProfiler.endStartSection("gbufferParticles");
+ 		GlStateManager.matrixMode(5888);
+ 		GlStateManager.popMatrix();
+ 		GlStateManager.pushMatrix();
+ 		mc.effectRenderer.acceleratedParticleRenderer = EaglerDeferredPipeline.instance.gbufferEffectRenderer;
+ 		mc.effectRenderer.renderParticles(entity, partialTicks, 1);
+ 		mc.effectRenderer.acceleratedParticleRenderer = EffectRenderer.vanillaAcceleratedParticleRenderer;
+ 		GlStateManager.matrixMode(5888);
+ 		GlStateManager.popMatrix();
+ 		DeferredStateManager.endUsingEnvMap();
+ 		disableLightmap();
+ 		DynamicLightManager.setIsRenderingLights(false);
+ 		NameTagRenderer.doRenderNameTags = false;
+ 
+ 		mc.mcProfiler.endStartSection("endDrawMainGBuffer");
+ 		EaglerDeferredPipeline.instance.endDrawMainGBuffer();
+ 		mc.mcProfiler.endStartSection("shadowSetup");
+ 
+ 		// calculate sun matrix and angle:
+ 
+ 		GlStateManager.matrixMode(5888);
+ 		GlStateManager.pushMatrix();
+ 		GlStateManager.loadIdentity();
+ 		GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
+ 		float celestialAngle = mc.theWorld.getCelestialAngle(partialTicks) * 360.0F;
+ 		GlStateManager.rotate(DeferredStateManager.sunAngle, 0.0F, 1.0F, 0.0F);
+ 
+ 		if (mc.theWorld.provider.getDimensionId() == 0) {
+ 			GlStateManager.pushMatrix();
+ 			GlStateManager.rotate(celestialAngle + 90.0f, 1.0F, 0.0F, 0.0F);
+ 			tmpVec4f_1.set(0.0f, 0.0f, 1.0f);
+ 			GlStateManager.transform(tmpVec4f_1, tmpVec4f_1);
+ 			tmpVec4f_1.normalise();
+ 			DeferredStateManager.setCurrentSunAngle(tmpVec4f_1);
+ 			if (tmpVec4f_1.y > 0.1f) {
+ 				celestialAngle += 180.0f;
+ 			}
+ 			GlStateManager.popMatrix();
+ 		} else {
+ 			tmpVec4f_1.set(0.0f, 1.0f, 0.0f);
+ 			DeferredStateManager.setCurrentSunAngle(tmpVec4f_1);
+ 			celestialAngle = 270.0f;
+ 		}
+ 
+ 		if (conf.is_rendering_shadowsSun_clamped > 0) {
+ 			if (conf.is_rendering_shadowsColored) {
+ 				DeferredStateManager.forwardCallbackHandler = DeferredStateManager.forwardCallbackSun;
+ 				DeferredStateManager.forwardCallbackHandler.reset();
+ 			} else {
+ 				DeferredStateManager.forwardCallbackHandler = null;
+ 			}
+ 			EaglerDeferredPipeline.instance.beginDrawMainShadowMap();
+ 			++shadowFrameIndex;
+ 			EaglerDeferredPipeline.instance.beginDrawMainShadowMapLOD(0);
+ 			GlStateManager.enableCull();
+ 			GlStateManager.matrixMode(5889);
+ 			GlStateManager.pushMatrix();
+ 			GlStateManager.loadIdentity();
+ 			int shadowMapDist = 16;
+ 			GlStateManager.ortho(-shadowMapDist, shadowMapDist, -shadowMapDist, shadowMapDist, -64.0f, 64.0f);
+ 
+ 			setupSunCameraTransform(celestialAngle);
+ 
+ 			DeferredStateManager.loadShadowPassViewMatrix();
+ 			DeferredStateManager.loadSunShadowMatrixLOD0();
+ 
+ 			GlStateManager.disableAlpha();
+ 			GlStateManager.disableBlend();
+ 
+ 			GlStateManager.matrixMode(5888);
+ 			GlStateManager.loadIdentity();
+ 
+ 			final AxisAlignedBB aabb = matrixToBounds(DeferredStateManager.getSunShadowMatrixLOD0(), d0, d1 + eyeHeight,
+ 					d2);
+ 			DeferredStateManager.setShadowMapBounds(aabb);
+ 
+ 			final BetterFrustum shadowLOD0Frustrum = new BetterFrustum(DeferredStateManager.getSunShadowMatrixLOD0());
+ 
+ 			ChunkCullAdapter shadowCullAdapter = (renderChunk) -> {
+ 				if (renderChunk.shadowLOD0FrameIndex != shadowFrameIndex) {
+ 					renderChunk.shadowLOD0FrameIndex = shadowFrameIndex;
+ 					AxisAlignedBB aabb2 = renderChunk.boundingBox;
+ 					if (aabb.intersectsWith(aabb2)) {
+ 						int shadowVisRet = shadowLOD0Frustrum.intersectAab((float) (aabb2.minX - d0),
+ 								(float) (aabb2.minY - d1 - eyeHeight), (float) (aabb2.minZ - d2),
+ 								(float) (aabb2.maxX - d0), (float) (aabb2.maxY - d1 - eyeHeight),
+ 								(float) (aabb2.maxZ - d2));
+ 						renderChunk.shadowLOD0InFrustum = shadowVisRet == BetterFrustum.INSIDE
+ 								? RenderChunk.ShadowFrustumState.INSIDE
+ 								: (shadowVisRet == BetterFrustum.INTERSECT ? RenderChunk.ShadowFrustumState.INTERSECT
+ 										: RenderChunk.ShadowFrustumState.OUTSIDE);
+ 					} else {
+ 						renderChunk.shadowLOD0InFrustum = RenderChunk.ShadowFrustumState.OUTSIDE_BB;
+ 						return true;
+ 					}
+ 				}
+ 				return renderChunk.shadowLOD0InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE;
+ 			};
+ 
+ 			mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
+ 			mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
+ 			mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.SOLID, aabb, shadowCullAdapter);
+ 			GlStateManager.enableAlpha();
+ 			GlStateManager.alphaFunc(516, 0.5F);
+ 			if (wavingBlocks) {
+ 				DeferredStateManager.enableDrawWavingBlocks();
+ 				enableLightmap();
+ 			}
+ 			mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.CUTOUT_MIPPED, aabb, shadowCullAdapter);
+ 			mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.CUTOUT, aabb, shadowCullAdapter);
+ 			mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
+ 			GlStateManager.alphaFunc(516, 0.1F);
+ 			if (wavingBlocks) {
+ 				DeferredStateManager.disableDrawWavingBlocks();
+ 				disableLightmap();
+ 			}
+ 
+ 			mc.renderGlobal.renderShadowLODEntities(entity, partialTicks, (renderChunk) -> {
+ 				return renderChunk.shadowLOD0FrameIndex == shadowFrameIndex
+ 						&& (renderChunk.shadowLOD0InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE
+ 								|| renderChunk.shadowLOD0InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE_BB);
+ 			}, (renderChunk, renderManager, renderEntity) -> {
+ 				boolean b;
+ 				if (renderEntity.ignoreFrustumCheck) {
+ 					return false;
+ 				} else if (!renderEntity.isInRangeToRender3d(d0, d1, d2)) {
+ 					return true;
+ 				} else if (renderChunk.shadowLOD0FrameIndex == shadowFrameIndex
+ 						&& ((b = renderChunk.shadowLOD0InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE)
+ 								|| renderChunk.shadowLOD0InFrustum == RenderChunk.ShadowFrustumState.INSIDE)) {
+ 					return b;
+ 				} else {
+ 					AxisAlignedBB aabbEntity = renderEntity.getEntityBoundingBox();
+ 					if (aabbEntity.func_181656_b() || aabbEntity.getAverageEdgeLength() == 0.0) {
+ 						aabbEntity = new AxisAlignedBB(d0 - 2.0, d1 - 2.0, d2 - 2.0, d0 + 2.0, d1 + 2.0, d2 + 2.0);
+ 					}
+ 					if (shadowLOD0Frustrum.testAab((float) (aabbEntity.minX - d0),
+ 							(float) (aabbEntity.minY - d1 - eyeHeight), (float) (aabbEntity.minZ - d2),
+ 							(float) (aabbEntity.maxX - d0), (float) (aabbEntity.maxY - d1 - eyeHeight),
+ 							(float) (aabbEntity.maxZ - d2))) {
+ 						return !renderManager.shouldRender(renderEntity, frustum, d0, d1, d2);
+ 					} else {
+ 						return true;
+ 					}
+ 				}
+ 			});
+ 			disableLightmap();
+ 
+ 			if (conf.is_rendering_shadowsColored) {
+ 				EaglerDeferredPipeline.instance.beginDrawColoredShadows();
+ 				List<ShadersRenderPassFuture> lst = DeferredStateManager.forwardCallbackHandler.renderPassList;
+ 				for (int i = 0, l = lst.size(); i < l; ++i) {
+ 					lst.get(i).draw(ShadersRenderPassFuture.PassType.SHADOW);
+ 				}
+ 				DeferredStateManager.forwardCallbackHandler.reset();
+ 				mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
+ 				mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
+ 				mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.TRANSLUCENT, aabb, shadowCullAdapter);
+ 				mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
+ 				if (conf.is_rendering_realisticWater) {
+ 					GlStateManager.disableTexture2D();
+ 					GlStateManager.color(0.173f, 0.239f, 0.957f, 0.25f);
+ 					mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.REALISTIC_WATER, aabb,
+ 							shadowCullAdapter);
+ 					GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 					GlStateManager.enableTexture2D();
+ 				}
+ 				EaglerDeferredPipeline.instance.endDrawColoredShadows();
+ 			}
+ 			disableLightmap();
+ 
+ 			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 			GlStateManager.disableAlpha();
+ 			GlStateManager.matrixMode(5889);
+ 			GlStateManager.popMatrix();
+ 
+ 			if (conf.is_rendering_shadowsSun_clamped > 1) {
+ 				EaglerDeferredPipeline.instance.beginDrawMainShadowMapLOD(1);
+ 				if (conf.is_rendering_shadowsColored) {
+ 					DeferredStateManager.forwardCallbackHandler.reset();
+ 				}
+ 
+ 				GlStateManager.enableCull();
+ 				GlStateManager.matrixMode(5889);
+ 				GlStateManager.pushMatrix();
+ 				GlStateManager.loadIdentity();
+ 				shadowMapDist = 32;
+ 				GlStateManager.ortho(-shadowMapDist, shadowMapDist, -shadowMapDist, shadowMapDist, -64.0f, 64.0f);
+ 
+ 				setupSunCameraTransform(celestialAngle);
+ 
+ 				DeferredStateManager.loadShadowPassViewMatrix();
+ 				DeferredStateManager.loadSunShadowMatrixLOD1();
+ 
+ 				GlStateManager.disableAlpha();
+ 				GlStateManager.disableBlend();
+ 
+ 				GlStateManager.matrixMode(5888);
+ 				GlStateManager.loadIdentity();
+ 
+ 				final AxisAlignedBB aabb2 = matrixToBounds(DeferredStateManager.getSunShadowMatrixLOD1(), d0,
+ 						d1 + eyeHeight, d2);
+ 				DeferredStateManager.setShadowMapBounds(aabb2);
+ 
+ 				BetterFrustum shadowLOD1Frustrum = new BetterFrustum(DeferredStateManager.getSunShadowMatrixLOD1());
+ 
+ 				ChunkCullAdapter shadowCullAdapter2 = (renderChunk) -> {
+ 					if (renderChunk.shadowLOD1FrameIndex != shadowFrameIndex) {
+ 						renderChunk.shadowLOD1FrameIndex = shadowFrameIndex;
+ 						if (renderChunk.shadowLOD0FrameIndex == shadowFrameIndex
+ 								&& renderChunk.shadowLOD0InFrustum == RenderChunk.ShadowFrustumState.INSIDE) {
+ 							renderChunk.shadowLOD1InFrustum = RenderChunk.ShadowFrustumState.OUTSIDE;
+ 							return true;
+ 						} else {
+ 							AxisAlignedBB aabb3 = renderChunk.boundingBox;
+ 							if (aabb2.intersectsWith(aabb3)) {
+ 								int shadowVisRet = shadowLOD1Frustrum.intersectAab((float) (aabb3.minX - d0),
+ 										(float) (aabb3.minY - d1 - eyeHeight), (float) (aabb3.minZ - d2),
+ 										(float) (aabb3.maxX - d0), (float) (aabb3.maxY - d1 - eyeHeight),
+ 										(float) (aabb3.maxZ - d2));
+ 								renderChunk.shadowLOD1InFrustum = shadowVisRet == BetterFrustum.INSIDE
+ 										? RenderChunk.ShadowFrustumState.INSIDE
+ 										: (shadowVisRet == BetterFrustum.INTERSECT
+ 												? RenderChunk.ShadowFrustumState.INTERSECT
+ 												: RenderChunk.ShadowFrustumState.OUTSIDE);
+ 							} else {
+ 								renderChunk.shadowLOD1InFrustum = RenderChunk.ShadowFrustumState.OUTSIDE_BB;
+ 								return true;
+ 							}
+ 						}
+ 					}
+ 					return renderChunk.shadowLOD1InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE;
+ 				};
+ 
+ 				mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
+ 				mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
+ 				mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.SOLID, aabb2, shadowCullAdapter2);
+ 				GlStateManager.enableAlpha();
+ 				GlStateManager.alphaFunc(516, 0.5F);
+ 				if (wavingBlocks) {
+ 					DeferredStateManager.enableDrawWavingBlocks();
+ 					enableLightmap();
+ 				}
+ 				mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.CUTOUT_MIPPED, aabb2, shadowCullAdapter2);
+ 				mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.CUTOUT, aabb2, shadowCullAdapter2);
+ 				mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
+ 				GlStateManager.alphaFunc(516, 0.1F);
+ 				if (wavingBlocks) {
+ 					DeferredStateManager.disableDrawWavingBlocks();
+ 					disableLightmap();
+ 				}
+ 
+ 				mc.renderGlobal.renderShadowLODEntities(entity, partialTicks, (renderChunk) -> {
+ 					return renderChunk.shadowLOD1FrameIndex == shadowFrameIndex
+ 							&& (renderChunk.shadowLOD1InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE
+ 									|| renderChunk.shadowLOD1InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE_BB);
+ 				}, (renderChunk, renderManager, renderEntity) -> {
+ 					boolean b;
+ 					if (renderEntity.ignoreFrustumCheck) {
+ 						return false;
+ 					} else if (!renderEntity.isInRangeToRender3d(d0, d1, d2)) {
+ 						return true;
+ 					} else if (renderChunk.shadowLOD1FrameIndex == shadowFrameIndex
+ 							&& (b = renderChunk.shadowLOD1InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE)) {
+ 						return b;
+ 					} else {
+ 						AxisAlignedBB aabbEntity = renderEntity.getEntityBoundingBox();
+ 						if (aabbEntity.func_181656_b() || aabbEntity.getAverageEdgeLength() == 0.0) {
+ 							aabbEntity = new AxisAlignedBB(d0 - 2.0, d1 - 2.0, d2 - 2.0, d0 + 2.0, d1 + 2.0, d2 + 2.0);
+ 						}
+ 						if (shadowLOD1Frustrum.testAab((float) (aabbEntity.minX - d0),
+ 								(float) (aabbEntity.minY - d1 - eyeHeight), (float) (aabbEntity.minZ - d2),
+ 								(float) (aabbEntity.maxX - d0), (float) (aabbEntity.maxY - d1 - eyeHeight),
+ 								(float) (aabbEntity.maxZ - d2))) {
+ 							return !renderManager.shouldRender(renderEntity, frustum, d0, d1, d2);
+ 						} else {
+ 							return true;
+ 						}
+ 					}
+ 				});
+ 				disableLightmap();
+ 
+ 				if (conf.is_rendering_shadowsColored) {
+ 					EaglerDeferredPipeline.instance.beginDrawColoredShadows();
+ 					List<ShadersRenderPassFuture> lst = DeferredStateManager.forwardCallbackHandler.renderPassList;
+ 					for (int i = 0, l = lst.size(); i < l; ++i) {
+ 						lst.get(i).draw(ShadersRenderPassFuture.PassType.SHADOW);
+ 					}
+ 					DeferredStateManager.forwardCallbackHandler.reset();
+ 					mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
+ 					mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
+ 					mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.TRANSLUCENT, aabb2, shadowCullAdapter2);
+ 					mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
+ 					if (conf.is_rendering_realisticWater) {
+ 						GlStateManager.disableTexture2D();
+ 						GlStateManager.color(0.173f, 0.239f, 0.957f, 0.25f);
+ 						mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.REALISTIC_WATER, aabb2,
+ 								shadowCullAdapter2);
+ 						GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 						GlStateManager.enableTexture2D();
+ 					}
+ 					EaglerDeferredPipeline.instance.endDrawColoredShadows();
+ 				}
+ 				disableLightmap();
+ 
+ 				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 				GlStateManager.disableAlpha();
+ 				GlStateManager.matrixMode(5889);
+ 				GlStateManager.popMatrix();
+ 
+ 				if (conf.is_rendering_shadowsSun_clamped > 2) {
+ 					EaglerDeferredPipeline.instance.beginDrawMainShadowMapLOD(2);
+ 
+ 					GlStateManager.enableCull();
+ 					GlStateManager.matrixMode(5889);
+ 					GlStateManager.pushMatrix();
+ 					GlStateManager.loadIdentity();
+ 					shadowMapDist = 1 << (conf.is_rendering_shadowsSun_clamped + 3);
+ 					GlStateManager.ortho(-shadowMapDist, shadowMapDist, -shadowMapDist, shadowMapDist, -64.0f, 64.0f);
+ 
+ 					setupSunCameraTransform(celestialAngle);
+ 
+ 					DeferredStateManager.loadShadowPassViewMatrix();
+ 					DeferredStateManager.loadSunShadowMatrixLOD2();
+ 
+ 					GlStateManager.disableAlpha();
+ 					GlStateManager.disableBlend();
+ 
+ 					GlStateManager.matrixMode(5888);
+ 					GlStateManager.loadIdentity();
+ 
+ 					DeferredStateManager.loadPassViewMatrix();
+ 					DeferredStateManager.loadPassProjectionMatrix();
+ 
+ 					AxisAlignedBB aabb3 = matrixToBounds(DeferredStateManager.getSunShadowMatrixLOD2(), d0,
+ 							d1 + eyeHeight, d2);
+ 					DeferredStateManager.setShadowMapBounds(aabb3);
+ 
+ 					BetterFrustum shadowLOD2Frustum = new BetterFrustum(DeferredStateManager.getSunShadowMatrixLOD2());
+ 
+ 					ChunkCullAdapter shadowCullAdapter3 = (renderChunk) -> {
+ 						if (renderChunk.shadowLOD2FrameIndex != shadowFrameIndex) {
+ 							renderChunk.shadowLOD2FrameIndex = shadowFrameIndex;
+ 							if (renderChunk.shadowLOD0FrameIndex == shadowFrameIndex
+ 									&& renderChunk.shadowLOD0InFrustum == RenderChunk.ShadowFrustumState.INSIDE) {
+ 								renderChunk.shadowLOD2InFrustum = RenderChunk.ShadowFrustumState.OUTSIDE;
+ 								return true;
+ 							} else if (renderChunk.shadowLOD1FrameIndex == shadowFrameIndex
+ 									&& renderChunk.shadowLOD1InFrustum == RenderChunk.ShadowFrustumState.INSIDE) {
+ 								renderChunk.shadowLOD2InFrustum = RenderChunk.ShadowFrustumState.OUTSIDE;
+ 								return true;
+ 							} else {
+ 								AxisAlignedBB aabb4 = renderChunk.boundingBox;
+ 								if (aabb3.intersectsWith(aabb4)) {
+ 									int shadowVisRet = shadowLOD2Frustum.intersectAab((float) (aabb4.minX - d0),
+ 											(float) (aabb4.minY - d1 - eyeHeight), (float) (aabb4.minZ - d2),
+ 											(float) (aabb4.maxX - d0), (float) (aabb4.maxY - d1 - eyeHeight),
+ 											(float) (aabb4.maxZ - d2));
+ 									renderChunk.shadowLOD2InFrustum = shadowVisRet == BetterFrustum.INSIDE
+ 											? RenderChunk.ShadowFrustumState.INSIDE
+ 											: (shadowVisRet == BetterFrustum.INTERSECT
+ 													? RenderChunk.ShadowFrustumState.INTERSECT
+ 													: RenderChunk.ShadowFrustumState.OUTSIDE);
+ 								} else {
+ 									renderChunk.shadowLOD2InFrustum = RenderChunk.ShadowFrustumState.OUTSIDE_BB;
+ 									return true;
+ 								}
+ 							}
+ 						}
+ 						return renderChunk.shadowLOD2InFrustum == RenderChunk.ShadowFrustumState.OUTSIDE;
+ 					};
+ 
+ 					mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
+ 					mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
+ 					mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.SOLID, aabb3, shadowCullAdapter3);
+ 					GlStateManager.enableAlpha();
+ 					mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.CUTOUT_MIPPED, aabb3,
+ 							shadowCullAdapter3);
+ 					mc.renderGlobal.renderBlockLayerShadow(EnumWorldBlockLayer.CUTOUT, aabb3, shadowCullAdapter3);
+ 					mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
+ 					disableLightmap();
+ 
+ 					GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 					GlStateManager.disableAlpha();
+ 					GlStateManager.matrixMode(5889);
+ 					GlStateManager.popMatrix();
+ 				}
+ 			}
+ 
+ 			EaglerDeferredPipeline.instance.endDrawMainShadowMap();
+ 			if (conf.is_rendering_shadowsColored) {
+ 				DeferredStateManager.forwardCallbackHandler.reset();
+ 			}
+ 		}
+ 
+ 		GlStateManager.matrixMode(5888);
+ 		GlStateManager.popMatrix();
+ 
+ 		if (conf.is_rendering_dynamicLights && entity != null && mc.gameSettings.thirdPersonView == 0) {
+ 			if (entity instanceof EntityLivingBase) {
+ 				DynamicLightManager.setIsRenderingLights(true);
+ 				ItemStack itemStack = ((EntityLivingBase) entity).getHeldItem();
+ 				if (itemStack != null) {
+ 					float[] emission = EmissiveItems.getItemEmission(itemStack);
+ 					if (emission != null) {
+ 						float yaw = entity.prevRotationYaw
+ 								+ (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
+ 						yaw *= 0.017453293f;
+ 						float s = 0.2f;
+ 						double d02 = d0 + MathHelper.sin(yaw) * s;
+ 						double d22 = d2 + MathHelper.cos(yaw) * s;
+ 						float mag = 0.7f;
+ 						DynamicLightManager.renderDynamicLight("render_view_entity_holding", d02,
+ 								d1 + entity.getEyeHeight(), d22, emission[0] * mag, emission[1] * mag,
+ 								emission[2] * mag, false);
+ 					}
+ 				}
+ 				DynamicLightManager.setIsRenderingLights(false);
+ 			}
+ 		}
+ 
+ 		mc.mcProfiler.endStartSection("combineGBuffersAndIlluminate");
+ 		EaglerDeferredPipeline.instance.combineGBuffersAndIlluminate();
+ 
+ 		if (conf.is_rendering_useEnvMap) {
+ 			mc.mcProfiler.endStartSection("envMap");
+ 			DeferredStateManager.forwardCallbackHandler = null;
+ 			EaglerDeferredPipeline.instance.beginDrawEnvMap();
+ 			GlStateManager.enableCull();
+ 
+ 			EaglerDeferredPipeline.instance.beginDrawEnvMapTop(entity.getEyeHeight());
+ 			EaglerDeferredPipeline.instance.beginDrawEnvMapSolid();
+ 			mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
+ 			mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.SOLID, (double) partialTicks, 1, entity);
+ 			GlStateManager.enableAlpha();
+ 			GlStateManager.alphaFunc(516, 0.5F);
+ 			mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.CUTOUT, (double) partialTicks, 1, entity);
+ 			mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, (double) partialTicks, 1,
+ 					entity);
+ 			DeferredStateManager.setDefaultMaterialConstants();
+ 			mc.renderGlobal.renderParaboloidTileEntities(entity, partialTicks, 1);
+ 			GlStateManager.alphaFunc(516, 0.1F);
+ 			EaglerDeferredPipeline.instance.beginDrawEnvMapTranslucent();
+ 			if (conf.is_rendering_realisticWater) {
+ 				GlStateManager.disableTexture2D();
+ 				DeferredStateManager.disableMaterialTexture();
+ 				DeferredStateManager.setRoughnessConstant(0.117f);
+ 				DeferredStateManager.setMetalnessConstant(0.067f);
+ 				DeferredStateManager.setEmissionConstant(0.0f);
+ 				GlStateManager.color(0.173f, 0.239f, 0.957f, 0.65f);
+ 				mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.REALISTIC_WATER, (double) partialTicks,
+ 						1, entity);
+ 				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 				GlStateManager.enableTexture2D();
+ 				DeferredStateManager.enableMaterialTexture();
+ 			}
+ 			mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, (double) partialTicks, 1,
+ 					entity);
+ 			mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
+ 			GlStateManager.disableAlpha();
+ 
+ 			EaglerDeferredPipeline.instance.beginDrawEnvMapBottom(entity.getEyeHeight());
+ 			EaglerDeferredPipeline.instance.beginDrawEnvMapSolid();
+ 			mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
+ 			mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.SOLID, (double) partialTicks, -1, entity);
+ 			GlStateManager.enableAlpha();
+ 			GlStateManager.alphaFunc(516, 0.5F);
+ 			mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.CUTOUT, (double) partialTicks, -1, entity);
+ 			mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, (double) partialTicks, -1,
+ 					entity);
+ 			DeferredStateManager.setDefaultMaterialConstants();
+ 			mc.renderGlobal.renderParaboloidTileEntities(entity, partialTicks, -1);
+ 			GlStateManager.alphaFunc(516, 0.1F);
+ 			EaglerDeferredPipeline.instance.beginDrawEnvMapTranslucent();
+ 			if (conf.is_rendering_realisticWater) {
+ 				GlStateManager.disableTexture2D();
+ 				DeferredStateManager.disableMaterialTexture();
+ 				DeferredStateManager.setRoughnessConstant(0.117f);
+ 				DeferredStateManager.setMetalnessConstant(0.067f);
+ 				DeferredStateManager.setEmissionConstant(0.0f);
+ 				GlStateManager.color(0.173f, 0.239f, 0.957f, 0.65f);
+ 				mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.REALISTIC_WATER, (double) partialTicks,
+ 						-1, entity);
+ 				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 				GlStateManager.enableTexture2D();
+ 				DeferredStateManager.enableMaterialTexture();
+ 			}
+ 			mc.renderGlobal.renderParaboloidBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, (double) partialTicks, -1,
+ 					entity);
+ 			mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
+ 			GlStateManager.disableAlpha();
+ 
+ 			EaglerDeferredPipeline.instance.endDrawEnvMap();
+ 		}
+ 
+ 		if (conf.is_rendering_realisticWater) {
+ 			mc.mcProfiler.endStartSection("realisticWaterMask");
+ 			EaglerDeferredPipeline.instance.beginDrawRealisticWaterMask();
+ 			enableLightmap();
+ 			mc.renderGlobal.renderBlockLayer(EnumWorldBlockLayer.REALISTIC_WATER, (double) partialTicks, 2, entity);
+ 			disableLightmap();
+ 			EaglerDeferredPipeline.instance.endDrawRealisticWaterMask();
+ 		}
+ 
+ 		mc.mcProfiler.endStartSection("setupShaderFog");
+ 
+ 		int dim = mc.theWorld.provider.getDimensionId();
+ 		float ff;
+ 		if (dim == 0) {
+ 			ff = (this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks) * 4.8f - 2.8f;
+ 			if (ff < 0.0f)
+ 				ff = 0.0f;
+ 			if (ff > 1.0f)
+ 				ff = 1.0f;
+ 		} else {
+ 			ff = 1.0f;
+ 		}
+ 
+ 		Block block = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, entity, partialTicks);
+ 		if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isPotionActive(Potion.blindness)) {
+ 			float f1 = 5.0F;
+ 			int i = ((EntityLivingBase) entity).getActivePotionEffect(Potion.blindness).getDuration();
+ 			if (i < 20) {
+ 				f1 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - (float) i / 20.0F);
+ 			}
+ 			if (partialTicks == -1) {
+ 				DeferredStateManager.enableFogLinear(0.0f, f1 * 0.8F, false, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
+ 						1.0f);
+ 			} else {
+ 				DeferredStateManager.enableFogLinear(f1 * 0.25F, f1, false, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
+ 						1.0f);
+ 			}
+ 		} else if (block.getMaterial() == Material.water) {
+ 			updateFogColor(partialTicks); // gen vanilla fog color
+ 			ff *= 0.2f;
+ 			ff += 0.8f;
+ 			fogColorRed *= 0.5f;
+ 			fogColorGreen *= 0.5f;
+ 			fogColorBlue *= 0.5f;
+ 			if (entity instanceof EntityLivingBase
+ 					&& ((EntityLivingBase) entity).isPotionActive(Potion.waterBreathing)) {
+ 				DeferredStateManager.enableFogExp(0.01F, false, fogColorRed, fogColorGreen, fogColorBlue, 1.0f,
+ 						fogColorRed * ff, fogColorGreen * ff, fogColorBlue * ff, 1.0f);
+ 			} else {
+ 				DeferredStateManager.enableFogExp(0.1F - (float) EnchantmentHelper.getRespiration(entity) * 0.03F,
+ 						false, fogColorRed, fogColorGreen, fogColorBlue, 1.0f, fogColorRed * ff, fogColorGreen * ff,
+ 						fogColorBlue * ff, 1.0f);
+ 			}
+ 		} else if (block.getMaterial() == Material.lava) {
+ 			updateFogColor(partialTicks); // gen vanilla fog color
+ 			DeferredStateManager.enableFogExp(2.0F, false, fogColorRed, fogColorGreen, fogColorBlue, 1.0f, fogColorRed,
+ 					fogColorGreen, fogColorBlue, 1.0f);
+ 		} else {
+ 			float ds = 0.0005f;
+ 			if (mc.gameSettings.renderDistanceChunks < 6) {
+ 				ds *= 3.0f - mc.gameSettings.renderDistanceChunks * 0.33f;
+ 			}
+ 			ds *= 1.5f + mc.theWorld.getRainStrength(partialTicks) * 10.0f
+ 					+ mc.theWorld.getThunderStrength(partialTicks) * 5.0f;
+ 			ds *= MathHelper.clamp_float(6.0f - DeferredStateManager.getSunHeight() * 17.0f, 1.0f, 2.0f);
+ 			DeferredStateManager.enableFogExp(ds, true, 1.0f, 1.0f, 1.0f, 1.0f, ff, ff, ff, 1.0f);
+ 		}
+ 
+ 		EaglerDeferredPipeline.instance.beginDrawHDRTranslucent();
+ 		DeferredStateManager.setDefaultMaterialConstants();
+ 
+ 		if (conf.is_rendering_realisticWater) {
+ 			mc.mcProfiler.endStartSection("realisticWaterSurface");
+ 			EaglerDeferredPipeline.instance.beginDrawRealisticWaterSurface();
+ 			mc.renderGlobal.renderBlockLayer(EnumWorldBlockLayer.REALISTIC_WATER, (double) partialTicks, 2, entity);
+ 			EaglerDeferredPipeline.instance.endDrawRealisticWaterSurface();
+ 		}
+ 
+ 		mc.mcProfiler.endStartSection("gbufferFog");
+ 		EaglerDeferredPipeline.instance.applyGBufferFog();
+ 
+ 		mc.mcProfiler.endStartSection("translucentEntities");
+ 		EaglerDeferredPipeline.instance.beginDrawTranslucentEntities();
+ 
+ 		TileEntityRendererDispatcher.staticPlayerX = d0;
+ 		TileEntityRendererDispatcher.staticPlayerY = d1;
+ 		TileEntityRendererDispatcher.staticPlayerZ = d2;
+ 		mc.getRenderManager().setRenderPosition(d0, d1, d2);
+ 
+ 		for (int i = 0; i < mc.theWorld.weatherEffects.size(); ++i) {
+ 			Entity entity1 = (Entity) mc.theWorld.weatherEffects.get(i);
+ 			if (entity1.isInRangeToRender3d(d0, d1, d2)) {
+ 				mc.getRenderManager().renderEntitySimple(entity1, partialTicks);
+ 			}
+ 		}
+ 		disableLightmap();
+ 
+ 		DeferredStateManager.forwardCallbackGBuffer.sort(0.0f, 0.0f, 0.0f);
+ 		List<ShadersRenderPassFuture> lst = DeferredStateManager.forwardCallbackGBuffer.renderPassList;
+ 		for (int i = 0, l = lst.size(); i < l; ++i) {
+ 			lst.get(i).draw(ShadersRenderPassFuture.PassType.MAIN);
+ 		}
+ 		DeferredStateManager.forwardCallbackGBuffer.reset();
+ 
+ 		EaglerDeferredPipeline.instance.beginDrawTranslucentBlocks();
+ 		mc.mcProfiler.endStartSection("translucentBlocks");
+ 		mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
+ 		mc.renderGlobal.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, (double) partialTicks, 2, entity);
+ 
+ 		EaglerDeferredPipeline.instance.beginDrawMainGBufferDestroyProgress();
+ 
+ 		mc.mcProfiler.endStartSection("destroyProgress");
+ 
+ 		GlStateManager.enableBlend();
+ 		GlStateManager.tryBlendFuncSeparate(0, 770, 0, 0);
+ 		GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
+ 		mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
+ 		mc.renderGlobal.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getWorldRenderer(),
+ 				entity, partialTicks);
+ 		mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
+ 
+ 		EaglerDeferredPipeline.instance.endDrawMainGBufferDestroyProgress();
+ 
+ 		if (mc.effectRenderer.hasParticlesInAlphaLayer()) {
+ 			mc.mcProfiler.endStartSection("transparentParticles");
+ 			GlStateManager.pushMatrix();
+ 			mc.effectRenderer.acceleratedParticleRenderer = EaglerDeferredPipeline.instance.forwardEffectRenderer;
+ 			DeferredStateManager.setHDRTranslucentPassBlendFunc();
+ 			DeferredStateManager.reportForwardRenderObjectPosition2(0.0f, 0.0f, 0.0f);
+ 			GlStateManager.enableBlend();
+ 			GlStateManager.depthMask(false);
+ 			mc.effectRenderer.renderParticles(entity, partialTicks, 0);
+ 			mc.effectRenderer.acceleratedParticleRenderer = EffectRenderer.vanillaAcceleratedParticleRenderer;
+ 			GlStateManager.matrixMode(5888);
+ 			GlStateManager.popMatrix();
+ 			GlStateManager.enableBlend();
+ 			GlStateManager.depthMask(true);
+ 		}
+ 
+ 		if (conf.is_rendering_useEnvMap) {
+ 			mc.mcProfiler.endStartSection("glassHighlights");
+ 			EaglerDeferredPipeline.instance.beginDrawGlassHighlights();
+ 			mc.renderGlobal.renderBlockLayer(EnumWorldBlockLayer.GLASS_HIGHLIGHTS, (double) partialTicks, 2, entity);
+ 			EaglerDeferredPipeline.instance.endDrawGlassHighlights();
+ 		}
+ 
+ 		mc.mcProfiler.endStartSection("saveReprojData");
+ 		EaglerDeferredPipeline.instance.saveReprojData();
+ 
+ 		mc.mcProfiler.endStartSection("rainSnow");
+ 		renderRainSnow(partialTicks);
+ 
+ 		GlStateManager.disableBlend();
+ 
+ 		if (renderHand) {
+ 			mc.mcProfiler.endStartSection("renderHandOverlay");
+ 			EaglerDeferredPipeline.instance.beginDrawHandOverlay();
+ 			DeferredStateManager.reportForwardRenderObjectPosition2(0.0f, 0.0f, 0.0f);
+ 			DeferredStateManager.forwardCallbackHandler = DeferredStateManager.forwardCallbackGBuffer;
+ 			GlStateManager.matrixMode(5888);
+ 			GlStateManager.pushMatrix();
+ 			GlStateManager.matrixMode(5889);
+ 			GlStateManager.pushMatrix();
+ 			GlStateManager.enableAlpha();
+ 			renderHand(partialTicks, 2);
+ 			DeferredStateManager.forwardCallbackHandler = null;
+ 			GlStateManager.enableBlend();
+ 			DeferredStateManager.setHDRTranslucentPassBlendFunc();
+ 			lst = DeferredStateManager.forwardCallbackGBuffer.renderPassList;
+ 			for (int i = 0, l = lst.size(); i < l; ++i) {
+ 				lst.get(i).draw(ShadersRenderPassFuture.PassType.MAIN);
+ 			}
+ 			GlStateManager.matrixMode(5889);
+ 			GlStateManager.popMatrix();
+ 			GlStateManager.matrixMode(5888);
+ 			GlStateManager.popMatrix();
+ 			EaglerDeferredPipeline.instance.endDrawHandOverlay();
+ 			GlStateManager.disableBlend();
+ 			GlStateManager.disableAlpha();
+ 		}
+ 
+ 		mc.mcProfiler.endStartSection("endDrawDeferred");
+ 		EaglerDeferredPipeline.instance.endDrawHDRTranslucent();
+ 
+ 		EaglerDeferredPipeline.instance.endDrawDeferred();
+ 
+ 		GlStateManager.setActiveTexture(33985);
+ 		this.mc.getTextureManager().bindTexture(this.locationLightMap);
+ 
+ 		GlStateManager.setActiveTexture(33984);
+ 		GlStateManager.matrixMode(5890);
+ 		GlStateManager.loadIdentity();
+ 		GlStateManager.matrixMode(5888);
+ 
+ 		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 		GlStateManager.enableDepth();
+ 		GlStateManager.depthMask(true);
+ 
+ 		if (!DebugFramebufferView.debugViewShown) {
+ 			GlStateManager.disableAlpha();
+ 			if (isDrawBlockOutline()) {
+ 				this.mc.mcProfiler.endStartSection("outline");
+ 				mc.renderGlobal.drawSelectionBox(mc.thePlayer, this.mc.objectMouseOver, 0, partialTicks);
+ 			}
+ 			GlStateManager.enableAlpha();
+ 			this.mc.mcProfiler.endStartSection("nameTags");
+ 			if (NameTagRenderer.nameTagsCount > 0) {
+ 				enableLightmap();
+ 				Arrays.sort(NameTagRenderer.nameTagsThisFrame, 0, NameTagRenderer.nameTagsCount, (n1, n2) -> {
+ 					return n1.dst2 < n2.dst2 ? 1 : (n1.dst2 > n2.dst2 ? -1 : 0);
+ 				});
+ 				for (int i = 0; i < NameTagRenderer.nameTagsCount; ++i) {
+ 					NameTagRenderer n = NameTagRenderer.nameTagsThisFrame[i];
+ 					int ii = n.entityIn.getBrightnessForRender(partialTicks);
+ 					int j = ii % 65536;
+ 					int k = ii / 65536;
+ 					OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j / 1.0F,
+ 							(float) k / 1.0F);
+ 					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+ 					if (n.maxDistance == -69) {
+ 						// calls renderName instead of renderLivingLabel:
+ 						Render.renderNameAdapter(mc.getRenderManager().getEntityRenderObject(n.entityIn), n.entityIn,
+ 								n.x, n.y, n.z);
+ 					} else {
+ 						mc.getRenderManager().getEntityRenderObject(n.entityIn).renderLivingLabel(n.entityIn, n.str,
+ 								n.x, n.y, n.z, n.maxDistance);
+ 					}
+ 				}
+ 				NameTagRenderer.nameTagsCount = 0;
+ 			}
+ 			disableLightmap();
+ 			GlStateManager.disableLighting();
+ 			this.mc.mcProfiler.endStartSection("worldBorder");
+ 			mc.renderGlobal.renderWorldBorder(entity, partialTicks);
+ 		}
+ 
+ 		mc.mcProfiler.endSection();
+ 	}
+ 
+ 	public boolean renderHeldItemLight(EntityLivingBase entityLiving, float mag) {
+ 		if (DynamicLightManager.isRenderingLights()) {
+ 			ItemStack itemStack = entityLiving.getHeldItem();
+ 			if (itemStack != null) {
+ 				float[] emission = EmissiveItems.getItemEmission(itemStack);
+ 				if (emission != null) {
+ 					double d0 = entityLiving.prevPosX + (entityLiving.posX - entityLiving.prevPosX) * eagPartialTicks;
+ 					double d1 = entityLiving.prevPosY + (entityLiving.posY - entityLiving.prevPosY) * eagPartialTicks;
+ 					double d2 = entityLiving.prevPosZ + (entityLiving.posZ - entityLiving.prevPosZ) * eagPartialTicks;
+ 					float yaw = entityLiving.prevRotationYaw
+ 							+ (entityLiving.rotationYaw - entityLiving.prevRotationYaw) * eagPartialTicks;
+ 					yaw *= 0.017453293f;
+ 					float s = 0.5f;
+ 					d0 -= MathHelper.sin(yaw) * s;
+ 					d2 += MathHelper.cos(yaw) * s;
+ 					mag *= 0.5f;
+ 					DynamicLightManager.renderDynamicLight("entity_" + entityLiving.getEntityId() + "_holding", d0,
+ 							d1 + entityLiving.getEyeHeight() * 0.63f, d2, emission[0] * mag, emission[1] * mag,
+ 							emission[2] * mag, false);
+ 					return true;
+ 				}
+ 			}
+ 		}
+ 		return false;
+ 	}
+ 
+ 	public boolean renderItemEntityLight(Entity entity, float mag) {
+ 		if (DynamicLightManager.isRenderingLights()) {
+ 			ItemStack itemStack = null;
+ 			float offsetX = 0.0f;
+ 			float offsetY = 0.0f;
+ 			float offsetZ = 0.0f;
+ 			if (entity instanceof EntityItem) {
+ 				EntityItem ei = (EntityItem) entity;
+ 				itemStack = ei.getEntityItem();
+ 				offsetY = MathHelper.sin(((float) ei.getAge() + eagPartialTicks) / 10.0F + ei.hoverStart) * 0.1F + 0.3F;
+ 			} else if (entity instanceof EntityItemFrame) {
+ 				itemStack = ((EntityItemFrame) entity).getDisplayedItem();
+ 				Vec3i facingVec = ((EntityItemFrame) entity).facingDirection.getDirectionVec();
+ 				offsetX = facingVec.x * 0.1f;
+ 				offsetZ = facingVec.z * 0.1f;
+ 			}
+ 			if (itemStack != null) {
+ 				float[] emission = EmissiveItems.getItemEmission(itemStack);
+ 				if (emission != null) {
+ 					double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * eagPartialTicks;
+ 					double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * eagPartialTicks;
+ 					double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * eagPartialTicks;
+ 					DynamicLightManager.renderDynamicLight("entity_" + entity.getEntityId() + "_item", d0 + offsetX,
+ 							d1 + offsetY, d2 + offsetZ, emission[0] * mag, emission[1] * mag, emission[2] * mag, false);
+ 					return true;
+ 				}
+ 			}
+ 		}
+ 		return false;
+ 	}
+ 
+ 	private static final Matrix4f matrixToBounds_tmpMat4f_1 = new Matrix4f();
+ 	private static final Vector4f matrixToBounds_tmpVec4f_1 = new Vector4f();
+ 	private static final Vector4f[] matrixToBounds_tmpVec4f_array = new Vector4f[] { new Vector4f(-1, -1, -1, 1),
+ 			new Vector4f(-1, -1, 1, 1), new Vector4f(-1, 1, -1, 1), new Vector4f(-1, 1, 1, 1),
+ 			new Vector4f(1, -1, -1, 1), new Vector4f(1, -1, 1, 1), new Vector4f(1, 1, -1, 1),
+ 			new Vector4f(1, 1, 1, 1) };
+ 
+ 	private static AxisAlignedBB matrixToBounds(Matrix4f matrixIn, double x, double y, double z) {
+ 		Matrix4f.invert(matrixIn, matrixToBounds_tmpMat4f_1);
+ 
+ 		float minX = Integer.MAX_VALUE;
+ 		float minY = Integer.MAX_VALUE;
+ 		float minZ = Integer.MAX_VALUE;
+ 		float maxX = Integer.MIN_VALUE;
+ 		float maxY = Integer.MIN_VALUE;
+ 		float maxZ = Integer.MIN_VALUE;
+ 		Vector4f tmpVec = matrixToBounds_tmpVec4f_1;
+ 		float vx, vy, vz;
+ 		for (int i = 0; i < 8; ++i) {
+ 			Matrix4f.transform(matrixToBounds_tmpMat4f_1, matrixToBounds_tmpVec4f_array[i], tmpVec);
+ 			vx = tmpVec.x;
+ 			vy = tmpVec.y;
+ 			vz = tmpVec.z;
+ 			if (vx < minX)
+ 				minX = vx;
+ 			if (vy < minY)
+ 				minY = vy;
+ 			if (vz < minZ)
+ 				minZ = vz;
+ 			if (vx > maxX)
+ 				maxX = vx;
+ 			if (vy > maxY)
+ 				maxY = vy;
+ 			if (vz > maxZ)
+ 				maxZ = vz;
+ 		}
+ 
+ 		return new AxisAlignedBB(minX + x, minY + y, minZ + z, maxX + x, maxY + y, maxZ + z);
+ 	}
+ 
+ 	public static void setupSunCameraTransform(float celestialAngle) {
+ 		GlStateManager.rotate(celestialAngle + 90.0f, 1.0F, 0.0F, 0.0F);
+ 		GlStateManager.rotate(-DeferredStateManager.sunAngle, 0.0F, 1.0F, 0.0F);
+ 		GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
+ 	}

> EOF
