
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  12 : 13  @  12 : 14

~ 	public static PropertyEnum<BlockPlanks.EnumType> VARIANT;

> INSERT  7 : 11  @  7

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockPlanks.EnumType>create("variant", BlockPlanks.EnumType.class);
+ 	}
+ 

> CHANGE  5 : 8  @  5 : 7

~ 		BlockPlanks.EnumType[] types = BlockPlanks.EnumType.META_LOOKUP;
~ 		for (int i = 0; i < types.length; ++i) {
~ 			list.add(new ItemStack(item, 1, types[i].getMetadata()));

> CHANGE  25 : 26  @  25 : 26

~ 		public static final BlockPlanks.EnumType[] META_LOOKUP = new BlockPlanks.EnumType[6];

> CHANGE  45 : 48  @  45 : 47

~ 			BlockPlanks.EnumType[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				META_LOOKUP[types[i].getMetadata()] = types[i];

> EOF
