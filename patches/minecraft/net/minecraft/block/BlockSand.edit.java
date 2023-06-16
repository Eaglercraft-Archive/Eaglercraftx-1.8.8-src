
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  11 : 12  @  11 : 13

~ 	public static PropertyEnum<BlockSand.EnumType> VARIANT;

> INSERT  5 : 9  @  5

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockSand.EnumType>create("variant", BlockSand.EnumType.class);
+ 	}
+ 

> EOF
