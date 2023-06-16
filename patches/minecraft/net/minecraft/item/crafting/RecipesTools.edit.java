
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  6  @  6 : 7

> CHANGE  4 : 5  @  4 : 11

~ 	private Object[][] recipeItems;

> INSERT  2 : 10  @  2

+ 		recipeItems = new Object[][] {
+ 				{ Blocks.planks, Blocks.cobblestone, Items.iron_ingot, Items.diamond, Items.gold_ingot },
+ 				{ Items.wooden_pickaxe, Items.stone_pickaxe, Items.iron_pickaxe, Items.diamond_pickaxe,
+ 						Items.golden_pickaxe },
+ 				{ Items.wooden_shovel, Items.stone_shovel, Items.iron_shovel, Items.diamond_shovel,
+ 						Items.golden_shovel },
+ 				{ Items.wooden_axe, Items.stone_axe, Items.iron_axe, Items.diamond_axe, Items.golden_axe },
+ 				{ Items.wooden_hoe, Items.stone_hoe, Items.iron_hoe, Items.diamond_hoe, Items.golden_hoe } };

> EOF
