
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  31 : 32  @  31 : 33

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

> CHANGE  5 : 6  @  5 : 6

~ 		if (entityplayer.getCurrentEquippedItem() != null

> CHANGE  26 : 27  @  26 : 27

~ 	public boolean canUseBonemeal(World var1, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> CHANGE  3 : 4  @  3 : 4

~ 	public void grow(World world, EaglercraftRandom var2, BlockPos blockpos, IBlockState iblockstate) {

> EOF
