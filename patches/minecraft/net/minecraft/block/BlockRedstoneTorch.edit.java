
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  2 : 7  @  2 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Maps;
~ 

> CHANGE  51 : 55  @  51 : 53

~ 			EnumFacing[] facings = EnumFacing._VALUES;
~ 			BlockPos tmp = new BlockPos(0, 0, 0);
~ 			for (int i = 0; i < facings.length; ++i) {
~ 				world.notifyNeighborsOfStateChange(blockpos.offsetEvenFaster(facings[i], tmp), this);

> CHANGE  7 : 11  @  7 : 9

~ 			EnumFacing[] facings = EnumFacing._VALUES;
~ 			BlockPos tmp = new BlockPos(0, 0, 0);
~ 			for (int i = 0; i < facings.length; ++i) {
~ 				world.notifyNeighborsOfStateChange(blockpos.offsetEvenFaster(facings[i], tmp), this);

> CHANGE  14 : 15  @  14 : 15

~ 	public void randomTick(World var1, BlockPos var2, IBlockState var3, EaglercraftRandom var4) {

> CHANGE  2 : 3  @  2 : 3

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> CHANGE  1 : 2  @  1 : 2

~ 		List<BlockRedstoneTorch.Toggle> list = toggles.get(world);

> CHANGE  1 : 9  @  1 : 4

~ 		if (list != null) {
~ 			int index = 0;
~ 			while (index < list.size() && world.getTotalWorldTime() - list.get(index).time > 60L) {
~ 				index++;
~ 			}
~ 			if (index > 0) {
~ 				list.subList(0, index).clear();
~ 			}

> CHANGE  42 : 43  @  42 : 43

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> CHANGE  7 : 8  @  7 : 8

~ 	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> EOF
