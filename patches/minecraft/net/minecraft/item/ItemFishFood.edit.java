
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 5  @  1 : 2

~ 
~ import com.carrotsearch.hppc.IntObjectHashMap;
~ import com.carrotsearch.hppc.IntObjectMap;
~ 

> DELETE  2  @  2 : 5

> CHANGE  43 : 46  @  43 : 44

~ 		ItemFishFood.FishType[] types = ItemFishFood.FishType.values();
~ 		for (int i = 0; i < types.length; ++i) {
~ 			ItemFishFood.FishType itemfishfood$fishtype = types[i];

> CHANGE  17 : 18  @  17 : 18

~ 		private static final IntObjectMap<ItemFishFood.FishType> META_LOOKUP = new IntObjectHashMap<>();

> CHANGE  58 : 59  @  58 : 60

~ 			ItemFishFood.FishType itemfishfood$fishtype = META_LOOKUP.get(meta);

> CHANGE  8 : 11  @  8 : 10

~ 			ItemFishFood.FishType[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				META_LOOKUP.put(types[i].getMetadata(), types[i]);

> EOF
