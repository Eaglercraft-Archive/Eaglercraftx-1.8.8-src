
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  8 : 9  @  8 : 10

~ 	public static PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE;

> INSERT  7 : 11  @  7

+ 	public static void bootstrapStates() {
+ 		SHAPE = PropertyEnum.<BlockRailBase.EnumRailDirection>create("shape", BlockRailBase.EnumRailDirection.class);
+ 	}
+ 

> EOF
