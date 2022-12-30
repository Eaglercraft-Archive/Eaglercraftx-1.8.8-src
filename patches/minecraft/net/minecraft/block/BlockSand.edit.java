
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  12 : 13  @  12 : 14

~ 	public static PropertyEnum<BlockSand.EnumType> VARIANT;

> INSERT  6 : 10  @  7

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockSand.EnumType>create("variant", BlockSand.EnumType.class);
+ 	}
+ 

> EOF
