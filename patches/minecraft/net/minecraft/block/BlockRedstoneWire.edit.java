
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  1 : 5  @  1 : 4

~ 
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Sets;
~ 

> CHANGE  20 : 24  @  20 : 28

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

> CHANGE  49 : 50  @  49 : 50

~ 		ArrayList<BlockPos> arraylist = Lists.newArrayList(this.blocksNeedingUpdate);

> DELETE  80  @  80 : 129

> CHANGE  9 : 10  @  9 : 22

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> CHANGE  99 : 100  @  99 : 100

~ 	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> EOF
