
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  2  @  2 : 9

> DELETE  12  @  12 : 13

> CHANGE  17 : 18  @  17 : 18

~ 	public void renderBlockDamage(IBlockState state, BlockPos pos, EaglerTextureAtlasSprite texture,

> CHANGE  55 : 59  @  55 : 61

~ 
~ 		try {
~ 			state = block.getActualState(state, worldIn, pos);
~ 		} catch (Exception eeeee) {

> EOF
