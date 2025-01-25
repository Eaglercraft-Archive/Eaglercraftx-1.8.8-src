
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import com.carrotsearch.hppc.IntObjectHashMap;
+ import com.carrotsearch.hppc.IntObjectMap;
+ import com.carrotsearch.hppc.LongHashSet;
+ import com.carrotsearch.hppc.LongSet;

> INSERT  3 : 6  @  3

+ 
+ import dev.redstudio.alfheim.lighting.LightingEngine;
+ 

> DELETE  5  @  5 : 6

> CHANGE  1 : 6  @  1 : 2

~ 
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ 

> INSERT  1 : 2  @  1

+ 

> DELETE  16  @  16 : 17

> DELETE  9  @  9 : 10

> CHANGE  5 : 6  @  5 : 19

~ import net.minecraft.world.biome.BiomeColorHelper;

> CHANGE  15 : 17  @  15 : 17

~ 	public final Set<TileEntity> loadedTileEntityList = Sets.newIdentityHashSet();
~ 	public final Set<TileEntity> tickableTileEntities = Sets.newIdentityHashSet();

> CHANGE  4 : 5  @  4 : 5

~ 	protected final IntObjectMap<Entity> entitiesById = new IntObjectHashMap<>();

> CHANGE  2 : 3  @  2 : 3

~ 	protected int updateLCG = (new EaglercraftRandom()).nextInt();

> CHANGE  6 : 7  @  6 : 7

~ 	public final EaglercraftRandom rand = new EaglercraftRandom();

> DELETE  8  @  8 : 9

> CHANGE  2 : 3  @  2 : 4

~ 	protected LongSet activeChunkSet = new LongHashSet();

> CHANGE  5 : 6  @  5 : 6

~ 	public final boolean isRemote;

> CHANGE  1 : 4  @  1 : 3

~ 	private LightingEngine alfheim$lightingEngine;
~ 
~ 	protected World(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, boolean client) {

> DELETE  3  @  3 : 4

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 3

> INSERT  1 : 3  @  1

+ 		this.isRemote = client;
+ 		this.alfheim$lightingEngine = new LightingEngine(this);

> CHANGE  17 : 19  @  17 : 18

~ 						return CrashReportCategory
~ 								.getCoordinateInfo(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ()));

> INSERT  9 : 13  @  9

+ 	public int getBiomeColorForCoords(BlockPos var1, int index) {
+ 		return BiomeColorHelper.getBiomeColorForCoordsOld(this, var1, index);
+ 	}
+ 

> CHANGE  34 : 35  @  34 : 35

~ 		return !this.isValid(pos) ? false : this.isChunkLoaded(pos.x >> 4, pos.z >> 4, true);

> CHANGE  3 : 4  @  3 : 4

~ 		return !this.isValid(pos) ? false : this.isChunkLoaded(pos.x >> 4, pos.z >> 4, allowEmpty);

> CHANGE  53 : 54  @  53 : 54

~ 		return this.chunkProvider.provideChunk(pos.x >> 4, pos.z >> 4);

> INSERT  2 : 8  @  2

+ 	public Chunk getChunkFromBlockCoordsIfLoaded(BlockPos pos) {
+ 		int x = pos.x >> 4;
+ 		int z = pos.z >> 4;
+ 		return this.chunkProvider.chunkExists(x, z) ? this.chunkProvider.provideChunk(x, z) : null;
+ 	}
+ 

> INSERT  4 : 8  @  4

+ 	public Chunk getChunkFromChunkCoordsIfLoaded(int chunkX, int chunkZ) {
+ 		return this.chunkProvider.chunkExists(chunkX, chunkZ) ? this.chunkProvider.provideChunk(chunkX, chunkZ) : null;
+ 	}
+ 

> DELETE  15  @  15 : 16

> DELETE  1  @  1 : 2

> CHANGE  131 : 132  @  131 : 132

~ 							return HString.format("ID #%d (%s // %s)",

> CHANGE  58 : 60  @  58 : 68

~ 		if (!checkNeighbors)
~ 			return getLight(pos);

> CHANGE  1 : 2  @  1 : 4

~ 		final IBlockState blockState = getBlockState(pos);

> CHANGE  1 : 4  @  1 : 23

~ 		return Math.max(blockState.getBlock().alfheim$getLightFor(blockState, this, EnumSkyBlock.BLOCK, pos),
~ 				blockState.getBlock().alfheim$getLightFor(blockState, this, EnumSkyBlock.SKY, pos)
~ 						- skylightSubtracted);

> CHANGE  32 : 34  @  32 : 71

~ 		IBlockState state = getBlockState(pos);
~ 		return state.getBlock().alfheim$getLightFor(state, this, type, pos);

> CHANGE  37 : 43  @  37 : 38

~ 		if (lightValue < 0) {
~ 			j += -lightValue;
~ 			if (j > 15) {
~ 				j = 15;
~ 			}
~ 		} else if (j < lightValue) {

> INSERT  19 : 28  @  19

+ 	public IBlockState getBlockStateIfLoaded(BlockPos pos) {
+ 		if (!this.isValid(pos)) {
+ 			return Blocks.air.getDefaultState();
+ 		} else {
+ 			Chunk chunk = this.getChunkFromBlockCoordsIfLoaded(pos);
+ 			return chunk != null ? chunk.getBlockState(pos) : null;
+ 		}
+ 	}
+ 

> CHANGE  23 : 26  @  23 : 24

~ 				IBlockState iblockstate = this.getBlockStateIfLoaded(blockpos);
~ 				if (iblockstate == null)
~ 					return null;

> CHANGE  98 : 101  @  98 : 99

~ 					IBlockState iblockstate1 = this.getBlockStateIfLoaded(blockpos);
~ 					if (iblockstate1 == null)
~ 						return null;

> CHANGE  178 : 179  @  178 : 179

~ 		BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  72 : 73  @  72 : 73

~ 		BlockPos blockpos$mutableblockpos = new BlockPos();

> DELETE  185  @  185 : 188

> DELETE  23  @  23 : 24

> DELETE  16  @  16 : 17

> DELETE  12  @  12 : 13

> DELETE  11  @  11 : 13

> DELETE  11  @  11 : 12

> DELETE  2  @  2 : 3

> DELETE  36  @  36 : 37

> DELETE  20  @  20 : 22

> DELETE  48  @  48 : 49

> DELETE  38  @  38 : 39

> CHANGE  37 : 38  @  37 : 38

~ 		BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  22 : 23  @  22 : 23

~ 		BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  23 : 24  @  23 : 24

~ 			BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  28 : 29  @  28 : 29

~ 			BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  38 : 39  @  38 : 39

~ 		BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  22 : 23  @  22 : 23

~ 		BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  122 : 123  @  122 : 123

~ 				tileEntityIn.setPos(new BlockPos(pos));

> CHANGE  101 : 102  @  101 : 102

~ 		if (!this.provider.getHasNoSky() && this.getGameRules().getBoolean("doWeatherCycle")) {

> CHANGE  12 : 13  @  12 : 13

~ 						this.worldInfo.setThunderTime((this.rand.nextInt(12000) / 2) + 3600);

> CHANGE  1 : 2  @  1 : 2

~ 						this.worldInfo.setThunderTime((this.rand.nextInt(168000) + 12000) * 2);

> CHANGE  20 : 21  @  20 : 21

~ 						this.worldInfo.setRainTime((this.rand.nextInt(12000) + 12000) / 2);

> CHANGE  1 : 2  @  1 : 2

~ 						this.worldInfo.setRainTime((this.rand.nextInt(168000) + 12000) * 2);

> DELETE  23  @  23 : 24

> INSERT  1 : 7  @  1

+ 		int l = this.getRenderDistanceChunks() - 1;
+ 		if (l > 7)
+ 			l = 7;
+ 		else if (l < 1)
+ 			l = 1;
+ 

> DELETE  4  @  4 : 5

> CHANGE  3 : 4  @  3 : 4

~ 					this.activeChunkSet.add(ChunkCoordIntPair.chunkXZ2Int(i1 + j, j1 + k));

> DELETE  4  @  4 : 5

> DELETE  4  @  4 : 5

> DELETE  9  @  9 : 10

> DELETE  5  @  5 : 6

> DELETE  23  @  23 : 24

> CHANGE  7 : 8  @  7 : 8

~ 	public void forceBlockUpdateTick(Block blockType, BlockPos pos, EaglercraftRandom random) {

> CHANGE  28 : 31  @  28 : 30

~ 					BlockPos tmp = new BlockPos();
~ 					boolean flag = this.isWater(pos.west(tmp)) && this.isWater(pos.east(tmp))
~ 							&& this.isWater(pos.north(tmp)) && this.isWater(pos.south(tmp));

> CHANGE  63 : 66  @  63 : 64

~ 				EnumFacing[] facings = EnumFacing._VALUES;
~ 				for (int m = 0; m < facings.length; ++m) {
~ 					EnumFacing enumfacing = facings[m];

> CHANGE  17 : 19  @  17 : 127

~ 		alfheim$lightingEngine.scheduleLightUpdate(lightType, pos);
~ 		return true;

> CHANGE  28 : 31  @  28 : 31

~ 				Chunk chunk = this.getChunkFromChunkCoordsIfLoaded(i1, j1);
~ 				if (chunk != null) {
~ 					chunk.getEntitiesWithinAABBForEntity(entityIn, boundingBox, arraylist, predicate);

> CHANGE  10 : 13  @  10 : 12

~ 		for (int i = 0, l = this.loadedEntityList.size(); i < l; ++i) {
~ 			Entity entity = this.loadedEntityList.get(i);
~ 			if (entityType.isAssignableFrom(entity.getClass()) && filter.apply((T) entity)) {

> CHANGE  10 : 13  @  10 : 12

~ 		for (int i = 0, l = this.playerEntities.size(); i < l; ++i) {
~ 			Entity entity = this.playerEntities.get(i);
~ 			if (playerType.isAssignableFrom(entity.getClass()) && filter.apply((T) entity)) {

> CHANGE  51 : 52  @  51 : 52

~ 		return this.entitiesById.get(id);

> CHANGE  16 : 18  @  16 : 17

~ 		for (int j = 0, l = this.loadedEntityList.size(); j < l; ++j) {
~ 			Entity entity = this.loadedEntityList.get(j);

> CHANGE  51 : 53  @  51 : 52

~ 		BlockPos tmp = new BlockPos();
~ 		i = Math.max(i, this.getStrongPower(pos.down(tmp), EnumFacing.DOWN));

> CHANGE  3 : 4  @  3 : 4

~ 			i = Math.max(i, this.getStrongPower(pos.up(tmp), EnumFacing.UP));

> CHANGE  3 : 4  @  3 : 4

~ 				i = Math.max(i, this.getStrongPower(pos.north(tmp), EnumFacing.NORTH));

> CHANGE  3 : 4  @  3 : 4

~ 					i = Math.max(i, this.getStrongPower(pos.south(tmp), EnumFacing.SOUTH));

> CHANGE  3 : 4  @  3 : 4

~ 						i = Math.max(i, this.getStrongPower(pos.west(tmp), EnumFacing.WEST));

> CHANGE  3 : 4  @  3 : 4

~ 							i = Math.max(i, this.getStrongPower(pos.east(tmp), EnumFacing.EAST));

> CHANGE  19 : 26  @  19 : 25

~ 		BlockPos tmp = new BlockPos(0, 0, 0);
~ 		return this.getRedstonePower(pos.down(tmp), EnumFacing.DOWN) > 0 ? true
~ 				: (this.getRedstonePower(pos.up(tmp), EnumFacing.UP) > 0 ? true
~ 						: (this.getRedstonePower(pos.north(tmp), EnumFacing.NORTH) > 0 ? true
~ 								: (this.getRedstonePower(pos.south(tmp), EnumFacing.SOUTH) > 0 ? true
~ 										: (this.getRedstonePower(pos.west(tmp), EnumFacing.WEST) > 0 ? true
~ 												: this.getRedstonePower(pos.east(tmp), EnumFacing.EAST) > 0))));

> CHANGE  5 : 10  @  5 : 7

~ 		EnumFacing[] facings = EnumFacing._VALUES;
~ 		BlockPos tmp = new BlockPos(0, 0, 0);
~ 		for (int k = 0; k < facings.length; ++k) {
~ 			EnumFacing enumfacing = facings[k];
~ 			int j = this.getRedstonePower(pos.offsetEvenFaster(enumfacing, tmp), enumfacing);

> CHANGE  59 : 60  @  59 : 60

~ 	public EntityPlayer getPlayerEntityByUUID(EaglercraftUUID uuid) {

> DELETE  13  @  13 : 17

> CHANGE  180 : 181  @  180 : 181

~ 	public EaglercraftRandom setRandomSeed(int parInt1, int parInt2, int parInt3) {

> CHANGE  67 : 70  @  67 : 68

~ 		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 		for (int i = 0; i < facings.length; ++i) {
~ 			EnumFacing enumfacing = facings[i];

> INSERT  61 : 64  @  61

+ 		if (!MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
+ 				.getBoolean("loadSpawnChunks"))
+ 			return false;

> INSERT  6 : 10  @  6

+ 
+ 	public LightingEngine alfheim$getLightingEngine() {
+ 		return alfheim$lightingEngine;
+ 	}

> EOF
