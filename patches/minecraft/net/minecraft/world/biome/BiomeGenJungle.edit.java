
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> DELETE  4  @  4 : 5

> DELETE  4  @  4 : 15

> CHANGE  3 : 6  @  3 : 11

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

> DELETE  1  @  1 : 9

> DELETE  7  @  7 : 37

> EOF
