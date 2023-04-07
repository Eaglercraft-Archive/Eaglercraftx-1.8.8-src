
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 4  @  1

+ 
+ import com.google.common.collect.AbstractIterator;
+ 

> DELETE  1  @  1 : 5

> INSERT  55 : 65  @  55

+ 	/**
+ 	 * eagler
+ 	 */
+ 	public BlockPos up(BlockPos dst) {
+ 		dst.x = x;
+ 		dst.y = y + 1;
+ 		dst.z = z;
+ 		return dst;
+ 	}
+ 

> INSERT  8 : 18  @  8

+ 	/**
+ 	 * eagler
+ 	 */
+ 	public BlockPos down(BlockPos dst) {
+ 		dst.x = x;
+ 		dst.y = y - 1;
+ 		dst.z = z;
+ 		return dst;
+ 	}
+ 

> INSERT  8 : 18  @  8

+ 	/**
+ 	 * eagler
+ 	 */
+ 	public BlockPos north(BlockPos dst) {
+ 		dst.x = x;
+ 		dst.y = y;
+ 		dst.z = z - 1;
+ 		return dst;
+ 	}
+ 

> INSERT  8 : 18  @  8

+ 	/**
+ 	 * eagler
+ 	 */
+ 	public BlockPos south(BlockPos dst) {
+ 		dst.x = x;
+ 		dst.y = y;
+ 		dst.z = z + 1;
+ 		return dst;
+ 	}
+ 

> INSERT  12 : 22  @  12

+ 	/**
+ 	 * eagler
+ 	 */
+ 	public BlockPos west(BlockPos dst) {
+ 		dst.x = x - 1;
+ 		dst.y = y;
+ 		dst.z = z;
+ 		return dst;
+ 	}
+ 

> INSERT  8 : 18  @  8

+ 	/**
+ 	 * eagler
+ 	 */
+ 	public BlockPos east(BlockPos dst) {
+ 		dst.x = x + 1;
+ 		dst.y = y;
+ 		dst.z = z;
+ 		return dst;
+ 	}
+ 

> INSERT  4 : 21  @  4

+ 	public BlockPos offsetFaster(EnumFacing facing, BlockPos ret) {
+ 		ret.x = this.getX() + facing.getFrontOffsetX();
+ 		ret.y = this.getY() + facing.getFrontOffsetY();
+ 		ret.z = this.getZ() + facing.getFrontOffsetZ();
+ 		return ret;
+ 	}
+ 
+ 	/**
+ 	 * only use with a regular "net.minecraft.util.BlockPos"!
+ 	 */
+ 	public BlockPos offsetEvenFaster(EnumFacing facing, BlockPos ret) {
+ 		ret.x = this.x + facing.getFrontOffsetX();
+ 		ret.y = this.y + facing.getFrontOffsetY();
+ 		ret.z = this.z + facing.getFrontOffsetZ();
+ 		return ret;
+ 	}
+ 

> CHANGE  2 : 4  @  2 : 4

~ 				: new BlockPos(this.x + facing.getFrontOffsetX() * n, this.y + facing.getFrontOffsetY() * n,
~ 						this.z + facing.getFrontOffsetZ() * n);

> DELETE  104  @  104 : 107

> CHANGE  6 : 7  @  6 : 10

~ 			super(x_, y_, z_);

> EOF
