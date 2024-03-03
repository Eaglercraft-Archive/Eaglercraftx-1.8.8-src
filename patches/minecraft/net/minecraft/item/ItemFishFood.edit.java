
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  2 : 5  @  2

+ 
+ import com.google.common.collect.Maps;
+ 

> DELETE  2  @  2 : 5

> CHANGE  43 : 46  @  43 : 44

~ 		ItemFishFood.FishType[] types = ItemFishFood.FishType.values();
~ 		for (int i = 0; i < types.length; ++i) {
~ 			ItemFishFood.FishType itemfishfood$fishtype = types[i];

> CHANGE  86 : 89  @  86 : 88

~ 			ItemFishFood.FishType[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				META_LOOKUP.put(Integer.valueOf(types[i].getMetadata()), types[i]);

> EOF
