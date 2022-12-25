
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  4 : 7  @  5

+ 
+ import com.google.common.collect.Maps;
+ 

> CHANGE  17 : 18  @  15 : 16

~ 	private static FurnaceRecipes smeltingBase;

> INSERT  22 : 25  @  20

+ 		if (smeltingBase == null) {
+ 			smeltingBase = new FurnaceRecipes();
+ 		}

> EOF
