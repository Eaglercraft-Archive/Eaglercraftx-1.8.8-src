
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 5  @  2

+ import java.util.function.Supplier;
+ 
+ import com.google.common.collect.Lists;
+ 

> DELETE  13  @  9 : 10

> CHANGE  169 : 178  @  170 : 177

~ 		BORDER("border", "bo", "###", "# #", "###"),
~ 		CURLY_BORDER("curly_border", "cbo", () -> new ItemStack(Blocks.vine)),
~ 		CREEPER("creeper", "cre", () -> new ItemStack(Items.skull, 1, 4)),
~ 		GRADIENT("gradient", "gra", "# #", " # ", " # "), GRADIENT_UP("gradient_up", "gru", " # ", " # ", "# #"),
~ 		BRICKS("bricks", "bri", () -> new ItemStack(Blocks.brick_block)),
~ 		SKULL("skull", "sku", () -> new ItemStack(Items.skull, 1, 1)),
~ 		FLOWER("flower", "flo",
~ 				() -> new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta())),
~ 		MOJANG("mojang", "moj", () -> new ItemStack(Items.golden_apple, 1, 1));

> INSERT  13 : 14  @  11

+ 		private Supplier<ItemStack> patternCraftingStackSupplier;

> CHANGE  9 : 10  @  8 : 9

~ 		private EnumBannerPattern(String name, String id, Supplier<ItemStack> craftingItem) {

> CHANGE  2 : 3  @  2 : 3

~ 			this.patternCraftingStackSupplier = craftingItem;

> CHANGE  23 : 24  @  23 : 24

~ 			return this.patternCraftingStackSupplier != null || this.craftingLayers[0] != null;

> CHANGE  4 : 5  @  4 : 5

~ 			return this.patternCraftingStackSupplier != null;

> INSERT  4 : 7  @  4

+ 			if (patternCraftingStack == null) {
+ 				patternCraftingStack = patternCraftingStackSupplier.get();
+ 			}

> EOF
