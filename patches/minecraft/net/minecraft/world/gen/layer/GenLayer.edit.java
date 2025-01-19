
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 5  @  3

+ 
+ import net.lax1dude.eaglercraft.v1_8.sp.server.GenLayerEaglerRivers;

> DELETE  6  @  6 : 25

> CHANGE  2 : 3  @  2 : 3

~ 	protected long worldGenSeed;

> CHANGE  43 : 44  @  43 : 44

~ 		GenLayer genlayerhills = new GenLayerHills(1000L, genlayerbiomeedge, genlayer1);

> CHANGE  2 : 5  @  2 : 4

~ 		GenLayer genlayerriver = new GenLayerRiver(1L, genlayer3);
~ 		genlayerriver = new GenLayerSmooth(1000L, genlayerriver);
~ 		genlayerriver = new GenLayerEaglerRivers(69L, genlayerriver);

> CHANGE  14 : 15  @  14 : 15

~ 		GenLayerRiverMix genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayerriver);

> EOF
