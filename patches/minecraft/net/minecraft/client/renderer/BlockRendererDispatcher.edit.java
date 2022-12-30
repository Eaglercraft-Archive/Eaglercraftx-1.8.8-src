
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  4  @  2 : 9

> DELETE  12  @  19 : 20

> CHANGE  17 : 18  @  18 : 19

~ 	public void renderBlockDamage(IBlockState state, BlockPos pos, EaglerTextureAtlasSprite texture,

> CHANGE  56 : 60  @  56 : 62

~ 
~ 		try {
~ 			state = block.getActualState(state, worldIn, pos);
~ 		} catch (Exception eeeee) {

> EOF
