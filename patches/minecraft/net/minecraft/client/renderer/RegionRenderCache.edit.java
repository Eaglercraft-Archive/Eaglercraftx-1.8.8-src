
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 4  @  3

+ 

> CHANGE  14 : 15  @  13 : 14

~ 	private final IBlockState DEFAULT_STATE = Blocks.air.getDefaultState();

> INSERT  56 : 70  @  55

+ 	/**
+ 	 * only use with a regular "net.minecraft.util.BlockPos"!
+ 	 */
+ 	public IBlockState getBlockStateFaster(BlockPos blockpos) {
+ 		int i = this.getPositionIndexFaster(blockpos);
+ 		IBlockState iblockstate = this.blockStates[i];
+ 		if (iblockstate == null) {
+ 			iblockstate = this.getBlockStateRawFaster(blockpos);
+ 			this.blockStates[i] = iblockstate;
+ 		}
+ 
+ 		return iblockstate;
+ 	}
+ 

> INSERT  80 : 93  @  65

+ 	/**
+ 	 * only use with a regular "net.minecraft.util.BlockPos"!
+ 	 */
+ 	private IBlockState getBlockStateRawFaster(BlockPos pos) {
+ 		if (pos.y >= 0 && pos.y < 256) {
+ 			int i = (pos.x >> 4) - this.chunkX;
+ 			int j = (pos.z >> 4) - this.chunkZ;
+ 			return this.chunkArray[i][j].getBlockState(pos);
+ 		} else {
+ 			return DEFAULT_STATE;
+ 		}
+ 	}
+ 

> INSERT  99 : 109  @  71

+ 
+ 	/**
+ 	 * only use with a regular "net.minecraft.util.BlockPos"!
+ 	 */
+ 	private int getPositionIndexFaster(BlockPos parBlockPos) {
+ 		int i = parBlockPos.x - this.position.x;
+ 		int j = parBlockPos.y - this.position.y;
+ 		int k = parBlockPos.z - this.position.z;
+ 		return i * 400 + k * 20 + j;
+ 	}

> EOF
