
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  12 : 13  @  12 : 14

~ 	public static PropertyEnum<BlockSandStone.EnumType> TYPE;

> INSERT  7 : 11  @  7

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockSandStone.EnumType>create("type", BlockSandStone.EnumType.class);
+ 	}
+ 

> EOF
