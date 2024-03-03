
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 5  @  4 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  10 : 12  @  10 : 13

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  21 : 22  @  21 : 22

~ 	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {

> CHANGE  66 : 69  @  66 : 68

~ 						EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 						for (int m = 0; m < facings.length; ++m) {
~ 							if (world.getBlockState(blockpos3.offset(facings[m])).getBlock().getMaterial().isSolid()) {

> CHANGE  35 : 36  @  35 : 36

~ 	private String pickMobSpawner(EaglercraftRandom parRandom) {

> EOF
