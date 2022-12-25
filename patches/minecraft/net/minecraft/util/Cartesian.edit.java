
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 6

> INSERT  10 : 15  @  14

+ import com.google.common.base.Function;
+ import com.google.common.collect.Iterables;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.UnmodifiableIterator;
+ 

> CHANGE  47 : 48  @  46 : 47

~ 			return (List<T>) Arrays.asList((Object[]) aobject);

> CHANGE  62 : 63  @  61 : 62

~ 					? Collections.singletonList((T[]) Cartesian.createArray(this.clazz, 0)).iterator()

> EOF
