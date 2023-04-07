
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 11  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
~ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
~ import net.minecraft.client.renderer.EntityRenderer;

> DELETE  2  @  2 : 4

> INSERT  6 : 48  @  6

+ 		if (DeferredStateManager.isInDeferredPass()) {
+ 			if (entitydragon.deathTicks > 0 && !DeferredStateManager.isEnableShadowRender()
+ 					&& DeferredStateManager.forwardCallbackHandler != null) {
+ 				final Matrix4f mat = new Matrix4f(GlStateManager.getModelViewReference());
+ 				final float ly = GlStateManager.getTexCoordY(1);
+ 				DeferredStateManager.forwardCallbackHandler.push(
+ 						new ShadersRenderPassFuture(entitydragon, EaglerDeferredPipeline.instance.getPartialTicks()) {
+ 							@Override
+ 							public void draw(PassType pass) {
+ 								if (pass == PassType.MAIN) {
+ 									DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
+ 								}
+ 								float bright = 0.01f + ly * 0.001f;
+ 								GlStateManager.color(bright, bright, bright, 1.0F);
+ 								GlStateManager.pushMatrix();
+ 								GlStateManager.loadMatrix(mat);
+ 								GlStateManager.tryBlendFuncSeparate(770, GL_ONE, GL_ZERO, GL_ZERO);
+ 								GlStateManager.enableCull();
+ 								GlStateManager.enableBlend();
+ 								GlStateManager.disableExtensionPipeline();
+ 								EntityRenderer.disableLightmapStatic();
+ 								doRenderLayer0(entitydragon, var2, var3, f, var5, var6, var7, var8);
+ 								GlStateManager.enableExtensionPipeline();
+ 								GlStateManager.popMatrix();
+ 								EntityRenderer.disableLightmapStatic();
+ 								GlStateManager.disableAlpha();
+ 								DeferredStateManager.setHDRTranslucentPassBlendFunc();
+ 							}
+ 						});
+ 			}
+ 		} else {
+ 			GlStateManager.enableBlend();
+ 			GlStateManager.enableCull();
+ 			GlStateManager.blendFunc(770, 1);
+ 			doRenderLayer0(entitydragon, var2, var3, f, var5, var6, var7, var8);
+ 			GlStateManager.disableBlend();
+ 			GlStateManager.disableCull();
+ 		}
+ 	}
+ 
+ 	public void doRenderLayer0(EntityDragon entitydragon, float var2, float var3, float f, float var5, float var6,
+ 			float var7, float var8) {

> CHANGE  10 : 11  @  10 : 11

~ 			EaglercraftRandom random = new EaglercraftRandom(432L);

> DELETE  2  @  2 : 4

> DELETE  1  @  1 : 2

> DELETE  27  @  27 : 29

> EOF
