
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  23 : 24  @  24 : 26

~ 	public static PropertyEnum<BlockStoneSlabNew.EnumType> VARIANT;

> INSERT  38 : 42  @  40

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockStoneSlabNew.EnumType>create("variant", BlockStoneSlabNew.EnumType.class);
+ 	}
+ 

> CHANGE  46 : 47  @  44 : 45

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> EOF
