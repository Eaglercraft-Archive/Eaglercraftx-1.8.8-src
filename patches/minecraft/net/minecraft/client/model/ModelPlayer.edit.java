
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 5

~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> CHANGE  72 : 73  @  72 : 73

~ 			if (entity != null && entity.isSneaking()) {

> INSERT  21 : 25  @  21

+ 		GlStateManager.matrixMode(5890);
+ 		GlStateManager.pushMatrix();
+ 		GlStateManager.scale(2.0f, 1.0f, 1.0f);
+ 		GlStateManager.matrixMode(5888);

> INSERT  1 : 4  @  1

+ 		GlStateManager.matrixMode(5890);
+ 		GlStateManager.popMatrix();
+ 		GlStateManager.matrixMode(5888);

> CHANGE  9 : 10  @  9 : 10

~ 		if (entity != null && entity.isSneaking()) {

> EOF
