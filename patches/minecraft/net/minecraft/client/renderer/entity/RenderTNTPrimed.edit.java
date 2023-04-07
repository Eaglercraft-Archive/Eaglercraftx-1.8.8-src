
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> DELETE  2  @  2 : 5

> INSERT  28 : 37  @  28

+ 		boolean light = entitytntprimed.fuse / 5 % 2 == 0;
+ 		boolean deferred = DeferredStateManager.isInDeferredPass();
+ 		if (light && deferred) {
+ 			DeferredStateManager.setEmissionConstant(1.0f);
+ 			DeferredStateManager.disableMaterialTexture();
+ 			GlStateManager.enableShaderBlendAdd();
+ 			GlStateManager.setShaderBlendSrc(0.0f, 0.0f, 0.0f, 0.0f);
+ 			GlStateManager.setShaderBlendAdd(1.0f, 1.0f, 1.0f, 1.0f);
+ 		}

> CHANGE  2 : 3  @  2 : 3

~ 		if (light && !deferred) {

> INSERT  18 : 24  @  18

+ 		if (light && deferred) {
+ 			DeferredStateManager.setEmissionConstant(0.0f);
+ 			DeferredStateManager.enableMaterialTexture();
+ 			GlStateManager.disableShaderBlendAdd();
+ 			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 		}

> EOF
