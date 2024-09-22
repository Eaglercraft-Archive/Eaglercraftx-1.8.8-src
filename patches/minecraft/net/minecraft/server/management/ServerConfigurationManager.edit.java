
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

> DELETE  23  @  23 : 34

> INSERT  3 : 4  @  3

+ import net.minecraft.util.ChatComponentText;

> CHANGE  12 : 22  @  12 : 14

~ import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageConstants;
~ import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageProtocol;
~ import net.lax1dude.eaglercraft.v1_8.socket.protocol.client.GameProtocolMessageController;
~ import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.server.SPacketUpdateCertEAG;
~ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;
~ import net.lax1dude.eaglercraft.v1_8.sp.server.WorldsDB;
~ import net.lax1dude.eaglercraft.v1_8.sp.server.socket.IntegratedServerPlayerNetworkManager;
~ import net.lax1dude.eaglercraft.v1_8.sp.server.voice.IntegratedVoiceService;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  2  @  2 : 6

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

> CHANGE  2 : 3  @  2 : 5

~ 		this.maxPlayers = 100;

> CHANGE  2 : 6  @  2 : 8

~ 	public void initializeConnectionToPlayer(IntegratedServerPlayerNetworkManager netManager, EntityPlayerMP playerIn,
~ 			int protocolVersion, EaglercraftUUID clientBrandUUID) {
~ 		playerIn.clientBrandUUID = clientBrandUUID;
~ 		GameProfile gameprofile1 = playerIn.getGameProfile();

> CHANGE  3 : 4  @  3 : 7

~ 		String s1 = "channel:" + netManager.playerChannel;

> INSERT  8 : 12  @  8

+ 		nethandlerplayserver.setEaglerMessageController(new GameProtocolMessageController(
+ 				GamePluginMessageProtocol.getByVersion(protocolVersion), GamePluginMessageConstants.SERVER_TO_CLIENT,
+ 				GameProtocolMessageController.createServerHandler(protocolVersion, nethandlerplayserver),
+ 				(ch, msg) -> nethandlerplayserver.sendPacket(new S3FPacketCustomPayload(ch, msg))));

> DELETE  4  @  4 : 6

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

> INSERT  23 : 31  @  23

+ 		if (EagRuntime.getConfiguration().allowUpdateSvc()) {
+ 			for (int i = 0, l = playerEntityList.size(); i < l; ++i) {
+ 				EntityPlayerMP playerItr = playerEntityList.get(i);
+ 				if (playerItr != playerIn && playerItr.updateCertificate != null) {
+ 					nethandlerplayserver.sendEaglerMessage(new SPacketUpdateCertEAG(playerItr.updateCertificate));
+ 				}
+ 			}
+ 		}

> CHANGE  12 : 15  @  12 : 14

~ 				List<Packet> lst = scoreboardIn.func_96550_d(scoreobjective);
~ 				for (int j = 0, l = lst.size(); j < l; ++j) {
~ 					playerIn.playerNetServerHandler.sendPacket(lst.get(j));

> CHANGE  74 : 75  @  74 : 75

~ 		StatisticsFile statisticsfile = (StatisticsFile) this.playerStatFiles.get(entityplayermp.getName());

> CHANGE  39 : 40  @  39 : 40

~ 		EaglercraftUUID uuid = playerIn.getUniqueID();

> CHANGE  3 : 4  @  3 : 4

~ 			this.playerStatFiles.remove(entityplayermp.getName());

> INSERT  2 : 9  @  2

+ 		((EaglerMinecraftServer) mcServer).getSkinService().unregisterPlayer(uuid);
+ 		((EaglerMinecraftServer) mcServer).getCapeService().unregisterPlayer(uuid);
+ 		IntegratedVoiceService vcs = ((EaglerMinecraftServer) mcServer).getVoiceService();
+ 		if (vcs != null) {
+ 			vcs.handlePlayerLoggedOut(playerIn);
+ 		}
+ 

> CHANGE  4 : 9  @  4 : 11

~ 	public String allowUserToConnect(GameProfile gameprofile) {
~ 		return doesPlayerAlreadyExist(gameprofile)
~ 				? "\"" + gameprofile.getName() + "\" is already playing on this world!"
~ 				: null;
~ 	}

> CHANGE  1 : 7  @  1 : 9

~ 	private boolean doesPlayerAlreadyExist(GameProfile gameprofile) {
~ 		for (int i = 0, l = playerEntityList.size(); i < l; ++i) {
~ 			EntityPlayerMP player = playerEntityList.get(i);
~ 			if (player.getName().equalsIgnoreCase(gameprofile.getName())
~ 					|| player.getUniqueID().equals(gameprofile.getId())) {
~ 				return true;

> DELETE  1  @  1 : 7

> INSERT  1 : 2  @  1

+ 		return false;

> CHANGE  3 : 5  @  3 : 5

~ 		EaglercraftUUID uuid = EntityPlayer.getUUID(profile);
~ 		ArrayList<EntityPlayerMP> arraylist = Lists.newArrayList();

> CHANGE  1 : 2  @  1 : 2

~ 		for (int i = 0, l = this.playerEntityList.size(); i < l; ++i) {

> CHANGE  1 : 3  @  1 : 2

~ 			if (entityplayermp.getUniqueID().equals(uuid)
~ 					|| entityplayermp.getName().equalsIgnoreCase(profile.getName())) {

> CHANGE  9 : 11  @  9 : 11

~ 		for (int i = 0, l = arraylist.size(); i < l; ++i) {
~ 			arraylist.get(i).playerNetServerHandler.kickPlayerFromServer("You logged in from another location");

> INSERT  32 : 34  @  32

+ 		entityplayermp.updateCertificate = playerIn.updateCertificate;
+ 		entityplayermp.clientBrandUUID = playerIn.clientBrandUUID;

> DELETE  75  @  75 : 76

> DELETE  35  @  35 : 36

> DELETE  1  @  1 : 2

> DELETE  8  @  8 : 10

> CHANGE  49 : 50  @  49 : 50

~ 			for (int i = 0, l = this.playerEntityList.size(); i < l; ++i) {

> CHANGE  30 : 31  @  30 : 31

~ 		for (int i = 0; i < astring.length; ++i) {

> CHANGE  9 : 10  @  9 : 10

~ 		for (int i = 0; i < agameprofile.length; ++i) {

> DELETE  6  @  6 : 23

> CHANGE  1 : 2  @  1 : 3

~ 		return true;

> CHANGE  3 : 4  @  3 : 4

~ 		return lanCheats

> CHANGE  21 : 22  @  21 : 22

~ 		for (int i = 0, l = this.playerEntityList.size(); i < l; ++i) {

> CHANGE  14 : 15  @  14 : 15

~ 		for (int i = 0, l = this.playerEntityList.size(); i < l; ++i) {

> DELETE  5  @  5 : 32

> CHANGE  40 : 42  @  40 : 41

~ 		for (int i = 0, l = playerEntityList.size(); i < l; ++i) {
~ 			EntityPlayerMP entityplayermp = playerEntityList.get(i);

> CHANGE  26 : 32  @  26 : 31

~ 		if (parEntityPlayerMP2 == null || parEntityPlayerMP2.getName().equals(mcServer.getServerOwner())) {
~ 			if (parEntityPlayerMP2 != null) {
~ 				worldIn.theItemInWorldManager.setGameType(parEntityPlayerMP2.theItemInWorldManager.getGameType());
~ 			} else if (this.gameType != null) {
~ 				worldIn.theItemInWorldManager.setGameType(this.gameType);
~ 			}

> CHANGE  1 : 5  @  1 : 2

~ 			worldIn.theItemInWorldManager.initializeGameType(parWorld.getWorldInfo().getGameType());
~ 		} else {
~ 			worldIn.theItemInWorldManager.setGameType(lanGamemode);
~ 		}

> CHANGE  7 : 8  @  7 : 8

~ 		for (int i = 0, l = this.playerEntityList.size(); i < l; ++i) {

> CHANGE  17 : 19  @  17 : 19

~ 		String name = playerIn.getName();
~ 		StatisticsFile statisticsfile = (StatisticsFile) this.playerStatFiles.get(name);

> CHANGE  1 : 4  @  1 : 11

~ 			VFile2 file1 = WorldsDB
~ 					.newVFile(this.mcServer.worldServerForDimension(0).getSaveHandler().getWorldDirectory(), "stats");
~ 			VFile2 file2 = WorldsDB.newVFile(file1, name + ".json");

> CHANGE  2 : 3  @  2 : 3

~ 			this.playerStatFiles.put(name, statisticsfile);

> CHANGE  8 : 11  @  8 : 9

~ 			WorldServer[] srv = this.mcServer.worldServers;
~ 			for (int i = 0; i < srv.length; ++i) {
~ 				WorldServer worldserver = srv[i];

> DELETE  4  @  4 : 5

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
