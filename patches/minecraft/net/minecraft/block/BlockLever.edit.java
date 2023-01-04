
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  17 : 18  @  17 : 19

~ 	public static PropertyEnum<BlockLever.EnumOrientation> FACING;

> INSERT  9 : 13  @  9

+ 	public static void bootstrapStates() {
+ 		FACING = PropertyEnum.<BlockLever.EnumOrientation>create("facing", BlockLever.EnumOrientation.class);
+ 	}
+ 

> CHANGE  121 : 122  @  121 : 134

~ 		return true;

> EOF
