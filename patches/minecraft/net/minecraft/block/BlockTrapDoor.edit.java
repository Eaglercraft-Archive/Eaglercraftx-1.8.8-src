
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  26 : 27  @  29 : 31

~ 	public static PropertyEnum<BlockTrapDoor.DoorHalf> HALF;

> INSERT  38 : 42  @  42

+ 	public static void bootstrapStates() {
+ 		HALF = PropertyEnum.<BlockTrapDoor.DoorHalf>create("half", BlockTrapDoor.DoorHalf.class);
+ 	}
+ 

> DELETE  119  @  119 : 139

> EOF
