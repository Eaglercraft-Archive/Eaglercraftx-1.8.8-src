
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  7  @  7 : 8

> CHANGE  2 : 3  @  2 : 10

~ 	private Object[][] recipeItems;

> INSERT  2 : 11  @  2

+ 		recipeItems = new Object[][] { { Blocks.gold_block, new ItemStack(Items.gold_ingot, 9) },
+ 				{ Blocks.iron_block, new ItemStack(Items.iron_ingot, 9) },
+ 				{ Blocks.diamond_block, new ItemStack(Items.diamond, 9) },
+ 				{ Blocks.emerald_block, new ItemStack(Items.emerald, 9) },
+ 				{ Blocks.lapis_block, new ItemStack(Items.dye, 9, EnumDyeColor.BLUE.getDyeDamage()) },
+ 				{ Blocks.redstone_block, new ItemStack(Items.redstone, 9) },
+ 				{ Blocks.coal_block, new ItemStack(Items.coal, 9, 0) },
+ 				{ Blocks.hay_block, new ItemStack(Items.wheat, 9) },
+ 				{ Blocks.slime_block, new ItemStack(Items.slime_ball, 9) } };

> EOF
