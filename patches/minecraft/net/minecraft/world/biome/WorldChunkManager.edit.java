
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 5  @  4 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> DELETE  6  @  6 : 8

> INSERT  47 : 51  @  47

+ 	public BiomeGenBase getBiomeGenerator(int x, int z, BiomeGenBase biomeGenBaseIn) {
+ 		return this.biomeCache.func_180284_a(x, z, biomeGenBaseIn);
+ 	}
+ 

> CHANGE  120 : 121  @  120 : 121

~ 	public BlockPos findBiomePosition(int x, int z, int range, List<BiomeGenBase> biomes, EaglercraftRandom random) {

> EOF
