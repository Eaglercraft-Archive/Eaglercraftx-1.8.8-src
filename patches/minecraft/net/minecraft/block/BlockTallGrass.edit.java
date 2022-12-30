
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 8

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  8  @  11 : 12

> DELETE  4  @  5 : 7

> CHANGE  7 : 8  @  9 : 11

~ 	public static PropertyEnum<BlockTallGrass.EnumType> TYPE;

> INSERT  9 : 13  @  10

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockTallGrass.EnumType>create("type", BlockTallGrass.EnumType.class);
+ 	}
+ 

> CHANGE  30 : 31  @  26 : 27

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom random, int var3) {

> CHANGE  4 : 5  @  4 : 5

~ 	public int quantityDroppedWithBonus(int i, EaglercraftRandom random) {

> DELETE  4  @  4 : 17

> CHANGE  16 : 17  @  29 : 30

~ 	public boolean canUseBonemeal(World var1, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> CHANGE  4 : 5  @  4 : 5

~ 	public void grow(World world, EaglercraftRandom var2, BlockPos blockpos, IBlockState iblockstate) {

> EOF
