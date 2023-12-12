
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  36 : 37  @  36 : 38

~ 	public static PropertyEnum<BlockWall.EnumType> VARIANT;

> INSERT  13 : 17  @  13

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockWall.EnumType>create("variant", BlockWall.EnumType.class);
+ 	}
+ 

> EOF
