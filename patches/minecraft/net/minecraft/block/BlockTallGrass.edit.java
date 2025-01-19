
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 8

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  20 : 21  @  20 : 22

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

> CHANGE  32 : 33  @  32 : 33

~ 	public boolean canUseBonemeal(World var1, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> CHANGE  3 : 4  @  3 : 4

~ 	public void grow(World world, EaglercraftRandom var2, BlockPos blockpos, IBlockState iblockstate) {

> CHANGE  30 : 31  @  30 : 31

~ 		private static final BlockTallGrass.EnumType[] META_LOOKUP = new BlockTallGrass.EnumType[3];

> CHANGE  29 : 32  @  29 : 31

~ 			BlockTallGrass.EnumType[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				META_LOOKUP[types[i].getMeta()] = types[i];

> EOF
