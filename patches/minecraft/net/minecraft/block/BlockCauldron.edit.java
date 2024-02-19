
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 5  @  3 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  71 : 72  @  71 : 74

~ 		if (!world.isRemote) {

> INSERT  84 : 86  @  84

+ 		} else {
+ 			return true;

> CHANGE  18 : 19  @  18 : 19

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> EOF
