
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 4  @  1 : 2

~ 
~ import com.google.common.collect.Lists;
~ 

> DELETE  76  @  76 : 77

> DELETE  31  @  31 : 32

> CHANGE  42 : 43  @  42 : 43

~ 		private static final BlockRailBase.EnumRailDirection[] META_LOOKUP = new BlockRailBase.EnumRailDirection[10];

> CHANGE  34 : 37  @  34 : 36

~ 			BlockRailBase.EnumRailDirection[] directions = values();
~ 			for (int i = 0; i < directions.length; ++i) {
~ 				META_LOOKUP[directions[i].getMetadata()] = directions[i];

> CHANGE  124 : 128  @  124 : 126

~ 			EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 			BlockPos tmp = new BlockPos(0, 0, 0);
~ 			for (int j = 0; j < facings.length; ++j) {
~ 				if (this.hasRailAt(this.pos.offsetEvenFaster(facings[j], tmp))) {

> EOF
