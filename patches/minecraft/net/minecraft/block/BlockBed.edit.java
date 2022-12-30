
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  8  @  9 : 10

> DELETE  4  @  5 : 6

> DELETE  5  @  6 : 7

> CHANGE  2 : 3  @  3 : 5

~ 	public static PropertyEnum<BlockBed.EnumPartType> PART;

> INSERT  10 : 14  @  11

+ 	public static void bootstrapStates() {
+ 		PART = PropertyEnum.<BlockBed.EnumPartType>create("part", BlockBed.EnumPartType.class);
+ 	}
+ 

> CHANGE  6 : 7  @  2 : 54

~ 		return true;

> DELETE  3  @  54 : 64

> DELETE  20  @  30 : 33

> CHANGE  4 : 5  @  7 : 8

~ 	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom var2, int var3) {

> EOF
