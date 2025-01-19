
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 5  @  2

+ import com.carrotsearch.hppc.IntObjectHashMap;
+ import com.carrotsearch.hppc.IntObjectMap;
+ 

> DELETE  2  @  2 : 3

> CHANGE  5 : 6  @  5 : 6

~ 	protected IntObjectMap<PathPoint> pointMap = new IntObjectHashMap<>();

> CHANGE  6 : 7  @  6 : 7

~ 		this.pointMap.clear();

> CHANGE  10 : 11  @  10 : 11

~ 		PathPoint pathpoint = this.pointMap.get(i);

> CHANGE  2 : 3  @  2 : 3

~ 			this.pointMap.put(i, pathpoint);

> EOF
