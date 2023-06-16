
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 6  @  4 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  24 : 26  @  24 : 28

~ 	public static PropertyEnum<BlockStairs.EnumHalf> HALF;
~ 	public static PropertyEnum<BlockStairs.EnumShape> SHAPE;

> INSERT  20 : 25  @  20

+ 	public static void bootstrapStates() {
+ 		HALF = PropertyEnum.<BlockStairs.EnumHalf>create("half", BlockStairs.EnumHalf.class);
+ 		SHAPE = PropertyEnum.<BlockStairs.EnumShape>create("shape", BlockStairs.EnumShape.class);
+ 	}
+ 

> CHANGE  344 : 345  @  344 : 345

~ 	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> CHANGE  60 : 61  @  60 : 61

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> EOF
