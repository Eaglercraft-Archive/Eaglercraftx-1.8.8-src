
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> DELETE  1  @  1 : 4

> INSERT  35 : 47  @  35

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
