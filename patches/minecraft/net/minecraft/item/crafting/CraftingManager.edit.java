
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  5 : 9  @  5

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 

> DELETE  12  @  12 : 29

> CHANGE  3 : 4  @  3 : 4

~ 	private static CraftingManager instance;

> INSERT  3 : 6  @  3

+ 		if (instance == null) {
+ 			instance = new CraftingManager();
+ 		}

> CHANGE  382 : 384  @  382 : 383

~ 		for (int i = 0; i < recipeComponents.length; ++i) {
~ 			Object object = recipeComponents[i];

> CHANGE  22 : 24  @  22 : 23

~ 		for (int i = 0, l = this.recipes.size(); i < l; ++i) {
~ 			IRecipe irecipe = this.recipes.get(i);

> CHANGE  9 : 11  @  9 : 10

~ 		for (int i = 0, l = this.recipes.size(); i < l; ++i) {
~ 			IRecipe irecipe = this.recipes.get(i);

> EOF
