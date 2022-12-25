
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> CHANGE  7 : 8  @  10 : 11

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  9 : 10  @  12 : 13

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> INSERT  11 : 16  @  14

+ 
+ import com.google.common.base.Predicate;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Sets;
+ 

> DELETE  34  @  32 : 33

> DELETE  46  @  45 : 60

> DELETE  47  @  61 : 62

> DELETE  50  @  65 : 66

> CHANGE  68 : 69  @  84 : 85

~ 	protected int updateLCG = (new EaglercraftRandom()).nextInt();

> CHANGE  75 : 76  @  91 : 92

~ 	public final EaglercraftRandom rand = new EaglercraftRandom();

> DELETE  83  @  99 : 100

> DELETE  86  @  103 : 104

> INSERT  96 : 99  @  114

+ 		if (!client) {
+ 			throw new IllegalStateException("Singleplayer is unavailable because all of it's code was deleted");
+ 		}

> DELETE  107  @  122 : 123

> CHANGE  119 : 120  @  135 : 136

~ 				return chunk.getBiome(pos);

> CHANGE  131 : 132  @  147 : 148

~ 			return BiomeGenBase.plains;

> DELETE  135  @  151 : 155

> DELETE  189  @  209 : 217

> DELETE  225  @  253 : 255

> CHANGE  240 : 241  @  270 : 271

~ 				if ((flags & 2) != 0 && ((flags & 4) == 0) && chunk.isPopulated()) {

> DELETE  244  @  274 : 281

> CHANGE  280 : 281  @  317 : 321

~ 		this.notifyNeighborsOfStateChange(pos, blockType);

> DELETE  348  @  388 : 390

> DELETE  349  @  391 : 411

> DELETE  1753  @  1815 : 1873

> DELETE  1793  @  1913 : 1937

> CHANGE  1801 : 1802  @  1945 : 1946

~ 	public void forceBlockUpdateTick(Block blockType, BlockPos pos, EaglercraftRandom random) {

> DELETE  2033  @  2177 : 2181

> CHANGE  2061 : 2062  @  2209 : 2210

~ 			if (entityType.isAssignableFrom(entity.getClass()) && filter.apply((T) entity)) {

> CHANGE  2072 : 2074  @  2220 : 2222

~ 		for (EntityPlayer entity : this.playerEntities) {
~ 			if (playerType.isAssignableFrom(entity.getClass()) && filter.apply((T) entity)) {

> CHANGE  2306 : 2307  @  2454 : 2455

~ 	public EntityPlayer getPlayerEntityByUUID(EaglercraftUUID uuid) {

> CHANGE  2504 : 2505  @  2652 : 2653

~ 	public EaglercraftRandom setRandomSeed(int parInt1, int parInt2, int parInt3) {

> CHANGE  2557 : 2558  @  2705 : 2706

~ 			this.theCalendar.setTimeInMillis(System.currentTimeMillis());

> DELETE  2625  @  2773 : 2777

> EOF
