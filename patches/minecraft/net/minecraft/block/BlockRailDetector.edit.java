
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 5  @  1 : 3

~ 
~ import com.google.common.base.Predicate;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  18 : 20  @  18 : 27

~ 
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

> CHANGE  16 : 17  @  16 : 17

~ 	public void randomTick(World var1, BlockPos var2, IBlockState var3, EaglercraftRandom var4) {

> CHANGE  2 : 3  @  2 : 3

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom var4) {

> EOF
