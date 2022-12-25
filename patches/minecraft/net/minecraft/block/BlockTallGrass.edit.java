
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 8

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  11  @  14 : 15

> DELETE  15  @  19 : 21

> CHANGE  22 : 23  @  28 : 30

~ 	public static PropertyEnum<BlockTallGrass.EnumType> TYPE;

> INSERT  31 : 35  @  38

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockTallGrass.EnumType>create("type", BlockTallGrass.EnumType.class);
+ 	}
+ 

> CHANGE  61 : 62  @  64 : 65

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom random, int var3) {

> CHANGE  65 : 66  @  68 : 69

~ 	public int quantityDroppedWithBonus(int i, EaglercraftRandom random) {

> DELETE  69  @  72 : 85

> CHANGE  85 : 86  @  101 : 102

~ 	public boolean canUseBonemeal(World var1, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> CHANGE  89 : 90  @  105 : 106

~ 	public void grow(World world, EaglercraftRandom var2, BlockPos blockpos, IBlockState iblockstate) {

> EOF
