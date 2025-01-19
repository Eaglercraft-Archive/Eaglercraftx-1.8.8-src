
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> DELETE  9  @  9 : 10

> CHANGE  10 : 11  @  10 : 11

~ 	public boolean generate(World world, EaglercraftRandom var2, BlockPos blockpos) {

> CHANGE  25 : 28  @  25 : 27

~ 			EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 			for (int i = 0; i < facings.length; ++i) {
~ 				world.setBlockState(blockpos.offset(facings[i]), this.field_175910_d, 2);

> EOF
