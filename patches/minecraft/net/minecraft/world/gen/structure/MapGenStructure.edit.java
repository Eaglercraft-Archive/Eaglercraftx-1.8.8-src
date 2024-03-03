
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 4  @  3

+ 

> CHANGE  3 : 6  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ 

> DELETE  11  @  11 : 16

> CHANGE  30 : 31  @  30 : 31

~ 						HString.format("%d,%d", new Object[] { Integer.valueOf(i), Integer.valueOf(j) }));

> CHANGE  15 : 16  @  15 : 16

~ 	public boolean generateStructure(World worldIn, EaglercraftRandom randomIn, ChunkCoordIntPair chunkCoord) {

> CHANGE  86 : 87  @  86 : 87

~ 			List<BlockPos> list = this.getCoordList();

> CHANGE  3 : 5  @  3 : 4

~ 				for (int m = 0, n = list.size(); m < n; ++m) {
~ 					BlockPos blockpos3 = list.get(m);

> EOF
