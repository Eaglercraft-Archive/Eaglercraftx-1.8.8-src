
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
+ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;

> CHANGE  2 : 4  @  2 : 3

~ import net.minecraft.client.renderer.EntityRenderer;
~ import net.minecraft.client.renderer.entity.RenderManager;

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

> INSERT  1 : 2  @  1

+ import net.minecraft.util.MathHelper;

> INSERT  11 : 44  @  11

+ 		if (DeferredStateManager.isInDeferredPass()) {
+ 			if (DeferredStateManager.forwardCallbackHandler != null) {
+ 				final Matrix4f mat = new Matrix4f(GlStateManager.getModelViewReference());
+ 				DeferredStateManager.forwardCallbackHandler.push(new ShadersRenderPassFuture(entityslime) {
+ 					@Override
+ 					public void draw(PassType pass) {
+ 						if (pass == PassType.MAIN) {
+ 							DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
+ 						}
+ 						DeferredStateManager.setDefaultMaterialConstants();
+ 						DeferredStateManager.setRoughnessConstant(0.3f);
+ 						DeferredStateManager.setMetalnessConstant(0.1f);
+ 						boolean flag = LayerSlimeGel.this.slimeRenderer.setBrightness(entityslime, partialTicks,
+ 								LayerSlimeGel.this.shouldCombineTextures());
+ 						EntityRenderer.enableLightmapStatic();
+ 						GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+ 						GlStateManager.pushMatrix();
+ 						GlStateManager.loadMatrix(mat);
+ 						RenderManager.setupLightmapCoords(entityslime, partialTicks);
+ 						LayerSlimeGel.this.slimeModel
+ 								.setModelAttributes(LayerSlimeGel.this.slimeRenderer.getMainModel());
+ 						LayerSlimeGel.this.slimeRenderer.bindTexture(RenderSlime.slimeTextures);
+ 						LayerSlimeGel.this.slimeModel.render(entityslime, f, f1, f2, f3, f4, f5);
+ 						GlStateManager.popMatrix();
+ 						EntityRenderer.disableLightmapStatic();
+ 						if (flag) {
+ 							LayerSlimeGel.this.slimeRenderer.unsetBrightness();
+ 						}
+ 					}
+ 				});
+ 			}
+ 			return;
+ 		}

> DELETE  2  @  2 : 3

> DELETE  5  @  5 : 6

> EOF
