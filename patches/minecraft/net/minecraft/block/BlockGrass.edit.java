
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 8

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  42 : 43  @  42 : 43

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom random) {

> CHANGE  1 : 4  @  1 : 3

~ 			BlockPos tmp = new BlockPos();
~ 			if (world.getLightFromNeighbors(blockpos.up(tmp)) < 4
~ 					&& world.getBlockState(blockpos.up(tmp)).getBlock().getLightOpacity() > 2) {

> CHANGE  2 : 4  @  2 : 3

~ 				if (world.getLightFromNeighbors(blockpos.up(tmp)) >= 9) {
~ 					BlockPos tmp2 = new BlockPos();

> CHANGE  2 : 4  @  2 : 4

~ 								random.nextInt(3) - 1, tmp2);
~ 						Block block = world.getBlockState(blockpos1.up(tmp)).getBlock();

> CHANGE  3 : 5  @  3 : 4

~ 								&& world.getLightFromNeighbors(blockpos1.up(tmp)) >= 4
~ 								&& block.getLightOpacity() <= 2) {

> DELETE  4  @  4 : 5

> CHANGE  4 : 5  @  4 : 5

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom random, int i) {

> CHANGE  8 : 9  @  8 : 9

~ 	public boolean canUseBonemeal(World var1, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> CHANGE  3 : 4  @  3 : 4

~ 	public void grow(World world, EaglercraftRandom random, BlockPos blockpos, IBlockState var4) {

> EOF
