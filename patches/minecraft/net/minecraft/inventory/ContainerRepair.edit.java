
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> DELETE  8  @  8 : 14

> DELETE  4  @  4 : 6

> INSERT  1 : 8  @  1

+ import com.carrotsearch.hppc.IntIntMap;
+ import com.carrotsearch.hppc.cursors.IntCursor;
+ import com.carrotsearch.hppc.cursors.IntIntCursor;
+ 
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ 

> CHANGE  118 : 119  @  118 : 119

~ 			IntIntMap map = EnchantmentHelper.getEnchantments(itemstack1);

> CHANGE  47 : 48  @  47 : 49

~ 					IntIntMap map1 = EnchantmentHelper.getEnchantments(itemstack2);

> CHANGE  1 : 3  @  1 : 3

~ 					for (IntIntCursor cur : map1) {
~ 						int i3 = cur.key;

> CHANGE  2 : 4  @  2 : 6

~ 							int k3 = map.getOrDefault(i3, 0);
~ 							int l1 = cur.value;

> CHANGE  15 : 17  @  15 : 19

~ 							for (IntCursor curr : map.keys()) {
~ 								int i2 = curr.value;

> CHANGE  11 : 12  @  11 : 12

~ 								map.put(i3, l1);

> EOF
