
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  17 : 18  @  19 : 21

~ 	public static PropertyEnum<BlockLever.EnumOrientation> FACING;

> INSERT  10 : 14  @  11

+ 	public static void bootstrapStates() {
+ 		FACING = PropertyEnum.<BlockLever.EnumOrientation>create("facing", BlockLever.EnumOrientation.class);
+ 	}
+ 

> CHANGE  125 : 126  @  121 : 134

~ 		return true;

> EOF
