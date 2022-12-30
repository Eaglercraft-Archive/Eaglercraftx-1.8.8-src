
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  13 : 14  @  13 : 15

~ 	public static PropertyEnum<BlockSandStone.EnumType> TYPE;

> INSERT  8 : 12  @  9

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockSandStone.EnumType>create("type", BlockSandStone.EnumType.class);
+ 	}
+ 

> EOF
