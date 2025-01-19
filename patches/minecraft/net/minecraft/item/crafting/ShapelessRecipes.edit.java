
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  2 : 5  @  2

+ 
+ import com.google.common.collect.Lists;
+ 

> DELETE  2  @  2 : 3

> CHANGE  29 : 30  @  29 : 30

~ 		ArrayList<ItemStack> arraylist = Lists.newArrayList(this.recipeItems);

> CHANGE  7 : 9  @  7 : 8

~ 					for (int m = 0, l = arraylist.size(); m < l; ++m) {
~ 						ItemStack itemstack1 = arraylist.get(m);

> EOF
