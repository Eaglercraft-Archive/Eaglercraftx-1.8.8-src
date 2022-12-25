
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  21 : 22  @  21 : 23

~ 	public static PropertyEnum<BlockDirt.DirtType> VARIANT;

> INSERT  31 : 35  @  32

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockDirt.DirtType>create("variant", BlockDirt.DirtType.class);
+ 	}
+ 

> EOF
