
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  19 : 20  @  19 : 25

~ 	private static Set<Block> EFFECTIVE_ON = null;

> INSERT  1 : 6  @  1

+ 	public static void doBootstrap() {
+ 		EFFECTIVE_ON = Sets.newHashSet(new Block[] { Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2,
+ 				Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder });
+ 	}
+ 

> EOF
