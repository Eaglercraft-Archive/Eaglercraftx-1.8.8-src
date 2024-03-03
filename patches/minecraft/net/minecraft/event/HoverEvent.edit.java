
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 4  @  1

+ 
+ import com.google.common.collect.Maps;
+ 

> CHANGE  78 : 81  @  78 : 80

~ 			HoverEvent.Action[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				nameMapping.put(types[i].getCanonicalName(), types[i]);

> EOF
