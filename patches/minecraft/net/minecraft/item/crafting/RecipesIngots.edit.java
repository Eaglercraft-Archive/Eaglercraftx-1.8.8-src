
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  7  @  7 : 8

> CHANGE  9 : 10  @  10 : 18

~ 	private Object[][] recipeItems;

> INSERT  12 : 21  @  20

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
