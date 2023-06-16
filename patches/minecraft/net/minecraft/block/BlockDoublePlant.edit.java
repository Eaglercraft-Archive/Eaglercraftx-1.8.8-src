
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 9

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  12  @  12 : 14

> CHANGE  8 : 10  @  8 : 12

~ 	public static PropertyEnum<BlockDoublePlant.EnumPlantType> VARIANT;
~ 	public static PropertyEnum<BlockDoublePlant.EnumBlockHalf> HALF;

> INSERT  13 : 18  @  13

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockDoublePlant.EnumPlantType>create("variant", BlockDoublePlant.EnumPlantType.class);
+ 		HALF = PropertyEnum.<BlockDoublePlant.EnumBlockHalf>create("half", BlockDoublePlant.EnumBlockHalf.class);
+ 	}
+ 

> CHANGE  60 : 61  @  60 : 61

~ 	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom random, int var3) {

> DELETE  39  @  39 : 49

> DELETE  10  @  10 : 18

> DELETE  14  @  14 : 30

> CHANGE  17 : 18  @  17 : 18

~ 	public boolean canUseBonemeal(World var1, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> CHANGE  3 : 4  @  3 : 4

~ 	public void grow(World world, EaglercraftRandom var2, BlockPos blockpos, IBlockState var4) {

> EOF
