
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.minecraft.block.Block;

> DELETE  6  @  6 : 10

> CHANGE  47 : 51  @  47 : 49

~ 		IBlockState state = getBlockState(blockpos);
~ 		Block b = state.getBlock();
~ 		int j = b.alfheim$getLightFor(state, this, EnumSkyBlock.SKY, blockpos);
~ 		int k = b.alfheim$getLightFor(state, this, EnumSkyBlock.BLOCK, blockpos);

> CHANGE  26 : 28  @  26 : 53

~ 	public int getBiomeColorForCoords(BlockPos var1, int index) {
~ 		return this.worldObj.getBiomeColorForCoords(var1, index);

> EOF
