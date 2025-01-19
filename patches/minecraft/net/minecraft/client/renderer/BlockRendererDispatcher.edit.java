
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  2  @  2 : 9

> DELETE  12  @  12 : 13

> CHANGE  17 : 18  @  17 : 18

~ 	public void renderBlockDamage(IBlockState state, BlockPos pos, EaglerTextureAtlasSprite texture,

> INSERT  15 : 16  @  15

+ 			boolean res;

> CHANGE  2 : 3  @  2 : 3

~ 				res = false;

> CHANGE  3 : 5  @  3 : 4

~ 					res = this.fluidRenderer.renderFluid(blockAccess, state, pos, worldRendererIn);
~ 					break;

> CHANGE  1 : 3  @  1 : 2

~ 					res = false;
~ 					break;

> CHANGE  2 : 4  @  2 : 3

~ 					res = this.blockModelRenderer.renderModel(blockAccess, ibakedmodel, state, pos, worldRendererIn);
~ 					break;

> CHANGE  1 : 3  @  1 : 2

~ 					res = false;
~ 					break;

> INSERT  2 : 3  @  2

+ 			return res;

> CHANGE  24 : 28  @  24 : 30

~ 
~ 		try {
~ 			state = block.getActualState(state, worldIn, pos);
~ 		} catch (Exception eeeee) {

> EOF
