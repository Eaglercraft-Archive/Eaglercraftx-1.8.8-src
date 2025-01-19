
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> DELETE  15  @  15 : 19

> CHANGE  11 : 12  @  11 : 12

~ 	private final EaglercraftRandom hellRNG;

> CHANGE  20 : 22  @  20 : 22

~ 	private final MapGenNetherBridge genNetherBridge;
~ 	private final MapGenBase netherCaveGenerator;

> CHANGE  9 : 13  @  9 : 10

~ 		boolean scramble = !worldIn.getWorldInfo().isOldEaglercraftRandom();
~ 		this.hellRNG = new EaglercraftRandom(parLong1, scramble);
~ 		this.genNetherBridge = new MapGenNetherBridge(scramble);
~ 		this.netherCaveGenerator = new MapGenCavesHell(scramble);

> EOF
