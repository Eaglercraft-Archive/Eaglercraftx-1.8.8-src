
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 9  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
~ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
~ import net.minecraft.client.model.ModelSpider;
~ import net.minecraft.client.renderer.EntityRenderer;

> DELETE  1  @  1 : 2

> INSERT  13 : 60  @  13

+ 		if (DeferredStateManager.isInDeferredPass()) {
+ 			if (entityspider.isInvisible()) {
+ 				if (!DeferredStateManager.isEnableShadowRender()
+ 						&& DeferredStateManager.forwardCallbackHandler != null) {
+ 					final Matrix4f mat = new Matrix4f(GlStateManager.getModelViewReference());
+ 					DeferredStateManager.forwardCallbackHandler.push(new ShadersRenderPassFuture(entityspider) {
+ 						@Override
+ 						public void draw(PassType pass) {
+ 							if (pass == PassType.MAIN) {
+ 								DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
+ 							}
+ 							LayerSpiderEyes.this.spiderRenderer.bindTexture(SPIDER_EYES);
+ 							DeferredStateManager.setDefaultMaterialConstants();
+ 							DeferredStateManager.setRoughnessConstant(0.3f);
+ 							DeferredStateManager.setMetalnessConstant(0.1f);
+ 							DeferredStateManager.setEmissionConstant(0.9f);
+ 							EntityRenderer.disableLightmapStatic();
+ 							GlStateManager.tryBlendFuncSeparate(GL_ONE, GL_ONE, GL_ZERO, GL_ZERO);
+ 							GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
+ 							GlStateManager.depthMask(false);
+ 							GlStateManager.pushMatrix();
+ 							GlStateManager.loadMatrix(mat);
+ 							GlStateManager.disableCull();
+ 							ModelSpider eee = (ModelSpider) LayerSpiderEyes.this.spiderRenderer.getMainModel();
+ 							eee.setLivingAnimations(entityspider, f, f1, f1);
+ 							eee.render(entityspider, f, f1, f3, f4, f5, f6);
+ 							GlStateManager.popMatrix();
+ 							GlStateManager.depthMask(true);
+ 							GlStateManager.enableCull();
+ 							DeferredStateManager.setDefaultMaterialConstants();
+ 							DeferredStateManager.setHDRTranslucentPassBlendFunc();
+ 							GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+ 						}
+ 					});
+ 				}
+ 				return;
+ 			}
+ 			this.spiderRenderer.bindTexture(SPIDER_EYES);
+ 			DeferredStateManager.setEmissionConstant(0.5f);
+ 			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+ 			GlStateManager.enablePolygonOffset();
+ 			GlStateManager.doPolygonOffset(-0.025f, 1.0f);
+ 			this.spiderRenderer.getMainModel().render(entityspider, f, f1, f3, f4, f5, f6);
+ 			GlStateManager.disablePolygonOffset();
+ 			DeferredStateManager.setEmissionConstant(0.0f);
+ 			return;
+ 		}

> EOF
