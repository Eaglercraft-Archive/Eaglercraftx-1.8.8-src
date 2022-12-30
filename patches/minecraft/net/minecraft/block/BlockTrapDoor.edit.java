
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  24 : 25  @  27 : 29

~ 	public static PropertyEnum<BlockTrapDoor.DoorHalf> HALF;

> INSERT  12 : 16  @  13

+ 	public static void bootstrapStates() {
+ 		HALF = PropertyEnum.<BlockTrapDoor.DoorHalf>create("half", BlockTrapDoor.DoorHalf.class);
+ 	}
+ 

> DELETE  81  @  77 : 97

> EOF
