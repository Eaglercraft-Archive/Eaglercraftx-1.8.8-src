
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 8

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  6  @  6 : 7

> DELETE  4  @  4 : 6

> CHANGE  7 : 8  @  7 : 9

~ 	public static PropertyEnum<BlockTallGrass.EnumType> TYPE;

> INSERT  8 : 12  @  8

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockTallGrass.EnumType>create("type", BlockTallGrass.EnumType.class);
+ 	}
+ 

> CHANGE  26 : 27  @  26 : 27

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom random, int var3) {

> CHANGE  3 : 4  @  3 : 4

~ 	public int quantityDroppedWithBonus(int i, EaglercraftRandom random) {

> DELETE  3  @  3 : 16

> CHANGE  16 : 17  @  16 : 17

~ 	public boolean canUseBonemeal(World var1, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> CHANGE  3 : 4  @  3 : 4

~ 	public void grow(World world, EaglercraftRandom var2, BlockPos blockpos, IBlockState iblockstate) {

> EOF
