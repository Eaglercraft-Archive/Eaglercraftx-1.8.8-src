
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  3 : 7  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.AcceleratedEffectRenderer;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.IAcceleratedParticleEngine;
~ 

> INSERT  1 : 9  @  1

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.GBufferAcceleratedEffectRenderer;

> DELETE  3  @  3 : 36

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 2

> INSERT  16 : 18  @  16

+ 	private static final ResourceLocation particleMaterialsTextures = new ResourceLocation(
+ 			"eagler:glsl/deferred/particles_s.png");

> CHANGE  4 : 5  @  4 : 5

~ 	private EaglercraftRandom rand = new EaglercraftRandom();

> INSERT  2 : 5  @  2

+ 	public static final AcceleratedEffectRenderer vanillaAcceleratedParticleRenderer = new AcceleratedEffectRenderer();
+ 	public IAcceleratedParticleEngine acceleratedParticleRenderer = vanillaAcceleratedParticleRenderer;
+ 

> CHANGE  157 : 167  @  157 : 158

~ 	public boolean hasParticlesInAlphaLayer() {
~ 		for (int i = 0; i < 3; ++i) {
~ 			if (!this.fxLayers[i][0].isEmpty()) {
~ 				return true;
~ 			}
~ 		}
~ 		return false;
~ 	}
~ 
~ 	public void renderParticles(Entity entityIn, float partialTicks, int pass) {

> CHANGE  8 : 12  @  8 : 10

~ 		if (!DeferredStateManager.isDeferredRenderer()) {
~ 			GlStateManager.enableBlend();
~ 			GlStateManager.blendFunc(770, 771);
~ 		}

> CHANGE  2 : 7  @  2 : 4

~ 		for (int i = 0; i < 3; ++i) {
~ 			for (int j = 1; j >= 0; --j) {
~ 				if (pass != 2 && j != pass) {
~ 					continue;
~ 				}

> CHANGE  1 : 8  @  1 : 8

~ //					switch (j) {
~ //					case 0:
~ //						GlStateManager.depthMask(false);
~ //						break;
~ //					case 1:
~ //						GlStateManager.depthMask(true);
~ //					}

> INSERT  1 : 3  @  1

+ 					float texCoordWidth = 0.001f;
+ 					float texCoordHeight = 0.001f;

> INSERT  3 : 4  @  3

+ 						GBufferAcceleratedEffectRenderer.isMaterialNormalTexture = false;

> INSERT  1 : 7  @  1

+ 						if (DeferredStateManager.isDeferredRenderer()) {
+ 							GlStateManager.setActiveTexture(33986);
+ 							this.renderer.bindTexture(particleMaterialsTextures);
+ 							GlStateManager.setActiveTexture(33984);
+ 						}
+ 						texCoordWidth = texCoordHeight = 1.0f / 256.0f;

> INSERT  2 : 3  @  2

+ 						GBufferAcceleratedEffectRenderer.isMaterialNormalTexture = true;

> INSERT  1 : 4  @  1

+ 						TextureMap blockMap = (TextureMap) this.renderer.getTexture(TextureMap.locationBlocksTexture);
+ 						texCoordWidth = 1.0f / blockMap.getWidth();
+ 						texCoordHeight = 1.0f / blockMap.getHeight();

> INSERT  7 : 11  @  7

+ 					boolean legacyRenderingHasOccured = false;
+ 
+ 					acceleratedParticleRenderer.begin(partialTicks);
+ 

> CHANGE  4 : 9  @  4 : 5

~ 							if (!entityfx.renderAccelerated(acceleratedParticleRenderer, entityIn, partialTicks, f, f4,
~ 									f1, f2, f3)) {
~ 								entityfx.renderParticle(worldrenderer, entityIn, partialTicks, f, f4, f1, f2, f3);
~ 								legacyRenderingHasOccured = true;
~ 							}

> INSERT  9 : 10  @  9

+ 							final int l = i;

> CHANGE  2 : 5  @  2 : 5

~ 									return l == 0 ? "MISC_TEXTURE"
~ 											: (l == 1 ? "TERRAIN_TEXTURE"
~ 													: (l == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + l));

> CHANGE  6 : 13  @  6 : 7

~ 					if (legacyRenderingHasOccured) {
~ 						tessellator.draw();
~ 					} else {
~ 						worldrenderer.finishDrawing();
~ 					}
~ 
~ 					acceleratedParticleRenderer.draw(texCoordWidth, texCoordHeight);

> EOF
