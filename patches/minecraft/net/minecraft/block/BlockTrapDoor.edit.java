
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  24 : 25  @  24 : 26

~ 	public static PropertyEnum<BlockTrapDoor.DoorHalf> HALF;

> INSERT  11 : 15  @  11

+ 	public static void bootstrapStates() {
+ 		HALF = PropertyEnum.<BlockTrapDoor.DoorHalf>create("half", BlockTrapDoor.DoorHalf.class);
+ 	}
+ 

> DELETE  92  @  92 : 93

> EOF
