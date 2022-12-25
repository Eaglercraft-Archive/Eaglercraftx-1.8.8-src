
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
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  9  @  5 : 6

> DELETE  10  @  7 : 8

> INSERT  75 : 76  @  73

+ 				int c;

> CHANGE  77 : 78  @  74 : 75

~ 					c = (i + i / 128 & 1) * 8 + 16 << 24;

> CHANGE  79 : 80  @  76 : 77

~ 					c = MapColor.mapColorArray[j / 4].func_151643_b(j & 3);

> INSERT  81 : 82  @  78

+ 				this.mapTextureData[i] = (c & 0xFF00FF00) | ((c & 0x00FF0000) >> 16) | ((c & 0x000000FF) << 16);

> EOF
