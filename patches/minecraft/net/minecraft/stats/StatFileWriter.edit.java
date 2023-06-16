
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 4  @  1

+ 
+ import com.google.common.collect.Maps;
+ 

> DELETE  1  @  1 : 3

> CHANGE  4 : 5  @  4 : 5

~ 	protected final Map<StatBase, TupleIntJsonSerializable> statsData = Maps.newHashMap();

> EOF
