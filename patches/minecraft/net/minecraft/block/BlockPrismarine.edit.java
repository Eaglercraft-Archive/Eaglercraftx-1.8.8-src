
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ 

> CHANGE  13 : 14  @  13 : 15

~ 	public static PropertyEnum<BlockPrismarine.EnumType> VARIANT;

> INSERT  10 : 14  @  10

+ 	public static void bootstrapStates() {
+ 		VARIANT = PropertyEnum.<BlockPrismarine.EnumType>create("variant", BlockPrismarine.EnumType.class);
+ 	}
+ 

> EOF
