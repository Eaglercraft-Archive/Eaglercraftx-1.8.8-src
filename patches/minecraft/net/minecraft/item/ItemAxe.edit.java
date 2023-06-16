
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 4  @  1

+ 
+ import com.google.common.collect.Sets;
+ 

> DELETE  3  @  3 : 6

> CHANGE  2 : 3  @  2 : 5

~ 	private static Set<Block> EFFECTIVE_ON;

> INSERT  1 : 6  @  1

+ 	public static void bootstrap() {
+ 		EFFECTIVE_ON = Sets.newHashSet(new Block[] { Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2,
+ 				Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder });
+ 	}
+ 

> EOF
