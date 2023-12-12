
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  36 : 37  @  36 : 38

~ 	public static PropertyEnum<BlockPlanks.EnumType> TYPE;

> INSERT  10 : 14  @  10

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockPlanks.EnumType>create("type", BlockPlanks.EnumType.class);
+ 	}
+ 

> CHANGE  5 : 7  @  5 : 7

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {
~ 		{

> DELETE  4  @  4 : 5

> CHANGE  3 : 4  @  3 : 4

~ 	public void grow(World worldIn, BlockPos pos, IBlockState state, EaglercraftRandom rand) {

> CHANGE  8 : 9  @  8 : 9

~ 	public void generateTree(World worldIn, BlockPos pos, IBlockState state, EaglercraftRandom rand) {

> CHANGE  119 : 120  @  119 : 120

~ 	public boolean canUseBonemeal(World world, EaglercraftRandom var2, BlockPos var3, IBlockState var4) {

> CHANGE  3 : 4  @  3 : 4

~ 	public void grow(World world, EaglercraftRandom random, BlockPos blockpos, IBlockState iblockstate) {

> EOF
