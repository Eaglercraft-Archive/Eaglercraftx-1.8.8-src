
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 5  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> DELETE  1  @  1 : 4

> DELETE  2  @  2 : 3

> DELETE  2  @  2 : 3

> DELETE  6  @  6 : 10

> CHANGE  30 : 32  @  30 : 31

~ 			int l = DeferredStateManager.isInDeferredPass() ? ((var2.getBrightnessForRender(f) >> 16) & 0xFF) : 0;
~ 			worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

> CHANGE  4 : 5  @  4 : 5

~ 					.lightmap(l, 240).normal(0.0F, 1.0F, 0.0F).endVertex();

> CHANGE  4 : 5  @  4 : 5

~ 					.lightmap(l, 240).normal(0.0F, 1.0F, 0.0F).endVertex();

> CHANGE  4 : 5  @  4 : 5

~ 					.lightmap(l, 240).normal(0.0F, 1.0F, 0.0F).endVertex();

> CHANGE  4 : 5  @  4 : 5

~ 					.lightmap(l, 240).normal(0.0F, 1.0F, 0.0F).endVertex();

> EOF
