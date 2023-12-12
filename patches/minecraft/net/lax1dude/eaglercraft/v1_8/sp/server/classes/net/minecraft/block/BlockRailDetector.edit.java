
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 5  @  4 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  26 : 27  @  26 : 35

~ 	public static PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE;

> INSERT  9 : 21  @  9

+ 	public static void bootstrapStates() {
+ 		SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class,
+ 				new Predicate<BlockRailBase.EnumRailDirection>() {
+ 					public boolean apply(BlockRailBase.EnumRailDirection blockrailbase$enumraildirection) {
+ 						return blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.NORTH_EAST
+ 								&& blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.NORTH_WEST
+ 								&& blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.SOUTH_EAST
+ 								&& blockrailbase$enumraildirection != BlockRailBase.EnumRailDirection.SOUTH_WEST;
+ 					}
+ 				});
+ 	}
+ 

> CHANGE  9 : 10  @  9 : 10

~ 		{

> CHANGE  6 : 7  @  6 : 7

~ 	public void randomTick(World var1, BlockPos var2, IBlockState var3, EaglercraftRandom var4) {

> CHANGE  2 : 4  @  2 : 4

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom var4) {
~ 		if (((Boolean) iblockstate.getValue(POWERED)).booleanValue()) {

> EOF
