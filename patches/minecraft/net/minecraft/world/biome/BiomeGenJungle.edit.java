
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> DELETE  6  @  7 : 8

> DELETE  10  @  12 : 23

> CHANGE  13 : 16  @  26 : 34

~ 	private final IBlockState field_181620_aE;
~ 	private final IBlockState field_181621_aF;
~ 	private final IBlockState field_181622_aG;

> INSERT  19 : 25  @  37

+ 		field_181620_aE = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
+ 		field_181621_aF = Blocks.leaves.getDefaultState()
+ 				.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE)
+ 				.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
+ 		field_181622_aG = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK)
+ 				.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

> DELETE  26  @  38 : 46

> DELETE  33  @  53 : 83

> EOF
