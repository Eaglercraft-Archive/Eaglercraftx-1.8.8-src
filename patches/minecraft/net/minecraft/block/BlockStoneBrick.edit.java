
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  15 : 16  @  15 : 17

~ 	public static PropertyEnum<BlockStoneBrick.EnumType> VARIANT;

> INSERT  27 : 31  @  28

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockStoneBrick.EnumType>create("variant", BlockStoneBrick.EnumType.class);
+ 	}
+ 

> EOF
