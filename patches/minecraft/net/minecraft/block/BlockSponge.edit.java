
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  3 : 7  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.google.common.collect.Lists;
~ 

> CHANGE  52 : 53  @  52 : 53

~ 		ArrayList<BlockPos> arraylist = Lists.newArrayList();

> CHANGE  8 : 11  @  8 : 9

~ 			EnumFacing[] facings = EnumFacing._VALUES;
~ 			for (int k = 0; k < facings.length; ++k) {
~ 				EnumFacing enumfacing = facings[k];

> CHANGE  16 : 18  @  16 : 18

~ 		for (int j = 0, l = arraylist.size(); j < l; ++j) {
~ 			worldIn.notifyNeighborsOfStateChange(arraylist.get(j), Blocks.air);

> CHANGE  22 : 23  @  22 : 23

~ 	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> EOF
