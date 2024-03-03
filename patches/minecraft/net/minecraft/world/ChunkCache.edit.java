
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  8  @  8 : 12

> CHANGE  77 : 78  @  77 : 78

~ 			return Chunk.getNoSkyLightValue();

> CHANGE  4 : 8  @  4 : 6

~ 				EnumFacing[] facings = EnumFacing._VALUES;
~ 				BlockPos tmp = new BlockPos(0, 0, 0);
~ 				for (int i = 0; i < facings.length; ++i) {
~ 					int k = this.getLightFor(pos, parBlockPos.offsetEvenFaster(facings[i], tmp));

> EOF
