
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> INSERT  3 : 8  @  3

+ 
+ import com.google.common.collect.ComparisonChain;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Ordering;
+ 

> CHANGE  29 : 32  @  29 : 30

~ 		List<NetworkPlayerInfo> lst = field_178674_a.sortedCopy(parCollection);
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			NetworkPlayerInfo networkplayerinfo = lst.get(i);

> EOF
