
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  7 : 11  @  9

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 

> DELETE  23  @  21 : 38

> CHANGE  26 : 27  @  41 : 42

~ 	private static CraftingManager instance;

> INSERT  30 : 33  @  45

+ 		if (instance == null) {
+ 			instance = new CraftingManager();
+ 		}

> EOF
