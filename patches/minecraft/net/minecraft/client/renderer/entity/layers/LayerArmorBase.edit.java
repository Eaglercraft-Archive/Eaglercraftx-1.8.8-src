
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 10  @  1

+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
+ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.client.renderer.EntityRenderer;

> DELETE  1  @  1 : 2

> CHANGE  47 : 48  @  47 : 48

~ 			this.func_177179_a((T) modelbase, parInt1);

> INSERT  2 : 3  @  2

+ 			DeferredStateManager.setDefaultMaterialConstants();

> INSERT  1 : 18  @  1

+ 			case CHAIN:
+ 			case IRON:
+ 				DeferredStateManager.setRoughnessConstant(0.123f);
+ 				DeferredStateManager.setMetalnessConstant(0.902f);
+ 				break;
+ 			case GOLD:
+ 				DeferredStateManager.setRoughnessConstant(0.108f);
+ 				DeferredStateManager.setMetalnessConstant(0.907f);
+ 				break;
+ 			case DIAMOND:
+ 				DeferredStateManager.setRoughnessConstant(0.078f);
+ 				DeferredStateManager.setMetalnessConstant(0.588f);
+ 				break;
+ 			default:
+ 				break;
+ 			}
+ 			switch (itemarmor.getArmorMaterial()) {

> INSERT  14 : 15  @  14

+ 				DeferredStateManager.setDefaultMaterialConstants();

> CHANGE  2 : 41  @  2 : 3

~ 					if (DeferredStateManager.isInDeferredPass()) {
~ 						if (!DeferredStateManager.isEnableShadowRender()
~ 								&& DeferredStateManager.forwardCallbackHandler != null) {
~ 							final Matrix4f mat = new Matrix4f(GlStateManager.getModelViewReference());
~ 							final float lx = GlStateManager.getTexCoordX(1), ly = GlStateManager.getTexCoordY(1);
~ 							DeferredStateManager.forwardCallbackHandler.push(new ShadersRenderPassFuture(
~ 									entitylivingbaseIn, EaglerDeferredPipeline.instance.getPartialTicks()) {
~ 								@Override
~ 								public void draw(PassType pass) {
~ 									if (pass == PassType.MAIN) {
~ 										DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
~ 									}
~ 									EntityRenderer.enableLightmapStatic();
~ 									float f = 0.55f;
~ 									GlStateManager.color(1.5F * f, 0.5F * f, 1.5F * f, 1.0F);
~ 									DeferredStateManager.setDefaultMaterialConstants();
~ 									DeferredStateManager.setRoughnessConstant(0.05f);
~ 									DeferredStateManager.setMetalnessConstant(0.01f);
~ 									GlStateManager.pushMatrix();
~ 									GlStateManager.loadMatrix(mat);
~ 									GlStateManager.texCoords2DDirect(1, lx, ly);
~ 									GlStateManager.enableBlend();
~ 									GlStateManager.tryBlendFuncSeparate(GL_ONE, GL_ONE, GL_ZERO, GL_ONE);
~ 									modelbase.setModelAttributes(LayerArmorBase.this.renderer.getMainModel());
~ 									modelbase.setLivingAnimations(entitylivingbaseIn, armorSlot, parFloat2, parFloat3);
~ 									LayerArmorBase.this.func_177179_a((T) modelbase, parInt1);
~ 									LayerArmorBase.this.func_177183_a(entitylivingbaseIn, (T) modelbase, armorSlot,
~ 											parFloat2, parFloat3, parFloat4, parFloat5, parFloat6, parFloat7);
~ 									DeferredStateManager.setHDRTranslucentPassBlendFunc();
~ 									GlStateManager.enableBlend();
~ 									GlStateManager.popMatrix();
~ 									EntityRenderer.disableLightmapStatic();
~ 									GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
~ 								}
~ 							});
~ 						}
~ 						break;
~ 					}
~ 					this.func_177183_a(entitylivingbaseIn, (T) modelbase, armorSlot, parFloat2, parFloat3, parFloat4,

> CHANGE  27 : 31  @  27 : 28

~ 		boolean d = !DeferredStateManager.isInDeferredPass();
~ 		if (d) {
~ 			GlStateManager.color(f1, f1, f1, 1.0F);
~ 		}

> DELETE  3  @  3 : 4

> CHANGE  1 : 5  @  1 : 2

~ 			if (d) {
~ 				GlStateManager.blendFunc(768, 1);
~ 				GlStateManager.color(0.5F * f2, 0.25F * f2, 0.8F * f2, 1.0F);
~ 			}

> CHANGE  24 : 25  @  24 : 25

~ 		String s = HString.format("textures/models/armor/%s_layer_%d%s.png",

> CHANGE  1 : 2  @  1 : 2

~ 						parString1 == null ? "" : HString.format("_%s", new Object[] { parString1 }) });

> EOF
