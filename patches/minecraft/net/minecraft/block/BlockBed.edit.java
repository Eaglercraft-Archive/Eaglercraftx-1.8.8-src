
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  10  @  11 : 12

> DELETE  14  @  16 : 17

> DELETE  19  @  22 : 23

> CHANGE  21 : 22  @  25 : 27

~ 	public static PropertyEnum<BlockBed.EnumPartType> PART;

> INSERT  31 : 35  @  36

+ 	public static void bootstrapStates() {
+ 		PART = PropertyEnum.<BlockBed.EnumPartType>create("part", BlockBed.EnumPartType.class);
+ 	}
+ 

> CHANGE  37 : 38  @  38 : 90

~ 		return true;

> DELETE  40  @  92 : 102

> DELETE  60  @  122 : 125

> CHANGE  64 : 65  @  129 : 130

~ 	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom var2, int var3) {

> EOF
