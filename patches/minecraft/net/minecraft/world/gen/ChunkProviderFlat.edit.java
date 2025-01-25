
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  5 : 6  @  5 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> DELETE  11  @  11 : 14

> CHANGE  11 : 12  @  11 : 12

~ 	private EaglercraftRandom random;

> CHANGE  10 : 12  @  10 : 11

~ 		boolean scramble = !worldIn.getWorldInfo().isOldEaglercraftRandom();
~ 		this.random = new EaglercraftRandom(seed, scramble);

> CHANGE  9 : 10  @  9 : 10

~ 				this.structureGenerators.add(new MapGenVillage(map1, scramble));

> CHANGE  3 : 4  @  3 : 4

~ 				this.structureGenerators.add(new MapGenScatteredFeature((Map) map.get("biome_1"), scramble));

> CHANGE  3 : 4  @  3 : 4

~ 				this.structureGenerators.add(new MapGenMineshaft((Map) map.get("mineshaft"), scramble));

> CHANGE  3 : 4  @  3 : 4

~ 				this.structureGenerators.add(new MapGenStronghold((Map) map.get("stronghold"), scramble));

> CHANGE  3 : 4  @  3 : 4

~ 				this.structureGenerators.add(new StructureOceanMonument((Map) map.get("oceanmonument"), scramble));

> CHANGE  84 : 86  @  84 : 85

~ 		for (int m = 0, n = this.structureGenerators.size(); m < n; ++m) {
~ 			MapGenStructure mapgenstructure = this.structureGenerators.get(m);

> CHANGE  63 : 65  @  63 : 64

~ 			for (int m = 0, n = this.structureGenerators.size(); m < n; ++m) {
~ 				MapGenStructure mapgenstructure = this.structureGenerators.get(m);

> CHANGE  14 : 16  @  14 : 16

~ 		for (int m = 0, n = this.structureGenerators.size(); m < n; ++m) {
~ 			this.structureGenerators.get(m).generate(this, this.worldObj, i, j, (ChunkPrimer) null);

> INSERT  7 : 11  @  7

+ 
+ 	public Chunk getLoadedChunk(int var1, int var2) {
+ 		return provideChunk(var1, var2);
+ 	}

> EOF
