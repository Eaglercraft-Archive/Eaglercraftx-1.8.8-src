
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  17 : 18  @  17 : 19

~ 	public static PropertyEnum<BlockDirt.DirtType> VARIANT;

> INSERT  9 : 13  @  9

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockDirt.DirtType>create("variant", BlockDirt.DirtType.class);
+ 	}
+ 

> CHANGE  50 : 51  @  50 : 51

~ 		private static final BlockDirt.DirtType[] METADATA_LOOKUP = new BlockDirt.DirtType[3];

> CHANGE  45 : 48  @  45 : 47

~ 			BlockDirt.DirtType[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				METADATA_LOOKUP[types[i].getMetadata()] = types[i];

> EOF
