
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 8

> DELETE  1  @  1 : 4

> DELETE  3  @  3 : 4

> DELETE  5  @  5 : 7

> CHANGE  4 : 6  @  4 : 11

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  1 : 3  @  1 : 7

~ public abstract class AnvilChunkLoader implements IChunkLoader {
~ 	private static final Logger logger = LogManager.getLogger("AnvilChunkLoader");

> DELETE  1  @  1 : 21

> CHANGE  24 : 25  @  24 : 109

~ 	protected void writeChunkToNBT(Chunk chunkIn, World worldIn, NBTTagCompound parNBTTagCompound) {

> CHANGE  80 : 81  @  80 : 81

~ 		List<NextTickListEntry> list = worldIn.getPendingBlockUpdates(chunkIn, false);

> CHANGE  4 : 6  @  4 : 5

~ 			for (int k = 0, l = list.size(); k < l; ++k) {
~ 				NextTickListEntry nextticklistentry = list.get(k);

> CHANGE  17 : 18  @  17 : 18

~ 	protected Chunk readChunkFromNBT(World worldIn, NBTTagCompound parNBTTagCompound) {

> EOF
