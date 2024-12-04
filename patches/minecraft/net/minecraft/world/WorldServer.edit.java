
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  6  @  6 : 7

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

> DELETE  48  @  48 : 49

> DELETE  2  @  2 : 3

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

> CHANGE  144 : 145  @  144 : 145

~ 			EaglercraftRandom random = new EaglercraftRandom(this.getSeed());

> CHANGE  49 : 50  @  49 : 50

~ 	public void saveAllChunks(boolean progressCallback, IProgressUpdate parIProgressUpdate) {

> CHANGE  12 : 15  @  12 : 13

~ 			List<Chunk> lst = Lists.newArrayList(this.theChunkProviderServer.func_152380_a());
~ 			for (int i = 0, l = lst.size(); i < l; ++i) {
~ 				Chunk chunk = lst.get(i);

> CHANGE  14 : 15  @  14 : 16

~ 	protected void saveLevel() {

> CHANGE  63 : 66  @  63 : 64

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
