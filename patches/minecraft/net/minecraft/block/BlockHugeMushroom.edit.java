
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  18 : 19  @  18 : 20

~ 	public static PropertyEnum<BlockHugeMushroom.EnumType> VARIANT;

> CHANGE  28 : 33  @  29 : 30

~ 	public static void bootstrapStates() {
~ 		VARIANT = PropertyEnum.<BlockHugeMushroom.EnumType>create("variant", BlockHugeMushroom.EnumType.class);
~ 	}
~ 
~ 	public int quantityDropped(EaglercraftRandom random) {

> CHANGE  49 : 50  @  46 : 47

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> EOF
