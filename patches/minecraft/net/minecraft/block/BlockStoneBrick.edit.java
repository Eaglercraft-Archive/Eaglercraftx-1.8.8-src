
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  11 : 12  @  11 : 13

~ 	public static PropertyEnum<BlockStoneBrick.EnumType> VARIANT;

> INSERT  11 : 15  @  11

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockStoneBrick.EnumType>create("variant", BlockStoneBrick.EnumType.class);
+ 	}
+ 

> EOF
