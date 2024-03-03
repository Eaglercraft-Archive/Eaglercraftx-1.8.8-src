
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> CHANGE  104 : 105  @  104 : 105

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> CHANGE  3 : 4  @  3 : 4

~ 	public int quantityDropped(EaglercraftRandom var1) {

> CHANGE  7 : 10  @  7 : 8

~ 		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 		for (int m = 0; m < facings.length; ++m) {
~ 			EnumFacing enumfacing = facings[m];

> CHANGE  20 : 22  @  20 : 21

~ 			for (int j = 0; j < facings.length; ++j) {
~ 				EnumFacing enumfacing1 = facings[j];

> CHANGE  32 : 36  @  32 : 33

~ 		return this.blockMaterial == Material.water
~ 				? (DeferredStateManager.isRenderingRealisticWater() ? EnumWorldBlockLayer.REALISTIC_WATER
~ 						: EnumWorldBlockLayer.TRANSLUCENT)
~ 				: EnumWorldBlockLayer.SOLID;

> CHANGE  2 : 3  @  2 : 3

~ 	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> CHANGE  69 : 72  @  69 : 70

~ 			EnumFacing[] facings = EnumFacing._VALUES;
~ 			for (int j = 0; j < facings.length; ++j) {
~ 				EnumFacing enumfacing = facings[j];

> EOF
