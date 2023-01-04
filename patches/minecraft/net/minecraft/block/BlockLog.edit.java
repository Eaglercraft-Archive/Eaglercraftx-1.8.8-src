
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  11 : 12  @  11 : 13

~ 	public static PropertyEnum<BlockLog.EnumAxis> LOG_AXIS = null;

> INSERT  8 : 12  @  8

+ 	public static void bootstrapStates() {
+ 		LOG_AXIS = PropertyEnum.<BlockLog.EnumAxis>create("axis", BlockLog.EnumAxis.class);
+ 	}
+ 

> EOF
