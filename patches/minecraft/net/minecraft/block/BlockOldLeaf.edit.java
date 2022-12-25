
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  3 : 6  @  4 : 7

~ 
~ import com.google.common.base.Predicate;
~ 

> DELETE  11  @  12 : 13

> DELETE  14  @  16 : 18

> CHANGE  20 : 21  @  24 : 30

~ 	public static PropertyEnum<BlockPlanks.EnumType> VARIANT;

> INSERT  27 : 35  @  36

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>() {
+ 			public boolean apply(BlockPlanks.EnumType blockplanks$enumtype) {
+ 				return blockplanks$enumtype.getMetadata() < 4;
+ 			}
+ 		});
+ 	}
+ 

> DELETE  118  @  119 : 130

> EOF
