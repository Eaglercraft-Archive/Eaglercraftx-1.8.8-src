
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  23 : 24  @  23 : 25

~ 	public static PropertyEnum<BlockSandStone.EnumType> TYPE;

> INSERT  7 : 11  @  7

+ 	public static void bootstrapStates() {
+ 		TYPE = PropertyEnum.<BlockSandStone.EnumType>create("type", BlockSandStone.EnumType.class);
+ 	}
+ 

> EOF
