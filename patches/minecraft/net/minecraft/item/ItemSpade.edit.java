
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  3 : 6  @  4

+ 
+ import com.google.common.collect.Sets;
+ 

> DELETE  8  @  6 : 8

> CHANGE  10 : 11  @  10 : 13

~ 	private static Set<Block> EFFECTIVE_ON;

> INSERT  12 : 17  @  14

+ 	public static void bootstrap() {
+ 		EFFECTIVE_ON = Sets.newHashSet(new Block[] { Blocks.clay, Blocks.dirt, Blocks.farmland, Blocks.grass,
+ 				Blocks.gravel, Blocks.mycelium, Blocks.sand, Blocks.snow, Blocks.snow_layer, Blocks.soul_sand });
+ 	}
+ 

> EOF
