
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  16 : 17  @  16 : 18

~ 	public static PropertyEnum<BlockQuartz.EnumType> VARIANT;

> INSERT  7 : 11  @  7

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockQuartz.EnumType>create("variant", BlockQuartz.EnumType.class);
+ 	}
+ 

> EOF
