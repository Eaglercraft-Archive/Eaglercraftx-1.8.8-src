
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 11  @  3 : 4

~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShadersRenderPassFuture;
~ import net.lax1dude.eaglercraft.v1_8.vector.Matrix4f;
~ import net.minecraft.client.renderer.EntityRenderer;

> DELETE  1  @  1 : 3

> DELETE  4  @  4 : 5

> INSERT  6 : 8  @  6

+ 		if (DeferredStateManager.isEnableShadowRender())
+ 			return;

> INSERT  3 : 4  @  3

+ 			boolean deferred = DeferredStateManager.isInDeferredPass();

> CHANGE  10 : 18  @  10 : 12

~ 				if (deferred) {
~ 					DeferredStateManager.setDefaultMaterialConstants();
~ 					DeferredStateManager.setRoughnessConstant(0.3f);
~ 					DeferredStateManager.setMetalnessConstant(0.3f);
~ 					DeferredStateManager.setEmissionConstant(0.9f);
~ 				}
~ 				EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, 10497);
~ 				EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, 10497);

> CHANGE  24 : 26  @  24 : 25

~ 				worldrenderer.begin(7, deferred ? DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL
~ 						: DefaultVertexFormats.POSITION_TEX_COLOR);

> INSERT  6 : 8  @  6

+ 				if (deferred)
+ 					worldrenderer.genNormals(true, 0);

> INSERT  8 : 10  @  8

+ 				if (deferred)
+ 					worldrenderer.genNormals(true, 0);

> INSERT  8 : 10  @  8

+ 				if (deferred)
+ 					worldrenderer.genNormals(true, 0);

> INSERT  6 : 8  @  6

+ 				if (deferred)
+ 					worldrenderer.genNormals(true, 0);

> INSERT  1 : 10  @  1

+ 
+ 				if (deferred) {
+ 					DeferredStateManager.setDefaultMaterialConstants();
+ 					GlStateManager.enableLighting();
+ 					GlStateManager.depthMask(true);
+ 					i = k;
+ 					continue;
+ 				}
+ 

> INSERT  55 : 149  @  55

+ 			if (deferred && list.size() > 0) {
+ 				if (DeferredStateManager.forwardCallbackHandler != null) {
+ 					final Matrix4f mat = new Matrix4f(GlStateManager.getModelViewReference());
+ 					final float lx = GlStateManager.getTexCoordX(1), ly = GlStateManager.getTexCoordY(1);
+ 					DeferredStateManager.forwardCallbackHandler
+ 							.push(new ShadersRenderPassFuture((float) d0, (float) d1, (float) d2, f) {
+ 								@Override
+ 								public void draw(PassType pass) {
+ 									if (pass == PassType.MAIN) {
+ 										DeferredStateManager.reportForwardRenderObjectPosition2(x, y, z);
+ 									}
+ 									TileEntityBeaconRenderer.this.bindTexture(beaconBeam);
+ 									EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, 10497);
+ 									EaglercraftGPU.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, 10497);
+ 									DeferredStateManager.setDefaultMaterialConstants();
+ 									DeferredStateManager.setRoughnessConstant(0.3f);
+ 									DeferredStateManager.setMetalnessConstant(0.2f);
+ 									DeferredStateManager.setEmissionConstant(0.6f);
+ 									GlStateManager.depthMask(false);
+ 									GlStateManager.pushMatrix();
+ 									GlStateManager.loadMatrix(mat);
+ 									GlStateManager.texCoords2DDirect(1, lx, ly);
+ 									EntityRenderer.enableLightmapStatic();
+ 									GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 									List list = tileentitybeacon.getBeamSegments();
+ 									int i = 0;
+ 
+ 									for (int j = 0; j < list.size(); ++j) {
+ 										TileEntityBeacon.BeamSegment tileentitybeacon$beamsegment = (TileEntityBeacon.BeamSegment) list
+ 												.get(j);
+ 										int k = i + tileentitybeacon$beamsegment.getHeight();
+ 
+ 										double d3 = (double) tileentitybeacon.getWorld().getTotalWorldTime()
+ 												+ (double) f;
+ 										double d4 = MathHelper.func_181162_h(
+ 												-d3 * 0.2D - (double) MathHelper.floor_double(-d3 * 0.1D));
+ 										float f2 = tileentitybeacon$beamsegment.getColors()[0];
+ 										float f3 = tileentitybeacon$beamsegment.getColors()[1];
+ 										float f4 = tileentitybeacon$beamsegment.getColors()[2];
+ 
+ 										double d15 = 0.0D;
+ 										double d16 = 1.0D;
+ 										double d17 = -1.0D + d4;
+ 
+ 										d15 = -1.0D + d4;
+ 										d16 = (double) ((float) tileentitybeacon$beamsegment.getHeight() * f1) + d15;
+ 										worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
+ 										worldrenderer.pos(d0 + 0.2D, d1 + (double) k, d2 + 0.2D).tex(1.0D, d16)
+ 												.color(f2, f3, f4, 0.125F).endVertex();
+ 										worldrenderer.pos(d0 + 0.2D, d1 + (double) i, d2 + 0.2D).tex(1.0D, d15)
+ 												.color(f2, f3, f4, 0.125F).endVertex();
+ 										worldrenderer.pos(d0 + 0.8D, d1 + (double) i, d2 + 0.2D).tex(0.0D, d15)
+ 												.color(f2, f3, f4, 0.125F).endVertex();
+ 										worldrenderer.pos(d0 + 0.8D, d1 + (double) k, d2 + 0.2D).tex(0.0D, d16)
+ 												.color(f2, f3, f4, 0.125F).endVertex();
+ 										worldrenderer.genNormals(true, 0);
+ 										worldrenderer.pos(d0 + 0.8D, d1 + (double) k, d2 + 0.8D).tex(1.0D, d16)
+ 												.color(f2, f3, f4, 0.125F).endVertex();
+ 										worldrenderer.pos(d0 + 0.8D, d1 + (double) i, d2 + 0.8D).tex(1.0D, d15)
+ 												.color(f2, f3, f4, 0.125F).endVertex();
+ 										worldrenderer.pos(d0 + 0.2D, d1 + (double) i, d2 + 0.8D).tex(0.0D, d15)
+ 												.color(f2, f3, f4, 0.125F).endVertex();
+ 										worldrenderer.pos(d0 + 0.2D, d1 + (double) k, d2 + 0.8D).tex(0.0D, d16)
+ 												.color(f2, f3, f4, 0.125F).endVertex();
+ 										worldrenderer.genNormals(true, 0);
+ 										worldrenderer.pos(d0 + 0.8D, d1 + (double) k, d2 + 0.2D).tex(1.0D, d16)
+ 												.color(f2, f3, f4, 0.125F).endVertex();
+ 										worldrenderer.pos(d0 + 0.8D, d1 + (double) i, d2 + 0.2D).tex(1.0D, d15)
+ 												.color(f2, f3, f4, 0.125F).endVertex();
+ 										worldrenderer.pos(d0 + 0.8D, d1 + (double) i, d2 + 0.8D).tex(0.0D, d15)
+ 												.color(f2, f3, f4, 0.125F).endVertex();
+ 										worldrenderer.pos(d0 + 0.8D, d1 + (double) k, d2 + 0.8D).tex(0.0D, d16)
+ 												.color(f2, f3, f4, 0.125F).endVertex();
+ 										worldrenderer.genNormals(true, 0);
+ 										worldrenderer.pos(d0 + 0.2D, d1 + (double) k, d2 + 0.8D).tex(1.0D, d16)
+ 												.color(f2, f3, f4, 0.125F).endVertex();
+ 										worldrenderer.pos(d0 + 0.2D, d1 + (double) i, d2 + 0.8D).tex(1.0D, d15)
+ 												.color(f2, f3, f4, 0.125F).endVertex();
+ 										worldrenderer.pos(d0 + 0.2D, d1 + (double) i, d2 + 0.2D).tex(0.0D, d15)
+ 												.color(f2, f3, f4, 0.125F).endVertex();
+ 										worldrenderer.pos(d0 + 0.2D, d1 + (double) k, d2 + 0.2D).tex(0.0D, d16)
+ 												.color(f2, f3, f4, 0.125F).endVertex();
+ 										worldrenderer.genNormals(true, 0);
+ 										tessellator.draw();
+ 										i = k;
+ 									}
+ 									GlStateManager.popMatrix();
+ 									EntityRenderer.disableLightmapStatic();
+ 									GlStateManager.depthMask(true);
+ 								}
+ 							});
+ 				}
+ 			}
+ 

> EOF
