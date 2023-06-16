
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 4  @  1 : 3

~ 
~ import com.google.common.base.Predicate;
~ 

> CHANGE  10 : 11  @  10 : 16

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
