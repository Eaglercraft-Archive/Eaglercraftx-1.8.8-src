
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  3 : 7  @  3

+ 
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Maps;
+ 

> DELETE  3  @  3 : 4

> CHANGE  15 : 16  @  15 : 16

~ 		LinkedHashMap<IProperty, Comparable> linkedhashmap = Maps.newLinkedHashMap(iblockstate.getProperties());

> CHANGE  4 : 5  @  4 : 5

~ 			s = this.name.getName(linkedhashmap.remove(this.name));

> EOF
