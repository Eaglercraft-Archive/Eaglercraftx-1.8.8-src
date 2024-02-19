
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 9

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  22 : 24  @  22 : 26

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

> CHANGE  114 : 115  @  114 : 115

~ 	public boolean canUseBonemeal(World var1, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> CHANGE  3 : 4  @  3 : 4

~ 	public void grow(World world, EaglercraftRandom var2, BlockPos blockpos, IBlockState var4) {

> EOF
