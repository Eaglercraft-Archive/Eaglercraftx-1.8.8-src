
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  3 : 8  @  4

+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;

> DELETE  9  @  5 : 7

> DELETE  10  @  8 : 19

> CHANGE  68 : 70  @  77 : 78

~ 			tileentityspecialrenderer = this
~ 					.getSpecialRendererByClass((Class<? extends TileEntity>) teClass.getSuperclass());

> EOF
