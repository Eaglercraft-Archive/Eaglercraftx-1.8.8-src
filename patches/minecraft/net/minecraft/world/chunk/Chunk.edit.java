
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 9  @  4 : 5

~ 
~ import dev.redstudio.alfheim.lighting.LightingEngine;
~ import dev.redstudio.alfheim.utils.EnumBoundaryFacing;
~ import dev.redstudio.alfheim.utils.WorldChunkSlice;
~ 

> INSERT  1 : 2  @  1

+ import java.util.LinkedList;

> CHANGE  2 : 5  @  2 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 
~ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;

> DELETE  4  @  4 : 6

> DELETE  8  @  8 : 9

> DELETE  3  @  3 : 4

> DELETE  2  @  2 : 5

> CHANGE  1 : 4  @  1 : 4

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> CHANGE  24 : 26  @  24 : 25

~ 	private List<BlockPos> tileEntityPosQueue;
~ 	private final ChunkCoordIntPair coordsCache;

> INSERT  1 : 5  @  1

+ 	private LightingEngine alfheim$lightingEngine;
+ 	private boolean alfheim$isLightInitialized;
+ 	public short[] alfheim$neighborLightChecks;
+ 

> CHANGE  7 : 8  @  7 : 8

~ 		this.tileEntityPosQueue = new LinkedList();

> INSERT  5 : 6  @  5

+ 		this.coordsCache = new ChunkCoordIntPair(x, z);

> INSERT  7 : 9  @  7

+ 
+ 		alfheim$lightingEngine = worldIn != null ? worldIn.alfheim$getLightingEngine() : null;

> CHANGE  31 : 32  @  31 : 32

~ 		return this.getHeightValue(pos.x & 15, pos.z & 15);

> CHANGE  29 : 30  @  29 : 30

~ 					Block block = this.getBlock(j, l - 1, k);

> INSERT  61 : 64  @  61

+ 		if (!this.worldObj.isRemote) {
+ 			++EaglerMinecraftServer.counterLightUpdate;
+ 		}

> DELETE  3  @  3 : 8

> CHANGE  1 : 4  @  1 : 11

~ 		if (!worldObj.isAreaLoaded(new BlockPos((xPosition << 4) + 8, 0, (zPosition << 4) + 8), 16)) {
~ 			return;
~ 		}

> CHANGE  1 : 4  @  1 : 5

~ 		if (!this.worldObj.isRemote) {
~ 			++EaglerMinecraftServer.counterLightUpdate;
~ 		}

> CHANGE  1 : 2  @  1 : 2

~ 		final WorldChunkSlice slice = new WorldChunkSlice(worldObj.getChunkProvider(), xPosition, zPosition);

> CHANGE  1 : 5  @  1 : 5

~ 		for (int x = 0; x < 16; ++x) {
~ 			for (int z = 0; z < 16; ++z) {
~ 				if (!alfheim$recheckGapsForColumn(slice, x, z))
~ 					continue;

> CHANGE  1 : 3  @  1 : 7

~ 				if (parFlag)
~ 					return;

> DELETE  1  @  1 : 3

> CHANGE  2 : 3  @  2 : 3

~ 		isGapLightingUpdated = false;

> CHANGE  24 : 26  @  24 : 29

~ 		int heightMapY = heightMap[z << 4 | x] & 255;
~ 		int newHeightMapY = Math.max(y, heightMapY);

> CHANGE  1 : 3  @  1 : 4

~ 		while (newHeightMapY > 0 && getBlockLightOpacity(x, newHeightMapY - 1, z) == 0)
~ 			--newHeightMapY;

> CHANGE  1 : 3  @  1 : 26

~ 		if (newHeightMapY == heightMapY)
~ 			return;

> CHANGE  1 : 4  @  1 : 2

~ 		if (!this.worldObj.isRemote) {
~ 			++EaglerMinecraftServer.counterLightUpdate;
~ 		}

> CHANGE  1 : 2  @  1 : 7

~ 		heightMap[z << 4 | x] = newHeightMapY;

> CHANGE  1 : 3  @  1 : 5

~ 		if (!worldObj.provider.getHasNoSky())
~ 			alfheim$relightSkylightColumn(x, z, heightMapY, newHeightMapY);

> CHANGE  1 : 2  @  1 : 7

~ 		final int heightMapY1 = heightMap[z << 4 | x];

> CHANGE  1 : 3  @  1 : 23

~ 		if (heightMapY1 < heightMapMinimum) {
~ 			heightMapMinimum = heightMapY1;

> CHANGE  8 : 9  @  8 : 9

~ 		return this.getBlock(x, y, z).getLightOpacity();

> CHANGE  2 : 3  @  2 : 4

~ 	public Block getBlock(int x, int y, int z) {

> CHANGE  3 : 4  @  3 : 9

~ 				return extendedblockstorage.getBlockByExtId(x, y & 15, z);

> CHANGE  3 : 4  @  3 : 4

~ 		return Blocks.air;

> CHANGE  2 : 11  @  2 : 15

~ 	public Block getBlock(final BlockPos pos) {
~ 		if (pos.y >= 0 && pos.y >> 4 < this.storageArrays.length) {
~ 			ExtendedBlockStorage extendedblockstorage = this.storageArrays[pos.y >> 4];
~ 			if (extendedblockstorage != null) {
~ 				int j = pos.x & 15;
~ 				int k = pos.y & 15;
~ 				int i = pos.z & 15;
~ 				return extendedblockstorage.getBlockByExtId(j, k, i);
~ 			}

> DELETE  1  @  1 : 2

> CHANGE  1 : 2  @  1 : 14

~ 		return Blocks.air;

> CHANGE  3 : 10  @  3 : 7

~ 		if (pos.y >= 0 && pos.y >> 4 < this.storageArrays.length) {
~ 			ExtendedBlockStorage extendedblockstorage = this.storageArrays[pos.y >> 4];
~ 			if (extendedblockstorage != null) {
~ 				int j = pos.x & 15;
~ 				int k = pos.y & 15;
~ 				int i = pos.z & 15;
~ 				return extendedblockstorage.get(j, k, i);

> DELETE  1  @  1 : 30

> INSERT  1 : 3  @  1

+ 
+ 		return Blocks.air.getDefaultState();

> CHANGE  16 : 19  @  16 : 19

~ 		int i = pos.x & 15;
~ 		int j = pos.y;
~ 		int k = pos.z & 15;

> CHANGE  21 : 23  @  21 : 22

~ 				alfheim$initSkylightForSection(extendedblockstorage);
~ 				// flag = j >= i1;

> CHANGE  18 : 19  @  18 : 19

~ 					// int k1 = block1.getLightOpacity();

> CHANGE  8 : 12  @  8 : 12

~ //					if (j1 != k1 && (j1 < k1 || this.getLightFor(EnumSkyBlock.SKY, pos) > 0
~ //							|| this.getLightFor(EnumSkyBlock.BLOCK, pos) > 0)) {
~ //						this.propagateSkylightOcclusion(i, k);
~ //					}

> CHANGE  33 : 35  @  33 : 43

~ 		alfheim$lightingEngine.processLightUpdatesForType(enumskyblock);
~ 		return alfheim$getCachedLightFor(enumskyblock, blockpos);

> CHANGE  3 : 6  @  3 : 6

~ 		int j = blockpos.x & 15;
~ 		int k = blockpos.y;
~ 		int l = blockpos.z & 15;

> CHANGE  4 : 5  @  4 : 5

~ 			alfheim$initSkylightForSection(storageArrays[k >> 4]);

> CHANGE  14 : 18  @  14 : 17

~ 		alfheim$lightingEngine.processLightUpdates();
~ 		int j = blockpos.x & 15;
~ 		int k = blockpos.y;
~ 		int l = blockpos.z & 15;

> CHANGE  4 : 5  @  4 : 5

~ 					: getNoSkyLightValue();

> CHANGE  1 : 3  @  1 : 2

~ 			int i1 = this.worldObj.provider.getHasNoSky() ? getNoSkyLightValue()
~ 					: extendedblockstorage.getExtSkylightValue(j, k & 15, l);

> INSERT  10 : 14  @  10

+ 	public static int getNoSkyLightValue() {
+ 		return DeferredStateManager.isDeferredRenderer() ? 5 : 0;
+ 	}
+ 

> CHANGE  43 : 46  @  43 : 46

~ 		int i = blockpos.x & 15;
~ 		int j = blockpos.y;
~ 		int k = blockpos.z & 15;

> INSERT  12 : 13  @  12

+ 			BlockPos pos2 = new BlockPos(blockpos);

> CHANGE  1 : 3  @  1 : 3

~ 				tileentity = this.createNewTileEntity(pos2);
~ 				this.worldObj.setTileEntity(pos2, tileentity);

> CHANGE  1 : 2  @  1 : 2

~ 				this.tileEntityPosQueue.add(pos2);

> INSERT  19 : 20  @  19

+ 		blockpos = new BlockPos(blockpos);

> INSERT  33 : 71  @  33

+ 		for (final EnumFacing facing : EnumFacing.HORIZONTALS) {
+ 			final int xOffset = facing.getFrontOffsetX();
+ 			final int zOffset = facing.getFrontOffsetZ();
+ 
+ 			final Chunk nChunk = worldObj.getChunkProvider().getLoadedChunk(xPosition + xOffset, zPosition + zOffset);
+ 
+ 			if (nChunk == null)
+ 				continue;
+ 
+ 			EnumSkyBlock[] lightTypes = EnumSkyBlock._VALUES;
+ 			EnumFacing.AxisDirection[] axisDirections = EnumFacing.AxisDirection._VALUES;
+ 			for (int ii = 0, ll = lightTypes.length; ii < ll; ++ii) {
+ 				final EnumSkyBlock lightType = lightTypes[ii];
+ 				for (int jj = 0, mm = axisDirections.length; jj < mm; ++jj) {
+ 					final EnumFacing.AxisDirection axisDir = axisDirections[jj];
+ 					// Merge flags upon loading of a chunk. This ensures that all flags are always
+ 					// already on the IN boundary below
+ 					alfheim$mergeFlags(lightType, this, nChunk, facing, axisDir);
+ 					alfheim$mergeFlags(lightType, nChunk, this, facing.getOpposite(), axisDir);
+ 
+ 					// Check everything that might have been canceled due to this chunk not being
+ 					// loaded.
+ 					// Also, pass in chunks if already known
+ 					// The boundary to the neighbor chunk (both ways)
+ 					alfheim$scheduleRelightChecksForBoundary(this, nChunk, null, lightType, xOffset, zOffset, axisDir);
+ 					alfheim$scheduleRelightChecksForBoundary(nChunk, this, null, lightType, -xOffset, -zOffset,
+ 							axisDir);
+ 					// The boundary to the diagonal neighbor (since the checks in that chunk were
+ 					// aborted if this chunk wasn't loaded, see
+ 					// alfheim$scheduleRelightChecksForBoundary)
+ 					alfheim$scheduleRelightChecksForBoundary(nChunk, null, this, lightType,
+ 							(zOffset != 0 ? axisDir.getOffset() : 0), (xOffset != 0 ? axisDir.getOffset() : 0),
+ 							facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE
+ 									? EnumFacing.AxisDirection.NEGATIVE
+ 									: EnumFacing.AxisDirection.POSITIVE);
+ 				}
+ 			}
+ 		}

> CHANGE  61 : 63  @  61 : 63

~ 						&& (predicate == null || predicate.apply((T) entity))) {
~ 					list.add((T) entity);

> CHANGE  18 : 20  @  18 : 20

~ 	public EaglercraftRandom getRandomWithSeed(long i) {
~ 		return new EaglercraftRandom(this.worldObj.getSeed() + (long) (this.xPosition * this.xPosition * 4987142)

> CHANGE  1 : 2  @  1 : 2

~ 				+ (long) (this.zPosition * 389711) ^ i, !this.worldObj.getWorldInfo().isOldEaglercraftRandom());

> CHANGE  90 : 91  @  90 : 91

~ 			BlockPos blockpos = (BlockPos) this.tileEntityPosQueue.remove(0);

> CHANGE  15 : 16  @  15 : 16

~ 		return coordsCache;

> INSERT  2 : 6  @  2

+ 	public long getChunkCoordLong() {
+ 		return ChunkCoordIntPair.chunkXZ2Int(this.xPosition, this.zPosition);
+ 	}
+ 

> CHANGE  95 : 96  @  95 : 96

~ 		if (chunkManager != null && k == 255) {

> INSERT  42 : 43  @  42

+ 			EnumFacing[] facings = EnumFacing._VALUES;

> CHANGE  5 : 7  @  5 : 7

~ 					for (int m = 0; m < facings.length; ++m) {
~ 						BlockPos blockpos2 = blockpos1.offset(facings[m]);

> DELETE  14  @  14 : 27

> CHANGE  1 : 3  @  1 : 7

~ 		if (!alfheim$isLightInitialized)
~ 			alfheim$initChunkLighting(this, worldObj);

> CHANGE  1 : 10  @  1 : 5

~ 		for (int x = -1; x <= 1; x++) {
~ 			for (int z = -1; z <= 1; z++) {
~ 				if (x == 0 && z == 0)
~ 					continue;
~ 
~ 				final Chunk nChunk = worldObj.getChunkProvider().getLoadedChunk(xPosition + x, zPosition + z);
~ 
~ 				if (nChunk == null || !nChunk.alfheim$isLightInitialized())
~ 					return;

> INSERT  3 : 4  @  3

+ 		setLightPopulated(true);

> DELETE  10  @  10 : 64

> INSERT  79 : 413  @  79

+ 
+ 	private boolean alfheim$recheckGapsForColumn(final WorldChunkSlice slice, final int x, final int z) {
+ 		final int i = x + (z << 4);
+ 
+ 		if (updateSkylightColumns[i]) {
+ 			updateSkylightColumns[i] = false;
+ 
+ 			final int x1 = (this.xPosition << 4) + x;
+ 			final int z1 = (this.zPosition << 4) + z;
+ 
+ 			alfheim$recheckGapsSkylightNeighborHeight(slice, x1, z1, getHeightValue(x, z),
+ 					alfheim$recheckGapsGetLowestHeight(slice, x1, z1));
+ 
+ 			return true;
+ 		}
+ 
+ 		return false;
+ 	}
+ 
+ 	private int alfheim$recheckGapsGetLowestHeight(final WorldChunkSlice slice, final int x, final int z) {
+ 		int max = Integer.MAX_VALUE;
+ 
+ 		Chunk chunk = slice.getChunkFromWorldCoords(x + 1, z);
+ 
+ 		if (chunk != null)
+ 			max = Math.min(max, chunk.getLowestHeight());
+ 
+ 		chunk = slice.getChunkFromWorldCoords(x, z + 1);
+ 
+ 		if (chunk != null)
+ 			max = Math.min(max, chunk.getLowestHeight());
+ 
+ 		chunk = slice.getChunkFromWorldCoords(x - 1, z);
+ 
+ 		if (chunk != null)
+ 			max = Math.min(max, chunk.getLowestHeight());
+ 
+ 		chunk = slice.getChunkFromWorldCoords(x, z - 1);
+ 
+ 		if (chunk != null)
+ 			max = Math.min(max, chunk.getLowestHeight());
+ 
+ 		return max;
+ 	}
+ 
+ 	private void alfheim$recheckGapsSkylightNeighborHeight(final WorldChunkSlice slice, final int x, final int z,
+ 			final int height, final int max) {
+ 		alfheim$checkSkylightNeighborHeight(slice, x, z, max);
+ 		alfheim$checkSkylightNeighborHeight(slice, x + 1, z, height);
+ 		alfheim$checkSkylightNeighborHeight(slice, x, z + 1, height);
+ 		alfheim$checkSkylightNeighborHeight(slice, x - 1, z, height);
+ 		alfheim$checkSkylightNeighborHeight(slice, x, z - 1, height);
+ 	}
+ 
+ 	private void alfheim$checkSkylightNeighborHeight(final WorldChunkSlice slice, final int x, final int z,
+ 			final int maxValue) {
+ 		Chunk c = slice.getChunkFromWorldCoords(x, z);
+ 		if (c == null)
+ 			return;
+ 
+ 		final int y = c.getHeightValue(x & 15, z & 15);
+ 
+ 		if (y > maxValue)
+ 			alfheim$updateSkylightNeighborHeight(slice, x, z, maxValue, y + 1);
+ 		else if (y < maxValue)
+ 			alfheim$updateSkylightNeighborHeight(slice, x, z, y, maxValue + 1);
+ 	}
+ 
+ 	private void alfheim$updateSkylightNeighborHeight(final WorldChunkSlice slice, final int x, final int z,
+ 			final int startY, final int endY) {
+ 		if (endY < startY)
+ 			return;
+ 
+ 		if (!slice.isLoaded(x, z, 16))
+ 			return;
+ 
+ 		for (int y = startY; y < endY; ++y)
+ 			worldObj.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x, y, z));
+ 
+ 		isModified = true;
+ 	}
+ 
+ 	private static void alfheim$mergeFlags(final EnumSkyBlock lightType, final Chunk inChunk, final Chunk outChunk,
+ 			final EnumFacing dir, final EnumFacing.AxisDirection axisDirection) {
+ 		if (outChunk.alfheim$neighborLightChecks == null)
+ 			return;
+ 
+ 		inChunk.alfheim$initNeighborLightChecks();
+ 
+ 		final int inIndex = alfheim$getFlagIndex(lightType, dir, axisDirection, EnumBoundaryFacing.IN);
+ 		final int outIndex = alfheim$getFlagIndex(lightType, dir.getOpposite(), axisDirection, EnumBoundaryFacing.OUT);
+ 
+ 		inChunk.alfheim$neighborLightChecks[inIndex] |= outChunk.alfheim$neighborLightChecks[outIndex];
+ 		// No need to call Chunk.setModified() since checks are not deleted from
+ 		// outChunk
+ 	}
+ 
+ 	private void alfheim$scheduleRelightChecksForBoundary(final Chunk chunk, Chunk nChunk, Chunk sChunk,
+ 			final EnumSkyBlock lightType, final int xOffset, final int zOffset,
+ 			final EnumFacing.AxisDirection axisDirection) {
+ 		if (chunk.alfheim$neighborLightChecks == null)
+ 			return;
+ 
+ 		final int flagIndex = alfheim$getFlagIndex(lightType, xOffset, zOffset, axisDirection, EnumBoundaryFacing.IN); // OUT
+ 																														// checks
+ 																														// from
+ 																														// neighbor
+ 																														// are
+ 																														// already
+ 																														// merged
+ 
+ 		final int flags = chunk.alfheim$neighborLightChecks[flagIndex];
+ 
+ 		if (flags == 0)
+ 			return;
+ 
+ 		if (nChunk == null) {
+ 			nChunk = worldObj.getChunkProvider().getLoadedChunk(chunk.xPosition + xOffset, chunk.zPosition + zOffset);
+ 
+ 			if (nChunk == null)
+ 				return;
+ 		}
+ 
+ 		if (sChunk == null) {
+ 			sChunk = worldObj.getChunkProvider().getLoadedChunk(
+ 					chunk.xPosition + (zOffset != 0 ? axisDirection.getOffset() : 0),
+ 					chunk.zPosition + (xOffset != 0 ? axisDirection.getOffset() : 0));
+ 
+ 			if (sChunk == null)
+ 				return; // Cancel, since the checks in the corner columns require the corner column of
+ 						// sChunk
+ 		}
+ 
+ 		final int reverseIndex = alfheim$getFlagIndex(lightType, -xOffset, -zOffset, axisDirection,
+ 				EnumBoundaryFacing.OUT);
+ 
+ 		chunk.alfheim$neighborLightChecks[flagIndex] = 0;
+ 
+ 		if (alfheim$neighborLightChecks != null)
+ 			nChunk.alfheim$neighborLightChecks[reverseIndex] = 0; // Clear only now that it's clear that the checks
+ 																	// are processed
+ 
+ 		chunk.setChunkModified();
+ 		nChunk.setChunkModified();
+ 
+ 		// Get the area to check
+ 		// Start in the corner...
+ 		int xMin = chunk.xPosition << 4;
+ 		int zMin = chunk.zPosition << 4;
+ 
+ 		// Move to other side of chunk if the direction is positive
+ 		if ((xOffset | zOffset) > 0) {
+ 			xMin += 15 * xOffset;
+ 			zMin += 15 * zOffset;
+ 		}
+ 
+ 		// Shift to other half if necessary (shift perpendicular to dir)
+ 		if (axisDirection == EnumFacing.AxisDirection.POSITIVE) {
+ 			xMin += 8 * (zOffset & 1); // x & 1 is same as abs(x) for x=-1,0,1
+ 			zMin += 8 * (xOffset & 1);
+ 		}
+ 
+ 		// Get maximal values (shift perpendicular to dir)
+ 		final int xMax = xMin + 7 * (zOffset & 1);
+ 		final int zMax = zMin + 7 * (xOffset & 1);
+ 
+ 		for (int y = 0; y < 16; ++y)
+ 			if ((flags & (1 << y)) != 0)
+ 				for (int x = xMin; x <= xMax; ++x)
+ 					for (int z = zMin; z <= zMax; ++z)
+ 						alfheim$scheduleRelightChecksForColumn(lightType, x, z, y << 4, (y << 4) + 15);
+ 	}
+ 
+ 	private void alfheim$initSkylightForSection(final ExtendedBlockStorage extendedBlockStorage) {
+ 		if (worldObj.provider.getHasNoSky())
+ 			return;
+ 
+ 		for (int x = 0; x < 16; ++x) {
+ 			for (int z = 0; z < 16; ++z) {
+ 				if (getHeightValue(x, z) > extendedBlockStorage.getYLocation())
+ 					continue;
+ 
+ 				for (int y = 0; y < 16; ++y)
+ 					extendedBlockStorage.setExtSkylightValue(x, y, z, EnumSkyBlock.SKY.defaultLightValue);
+ 			}
+ 		}
+ 	}
+ 
+ 	private void alfheim$scheduleRelightChecksForColumn(final EnumSkyBlock lightType, final int x, final int z,
+ 			final int yMin, final int yMax) {
+ 		final BlockPos mutableBlockPos = new BlockPos();
+ 
+ 		for (int y = yMin; y <= yMax; ++y)
+ 			worldObj.checkLightFor(lightType, mutableBlockPos.func_181079_c(x, y, z));
+ 	}
+ 
+ 	private static int alfheim$getFlagIndex(final EnumSkyBlock lightType, final int xOffset, final int zOffset,
+ 			final EnumFacing.AxisDirection axisDirection, final EnumBoundaryFacing boundaryFacing) {
+ 		return (lightType == EnumSkyBlock.BLOCK ? 0 : 16) | ((xOffset + 1) << 2) | ((zOffset + 1) << 1)
+ 				| (axisDirection.getOffset() + 1) | boundaryFacing.ordinal();
+ 	}
+ 
+ 	private static int alfheim$getFlagIndex(final EnumSkyBlock lightType, final EnumFacing facing,
+ 			final EnumFacing.AxisDirection axisDirection, final EnumBoundaryFacing boundaryFacing) {
+ 		return alfheim$getFlagIndex(lightType, facing.getFrontOffsetX(), facing.getFrontOffsetZ(), axisDirection,
+ 				boundaryFacing);
+ 	}
+ 
+ 	private static void alfheim$initChunkLighting(final Chunk chunk, final World world) {
+ 		final int xBase = chunk.xPosition << 4;
+ 		final int zBase = chunk.zPosition << 4;
+ 
+ 		final BlockPos mutableBlockPos = new BlockPos(xBase, 0, zBase);
+ 
+ 		if (world.isAreaLoaded(mutableBlockPos.add(-16, 0, -16), mutableBlockPos.add(31, 255, 31), false)) {
+ 			final ExtendedBlockStorage[] extendedBlockStorage = chunk.getBlockStorageArray();
+ 
+ 			for (int i = 0; i < extendedBlockStorage.length; ++i) {
+ 				final ExtendedBlockStorage storage = extendedBlockStorage[i];
+ 
+ 				if (storage == null)
+ 					continue;
+ 
+ 				int yBase = i * 16;
+ 
+ 				for (int y = 0; y < 16; y++) {
+ 					for (int z = 0; z < 16; z++) {
+ 						for (int x = 0; x < 16; x++) {
+ 							if (storage.getBlockByExtId(x, y, z).getLightValue() > 0) {
+ 								mutableBlockPos.func_181079_c(xBase + x, yBase + y, zBase + z);
+ 								world.checkLightFor(EnumSkyBlock.BLOCK, mutableBlockPos);
+ 							}
+ 						}
+ 					}
+ 				}
+ 			}
+ 
+ 			if (!world.provider.getHasNoSky())
+ 				chunk.alfheim$setSkylightUpdatedPublic();
+ 
+ 			chunk.alfheim$setLightInitialized(true);
+ 		}
+ 	}
+ 
+ 	private void alfheim$relightSkylightColumn(final int x, final int z, final int height1, final int height2) {
+ 		final int yMin = Math.min(height1, height2);
+ 		final int yMax = Math.max(height1, height2) - 1;
+ 
+ 		final ExtendedBlockStorage[] sections = getBlockStorageArray();
+ 
+ 		final int xBase = (xPosition << 4) + x;
+ 		final int zBase = (zPosition << 4) + z;
+ 
+ 		alfheim$scheduleRelightChecksForColumn(EnumSkyBlock.SKY, xBase, zBase, yMin, yMax);
+ 
+ 		if (sections[yMin >> 4] == null && yMin > 0) {
+ 			worldObj.checkLightFor(EnumSkyBlock.SKY, new BlockPos(xBase, yMin - 1, zBase));
+ 		}
+ 
+ 		short emptySections = 0;
+ 
+ 		for (int sec = yMax >> 4; sec >= yMin >> 4; --sec) {
+ 			if (sections[sec] == null) {
+ 				emptySections |= (short) (1 << sec);
+ 			}
+ 		}
+ 
+ 		if (emptySections != 0) {
+ 			for (final EnumFacing facing : EnumFacing.HORIZONTALS) {
+ 				final int xOffset = facing.getFrontOffsetX();
+ 				final int zOffset = facing.getFrontOffsetZ();
+ 
+ 				final boolean neighborColumnExists = (((x + xOffset) | (z + zOffset)) & 16) == 0
+ 						// Checks whether the position is at the specified border (the 16 bit is set for
+ 						// both 15+1 and 0-1)
+ 						|| worldObj.getChunkProvider().getLoadedChunk(xPosition + xOffset, zPosition + zOffset) != null;
+ 
+ 				if (neighborColumnExists) {
+ 					for (int sec = yMax >> 4; sec >= yMin >> 4; --sec) {
+ 						if ((emptySections & (1 << sec)) != 0)
+ 							alfheim$scheduleRelightChecksForColumn(EnumSkyBlock.SKY, xBase + xOffset, zBase + zOffset,
+ 									sec << 4, (sec << 4) + 15);
+ 					}
+ 				} else {
+ 					alfheim$initNeighborLightChecks();
+ 
+ 					final EnumFacing.AxisDirection axisDirection = ((facing.getAxis() == EnumFacing.Axis.X ? z : x)
+ 							& 15) < 8 ? EnumFacing.AxisDirection.NEGATIVE : EnumFacing.AxisDirection.POSITIVE;
+ 					alfheim$neighborLightChecks[alfheim$getFlagIndex(EnumSkyBlock.SKY, facing, axisDirection,
+ 							EnumBoundaryFacing.OUT)] |= emptySections;
+ 
+ 					setChunkModified();
+ 				}
+ 			}
+ 		}
+ 	}
+ 
+ 	public LightingEngine alfheim$getLightingEngine() {
+ 		return alfheim$lightingEngine;
+ 	}
+ 
+ 	public boolean alfheim$isLightInitialized() {
+ 		return alfheim$isLightInitialized;
+ 	}
+ 
+ 	public void alfheim$setLightInitialized(final boolean lightInitialized) {
+ 		alfheim$isLightInitialized = lightInitialized;
+ 	}
+ 
+ 	public void alfheim$setSkylightUpdatedPublic() {
+ 		func_177441_y();
+ 	}
+ 
+ 	public void alfheim$initNeighborLightChecks() {
+ 		if (alfheim$neighborLightChecks == null) {
+ 			alfheim$neighborLightChecks = new short[32];
+ 		}
+ 	}
+ 
+ 	public byte alfheim$getCachedLightFor(final EnumSkyBlock lightType, final BlockPos blockPos) {
+ 		final int x = blockPos.x & 15;
+ 		final int y = blockPos.y;
+ 		final int z = blockPos.z & 15;
+ 
+ 		final ExtendedBlockStorage extendedblockstorage = storageArrays[y >> 4];
+ 
+ 		if (extendedblockstorage == null)
+ 			return canSeeSky(blockPos) ? (byte) lightType.defaultLightValue : 0;
+ 		else if (lightType == EnumSkyBlock.SKY)
+ 			return !worldObj.provider.getHasNoSky() ? (byte) extendedblockstorage.getExtSkylightValue(x, y & 15, z) : 0;
+ 		else
+ 			return lightType == EnumSkyBlock.BLOCK ? (byte) extendedblockstorage.getExtBlocklightValue(x, y & 15, z)
+ 					: (byte) lightType.defaultLightValue;
+ 	}

> EOF
