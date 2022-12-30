
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 8

> DELETE  4  @  10 : 12

> INSERT  1 : 13  @  3

+ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
+ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
+ 
+ import com.google.common.collect.Maps;
+ 
+ import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
+ import net.lax1dude.eaglercraft.v1_8.profile.ServerSkinCache;
+ import net.lax1dude.eaglercraft.v1_8.profile.SkinPackets;
+ import net.lax1dude.eaglercraft.v1_8.socket.EaglercraftNetworkManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> DELETE  26  @  14 : 16

> DELETE  9  @  11 : 12

> DELETE  5  @  6 : 9

> DELETE  47  @  50 : 51

> DELETE  2  @  3 : 4

> DELETE  78  @  79 : 80

> DELETE  32  @  33 : 35

> CHANGE  3 : 4  @  5 : 6

~ 	private final EaglercraftNetworkManager netManager;

> CHANGE  6 : 7  @  6 : 7

~ 	private final Map<EaglercraftUUID, NetworkPlayerInfo> playerInfoMap = Maps.newHashMap();

> CHANGE  3 : 5  @  3 : 4

~ 	private final EaglercraftRandom avRandomizer = new EaglercraftRandom();
~ 	private final ServerSkinCache skinCache;

> CHANGE  3 : 4  @  2 : 3

~ 	public NetHandlerPlayClient(Minecraft mcIn, GuiScreen parGuiScreen, EaglercraftNetworkManager parNetworkManager,

> INSERT  6 : 7  @  6

+ 		this.skinCache = new ServerSkinCache(parNetworkManager, mcIn.getTextureManager());

> INSERT  5 : 6  @  4

+ 		this.skinCache.destroy();

> INSERT  3 : 7  @  2

+ 	public ServerSkinCache getSkinCache() {
+ 		return this.skinCache;
+ 	}
+ 

> DELETE  5  @  1 : 2

> DELETE  19  @  20 : 21

> DELETE  105  @  106 : 107

> DELETE  12  @  13 : 14

> DELETE  21  @  22 : 23

> DELETE  6  @  7 : 8

> DELETE  8  @  9 : 10

> DELETE  8  @  9 : 10

> DELETE  31  @  32 : 33

> DELETE  22  @  23 : 24

> DELETE  8  @  9 : 10

> DELETE  17  @  18 : 19

> DELETE  8  @  9 : 11

> DELETE  7  @  9 : 10

> DELETE  47  @  48 : 50

> DELETE  9  @  11 : 12

> DELETE  22  @  23 : 24

> CHANGE  8 : 11  @  9 : 10

~ 		if (this.gameController.theWorld != null) {
~ 			this.gameController.loadWorld((WorldClient) null);
~ 		}

> CHANGE  4 : 6  @  2 : 10

~ 			this.gameController
~ 					.displayGuiScreen(new GuiDisconnected(this.guiScreenServer, "disconnect.lost", ichatcomponent));

> DELETE  6  @  12 : 13

> DELETE  7  @  8 : 9

> DELETE  23  @  24 : 25

> DELETE  9  @  10 : 11

> DELETE  20  @  21 : 22

> DELETE  4  @  5 : 6

> DELETE  35  @  36 : 37

> DELETE  5  @  6 : 7

> DELETE  5  @  6 : 7

> DELETE  39  @  40 : 41

> DELETE  12  @  13 : 14

> DELETE  6  @  7 : 8

> DELETE  5  @  6 : 7

> DELETE  17  @  18 : 19

> DELETE  9  @  10 : 11

> DELETE  27  @  28 : 29

> DELETE  27  @  28 : 29

> DELETE  16  @  17 : 18

> DELETE  10  @  11 : 12

> DELETE  11  @  12 : 13

> DELETE  22  @  23 : 24

> DELETE  16  @  17 : 18

> DELETE  8  @  9 : 10

> DELETE  8  @  9 : 10

> DELETE  4  @  5 : 6

> DELETE  5  @  6 : 7

> DELETE  5  @  6 : 8

> DELETE  18  @  20 : 21

> CHANGE  21 : 24  @  22 : 41

~ 
~ 			// minecraft demo screen
~ 

> DELETE  21  @  37 : 38

> DELETE  6  @  7 : 8

> DELETE  11  @  12 : 13

> DELETE  9  @  10 : 11

> DELETE  25  @  26 : 27

> DELETE  10  @  11 : 27

> INSERT  1 : 3  @  17

+ 		// used by twitch stream
+ 

> DELETE  5  @  3 : 4

> CHANGE  5 : 6  @  6 : 7

~ 

> DELETE  9  @  9 : 10

> DELETE  4  @  5 : 6

> DELETE  36  @  37 : 38

> DELETE  8  @  9 : 11

> CHANGE  2 : 5  @  4 : 5

~ 				EaglercraftUUID uuid = s38packetplayerlistitem$addplayerdata.getProfile().getId();
~ 				this.playerInfoMap.remove(uuid);
~ 				this.skinCache.evictSkin(uuid);

> DELETE  37  @  35 : 36

> DELETE  10  @  11 : 12

> DELETE  9  @  10 : 11

> CHANGE  8 : 30  @  9 : 32

~ 			this.netManager
~ 					.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));
~ 			return;
~ 		}
~ 		if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData()
~ 				.getResourceMode() == ServerData.ServerResourceMode.ENABLED) {
~ 			NetHandlerPlayClient.this.netManager
~ 					.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
~ 			NetHandlerPlayClient.this.gameController.getResourcePackRepository().downloadResourcePack(s, s1,
~ 					success -> {
~ 						if (success) {
~ 							NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s1,
~ 									C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
~ 						} else {
~ 							NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s1,
~ 									C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
~ 						}
~ 					});
~ 		} else if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData()
~ 				.getResourceMode() != ServerData.ServerResourceMode.PROMPT) {
~ 			this.netManager
~ 					.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));

> CHANGE  23 : 31  @  24 : 34

~ 			NetHandlerPlayClient.this.gameController.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
~ 				public void confirmClicked(boolean flag, int var2) {
~ 					NetHandlerPlayClient.this.gameController = Minecraft.getMinecraft();
~ 					if (flag) {
~ 						if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null) {
~ 							NetHandlerPlayClient.this.gameController.getCurrentServerData()
~ 									.setResourceMode(ServerData.ServerResourceMode.ENABLED);
~ 						}

> CHANGE  9 : 19  @  11 : 30

~ 						NetHandlerPlayClient.this.netManager.sendPacket(
~ 								new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
~ 						NetHandlerPlayClient.this.gameController.getResourcePackRepository().downloadResourcePack(s, s1,
~ 								success -> {
~ 									if (success) {
~ 										NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(
~ 												s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
~ 									} else {
~ 										NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(
~ 												s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));

> INSERT  11 : 17  @  20

+ 								});
+ 					} else {
+ 						if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null) {
+ 							NetHandlerPlayClient.this.gameController.getCurrentServerData()
+ 									.setResourceMode(ServerData.ServerResourceMode.DISABLED);
+ 						}

> CHANGE  7 : 9  @  1 : 34

~ 						NetHandlerPlayClient.this.netManager.sendPacket(
~ 								new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));

> DELETE  3  @  34 : 36

> INSERT  1 : 6  @  3

+ 					ServerList.func_147414_b(NetHandlerPlayClient.this.gameController.getCurrentServerData());
+ 					NetHandlerPlayClient.this.gameController.displayGuiScreen((GuiScreen) null);
+ 				}
+ 			}, I18n.format("multiplayer.texturePrompt.line1", new Object[0]),
+ 					I18n.format("multiplayer.texturePrompt.line2", new Object[0]), 0));

> DELETE  9  @  4 : 5

> DELETE  8  @  9 : 10

> DELETE  2  @  3 : 4

> DELETE  11  @  12 : 14

> INSERT  9 : 16  @  11

+ 		} else if ("EAG|Skins-1.8".equals(packetIn.getChannelName())) {
+ 			try {
+ 				SkinPackets.readPluginMessage(packetIn.getBufferData(), skinCache);
+ 			} catch (IOException e) {
+ 				logger.error("Couldn't read EAG|Skins-1.8 packet!");
+ 				logger.error(e);
+ 			}

> DELETE  12  @  5 : 6

> DELETE  19  @  20 : 21

> DELETE  16  @  17 : 18

> DELETE  11  @  12 : 13

> DELETE  39  @  40 : 41

> DELETE  35  @  36 : 37

> CHANGE  29 : 30  @  30 : 31

~ 	public EaglercraftNetworkManager getNetworkManager() {

> CHANGE  8 : 9  @  8 : 9

~ 	public NetworkPlayerInfo getPlayerInfo(EaglercraftUUID parUUID) {

> EOF
