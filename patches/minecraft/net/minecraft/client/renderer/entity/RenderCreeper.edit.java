
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 5  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DynamicLightManager;

> DELETE  1  @  1 : 4

> INSERT  1 : 2  @  1

+ import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

> INSERT  34 : 46  @  34

+ 	public void doRender(EntityCreeper entitycreeper, double d0, double d1, double d2, float f, float f1) {
+ 		float ff = entitycreeper.getCreeperFlashIntensity(f);
+ 		if ((int) (ff * 10.0F) % 2 != 0) {
+ 			DeferredStateManager.setEmissionConstant(1.0f);
+ 		}
+ 		try {
+ 			super.doRender(entitycreeper, d0, d1, d2, f, f1);
+ 		} finally {
+ 			DeferredStateManager.setEmissionConstant(0.0f);
+ 		}
+ 	}
+ 

> EOF
