
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  6  @  6 : 7

> CHANGE  3 : 4  @  3 : 6

~ 	private Object[][] recipeItems;

> INSERT  2 : 5  @  2

+ 		recipeItems = new Object[][] {
+ 				{ Blocks.planks, Blocks.cobblestone, Items.iron_ingot, Items.diamond, Items.gold_ingot },
+ 				{ Items.wooden_sword, Items.stone_sword, Items.iron_sword, Items.diamond_sword, Items.golden_sword } };

> EOF
