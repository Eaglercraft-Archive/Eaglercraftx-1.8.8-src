
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 6  @  2 : 3

~ import com.carrotsearch.hppc.LongObjectHashMap;
~ import com.carrotsearch.hppc.LongObjectMap;
~ import com.carrotsearch.hppc.cursors.ObjectCursor;
~ 

> CHANGE  2 : 5  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ 

> DELETE  11  @  11 : 16

> CHANGE  3 : 4  @  3 : 4

~ 	protected LongObjectMap<StructureStart> structureMap = new LongObjectHashMap<>();

> INSERT  3 : 11  @  3

+ 	public MapGenStructure() {
+ 		super();
+ 	}
+ 
+ 	public MapGenStructure(boolean scramble) {
+ 		super(scramble);
+ 	}
+ 

> CHANGE  3 : 4  @  3 : 4

~ 		if (!this.structureMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(i, j))) {

> CHANGE  5 : 6  @  5 : 6

~ 					this.structureMap.put(ChunkCoordIntPair.chunkXZ2Int(i, j), structurestart);

> CHANGE  13 : 14  @  13 : 14

~ 						HString.format("%d,%d", new Object[] { Integer.valueOf(i), Integer.valueOf(j) }));

> CHANGE  15 : 16  @  15 : 16

~ 	public boolean generateStructure(World worldIn, EaglercraftRandom randomIn, ChunkCoordIntPair chunkCoord) {

> CHANGE  5 : 7  @  5 : 6

~ 		for (ObjectCursor<StructureStart> structurestart_ : this.structureMap.values()) {
~ 			StructureStart structurestart = structurestart_.value;

> CHANGE  18 : 20  @  18 : 19

~ 		label24: for (ObjectCursor<StructureStart> structurestart_ : this.structureMap.values()) {
~ 			StructureStart structurestart = structurestart_.value;

> CHANGE  24 : 26  @  24 : 25

~ 		for (ObjectCursor<StructureStart> structurestart_ : this.structureMap.values()) {
~ 			StructureStart structurestart = structurestart_.value;

> CHANGE  21 : 23  @  21 : 22

~ 		for (ObjectCursor<StructureStart> structurestart_ : this.structureMap.values()) {
~ 			StructureStart structurestart = structurestart_.value;

> CHANGE  14 : 15  @  14 : 15

~ 			List<BlockPos> list = this.getCoordList();

> CHANGE  3 : 5  @  3 : 4

~ 				for (int m = 0, n = list.size(); m < n; ++m) {
~ 					BlockPos blockpos3 = list.get(m);

> CHANGE  38 : 39  @  38 : 40

~ 								this.structureMap.put(ChunkCoordIntPair.chunkXZ2Int(i, j), structurestart);

> EOF
