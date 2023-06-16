
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  3 : 6  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.AcceleratedEffectRenderer;
~ 

> INSERT  1 : 7  @  1

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  3  @  3 : 36

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 2

> CHANGE  20 : 21  @  20 : 21

~ 	private EaglercraftRandom rand = new EaglercraftRandom();

> INSERT  2 : 4  @  2

+ 	private AcceleratedEffectRenderer acceleratedParticleRenderer = new AcceleratedEffectRenderer();
+ 

> CHANGE  170 : 172  @  170 : 172

~ 		for (int i = 0; i < 3; ++i) {
~ 			for (int j = 1; j >= 0; --j) {

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

> INSERT  4 : 5  @  4

+ 						texCoordWidth = texCoordHeight = 1.0f / 256.0f;

> INSERT  3 : 6  @  3

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
