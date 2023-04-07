
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  4 : 12  @  4

+ 
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture.PassType;
+ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;

> DELETE  21  @  21 : 22

> DELETE  3  @  3 : 4

> INSERT  21 : 22  @  21

+ import net.minecraft.item.ItemBlock;

> INSERT  5 : 6  @  5

+ import net.minecraft.util.EnumWorldBlockLayer;

> INSERT  63 : 67  @  63

+ 	public static float renderPosX = 0.0f;
+ 	public static float renderPosY = 0.0f;
+ 	public static float renderPosZ = 0.0f;
+ 

> CHANGE  12 : 82  @  12 : 15

~ 				if (DeferredStateManager.isInDeferredPass() && isTransparentItem(stack)) {
~ 					if (DeferredStateManager.forwardCallbackHandler != null) {
~ 						final Matrix4f mat = new Matrix4f(GlStateManager.getModelViewReference());
~ 						final float lx = GlStateManager.getTexCoordX(1), ly = GlStateManager.getTexCoordY(1);
~ 						DeferredStateManager.forwardCallbackHandler.push(new ShadersRenderPassFuture(renderPosX,
~ 								renderPosY, renderPosZ, EaglerDeferredPipeline.instance.getPartialTicks()) {
~ 							@Override
~ 							public void draw(PassType pass) {
~ 								if (pass == PassType.MAIN) {
~ 									DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
~ 								}
~ 								EntityRenderer.enableLightmapStatic();
~ 								GlStateManager.pushMatrix();
~ 								GlStateManager.loadMatrix(mat);
~ 								GlStateManager.texCoords2DDirect(1, lx, ly);
~ 								Minecraft.getMinecraft().getTextureManager()
~ 										.bindTexture(TextureMap.locationBlocksTexture);
~ 								RenderItem.this.renderModel(model, stack);
~ 								if (pass != PassType.SHADOW && stack.hasEffect()) {
~ 									GlStateManager.color(1.5F, 0.5F, 1.5F, 1.0F);
~ 									DeferredStateManager.setDefaultMaterialConstants();
~ 									DeferredStateManager.setRoughnessConstant(0.05f);
~ 									DeferredStateManager.setMetalnessConstant(0.01f);
~ 									GlStateManager.blendFunc(768, 1);
~ 									renderEffect(model);
~ 									DeferredStateManager.setHDRTranslucentPassBlendFunc();
~ 								}
~ 								GlStateManager.popMatrix();
~ 								EntityRenderer.disableLightmapStatic();
~ 								GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
~ 							}
~ 						});
~ 					}
~ 				} else {
~ 					this.renderModel(model, stack);
~ 					if (stack.hasEffect()) {
~ 						if (DeferredStateManager.isInDeferredPass()) {
~ 							if (DeferredStateManager.forwardCallbackHandler != null
~ 									&& !DeferredStateManager.isEnableShadowRender()) {
~ 								final Matrix4f mat = new Matrix4f(GlStateManager.getModelViewReference());
~ 								final float lx = GlStateManager.getTexCoordX(1), ly = GlStateManager.getTexCoordY(1);
~ 								DeferredStateManager.forwardCallbackHandler.push(new ShadersRenderPassFuture(renderPosX,
~ 										renderPosY, renderPosZ, EaglerDeferredPipeline.instance.getPartialTicks()) {
~ 									@Override
~ 									public void draw(PassType pass) {
~ 										if (pass == PassType.MAIN) {
~ 											DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
~ 										}
~ 										EntityRenderer.enableLightmapStatic();
~ 										GlStateManager.color(1.5F, 0.5F, 1.5F, 1.0F);
~ 										DeferredStateManager.setDefaultMaterialConstants();
~ 										DeferredStateManager.setRoughnessConstant(0.05f);
~ 										DeferredStateManager.setMetalnessConstant(0.01f);
~ 										GlStateManager.pushMatrix();
~ 										GlStateManager.loadMatrix(mat);
~ 										GlStateManager.texCoords2DDirect(1, lx, ly);
~ 										GlStateManager.tryBlendFuncSeparate(GL_ONE, GL_ONE, GL_ZERO, GL_ONE);
~ 										renderEffect(model);
~ 										DeferredStateManager.setHDRTranslucentPassBlendFunc();
~ 										GlStateManager.popMatrix();
~ 										EntityRenderer.disableLightmapStatic();
~ 										GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
~ 									}
~ 								});
~ 							}
~ 						} else {
~ 							GlStateManager.blendFunc(768, 1);
~ 							this.renderEffect(model);
~ 						}
~ 					}

> INSERT  7 : 13  @  7

+ 	private static boolean isTransparentItem(ItemStack stack) {
+ 		Item itm = stack.getItem();
+ 		return itm instanceof ItemBlock
+ 				&& ((ItemBlock) itm).getBlock().getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT;
+ 	}
+ 

> DELETE  4  @  4 : 5

> INSERT  123 : 124  @  123

+ 		boolean flag = DeferredStateManager.isEnableShadowRender();

> CHANGE  1 : 2  @  1 : 2

~ 			GlStateManager.cullFace(flag ? GL_BACK : GL_FRONT);

> CHANGE  3 : 4  @  3 : 4

~ 		GlStateManager.cullFace(flag ? GL_FRONT : GL_BACK);

> EOF
