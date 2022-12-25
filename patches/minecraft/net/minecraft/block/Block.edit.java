
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 135

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> DELETE  14  @  144 : 146

> CHANGE  357 : 358  @  489 : 490

~ 	public void randomTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> CHANGE  361 : 362  @  493 : 494

~ 	public void updateTick(World var1, BlockPos var2, IBlockState var3, EaglercraftRandom var4) {

> CHANGE  364 : 365  @  496 : 497

~ 	public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, EaglercraftRandom rand) {

> CHANGE  383 : 384  @  515 : 516

~ 	public int quantityDropped(EaglercraftRandom random) {

> CHANGE  387 : 388  @  519 : 520

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> DELETE  403  @  535 : 537

> DELETE  404  @  538 : 548

> CHANGE  407 : 408  @  551 : 561

~ 

> DELETE  411  @  564 : 572

> CHANGE  663 : 664  @  824 : 825

~ 	public int quantityDroppedWithBonus(int fortune, EaglercraftRandom random) {

> INSERT  800 : 801  @  961

+ 		bootstrapStates();

> INSERT  1269 : 1309  @  1429

+ 	public static void bootstrapStates() {
+ 		BlockBed.bootstrapStates();
+ 		BlockDirt.bootstrapStates();
+ 		BlockDoor.bootstrapStates();
+ 		BlockDoublePlant.bootstrapStates();
+ 		BlockFlowerPot.bootstrapStates();
+ 		BlockHugeMushroom.bootstrapStates();
+ 		BlockLever.bootstrapStates();
+ 		BlockLog.bootstrapStates();
+ 		BlockNewLeaf.bootstrapStates();
+ 		BlockNewLog.bootstrapStates();
+ 		BlockOldLeaf.bootstrapStates();
+ 		BlockOldLog.bootstrapStates();
+ 		BlockPistonExtension.bootstrapStates();
+ 		BlockPistonMoving.bootstrapStates();
+ 		BlockPlanks.bootstrapStates();
+ 		BlockPrismarine.bootstrapStates();
+ 		BlockQuartz.bootstrapStates();
+ 		BlockRail.bootstrapStates();
+ 		BlockRailDetector.bootstrapStates();
+ 		BlockRailPowered.bootstrapStates();
+ 		BlockRedSandstone.bootstrapStates();
+ 		BlockRedstoneComparator.bootstrapStates();
+ 		BlockRedstoneWire.bootstrapStates();
+ 		BlockSand.bootstrapStates();
+ 		BlockSandStone.bootstrapStates();
+ 		BlockSapling.bootstrapStates();
+ 		BlockSilverfish.bootstrapStates();
+ 		BlockSlab.bootstrapStates();
+ 		BlockStairs.bootstrapStates();
+ 		BlockStone.bootstrapStates();
+ 		BlockStoneBrick.bootstrapStates();
+ 		BlockStoneSlab.bootstrapStates();
+ 		BlockStoneSlabNew.bootstrapStates();
+ 		BlockTallGrass.bootstrapStates();
+ 		BlockTrapDoor.bootstrapStates();
+ 		BlockWall.bootstrapStates();
+ 		BlockWoodSlab.bootstrapStates();
+ 	}
+ 

> EOF
