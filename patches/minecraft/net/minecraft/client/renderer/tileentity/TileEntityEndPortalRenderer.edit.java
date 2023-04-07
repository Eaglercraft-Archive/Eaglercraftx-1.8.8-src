
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 9  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;

> DELETE  3  @  3 : 4

> DELETE  1  @  1 : 3

> CHANGE  7 : 8  @  7 : 8

~ 	private static final EaglercraftRandom field_147527_e = new EaglercraftRandom(31100L);

> INSERT  3 : 31  @  3

+ 		if (DeferredStateManager.isInDeferredPass()) {
+ 			if (!DeferredStateManager.isInParaboloidPass() && !DeferredStateManager.isEnableShadowRender()
+ 					&& DeferredStateManager.forwardCallbackHandler != null) {
+ 				DeferredStateManager.forwardCallbackHandler
+ 						.push(new ShadersRenderPassFuture((float) d0, (float) d1, (float) d2, var8) {
+ 							@Override
+ 							public void draw(PassType pass) {
+ 								if (pass == PassType.MAIN) {
+ 									DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
+ 								}
+ 								DeferredStateManager.setDefaultMaterialConstants();
+ 								DeferredStateManager.setRoughnessConstant(0.3f);
+ 								DeferredStateManager.setMetalnessConstant(0.3f);
+ 								DeferredStateManager.setEmissionConstant(0.9f);
+ 								renderTileEntityAt0(var1, d0, d1, d2, var8, var9);
+ 								DeferredStateManager.setDefaultMaterialConstants();
+ 								DeferredStateManager.setHDRTranslucentPassBlendFunc();
+ 							}
+ 						});
+ 			}
+ 			return;
+ 		}
+ 		GlStateManager.enableBlend();
+ 		renderTileEntityAt0(var1, d0, d1, d2, var8, var9);
+ 		GlStateManager.disableBlend();
+ 	}
+ 
+ 	private void renderTileEntityAt0(TileEntityEndPortal var1, double d0, double d1, double d2, float var8, int var9) {

> CHANGE  17 : 22  @  17 : 19

~ 				if (DeferredStateManager.isInDeferredPass()) {
~ 					DeferredStateManager.setHDRTranslucentPassBlendFunc();
~ 				} else {
~ 					GlStateManager.blendFunc(770, 771);
~ 				}

> CHANGE  7 : 12  @  7 : 9

~ 				if (DeferredStateManager.isInDeferredPass()) {
~ 					GlStateManager.tryBlendFuncSeparate(GL_ONE, GL_ONE, GL_ZERO, GL_ZERO);
~ 				} else {
~ 					GlStateManager.blendFunc(1, 1);
~ 				}

> CHANGE  3 : 4  @  3 : 4

~ 			float f7 = (float) (-(d1 + (double) f3 - 1.25));

> CHANGE  13 : 14  @  13 : 17

~ 			GlStateManager.enableTexGen();

> CHANGE  33 : 34  @  33 : 38

~ 		GlStateManager.disableTexGen();

> EOF
