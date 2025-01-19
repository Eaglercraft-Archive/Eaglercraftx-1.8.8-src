
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  8  @  8 : 12

> INSERT  75 : 79  @  75

+ 	public int getBiomeColorForCoords(BlockPos var1, int index) {
+ 		return this.worldObj.getBiomeColorForCoords(var1, index);
+ 	}
+ 

> CHANGE  2 : 3  @  2 : 3

~ 			return Chunk.getNoSkyLightValue();

> CHANGE  4 : 8  @  4 : 6

~ 				EnumFacing[] facings = EnumFacing._VALUES;
~ 				BlockPos tmp = new BlockPos(0, 0, 0);
~ 				for (int i = 0; i < facings.length; ++i) {
~ 					int k = this.getLightFor(pos, parBlockPos.offsetEvenFaster(facings[i], tmp));

> EOF
