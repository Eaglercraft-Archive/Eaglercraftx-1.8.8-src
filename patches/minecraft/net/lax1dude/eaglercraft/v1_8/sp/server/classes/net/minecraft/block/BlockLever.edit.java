
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  28 : 29  @  28 : 30

~ 	public static PropertyEnum<BlockLever.EnumOrientation> FACING;

> INSERT  9 : 13  @  9

+ 	public static void bootstrapStates() {
+ 		FACING = PropertyEnum.<BlockLever.EnumOrientation>create("facing", BlockLever.EnumOrientation.class);
+ 	}
+ 

> CHANGE  121 : 122  @  121 : 124

~ 		{

> EOF
