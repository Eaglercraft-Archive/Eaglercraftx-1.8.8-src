
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 3

~ import com.carrotsearch.hppc.LongHashSet;
~ import com.carrotsearch.hppc.LongSet;
~ import com.carrotsearch.hppc.cursors.LongCursor;

> CHANGE  1 : 4  @  1 : 3

~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> INSERT  1 : 2  @  1

+ import net.minecraft.entity.EntityList;

> DELETE  9  @  9 : 12

> CHANGE  5 : 6  @  5 : 6

~ 	private final LongSet eligibleChunksForSpawning = new LongHashSet();

> CHANGE  9 : 12  @  9 : 10

~ 			List<EntityPlayer> lst = spawnHostileMobs.playerEntities;
~ 			for (int m = 0, n = lst.size(); m < n; ++m) {
~ 				EntityPlayer entityplayer = lst.get(m);

> CHANGE  3 : 4  @  3 : 4

~ 					byte b0 = (byte) spawnHostileMobs.getMinecraftServer().getConfigurationManager().getViewDistance();

> CHANGE  4 : 9  @  4 : 6

~ 							int cx = l + j;
~ 							int cz = i1 + k;
~ 							long chunkcoordintpair = ChunkCoordIntPair.chunkXZ2Int(cx, cz);
~ 							if (!this.eligibleChunksForSpawning.contains(chunkcoordintpair)
~ 									&& spawnHostileMobs.theChunkProviderServer.chunkExists(cx, cz)) {

> CHANGE  13 : 16  @  13 : 14

~ 			EnumCreatureType[] types = EnumCreatureType._VALUES;
~ 			for (int m = 0; m < types.length; ++m) {
~ 				EnumCreatureType enumcreaturetype = types[m];

> CHANGE  6 : 11  @  6 : 9

~ 						label374: for (LongCursor chunkcoordintpair1 : this.eligibleChunksForSpawning) {
~ 							long chunkcoordintpair1l = chunkcoordintpair1.value;
~ 							int chunkXPos = (int) (chunkcoordintpair1l & 4294967295L);
~ 							int chunkZPos = (int) (chunkcoordintpair1l >>> 32);
~ 							BlockPos blockpos = getRandomChunkPosition(spawnHostileMobs, chunkXPos, chunkZPos);

> CHANGE  42 : 44  @  42 : 45

~ 													entityliving = (EntityLiving) EntityList.createEntityByClassUnsafe(
~ 															biomegenbase$spawnlistentry.entityClass, spawnHostileMobs);

> CHANGE  1 : 2  @  1 : 2

~ 													EagRuntime.debugPrintStackTrace(exception);

> CHANGE  70 : 71  @  70 : 71

~ 			int parInt3, int parInt4, EaglercraftRandom parRandom) {

> CHANGE  22 : 24  @  22 : 25

~ 								entityliving = (EntityLiving) EntityList
~ 										.createEntityByClass(biomegenbase$spawnlistentry.entityClass, worldIn);

> CHANGE  1 : 2  @  1 : 2

~ 								EagRuntime.debugPrintStackTrace(exception);

> EOF
