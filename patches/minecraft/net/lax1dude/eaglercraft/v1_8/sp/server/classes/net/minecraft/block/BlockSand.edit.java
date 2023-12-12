
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  22 : 23  @  22 : 24

~ 	public static PropertyEnum<BlockSand.EnumType> VARIANT;

> INSERT  5 : 9  @  5

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockSand.EnumType>create("variant", BlockSand.EnumType.class);
+ 	}
+ 

> EOF
