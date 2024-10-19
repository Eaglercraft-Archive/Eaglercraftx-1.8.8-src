
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  6 : 7  @  6 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> DELETE  20  @  20 : 24

> CHANGE  3 : 29  @  3 : 16

~ 		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House1.class,
~ 				StructureVillagePieces.House1::new, "ViBH");
~ 		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Field1.class,
~ 				StructureVillagePieces.Field1::new, "ViDF");
~ 		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Field2.class,
~ 				StructureVillagePieces.Field2::new, "ViF");
~ 		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Torch.class,
~ 				StructureVillagePieces.Torch::new, "ViL");
~ 		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Hall.class,
~ 				StructureVillagePieces.Hall::new, "ViPH");
~ 		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House4Garden.class,
~ 				StructureVillagePieces.House4Garden::new, "ViSH");
~ 		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.WoodHut.class,
~ 				StructureVillagePieces.WoodHut::new, "ViSmH");
~ 		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Church.class,
~ 				StructureVillagePieces.Church::new, "ViST");
~ 		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House2.class,
~ 				StructureVillagePieces.House2::new, "ViS");
~ 		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Start.class,
~ 				StructureVillagePieces.Start::new, "ViStart");
~ 		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Path.class,
~ 				StructureVillagePieces.Path::new, "ViSR");
~ 		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House3.class,
~ 				StructureVillagePieces.House3::new, "ViTRH");
~ 		MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Well.class,
~ 				StructureVillagePieces.Well::new, "ViW");

> CHANGE  2 : 4  @  2 : 4

~ 	public static List<StructureVillagePieces.PieceWeight> getStructureVillageWeightedPieceList(
~ 			EaglercraftRandom random, int parInt1) {

> CHANGE  47 : 49  @  47 : 49

~ 			StructureVillagePieces.PieceWeight weight, List<StructureComponent> rand, EaglercraftRandom facing,
~ 			int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing, int parInt4) {

> CHANGE  35 : 36  @  35 : 36

~ 			List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,

> CHANGE  47 : 48  @  47 : 48

~ 			EaglercraftRandom facing, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing, int parInt4) {

> CHANGE  30 : 31  @  30 : 31

~ 			EaglercraftRandom facing, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing, int parInt4) {

> CHANGE  33 : 34  @  33 : 34

~ 		public Church(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,

> CHANGE  7 : 8  @  7 : 8

~ 				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,

> CHANGE  10 : 12  @  10 : 11

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  131 : 132  @  131 : 132

~ 		public Field1(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,

> CHANGE  26 : 27  @  26 : 27

~ 		private Block func_151559_a(EaglercraftRandom rand) {

> CHANGE  11 : 12  @  11 : 12

~ 				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,

> CHANGE  10 : 12  @  10 : 11

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  80 : 81  @  80 : 81

~ 		public Field2(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,

> CHANGE  20 : 21  @  20 : 21

~ 		private Block func_151560_a(EaglercraftRandom rand) {

> CHANGE  11 : 12  @  11 : 12

~ 				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,

> CHANGE  10 : 12  @  10 : 11

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  57 : 58  @  57 : 58

~ 		public Hall(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,

> CHANGE  7 : 8  @  7 : 8

~ 				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,

> CHANGE  10 : 12  @  10 : 11

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  127 : 128  @  127 : 128

~ 		public House1(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,

> CHANGE  7 : 8  @  7 : 8

~ 				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,

> CHANGE  10 : 12  @  10 : 11

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  146 : 147  @  146 : 147

~ 		public House2(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,

> CHANGE  7 : 8  @  7 : 8

~ 				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,

> CHANGE  20 : 22  @  20 : 21

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  104 : 105  @  104 : 105

~ 		public House3(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,

> CHANGE  7 : 8  @  7 : 8

~ 				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,

> CHANGE  10 : 12  @  10 : 11

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  159 : 160  @  159 : 160

~ 		public House4Garden(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,

> CHANGE  18 : 19  @  18 : 19

~ 				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,

> CHANGE  8 : 10  @  8 : 9

~ 		public boolean addComponentParts(World world, EaglercraftRandom var2,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  104 : 105  @  104 : 105

~ 		public Path(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,

> CHANGE  18 : 19  @  18 : 19

~ 				EaglercraftRandom random) {

> CHANGE  73 : 74  @  73 : 74

~ 				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,

> CHANGE  12 : 14  @  12 : 13

~ 		public boolean addComponentParts(World world, EaglercraftRandom var2,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  60 : 61  @  60 : 61

~ 		public Start(WorldChunkManager chunkManagerIn, int parInt1, EaglercraftRandom rand, int parInt2, int parInt3,

> CHANGE  20 : 21  @  20 : 21

~ 		public Torch(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,

> CHANGE  7 : 8  @  7 : 8

~ 				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,

> CHANGE  7 : 9  @  7 : 8

~ 		public boolean addComponentParts(World world, EaglercraftRandom var2,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  62 : 63  @  62 : 63

~ 				List<StructureComponent> rand, EaglercraftRandom parRandom, int parInt1, int parInt2) {

> CHANGE  25 : 26  @  25 : 26

~ 				List<StructureComponent> rand, EaglercraftRandom parRandom, int parInt1, int parInt2) {

> CHANGE  138 : 139  @  138 : 139

~ 		public Well(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand, int parInt2, int parInt3) {

> CHANGE  14 : 15  @  14 : 15

~ 				EaglercraftRandom random) {

> CHANGE  14 : 16  @  14 : 15

~ 		public boolean addComponentParts(World world, EaglercraftRandom var2,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  46 : 47  @  46 : 47

~ 		public WoodHut(StructureVillagePieces.Start start, int parInt1, EaglercraftRandom rand,

> CHANGE  21 : 22  @  21 : 22

~ 				List<StructureComponent> rand, EaglercraftRandom facing, int parInt1, int parInt2, int parInt3,

> CHANGE  10 : 12  @  10 : 11

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> EOF
