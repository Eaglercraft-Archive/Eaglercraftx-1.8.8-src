
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 8

> CHANGE  4 : 32  @  4 : 7

~ 
~ import net.lax1dude.eaglercraft.v1_8.ClientUUIDLoadingCache;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 
~ import com.carrotsearch.hppc.cursors.ObjectIntCursor;
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
~ import net.lax1dude.eaglercraft.v1_8.notifications.ServerNotificationManager;
~ import net.lax1dude.eaglercraft.v1_8.skin_cache.ServerTextureCache;
~ import net.lax1dude.eaglercraft.v1_8.socket.EaglercraftNetworkManager;
~ import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageConstants;
~ import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageProtocol;
~ import net.lax1dude.eaglercraft.v1_8.socket.protocol.client.ClientMessageHandler;
~ import net.lax1dude.eaglercraft.v1_8.socket.protocol.handshake.StandardCaps;
~ import net.lax1dude.eaglercraft.v1_8.socket.protocol.message.InjectedMessageController;
~ import net.lax1dude.eaglercraft.v1_8.socket.protocol.message.LegacyMessageController;
~ import net.lax1dude.eaglercraft.v1_8.socket.protocol.message.MessageController;
~ import net.lax1dude.eaglercraft.v1_8.socket.protocol.pkt.GameMessagePacket;
~ import net.lax1dude.eaglercraft.v1_8.sp.lan.LANClientNetworkManager;
~ import net.lax1dude.eaglercraft.v1_8.sp.socket.ClientIntegratedServerNetworkManager;
~ import net.lax1dude.eaglercraft.v1_8.voice.VoiceClientController;
~ import net.lax1dude.eaglercraft.v1_8.webview.WebViewOverlayController;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> DELETE  14  @  14 : 16

> DELETE  9  @  9 : 10

> DELETE  5  @  5 : 8

> DELETE  47  @  47 : 48

> DELETE  2  @  2 : 3

> INSERT  36 : 37  @  36

+ import net.minecraft.network.play.server.S20PacketEntityProperties.Snapshot;

> INSERT  2 : 3  @  2

+ import net.minecraft.network.play.server.S22PacketMultiBlockChange.BlockUpdateData;

> INSERT  22 : 23  @  22

+ import net.minecraft.network.play.server.S38PacketPlayerListItem.AddPlayerData;

> DELETE  18  @  18 : 19

> DELETE  32  @  32 : 34

> CHANGE  3 : 4  @  3 : 4

~ 	private final EaglercraftNetworkManager netManager;

> CHANGE  5 : 6  @  5 : 6

~ 	private final Map<EaglercraftUUID, NetworkPlayerInfo> playerInfoMap = Maps.newHashMap();

> CHANGE  2 : 13  @  2 : 3

~ 	private boolean isIntegratedServer = false;
~ 	private final EaglercraftRandom avRandomizer = new EaglercraftRandom();
~ 	private final ServerTextureCache textureCache;
~ 	private final ServerNotificationManager notifManager;
~ 	public boolean currentFNAWSkinAllowedState = true;
~ 	public boolean currentFNAWSkinForcedState = true;
~ 	private final MessageController eaglerMessageController;
~ 	public byte[] cachedServerInfoHash = null;
~ 	public byte[] cachedServerInfoData = null;
~ 	public boolean allowedDisplayWebview = false;
~ 	public boolean allowedDisplayWebviewYes = false;

> CHANGE  1 : 3  @  1 : 3

~ 	public NetHandlerPlayClient(Minecraft mcIn, GuiScreen parGuiScreen, EaglercraftNetworkManager parNetworkManager,
~ 			GameProfile parGameProfile, GamePluginMessageProtocol eaglerProtocol) {

> INSERT  3 : 4  @  3

+ 		this.netManager.setNetHandler(this);

> INSERT  1 : 15  @  1

+ 		this.isIntegratedServer = (parNetworkManager instanceof ClientIntegratedServerNetworkManager)
+ 				|| (parNetworkManager instanceof LANClientNetworkManager);
+ 		ClientMessageHandler handler = ClientMessageHandler.createClientHandler(eaglerProtocol.ver, this);
+ 		if (eaglerProtocol.ver >= 5) {
+ 			this.eaglerMessageController = new InjectedMessageController(eaglerProtocol, handler,
+ 					GamePluginMessageConstants.CLIENT_TO_SERVER, parNetworkManager::injectRawFrame);
+ 			parNetworkManager.setInjectedMessageController((InjectedMessageController) eaglerMessageController);
+ 		} else {
+ 			this.eaglerMessageController = new LegacyMessageController(eaglerProtocol, handler,
+ 					GamePluginMessageConstants.CLIENT_TO_SERVER,
+ 					(ch, msg) -> addToSendQueue(new C17PacketCustomPayload(ch, msg)));
+ 		}
+ 		this.textureCache = ServerTextureCache.create(this, mcIn.getTextureManager());
+ 		this.notifManager = new ServerNotificationManager(mcIn.getTextureManager());

> INSERT  4 : 6  @  4

+ 		this.textureCache.destroy();
+ 		this.notifManager.destroy();

> INSERT  2 : 38  @  2

+ 	public ServerTextureCache getTextureCache() {
+ 		return this.textureCache;
+ 	}
+ 
+ 	public ServerNotificationManager getNotifManager() {
+ 		return this.notifManager;
+ 	}
+ 
+ 	public MessageController getEaglerMessageController() {
+ 		return eaglerMessageController;
+ 	}
+ 
+ 	public GamePluginMessageProtocol getEaglerMessageProtocol() {
+ 		return eaglerMessageController != null ? eaglerMessageController.getProtocol() : null;
+ 	}
+ 
+ 	public void sendEaglerMessage(GameMessagePacket packet) {
+ 		eaglerMessageController.sendPacket(packet);
+ 	}
+ 
+ 	public boolean webViewSendHandler(GameMessagePacket pkt) {
+ 		if (eaglerMessageController == null) {
+ 			return false;
+ 		}
+ 		if (this.gameController.thePlayer == null || this.gameController.thePlayer.sendQueue != this) {
+ 			logger.error("WebView sent message on a dead handler!");
+ 			return false;
+ 		}
+ 		if (eaglerMessageController.getProtocol().ver >= 4) {
+ 			sendEaglerMessage(pkt);
+ 			return true;
+ 		} else {
+ 			return false;
+ 		}
+ 	}
+ 

> DELETE  1  @  1 : 2

> CHANGE  1 : 3  @  1 : 5

~ 		this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false,
~ 				packetIn.isHardcoreMode(), packetIn.getWorldType()), packetIn.getDimension(), packetIn.getDifficulty());

> INSERT  11 : 24  @  11

+ 		if (VoiceClientController.isClientSupported()) {
+ 			if (netManager.getServerCapabilities().hasCapability(StandardCaps.VOICE, 0)) {
+ 				VoiceClientController.initializeVoiceClient(this::sendEaglerMessage,
+ 						eaglerMessageController.getProtocol().ver);
+ 			} else {
+ 				VoiceClientController.initializeVoiceClient(null, -1);
+ 			}
+ 		}
+ 		if (netManager.getServerCapabilities().hasCapability(StandardCaps.WEBVIEW, 0)) {
+ 			WebViewOverlayController.setPacketSendCallback(this::webViewSendHandler);
+ 		} else {
+ 			WebViewOverlayController.setPacketSendCallback(null);
+ 		}

> DELETE  3  @  3 : 4

> CHANGE  3 : 7  @  3 : 5

~ 		Entity object = null;
~ 		boolean b = false;
~ 		switch (packetIn.getType()) {
~ 		case 10:

> CHANGE  2 : 5  @  2 : 3

~ 			break;
~ 		case 90:
~ 			b = true;

> CHANGE  4 : 6  @  4 : 7

~ 			break;
~ 		case 60:

> CHANGE  1 : 3  @  1 : 2

~ 			break;
~ 		case 61:

> CHANGE  1 : 4  @  1 : 2

~ 			break;
~ 		case 71:
~ 			b = true;

> CHANGE  3 : 6  @  3 : 5

~ 			break;
~ 		case 77:
~ 			b = true;

> CHANGE  2 : 4  @  2 : 4

~ 			break;
~ 		case 65:

> CHANGE  1 : 3  @  1 : 2

~ 			break;
~ 		case 72:

> CHANGE  1 : 3  @  1 : 2

~ 			break;
~ 		case 76:

> CHANGE  1 : 4  @  1 : 2

~ 			break;
~ 		case 63:
~ 			b = true;

> CHANGE  3 : 6  @  3 : 5

~ 			break;
~ 		case 64:
~ 			b = true;

> CHANGE  3 : 6  @  3 : 5

~ 			break;
~ 		case 66:
~ 			b = true;

> CHANGE  3 : 5  @  3 : 5

~ 			break;
~ 		case 62:

> CHANGE  1 : 4  @  1 : 2

~ 			break;
~ 		case 73:
~ 			b = true;

> CHANGE  1 : 4  @  1 : 3

~ 			break;
~ 		case 75:
~ 			b = true;

> CHANGE  1 : 3  @  1 : 3

~ 			break;
~ 		case 1:

> CHANGE  1 : 3  @  1 : 2

~ 			break;
~ 		case 50:

> CHANGE  1 : 3  @  1 : 2

~ 			break;
~ 		case 78:

> CHANGE  1 : 3  @  1 : 2

~ 			break;
~ 		case 51:

> CHANGE  1 : 3  @  1 : 2

~ 			break;
~ 		case 2:

> CHANGE  1 : 4  @  1 : 2

~ 			break;
~ 		case 70:
~ 			b = true;

> INSERT  2 : 7  @  2

+ 			break;
+ 		}
+ 
+ 		if (b) {
+ 			// fix for compiler bug

> CHANGE  4 : 10  @  4 : 10

~ 			object.serverPosX = packetIn.getX();
~ 			object.serverPosY = packetIn.getY();
~ 			object.serverPosZ = packetIn.getZ();
~ 			object.rotationPitch = (float) (packetIn.getPitch() * 360) / 256.0F;
~ 			object.rotationYaw = (float) (packetIn.getYaw() * 360) / 256.0F;
~ 			Entity[] aentity = object.getParts();

> CHANGE  1 : 2  @  1 : 2

~ 				int i = packetIn.getEntityID() - object.getEntityId();

> CHANGE  6 : 8  @  6 : 8

~ 			object.setEntityId(packetIn.getEntityID());
~ 			this.clientWorldController.addEntityToWorld(packetIn.getEntityID(), object);

> CHANGE  8 : 10  @  8 : 10

~ 				object.setVelocity((double) packetIn.getSpeedX() / 8000.0D, (double) packetIn.getSpeedY() / 8000.0D,
~ 						(double) packetIn.getSpeedZ() / 8000.0D);

> DELETE  6  @  6 : 7

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

> CHANGE  47 : 50  @  47 : 51

~ 		BlockUpdateData[] dat = packetIn.getChangedBlocks();
~ 		for (int i = 0; i < dat.length; ++i) {
~ 			BlockUpdateData s22packetmultiblockchange$blockupdatedata = dat[i];

> DELETE  7  @  7 : 8

> DELETE  22  @  22 : 23

> CHANGE  8 : 14  @  8 : 9

~ 		VoiceClientController.handleServerDisconnect();
~ 		Minecraft.getMinecraft().getRenderManager()
~ 				.setEnableFNAWSkins(this.gameController.gameSettings.enableFNAWSkins);
~ 		if (this.gameController.theWorld != null) {
~ 			this.gameController.loadWorld((WorldClient) null);
~ 		}

> CHANGE  1 : 3  @  1 : 9

~ 			this.gameController.shutdownIntegratedServer(
~ 					new GuiDisconnected(this.guiScreenServer, "disconnect.lost", ichatcomponent));

> CHANGE  1 : 2  @  1 : 2

~ 			this.gameController.shutdownIntegratedServer(

> DELETE  2  @  2 : 3

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

> CHANGE  5 : 6  @  5 : 6

~ 					packetIn.getDimensionID(), packetIn.getDifficulty());

> DELETE  11  @  11 : 12

> DELETE  9  @  9 : 10

> DELETE  27  @  27 : 28

> DELETE  27  @  27 : 28

> DELETE  16  @  16 : 17

> DELETE  10  @  10 : 11

> DELETE  11  @  11 : 12

> INSERT  8 : 9  @  8

+ 					tileentitysign.clearProfanityFilterCache();

> DELETE  14  @  14 : 15

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

> CHANGE  2 : 5  @  2 : 5

~ 		for (ObjectIntCursor<StatBase> entry : packetIn.func_148974_c()) {
~ 			StatBase statbase = entry.key;
~ 			int i = entry.value;

> DELETE  4  @  4 : 5

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

> CHANGE  8 : 11  @  8 : 11

~ 		List<AddPlayerData> lst = packetIn.func_179767_a();
~ 		for (int i = 0, l = lst.size(); i < l; ++i) {
~ 			S38PacketPlayerListItem.AddPlayerData s38packetplayerlistitem$addplayerdata = lst.get(i);

> CHANGE  1 : 5  @  1 : 2

~ 				EaglercraftUUID uuid = s38packetplayerlistitem$addplayerdata.getProfile().getId();
~ 				this.playerInfoMap.remove(uuid);
~ 				this.textureCache.evictPlayer(uuid);
~ 				ClientUUIDLoadingCache.evict(uuid);

> DELETE  34  @  34 : 35

> DELETE  10  @  10 : 11

> DELETE  9  @  9 : 10

> CHANGE  7 : 30  @  7 : 31

~ 		if (!EaglerFolderResourcePack.isSupported() || s.startsWith("level://")) {
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

> INSERT  9 : 18  @  9

+ 		} else if (eaglerMessageController instanceof LegacyMessageController) {
+ 			try {
+ 				((LegacyMessageController) eaglerMessageController).handlePacket(packetIn.getChannelName(),
+ 						packetIn.getBufferData());
+ 			} catch (IOException e) {
+ 				logger.error("Couldn't read \"{}\" packet as an eaglercraft plugin message!",
+ 						packetIn.getChannelName());
+ 				logger.error(e);
+ 			}

> DELETE  1  @  1 : 2

> DELETE  3  @  3 : 4

> DELETE  19  @  19 : 20

> DELETE  16  @  16 : 17

> DELETE  11  @  11 : 12

> DELETE  39  @  39 : 40

> DELETE  35  @  35 : 36

> CHANGE  8 : 11  @  8 : 9

~ 				List<Snapshot> lst = packetIn.func_149441_d();
~ 				for (int i = 0, l = lst.size(); i < l; ++i) {
~ 					S20PacketEntityProperties.Snapshot s20packetentityproperties$snapshot = lst.get(i);

> CHANGE  20 : 21  @  20 : 21

~ 	public EaglercraftNetworkManager getNetworkManager() {

> CHANGE  7 : 8  @  7 : 8

~ 	public NetworkPlayerInfo getPlayerInfo(EaglercraftUUID parUUID) {

> INSERT  16 : 20  @  16

+ 
+ 	public boolean isClientInEaglerSingleplayerOrLAN() {
+ 		return isIntegratedServer;
+ 	}

> EOF
