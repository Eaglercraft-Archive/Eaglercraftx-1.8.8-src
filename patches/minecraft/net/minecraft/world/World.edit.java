
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  5 : 6  @  8 : 9

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> INSERT  2 : 7  @  2

+ 
+ import com.google.common.base.Predicate;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Sets;
+ 

> DELETE  23  @  18 : 19

> DELETE  12  @  13 : 28

> DELETE  1  @  16 : 17

> DELETE  3  @  4 : 5

> CHANGE  18 : 19  @  19 : 20

~ 	protected int updateLCG = (new EaglercraftRandom()).nextInt();

> CHANGE  7 : 8  @  7 : 8

~ 	public final EaglercraftRandom rand = new EaglercraftRandom();

> DELETE  8  @  8 : 9

> DELETE  3  @  4 : 5

> INSERT  10 : 13  @  11

+ 		if (!client) {
+ 			throw new IllegalStateException("Singleplayer is unavailable because all of it's code was deleted");
+ 		}

> DELETE  11  @  8 : 9

> CHANGE  12 : 13  @  13 : 14

~ 				return chunk.getBiome(pos);

> CHANGE  12 : 13  @  12 : 13

~ 			return BiomeGenBase.plains;

> DELETE  4  @  4 : 8

> DELETE  54  @  58 : 66

> DELETE  36  @  44 : 46

> CHANGE  15 : 16  @  17 : 18

~ 				if ((flags & 2) != 0 && ((flags & 4) == 0) && chunk.isPopulated()) {

> DELETE  4  @  4 : 11

> CHANGE  36 : 37  @  43 : 47

~ 		this.notifyNeighborsOfStateChange(pos, blockType);

> DELETE  68  @  71 : 73

> DELETE  1  @  3 : 23

> DELETE  1404  @  1424 : 1482

> DELETE  40  @  98 : 122

> CHANGE  8 : 9  @  32 : 33

~ 	public void forceBlockUpdateTick(Block blockType, BlockPos pos, EaglercraftRandom random) {

> DELETE  232  @  232 : 236

> CHANGE  28 : 29  @  32 : 33

~ 			if (entityType.isAssignableFrom(entity.getClass()) && filter.apply((T) entity)) {

> CHANGE  11 : 13  @  11 : 13

~ 		for (EntityPlayer entity : this.playerEntities) {
~ 			if (playerType.isAssignableFrom(entity.getClass()) && filter.apply((T) entity)) {

> CHANGE  234 : 235  @  234 : 235

~ 	public EntityPlayer getPlayerEntityByUUID(EaglercraftUUID uuid) {

> CHANGE  198 : 199  @  198 : 199

~ 	public EaglercraftRandom setRandomSeed(int parInt1, int parInt2, int parInt3) {

> CHANGE  53 : 54  @  53 : 54

~ 			this.theCalendar.setTimeInMillis(System.currentTimeMillis());

> DELETE  68  @  68 : 72

> EOF
