
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 5

~ 

> CHANGE  24 : 25  @  24 : 26

~ 	public static PropertyEnum<BlockWall.EnumType> VARIANT;

> INSERT  13 : 17  @  13

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockWall.EnumType>create("variant", BlockWall.EnumType.class);
+ 	}
+ 

> EOF
