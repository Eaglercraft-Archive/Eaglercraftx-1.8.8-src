
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

> CHANGE  17 : 18  @  10 : 14

~ import net.minecraft.client.network.NetHandlerPlayClient;

> DELETE  20  @  16 : 19

> DELETE  21  @  20 : 23

> DELETE  23  @  25 : 26

> CHANGE  24 : 28  @  27 : 28

~ 	private EaglercraftNetworkManager networkManager;
~ 	private String currentAddress;
~ 	private String currentPassword;
~ 	private boolean allowPlaintext;

> INSERT  29 : 30  @  29

+ 	private boolean hasOpened;

> INSERT  31 : 32  @  30

+ 	private int timer = 0;

> INSERT  34 : 47  @  32

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

> CHANGE  49 : 50  @  34 : 35

~ 		String serveraddress = AddressResolver.resolveURI(parServerData);

> CHANGE  52 : 57  @  37 : 38

~ 		if (RateLimitTracker.isLockedOut(serveraddress)) {
~ 			logger.error("Server locked this client out on a previous connection, will not attempt to reconnect");
~ 		} else {
~ 			this.connect(serveraddress, password, allowPlaintext);
~ 		}

> INSERT  60 : 73  @  41

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

> CHANGE  76 : 77  @  44 : 45

~ 		this.connect(hostName, password, allowPlaintext);

> CHANGE  79 : 82  @  47 : 52

~ 	public GuiConnecting(GuiConnecting previous, String password) {
~ 		this(previous, password, false);
~ 	}

> CHANGE  83 : 88  @  53 : 57

~ 	public GuiConnecting(GuiConnecting previous, String password, boolean allowPlaintext) {
~ 		this.mc = previous.mc;
~ 		this.previousGuiScreen = previous.previousGuiScreen;
~ 		this.connect(previous.currentAddress, password, allowPlaintext);
~ 	}

> CHANGE  89 : 94  @  58 : 72

~ 	private void connect(String ip, String password, boolean allowPlaintext) {
~ 		this.currentAddress = ip;
~ 		this.currentPassword = password;
~ 		this.allowPlaintext = allowPlaintext;
~ 	}

> CHANGE  95 : 131  @  73 : 80

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

> CHANGE  132 : 135  @  81 : 87

~ 					try {
~ 						this.networkManager.processReceivedPackets();
~ 					} catch (IOException ex) {

> CHANGE  136 : 164  @  88 : 92

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

> DELETE  165  @  93 : 94

> DELETE  166  @  95 : 105

> CHANGE  170 : 171  @  109 : 110

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  175 : 177  @  114 : 116

~ 		this.buttonList.add(
~ 				new GuiButton(0, this.width / 2 - 100, this.height / 2 - 10, I18n.format("gui.cancel", new Object[0])));

> CHANGE  179 : 180  @  118 : 119

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  193 : 194  @  132 : 133

~ 		if (this.networkManager == null || !this.networkManager.isChannelOpen()) {

> INSERT  203 : 217  @  142

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
