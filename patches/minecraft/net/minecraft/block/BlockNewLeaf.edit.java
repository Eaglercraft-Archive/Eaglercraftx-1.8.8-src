
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 4  @  2 : 5

~ 
~ import com.google.common.base.Predicate;
~ 

> DELETE  8  @  8 : 9

> DELETE  3  @  4 : 6

> CHANGE  4 : 5  @  6 : 12

~ 	public static PropertyEnum<BlockPlanks.EnumType> VARIANT;

> INSERT  7 : 15  @  12

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate<BlockPlanks.EnumType>() {
+ 			public boolean apply(BlockPlanks.EnumType blockplanks$enumtype) {
+ 				return blockplanks$enumtype.getMetadata() >= 4;
+ 			}
+ 		});
+ 	}
+ 

> DELETE  62  @  54 : 65

> EOF
