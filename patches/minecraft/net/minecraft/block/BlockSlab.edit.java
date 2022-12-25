
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  21 : 22  @  21 : 23

~ 	public static PropertyEnum<BlockSlab.EnumBlockHalf> HALF;

> INSERT  34 : 38  @  35

+ 	public static void bootstrapStates() {
+ 		HALF = PropertyEnum.<BlockSlab.EnumBlockHalf>create("half", BlockSlab.EnumBlockHalf.class);
+ 	}
+ 

> CHANGE  86 : 87  @  83 : 84

~ 	public int quantityDropped(EaglercraftRandom var1) {

> EOF
