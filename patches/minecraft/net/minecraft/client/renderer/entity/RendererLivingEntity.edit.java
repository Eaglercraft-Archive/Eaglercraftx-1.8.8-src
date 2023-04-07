
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;

> INSERT  1 : 15  @  1

+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.NameTagRenderer;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
+ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;

> INSERT  4 : 6  @  4

+ import net.minecraft.client.model.ModelBiped;
+ import net.minecraft.client.renderer.EntityRenderer;

> DELETE  1  @  1 : 3

> DELETE  1  @  1 : 4

> DELETE  10  @  10 : 13

> CHANGE  16 : 17  @  16 : 17

~ 		return this.layerRenderers.add((LayerRenderer<T>) layer);

> DELETE  115  @  115 : 116

> CHANGE  36 : 42  @  36 : 37

~ 	protected void renderModel(T entitylivingbase, float f, float f1, float f2, float f3, float f4, float f5) { // f8,
~ 																												// f7,
~ 																												// f10,
~ 																												// f4,
~ 																												// f9,
~ 																												// 0.0625

> INSERT  6 : 65  @  6

+ 			if (flag1 && DeferredStateManager.isDeferredRenderer()) {
+ 				if (!DeferredStateManager.isEnableShadowRender()
+ 						&& DeferredStateManager.forwardCallbackHandler != null) {
+ 					final Matrix4f mat = new Matrix4f(GlStateManager.getModelViewReference());
+ 					final float lx = GlStateManager.getTexCoordX(1), ly = GlStateManager.getTexCoordY(1);
+ 					DeferredStateManager.forwardCallbackHandler.push(new ShadersRenderPassFuture(entitylivingbase,
+ 							EaglerDeferredPipeline.instance.getPartialTicks()) {
+ 						@Override
+ 						public void draw(PassType pass) {
+ 							if (pass == PassType.MAIN) {
+ 								DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
+ 							}
+ 							EntityRenderer.enableLightmapStatic();
+ 							GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+ 							DeferredStateManager.setDefaultMaterialConstants();
+ 							DeferredStateManager.setRoughnessConstant(0.05f);
+ 							DeferredStateManager.setMetalnessConstant(0.2f);
+ 							DeferredStateManager.setEmissionConstant(0.5f);
+ 							GlStateManager.pushMatrix();
+ 							GlStateManager.loadMatrix(mat);
+ 							GlStateManager.texCoords2DDirect(1, lx, ly);
+ 							DeferredStateManager.setHDRTranslucentPassBlendFunc();
+ 							GlStateManager.enableAlpha();
+ 							GlStateManager.alphaFunc(516, 0.003921569F);
+ 							GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
+ 							RendererLivingEntity.this.bindEntityTexture(entitylivingbase);
+ 							RendererLivingEntity.this.mainModel.swingProgress = RendererLivingEntity.this
+ 									.getSwingProgress(entitylivingbase, f1);
+ 							RendererLivingEntity.this.mainModel.isRiding = entitylivingbase.isRiding();
+ 							RendererLivingEntity.this.mainModel.isChild = entitylivingbase.isChild();
+ 							if (RendererLivingEntity.this.mainModel instanceof ModelBiped) {
+ 								if ((entitylivingbase instanceof EntityPlayer)
+ 										&& ((EntityPlayer) entitylivingbase).isSpectator()) {
+ 									((ModelBiped) RendererLivingEntity.this.mainModel).setInvisible(false);
+ 									((ModelBiped) RendererLivingEntity.this.mainModel).bipedHead.showModel = true;
+ 									((ModelBiped) RendererLivingEntity.this.mainModel).bipedHeadwear.showModel = true;
+ 								} else {
+ 									((ModelBiped) RendererLivingEntity.this.mainModel).setInvisible(true);
+ 								}
+ 							}
+ 							RendererLivingEntity.this.mainModel.setLivingAnimations(entitylivingbase, f, f1, f1);
+ 							RendererLivingEntity.this.mainModel.setRotationAngles(f, f1, f2, f3, f4, f5,
+ 									entitylivingbase);
+ 							RendererLivingEntity.this.mainModel.render(entitylivingbase, f, f1, f2, f3, f4, f5);
+ 							if (RendererLivingEntity.this.mainModel instanceof ModelBiped) {
+ 								if ((entitylivingbase instanceof EntityPlayer)
+ 										&& ((EntityPlayer) entitylivingbase).isSpectator()) {
+ 									((ModelBiped) RendererLivingEntity.this.mainModel).setInvisible(true);
+ 								}
+ 							}
+ 							GlStateManager.alphaFunc(516, 0.1F);
+ 							GlStateManager.popMatrix();
+ 							EntityRenderer.disableLightmapStatic();
+ 							GlStateManager.disableAlpha();
+ 						}
+ 					});
+ 				}
+ 				return;
+ 			}

> CHANGE  25 : 26  @  25 : 26

~ 	public boolean setBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures) {

> CHANGE  9 : 16  @  9 : 65

~ 			GlStateManager.enableShaderBlendAdd();
~ 			float f1 = 1.0F - (float) (i >> 24 & 255) / 255.0F;
~ 			float f2 = (float) (i >> 16 & 255) / 255.0F;
~ 			float f3 = (float) (i >> 8 & 255) / 255.0F;
~ 			float f4 = (float) (i & 255) / 255.0F;
~ 			GlStateManager.setShaderBlendSrc(f1, f1, f1, 1.0F);
~ 			GlStateManager.setShaderBlendAdd(f2 * f1 + 0.4F, f3 * f1, f4 * f1, 0.0f);

> CHANGE  4 : 6  @  4 : 42

~ 	public void unsetBrightness() {
~ 		GlStateManager.disableShaderBlendAdd();

> CHANGE  2 : 3  @  2 : 3

~ 	public void renderLivingAt(T entityLivingBaseIn, double x, double y, double z) {

> INSERT  66 : 70  @  66

+ 					if (DeferredStateManager.isInDeferredPass()) {
+ 						NameTagRenderer.renderNameTag(entitylivingbase, null, d0, d1, d2, -69);
+ 						return;
+ 					}

> CHANGE  4 : 5  @  4 : 5

~ 					EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);

> EOF
