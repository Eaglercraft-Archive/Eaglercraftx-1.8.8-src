
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  17 : 18  @  17 : 19

~ 	public static PropertyEnum<BlockDirt.DirtType> VARIANT;

> INSERT  9 : 13  @  9

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockDirt.DirtType>create("variant", BlockDirt.DirtType.class);
+ 	}
+ 

> EOF
