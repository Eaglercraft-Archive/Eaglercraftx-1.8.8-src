
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  16 : 17  @  16 : 18

~ 	public static PropertyEnum<BlockSlab.EnumBlockHalf> HALF;

> INSERT  12 : 16  @  12

+ 	public static void bootstrapStates() {
+ 		HALF = PropertyEnum.<BlockSlab.EnumBlockHalf>create("half", BlockSlab.EnumBlockHalf.class);
+ 	}
+ 

> CHANGE  48 : 49  @  48 : 49

~ 	public int quantityDropped(EaglercraftRandom var1) {

> EOF
