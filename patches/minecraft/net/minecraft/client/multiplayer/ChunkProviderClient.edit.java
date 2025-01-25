
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 9  @  1

+ 
+ import com.carrotsearch.hppc.LongObjectHashMap;
+ import com.carrotsearch.hppc.LongObjectMap;
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  3  @  3 : 4

> DELETE  6  @  6 : 8

> CHANGE  4 : 5  @  4 : 5

~ 	private LongObjectMap<Chunk> chunkMapping = new LongObjectHashMap<>();

> CHANGE  24 : 25  @  24 : 25

~ 		this.chunkMapping.put(ChunkCoordIntPair.chunkXZ2Int(parInt1, parInt2), chunk);

> CHANGE  6 : 7  @  6 : 7

~ 		Chunk chunk = this.chunkMapping.get(ChunkCoordIntPair.chunkXZ2Int(i, j));

> CHANGE  11 : 12  @  11 : 12

~ 		long i = EagRuntime.steadyTimeMillis();

> CHANGE  1 : 3  @  1 : 3

~ 		for (int j = 0, k = this.chunkListing.size(); j < k; ++j) {
~ 			this.chunkListing.get(j).func_150804_b(EagRuntime.steadyTimeMillis() - i > 5L);

> CHANGE  2 : 3  @  2 : 3

~ 		if (EagRuntime.steadyTimeMillis() - i > 100L) {

> CHANGE  1 : 2  @  1 : 2

~ 					new Object[] { Long.valueOf(EagRuntime.steadyTimeMillis() - i) });

> CHANGE  17 : 18  @  17 : 18

~ 		return "MultiplayerChunkCache: " + this.chunkMapping.size() + ", " + this.chunkListing.size();

> INSERT  20 : 24  @  20

+ 
+ 	public Chunk getLoadedChunk(int var1, int var2) {
+ 		return this.chunkMapping.get(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
+ 	}

> EOF
