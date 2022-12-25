
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  3 : 6  @  4

+ 
+ import com.google.common.collect.Sets;
+ 

> DELETE  9  @  7 : 10

> CHANGE  11 : 12  @  12 : 15

~ 	private static Set<Block> EFFECTIVE_ON;

> INSERT  13 : 18  @  16

+ 	public static void bootstrap() {
+ 		EFFECTIVE_ON = Sets.newHashSet(new Block[] { Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2,
+ 				Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder });
+ 	}
+ 

> EOF
