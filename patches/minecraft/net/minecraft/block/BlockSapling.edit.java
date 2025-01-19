
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 10

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  23 : 24  @  23 : 25

~ 	public static PropertyEnum<BlockPlanks.EnumType> TYPE;

> INSERT  10 : 14  @  10

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockPlanks.EnumType>create("type", BlockPlanks.EnumType.class);
+ 	}
+ 

> CHANGE  5 : 6  @  5 : 6

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> DELETE  5  @  5 : 6

> CHANGE  3 : 4  @  3 : 4

~ 	public void grow(World worldIn, BlockPos pos, IBlockState state, EaglercraftRandom rand) {

> CHANGE  8 : 9  @  8 : 9

~ 	public void generateTree(World worldIn, BlockPos pos, IBlockState state, EaglercraftRandom rand) {

> CHANGE  109 : 112  @  109 : 111

~ 		BlockPlanks.EnumType[] types = BlockPlanks.EnumType.META_LOOKUP;
~ 		for (int i = 0; i < types.length; ++i) {
~ 			list.add(new ItemStack(item, 1, types[i].getMetadata()));

> CHANGE  8 : 9  @  8 : 9

~ 	public boolean canUseBonemeal(World world, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> CHANGE  3 : 4  @  3 : 4

~ 	public void grow(World world, EaglercraftRandom random, BlockPos blockpos, IBlockState iblockstate) {

> EOF
