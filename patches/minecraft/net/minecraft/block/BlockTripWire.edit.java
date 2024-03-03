
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  66 : 67  @  66 : 67

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> DELETE  48  @  48 : 49

> CHANGE  31 : 32  @  31 : 32

~ 	public void randomTick(World var1, BlockPos var2, IBlockState var3, EaglercraftRandom var4) {

> CHANGE  2 : 3  @  2 : 3

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom var4) {

> CHANGE  11 : 12  @  11 : 12

~ 		List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity((Entity) null,

> CHANGE  4 : 6  @  4 : 6

~ 			for (int i = 0, l = list.size(); i < l; ++i) {
~ 				if (!list.get(i).doesEntityNotTriggerPressurePlate()) {

> EOF
