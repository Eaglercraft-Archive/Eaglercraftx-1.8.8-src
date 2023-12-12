
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  36 : 37  @  36 : 38

~ 	public static PropertyEnum<BlockTrapDoor.DoorHalf> HALF;

> INSERT  11 : 15  @  11

+ 	public static void bootstrapStates() {
+ 		HALF = PropertyEnum.<BlockTrapDoor.DoorHalf>create("half", BlockTrapDoor.DoorHalf.class);
+ 	}
+ 

> CHANGE  78 : 79  @  78 : 79

~ 		{

> DELETE  13  @  13 : 14

> EOF
