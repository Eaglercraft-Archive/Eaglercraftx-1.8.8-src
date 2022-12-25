
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> DELETE  3  @  4 : 5

> INSERT  4 : 6  @  6

+ import com.google.common.collect.ImmutableSet;
+ 

> CHANGE  21 : 23  @  21 : 23

~ 	public String getName(Object value) {
~ 		return ((Boolean) value).toString();

> EOF
