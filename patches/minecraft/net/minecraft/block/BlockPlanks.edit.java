
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  16 : 17  @  16 : 18

~ 	public static PropertyEnum<BlockPlanks.EnumType> VARIANT;

> INSERT  24 : 28  @  25

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockPlanks.EnumType>create("variant", BlockPlanks.EnumType.class);
+ 	}
+ 

> EOF
