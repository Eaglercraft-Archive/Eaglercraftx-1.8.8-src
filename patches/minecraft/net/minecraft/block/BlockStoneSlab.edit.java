
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  22 : 23  @  22 : 24

~ 	public static PropertyEnum<BlockStoneSlab.EnumType> VARIANT;

> CHANGE  37 : 42  @  38 : 39

~ 	public static void bootstrapStates() {
~ 		VARIANT = PropertyEnum.<BlockStoneSlab.EnumType>create("variant", BlockStoneSlab.EnumType.class);
~ 	}
~ 
~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> EOF
