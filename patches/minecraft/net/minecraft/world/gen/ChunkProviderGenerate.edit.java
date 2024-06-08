
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> DELETE  15  @  15 : 21

> CHANGE  9 : 10  @  9 : 10

~ 	private EaglercraftRandom rand;

> CHANGE  15 : 22  @  15 : 22

~ 	private MapGenBase caveGenerator;
~ 	private MapGenStronghold strongholdGenerator;
~ 	private MapGenVillage villageGenerator;
~ 	private MapGenMineshaft mineshaftGenerator;
~ 	private MapGenScatteredFeature scatteredFeatureGenerator;
~ 	private MapGenBase ravineGenerator;
~ 	private StructureOceanMonument oceanMonumentGenerator;

> CHANGE  10 : 19  @  10 : 11

~ 		boolean scramble = !worldIn.getWorldInfo().isOldEaglercraftRandom();
~ 		this.rand = new EaglercraftRandom(parLong1, scramble);
~ 		this.caveGenerator = new MapGenCaves(scramble);
~ 		this.strongholdGenerator = new MapGenStronghold(scramble);
~ 		this.villageGenerator = new MapGenVillage(scramble);
~ 		this.mineshaftGenerator = new MapGenMineshaft(scramble);
~ 		this.scatteredFeatureGenerator = new MapGenScatteredFeature(scramble);
~ 		this.ravineGenerator = new MapGenRavine(scramble);
~ 		this.oceanMonumentGenerator = new StructureOceanMonument(scramble);

> EOF
