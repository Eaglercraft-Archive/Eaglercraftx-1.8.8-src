
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> CHANGE  104 : 105  @  104 : 105

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> CHANGE  3 : 4  @  3 : 4

~ 	public int quantityDropped(EaglercraftRandom var1) {

> CHANGE  61 : 65  @  61 : 62

~ 		return this.blockMaterial == Material.water
~ 				? (DeferredStateManager.isRenderingRealisticWater() ? EnumWorldBlockLayer.REALISTIC_WATER
~ 						: EnumWorldBlockLayer.TRANSLUCENT)
~ 				: EnumWorldBlockLayer.SOLID;

> CHANGE  2 : 3  @  2 : 3

~ 	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> EOF
