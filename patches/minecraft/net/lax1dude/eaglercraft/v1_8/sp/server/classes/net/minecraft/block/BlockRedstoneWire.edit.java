
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  6 : 7  @  6 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  31 : 35  @  31 : 39

~ 	public static PropertyEnum<BlockRedstoneWire.EnumAttachPosition> NORTH;
~ 	public static PropertyEnum<BlockRedstoneWire.EnumAttachPosition> EAST;
~ 	public static PropertyEnum<BlockRedstoneWire.EnumAttachPosition> SOUTH;
~ 	public static PropertyEnum<BlockRedstoneWire.EnumAttachPosition> WEST;

> INSERT  14 : 25  @  14

+ 	public static void bootstrapStates() {
+ 		NORTH = PropertyEnum.<BlockRedstoneWire.EnumAttachPosition>create("north",
+ 				BlockRedstoneWire.EnumAttachPosition.class);
+ 		EAST = PropertyEnum.<BlockRedstoneWire.EnumAttachPosition>create("east",
+ 				BlockRedstoneWire.EnumAttachPosition.class);
+ 		SOUTH = PropertyEnum.<BlockRedstoneWire.EnumAttachPosition>create("south",
+ 				BlockRedstoneWire.EnumAttachPosition.class);
+ 		WEST = PropertyEnum.<BlockRedstoneWire.EnumAttachPosition>create("west",
+ 				BlockRedstoneWire.EnumAttachPosition.class);
+ 	}
+ 

> CHANGE  52 : 53  @  52 : 53

~ 		for (BlockPos blockpos : (ArrayList<BlockPos>) arraylist) {

> CHANGE  78 : 79  @  78 : 79

~ 		{

> DELETE  18  @  18 : 19

> CHANGE  5 : 6  @  5 : 6

~ 		{

> DELETE  18  @  18 : 19

> CHANGE  13 : 18  @  13 : 21

~ 		if (this.canPlaceBlockAt(world, blockpos)) {
~ 			this.updateSurroundingRedstone(world, blockpos, iblockstate);
~ 		} else {
~ 			this.dropBlockAsItem(world, blockpos, iblockstate, 0);
~ 			world.setBlockToAir(blockpos);

> CHANGE  3 : 4  @  3 : 4

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> CHANGE  99 : 100  @  99 : 100

~ 	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> EOF
