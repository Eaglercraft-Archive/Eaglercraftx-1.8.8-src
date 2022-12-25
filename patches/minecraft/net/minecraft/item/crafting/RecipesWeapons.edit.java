
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  6  @  6 : 7

> CHANGE  9 : 10  @  10 : 13

~ 	private Object[][] recipeItems;

> INSERT  12 : 15  @  15

+ 		recipeItems = new Object[][] {
+ 				{ Blocks.planks, Blocks.cobblestone, Items.iron_ingot, Items.diamond, Items.gold_ingot },
+ 				{ Items.wooden_sword, Items.stone_sword, Items.iron_sword, Items.diamond_sword, Items.golden_sword } };

> EOF
