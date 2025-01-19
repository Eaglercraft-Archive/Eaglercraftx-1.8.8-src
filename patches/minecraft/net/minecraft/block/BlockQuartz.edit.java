
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  16 : 17  @  16 : 18

~ 	public static PropertyEnum<BlockQuartz.EnumType> VARIANT;

> INSERT  7 : 11  @  7

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockQuartz.EnumType>create("variant", BlockQuartz.EnumType.class);
+ 	}
+ 

> CHANGE  59 : 60  @  59 : 60

~ 		private static final BlockQuartz.EnumType[] META_LOOKUP = new BlockQuartz.EnumType[5];

> CHANGE  31 : 34  @  31 : 33

~ 			BlockQuartz.EnumType[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				META_LOOKUP[types[i].getMetadata()] = types[i];

> EOF
