
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
+ 

> CHANGE  1 : 2  @  1 : 4

~ 

> CHANGE  55 : 59  @  55 : 57

~ 		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 		BlockPos tmp = new BlockPos(0, 0, 0);
~ 		for (int i = 0; i < facings.length; ++i) {
~ 			if (world.getBlockState(blockpos.offsetEvenFaster(facings[i], tmp)).getBlock().isNormalCube()) {

> CHANGE  110 : 111  @  110 : 111

~ 	public void randomTick(World var1, BlockPos var2, IBlockState var3, EaglercraftRandom var4) {

> CHANGE  2 : 3  @  2 : 3

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom var4) {

> EOF
