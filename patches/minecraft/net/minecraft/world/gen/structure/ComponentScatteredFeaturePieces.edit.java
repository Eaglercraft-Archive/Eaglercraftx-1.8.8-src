
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 5  @  4 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> DELETE  18  @  18 : 21

> CHANGE  3 : 9  @  3 : 6

~ 		MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.DesertPyramid.class,
~ 				ComponentScatteredFeaturePieces.DesertPyramid::new, "TeDP");
~ 		MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.JunglePyramid.class,
~ 				ComponentScatteredFeaturePieces.JunglePyramid::new, "TeJP");
~ 		MapGenStructureIO.registerStructureComponent(ComponentScatteredFeaturePieces.SwampHut.class,
~ 				ComponentScatteredFeaturePieces.SwampHut::new, "TeSH");

> CHANGE  19 : 20  @  19 : 20

~ 		public DesertPyramid(EaglercraftRandom parRandom, int parInt1, int parInt2) {

> CHANGE  19 : 21  @  19 : 20

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  352 : 355  @  352 : 353

~ 			EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 			for (int m = 0; m < facings.length; ++m) {
~ 				EnumFacing enumfacing = facings[m];

> CHANGE  24 : 25  @  24 : 25

~ 		protected Feature(EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, int parInt4, int parInt5,

> CHANGE  39 : 40  @  39 : 40

~ 				BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  46 : 47  @  46 : 47

~ 		public JunglePyramid(EaglercraftRandom parRandom, int parInt1, int parInt2) {

> CHANGE  19 : 21  @  19 : 20

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  281 : 282  @  281 : 282

~ 			public void selectBlocks(EaglercraftRandom rand, int x, int y, int z, boolean parFlag) {

> CHANGE  16 : 17  @  16 : 17

~ 		public SwampHut(EaglercraftRandom parRandom, int parInt1, int parInt2) {

> CHANGE  13 : 15  @  13 : 14

~ 		public boolean addComponentParts(World world, EaglercraftRandom var2,
~ 				StructureBoundingBox structureboundingbox) {

> EOF
