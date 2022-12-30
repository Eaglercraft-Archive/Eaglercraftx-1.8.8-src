
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  16 : 17  @  16 : 18

~ 	public static PropertyEnum<BlockHugeMushroom.EnumType> VARIANT;

> CHANGE  10 : 15  @  11 : 12

~ 	public static void bootstrapStates() {
~ 		VARIANT = PropertyEnum.<BlockHugeMushroom.EnumType>create("variant", BlockHugeMushroom.EnumType.class);
~ 	}
~ 
~ 	public int quantityDropped(EaglercraftRandom random) {

> CHANGE  21 : 22  @  17 : 18

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> EOF
