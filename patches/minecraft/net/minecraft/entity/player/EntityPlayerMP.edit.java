
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 8  @  2

+ import com.carrotsearch.hppc.IntArrayDeque;
+ import com.carrotsearch.hppc.IntDeque;
+ import com.carrotsearch.hppc.LongHashSet;
+ import com.carrotsearch.hppc.LongSet;
+ import com.carrotsearch.hppc.cursors.IntCursor;
+ import com.carrotsearch.hppc.cursors.LongCursor;

> CHANGE  2 : 6  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
~ import net.lax1dude.eaglercraft.v1_8.sp.server.skins.PlayerTextureData;
~ 

> DELETE  17  @  17 : 18

> DELETE  51  @  51 : 52

> DELETE  15  @  15 : 16

> CHANGE  6 : 10  @  6 : 8

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> INSERT  2 : 3  @  2

+ 

> CHANGE  7 : 9  @  7 : 9

~ 	public final LongSet loadedChunks = new LongHashSet();
~ 	private final IntDeque destroyedItemsNetCache = new IntArrayDeque();

> CHANGE  9 : 10  @  9 : 10

~ 	private long playerLastActiveTime = EagRuntime.steadyTimeMillis();

> INSERT  5 : 8  @  5

+ 	public byte[] updateCertificate = null;
+ 	public PlayerTextureData textureData = null;
+ 	public EaglercraftUUID clientBrandUUID = null;

> CHANGE  87 : 88  @  87 : 88

~ 		if (!this.openContainer.canInteractWith(this)) {

> DELETE  7  @  7 : 8

> CHANGE  2 : 4  @  2 : 5

~ 			while (!destroyedItemsNetCache.isEmpty() && j < i) {
~ 				aint[j++] = destroyedItemsNetCache.removeFirst();

> CHANGE  6 : 9  @  6 : 9

~ 			ArrayList<Chunk> arraylist = Lists.newArrayList();
~ 			Iterator<LongCursor> iterator1 = this.loadedChunks.iterator();
~ 			ArrayList<TileEntity> arraylist1 = Lists.newArrayList();

> CHANGE  2 : 11  @  2 : 15

~ 				long l = iterator1.next().value;
~ 				int chunkXPos = (int) (l & 4294967295L);
~ 				int chunkZPos = (int) (l >>> 32);
~ 				if (this.worldObj.isBlockLoaded(new BlockPos(chunkXPos << 4, 0, chunkZPos << 4))) {
~ 					Chunk chunk = this.worldObj.getChunkFromChunkCoords(chunkXPos, chunkZPos);
~ 					if (chunk.isPopulated()) {
~ 						arraylist.add(chunk);
~ 						arraylist1.addAll(((WorldServer) this.worldObj).getTileEntitiesIn(chunkXPos * 16, 0,
~ 								chunkZPos * 16, chunkXPos * 16 + 16, 256, chunkZPos * 16 + 16));

> DELETE  1  @  1 : 3

> CHANGE  5 : 6  @  5 : 7

~ 					this.playerNetServerHandler.sendPacket(new S21PacketChunkData(arraylist.get(0), true, '\uffff'));

> CHANGE  4 : 6  @  4 : 6

~ 				for (int i = 0, l = arraylist1.size(); i < l; ++i) {
~ 					this.sendTileEntityUpdate(arraylist1.get(i));

> CHANGE  2 : 6  @  2 : 4

~ 				for (int i = 0, l = arraylist.size(); i < l; ++i) {
~ 					Chunk c = arraylist.get(i);
~ 					this.getServerForPlayer().getEntityTracker().func_85172_a(this, c);
~ 					this.loadedChunks.removeAll(c.getChunkCoordLong());

> CHANGE  140 : 141  @  140 : 141

~ 					.get(EntityList.getEntityID(entitylivingbase));

> CHANGE  377 : 380  @  377 : 378

~ 		for (IntCursor cur : ((EntityPlayerMP) oldPlayer).destroyedItemsNetCache) {
~ 			destroyedItemsNetCache.addLast(cur.value);
~ 		}

> CHANGE  62 : 63  @  62 : 63

~ 		if ("seed".equals(s)) {

> CHANGE  2 : 3  @  2 : 10

~ 			return this.mcServer.getConfigurationManager().canSendCommands(this.getGameProfile());

> CHANGE  6 : 7  @  6 : 10

~ 		return "channel:" + this.playerNetServerHandler.netManager.playerChannel;

> INSERT  6 : 7  @  6

+ 		this.mcServer.getConfigurationManager().updatePlayerViewDistance(this, packetIn.getViewDistance());

> CHANGE  27 : 28  @  27 : 28

~ 			this.destroyedItemsNetCache.addLast(parEntity.getEntityId());

> EOF
