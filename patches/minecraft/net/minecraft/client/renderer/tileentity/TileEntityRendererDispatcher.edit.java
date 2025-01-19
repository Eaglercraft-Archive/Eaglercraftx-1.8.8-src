
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 7  @  1

+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.dynamiclights.DynamicLightsStateManager;

> DELETE  1  @  1 : 3

> DELETE  1  @  1 : 12

> CHANGE  58 : 60  @  58 : 59

~ 			tileentityspecialrenderer = this
~ 					.getSpecialRendererByClass((Class<? extends TileEntity>) teClass.getSuperclass());

> INSERT  52 : 55  @  52

+ 				if (DynamicLightsStateManager.isInDynamicLightsPass()) {
+ 					DynamicLightsStateManager.reportForwardRenderObjectPosition2((float) x, (float) y, (float) z);
+ 				}

> EOF
