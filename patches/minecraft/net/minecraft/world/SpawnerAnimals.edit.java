
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 4  @  3

+ 

> CHANGE  1 : 4  @  1 : 2

~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> INSERT  2 : 3  @  2

+ import net.minecraft.entity.EntityList;

> INSERT  16 : 17  @  16

+ 

> CHANGE  21 : 24  @  21 : 22

~ 							if (!this.eligibleChunksForSpawning.contains(chunkcoordintpair)
~ 									&& spawnHostileMobs.theChunkProviderServer.chunkExists(chunkcoordintpair.chunkXPos,
~ 											chunkcoordintpair.chunkZPos)) {

> CHANGE  65 : 67  @  65 : 68

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
