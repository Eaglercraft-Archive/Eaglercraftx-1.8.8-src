
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  13 : 14  @  13 : 15

~ 	public static PropertyEnum<BlockPrismarine.EnumType> VARIANT;

> INSERT  10 : 14  @  10

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockPrismarine.EnumType>create("variant", BlockPrismarine.EnumType.class);
+ 	}
+ 

> CHANGE  35 : 36  @  35 : 36

~ 		private static final BlockPrismarine.EnumType[] META_LOOKUP = new BlockPrismarine.EnumType[3];

> CHANGE  35 : 38  @  35 : 37

~ 			BlockPrismarine.EnumType[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				META_LOOKUP[types[i].getMetadata()] = types[i];

> EOF
