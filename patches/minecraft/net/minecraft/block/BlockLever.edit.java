
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  19 : 20  @  21 : 23

~ 	public static PropertyEnum<BlockLever.EnumOrientation> FACING;

> INSERT  29 : 33  @  32

+ 	public static void bootstrapStates() {
+ 		FACING = PropertyEnum.<BlockLever.EnumOrientation>create("facing", BlockLever.EnumOrientation.class);
+ 	}
+ 

> CHANGE  154 : 155  @  153 : 166

~ 		return true;

> EOF
