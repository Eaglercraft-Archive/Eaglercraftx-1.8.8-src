
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> DELETE  1  @  1 : 2

> INSERT  1 : 3  @  1

+ import com.google.common.collect.Lists;
+ 

> CHANGE  91 : 95  @  91 : 94

~ 				SoundList.SoundEntry.Type[] types = values();
~ 				for (int i = 0; i < types.length; ++i) {
~ 					if (types[i].field_148583_c.equals(parString1)) {
~ 						return types[i];

> EOF
