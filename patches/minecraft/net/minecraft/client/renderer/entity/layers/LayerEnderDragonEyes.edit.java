
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> DELETE  1  @  1 : 2

> INSERT  14 : 25  @  14

+ 		if (DeferredStateManager.isInDeferredPass()) {
+ 			DeferredStateManager.setEmissionConstant(0.5f);
+ 			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
+ 			GlStateManager.enablePolygonOffset();
+ 			GlStateManager.doPolygonOffset(-0.025f, 1.0f);
+ 			this.dragonRenderer.getMainModel().render(entitydragon, f, f1, f3, f4, f5, f6);
+ 			this.dragonRenderer.func_177105_a(entitydragon, f2);
+ 			GlStateManager.disablePolygonOffset();
+ 			DeferredStateManager.setEmissionConstant(0.0f);
+ 			return;
+ 		}

> EOF
