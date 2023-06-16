
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  6  @  6 : 7

> CHANGE  8 : 9  @  8 : 10

~ 	public static PropertyEnum<BlockSilverfish.EnumType> VARIANT;

> CHANGE  8 : 13  @  8 : 9

~ 	public static void bootstrapStates() {
~ 		VARIANT = PropertyEnum.<BlockSilverfish.EnumType>create("variant", BlockSilverfish.EnumType.class);
~ 	}
~ 
~ 	public int quantityDropped(EaglercraftRandom var1) {

> DELETE  26  @  26 : 37

> EOF
