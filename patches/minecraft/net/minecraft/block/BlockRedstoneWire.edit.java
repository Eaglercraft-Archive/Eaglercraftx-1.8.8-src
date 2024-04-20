
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

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

> CHANGE  11 : 15  @  11 : 14

~ 		BlockPos posTmp = new BlockPos(0, 0, 0);
~ 		Block block = worldIn.getBlockState(blockpos).getBlock();
~ 		if (!canConnectTo(worldIn.getBlockState(blockpos), direction) && (block.isBlockNormalCube()
~ 				|| !canConnectUpwardsTo(worldIn.getBlockState(blockpos.offsetEvenFaster(EnumFacing.DOWN, posTmp))))) {

> CHANGE  2 : 3  @  2 : 3

~ 					&& canConnectUpwardsTo(worldIn.getBlockState(blockpos.offsetEvenFaster(EnumFacing.UP, posTmp)))

> CHANGE  26 : 29  @  26 : 28

~ 		BlockPos fuckOff = blockpos.down();
~ 		return World.doesBlockHaveSolidTopSurface(world, fuckOff)
~ 				|| world.getBlockState(fuckOff).getBlock() == Blocks.glowstone;

> CHANGE  4 : 5  @  4 : 5

~ 		ArrayList<BlockPos> arraylist = Lists.newArrayList(this.blocksNeedingUpdate);

> CHANGE  2 : 4  @  2 : 4

~ 		for (int i = 0, l = arraylist.size(); i < l; ++i) {
~ 			worldIn.notifyNeighborsOfStateChange(arraylist.get(i), this);

> CHANGE  19 : 25  @  19 : 22

~ 		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 		BlockPos tmp = new BlockPos(0, 0, 0);
~ 		for (int m = 0; m < facings.length; ++m) {
~ 			EnumFacing enumfacing = facings[m];
~ 			BlockPos blockpos = pos1.offsetEvenFaster(enumfacing, tmp);
~ 			boolean flag = blockpos.x != pos2.x || blockpos.z != pos2.z;

> CHANGE  7 : 9  @  7 : 8

~ 					++blockpos.y;
~ 					l = this.getMaxCurrentStrength(worldIn, blockpos, l);

> CHANGE  3 : 5  @  3 : 4

~ 				--blockpos.y;
~ 				l = this.getMaxCurrentStrength(worldIn, blockpos, l);

> CHANGE  23 : 26  @  23 : 25

~ 			facings = EnumFacing._VALUES;
~ 			for (int m = 0; m < facings.length; ++m) {
~ 				this.blocksNeedingUpdate.add(pos1.offset(facings[m]));

> CHANGE  10 : 14  @  10 : 12

~ 			EnumFacing[] facings = EnumFacing._VALUES;
~ 			BlockPos tmp = new BlockPos(0, 0, 0);
~ 			for (int i = 0; i < facings.length; ++i) {
~ 				worldIn.notifyNeighborsOfStateChange(pos.offsetEvenFaster(facings[i], tmp), this);

> CHANGE  9 : 13  @  9 : 11

~ 			BlockPos tmp = new BlockPos(0, 0, 0);
~ 			EnumFacing[] facings = EnumFacing.Plane.VERTICAL.facingsArray;
~ 			for (int i = 0; i < facings.length; ++i) {
~ 				world.notifyNeighborsOfStateChange(blockpos.offsetEvenFaster(facings[i], tmp), this);

> CHANGE  2 : 5  @  2 : 4

~ 			facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 			for (int i = 0; i < facings.length; ++i) {
~ 				this.notifyWireNeighborsOfStateChange(world, blockpos.offsetEvenFaster(facings[i], tmp));

> CHANGE  2 : 4  @  2 : 4

~ 			for (int i = 0; i < facings.length; ++i) {
~ 				BlockPos blockpos1 = blockpos.offsetEvenFaster(facings[i], tmp);

> CHANGE  1 : 3  @  1 : 2

~ 					++blockpos1.y;
~ 					this.notifyWireNeighborsOfStateChange(world, blockpos1);

> CHANGE  1 : 3  @  1 : 2

~ 					--blockpos1.y;
~ 					this.notifyWireNeighborsOfStateChange(world, blockpos1);

> DELETE  2  @  2 : 3

> CHANGE  6 : 10  @  6 : 8

~ 			BlockPos tmp = new BlockPos(0, 0, 0);
~ 			EnumFacing[] facings = EnumFacing._VALUES;
~ 			for (int i = 0; i < facings.length; ++i) {
~ 				world.notifyNeighborsOfStateChange(blockpos.offsetEvenFaster(facings[i], tmp), this);

> CHANGE  4 : 7  @  4 : 6

~ 			facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 			for (int i = 0; i < facings.length; ++i) {
~ 				this.notifyWireNeighborsOfStateChange(world, blockpos.offsetEvenFaster(facings[i], tmp));

> CHANGE  2 : 5  @  2 : 4

~ 			facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 			for (int i = 0; i < facings.length; ++i) {
~ 				BlockPos blockpos1 = blockpos.offsetEvenFaster(facings[i], tmp);

> CHANGE  1 : 3  @  1 : 2

~ 					++blockpos1.y;
~ 					this.notifyWireNeighborsOfStateChange(world, blockpos1);

> CHANGE  1 : 3  @  1 : 2

~ 					--blockpos1.y;
~ 					this.notifyWireNeighborsOfStateChange(world, blockpos1);

> DELETE  2  @  2 : 3

> DELETE  20  @  20 : 21

> CHANGE  3 : 4  @  3 : 4

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> CHANGE  21 : 24  @  21 : 22

~ 				EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 				for (int j = 0; j < facings.length; ++j) {
~ 					EnumFacing enumfacing1 = facings[j];

> CHANGE  77 : 78  @  77 : 78

~ 	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> EOF
