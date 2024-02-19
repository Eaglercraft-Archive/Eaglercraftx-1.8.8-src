
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  5 : 8  @  5 : 9

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;
~ import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
~ 

> CHANGE  5 : 9  @  5 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.internal.vfs2.VFile2;

> DELETE  6  @  6 : 7

> DELETE  23  @  23 : 25

> DELETE  2  @  2 : 9

> INSERT  3 : 4  @  3

+ import net.minecraft.util.ChatComponentText;

> CHANGE  12 : 15  @  12 : 14

~ import net.lax1dude.eaglercraft.v1_8.sp.server.socket.IntegratedServerPlayerNetworkManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  2 : 3  @  2 : 6

~ 

> CHANGE  4 : 6  @  4 : 10

~ 	private final Map<EaglercraftUUID, EntityPlayerMP> uuidToPlayerMap = Maps.newHashMap();
~ 	private final Map<String, StatisticsFile> playerStatFiles;

> CHANGE  3 : 4  @  3 : 4

~ 	protected int viewDistance;

> INSERT  4 : 7  @  4

+ 	private WorldSettings.GameType lanGamemode = WorldSettings.GameType.SURVIVAL;
+ 	private boolean lanCheats = false;
+ 

> DELETE  1  @  1 : 5

> DELETE  2  @  2 : 4

> CHANGE  3 : 5  @  3 : 9

~ 	public void initializeConnectionToPlayer(IntegratedServerPlayerNetworkManager netManager, EntityPlayerMP playerIn) {
~ 		GameProfile gameprofile1 = playerIn.getGameProfile();

> CHANGE  3 : 4  @  3 : 7

~ 		String s1 = "channel:" + netManager.playerChannel;

> DELETE  12  @  12 : 14

> INSERT  1 : 4  @  1

+ 				.sendPacket(new S3FPacketCustomPayload("MC|Brand", (PacketBuffer) (new PacketBuffer(Unpooled.buffer()))
+ 						.writeString(this.getServerInstance().getServerModName())));
+ 		nethandlerplayserver

> CHANGE  9 : 11  @  9 : 17

~ 		chatcomponenttranslation = new ChatComponentTranslation("multiplayer.player.joined",
~ 				new Object[] { playerIn.getDisplayName() });

> INSERT  2 : 10  @  2

+ 		if (playerIn.canCommandSenderUseCommand(2, "give")) {
+ 			ChatComponentText shaderF4Msg = new ChatComponentText("[EaglercraftX] ");
+ 			shaderF4Msg.getChatStyle().setColor(EnumChatFormatting.GOLD);
+ 			ChatComponentTranslation shaderF4Msg2 = new ChatComponentTranslation("command.skull.tip");
+ 			shaderF4Msg2.getChatStyle().setColor(EnumChatFormatting.AQUA);
+ 			shaderF4Msg.appendSibling(shaderF4Msg2);
+ 			playerIn.addChatMessage(shaderF4Msg);
+ 		}

> INSERT  23 : 34  @  23

+ 		if (EagRuntime.getConfiguration().allowUpdateSvc()) {
+ 			for (EntityPlayerMP playerItr : playerEntityList) {
+ 				if (playerItr != playerIn && playerItr.updateCertificate != null) {
+ 					nethandlerplayserver
+ 							.sendPacket(new S3FPacketCustomPayload("EAG|UpdateCert-1.8",
+ 									new PacketBuffer(Unpooled
+ 											.buffer(playerItr.updateCertificate, playerItr.updateCertificate.length)
+ 											.writerIndex(playerItr.updateCertificate.length))));
+ 				}
+ 			}
+ 		}

> CHANGE  88 : 89  @  88 : 89

~ 		StatisticsFile statisticsfile = (StatisticsFile) this.playerStatFiles.get(entityplayermp.getName());

> CHANGE  39 : 40  @  39 : 40

~ 		EaglercraftUUID uuid = playerIn.getUniqueID();

> CHANGE  3 : 4  @  3 : 4

~ 			this.playerStatFiles.remove(entityplayermp.getName());

> CHANGE  6 : 13  @  6 : 13

~ 	public String allowUserToConnect(GameProfile gameprofile) {
~ 		return this.playerEntityList.size() >= this.maxPlayers && !this.func_183023_f(gameprofile)
~ 				? "The server is full!"
~ 				: (doesPlayerAlreadyExist(gameprofile)
~ 						? "\"" + gameprofile.getName() + "\" is already playing on this world!"
~ 						: null);
~ 	}

> CHANGE  1 : 6  @  1 : 9

~ 	private boolean doesPlayerAlreadyExist(GameProfile gameprofile) {
~ 		for (EntityPlayerMP player : this.playerEntityList) {
~ 			if (player.getName().equalsIgnoreCase(gameprofile.getName())
~ 					|| player.getUniqueID().equals(gameprofile.getId())) {
~ 				return true;

> DELETE  1  @  1 : 7

> INSERT  1 : 2  @  1

+ 		return false;

> CHANGE  3 : 4  @  3 : 4

~ 		EaglercraftUUID uuid = EntityPlayer.getUUID(profile);

> CHANGE  4 : 6  @  4 : 5

~ 			if (entityplayermp.getUniqueID().equals(uuid)
~ 					|| entityplayermp.getName().equalsIgnoreCase(profile.getName())) {

> CHANGE  9 : 10  @  9 : 10

~ 		for (EntityPlayerMP entityplayermp1 : (ArrayList<EntityPlayerMP>) arraylist) {

> DELETE  254  @  254 : 271

> CHANGE  1 : 2  @  1 : 3

~ 		return true;

> CHANGE  3 : 4  @  3 : 4

~ 		return lanCheats

> DELETE  42  @  42 : 69

> CHANGE  67 : 73  @  67 : 72

~ 		if (parEntityPlayerMP2 == null || parEntityPlayerMP2.getName().equals(mcServer.getServerOwner())) {
~ 			if (parEntityPlayerMP2 != null) {
~ 				worldIn.theItemInWorldManager.setGameType(parEntityPlayerMP2.theItemInWorldManager.getGameType());
~ 			} else if (this.gameType != null) {
~ 				worldIn.theItemInWorldManager.setGameType(this.gameType);
~ 			}

> CHANGE  1 : 5  @  1 : 2

~ 			worldIn.theItemInWorldManager.initializeGameType(parWorld.getWorldInfo().getGameType());
~ 		} else {
~ 			parEntityPlayerMP2.theItemInWorldManager.setGameType(lanGamemode);
~ 		}

> CHANGE  25 : 27  @  25 : 27

~ 		String name = playerIn.getName();
~ 		StatisticsFile statisticsfile = (StatisticsFile) this.playerStatFiles.get(name);

> CHANGE  1 : 2  @  1 : 2

~ 			VFile2 file1 = new VFile2(this.mcServer.worldServerForDimension(0).getSaveHandler().getWorldDirectory(),

> CHANGE  1 : 2  @  1 : 9

~ 			VFile2 file2 = new VFile2(file1, name + ".json");

> CHANGE  2 : 3  @  2 : 3

~ 			this.playerStatFiles.put(name, statisticsfile);

> DELETE  13  @  13 : 14

> CHANGE  7 : 8  @  7 : 8

~ 	public EntityPlayerMP getPlayerByUUID(EaglercraftUUID playerUUID) {

> INSERT  6 : 20  @  6

+ 
+ 	public void updatePlayerViewDistance(EntityPlayerMP entityPlayerMP, int viewDistance2) {
+ 		if (entityPlayerMP.getName().equals(mcServer.getServerOwner())) {
+ 			if (viewDistance != viewDistance2) {
+ 				logger.info("Owner is setting view distance: {}", viewDistance2);
+ 				setViewDistance(viewDistance2);
+ 			}
+ 		}
+ 	}
+ 
+ 	public void configureLAN(int gamemode, boolean cheats) {
+ 		lanGamemode = WorldSettings.GameType.getByID(gamemode);
+ 		lanCheats = cheats;
+ 	}

> EOF
