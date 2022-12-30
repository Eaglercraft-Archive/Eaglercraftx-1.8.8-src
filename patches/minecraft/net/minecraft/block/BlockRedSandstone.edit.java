
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 5

~ 

> CHANGE  12 : 13  @  13 : 15

~ 	public static PropertyEnum<BlockRedSandstone.EnumType> TYPE;

> INSERT  8 : 12  @  9

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockRedSandstone.EnumType>create("type", BlockRedSandstone.EnumType.class);
+ 	}
+ 

> EOF
