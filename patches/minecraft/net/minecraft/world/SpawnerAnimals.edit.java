
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

> DELETE  9  @  9 : 12

> CHANGE  15 : 18  @  15 : 16

~ 			List<EntityPlayer> lst = spawnHostileMobs.playerEntities;
~ 			for (int m = 0, n = lst.size(); m < n; ++m) {
~ 				EntityPlayer entityplayer = lst.get(m);

> CHANGE  9 : 12  @  9 : 10

~ 							if (!this.eligibleChunksForSpawning.contains(chunkcoordintpair)
~ 									&& spawnHostileMobs.theChunkProviderServer.chunkExists(chunkcoordintpair.chunkXPos,
~ 											chunkcoordintpair.chunkZPos)) {

> CHANGE  13 : 16  @  13 : 14

~ 			EnumCreatureType[] types = EnumCreatureType._VALUES;
~ 			for (int m = 0; m < types.length; ++m) {
~ 				EnumCreatureType enumcreaturetype = types[m];

> CHANGE  51 : 53  @  51 : 54

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
