
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 9

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  14  @  18 : 20

> CHANGE  8 : 10  @  10 : 14

~ 	public static PropertyEnum<BlockDoublePlant.EnumPlantType> VARIANT;
~ 	public static PropertyEnum<BlockDoublePlant.EnumBlockHalf> HALF;

> INSERT  15 : 20  @  17

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockDoublePlant.EnumPlantType>create("variant", BlockDoublePlant.EnumPlantType.class);
+ 		HALF = PropertyEnum.<BlockDoublePlant.EnumBlockHalf>create("half", BlockDoublePlant.EnumBlockHalf.class);
+ 	}
+ 

> CHANGE  65 : 66  @  60 : 61

~ 	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom random, int var3) {

> DELETE  40  @  40 : 50

> DELETE  10  @  20 : 28

> DELETE  14  @  22 : 38

> CHANGE  17 : 18  @  33 : 34

~ 	public boolean canUseBonemeal(World var1, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> CHANGE  4 : 5  @  4 : 5

~ 	public void grow(World world, EaglercraftRandom var2, BlockPos blockpos, IBlockState var4) {

> EOF
