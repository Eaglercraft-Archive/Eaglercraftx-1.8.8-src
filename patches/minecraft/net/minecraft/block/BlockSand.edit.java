
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  11 : 12  @  11 : 13

~ 	public static PropertyEnum<BlockSand.EnumType> VARIANT;

> INSERT  5 : 9  @  5

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockSand.EnumType>create("variant", BlockSand.EnumType.class);
+ 	}
+ 

> CHANGE  5 : 8  @  5 : 7

~ 		BlockSand.EnumType[] blocks = BlockSand.EnumType.META_LOOKUP;
~ 		for (int i = 0; i < blocks.length; ++i) {
~ 			list.add(new ItemStack(item, 1, blocks[i].getMetadata()));

> CHANGE  23 : 24  @  23 : 24

~ 		public static final BlockSand.EnumType[] META_LOOKUP = new BlockSand.EnumType[2];

> CHANGE  41 : 44  @  41 : 43

~ 			BlockSand.EnumType[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				META_LOOKUP[types[i].getMetadata()] = types[i];

> EOF
