
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 5

~ 

> CHANGE  24 : 25  @  24 : 26

~ 	public static PropertyEnum<BlockWall.EnumType> VARIANT;

> INSERT  13 : 17  @  13

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockWall.EnumType>create("variant", BlockWall.EnumType.class);
+ 	}
+ 

> CHANGE  72 : 75  @  72 : 74

~ 		BlockWall.EnumType[] types = BlockWall.EnumType.META_LOOKUP;
~ 		for (int i = 0; i < types.length; ++i) {
~ 			list.add(new ItemStack(item, 1, types[i].getMetadata()));

> CHANGE  35 : 36  @  35 : 36

~ 		public static final BlockWall.EnumType[] META_LOOKUP = new BlockWall.EnumType[2];

> CHANGE  35 : 38  @  35 : 37

~ 			BlockWall.EnumType[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				META_LOOKUP[types[i].getMetadata()] = types[i];

> EOF
