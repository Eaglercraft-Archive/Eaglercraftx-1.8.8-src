
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  4 : 7  @  4

+ 
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  10  @  7 : 11

> INSERT  85 : 86  @  86

+ 		BlockPos.MutableBlockPos pointer = new BlockPos.MutableBlockPos();

> CHANGE  89 : 90  @  89 : 90

~ 				BlockPos blockpos = blockPosIn.offsetEvenFaster(enumfacing, pointer);

> EOF
