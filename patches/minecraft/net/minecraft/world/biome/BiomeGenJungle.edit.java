
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> DELETE  4  @  5 : 6

> DELETE  4  @  5 : 16

> CHANGE  3 : 6  @  14 : 22

~ 	private final IBlockState field_181620_aE;
~ 	private final IBlockState field_181621_aF;
~ 	private final IBlockState field_181622_aG;

> INSERT  6 : 12  @  11

+ 		field_181620_aE = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
+ 		field_181621_aF = Blocks.leaves.getDefaultState()
+ 				.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE)
+ 				.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
+ 		field_181622_aG = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK)
+ 				.withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

> DELETE  7  @  1 : 9

> DELETE  7  @  15 : 45

> EOF
