
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
+ 

> CHANGE  1 : 2  @  1 : 6

~ 

> CHANGE  39 : 44  @  39 : 41

~ 		BlockPos tmp = new BlockPos(0, 0, 0);
~ 		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 		for (int i = 0; i < facings.length; ++i) {
~ 			EnumFacing enumfacing = facings[i];
~ 			if (iblockaccess.getBlockState(blockpos.offsetEvenFaster(enumfacing, tmp)).getBlock() == this.crop) {

> CHANGE  12 : 13  @  12 : 13

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> CHANGE  9 : 12  @  9 : 11

~ 					EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 					for (int j = 0; j < facings.length; ++j) {
~ 						if (world.getBlockState(blockpos.offset(facings[j])).getBlock() == this.crop) {

> CHANGE  71 : 72  @  71 : 72

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> CHANGE  12 : 13  @  12 : 13

~ 	public boolean canUseBonemeal(World var1, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> CHANGE  3 : 4  @  3 : 4

~ 	public void grow(World world, EaglercraftRandom var2, BlockPos blockpos, IBlockState iblockstate) {

> EOF
