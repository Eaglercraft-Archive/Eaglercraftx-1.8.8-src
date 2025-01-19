
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import java.util.Iterator;
+ import java.util.List;
+ 
+ import com.carrotsearch.hppc.ObjectIntIdentityHashMap;

> DELETE  3  @  3 : 7

> CHANGE  2 : 3  @  2 : 3

~ 	private final ObjectIntIdentityHashMap<T> identityMap = new ObjectIntIdentityHashMap<>(512);

> CHANGE  3 : 4  @  3 : 4

~ 		this.identityMap.put(key, value);

> CHANGE  2 : 3  @  2 : 3

~ 			this.objectList.add((T) null);

> CHANGE  6 : 7  @  6 : 8

~ 		return this.identityMap.getOrDefault(key, -1);

> EOF
