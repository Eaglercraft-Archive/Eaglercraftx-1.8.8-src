
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  2 : 8  @  2

+ 
+ import com.carrotsearch.hppc.ObjectFloatHashMap;
+ import com.carrotsearch.hppc.ObjectFloatMap;
+ import com.carrotsearch.hppc.cursors.ObjectFloatCursor;
+ import com.google.common.collect.Maps;
+ 

> CHANGE  10 : 11  @  10 : 11

~ 	private static FurnaceRecipes smeltingBase;

> CHANGE  1 : 2  @  1 : 2

~ 	private ObjectFloatMap<ItemStack> experienceList = new ObjectFloatHashMap<>();

> INSERT  2 : 5  @  2

+ 		if (smeltingBase == null) {
+ 			smeltingBase = new FurnaceRecipes();
+ 		}

> CHANGE  27 : 30  @  27 : 28

~ 		ItemFishFood.FishType[] types = ItemFishFood.FishType.values();
~ 		for (int i = 0; i < types.length; ++i) {
~ 			ItemFishFood.FishType itemfishfood$fishtype = types[i];

> CHANGE  23 : 24  @  23 : 24

~ 		this.experienceList.put(stack, experience);

> CHANGE  22 : 25  @  22 : 25

~ 		for (ObjectFloatCursor<ItemStack> entry : this.experienceList) {
~ 			if (this.compareItemStacks(stack, entry.key)) {
~ 				return entry.value;

> EOF
