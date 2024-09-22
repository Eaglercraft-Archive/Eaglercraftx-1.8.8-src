
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  2 : 4  @  2

+ import com.google.common.collect.Maps;
+ 

> INSERT  4 : 6  @  4

+ 	public static final SoundCategory[] _VALUES = values();
+ 

> CHANGE  23 : 26  @  23 : 24

~ 		SoundCategory[] categories = _VALUES;
~ 		for (int i = 0; i < categories.length; ++i) {
~ 			SoundCategory soundcategory = categories[i];

> EOF
