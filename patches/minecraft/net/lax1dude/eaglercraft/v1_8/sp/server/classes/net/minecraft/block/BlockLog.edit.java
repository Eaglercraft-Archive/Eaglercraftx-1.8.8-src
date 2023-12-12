
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  22 : 23  @  22 : 24

~ 	public static PropertyEnum<BlockLog.EnumAxis> LOG_AXIS;

> INSERT  8 : 12  @  8

+ 	public static void bootstrapStates() {
+ 		LOG_AXIS = PropertyEnum.<BlockLog.EnumAxis>create("axis", BlockLog.EnumAxis.class);
+ 	}
+ 

> EOF
