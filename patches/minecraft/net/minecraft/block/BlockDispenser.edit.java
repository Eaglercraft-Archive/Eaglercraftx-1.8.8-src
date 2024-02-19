
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  32 : 33  @  32 : 33

~ 	protected EaglercraftRandom rand = new EaglercraftRandom();

> CHANGE  43 : 44  @  43 : 46

~ 		if (!world.isRemote) {

> DELETE  9  @  9 : 11

> INSERT  1 : 2  @  1

+ 		return true;

> CHANGE  37 : 38  @  37 : 38

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState var3, EaglercraftRandom var4) {

> DELETE  3  @  3 : 4

> EOF
