
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 6  @  1

+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;

> DELETE  1  @  1 : 3

> DELETE  1  @  1 : 12

> CHANGE  58 : 60  @  58 : 59

~ 			tileentityspecialrenderer = this
~ 					.getSpecialRendererByClass((Class<? extends TileEntity>) teClass.getSuperclass());

> EOF
