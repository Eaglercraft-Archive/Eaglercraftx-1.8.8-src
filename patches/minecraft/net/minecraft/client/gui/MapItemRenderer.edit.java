
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
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 2

> INSERT  65 : 66  @  65

+ 				int c;

> CHANGE  1 : 2  @  1 : 2

~ 					c = (i + i / 128 & 1) * 8 + 16 << 24;

> CHANGE  1 : 2  @  1 : 2

~ 					c = MapColor.mapColorArray[j / 4].func_151643_b(j & 3);

> INSERT  1 : 2  @  1

+ 				this.mapTextureData[i] = (c & 0xFF00FF00) | ((c & 0x00FF0000) >> 16) | ((c & 0x000000FF) << 16);

> EOF
