
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  23 : 24  @  23 : 29

~ 	public static PropertyEnum<BlockPlanks.EnumType> VARIANT;

> INSERT  6 : 14  @  6

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>() {
+ 			public boolean apply(BlockPlanks.EnumType blockplanks$enumtype) {
+ 				return blockplanks$enumtype.getMetadata() >= 4;
+ 			}
+ 		});
+ 	}
+ 

> EOF
