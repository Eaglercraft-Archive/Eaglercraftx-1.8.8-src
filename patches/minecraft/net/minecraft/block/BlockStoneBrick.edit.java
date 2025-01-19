
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  11 : 12  @  11 : 13

~ 	public static PropertyEnum<BlockStoneBrick.EnumType> VARIANT;

> INSERT  11 : 15  @  11

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockStoneBrick.EnumType>create("variant", BlockStoneBrick.EnumType.class);
+ 	}
+ 

> CHANGE  5 : 8  @  5 : 7

~ 		BlockStoneBrick.EnumType[] types = BlockStoneBrick.EnumType.META_LOOKUP;
~ 		for (int i = 0; i < types.length; ++i) {
~ 			list.add(new ItemStack(item, 1, types[i].getMetadata()));

> CHANGE  20 : 21  @  20 : 21

~ 		public static final BlockStoneBrick.EnumType[] META_LOOKUP = new BlockStoneBrick.EnumType[4];

> CHANGE  35 : 38  @  35 : 37

~ 			BlockStoneBrick.EnumType[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				META_LOOKUP[types[i].getMetadata()] = types[i];

> EOF
