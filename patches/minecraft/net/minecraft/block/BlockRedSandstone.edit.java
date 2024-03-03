
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 5

~ 

> CHANGE  11 : 12  @  11 : 13

~ 	public static PropertyEnum<BlockRedSandstone.EnumType> TYPE;

> INSERT  7 : 11  @  7

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockRedSandstone.EnumType>create("type", BlockRedSandstone.EnumType.class);
+ 	}
+ 

> CHANGE  5 : 8  @  5 : 7

~ 		BlockRedSandstone.EnumType[] types = BlockRedSandstone.EnumType.META_LOOKUP;
~ 		for (int i = 0; i < types.length; ++i) {
~ 			list.add(new ItemStack(item, 1, types[i].getMetadata()));

> CHANGE  20 : 21  @  20 : 21

~ 		public static final BlockRedSandstone.EnumType[] META_LOOKUP = new BlockRedSandstone.EnumType[3];

> CHANGE  35 : 38  @  35 : 37

~ 			BlockRedSandstone.EnumType[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				META_LOOKUP[types[i].getMetadata()] = types[i];

> EOF
