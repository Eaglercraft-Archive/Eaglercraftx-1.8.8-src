
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  2 : 4  @  2

+ import com.google.common.collect.Maps;
+ 

> CHANGE  77 : 80  @  77 : 79

~ 			ClickEvent.Action[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				nameMapping.put(types[i].getCanonicalName(), types[i]);

> EOF
