
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  13 : 14  @  15 : 17

~ 	public static PropertyEnum<BlockLog.EnumAxis> LOG_AXIS = null;

> INSERT  22 : 26  @  25

+ 	public static void bootstrapStates() {
+ 		LOG_AXIS = PropertyEnum.<BlockLog.EnumAxis>create("axis", BlockLog.EnumAxis.class);
+ 	}
+ 

> EOF
