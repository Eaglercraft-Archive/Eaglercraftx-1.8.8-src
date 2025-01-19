
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import com.carrotsearch.hppc.LongObjectHashMap;
+ import com.carrotsearch.hppc.LongObjectMap;

> DELETE  3  @  3 : 6

> CHANGE  4 : 5  @  4 : 5

~ 	private LongObjectMap<BiomeCache.Block> cacheMap = new LongObjectHashMap<>();

> CHANGE  10 : 11  @  10 : 11

~ 		BiomeCache.Block biomecache$block = this.cacheMap.get(i);

> CHANGE  2 : 3  @  2 : 3

~ 			this.cacheMap.put(i, biomecache$block);

> CHANGE  19 : 20  @  19 : 20

~ 				BiomeCache.Block biomecache$block = this.cache.get(k);

> EOF
