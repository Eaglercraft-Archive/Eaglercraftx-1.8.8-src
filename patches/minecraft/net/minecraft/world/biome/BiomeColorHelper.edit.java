
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  4  @  4 : 5

> INSERT  2 : 3  @  2

+ 

> CHANGE  22 : 23  @  22 : 23

~ 		for (BlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(parBlockPos.add(-1, 0, -1),

> CHANGE  12 : 13  @  12 : 13

~ 		return parIBlockAccess.getBiomeColorForCoords(parBlockPos, 0);

> CHANGE  3 : 4  @  3 : 4

~ 		return parIBlockAccess.getBiomeColorForCoords(parBlockPos, 1);

> CHANGE  3 : 4  @  3 : 4

~ 		return parIBlockAccess.getBiomeColorForCoords(parBlockPos, 2);

> INSERT  2 : 12  @  2

+ 	public static int getBiomeColorForCoordsOld(IBlockAccess parIBlockAccess, BlockPos parBlockPos, int index) {
+ 		if (index == 0) {
+ 			return func_180285_a(parIBlockAccess, parBlockPos, field_180291_a);
+ 		} else if (index == 1) {
+ 			return func_180285_a(parIBlockAccess, parBlockPos, field_180289_b);
+ 		} else {
+ 			return func_180285_a(parIBlockAccess, parBlockPos, field_180290_c);
+ 		}
+ 	}
+ 

> EOF
