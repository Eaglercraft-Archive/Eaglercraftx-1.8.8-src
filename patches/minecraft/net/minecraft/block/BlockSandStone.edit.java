
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  12 : 13  @  12 : 14

~ 	public static PropertyEnum<BlockSandStone.EnumType> TYPE;

> INSERT  7 : 11  @  7

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockSandStone.EnumType>create("type", BlockSandStone.EnumType.class);
+ 	}
+ 

> CHANGE  5 : 8  @  5 : 7

~ 		BlockSandStone.EnumType[] types = BlockSandStone.EnumType.META_LOOKUP;
~ 		for (int i = 0; i < types.length; ++i) {
~ 			list.add(new ItemStack(item, 1, types[i].getMetadata()));

> CHANGE  24 : 25  @  24 : 25

~ 		public static final BlockSandStone.EnumType[] META_LOOKUP = new BlockSandStone.EnumType[3];

> CHANGE  35 : 38  @  35 : 37

~ 			BlockSandStone.EnumType[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				META_LOOKUP[types[i].getMetadata()] = types[i];

> EOF
