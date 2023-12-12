
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  25 : 26  @  25 : 27

~ 	public static PropertyEnum<BlockSilverfish.EnumType> VARIANT;

> CHANGE  8 : 13  @  8 : 9

~ 	public static void bootstrapStates() {
~ 		VARIANT = PropertyEnum.<BlockSilverfish.EnumType>create("variant", BlockSilverfish.EnumType.class);
~ 	}
~ 
~ 	public int quantityDropped(EaglercraftRandom var1) {

> CHANGE  27 : 28  @  27 : 28

~ 		if (world.getGameRules().getBoolean("doTileDrops")) {

> EOF
