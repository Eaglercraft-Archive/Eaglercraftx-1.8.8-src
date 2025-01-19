
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 7  @  2 : 6

~ import com.carrotsearch.hppc.LongArrayList;
~ import com.carrotsearch.hppc.LongObjectHashMap;
~ import com.carrotsearch.hppc.LongObjectMap;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> DELETE  7  @  7 : 8

> DELETE  1  @  1 : 3

> CHANGE  3 : 6  @  3 : 6

~ 	private final EaglercraftRandom random;
~ 	private final LongObjectMap<Teleporter.PortalPosition> destinationCoordinateCache = new LongObjectHashMap<>();
~ 	private final LongArrayList destinationCoordinateKeys = new LongArrayList();

> CHANGE  3 : 4  @  3 : 4

~ 		this.random = new EaglercraftRandom(worldIn.getSeed(), !worldIn.getWorldInfo().isOldEaglercraftRandom());

> CHANGE  41 : 43  @  41 : 44

~ 		if (this.destinationCoordinateCache.containsKey(k)) {
~ 			Teleporter.PortalPosition teleporter$portalposition = this.destinationCoordinateCache.get(k);

> CHANGE  33 : 34  @  33 : 34

~ 				this.destinationCoordinateCache.put(k,

> CHANGE  74 : 75  @  74 : 75

~ 		BlockPos blockpos$mutableblockpos = new BlockPos();

> DELETE  165  @  165 : 166

> CHANGE  2 : 5  @  2 : 6

~ 			for (int j = 0; j < destinationCoordinateKeys.size(); ++j) {
~ 				long olong = destinationCoordinateKeys.get(j);
~ 				Teleporter.PortalPosition teleporter$portalposition = this.destinationCoordinateCache.get(olong);

> CHANGE  1 : 3  @  1 : 3

~ 					destinationCoordinateKeys.removeAt(j--);
~ 					this.destinationCoordinateCache.remove(olong);

> EOF
