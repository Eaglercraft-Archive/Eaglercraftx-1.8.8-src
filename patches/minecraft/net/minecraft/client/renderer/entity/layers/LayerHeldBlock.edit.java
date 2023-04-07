
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 7  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
+ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;

> CHANGE  4 : 5  @  4 : 6

~ import net.minecraft.client.renderer.EntityRenderer;

> DELETE  1  @  1 : 2

> INSERT  2 : 3  @  2

+ import net.minecraft.util.EnumWorldBlockLayer;

> INSERT  25 : 55  @  25

+ 
+ 			if (DeferredStateManager.isInDeferredPass()
+ 					&& iblockstate.getBlock().getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT) {
+ 				if (DeferredStateManager.forwardCallbackHandler != null) {
+ 					final Matrix4f mat = new Matrix4f(GlStateManager.getModelViewReference());
+ 					final float lx = GlStateManager.getTexCoordX(1), ly = GlStateManager.getTexCoordY(1);
+ 					DeferredStateManager.forwardCallbackHandler.push(new ShadersRenderPassFuture(entityenderman) {
+ 						@Override
+ 						public void draw(PassType pass) {
+ 							if (pass == PassType.MAIN) {
+ 								DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
+ 							}
+ 							EntityRenderer.enableLightmapStatic();
+ 							GlStateManager.pushMatrix();
+ 							GlStateManager.loadMatrix(mat);
+ 							GlStateManager.texCoords2DDirect(1, lx, ly);
+ 							LayerHeldBlock.this.endermanRenderer.bindTexture(TextureMap.locationBlocksTexture);
+ 							GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+ 							blockrendererdispatcher.renderBlockBrightness(iblockstate, 1.0F);
+ 							GlStateManager.popMatrix();
+ 							EntityRenderer.disableLightmapStatic();
+ 							GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+ 						}
+ 					});
+ 				}
+ 				GlStateManager.popMatrix();
+ 				GlStateManager.disableRescaleNormal();
+ 				return;
+ 			}
+ 

> INSERT  3 : 4  @  3

+ 

> EOF
