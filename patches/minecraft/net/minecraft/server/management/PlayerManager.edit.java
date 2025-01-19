
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import com.carrotsearch.hppc.LongHashSet;
+ import com.carrotsearch.hppc.LongObjectHashMap;
+ import com.carrotsearch.hppc.LongObjectMap;
+ import com.carrotsearch.hppc.LongSet;

> DELETE  1  @  1 : 2

> DELETE  8  @  8 : 9

> CHANGE  5 : 7  @  5 : 7

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  5 : 6  @  5 : 6

~ 	private final LongObjectMap<PlayerManager.PlayerInstance> playerInstances = new LongObjectHashMap<>();

> CHANGE  46 : 47  @  46 : 47

~ 		return this.playerInstances.get(i) != null;

> CHANGE  4 : 5  @  4 : 6

~ 		PlayerManager.PlayerInstance playermanager$playerinstance = this.playerInstances.get(i);

> CHANGE  2 : 3  @  2 : 3

~ 			this.playerInstances.put(i, playermanager$playerinstance);

> CHANGE  33 : 34  @  33 : 34

~ 		LongSet arraylist = new LongHashSet(player.loadedChunks);

> CHANGE  6 : 7  @  6 : 7

~ 		long chunkcoordintpair = this.getPlayerInstance(k, l, true).chunkCoordsHash;

> CHANGE  12 : 13  @  12 : 13

~ 					chunkcoordintpair = this.getPlayerInstance(k + i1, l + j1, true).chunkCoordsHash;

> CHANGE  12 : 13  @  12 : 13

~ 			chunkcoordintpair = this.getPlayerInstance(k + i1, l + j1, true).chunkCoordsHash;

> CHANGE  69 : 70  @  69 : 70

~ 				&& !player.loadedChunks.contains(playermanager$playerinstance.chunkCoordsHash);

> CHANGE  7 : 10  @  7 : 8

~ 			List<EntityPlayerMP> playerz = Lists.newArrayList(this.players);
~ 			for (int m = 0, n = playerz.size(); m < n; ++m) {
~ 				EntityPlayerMP entityplayermp = playerz.get(m);

> INSERT  34 : 35  @  34

+ 		private final long chunkCoordsHash;

> INSERT  7 : 8  @  7

+ 			this.chunkCoordsHash = ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ);

> CHANGE  14 : 15  @  14 : 15

~ 				player.loadedChunks.add(this.chunkCoordsHash);

> CHANGE  12 : 13  @  12 : 13

~ 				player.loadedChunks.removeAll(this.chunkCoordsHash);

> CHANGE  51 : 52  @  51 : 52

~ 				if (!entityplayermp.loadedChunks.contains(this.chunkCoordsHash)) {

> EOF
