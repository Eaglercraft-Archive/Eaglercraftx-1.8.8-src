
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  8  @  10 : 11

> CHANGE  8 : 9  @  9 : 11

~ 	public static PropertyEnum<BlockSilverfish.EnumType> VARIANT;

> CHANGE  9 : 14  @  10 : 11

~ 	public static void bootstrapStates() {
~ 		VARIANT = PropertyEnum.<BlockSilverfish.EnumType>create("variant", BlockSilverfish.EnumType.class);
~ 	}
~ 
~ 	public int quantityDropped(EaglercraftRandom var1) {

> DELETE  31  @  27 : 38

> EOF
