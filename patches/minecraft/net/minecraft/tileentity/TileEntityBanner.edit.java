
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 5  @  1

+ import java.util.function.Supplier;
+ 
+ import com.google.common.collect.Lists;
+ 

> DELETE  9  @  9 : 10

> CHANGE  169 : 178  @  169 : 176

~ 		BORDER("border", "bo", "###", "# #", "###"),
~ 		CURLY_BORDER("curly_border", "cbo", () -> new ItemStack(Blocks.vine)),
~ 		CREEPER("creeper", "cre", () -> new ItemStack(Items.skull, 1, 4)),
~ 		GRADIENT("gradient", "gra", "# #", " # ", " # "), GRADIENT_UP("gradient_up", "gru", " # ", " # ", "# #"),
~ 		BRICKS("bricks", "bri", () -> new ItemStack(Blocks.brick_block)),
~ 		SKULL("skull", "sku", () -> new ItemStack(Items.skull, 1, 1)),
~ 		FLOWER("flower", "flo",
~ 				() -> new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta())),
~ 		MOJANG("mojang", "moj", () -> new ItemStack(Items.golden_apple, 1, 1));

> INSERT  4 : 5  @  4

+ 		private Supplier<ItemStack> patternCraftingStackSupplier;

> CHANGE  8 : 9  @  8 : 9

~ 		private EnumBannerPattern(String name, String id, Supplier<ItemStack> craftingItem) {

> CHANGE  1 : 2  @  1 : 2

~ 			this.patternCraftingStackSupplier = craftingItem;

> CHANGE  22 : 23  @  22 : 23

~ 			return this.patternCraftingStackSupplier != null || this.craftingLayers[0] != null;

> CHANGE  3 : 4  @  3 : 4

~ 			return this.patternCraftingStackSupplier != null;

> INSERT  3 : 6  @  3

+ 			if (patternCraftingStack == null) {
+ 				patternCraftingStack = patternCraftingStackSupplier.get();
+ 			}

> EOF
