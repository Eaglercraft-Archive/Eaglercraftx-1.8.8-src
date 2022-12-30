
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 13  @  3 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumEaglerConnectionState;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumServerRateLimit;
~ import net.lax1dude.eaglercraft.v1_8.internal.PlatformNetworking;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.socket.AddressResolver;
~ import net.lax1dude.eaglercraft.v1_8.socket.ConnectionHandshake;
~ import net.lax1dude.eaglercraft.v1_8.socket.EaglercraftNetworkManager;
~ import net.lax1dude.eaglercraft.v1_8.socket.RateLimitTracker;

> CHANGE  14 : 15  @  7 : 11

~ import net.minecraft.client.network.NetHandlerPlayClient;

> DELETE  3  @  6 : 9

> DELETE  1  @  4 : 7

> DELETE  2  @  5 : 6

> CHANGE  1 : 5  @  2 : 3

~ 	private EaglercraftNetworkManager networkManager;
~ 	private String currentAddress;
~ 	private String currentPassword;
~ 	private boolean allowPlaintext;

> INSERT  5 : 6  @  2

+ 	private boolean hasOpened;

> INSERT  2 : 3  @  1

+ 	private int timer = 0;

> INSERT  3 : 16  @  2

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

> CHANGE  15 : 16  @  2 : 3

~ 		String serveraddress = AddressResolver.resolveURI(parServerData);

> CHANGE  3 : 8  @  3 : 4

~ 		if (RateLimitTracker.isLockedOut(serveraddress)) {
~ 			logger.error("Server locked this client out on a previous connection, will not attempt to reconnect");
~ 		} else {
~ 			this.connect(serveraddress, password, allowPlaintext);
~ 		}

> INSERT  8 : 21  @  4

+ 		this(parGuiScreen, mcIn, hostName, port, false);
+ 	}
+ 
+ 	public GuiConnecting(GuiScreen parGuiScreen, Minecraft mcIn, String hostName, int port, boolean allowPlaintext) {
+ 		this(parGuiScreen, mcIn, hostName, port, null, allowPlaintext);
+ 	}
+ 
+ 	public GuiConnecting(GuiScreen parGuiScreen, Minecraft mcIn, String hostName, int port, String password) {
+ 		this(parGuiScreen, mcIn, hostName, port, password, false);
+ 	}
+ 
+ 	public GuiConnecting(GuiScreen parGuiScreen, Minecraft mcIn, String hostName, int port, String password,
+ 			boolean allowPlaintext) {

> CHANGE  16 : 17  @  3 : 4

~ 		this.connect(hostName, password, allowPlaintext);

> CHANGE  3 : 6  @  3 : 8

~ 	public GuiConnecting(GuiConnecting previous, String password) {
~ 		this(previous, password, false);
~ 	}

> CHANGE  4 : 9  @  6 : 10

~ 	public GuiConnecting(GuiConnecting previous, String password, boolean allowPlaintext) {
~ 		this.mc = previous.mc;
~ 		this.previousGuiScreen = previous.previousGuiScreen;
~ 		this.connect(previous.currentAddress, password, allowPlaintext);
~ 	}

> CHANGE  6 : 11  @  5 : 19

~ 	private void connect(String ip, String password, boolean allowPlaintext) {
~ 		this.currentAddress = ip;
~ 		this.currentPassword = password;
~ 		this.allowPlaintext = allowPlaintext;
~ 	}

> CHANGE  6 : 42  @  15 : 22

~ 	public void updateScreen() {
~ 		++timer;
~ 		if (timer > 1) {
~ 			if (this.currentAddress == null) {
~ 				mc.displayGuiScreen(GuiDisconnected.createRateLimitKick(previousGuiScreen));
~ 			} else if (this.networkManager == null) {
~ 				logger.info("Connecting to: {}", currentAddress);
~ 				this.networkManager = new EaglercraftNetworkManager(currentAddress);
~ 				this.networkManager.connect();
~ 			} else {
~ 				if (this.networkManager.isChannelOpen()) {
~ 					if (!hasOpened) {
~ 						hasOpened = true;
~ 						logger.info("Logging in: {}", currentAddress);
~ 						if (ConnectionHandshake.attemptHandshake(this.mc, this, previousGuiScreen, currentPassword,
~ 								allowPlaintext)) {
~ 							logger.info("Handshake Success");
~ 							this.networkManager.setConnectionState(EnumConnectionState.PLAY);
~ 							this.networkManager.setNetHandler(new NetHandlerPlayClient(this.mc, previousGuiScreen,
~ 									this.networkManager, this.mc.getSession().getProfile()));
~ 						} else {
~ 							if (mc.currentScreen == this) {
~ 								checkLowLevelRatelimit();
~ 							}
~ 							if (mc.currentScreen == this) {
~ 								logger.info("Handshake Failure");
~ 								mc.getSession().reset();
~ 								mc.displayGuiScreen(
~ 										new GuiDisconnected(previousGuiScreen, "connect.failed", new ChatComponentText(
~ 												"Handshake Failure\n\nAre you sure this is an eagler 1.8 server?")));
~ 							}
~ 							if (!PlatformNetworking.playConnectionState().isClosed()) {
~ 								PlatformNetworking.playDisconnect();
~ 							}
~ 							return;
~ 						}

> CHANGE  37 : 40  @  8 : 14

~ 					try {
~ 						this.networkManager.processReceivedPackets();
~ 					} catch (IOException ex) {

> CHANGE  4 : 32  @  7 : 11

~ 				} else {
~ 					if (PlatformNetworking.playConnectionState() == EnumEaglerConnectionState.FAILED) {
~ 						if (!hasOpened) {
~ 							mc.getSession().reset();
~ 							checkLowLevelRatelimit();
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
~ 						if (this.networkManager.checkDisconnected()) {
~ 							this.mc.getSession().reset();
~ 							checkLowLevelRatelimit();
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

> DELETE  29  @  5 : 6

> DELETE  1  @  2 : 12

> CHANGE  4 : 5  @  14 : 15

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  5 : 7  @  5 : 7

~ 		this.buttonList.add(
~ 				new GuiButton(0, this.width / 2 - 100, this.height / 2 - 10, I18n.format("gui.cancel", new Object[0])));

> CHANGE  4 : 5  @  4 : 5

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  14 : 15  @  14 : 15

~ 		if (this.networkManager == null || !this.networkManager.isChannelOpen()) {

> INSERT  10 : 24  @  10

+ 
+ 	private void checkLowLevelRatelimit() {
+ 		EnumServerRateLimit rateLimit = PlatformNetworking.getRateLimit();
+ 		if (rateLimit == EnumServerRateLimit.BLOCKED) {
+ 			RateLimitTracker.registerBlock(currentAddress);
+ 			mc.displayGuiScreen(GuiDisconnected.createRateLimitKick(previousGuiScreen));
+ 			logger.info("Handshake Failure: Too Many Requests!");
+ 		} else if (rateLimit == EnumServerRateLimit.LOCKED_OUT) {
+ 			RateLimitTracker.registerLockOut(currentAddress);
+ 			mc.displayGuiScreen(GuiDisconnected.createRateLimitKick(previousGuiScreen));
+ 			logger.info("Handshake Failure: Too Many Requests!");
+ 			logger.info("Server has locked this client out");
+ 		}
+ 	}

> EOF
