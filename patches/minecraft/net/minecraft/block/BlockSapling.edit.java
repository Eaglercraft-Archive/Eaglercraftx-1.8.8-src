
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 10

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  8  @  13 : 14

> DELETE  5  @  6 : 15

> CHANGE  2 : 3  @  11 : 13

~ 	public static PropertyEnum<BlockPlanks.EnumType> TYPE;

> INSERT  11 : 15  @  12

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockPlanks.EnumType>create("type", BlockPlanks.EnumType.class);
+ 	}
+ 

> CHANGE  9 : 10  @  5 : 13

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> DELETE  3  @  10 : 111

> CHANGE  28 : 29  @  129 : 130

~ 	public boolean canUseBonemeal(World world, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> DELETE  4  @  4 : 8

> EOF
