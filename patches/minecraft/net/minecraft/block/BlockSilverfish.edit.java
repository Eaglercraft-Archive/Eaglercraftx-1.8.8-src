
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  11  @  13 : 14

> CHANGE  19 : 20  @  22 : 24

~ 	public static PropertyEnum<BlockSilverfish.EnumType> VARIANT;

> CHANGE  28 : 33  @  32 : 33

~ 	public static void bootstrapStates() {
~ 		VARIANT = PropertyEnum.<BlockSilverfish.EnumType>create("variant", BlockSilverfish.EnumType.class);
~ 	}
~ 
~ 	public int quantityDropped(EaglercraftRandom var1) {

> DELETE  59  @  59 : 70

> EOF
