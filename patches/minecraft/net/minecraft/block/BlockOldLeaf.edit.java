
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 4  @  1 : 4

~ 
~ import com.google.common.base.Predicate;
~ 

> DELETE  5  @  5 : 6

> DELETE  3  @  3 : 5

> CHANGE  6 : 7  @  6 : 12

~ 	public static PropertyEnum<BlockPlanks.EnumType> VARIANT;

> INSERT  6 : 14  @  6

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>() {
+ 			public boolean apply(BlockPlanks.EnumType blockplanks$enumtype) {
+ 				return blockplanks$enumtype.getMetadata() < 4;
+ 			}
+ 		});
+ 	}
+ 

> DELETE  83  @  83 : 94

> EOF
