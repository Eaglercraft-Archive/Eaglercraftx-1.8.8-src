
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 5

~ 

> CHANGE  28 : 29  @  29 : 31

~ 	public static PropertyEnum<BlockWall.EnumType> VARIANT;

> INSERT  42 : 46  @  44

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockWall.EnumType>create("variant", BlockWall.EnumType.class);
+ 	}
+ 

> EOF
