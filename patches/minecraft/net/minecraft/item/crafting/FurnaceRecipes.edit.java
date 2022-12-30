
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  2 : 5  @  3

+ 
+ import com.google.common.collect.Maps;
+ 

> CHANGE  13 : 14  @  10 : 11

~ 	private static FurnaceRecipes smeltingBase;

> INSERT  5 : 8  @  5

+ 		if (smeltingBase == null) {
+ 			smeltingBase = new FurnaceRecipes();
+ 		}

> EOF
