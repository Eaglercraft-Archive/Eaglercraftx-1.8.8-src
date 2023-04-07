
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 6  @  2 : 5

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> DELETE  1  @  1 : 2

> CHANGE  7 : 13  @  7 : 11

~ 		if (!DeferredStateManager.isInDeferredPass()) {
~ 			GlStateManager.disableLighting();
~ 			GlStateManager.disableMCLight(0);
~ 			GlStateManager.disableMCLight(1);
~ 			GlStateManager.disableColorMaterial();
~ 		}

> CHANGE  3 : 10  @  3 : 21

~ 		if (!DeferredStateManager.isInDeferredPass()) {
~ 			GlStateManager.enableLighting();
~ 			GlStateManager.enableMCLight(0, 0.6f, LIGHT0_POS.xCoord, LIGHT0_POS.yCoord, LIGHT0_POS.zCoord, 0.0D);
~ 			GlStateManager.enableMCLight(1, 0.6f, LIGHT1_POS.xCoord, LIGHT1_POS.yCoord, LIGHT1_POS.zCoord, 0.0D);
~ 			GlStateManager.setMCLightAmbient(0.4f, 0.4f, 0.4f);
~ 			GlStateManager.enableColorMaterial();
~ 		}

> CHANGE  15 : 22  @  15 : 20

~ 		if (!DeferredStateManager.isInDeferredPass()) {
~ 			GlStateManager.pushMatrix();
~ 			GlStateManager.rotate(-30.0F, 0.0F, 1.0F, 0.0F);
~ 			GlStateManager.rotate(165.0F, 1.0F, 0.0F, 0.0F);
~ 			enableStandardItemLighting();
~ 			GlStateManager.popMatrix();
~ 		}

> EOF
