
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  15 : 16  @  15 : 17

~ 	public static PropertyEnum<BlockSand.EnumType> VARIANT;

> INSERT  21 : 25  @  22

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockSand.EnumType>create("variant", BlockSand.EnumType.class);
+ 	}
+ 

> EOF
