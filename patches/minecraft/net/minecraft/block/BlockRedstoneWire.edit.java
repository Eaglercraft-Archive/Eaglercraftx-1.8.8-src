
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> CHANGE  4 : 5  @  6 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  6 : 10  @  8 : 11

~ 
~ import com.google.common.collect.Lists;
~ import com.google.common.collect.Sets;
~ 

> CHANGE  30 : 34  @  31 : 39

~ 	public static PropertyEnum<BlockRedstoneWire.EnumAttachPosition> NORTH;
~ 	public static PropertyEnum<BlockRedstoneWire.EnumAttachPosition> EAST;
~ 	public static PropertyEnum<BlockRedstoneWire.EnumAttachPosition> SOUTH;
~ 	public static PropertyEnum<BlockRedstoneWire.EnumAttachPosition> WEST;

> INSERT  48 : 59  @  53

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

> CHANGE  108 : 109  @  102 : 103

~ 		ArrayList<BlockPos> arraylist = Lists.newArrayList(this.blocksNeedingUpdate);

> DELETE  189  @  183 : 232

> CHANGE  198 : 199  @  241 : 254

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> CHANGE  298 : 299  @  353 : 354

~ 	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> EOF
