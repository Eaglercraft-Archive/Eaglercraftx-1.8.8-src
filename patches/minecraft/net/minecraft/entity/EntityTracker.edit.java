
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import com.carrotsearch.hppc.IntObjectHashMap;
+ import com.carrotsearch.hppc.IntObjectMap;

> DELETE  7  @  7 : 10

> DELETE  26  @  26 : 27

> CHANGE  3 : 5  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  5 : 6  @  5 : 6

~ 	private IntObjectMap<EntityTrackerEntry> trackedEntityHashTable = new IntObjectHashMap<>();

> INSERT  8 : 12  @  8

+ 	public void updateMaxTrackingThreshold(int dist) {
+ 		maxTrackingDistanceThreshold = dist;
+ 	}
+ 

> CHANGE  7 : 8  @  7 : 8

~ 					entitytrackerentry.updatePlayerEntity(entityplayermp, maxTrackingDistanceThreshold);

> DELETE  62  @  62 : 66

> CHANGE  1 : 2  @  1 : 2

~ 			if (this.trackedEntityHashTable.containsKey(entityIn.getEntityId())) {

> CHANGE  6 : 8  @  6 : 8

~ 			this.trackedEntityHashTable.put(entityIn.getEntityId(), entitytrackerentry);
~ 			entitytrackerentry.updatePlayerEntities(this.theWorld.playerEntities, maxTrackingDistanceThreshold);

> CHANGE  16 : 17  @  16 : 17

~ 			this.trackedEntityHashTable.get(entityIn.getEntityId()).trackedEntity

> CHANGE  20 : 21  @  20 : 22

~ 		EntityTrackerEntry entitytrackerentry1 = this.trackedEntityHashTable.remove(entityIn.getEntityId());

> CHANGE  11 : 12  @  11 : 12

~ 			entitytrackerentry.updatePlayerList(this.theWorld.playerEntities, maxTrackingDistanceThreshold);

> CHANGE  11 : 12  @  11 : 12

~ 					entitytrackerentry1.updatePlayerEntity(entityplayermp, maxTrackingDistanceThreshold);

> CHANGE  9 : 10  @  9 : 10

~ 				entitytrackerentry.updatePlayerEntities(this.theWorld.playerEntities, maxTrackingDistanceThreshold);

> CHANGE  1 : 2  @  1 : 2

~ 				entitytrackerentry.updatePlayerEntity(parEntityPlayerMP, maxTrackingDistanceThreshold);

> CHANGE  6 : 7  @  6 : 8

~ 		EntityTrackerEntry entitytrackerentry = this.trackedEntityHashTable.get(entityIn.getEntityId());

> CHANGE  7 : 8  @  7 : 9

~ 		EntityTrackerEntry entitytrackerentry = this.trackedEntityHashTable.get(entityIn.getEntityId());

> CHANGE  18 : 19  @  18 : 19

~ 				entitytrackerentry.updatePlayerEntity(parEntityPlayerMP, maxTrackingDistanceThreshold);

> EOF
