
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 8

> DELETE  6  @  12 : 14

> INSERT  7 : 19  @  15

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

> DELETE  33  @  29 : 31

> DELETE  42  @  40 : 41

> DELETE  47  @  46 : 49

> DELETE  94  @  96 : 97

> DELETE  96  @  99 : 100

> DELETE  174  @  178 : 179

> DELETE  206  @  211 : 213

> CHANGE  209 : 210  @  216 : 217

~ 	private final EaglercraftNetworkManager netManager;

> CHANGE  215 : 216  @  222 : 223

~ 	private final Map<EaglercraftUUID, NetworkPlayerInfo> playerInfoMap = Maps.newHashMap();

> CHANGE  218 : 220  @  225 : 226

~ 	private final EaglercraftRandom avRandomizer = new EaglercraftRandom();
~ 	private final ServerSkinCache skinCache;

> CHANGE  221 : 222  @  227 : 228

~ 	public NetHandlerPlayClient(Minecraft mcIn, GuiScreen parGuiScreen, EaglercraftNetworkManager parNetworkManager,

> INSERT  227 : 228  @  233

+ 		this.skinCache = new ServerSkinCache(parNetworkManager, mcIn.getTextureManager());

> INSERT  232 : 233  @  237

+ 		this.skinCache.destroy();

> INSERT  235 : 239  @  239

+ 	public ServerSkinCache getSkinCache() {
+ 		return this.skinCache;
+ 	}
+ 

> DELETE  240  @  240 : 241

> DELETE  259  @  260 : 261

> DELETE  364  @  366 : 367

> DELETE  376  @  379 : 380

> DELETE  397  @  401 : 402

> DELETE  403  @  408 : 409

> DELETE  411  @  417 : 418

> DELETE  419  @  426 : 427

> DELETE  450  @  458 : 459

> DELETE  472  @  481 : 482

> DELETE  480  @  490 : 491

> DELETE  497  @  508 : 509

> DELETE  505  @  517 : 519

> DELETE  512  @  526 : 527

> DELETE  559  @  574 : 576

> DELETE  568  @  585 : 586

> DELETE  590  @  608 : 609

> CHANGE  598 : 601  @  617 : 618

~ 		if (this.gameController.theWorld != null) {
~ 			this.gameController.loadWorld((WorldClient) null);
~ 		}

> CHANGE  602 : 604  @  619 : 627

~ 			this.gameController
~ 					.displayGuiScreen(new GuiDisconnected(this.guiScreenServer, "disconnect.lost", ichatcomponent));

> DELETE  608  @  631 : 632

> DELETE  615  @  639 : 640

> DELETE  638  @  663 : 664

> DELETE  647  @  673 : 674

> DELETE  667  @  694 : 695

> DELETE  671  @  699 : 700

> DELETE  706  @  735 : 736

> DELETE  711  @  741 : 742

> DELETE  716  @  747 : 748

> DELETE  755  @  787 : 788

> DELETE  767  @  800 : 801

> DELETE  773  @  807 : 808

> DELETE  778  @  813 : 814

> DELETE  795  @  831 : 832

> DELETE  804  @  841 : 842

> DELETE  831  @  869 : 870

> DELETE  858  @  897 : 898

> DELETE  874  @  914 : 915

> DELETE  884  @  925 : 926

> DELETE  895  @  937 : 938

> DELETE  917  @  960 : 961

> DELETE  933  @  977 : 978

> DELETE  941  @  986 : 987

> DELETE  949  @  995 : 996

> DELETE  953  @  1000 : 1001

> DELETE  958  @  1006 : 1007

> DELETE  963  @  1012 : 1014

> DELETE  981  @  1032 : 1033

> CHANGE  1002 : 1005  @  1054 : 1073

~ 
~ 			// minecraft demo screen
~ 

> DELETE  1023  @  1091 : 1092

> DELETE  1029  @  1098 : 1099

> DELETE  1040  @  1110 : 1111

> DELETE  1049  @  1120 : 1121

> DELETE  1074  @  1146 : 1147

> DELETE  1084  @  1157 : 1173

> INSERT  1085 : 1087  @  1174

+ 		// used by twitch stream
+ 

> DELETE  1090  @  1177 : 1178

> CHANGE  1095 : 1096  @  1183 : 1184

~ 

> DELETE  1104  @  1192 : 1193

> DELETE  1108  @  1197 : 1198

> DELETE  1144  @  1234 : 1235

> DELETE  1152  @  1243 : 1245

> CHANGE  1154 : 1157  @  1247 : 1248

~ 				EaglercraftUUID uuid = s38packetplayerlistitem$addplayerdata.getProfile().getId();
~ 				this.playerInfoMap.remove(uuid);
~ 				this.skinCache.evictSkin(uuid);

> DELETE  1191  @  1282 : 1283

> DELETE  1201  @  1293 : 1294

> DELETE  1210  @  1303 : 1304

> CHANGE  1218 : 1240  @  1312 : 1335

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

> CHANGE  1241 : 1249  @  1336 : 1346

~ 			NetHandlerPlayClient.this.gameController.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
~ 				public void confirmClicked(boolean flag, int var2) {
~ 					NetHandlerPlayClient.this.gameController = Minecraft.getMinecraft();
~ 					if (flag) {
~ 						if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null) {
~ 							NetHandlerPlayClient.this.gameController.getCurrentServerData()
~ 									.setResourceMode(ServerData.ServerResourceMode.ENABLED);
~ 						}

> CHANGE  1250 : 1260  @  1347 : 1366

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

> INSERT  1261 : 1267  @  1367

+ 								});
+ 					} else {
+ 						if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null) {
+ 							NetHandlerPlayClient.this.gameController.getCurrentServerData()
+ 									.setResourceMode(ServerData.ServerResourceMode.DISABLED);
+ 						}

> CHANGE  1268 : 1270  @  1368 : 1401

~ 						NetHandlerPlayClient.this.netManager.sendPacket(
~ 								new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));

> DELETE  1271  @  1402 : 1404

> INSERT  1272 : 1277  @  1405

+ 					ServerList.func_147414_b(NetHandlerPlayClient.this.gameController.getCurrentServerData());
+ 					NetHandlerPlayClient.this.gameController.displayGuiScreen((GuiScreen) null);
+ 				}
+ 			}, I18n.format("multiplayer.texturePrompt.line1", new Object[0]),
+ 					I18n.format("multiplayer.texturePrompt.line2", new Object[0]), 0));

> DELETE  1281  @  1409 : 1410

> DELETE  1289  @  1418 : 1419

> DELETE  1291  @  1421 : 1422

> DELETE  1302  @  1433 : 1435

> INSERT  1311 : 1318  @  1444

+ 		} else if ("EAG|Skins-1.8".equals(packetIn.getChannelName())) {
+ 			try {
+ 				SkinPackets.readPluginMessage(packetIn.getBufferData(), skinCache);
+ 			} catch (IOException e) {
+ 				logger.error("Couldn't read EAG|Skins-1.8 packet!");
+ 				logger.error(e);
+ 			}

> DELETE  1323  @  1449 : 1450

> DELETE  1342  @  1469 : 1470

> DELETE  1358  @  1486 : 1487

> DELETE  1369  @  1498 : 1499

> DELETE  1408  @  1538 : 1539

> DELETE  1443  @  1574 : 1575

> CHANGE  1472 : 1473  @  1604 : 1605

~ 	public EaglercraftNetworkManager getNetworkManager() {

> CHANGE  1480 : 1481  @  1612 : 1613

~ 	public NetworkPlayerInfo getPlayerInfo(EaglercraftUUID parUUID) {

> EOF
