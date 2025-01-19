
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import com.carrotsearch.hppc.cursors.LongCursor;

> DELETE  4  @  4 : 5

> CHANGE  4 : 5  @  4 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  2 : 4  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;

> DELETE  25  @  25 : 26

> DELETE  16  @  16 : 27

> CHANGE  12 : 14  @  12 : 14

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  8 : 9  @  8 : 9

~ 	private final Map<EaglercraftUUID, Entity> entitiesByUuid = Maps.newHashMap();

> CHANGE  23 : 25  @  23 : 26

~ 	public WorldServer(MinecraftServer server, ISaveHandler saveHandlerIn, WorldInfo info, int dimensionId) {
~ 		super(saveHandlerIn, info, WorldProvider.getProviderForDimension(dimensionId), false);

> DELETE  64  @  64 : 65

> DELETE  6  @  6 : 7

> DELETE  11  @  11 : 12

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 3

> DELETE  1  @  1 : 2

> CHANGE  22 : 24  @  22 : 23

~ 			for (int k = 0, l = this.playerEntities.size(); k < l; ++k) {
~ 				EntityPlayer entityplayer = this.playerEntities.get(k);

> CHANGE  15 : 17  @  15 : 16

~ 		for (int k = 0, l = this.playerEntities.size(); k < l; ++k) {
~ 			EntityPlayer entityplayer = this.playerEntities.get(k);

> CHANGE  16 : 19  @  16 : 18

~ 		if (this.allPlayersSleeping) {
~ 			for (int k = 0, l = this.playerEntities.size(); k < l; ++k) {
~ 				EntityPlayer entityplayer = this.playerEntities.get(k);

> CHANGE  36 : 39  @  36 : 39

~ 			for (LongCursor chunkcoordintpair1 : this.activeChunkSet) {
~ 				long l = chunkcoordintpair1.value;
~ 				this.getChunkFromChunkCoords((int) (l & 4294967295L), (int) (l >>> 32)).func_150804_b(false);

> CHANGE  6 : 13  @  6 : 11

~ 			for (LongCursor chunkcoordintpair : this.activeChunkSet) {
~ 				long ll = chunkcoordintpair.value;
~ 				int chunkXPos = (int) (ll & 4294967295L);
~ 				int chunkZPos = (int) (ll >>> 32);
~ 				int k = chunkXPos * 16;
~ 				int l = chunkZPos * 16;
~ 				Chunk chunk = this.getChunkFromChunkCoords(chunkXPos, chunkZPos);

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 2

> DELETE  11  @  11 : 12

> DELETE  19  @  19 : 20

> CHANGE  2 : 5  @  2 : 3

~ 					ExtendedBlockStorage[] vigg = chunk.getBlockStorageArray();
~ 					for (int m = 0; m < vigg.length; ++m) {
~ 						ExtendedBlockStorage extendedblockstorage = vigg[m];

> INSERT  15 : 16  @  15

+ 									++EaglerMinecraftServer.counterTileUpdate;

> DELETE  5  @  5 : 7

> DELETE  1  @  1 : 2

> INSERT  38 : 39  @  38

+ 						++EaglerMinecraftServer.counterTileUpdate;

> DELETE  65  @  65 : 67

> DELETE  12  @  12 : 14

> INSERT  14 : 15  @  14

+ 								++EaglerMinecraftServer.counterTileUpdate;

> DELETE  15  @  15 : 16

> CHANGE  76 : 77  @  76 : 77

~ 		ArrayList<TileEntity> arraylist = Lists.newArrayList();

> CHANGE  1 : 15  @  1 : 7

~ 		for (int chunkX = (minX >> 4); chunkX <= ((maxX - 1) >> 4); chunkX++) {
~ 			for (int chunkZ = (minZ >> 4); chunkZ <= ((maxZ - 1) >> 4); chunkZ++) {
~ 				Chunk chunk = getChunkFromChunkCoords(chunkX, chunkZ);
~ 				if (chunk == null) {
~ 					continue;
~ 				}
~ 
~ 				for (TileEntity tileentity : chunk.getTileEntityMap().values()) {
~ 					BlockPos pos = tileentity.getPos();
~ 					if ((pos.x >= minX) && (pos.y >= minY) && (pos.z >= minZ) && (pos.x < maxX) && (pos.y < maxY)
~ 							&& (pos.z < maxZ)) {
~ 						arraylist.add(tileentity);
~ 					}
~ 				}

> CHANGE  60 : 61  @  60 : 61

~ 			EaglercraftRandom random = new EaglercraftRandom(this.getSeed());

> CHANGE  49 : 50  @  49 : 50

~ 	public void saveAllChunks(boolean progressCallback, IProgressUpdate parIProgressUpdate) {

> CHANGE  12 : 15  @  12 : 13

~ 			List<Chunk> lst = Lists.newArrayList(this.theChunkProviderServer.func_152380_a());
~ 			for (int i = 0, l = lst.size(); i < l; ++i) {
~ 				Chunk chunk = lst.get(i);

> CHANGE  14 : 15  @  14 : 16

~ 	protected void saveLevel() {

> CHANGE  16 : 17  @  16 : 17

~ 		this.entitiesById.put(entity.getEntityId(), entity);

> CHANGE  4 : 5  @  4 : 5

~ 				this.entitiesById.put(aentity[i].getEntityId(), aentity[i]);

> CHANGE  7 : 8  @  7 : 8

~ 		this.entitiesById.remove(entity.getEntityId());

> CHANGE  4 : 5  @  4 : 5

~ 				this.entitiesById.remove(aentity[i].getEntityId());

> CHANGE  28 : 31  @  28 : 29

~ 		List<EntityPlayer> lst = this.playerEntities;
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			EntityPlayer entityplayer = lst.get(i);

> CHANGE  13 : 16  @  13 : 15

~ 		ServerBlockEventList lst = this.field_147490_S[this.blockEventCacheIndex];
~ 		for (int k = 0, l = lst.size(); k < l; ++k) {
~ 			if (lst.get(k).equals(blockeventdata)) {

> CHANGE  12 : 15  @  12 : 13

~ 			ServerBlockEventList lst = this.field_147490_S[i];
~ 			for (int k = 0, l = lst.size(); k < l; ++k) {
~ 				BlockEventData blockeventdata = lst.get(k);

> CHANGE  97 : 98  @  97 : 98

~ 	public Entity getEntityFromUuid(EaglercraftUUID uuid) {

> CHANGE  3 : 5  @  3 : 5

~ 	public void addScheduledTask(Runnable runnable) {
~ 		this.mcServer.addScheduledTask(runnable);

> DELETE  2  @  2 : 6

> EOF
