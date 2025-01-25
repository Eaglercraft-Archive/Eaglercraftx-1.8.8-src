
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> DELETE  13  @  13 : 14

> CHANGE  2 : 3  @  2 : 3

~ 	private EaglercraftRandom endRNG;

> CHANGE  16 : 17  @  16 : 17

~ 		this.endRNG = new EaglercraftRandom(parLong1, !worldIn.getWorldInfo().isOldEaglercraftRandom());

> INSERT  246 : 250  @  246

+ 
+ 	public Chunk getLoadedChunk(int var1, int var2) {
+ 		return provideChunk(var1, var2);
+ 	}

> EOF
