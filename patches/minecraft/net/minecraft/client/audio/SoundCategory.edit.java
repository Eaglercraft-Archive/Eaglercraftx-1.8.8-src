
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  2 : 6  @  2

+ import com.carrotsearch.hppc.IntObjectHashMap;
+ import com.carrotsearch.hppc.IntObjectMap;
+ import com.google.common.collect.Maps;
+ 

> INSERT  4 : 6  @  4

+ 	public static final SoundCategory[] _VALUES = values();
+ 

> CHANGE  1 : 2  @  1 : 2

~ 	private static final IntObjectMap<SoundCategory> ID_CATEGORY_MAP = new IntObjectHashMap<>();

> CHANGE  21 : 24  @  21 : 22

~ 		SoundCategory[] categories = _VALUES;
~ 		for (int i = 0; i < categories.length; ++i) {
~ 			SoundCategory soundcategory = categories[i];

> CHANGE  1 : 2  @  1 : 2

~ 					|| ID_CATEGORY_MAP.containsKey(soundcategory.getCategoryId())) {

> CHANGE  4 : 5  @  4 : 5

~ 			ID_CATEGORY_MAP.put(soundcategory.getCategoryId(), soundcategory);

> EOF
