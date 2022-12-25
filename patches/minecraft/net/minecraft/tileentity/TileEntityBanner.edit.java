
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  3 : 7  @  4

+ import java.util.function.Supplier;
+ 
+ import com.google.common.collect.Lists;
+ 

> DELETE  16  @  13 : 14

> CHANGE  185 : 194  @  183 : 190

~ 		BORDER("border", "bo", "###", "# #", "###"),
~ 		CURLY_BORDER("curly_border", "cbo", () -> new ItemStack(Blocks.vine)),
~ 		CREEPER("creeper", "cre", () -> new ItemStack(Items.skull, 1, 4)),
~ 		GRADIENT("gradient", "gra", "# #", " # ", " # "), GRADIENT_UP("gradient_up", "gru", " # ", " # ", "# #"),
~ 		BRICKS("bricks", "bri", () -> new ItemStack(Blocks.brick_block)),
~ 		SKULL("skull", "sku", () -> new ItemStack(Items.skull, 1, 1)),
~ 		FLOWER("flower", "flo",
~ 				() -> new ItemStack(Blocks.red_flower, 1, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta())),
~ 		MOJANG("mojang", "moj", () -> new ItemStack(Items.golden_apple, 1, 1));

> INSERT  198 : 199  @  194

+ 		private Supplier<ItemStack> patternCraftingStackSupplier;

> CHANGE  207 : 208  @  202 : 203

~ 		private EnumBannerPattern(String name, String id, Supplier<ItemStack> craftingItem) {

> CHANGE  209 : 210  @  204 : 205

~ 			this.patternCraftingStackSupplier = craftingItem;

> CHANGE  232 : 233  @  227 : 228

~ 			return this.patternCraftingStackSupplier != null || this.craftingLayers[0] != null;

> CHANGE  236 : 237  @  231 : 232

~ 			return this.patternCraftingStackSupplier != null;

> INSERT  240 : 243  @  235

+ 			if (patternCraftingStack == null) {
+ 				patternCraftingStack = patternCraftingStackSupplier.get();
+ 			}

> EOF
