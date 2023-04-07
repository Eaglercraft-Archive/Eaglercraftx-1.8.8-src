
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 5  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 5

> INSERT  43 : 67  @  43

+ 		if (DeferredStateManager.isDeferredRenderer()) {
+ 			if (entitydragon.deathTicks > 0) {
+ 				float f6 = (float) entitydragon.deathTicks / 200.0F;
+ 				GlStateManager.depthFunc(515);
+ 				GlStateManager.enableAlpha();
+ 				GlStateManager.alphaFunc(516, f6);
+ 				this.bindTexture(enderDragonExplodingTextures);
+ 				this.mainModel.render(entitydragon, f, f1, f2, f3, f4, f5);
+ 				GlStateManager.alphaFunc(516, 0.1F);
+ 				GlStateManager.depthFunc(514);
+ 			}
+ 			if (entitydragon.hurtTime > 0) {
+ 				GlStateManager.enableShaderBlendAdd();
+ 				GlStateManager.setShaderBlendSrc(0.5f, 0.5f, 0.5f, 1.0f);
+ 				GlStateManager.setShaderBlendAdd(1.0f, 0.0f, 0.0f, 0.0f);
+ 			}
+ 			this.bindEntityTexture(entitydragon);
+ 			this.mainModel.render(entitydragon, f, f1, f2, f3, f4, f5);
+ 			GlStateManager.depthFunc(515);
+ 			if (entitydragon.hurtTime > 0) {
+ 				GlStateManager.disableShaderBlendAdd();
+ 			}
+ 			return;
+ 		}

> EOF
