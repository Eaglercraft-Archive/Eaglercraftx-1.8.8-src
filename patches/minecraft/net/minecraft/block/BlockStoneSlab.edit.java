
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  17 : 18  @  17 : 19

~ 	public static PropertyEnum<BlockStoneSlab.EnumType> VARIANT;

> CHANGE  14 : 19  @  14 : 15

~ 	public static void bootstrapStates() {
~ 		VARIANT = PropertyEnum.<BlockStoneSlab.EnumType>create("variant", BlockStoneSlab.EnumType.class);
~ 	}
~ 
~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> EOF
