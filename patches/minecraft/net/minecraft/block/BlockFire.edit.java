
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 7  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import com.carrotsearch.hppc.ObjectIntIdentityHashMap;
~ import com.carrotsearch.hppc.ObjectIntMap;
~ 

> CHANGE  26 : 28  @  26 : 28

~ 	private final ObjectIntMap<Block> encouragements = new ObjectIntIdentityHashMap<>();
~ 	private final ObjectIntMap<Block> flammabilities = new ObjectIntIdentityHashMap<>();

> CHANGE  74 : 76  @  74 : 76

~ 		this.encouragements.put(blockIn, encouragement);
~ 		this.flammabilities.put(blockIn, flammability);

> CHANGE  14 : 15  @  14 : 15

~ 	public int quantityDropped(EaglercraftRandom var1) {

> CHANGE  7 : 8  @  7 : 8

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> INSERT  59 : 61  @  59

+ 								if (!world.isBlockLoaded(blockpos1))
+ 									continue;

> CHANGE  38 : 39  @  38 : 40

~ 		return this.flammabilities.getOrDefault(blockIn, 0);

> CHANGE  3 : 4  @  3 : 5

~ 		return this.encouragements.getOrDefault(blockIn, 0);

> CHANGE  2 : 8  @  2 : 4

~ 	private void catchOnFire(World worldIn, BlockPos pos, int chance, EaglercraftRandom random, int age) {
~ 		IBlockState iblockstate = worldIn.getBlockStateIfLoaded(pos);
~ 		if (iblockstate == null) {
~ 			return;
~ 		}
~ 		int i = this.getFlammability(iblockstate.getBlock());

> DELETE  1  @  1 : 2

> CHANGE  20 : 23  @  20 : 21

~ 		EnumFacing[] facings = EnumFacing._VALUES;
~ 		for (int i = 0; i < facings.length; ++i) {
~ 			EnumFacing enumfacing = facings[i];

> CHANGE  14 : 21  @  14 : 16

~ 			EnumFacing[] facings = EnumFacing._VALUES;
~ 			BlockPos tmp = new BlockPos(0, 0, 0);
~ 			for (int j = 0; j < facings.length; ++j) {
~ 				IBlockState type = worldIn.getBlockStateIfLoaded(pos.offsetEvenFaster(facings[j], tmp));
~ 				if (type != null) {
~ 					i = Math.max(this.getEncouragement(type.getBlock()), i);
~ 				}

> CHANGE  37 : 38  @  37 : 38

~ 	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom random) {

> EOF
