
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 9  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
~ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
~ import net.minecraft.client.renderer.EntityRenderer;

> DELETE  1  @  1 : 4

> INSERT  11 : 33  @  11

+ 		if (DeferredStateManager.isInDeferredPass()) {
+ 			GlStateManager.disableExtensionPipeline();
+ 			EntityRenderer.disableLightmapStatic();
+ 			GlStateManager.tryBlendFuncSeparate(768, GL_ONE, GL_ZERO, GL_ZERO);
+ 			GlStateManager.disableCull();
+ 			float bright = 0.04f;
+ 			GlStateManager.color(6.0f * bright, 6.25f * bright, 7.0f * bright, 1.0f);
+ 			doRender0(entitylightningbolt, d0, d1, d2, var8, var9);
+ 			GlStateManager.enableCull();
+ 			DeferredStateManager.setHDRTranslucentPassBlendFunc();
+ 			GlStateManager.enableExtensionPipeline();
+ 			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 			return;
+ 		}
+ 		GlStateManager.enableBlend();
+ 		GlStateManager.blendFunc(770, 1);
+ 		doRender0(entitylightningbolt, d0, d1, d2, var8, var9);
+ 		GlStateManager.disableBlend();
+ 	}
+ 
+ 	private void doRender0(EntityLightningBolt entitylightningbolt, double d0, double d1, double d2, float var8,
+ 			float var9) {

> DELETE  4  @  4 : 6

> CHANGE  4 : 5  @  4 : 5

~ 		EaglercraftRandom random = new EaglercraftRandom(entitylightningbolt.boltVertex);

> CHANGE  9 : 10  @  9 : 10

~ 			EaglercraftRandom random1 = new EaglercraftRandom(entitylightningbolt.boltVertex);

> DELETE  73  @  73 : 74

> EOF
