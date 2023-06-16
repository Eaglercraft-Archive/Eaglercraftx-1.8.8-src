
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  18 : 19  @  18 : 20

~ 	public static PropertyEnum<BlockStoneSlabNew.EnumType> VARIANT;

> INSERT  14 : 18  @  14

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockStoneSlabNew.EnumType>create("variant", BlockStoneSlabNew.EnumType.class);
+ 	}
+ 

> CHANGE  4 : 5  @  4 : 5

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> EOF
