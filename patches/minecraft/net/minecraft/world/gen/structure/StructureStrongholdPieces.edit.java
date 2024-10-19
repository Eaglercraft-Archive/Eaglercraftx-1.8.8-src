
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 5  @  4 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> DELETE  13  @  13 : 16

> CHANGE  27 : 53  @  27 : 40

~ 		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.ChestCorridor.class,
~ 				StructureStrongholdPieces.ChestCorridor::new, "SHCC");
~ 		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Corridor.class,
~ 				StructureStrongholdPieces.Corridor::new, "SHFC");
~ 		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Crossing.class,
~ 				StructureStrongholdPieces.Crossing::new, "SH5C");
~ 		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.LeftTurn.class,
~ 				StructureStrongholdPieces.LeftTurn::new, "SHLT");
~ 		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Library.class,
~ 				StructureStrongholdPieces.Library::new, "SHLi");
~ 		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.PortalRoom.class,
~ 				StructureStrongholdPieces.PortalRoom::new, "SHPR");
~ 		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Prison.class,
~ 				StructureStrongholdPieces.Prison::new, "SHPH");
~ 		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.RightTurn.class,
~ 				StructureStrongholdPieces.RightTurn::new, "SHRT");
~ 		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.RoomCrossing.class,
~ 				StructureStrongholdPieces.RoomCrossing::new, "SHRC");
~ 		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Stairs.class,
~ 				StructureStrongholdPieces.Stairs::new, "SHSD");
~ 		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Stairs2.class,
~ 				StructureStrongholdPieces.Stairs2::new, "SHStart");
~ 		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Straight.class,
~ 				StructureStrongholdPieces.Straight::new, "SHS");
~ 		MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.StairsStraight.class,
~ 				StructureStrongholdPieces.StairsStraight::new, "SHSSD");

> CHANGE  31 : 32  @  31 : 32

~ 			EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing, int parInt4) {

> CHANGE  40 : 41  @  40 : 41

~ 			List<StructureComponent> parList, EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3,

> CHANGE  54 : 55  @  54 : 55

~ 			List<StructureComponent> parList, EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3,

> CHANGE  43 : 44  @  43 : 44

~ 		public ChestCorridor(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,

> CHANGE  18 : 19  @  18 : 19

~ 				EaglercraftRandom random) {

> CHANGE  4 : 6  @  4 : 5

~ 				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
~ 				int parInt4) {

> CHANGE  9 : 11  @  9 : 10

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  49 : 50  @  49 : 50

~ 		public Corridor(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,

> CHANGE  19 : 20  @  19 : 20

~ 		public static StructureBoundingBox func_175869_a(List<StructureComponent> parList, EaglercraftRandom parRandom,

> CHANGE  23 : 25  @  23 : 24

~ 		public boolean addComponentParts(World world, EaglercraftRandom var2,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  39 : 40  @  39 : 40

~ 		public Crossing(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,

> CHANGE  28 : 29  @  28 : 29

~ 				EaglercraftRandom random) {

> CHANGE  27 : 29  @  27 : 28

~ 				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
~ 				int parInt4) {

> CHANGE  9 : 11  @  9 : 10

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  64 : 65  @  64 : 65

~ 		public LeftTurn(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,

> CHANGE  8 : 9  @  8 : 9

~ 				EaglercraftRandom random) {

> CHANGE  9 : 11  @  9 : 10

~ 				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
~ 				int parInt4) {

> CHANGE  9 : 11  @  9 : 10

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  30 : 31  @  30 : 31

~ 		public Library(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,

> CHANGE  19 : 21  @  19 : 20

~ 				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
~ 				int parInt4) {

> CHANGE  15 : 17  @  15 : 16

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  154 : 155  @  154 : 155

~ 		public PortalRoom(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,

> CHANGE  16 : 18  @  16 : 17

~ 		public void buildComponent(StructureComponent structurecomponent, List<StructureComponent> var2,
~ 				EaglercraftRandom var3) {

> CHANGE  7 : 9  @  7 : 8

~ 				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
~ 				int parInt4) {

> CHANGE  9 : 11  @  9 : 10

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  124 : 125  @  124 : 125

~ 		public Prison(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,

> CHANGE  8 : 9  @  8 : 9

~ 				EaglercraftRandom random) {

> CHANGE  3 : 6  @  3 : 5

~ 		public static StructureStrongholdPieces.Prison func_175860_a(List<StructureComponent> parList,
~ 				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
~ 				int parInt4) {

> CHANGE  9 : 11  @  9 : 10

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  41 : 42  @  41 : 42

~ 				EaglercraftRandom random) {

> CHANGE  8 : 10  @  8 : 9

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  33 : 34  @  33 : 34

~ 		public RoomCrossing(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,

> CHANGE  19 : 20  @  19 : 20

~ 				EaglercraftRandom random) {

> CHANGE  6 : 8  @  6 : 7

~ 				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
~ 				int parInt4) {

> CHANGE  9 : 11  @  9 : 10

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  118 : 119  @  118 : 119

~ 		public Stairs(int parInt1, EaglercraftRandom parRandom, int parInt2, int parInt3) {

> CHANGE  15 : 16  @  15 : 16

~ 		public Stairs(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,

> CHANGE  19 : 20  @  19 : 20

~ 				EaglercraftRandom random) {

> CHANGE  7 : 10  @  7 : 9

~ 		public static StructureStrongholdPieces.Stairs func_175863_a(List<StructureComponent> parList,
~ 				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
~ 				int parInt4) {

> CHANGE  9 : 11  @  9 : 10

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  50 : 51  @  50 : 51

~ 		public Stairs2(int parInt1, EaglercraftRandom parRandom, int parInt2, int parInt3) {

> CHANGE  13 : 14  @  13 : 14

~ 		public StairsStraight(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,

> CHANGE  8 : 9  @  8 : 9

~ 				EaglercraftRandom random) {

> CHANGE  4 : 6  @  4 : 5

~ 				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
~ 				int parInt4) {

> CHANGE  9 : 11  @  9 : 10

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  36 : 37  @  36 : 37

~ 		public void selectBlocks(EaglercraftRandom random, int var2, int var3, int var4, boolean flag) {

> CHANGE  26 : 27  @  26 : 27

~ 		public Straight(int parInt1, EaglercraftRandom parRandom, StructureBoundingBox parStructureBoundingBox,

> CHANGE  22 : 23  @  22 : 23

~ 				EaglercraftRandom random) {

> CHANGE  12 : 14  @  12 : 13

~ 				EaglercraftRandom parRandom, int parInt1, int parInt2, int parInt3, EnumFacing parEnumFacing,
~ 				int parInt4) {

> CHANGE  9 : 11  @  9 : 10

~ 		public boolean addComponentParts(World world, EaglercraftRandom random,
~ 				StructureBoundingBox structureboundingbox) {

> CHANGE  50 : 53  @  50 : 52

~ 		protected void placeDoor(World worldIn, EaglercraftRandom parRandom,
~ 				StructureBoundingBox parStructureBoundingBox, StructureStrongholdPieces.Stronghold.Door parDoor,
~ 				int parInt1, int parInt2, int parInt3) {

> CHANGE  75 : 76  @  75 : 76

~ 		protected StructureStrongholdPieces.Stronghold.Door getRandomDoor(EaglercraftRandom parRandom) {

> CHANGE  16 : 17  @  16 : 17

~ 				List<StructureComponent> parList, EaglercraftRandom parRandom, int parInt1, int parInt2) {

> CHANGE  25 : 26  @  25 : 26

~ 				List<StructureComponent> parList, EaglercraftRandom parRandom, int parInt1, int parInt2) {

> CHANGE  25 : 26  @  25 : 26

~ 				List<StructureComponent> parList, EaglercraftRandom parRandom, int parInt1, int parInt2) {

> EOF
