
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 6  @  4 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  30 : 32  @  30 : 34

~ 	public static PropertyEnum<BlockStairs.EnumHalf> HALF;
~ 	public static PropertyEnum<BlockStairs.EnumShape> SHAPE;

> INSERT  52 : 57  @  54

+ 	public static void bootstrapStates() {
+ 		HALF = PropertyEnum.<BlockStairs.EnumHalf>create("half", BlockStairs.EnumHalf.class);
+ 		SHAPE = PropertyEnum.<BlockStairs.EnumShape>create("shape", BlockStairs.EnumShape.class);
+ 	}
+ 

> CHANGE  401 : 402  @  398 : 399

~ 	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> CHANGE  462 : 463  @  459 : 460

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> EOF
