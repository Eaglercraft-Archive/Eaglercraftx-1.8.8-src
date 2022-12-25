
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  5 : 9  @  7

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 

> DELETE  12  @  10 : 11

> CHANGE  27 : 28  @  26 : 27

~ 		LinkedHashMap<IProperty, Comparable> linkedhashmap = Maps.newLinkedHashMap(iblockstate.getProperties());

> CHANGE  32 : 33  @  31 : 32

~ 			s = this.name.getName(linkedhashmap.remove(this.name));

> EOF
