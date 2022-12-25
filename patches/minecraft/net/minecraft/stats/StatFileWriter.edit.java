
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  3 : 6  @  4

+ 
+ import com.google.common.collect.Maps;
+ 

> DELETE  7  @  5 : 7

> CHANGE  11 : 12  @  11 : 12

~ 	protected final Map<StatBase, TupleIntJsonSerializable> statsData = Maps.newHashMap();

> EOF
