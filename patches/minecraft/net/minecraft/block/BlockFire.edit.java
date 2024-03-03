
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 5  @  1 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.google.common.collect.Maps;
~ 

> CHANGE  118 : 119  @  118 : 119

~ 	public int quantityDropped(EaglercraftRandom var1) {

> CHANGE  7 : 8  @  7 : 8

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> CHANGE  106 : 107  @  106 : 107

~ 	private void catchOnFire(World worldIn, BlockPos pos, int chance, EaglercraftRandom random, int age) {

> CHANGE  23 : 26  @  23 : 24

~ 		EnumFacing[] facings = EnumFacing._VALUES;
~ 		for (int i = 0; i < facings.length; ++i) {
~ 			EnumFacing enumfacing = facings[i];

> CHANGE  14 : 17  @  14 : 16

~ 			EnumFacing[] facings = EnumFacing._VALUES;
~ 			for (int j = 0; j < facings.length; ++j) {
~ 				i = Math.max(this.getEncouragement(worldIn.getBlockState(pos.offset(facings[j])).getBlock()), i);

> CHANGE  37 : 38  @  37 : 38

~ 	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom random) {

> EOF
