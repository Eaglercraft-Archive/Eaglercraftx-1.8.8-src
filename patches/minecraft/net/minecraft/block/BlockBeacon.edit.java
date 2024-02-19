
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> DELETE  14  @  14 : 15

> CHANGE  17 : 18  @  17 : 20

~ 		if (!world.isRemote) {

> DELETE  5  @  5 : 7

> INSERT  1 : 2  @  1

+ 		return true;

> CHANGE  40 : 41  @  40 : 43

~ 		Chunk chunk = worldIn.getChunkFromBlockCoords(glassPos);

> CHANGE  1 : 6  @  1 : 6

~ 		for (int i = glassPos.getY() - 1; i >= 0; --i) {
~ 			final BlockPos blockpos = new BlockPos(glassPos.getX(), i, glassPos.getZ());
~ 			if (!chunk.canSeeSky(blockpos)) {
~ 				break;
~ 			}

> CHANGE  1 : 10  @  1 : 13

~ 			IBlockState iblockstate = worldIn.getBlockState(blockpos);
~ 			if (iblockstate.getBlock() == Blocks.beacon) {
~ 				((WorldServer) worldIn).addScheduledTask(new Runnable() {
~ 					public void run() {
~ 						TileEntity tileentity = worldIn.getTileEntity(blockpos);
~ 						if (tileentity instanceof TileEntityBeacon) {
~ 							((TileEntityBeacon) tileentity).updateBeacon();
~ 							worldIn.addBlockEvent(blockpos, Blocks.beacon, 1, 0);
~ 						}

> CHANGE  1 : 2  @  1 : 3

~ 				});

> CHANGE  1 : 2  @  1 : 2

~ 		}

> EOF
