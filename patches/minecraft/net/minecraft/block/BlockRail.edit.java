
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  10 : 11  @  12 : 14

~ 	public static PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE;

> INSERT  18 : 22  @  21

+ 	public static void bootstrapStates() {
+ 		SHAPE = PropertyEnum.<BlockRailBase.EnumRailDirection>create("shape", BlockRailBase.EnumRailDirection.class);
+ 	}
+ 

> EOF
