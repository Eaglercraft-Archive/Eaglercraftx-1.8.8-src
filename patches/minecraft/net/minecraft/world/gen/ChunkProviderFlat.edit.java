
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  5 : 6  @  5 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> DELETE  11  @  11 : 14

> CHANGE  11 : 12  @  11 : 12

~ 	private EaglercraftRandom random;

> CHANGE  10 : 11  @  10 : 11

~ 		this.random = new EaglercraftRandom(seed);

> CHANGE  110 : 112  @  110 : 111

~ 		for (int m = 0, n = this.structureGenerators.size(); m < n; ++m) {
~ 			MapGenStructure mapgenstructure = this.structureGenerators.get(m);

> CHANGE  63 : 65  @  63 : 64

~ 			for (int m = 0, n = this.structureGenerators.size(); m < n; ++m) {
~ 				MapGenStructure mapgenstructure = this.structureGenerators.get(m);

> CHANGE  14 : 16  @  14 : 16

~ 		for (int m = 0, n = this.structureGenerators.size(); m < n; ++m) {
~ 			this.structureGenerators.get(m).generate(this, this.worldObj, i, j, (ChunkPrimer) null);

> EOF
