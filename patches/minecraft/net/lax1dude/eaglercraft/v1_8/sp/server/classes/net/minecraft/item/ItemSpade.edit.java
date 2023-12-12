
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  17 : 18  @  17 : 20

~ 	private static Set<Block> EFFECTIVE_ON = null;

> INSERT  1 : 6  @  1

+ 	public static void doBootstrap() {
+ 		EFFECTIVE_ON = Sets.newHashSet(new Block[] { Blocks.clay, Blocks.dirt, Blocks.farmland, Blocks.grass,
+ 				Blocks.gravel, Blocks.mycelium, Blocks.sand, Blocks.snow, Blocks.snow_layer, Blocks.soul_sand });
+ 	}
+ 

> EOF
