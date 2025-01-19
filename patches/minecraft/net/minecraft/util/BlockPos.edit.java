
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 4  @  1

+ 
+ import com.google.common.collect.AbstractIterator;
+ 

> DELETE  1  @  1 : 5

> INSERT  12 : 16  @  12

+ 	public BlockPos() {
+ 		super(0, 0, 0);
+ 	}
+ 

> CHANGE  17 : 18  @  17 : 18

~ 		this(source.x, source.y, source.z);

> CHANGE  4 : 5  @  4 : 5

~ 				: new BlockPos((double) this.x + x, (double) this.y + y, (double) this.z + z);

> CHANGE  3 : 4  @  3 : 4

~ 		return x == 0 && y == 0 && z == 0 ? this : new BlockPos(this.x + x, this.y + y, this.z + z);

> INSERT  2 : 12  @  2

+ 	/**
+ 	 * eagler
+ 	 */
+ 	public BlockPos add(int x, int y, int z, BlockPos dst) {
+ 		dst.x = this.x + x;
+ 		dst.y = this.y + y;
+ 		dst.z = this.z + z;
+ 		return dst;
+ 	}
+ 

> CHANGE  1 : 3  @  1 : 3

~ 		return vec.x == 0 && vec.y == 0 && vec.z == 0 ? this
~ 				: new BlockPos(this.x + vec.x, this.y + vec.y, this.z + vec.z);

> CHANGE  3 : 5  @  3 : 5

~ 		return vec.x == 0 && vec.y == 0 && vec.z == 0 ? this
~ 				: new BlockPos(this.x - vec.x, this.y - vec.y, this.z - vec.z);

> INSERT  6 : 16  @  6

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
+ 		ret.x = this.x + facing.getFrontOffsetX();
+ 		ret.y = this.y + facing.getFrontOffsetY();
+ 		ret.z = this.z + facing.getFrontOffsetZ();
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

> CHANGE  3 : 5  @  3 : 6

~ 		return new BlockPos(this.y * vec3i.z - this.z * vec3i.y, this.z * vec3i.x - this.x * vec3i.z,
~ 				this.x * vec3i.y - this.y * vec3i.x);

> CHANGE  3 : 5  @  3 : 5

~ 		return ((long) this.x & X_MASK) << X_SHIFT | ((long) this.y & Y_MASK) << Y_SHIFT
~ 				| ((long) this.z & Z_MASK) << 0;

> CHANGE  10 : 12  @  10 : 14

~ 		final BlockPos blockpos = new BlockPos(Math.min(from.x, to.x), Math.min(from.y, to.y), Math.min(from.z, to.z));
~ 		final BlockPos blockpos1 = new BlockPos(Math.max(from.x, to.x), Math.max(from.y, to.y), Math.max(from.z, to.z));

> CHANGE  12 : 16  @  12 : 16

~ 							int i = this.lastReturned.x;
~ 							int j = this.lastReturned.y;
~ 							int k = this.lastReturned.z;
~ 							if (i < blockpos1.x) {

> CHANGE  1 : 3  @  1 : 3

~ 							} else if (j < blockpos1.y) {
~ 								i = blockpos.x;

> CHANGE  1 : 4  @  1 : 4

~ 							} else if (k < blockpos1.z) {
~ 								i = blockpos.x;
~ 								j = blockpos.y;

> CHANGE  12 : 19  @  12 : 21

~ 	public static Iterable<BlockPos> getAllInBoxMutable(BlockPos from, BlockPos to) {
~ 		final BlockPos blockpos = new BlockPos(Math.min(from.x, to.x), Math.min(from.y, to.y), Math.min(from.z, to.z));
~ 		final BlockPos blockpos1 = new BlockPos(Math.max(from.x, to.x), Math.max(from.y, to.y), Math.max(from.z, to.z));
~ 		return new Iterable<BlockPos>() {
~ 			public Iterator<BlockPos> iterator() {
~ 				return new AbstractIterator<BlockPos>() {
~ 					private BlockPos theBlockPos = null;

> CHANGE  1 : 2  @  1 : 2

~ 					protected BlockPos computeNext() {

> CHANGE  1 : 2  @  1 : 3

~ 							this.theBlockPos = new BlockPos(blockpos.x, blockpos.y, blockpos.z);

> CHANGE  2 : 3  @  2 : 3

~ 							return (BlockPos) this.endOfData();

> CHANGE  1 : 5  @  1 : 5

~ 							int i = this.theBlockPos.x;
~ 							int j = this.theBlockPos.y;
~ 							int k = this.theBlockPos.z;
~ 							if (i < blockpos1.x) {

> CHANGE  1 : 3  @  1 : 3

~ 							} else if (j < blockpos1.y) {
~ 								i = blockpos.x;

> CHANGE  1 : 4  @  1 : 4

~ 							} else if (k < blockpos1.z) {
~ 								i = blockpos.x;
~ 								j = blockpos.y;

> CHANGE  14 : 19  @  14 : 48

~ 	public BlockPos func_181079_c(int x, int y, int z) {
~ 		this.x = x;
~ 		this.y = y;
~ 		this.z = z;
~ 		return this;

> INSERT  1 : 2  @  1

+ 

> EOF
