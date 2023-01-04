
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 10

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  6  @  6 : 7

> DELETE  5  @  5 : 14

> CHANGE  2 : 3  @  2 : 4

~ 	public static PropertyEnum<BlockPlanks.EnumType> TYPE;

> INSERT  10 : 14  @  10

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockPlanks.EnumType>create("type", BlockPlanks.EnumType.class);
+ 	}
+ 

> CHANGE  5 : 6  @  5 : 13

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> DELETE  2  @  2 : 103

> CHANGE  28 : 29  @  28 : 29

~ 	public boolean canUseBonemeal(World world, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> DELETE  3  @  3 : 7

> EOF
