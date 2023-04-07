
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> DELETE  1  @  1 : 3

> INSERT  13 : 26  @  13

+ 
+ 	public void doRender(EntityBlaze entityliving, double d0, double d1, double d2, float f, float f1) {
+ 		if (DeferredStateManager.isInDeferredPass()) {
+ 			DeferredStateManager.setEmissionConstant(1.0f);
+ 			try {
+ 				super.doRender(entityliving, d0, d1, d2, f, f1);
+ 			} finally {
+ 				DeferredStateManager.setEmissionConstant(0.0f);
+ 			}
+ 		} else {
+ 			super.doRender(entityliving, d0, d1, d2, f, f1);
+ 		}
+ 	}

> EOF
