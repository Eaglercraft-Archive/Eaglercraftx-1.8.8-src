
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 6

> INSERT  8 : 13  @  12

+ import com.google.common.base.Function;
+ import com.google.common.collect.Iterables;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.UnmodifiableIterator;
+ 

> CHANGE  37 : 38  @  32 : 33

~ 			return (List<T>) Arrays.asList((Object[]) aobject);

> CHANGE  15 : 16  @  15 : 16

~ 					? Collections.singletonList((T[]) Cartesian.createArray(this.clazz, 0)).iterator()

> EOF
