
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
+ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.client.renderer.EntityRenderer;

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.client.renderer.entity.RendererLivingEntity;

> INSERT  16 : 67  @  16

+ 			if (DeferredStateManager.isInDeferredPass()) {
+ 				if (DeferredStateManager.forwardCallbackHandler != null
+ 						&& !DeferredStateManager.isEnableShadowRender()) {
+ 					final Matrix4f mat = new Matrix4f(GlStateManager.getModelViewReference());
+ 					DeferredStateManager.forwardCallbackHandler.push(new ShadersRenderPassFuture(entitycreeper) {
+ 						@Override
+ 						public void draw(PassType pass) {
+ 							if (pass == PassType.MAIN) {
+ 								DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
+ 							}
+ 							boolean flag = entitycreeper.isInvisible();
+ 							DeferredStateManager.setDefaultMaterialConstants();
+ 							DeferredStateManager.setRoughnessConstant(0.3f);
+ 							DeferredStateManager.setMetalnessConstant(0.1f);
+ 							DeferredStateManager.setEmissionConstant(0.9f);
+ 							EntityRenderer.disableLightmapStatic();
+ 							GlStateManager.tryBlendFuncSeparate(GL_ONE, GL_ONE, GL_ZERO, GL_ZERO);
+ 							GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
+ 							if (flag) {
+ 								GlStateManager.depthMask(false);
+ 							}
+ 							GlStateManager.pushMatrix();
+ 							GlStateManager.loadMatrix(mat);
+ 							GlStateManager.disableCull();
+ 							GlStateManager.matrixMode(5890);
+ 							GlStateManager.pushMatrix();
+ 							GlStateManager.loadIdentity();
+ 							float f7 = (float) entitycreeper.ticksExisted + f2;
+ 							GlStateManager.translate(f7 * 0.01F, f7 * 0.01F, 0.0F);
+ 							GlStateManager.matrixMode(5888);
+ 							LayerCreeperCharge.this.creeperRenderer.bindTexture(LIGHTNING_TEXTURE);
+ 							LayerCreeperCharge.this.creeperModel
+ 									.setModelAttributes(LayerCreeperCharge.this.creeperRenderer.getMainModel());
+ 							LayerCreeperCharge.this.creeperModel.setLivingAnimations(entitycreeper, f, f1, f1);
+ 							LayerCreeperCharge.this.creeperModel.render(entitycreeper, f, f1, f3, f4, f5, f6);
+ 							GlStateManager.matrixMode(5890);
+ 							GlStateManager.popMatrix();
+ 							GlStateManager.matrixMode(5888);
+ 							GlStateManager.popMatrix();
+ 							if (flag) {
+ 								GlStateManager.depthMask(true);
+ 							}
+ 							GlStateManager.enableCull();
+ 							DeferredStateManager.setDefaultMaterialConstants();
+ 							DeferredStateManager.setHDRTranslucentPassBlendFunc();
+ 							GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+ 						}
+ 					});
+ 				}
+ 				return;
+ 			}

> CHANGE  1 : 4  @  1 : 2

~ 			if (flag) {
~ 				GlStateManager.depthMask(false);
~ 			}

> CHANGE  18 : 21  @  18 : 19

~ 			if (flag) {
~ 				GlStateManager.depthMask(true);
~ 			}

> EOF
