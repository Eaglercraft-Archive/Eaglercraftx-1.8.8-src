
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  26 : 29  @  26 : 28

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom var4) {
~ 		BlockPos tmp = new BlockPos(0, 0, 0);
~ 		if (world.getBlockState(blockpos.offsetEvenFaster(EnumFacing.DOWN, tmp)).getBlock() == Blocks.reeds

> CHANGE  1 : 2  @  1 : 2

~ 			if (world.isAirBlock(blockpos.offsetEvenFaster(EnumFacing.UP, tmp))) {

> CHANGE  1 : 3  @  1 : 2

~ 				--tmp.y;
~ 				for (i = 1; world.getBlockState(tmp.offsetEvenFaster(EnumFacing.DOWN, tmp)).getBlock() == this; ++i) {

> CHANGE  6 : 7  @  6 : 7

~ 						world.setBlockState(blockpos.offsetEvenFaster(EnumFacing.UP, tmp), this.getDefaultState());

> CHANGE  11 : 13  @  11 : 12

~ 		BlockPos down = blockpos.down();
~ 		Block block = world.getBlockState(down).getBlock();

> CHANGE  5 : 11  @  5 : 8

~ 			EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 			for (int i = 0; i < facings.length; ++i) {
~ 				EnumFacing enumfacing = facings[i];
~ 				down = blockpos.offsetEvenFaster(enumfacing, down);
~ 				--down.y;
~ 				if (world.getBlockState(down).getBlock().getMaterial() == Material.water) {

> CHANGE  30 : 31  @  30 : 31

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> EOF
