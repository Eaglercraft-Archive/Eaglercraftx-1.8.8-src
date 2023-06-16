
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  6  @  6 : 7

> DELETE  4  @  4 : 5

> DELETE  5  @  5 : 6

> CHANGE  2 : 3  @  2 : 4

~ 	public static PropertyEnum<BlockBed.EnumPartType> PART;

> INSERT  9 : 13  @  9

+ 	public static void bootstrapStates() {
+ 		PART = PropertyEnum.<BlockBed.EnumPartType>create("part", BlockBed.EnumPartType.class);
+ 	}
+ 

> CHANGE  2 : 3  @  2 : 54

~ 		return true;

> DELETE  2  @  2 : 12

> DELETE  20  @  20 : 23

> CHANGE  4 : 5  @  4 : 5

~ 	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom var2, int var3) {

> EOF
