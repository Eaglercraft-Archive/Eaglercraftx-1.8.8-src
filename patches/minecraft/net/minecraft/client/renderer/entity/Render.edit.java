
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 8  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.NameTagRenderer;

> DELETE  3  @  3 : 4

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 3

> DELETE  10  @  10 : 11

> INSERT  12 : 15  @  12

+ 		if (DeferredStateManager.isEnableShadowRender()) {
+ 			return true;
+ 		}

> INSERT  21 : 25  @  21

+ 	public static void renderNameAdapter(Render r, Entity e, double x, double y, double z) {
+ 		r.renderName(e, x, y, z);
+ 	}
+ 

> CHANGE  28 : 30  @  28 : 30

~ 		EaglerTextureAtlasSprite textureatlassprite = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_0");
~ 		EaglerTextureAtlasSprite textureatlassprite1 = texturemap.getAtlasSprite("minecraft:blocks/fire_layer_1");

> CHANGE  18 : 19  @  18 : 19

~ 			EaglerTextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;

> CHANGE  150 : 152  @  150 : 152

~ 			if (!DeferredStateManager.isInDeferredPass() && this.renderManager.options.field_181151_V
~ 					&& this.shadowSize > 0.0F && !entityIn.isInvisible() && this.renderManager.isRenderShadow()) {

> CHANGE  19 : 20  @  19 : 20

~ 	public void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance) {

> INSERT  2 : 6  @  2

+ 			if (DeferredStateManager.isInDeferredPass()) {
+ 				NameTagRenderer.renderNameTag(entityIn, str, x, y, z, maxDistance);
+ 				return;
+ 			}

> CHANGE  5 : 6  @  5 : 6

~ 			EaglercraftGPU.glNormal3f(0.0F, 1.0F, 0.0F);

> EOF
