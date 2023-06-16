
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  14 : 15  @  14 : 16

~ 	public static PropertyEnum<BlockHugeMushroom.EnumType> VARIANT;

> CHANGE  9 : 14  @  9 : 10

~ 	public static void bootstrapStates() {
~ 		VARIANT = PropertyEnum.<BlockHugeMushroom.EnumType>create("variant", BlockHugeMushroom.EnumType.class);
~ 	}
~ 
~ 	public int quantityDropped(EaglercraftRandom random) {

> CHANGE  16 : 17  @  16 : 17

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> EOF
