
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 21  @  3 : 6

~ import java.util.List;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.cookie.ServerCookieDataStore;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumEaglerConnectionState;
~ import net.lax1dude.eaglercraft.v1_8.internal.IWebSocketClient;
~ import net.lax1dude.eaglercraft.v1_8.internal.IWebSocketFrame;
~ import net.lax1dude.eaglercraft.v1_8.internal.PlatformNetworking;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.socket.AddressResolver;
~ import net.lax1dude.eaglercraft.v1_8.socket.ConnectionHandshake;
~ import net.lax1dude.eaglercraft.v1_8.socket.EaglercraftNetworkManager;
~ import net.lax1dude.eaglercraft.v1_8.socket.RateLimitTracker;
~ import net.lax1dude.eaglercraft.v1_8.socket.WebSocketNetworkManager;
~ import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageConstants;
~ import net.lax1dude.eaglercraft.v1_8.socket.protocol.GamePluginMessageProtocol;
~ import net.lax1dude.eaglercraft.v1_8.socket.protocol.client.GameProtocolMessageController;

> CHANGE  4 : 5  @  4 : 8

~ import net.minecraft.client.network.NetHandlerPlayClient;

> CHANGE  2 : 3  @  2 : 5

~ import net.minecraft.network.play.client.C17PacketCustomPayload;

> DELETE  1  @  1 : 4

> DELETE  2  @  2 : 3

> CHANGE  1 : 7  @  1 : 2

~ 	private IWebSocketClient webSocket;
~ 	private EaglercraftNetworkManager networkManager;
~ 	private String currentAddress;
~ 	private String currentPassword;
~ 	private boolean allowPlaintext;
~ 	private boolean allowCookies;

> INSERT  1 : 2  @  1

+ 	private boolean hasOpened;

> INSERT  1 : 2  @  1

+ 	private int timer = 0;

> INSERT  2 : 15  @  2

+ 		this(parGuiScreen, mcIn, parServerData, false);
+ 	}
+ 
+ 	public GuiConnecting(GuiScreen parGuiScreen, Minecraft mcIn, ServerData parServerData, boolean allowPlaintext) {
+ 		this(parGuiScreen, mcIn, parServerData, null, allowPlaintext);
+ 	}
+ 
+ 	public GuiConnecting(GuiScreen parGuiScreen, Minecraft mcIn, ServerData parServerData, String password) {
+ 		this(parGuiScreen, mcIn, parServerData, password, false);
+ 	}
+ 
+ 	public GuiConnecting(GuiScreen parGuiScreen, Minecraft mcIn, ServerData parServerData, String password,
+ 			boolean allowPlaintext) {

> CHANGE  2 : 3  @  2 : 3

~ 		String serveraddress = AddressResolver.resolveURI(parServerData);

> CHANGE  2 : 8  @  2 : 3

~ 		if (RateLimitTracker.isLockedOut(serveraddress)) {
~ 			logger.error("Server locked this client out on a previous connection, will not attempt to reconnect");
~ 		} else {
~ 			this.connect(serveraddress, password, allowPlaintext,
~ 					parServerData.enableCookies && EagRuntime.getConfiguration().isEnableServerCookies());
~ 		}

> CHANGE  3 : 4  @  3 : 7

~ 		this(parGuiScreen, mcIn, hostName, port, false, EagRuntime.getConfiguration().isEnableServerCookies());

> CHANGE  2 : 5  @  2 : 7

~ 	public GuiConnecting(GuiScreen parGuiScreen, Minecraft mcIn, String hostName, int port, boolean allowCookies) {
~ 		this(parGuiScreen, mcIn, hostName, port, false, allowCookies);
~ 	}

> CHANGE  1 : 5  @  1 : 5

~ 	public GuiConnecting(GuiScreen parGuiScreen, Minecraft mcIn, String hostName, int port, boolean allowPlaintext,
~ 			boolean allowCookies) {
~ 		this(parGuiScreen, mcIn, hostName, port, null, allowPlaintext, allowCookies);
~ 	}

> CHANGE  1 : 5  @  1 : 15

~ 	public GuiConnecting(GuiScreen parGuiScreen, Minecraft mcIn, String hostName, int port, String password,
~ 			boolean allowCookies) {
~ 		this(parGuiScreen, mcIn, hostName, port, password, false, allowCookies);
~ 	}

> CHANGE  1 : 9  @  1 : 9

~ 	public GuiConnecting(GuiScreen parGuiScreen, Minecraft mcIn, String hostName, int port, String password,
~ 			boolean allowPlaintext, boolean allowCookies) {
~ 		this.mc = mcIn;
~ 		this.previousGuiScreen = parGuiScreen;
~ 		mcIn.loadWorld((WorldClient) null);
~ 		this.connect(hostName, password, allowPlaintext,
~ 				allowCookies && EagRuntime.getConfiguration().isEnableServerCookies());
~ 	}

> CHANGE  1 : 4  @  1 : 7

~ 	public GuiConnecting(GuiConnecting previous, String password) {
~ 		this(previous, password, false);
~ 	}

> CHANGE  1 : 6  @  1 : 5

~ 	public GuiConnecting(GuiConnecting previous, String password, boolean allowPlaintext) {
~ 		this.mc = previous.mc;
~ 		this.previousGuiScreen = previous.previousGuiScreen;
~ 		this.connect(previous.currentAddress, password, allowPlaintext, previous.allowCookies);
~ 	}

> CHANGE  1 : 6  @  1 : 3

~ 	private void connect(String ip, String password, boolean allowPlaintext, boolean allowCookies) {
~ 		this.currentAddress = ip;
~ 		this.currentPassword = password;
~ 		this.allowPlaintext = allowPlaintext;
~ 		this.allowCookies = allowCookies;

> CHANGE  3 : 14  @  3 : 6

~ 		++timer;
~ 		if (timer > 1) {
~ 			if (this.currentAddress == null) {
~ 				mc.displayGuiScreen(GuiDisconnected.createRateLimitKick(previousGuiScreen));
~ 			} else if (webSocket == null) {
~ 				logger.info("Connecting to: {}", currentAddress);
~ 				webSocket = PlatformNetworking.openWebSocket(currentAddress);
~ 				if (webSocket == null) {
~ 					mc.displayGuiScreen(new GuiDisconnected(previousGuiScreen, "connect.failed",
~ 							new ChatComponentText("Could not open WebSocket to \"" + currentAddress + "\"!")));
~ 				}

> CHANGE  1 : 81  @  1 : 2

~ 				if (webSocket.getState() == EnumEaglerConnectionState.CONNECTED) {
~ 					if (!hasOpened) {
~ 						hasOpened = true;
~ 						logger.info("Logging in: {}", currentAddress);
~ 						byte[] cookieData = null;
~ 						if (allowCookies) {
~ 							ServerCookieDataStore.ServerCookie cookie = ServerCookieDataStore
~ 									.loadCookie(currentAddress);
~ 							if (cookie != null) {
~ 								cookieData = cookie.cookie;
~ 							}
~ 						}
~ 						if (ConnectionHandshake.attemptHandshake(this.mc, webSocket, this, previousGuiScreen,
~ 								currentPassword, allowPlaintext, allowCookies, cookieData)) {
~ 							logger.info("Handshake Success");
~ 							webSocket.setEnableStringFrames(false);
~ 							webSocket.clearStringFrames();
~ 							this.networkManager = new WebSocketNetworkManager(webSocket);
~ 							this.networkManager.setPluginInfo(ConnectionHandshake.pluginBrand,
~ 									ConnectionHandshake.pluginVersion);
~ 							mc.bungeeOutdatedMsgTimer = 80;
~ 							mc.clearTitles();
~ 							this.networkManager.setConnectionState(EnumConnectionState.PLAY);
~ 							NetHandlerPlayClient netHandler = new NetHandlerPlayClient(this.mc, previousGuiScreen,
~ 									this.networkManager, this.mc.getSession().getProfile());
~ 							this.networkManager.setNetHandler(netHandler);
~ 							netHandler.setEaglerMessageController(new GameProtocolMessageController(
~ 									GamePluginMessageProtocol.getByVersion(ConnectionHandshake.protocolVersion),
~ 									GamePluginMessageConstants.CLIENT_TO_SERVER,
~ 									GameProtocolMessageController
~ 											.createClientHandler(ConnectionHandshake.protocolVersion, netHandler),
~ 									(ch, msg) -> netHandler.addToSendQueue(new C17PacketCustomPayload(ch, msg))));
~ 						} else {
~ 							if (mc.currentScreen == this) {
~ 								checkRatelimit();
~ 								logger.info("Handshake Failure");
~ 								mc.getSession().reset();
~ 								mc.displayGuiScreen(
~ 										new GuiDisconnected(previousGuiScreen, "connect.failed", new ChatComponentText(
~ 												"Handshake Failure\n\nAre you sure this is an eagler 1.8 server?")));
~ 							}
~ 							webSocket.close();
~ 							return;
~ 						}
~ 					}
~ 					if (this.networkManager != null) {
~ 						try {
~ 							this.networkManager.processReceivedPackets();
~ 						} catch (IOException ex) {
~ 						}
~ 					}
~ 				} else {
~ 					if (webSocket.getState() == EnumEaglerConnectionState.FAILED) {
~ 						if (!hasOpened) {
~ 							mc.getSession().reset();
~ 							checkRatelimit();
~ 							if (mc.currentScreen == this) {
~ 								if (RateLimitTracker.isProbablyLockedOut(currentAddress)) {
~ 									mc.displayGuiScreen(GuiDisconnected.createRateLimitKick(previousGuiScreen));
~ 								} else {
~ 									mc.displayGuiScreen(new GuiDisconnected(previousGuiScreen, "connect.failed",
~ 											new ChatComponentText("Connection Refused")));
~ 								}
~ 							}
~ 						}
~ 					} else {
~ 						if (this.networkManager != null && this.networkManager.checkDisconnected()) {
~ 							this.mc.getSession().reset();
~ 							checkRatelimit();
~ 							if (mc.currentScreen == this) {
~ 								if (RateLimitTracker.isProbablyLockedOut(currentAddress)) {
~ 									mc.displayGuiScreen(GuiDisconnected.createRateLimitKick(previousGuiScreen));
~ 								} else {
~ 									mc.displayGuiScreen(new GuiDisconnected(previousGuiScreen, "connect.failed",
~ 											new ChatComponentText("Connection Refused")));
~ 								}
~ 							}
~ 						}
~ 					}
~ 				}

> INSERT  1 : 8  @  1

+ 			if (timer > 200) {
+ 				if (webSocket != null) {
+ 					webSocket.close();
+ 				}
+ 				mc.displayGuiScreen(new GuiDisconnected(previousGuiScreen, "connect.failed",
+ 						new ChatComponentText("Handshake timed out")));
+ 			}

> DELETE  1  @  1 : 2

> CHANGE  2 : 3  @  2 : 3

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  4 : 6  @  4 : 6

~ 		this.buttonList.add(
~ 				new GuiButton(0, this.width / 2 - 100, this.height / 2 - 10, I18n.format("gui.cancel", new Object[0])));

> CHANGE  2 : 3  @  2 : 3

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  4 : 6  @  4

+ 			} else if (this.webSocket != null) {
+ 				this.webSocket.close();

> CHANGE  9 : 10  @  9 : 10

~ 		if (this.networkManager == null || !this.networkManager.isChannelOpen()) {

> INSERT  9 : 34  @  9

+ 
+ 	private void checkRatelimit() {
+ 		if (this.webSocket != null) {
+ 			List<IWebSocketFrame> strFrames = webSocket.getNextStringFrames();
+ 			if (strFrames != null) {
+ 				for (int i = 0; i < strFrames.size(); ++i) {
+ 					String str = strFrames.get(i).getString();
+ 					if (str.equalsIgnoreCase("BLOCKED")) {
+ 						RateLimitTracker.registerBlock(currentAddress);
+ 						mc.displayGuiScreen(GuiDisconnected.createRateLimitKick(previousGuiScreen));
+ 						logger.info("Handshake Failure: Too Many Requests!");
+ 					} else if (str.equalsIgnoreCase("LOCKED")) {
+ 						RateLimitTracker.registerLockOut(currentAddress);
+ 						mc.displayGuiScreen(GuiDisconnected.createRateLimitKick(previousGuiScreen));
+ 						logger.info("Handshake Failure: Too Many Requests!");
+ 						logger.info("Server has locked this client out");
+ 					}
+ 				}
+ 			}
+ 		}
+ 	}
+ 
+ 	public boolean canCloseGui() {
+ 		return false;
+ 	}

> EOF
