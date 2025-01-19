
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> INSERT  2 : 8  @  2

+ 
+ import com.carrotsearch.hppc.LongHashSet;
+ import com.carrotsearch.hppc.LongSet;
+ import com.carrotsearch.hppc.cursors.LongCursor;
+ import com.google.common.collect.Sets;
+ 

> DELETE  5  @  5 : 6

> DELETE  9  @  9 : 10

> DELETE  5  @  5 : 6

> CHANGE  16 : 17  @  16 : 17

~ 	private final LongSet previousActiveChunkSet = new LongHashSet();

> CHANGE  2 : 3  @  2 : 3

~ 			EnumDifficulty parEnumDifficulty) {

> CHANGE  1 : 2  @  1 : 2

~ 				WorldProvider.getProviderForDimension(parInt1), true);

> DELETE  17  @  17 : 19

> DELETE  8  @  8 : 9

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 2

> CHANGE  13 : 14  @  13 : 14

~ 		this.previousActiveChunkSet.retainAll(this.activeChunkSet::contains);

> CHANGE  6 : 14  @  6 : 12

~ 		for (LongCursor chunkcoordintpair : this.activeChunkSet) {
~ 			long l = chunkcoordintpair.value;
~ 			if (!this.previousActiveChunkSet.contains(l)) {
~ 				int chunkXPos = (int) (l & 4294967295L);
~ 				int chunkZPos = (int) (l >>> 32);
~ 				int j = chunkXPos * 16;
~ 				int k = chunkZPos * 16;
~ 				Chunk chunk = this.getChunkFromChunkCoords(chunkXPos, chunkZPos);

> CHANGE  1 : 2  @  1 : 3

~ 				this.previousActiveChunkSet.add(l);

> CHANGE  74 : 75  @  74 : 75

~ 		this.entitiesById.put(parInt1, parEntity);

> CHANGE  3 : 4  @  3 : 4

~ 		return i == this.mc.thePlayer.getEntityId() ? this.mc.thePlayer : super.getEntityByID(i);

> CHANGE  3 : 4  @  3 : 4

~ 		Entity entity = this.entitiesById.remove(parInt1);

> CHANGE  29 : 30  @  29 : 30

~ 		EaglercraftRandom random = new EaglercraftRandom();

> CHANGE  3 : 4  @  3 : 4

~ 		BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  79 : 80  @  79 : 81

~ 				return "Non-integrated multiplayer server";

> EOF
