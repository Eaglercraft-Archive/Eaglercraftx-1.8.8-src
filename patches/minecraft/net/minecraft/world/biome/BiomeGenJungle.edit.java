
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> DELETE  11  @  11 : 12

> CHANGE  11 : 14  @  11 : 19

~ 	private final IBlockState field_181620_aE;
~ 	private final IBlockState field_181621_aF;
~ 	private final IBlockState field_181622_aG;

> INSERT  3 : 9  @  3

+ 		field_181620_aE = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
+ 		field_181621_aF = Blocks.leaves.getDefaultState()
+ 				.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE)
+ 				.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
+ 		field_181622_aG = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK)
+ 				.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

> CHANGE  16 : 17  @  16 : 17

~ 	public WorldGenAbstractTree genBigTreeChance(EaglercraftRandom random) {

> CHANGE  8 : 9  @  8 : 9

~ 	public WorldGenerator getRandomWorldGenForGrass(EaglercraftRandom random) {

> CHANGE  4 : 5  @  4 : 5

~ 	public void decorate(World world, EaglercraftRandom random, BlockPos blockpos) {

> EOF
