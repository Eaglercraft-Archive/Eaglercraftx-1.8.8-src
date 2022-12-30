
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  5 : 9  @  7

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 

> DELETE  16  @  12 : 29

> CHANGE  3 : 4  @  20 : 21

~ 	private static CraftingManager instance;

> INSERT  4 : 7  @  4

+ 		if (instance == null) {
+ 			instance = new CraftingManager();
+ 		}

> EOF
