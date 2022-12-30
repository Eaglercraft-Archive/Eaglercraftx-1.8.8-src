
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 4  @  2 : 4

~ 
~ import com.google.common.base.Predicate;
~ 

> CHANGE  13 : 14  @  12 : 18

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

> EOF
