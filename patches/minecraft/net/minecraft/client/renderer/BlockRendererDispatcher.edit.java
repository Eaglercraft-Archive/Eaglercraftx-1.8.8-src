
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  6  @  4 : 11

> DELETE  18  @  23 : 24

> CHANGE  35 : 36  @  41 : 42

~ 	public void renderBlockDamage(IBlockState state, BlockPos pos, EaglerTextureAtlasSprite texture,

> CHANGE  91 : 95  @  97 : 103

~ 
~ 		try {
~ 			state = block.getActualState(state, worldIn, pos);
~ 		} catch (Exception eeeee) {

> EOF
