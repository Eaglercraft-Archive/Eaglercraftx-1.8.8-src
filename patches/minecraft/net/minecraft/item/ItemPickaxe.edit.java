
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

> CHANGE  11 : 12  @  12 : 18

~ 	private static Set<Block> EFFECTIVE_ON;

> INSERT  13 : 22  @  19

+ 	public static void bootstrap() {
+ 		EFFECTIVE_ON = Sets.newHashSet(new Block[] { Blocks.activator_rail, Blocks.coal_ore, Blocks.cobblestone,
+ 				Blocks.detector_rail, Blocks.diamond_block, Blocks.diamond_ore, Blocks.double_stone_slab,
+ 				Blocks.golden_rail, Blocks.gold_block, Blocks.gold_ore, Blocks.ice, Blocks.iron_block, Blocks.iron_ore,
+ 				Blocks.lapis_block, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.mossy_cobblestone,
+ 				Blocks.netherrack, Blocks.packed_ice, Blocks.rail, Blocks.redstone_ore, Blocks.sandstone,
+ 				Blocks.red_sandstone, Blocks.stone, Blocks.stone_slab });
+ 	}
+ 

> EOF
