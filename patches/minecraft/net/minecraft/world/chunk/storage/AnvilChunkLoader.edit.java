
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  5  @  5 : 6

> CHANGE  23 : 25  @  23 : 28

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  1 : 2  @  1 : 7

~ public abstract class AnvilChunkLoader implements IChunkLoader {

> CHANGE  1 : 2  @  1 : 4

~ 	private static final Logger logger = LogManager.getLogger("AnvilChunkLoader");

> DELETE  1  @  1 : 17

> CHANGE  24 : 25  @  24 : 109

~ 	protected void writeChunkToNBT(Chunk chunkIn, World worldIn, NBTTagCompound parNBTTagCompound) {

> CHANGE  85 : 86  @  85 : 86

~ 			for (NextTickListEntry nextticklistentry : (List<NextTickListEntry>) list) {

> CHANGE  17 : 18  @  17 : 18

~ 	protected Chunk readChunkFromNBT(World worldIn, NBTTagCompound parNBTTagCompound) {

> EOF
