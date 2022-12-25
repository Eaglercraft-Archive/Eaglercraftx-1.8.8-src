
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  5 : 8  @  7 : 8

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.AcceleratedEffectRenderer;
~ 

> INSERT  9 : 15  @  9

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  18  @  12 : 45

> DELETE  19  @  46 : 47

> DELETE  20  @  48 : 49

> CHANGE  40 : 41  @  69 : 70

~ 	private EaglercraftRandom rand = new EaglercraftRandom();

> INSERT  43 : 45  @  72

+ 	private AcceleratedEffectRenderer acceleratedParticleRenderer = new AcceleratedEffectRenderer();
+ 

> CHANGE  215 : 217  @  242 : 244

~ 		for (int i = 0; i < 3; ++i) {
~ 			for (int j = 1; j >= 0; --j) {

> CHANGE  218 : 225  @  245 : 252

~ //					switch (j) {
~ //					case 0:
~ //						GlStateManager.depthMask(false);
~ //						break;
~ //					case 1:
~ //						GlStateManager.depthMask(true);
~ //					}

> INSERT  226 : 228  @  253

+ 					float texCoordWidth = 0.001f;
+ 					float texCoordHeight = 0.001f;

> INSERT  232 : 233  @  257

+ 						texCoordWidth = texCoordHeight = 1.0f / 256.0f;

> INSERT  236 : 239  @  260

+ 						TextureMap blockMap = (TextureMap) this.renderer.getTexture(TextureMap.locationBlocksTexture);
+ 						texCoordWidth = 1.0f / blockMap.getWidth();
+ 						texCoordHeight = 1.0f / blockMap.getHeight();

> INSERT  246 : 250  @  267

+ 					boolean legacyRenderingHasOccured = false;
+ 
+ 					acceleratedParticleRenderer.begin(partialTicks);
+ 

> CHANGE  254 : 259  @  271 : 272

~ 							if (!entityfx.renderAccelerated(acceleratedParticleRenderer, entityIn, partialTicks, f, f4,
~ 									f1, f2, f3)) {
~ 								entityfx.renderParticle(worldrenderer, entityIn, partialTicks, f, f4, f1, f2, f3);
~ 								legacyRenderingHasOccured = true;
~ 							}

> INSERT  268 : 269  @  281

+ 							final int l = i;

> CHANGE  271 : 274  @  283 : 286

~ 									return l == 0 ? "MISC_TEXTURE"
~ 											: (l == 1 ? "TERRAIN_TEXTURE"
~ 													: (l == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + l));

> CHANGE  280 : 287  @  292 : 293

~ 					if (legacyRenderingHasOccured) {
~ 						tessellator.draw();
~ 					} else {
~ 						worldrenderer.finishDrawing();
~ 					}
~ 
~ 					acceleratedParticleRenderer.draw(texCoordWidth, texCoordHeight);

> EOF
