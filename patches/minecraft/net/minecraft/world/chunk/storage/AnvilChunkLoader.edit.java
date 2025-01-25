
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 8

> DELETE  1  @  1 : 4

> DELETE  3  @  3 : 4

> INSERT  2 : 3  @  2

+ import net.minecraft.nbt.NBTTagShort;

> DELETE  3  @  3 : 5

> CHANGE  4 : 6  @  4 : 11

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  1 : 4  @  1 : 7

~ public abstract class AnvilChunkLoader implements IChunkLoader {
~ 	private static final Logger logger = LogManager.getLogger("AnvilChunkLoader");
~ 	private static final String NEIGHBOR_LIGHT_CHECKS_KEY = "NeighborLightChecks";

> DELETE  1  @  1 : 21

> CHANGE  24 : 27  @  24 : 109

~ 	protected void writeChunkToNBT(Chunk chunkIn, World worldIn, NBTTagCompound parNBTTagCompound) {
~ 		alfheim$writeNeighborLightChecksToNBT(chunkIn, parNBTTagCompound);
~ 		parNBTTagCompound.setBoolean("LightPopulated", chunkIn.alfheim$isLightInitialized());

> CHANGE  80 : 81  @  80 : 81

~ 		List<NextTickListEntry> list = worldIn.getPendingBlockUpdates(chunkIn, false);

> CHANGE  4 : 6  @  4 : 5

~ 			for (int k = 0, l = list.size(); k < l; ++k) {
~ 				NextTickListEntry nextticklistentry = list.get(k);

> CHANGE  17 : 18  @  17 : 18

~ 	protected Chunk readChunkFromNBT(World worldIn, NBTTagCompound parNBTTagCompound) {

> INSERT  102 : 105  @  102

+ 		alfheim$readNeighborLightChecksFromNBT(chunk, parNBTTagCompound);
+ 		chunk.alfheim$setLightInitialized(parNBTTagCompound.getBoolean("LightPopulated"));
+ 

> INSERT  2 : 46  @  2

+ 
+ 	private static void alfheim$readNeighborLightChecksFromNBT(final Chunk chunk, final NBTTagCompound compound) {
+ 		if (!compound.hasKey(NEIGHBOR_LIGHT_CHECKS_KEY, 9)) {
+ 			return;
+ 		}
+ 
+ 		final NBTTagList tagList = compound.getTagList(NEIGHBOR_LIGHT_CHECKS_KEY, 2);
+ 
+ 		if (tagList.tagCount() != 32) {
+ 			return;
+ 		}
+ 
+ 		chunk.alfheim$initNeighborLightChecks();
+ 
+ 		final short[] neighborLightChecks = chunk.alfheim$neighborLightChecks;
+ 
+ 		for (int i = 0; i < 32; ++i) {
+ 			neighborLightChecks[i] = ((NBTTagShort) tagList.get(i)).getShort();
+ 		}
+ 	}
+ 
+ 	private static void alfheim$writeNeighborLightChecksToNBT(final Chunk chunk, final NBTTagCompound compound) {
+ 		final short[] neighborLightChecks = chunk.alfheim$neighborLightChecks;
+ 
+ 		if (neighborLightChecks == null) {
+ 			return;
+ 		}
+ 
+ 		boolean empty = true;
+ 
+ 		final NBTTagList list = new NBTTagList();
+ 
+ 		for (final short flags : neighborLightChecks) {
+ 			list.appendTag(new NBTTagShort(flags));
+ 
+ 			if (flags != 0) {
+ 				empty = false;
+ 			}
+ 		}
+ 
+ 		if (!empty) {
+ 			compound.setTag(NEIGHBOR_LIGHT_CHECKS_KEY, list);
+ 		}
+ 	}

> EOF
