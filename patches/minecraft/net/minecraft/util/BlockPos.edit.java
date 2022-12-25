
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  3 : 6  @  4

+ 
+ import com.google.common.collect.AbstractIterator;
+ 

> DELETE  7  @  5 : 9

> INSERT  110 : 127  @  112

+ 	public BlockPos offsetFaster(EnumFacing facing, MutableBlockPos ret) {
+ 		ret.x = this.getX() + facing.getFrontOffsetX();
+ 		ret.y = this.getY() + facing.getFrontOffsetY();
+ 		ret.z = this.getZ() + facing.getFrontOffsetZ();
+ 		return ret;
+ 	}
+ 
+ 	/**
+ 	 * only use with a regular "net.minecraft.util.BlockPos"!
+ 	 */
+ 	public BlockPos offsetEvenFaster(EnumFacing facing, MutableBlockPos ret) {
+ 		ret.x = this.x + facing.getFrontOffsetX();
+ 		ret.y = this.y + facing.getFrontOffsetY();
+ 		ret.z = this.z + facing.getFrontOffsetZ();
+ 		return ret;
+ 	}
+ 

> DELETE  235  @  220 : 223

> CHANGE  241 : 242  @  229 : 233

~ 			super(x_, y_, z_);

> EOF
