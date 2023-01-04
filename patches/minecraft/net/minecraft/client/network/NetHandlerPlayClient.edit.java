
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 8

> DELETE  4  @  4 : 6

> INSERT  1 : 13  @  1

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

> DELETE  14  @  14 : 16

> DELETE  9  @  9 : 10

> DELETE  5  @  5 : 8

> DELETE  47  @  47 : 48

> DELETE  2  @  2 : 3

> DELETE  78  @  78 : 79

> DELETE  32  @  32 : 34

> CHANGE  3 : 4  @  3 : 4

~ 	private final EaglercraftNetworkManager netManager;

> CHANGE  5 : 6  @  5 : 6

~ 	private final Map<EaglercraftUUID, NetworkPlayerInfo> playerInfoMap = Maps.newHashMap();

> CHANGE  2 : 4  @  2 : 3

~ 	private final EaglercraftRandom avRandomizer = new EaglercraftRandom();
~ 	private final ServerSkinCache skinCache;

> CHANGE  1 : 2  @  1 : 2

~ 	public NetHandlerPlayClient(Minecraft mcIn, GuiScreen parGuiScreen, EaglercraftNetworkManager parNetworkManager,

> INSERT  5 : 6  @  5

+ 		this.skinCache = new ServerSkinCache(parNetworkManager, mcIn.getTextureManager());

> INSERT  4 : 5  @  4

+ 		this.skinCache.destroy();

> INSERT  2 : 6  @  2

+ 	public ServerSkinCache getSkinCache() {
+ 		return this.skinCache;
+ 	}
+ 

> DELETE  1  @  1 : 2

> DELETE  19  @  19 : 20

> DELETE  105  @  105 : 106

> DELETE  12  @  12 : 13

> DELETE  21  @  21 : 22

> DELETE  6  @  6 : 7

> DELETE  8  @  8 : 9

> DELETE  8  @  8 : 9

> DELETE  31  @  31 : 32

> DELETE  22  @  22 : 23

> DELETE  8  @  8 : 9

> DELETE  17  @  17 : 18

> DELETE  8  @  8 : 10

> DELETE  7  @  7 : 8

> DELETE  47  @  47 : 49

> DELETE  9  @  9 : 10

> DELETE  22  @  22 : 23

> CHANGE  8 : 11  @  8 : 9

~ 		if (this.gameController.theWorld != null) {
~ 			this.gameController.loadWorld((WorldClient) null);
~ 		}

> CHANGE  1 : 3  @  1 : 9

~ 			this.gameController
~ 					.displayGuiScreen(new GuiDisconnected(this.guiScreenServer, "disconnect.lost", ichatcomponent));

> DELETE  4  @  4 : 5

> DELETE  7  @  7 : 8

> DELETE  23  @  23 : 24

> DELETE  9  @  9 : 10

> DELETE  20  @  20 : 21

> DELETE  4  @  4 : 5

> DELETE  35  @  35 : 36

> DELETE  5  @  5 : 6

> DELETE  5  @  5 : 6

> DELETE  39  @  39 : 40

> DELETE  12  @  12 : 13

> DELETE  6  @  6 : 7

> DELETE  5  @  5 : 6

> DELETE  17  @  17 : 18

> DELETE  9  @  9 : 10

> DELETE  27  @  27 : 28

> DELETE  27  @  27 : 28

> DELETE  16  @  16 : 17

> DELETE  10  @  10 : 11

> DELETE  11  @  11 : 12

> DELETE  22  @  22 : 23

> DELETE  16  @  16 : 17

> DELETE  8  @  8 : 9

> DELETE  8  @  8 : 9

> DELETE  4  @  4 : 5

> DELETE  5  @  5 : 6

> DELETE  5  @  5 : 7

> DELETE  18  @  18 : 19

> CHANGE  21 : 24  @  21 : 40

~ 
~ 			// minecraft demo screen
~ 

> DELETE  18  @  18 : 19

> DELETE  6  @  6 : 7

> DELETE  11  @  11 : 12

> DELETE  9  @  9 : 10

> DELETE  25  @  25 : 26

> DELETE  10  @  10 : 26

> INSERT  1 : 3  @  1

+ 		// used by twitch stream
+ 

> DELETE  3  @  3 : 4

> CHANGE  5 : 6  @  5 : 6

~ 

> DELETE  8  @  8 : 9

> DELETE  4  @  4 : 5

> DELETE  36  @  36 : 37

> DELETE  8  @  8 : 10

> CHANGE  2 : 5  @  2 : 3

~ 				EaglercraftUUID uuid = s38packetplayerlistitem$addplayerdata.getProfile().getId();
~ 				this.playerInfoMap.remove(uuid);
~ 				this.skinCache.evictSkin(uuid);

> DELETE  34  @  34 : 35

> DELETE  10  @  10 : 11

> DELETE  9  @  9 : 10

> CHANGE  8 : 30  @  8 : 31

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

> CHANGE  1 : 9  @  1 : 11

~ 			NetHandlerPlayClient.this.gameController.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
~ 				public void confirmClicked(boolean flag, int var2) {
~ 					NetHandlerPlayClient.this.gameController = Minecraft.getMinecraft();
~ 					if (flag) {
~ 						if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null) {
~ 							NetHandlerPlayClient.this.gameController.getCurrentServerData()
~ 									.setResourceMode(ServerData.ServerResourceMode.ENABLED);
~ 						}

> CHANGE  1 : 11  @  1 : 20

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

> INSERT  1 : 7  @  1

+ 								});
+ 					} else {
+ 						if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null) {
+ 							NetHandlerPlayClient.this.gameController.getCurrentServerData()
+ 									.setResourceMode(ServerData.ServerResourceMode.DISABLED);
+ 						}

> CHANGE  1 : 3  @  1 : 34

~ 						NetHandlerPlayClient.this.netManager.sendPacket(
~ 								new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));

> DELETE  1  @  1 : 3

> INSERT  1 : 6  @  1

+ 					ServerList.func_147414_b(NetHandlerPlayClient.this.gameController.getCurrentServerData());
+ 					NetHandlerPlayClient.this.gameController.displayGuiScreen((GuiScreen) null);
+ 				}
+ 			}, I18n.format("multiplayer.texturePrompt.line1", new Object[0]),
+ 					I18n.format("multiplayer.texturePrompt.line2", new Object[0]), 0));

> DELETE  4  @  4 : 5

> DELETE  8  @  8 : 9

> DELETE  2  @  2 : 3

> DELETE  11  @  11 : 13

> INSERT  9 : 16  @  9

+ 		} else if ("EAG|Skins-1.8".equals(packetIn.getChannelName())) {
+ 			try {
+ 				SkinPackets.readPluginMessage(packetIn.getBufferData(), skinCache);
+ 			} catch (IOException e) {
+ 				logger.error("Couldn't read EAG|Skins-1.8 packet!");
+ 				logger.error(e);
+ 			}

> DELETE  5  @  5 : 6

> DELETE  19  @  19 : 20

> DELETE  16  @  16 : 17

> DELETE  11  @  11 : 12

> DELETE  39  @  39 : 40

> DELETE  35  @  35 : 36

> CHANGE  29 : 30  @  29 : 30

~ 	public EaglercraftNetworkManager getNetworkManager() {

> CHANGE  7 : 8  @  7 : 8

~ 	public NetworkPlayerInfo getPlayerInfo(EaglercraftUUID parUUID) {

> EOF
