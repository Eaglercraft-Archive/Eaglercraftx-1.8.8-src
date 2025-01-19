
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> DELETE  5  @  5 : 6

> CHANGE  2 : 4  @  2 : 3

~ 	public boolean generate(World world, EaglercraftRandom random, BlockPos blockpos) {
~ 		BlockPos tmp;

> CHANGE  2 : 4  @  2 : 3

~ 		} else if (world.getBlockState(blockpos.offsetEvenFaster(EnumFacing.UP, tmp = new BlockPos(0, 0, 0)))
~ 				.getBlock() != Blocks.netherrack) {

> CHANGE  10 : 14  @  10 : 12

~ 					EnumFacing[] facings = EnumFacing._VALUES;
~ 					for (int k = 0; k < facings.length; ++k) {
~ 						if (world.getBlockState(blockpos1.offsetEvenFaster(facings[k], tmp))
~ 								.getBlock() == Blocks.glowstone) {

> EOF
