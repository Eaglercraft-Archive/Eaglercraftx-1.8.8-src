
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  17 : 18  @  17 : 19

~ 	public static PropertyEnum<BlockLever.EnumOrientation> FACING;

> INSERT  9 : 13  @  9

+ 	public static void bootstrapStates() {
+ 		FACING = PropertyEnum.<BlockLever.EnumOrientation>create("facing", BlockLever.EnumOrientation.class);
+ 	}
+ 

> CHANGE  17 : 20  @  17 : 18

~ 		EnumFacing[] facings = EnumFacing._VALUES;
~ 		for (int i = 0; i < facings.length; ++i) {
~ 			EnumFacing enumfacing = facings[i];

> CHANGE  19 : 22  @  19 : 20

~ 			EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 			for (int i = 0; i < facings.length; ++i) {
~ 				EnumFacing enumfacing1 = facings[i];

> CHANGE  145 : 146  @  145 : 146

~ 		private static final BlockLever.EnumOrientation[] META_LOOKUP = new BlockLever.EnumOrientation[8];

> CHANGE  70 : 73  @  70 : 72

~ 			BlockLever.EnumOrientation[] orientations = values();
~ 			for (int i = 0; i < orientations.length; ++i) {
~ 				META_LOOKUP[orientations[i].getMetadata()] = orientations[i];

> EOF
