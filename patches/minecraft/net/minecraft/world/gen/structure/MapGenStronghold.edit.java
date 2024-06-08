
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  6 : 7  @  6 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> DELETE  6  @  6 : 10

> CHANGE  8 : 10  @  8 : 9

~ 	public MapGenStronghold(boolean scramble) {
~ 		super(scramble);

> CHANGE  5 : 8  @  5 : 6

~ 		BiomeGenBase[] biomes = BiomeGenBase.getBiomeGenArray();
~ 		for (int i = 0; i < biomes.length; ++i) {
~ 			BiomeGenBase biomegenbase = biomes[i];

> CHANGE  7 : 9  @  7 : 9

~ 	public MapGenStronghold(Map<String, String> parMap, boolean scramble) {
~ 		this(scramble);

> CHANGE  22 : 23  @  22 : 23

~ 			EaglercraftRandom random = new EaglercraftRandom(!this.worldObj.getWorldInfo().isOldEaglercraftRandom());

> CHANGE  26 : 28  @  26 : 27

~ 		for (int l = 0; l < this.structureCoords.length; ++l) {
~ 			ChunkCoordIntPair chunkcoordintpair = this.structureCoords[l];

> CHANGE  11 : 13  @  11 : 12

~ 		for (int l = 0; l < this.structureCoords.length; ++l) {
~ 			ChunkCoordIntPair chunkcoordintpair = this.structureCoords[l];

> CHANGE  25 : 26  @  25 : 26

~ 		public Start(World worldIn, EaglercraftRandom parRandom, int parInt1, int parInt2) {

> EOF
