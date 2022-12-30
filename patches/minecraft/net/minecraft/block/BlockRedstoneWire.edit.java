
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  2 : 3  @  4 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  2 : 6  @  2 : 5

~ 
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Sets;
~ 

> CHANGE  24 : 28  @  23 : 31

~ 	public static PropertyEnum<BlockRedstoneWire.EnumAttachPosition> NORTH;
~ 	public static PropertyEnum<BlockRedstoneWire.EnumAttachPosition> EAST;
~ 	public static PropertyEnum<BlockRedstoneWire.EnumAttachPosition> SOUTH;
~ 	public static PropertyEnum<BlockRedstoneWire.EnumAttachPosition> WEST;

> INSERT  18 : 29  @  22

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

> CHANGE  60 : 61  @  49 : 50

~ 		ArrayList<BlockPos> arraylist = Lists.newArrayList(this.blocksNeedingUpdate);

> DELETE  81  @  81 : 130

> CHANGE  9 : 10  @  58 : 71

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> CHANGE  100 : 101  @  112 : 113

~ 	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> EOF
