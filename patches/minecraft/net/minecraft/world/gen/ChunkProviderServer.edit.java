
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import com.carrotsearch.hppc.LongHashSet;
+ import com.carrotsearch.hppc.LongObjectHashMap;
+ import com.carrotsearch.hppc.LongObjectMap;
+ import com.carrotsearch.hppc.LongSet;

> DELETE  3  @  3 : 4

> DELETE  1  @  1 : 3

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;

> DELETE  3  @  3 : 4

> DELETE  2  @  2 : 3

> CHANGE  7 : 10  @  7 : 9

~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  3 : 4  @  3 : 4

~ 	private LongSet droppedChunksSet = new LongHashSet();

> CHANGE  4 : 6  @  4 : 6

~ 	private LongObjectMap<Chunk> id2ChunkMap = new LongObjectHashMap<>();
~ 	private List<Chunk> loadedChunks = Lists.newLinkedList();

> CHANGE  4 : 5  @  4 : 5

~ 		this.dummyChunk = new EmptyChunk(parWorldServer, Integer.MIN_VALUE, Integer.MIN_VALUE);

> CHANGE  6 : 7  @  6 : 7

~ 		return this.id2ChunkMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(i, j));

> CHANGE  9 : 10  @  9 : 10

~ 				this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(parInt1, parInt2));

> CHANGE  2 : 3  @  2 : 3

~ 			this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(parInt1, parInt2));

> CHANGE  13 : 15  @  13 : 15

~ 		this.droppedChunksSet.removeAll(k);
~ 		Chunk chunk = this.id2ChunkMap.get(k);

> INSERT  8 : 9  @  8

+ 						++EaglerMinecraftServer.counterChunkGenerate;

> CHANGE  5 : 6  @  5 : 6

~ 								HString.format("%d,%d", new Object[] { Integer.valueOf(i), Integer.valueOf(j) }));

> INSERT  5 : 7  @  5

+ 			} else {
+ 				++EaglerMinecraftServer.counterChunkRead;

> CHANGE  2 : 3  @  2 : 3

~ 			this.id2ChunkMap.put(k, chunk);

> CHANGE  9 : 10  @  9 : 10

~ 		Chunk chunk = this.id2ChunkMap.get(ChunkCoordIntPair.chunkXZ2Int(i, j));

> CHANGE  19 : 21  @  19 : 20

~ 				logger.error("Couldn\'t load chunk");
~ 				logger.error(exception);

> CHANGE  10 : 12  @  10 : 11

~ 				logger.error("Couldn\'t save entities");
~ 				logger.error(exception);

> INSERT  10 : 11  @  10

+ 				++EaglerMinecraftServer.counterChunkWrite;

> CHANGE  1 : 3  @  1 : 5

~ 				logger.error("Couldn\'t save chunk");
~ 				logger.error(ioexception);

> DELETE  1  @  1 : 2

> CHANGE  29 : 30  @  29 : 30

~ 		for (int j = 0, l = arraylist.size(); j < l; ++j) {

> CHANGE  29 : 31  @  29 : 31

~ 					long olong = this.droppedChunksSet.iterator().next().value;
~ 					Chunk chunk = this.id2ChunkMap.get(olong);

> CHANGE  4 : 5  @  4 : 5

~ 						this.id2ChunkMap.remove(olong);

> CHANGE  3 : 4  @  3 : 4

~ 					this.droppedChunksSet.removeAll(olong);

> CHANGE  16 : 17  @  16 : 17

~ 		return "ServerChunkCache: " + this.id2ChunkMap.size() + " Drop: " + this.droppedChunksSet.size();

> CHANGE  12 : 13  @  12 : 13

~ 		return this.id2ChunkMap.size();

> EOF
