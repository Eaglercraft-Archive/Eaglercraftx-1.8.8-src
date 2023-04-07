
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

> DELETE  1  @  1 : 2

> INSERT  17 : 67  @  17

+ 			if (DeferredStateManager.isInDeferredPass()) {
+ 				if (!DeferredStateManager.isEnableShadowRender()
+ 						&& DeferredStateManager.forwardCallbackHandler != null) {
+ 					final Matrix4f mat = new Matrix4f(GlStateManager.getModelViewReference());
+ 					DeferredStateManager.forwardCallbackHandler.push(new ShadersRenderPassFuture(entitywither) {
+ 						@Override
+ 						public void draw(PassType pass) {
+ 							if (pass == PassType.MAIN) {
+ 								DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
+ 							}
+ 							boolean flag = entitywither.isInvisible();
+ 							DeferredStateManager.setDefaultMaterialConstants();
+ 							DeferredStateManager.setRoughnessConstant(0.5f);
+ 							DeferredStateManager.setMetalnessConstant(0.2f);
+ 							DeferredStateManager.setEmissionConstant(0.9f);
+ 							EntityRenderer.disableLightmapStatic();
+ 							GlStateManager.tryBlendFuncSeparate(GL_ONE, GL_ONE, GL_ZERO, GL_ZERO);
+ 							GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
+ 							if (flag) {
+ 								GlStateManager.depthMask(false);
+ 							}
+ 							LayerWitherAura.this.witherRenderer.bindTexture(WITHER_ARMOR);
+ 							GlStateManager.pushMatrix();
+ 							GlStateManager.loadMatrix(mat);
+ 							GlStateManager.matrixMode(5890);
+ 							GlStateManager.loadIdentity();
+ 							float f7 = (float) entitywither.ticksExisted + f2;
+ 							float f8 = MathHelper.cos(f7 * 0.02F) * 3.0F;
+ 							float f9 = f7 * 0.01F;
+ 							GlStateManager.translate(f8, f9, 0.0F);
+ 							GlStateManager.matrixMode(5888);
+ 							GlStateManager.disableCull();
+ 							LayerWitherAura.this.witherModel.setLivingAnimations(entitywither, f, f1, f2);
+ 							LayerWitherAura.this.witherModel.setRotationAngles(f, f1, f2, f3, f4, f5, entitywither);
+ 							LayerWitherAura.this.witherModel
+ 									.setModelAttributes(LayerWitherAura.this.witherRenderer.getMainModel());
+ 							LayerWitherAura.this.witherModel.render(entitywither, f, f1, f3, f4, f5, f6);
+ 							GlStateManager.matrixMode(5890);
+ 							GlStateManager.loadIdentity();
+ 							GlStateManager.matrixMode(5888);
+ 							GlStateManager.popMatrix();
+ 							if (flag) {
+ 								GlStateManager.depthMask(true);
+ 							}
+ 							GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+ 						}
+ 					});
+ 				}
+ 				return;
+ 			}

> INSERT  22 : 23  @  22

+ 			GlStateManager.depthMask(true);

> EOF
