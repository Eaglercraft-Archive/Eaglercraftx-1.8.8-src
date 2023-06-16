
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  2 : 5  @  2

+ 
+ import com.google.common.collect.Maps;
+ 

> CHANGE  10 : 11  @  10 : 11

~ 	private static FurnaceRecipes smeltingBase;

> INSERT  4 : 7  @  4

+ 		if (smeltingBase == null) {
+ 			smeltingBase = new FurnaceRecipes();
+ 		}

> EOF
