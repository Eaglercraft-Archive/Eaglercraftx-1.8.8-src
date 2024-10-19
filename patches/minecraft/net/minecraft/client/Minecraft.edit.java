
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 17

~ import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL._wglBindFramebuffer;
~ 

> DELETE  2  @  2 : 8

> CHANGE  2 : 3  @  2 : 6

~ import java.util.LinkedList;

> DELETE  1  @  1 : 4

> CHANGE  1 : 73  @  1 : 4

~ 
~ import net.lax1dude.eaglercraft.v1_8.ClientUUIDLoadingCache;
~ import net.lax1dude.eaglercraft.v1_8.Display;
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.EagUtils;
~ import net.lax1dude.eaglercraft.v1_8.EaglerXBungeeVersion;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ import net.lax1dude.eaglercraft.v1_8.PauseMenuCustomizeState;
~ import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
~ import net.lax1dude.eaglercraft.v1_8.Touch;
~ import net.lax1dude.eaglercraft.v1_8.cookie.ServerCookieDataStore;
~ 
~ import org.apache.commons.lang3.Validate;
~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.futures.Executors;
~ import net.lax1dude.eaglercraft.v1_8.futures.FutureTask;
~ import net.lax1dude.eaglercraft.v1_8.futures.ListenableFuture;
~ import net.lax1dude.eaglercraft.v1_8.futures.ListenableFutureTask;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
~ import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
~ import net.lax1dude.eaglercraft.v1_8.internal.PlatformWebRTC;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFontRenderer;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.GuiScreenGenericErrorMessage;
~ import net.lax1dude.eaglercraft.v1_8.notifications.ServerNotificationRenderer;
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglerMeshLoader;
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.BlockVertexIDs;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DebugFramebufferView;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.ShaderPackInfoReloadListener;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.program.ShaderSource;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture.EmissiveItems;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture.MetalsLUT;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture.PBRTextureMapUtils;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.texture.TemperaturesLUT;
~ import net.lax1dude.eaglercraft.v1_8.profanity_filter.GuiScreenContentWarning;
~ import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;
~ import net.lax1dude.eaglercraft.v1_8.profile.GuiScreenEditProfile;
~ import net.lax1dude.eaglercraft.v1_8.profile.SkinPreviewRenderer;
~ import net.lax1dude.eaglercraft.v1_8.socket.AddressResolver;
~ import net.lax1dude.eaglercraft.v1_8.socket.EaglercraftNetworkManager;
~ import net.lax1dude.eaglercraft.v1_8.socket.RateLimitTracker;
~ import net.lax1dude.eaglercraft.v1_8.sp.IntegratedServerState;
~ import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
~ import net.lax1dude.eaglercraft.v1_8.sp.SkullCommand;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenDemoIntegratedServerStartup;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenIntegratedServerBusy;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenSingleplayerConnecting;
~ import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerController;
~ import net.lax1dude.eaglercraft.v1_8.touch_gui.TouchControls;
~ import net.lax1dude.eaglercraft.v1_8.touch_gui.TouchOverlayRenderer;
~ import net.lax1dude.eaglercraft.v1_8.update.GuiUpdateDownloadSuccess;
~ import net.lax1dude.eaglercraft.v1_8.update.GuiUpdateInstallOptions;
~ import net.lax1dude.eaglercraft.v1_8.update.RelayUpdateChecker;
~ import net.lax1dude.eaglercraft.v1_8.update.UpdateDataObj;
~ import net.lax1dude.eaglercraft.v1_8.update.UpdateResultObj;
~ import net.lax1dude.eaglercraft.v1_8.update.UpdateService;
~ import net.lax1dude.eaglercraft.v1_8.voice.GuiVoiceOverlay;
~ import net.lax1dude.eaglercraft.v1_8.voice.VoiceClientController;
~ import net.lax1dude.eaglercraft.v1_8.webview.WebViewOverlayController;

> DELETE  2  @  2 : 4

> DELETE  10  @  10 : 11

> DELETE  2  @  2 : 4

> DELETE  3  @  3 : 4

> INSERT  3 : 4  @  3

+ import net.minecraft.client.multiplayer.ServerAddress;

> INSERT  1 : 2  @  1

+ import net.minecraft.client.multiplayer.ServerList;

> DELETE  1  @  1 : 2

> DELETE  4  @  4 : 5

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 3

> DELETE  15  @  15 : 16

> DELETE  2  @  2 : 3

> DELETE  14  @  14 : 18

> INSERT  13 : 14  @  13

+ import net.minecraft.event.ClickEvent;

> DELETE  8  @  8 : 12

> DELETE  1  @  1 : 6

> INSERT  6 : 9  @  6

+ import net.minecraft.util.ChatComponentTranslation;
+ import net.minecraft.util.ChatStyle;
+ import net.minecraft.util.EnumChatFormatting;

> INSERT  7 : 8  @  7

+ import net.minecraft.util.MovingObjectPosition.MovingObjectType;

> INSERT  4 : 5  @  4

+ import net.minecraft.util.StringTranslate;

> DELETE  6  @  6 : 7

> DELETE  1  @  1 : 19

> CHANGE  1 : 2  @  1 : 2

~ public class Minecraft implements IThreadListener {

> CHANGE  2 : 3  @  2 : 9

~ 	public static final boolean isRunningOnMac = false;

> CHANGE  5 : 6  @  5 : 6

~ 	private boolean enableGLErrorChecking = false;

> INSERT  4 : 5  @  4

+ 	public float displayDPI;

> DELETE  2  @  2 : 4

> INSERT  11 : 12  @  11

+ 	private boolean wasPaused;

> DELETE  8  @  8 : 9

> DELETE  6  @  6 : 8

> DELETE  1  @  1 : 3

> CHANGE  8 : 9  @  8 : 9

~ 	long field_181543_z = EagRuntime.nanoTime();

> CHANGE  1 : 2  @  1 : 3

~ 	private EaglercraftNetworkManager myNetworkManager;

> DELETE  1  @  1 : 2

> DELETE  7  @  7 : 9

> CHANGE  4 : 5  @  4 : 7

~ 	private final List<FutureTask<?>> scheduledTasks = new LinkedList<>();

> INSERT  14 : 18  @  14

+ 	public int joinWorldTickCounter = 0;
+ 	private int dontPauseTimer = 0;
+ 	public int bungeeOutdatedMsgTimer = 0;
+ 	private boolean isLANOpen = false;

> INSERT  1 : 14  @  1

+ 	public SkullCommand eagskullCommand;
+ 
+ 	public GuiVoiceOverlay voiceOverlay;
+ 	public ServerNotificationRenderer notifRenderer;
+ 	public TouchOverlayRenderer touchOverlayRenderer;
+ 
+ 	public float startZoomValue = 18.0f;
+ 	public float adjustedZoomValue = 18.0f;
+ 	public boolean isZoomKey = false;
+ 	private String reconnectURI = null;
+ 	public boolean mouseGrabSupported = false;
+ 	public ScaledResolution scaledResolution = null;
+ 

> CHANGE  2 : 3  @  2 : 5

~ 		StringTranslate.initClient();

> CHANGE  1 : 2  @  1 : 9

~ 		this.mcDefaultResourcePack = new DefaultResourcePack();

> CHANGE  1 : 2  @  1 : 4

~ 		logger.info("Setting user: " + this.session.getProfile().getName());

> INSERT  2 : 3  @  2

+ 		this.displayDPI = 1.0f;

> CHANGE  4 : 10  @  4 : 8

~ 		this.enableGLErrorChecking = EagRuntime.getConfiguration().isCheckGLErrors();
~ 		String serverToJoin = EagRuntime.getConfiguration().getServerToJoin();
~ 		if (serverToJoin != null) {
~ 			ServerAddress addr = AddressResolver.resolveAddressFromURI(serverToJoin);
~ 			this.serverName = addr.getIP();
~ 			this.serverPort = addr.getPort();

> DELETE  2  @  2 : 3

> CHANGE  15 : 17  @  15 : 17

~ 		try {
~ 			while (true) {

> CHANGE  5 : 6  @  5 : 12

~ 					this.runGameLoop();

> DELETE  4  @  4 : 21

> CHANGE  1 : 14  @  1 : 3

~ 		} catch (MinecraftError var12) {
~ 			// ??
~ 		} catch (ReportedException reportedexception) {
~ 			this.addGraphicsAndWorldToCrashReport(reportedexception.getCrashReport());
~ 			logger.fatal("Reported exception thrown!", reportedexception);
~ 			this.displayCrashReport(reportedexception.getCrashReport());
~ 		} catch (Throwable throwable1) {
~ 			CrashReport crashreport1 = this
~ 					.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", throwable1));
~ 			logger.fatal("Unreported exception thrown!", throwable1);
~ 			this.displayCrashReport(crashreport1);
~ 		} finally {
~ 			this.shutdownMinecraftApplet();

> CHANGE  4 : 6  @  4 : 6

~ 	private void startGame() throws IOException {
~ 		this.gameSettings = new GameSettings(this);

> DELETE  1  @  1 : 2

> CHANGE  5 : 6  @  5 : 8

~ 		logger.info("EagRuntime Version: " + EagRuntime.getVersion());

> DELETE  1  @  1 : 4

> CHANGE  1 : 3  @  1 : 3

~ 		EaglerFolderResourcePack.deleteOldResourcePacks(EaglerFolderResourcePack.SERVER_RESOURCE_PACKS, 604800000L);
~ 		this.mcResourcePackRepository = new ResourcePackRepository(this.mcDefaultResourcePack, this.metadataSerializer_,

> INSERT  4 : 5  @  4

+ 		this.scaledResolution = new ScaledResolution(this);

> DELETE  4  @  4 : 7

> CHANGE  3 : 5  @  3 : 5

~ 		this.fontRendererObj = EaglerFontRenderer.createSupportedFontRenderer(this.gameSettings,
~ 				new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);

> CHANGE  5 : 6  @  5 : 6

~ 		this.standardGalacticFontRenderer = EaglerFontRenderer.createSupportedFontRenderer(this.gameSettings,

> INSERT  5 : 12  @  5

+ 		this.mcResourceManager.registerReloadListener(new ShaderPackInfoReloadListener());
+ 		this.mcResourceManager.registerReloadListener(PBRTextureMapUtils.blockMaterialConstants);
+ 		this.mcResourceManager.registerReloadListener(new TemperaturesLUT());
+ 		this.mcResourceManager.registerReloadListener(new MetalsLUT());
+ 		this.mcResourceManager.registerReloadListener(new EmissiveItems());
+ 		this.mcResourceManager.registerReloadListener(new BlockVertexIDs());
+ 		this.mcResourceManager.registerReloadListener(new EaglerMeshLoader());

> CHANGE  3 : 4  @  3 : 4

~ 					return HString.format(parString1, new Object[] { GameSettings

> CHANGE  10 : 11  @  10 : 11

~ 		GlStateManager.clearDepth(1.0f);

> INSERT  10 : 11  @  10

+ 		this.textureMapBlocks.setEnablePBREagler(gameSettings.shaders);

> INSERT  20 : 21  @  20

+ 		SkinPreviewRenderer.initialize();

> INSERT  2 : 24  @  2

+ 
+ 		this.mouseGrabSupported = Mouse.isMouseGrabSupported();
+ 		PointerInputAbstraction.init(this);
+ 
+ 		this.eagskullCommand = new SkullCommand(this);
+ 		this.voiceOverlay = new GuiVoiceOverlay(this);
+ 		this.voiceOverlay.setResolution(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
+ 
+ 		this.notifRenderer = new ServerNotificationRenderer();
+ 		this.notifRenderer.init();
+ 		this.notifRenderer.setResolution(this, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(),
+ 				scaledResolution.getScaleFactor());
+ 		this.touchOverlayRenderer = new TouchOverlayRenderer(this);
+ 
+ 		ServerList.initServerList(this);
+ 		EaglerProfile.read();
+ 		ServerCookieDataStore.load();
+ 
+ 		GuiScreen mainMenu = new GuiMainMenu();
+ 		if (isDemo()) {
+ 			mainMenu = new GuiScreenDemoIntegratedServerStartup(mainMenu);
+ 		}

> CHANGE  1 : 2  @  1 : 4

~ 			mainMenu = new GuiConnecting(mainMenu, this, this.serverName, this.serverPort);

> INSERT  2 : 10  @  2

+ 		mainMenu = new GuiScreenEditProfile(mainMenu);
+ 
+ 		if (!EagRuntime.getConfiguration().isForceProfanityFilter() && !gameSettings.hasShownProfanityFilter) {
+ 			mainMenu = new GuiScreenContentWarning(mainMenu);
+ 		}
+ 
+ 		this.displayGuiScreen(mainMenu);
+ 

> DELETE  3  @  3 : 6

> CHANGE  1 : 7  @  1 : 9

~ 		while (Mouse.next())
~ 			;
~ 		while (Keyboard.next())
~ 			;
~ 		while (Touch.next())
~ 			;

> CHANGE  15 : 18  @  15 : 24

~ 	private void createDisplay() {
~ 		Display.create();
~ 		Display.setTitle("Eaglercraft 1.8.8");

> DELETE  2  @  2 : 63

> CHANGE  1 : 2  @  1 : 11

~ 		return true;

> DELETE  2  @  2 : 6

> DELETE  4  @  4 : 21

> CHANGE  6 : 18  @  6 : 19

~ 		String report = crashReportIn.getCompleteReport();
~ 		Bootstrap.printToSYSOUT(report);
~ 		PlatformRuntime.writeCrashReport(report);
~ 		if (PlatformRuntime.getPlatformType() == EnumPlatformType.JAVASCRIPT) {
~ 			System.err.println(
~ 					"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
~ 			System.err.println("NATIVE BROWSER EXCEPTION:");
~ 			if (!PlatformRuntime.printJSExceptionIfBrowser(crashReportIn.getCrashCause())) {
~ 				System.err.println("<undefined>");
~ 			}
~ 			System.err.println(
~ 					"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

> DELETE  1  @  1 : 2

> INSERT  7 : 9  @  7

+ 		GlStateManager.recompileShaders();
+ 

> CHANGE  14 : 16  @  14 : 15

~ 			logger.info("Caught error stitching, removing all assigned resourcepacks");
~ 			logger.info(runtimeexception);

> INSERT  9 : 12  @  9

+ 		ShaderSource.clearCache();
+ 		GuiMainMenu.doResourceReloadHack();
+ 

> CHANGE  7 : 12  @  7 : 19

~ 	private void updateDisplayMode() {
~ 		this.displayWidth = Display.getWidth();
~ 		this.displayHeight = Display.getHeight();
~ 		this.displayDPI = Display.getDPI();
~ 		this.scaledResolution = new ScaledResolution(this);

> CHANGE  2 : 6  @  2 : 51

~ 	private void drawSplashScreen(TextureManager textureManagerInstance) {
~ 		Display.update();
~ 		updateDisplayMode();
~ 		GlStateManager.viewport(0, 0, displayWidth, displayHeight);

> CHANGE  2 : 4  @  2 : 4

~ 		GlStateManager.ortho(0.0D, (double) scaledResolution.getScaledWidth(),
~ 				(double) scaledResolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);

> CHANGE  12 : 13  @  12 : 13

~ 					new DynamicTexture(ImageData.loadImageFile(inputstream)));

> CHANGE  20 : 22  @  20 : 22

~ 		this.func_181536_a((scaledResolution.getScaledWidth() - short1) / 2,
~ 				(scaledResolution.getScaledHeight() - short2) / 2, 0, 0, short1, short2, 255, 255, 255, 255);

> DELETE  2  @  2 : 4

> DELETE  26  @  26 : 30

> INSERT  17 : 18  @  17

+ 		this.scaledResolution = new ScaledResolution(this);

> CHANGE  2 : 4  @  2 : 6

~ 			((GuiScreen) guiScreenIn).setWorldAndResolution(this, scaledResolution.getScaledWidth(),
~ 					scaledResolution.getScaledHeight());

> INSERT  5 : 9  @  5

+ 		EagRuntime.getConfiguration().getHooks().callScreenChangedHook(
+ 				currentScreen != null ? currentScreen.getClass().getName() : null, scaledResolution.getScaledWidth(),
+ 				scaledResolution.getScaledHeight(), displayWidth, displayHeight, scaledResolution.getScaleFactor());
+ 	}

> INSERT  1 : 9  @  1

+ 	public void shutdownIntegratedServer(GuiScreen cont) {
+ 		if (SingleplayerServerController.shutdownEaglercraftServer()
+ 				|| SingleplayerServerController.getStatusState() == IntegratedServerState.WORLD_UNLOADING) {
+ 			displayGuiScreen(new GuiScreenIntegratedServerBusy(cont, "singleplayer.busy.stoppingIntegratedServer",
+ 					"singleplayer.failed.stoppingIntegratedServer", SingleplayerServerController::isReady));
+ 		} else {
+ 			displayGuiScreen(cont);
+ 		}

> CHANGE  2 : 3  @  2 : 3

~ 	public void checkGLError(String message) {

> CHANGE  1 : 2  @  1 : 2

~ 			int i = EaglercraftGPU.glGetError();

> CHANGE  1 : 2  @  1 : 2

~ 				String s = EaglercraftGPU.gluErrorString(i);

> DELETE  10  @  10 : 11

> INSERT  9 : 21  @  9

+ 			if (SingleplayerServerController.isWorldRunning()) {
+ 				SingleplayerServerController.shutdownEaglercraftServer();
+ 				while (SingleplayerServerController.getStatusState() == IntegratedServerState.WORLD_UNLOADING) {
+ 					EagUtils.sleep(50);
+ 					SingleplayerServerController.runTick();
+ 				}
+ 			}
+ 			if (SingleplayerServerController.isIntegratedServerWorkerAlive()
+ 					&& SingleplayerServerController.canKillWorker()) {
+ 				SingleplayerServerController.killWorker();
+ 				EagUtils.sleep(50);
+ 			}

> CHANGE  1 : 2  @  1 : 2

~ 			EagRuntime.destroy();

> CHANGE  1 : 2  @  1 : 2

~ 				EagRuntime.exit();

> DELETE  3  @  3 : 5

> CHANGE  3 : 5  @  3 : 6

~ 		long i = EagRuntime.nanoTime();
~ 		if (Display.isCloseRequested()) {

> INSERT  3 : 6  @  3

+ 		PointerInputAbstraction.runGameLoop();
+ 		this.gameSettings.touchscreen = PointerInputAbstraction.isTouchMode();
+ 

> DELETE  8  @  8 : 9

> CHANGE  2 : 3  @  2 : 3

~ 				Util.func_181617_a((FutureTask) this.scheduledTasks.remove(0), logger);

> CHANGE  3 : 4  @  3 : 6

~ 		long l = EagRuntime.nanoTime();

> INSERT  3 : 6  @  3

+ 			if (j < this.timer.elapsedTicks - 1) {
+ 				PointerInputAbstraction.runGameLoop();
+ 			}

> CHANGE  2 : 3  @  2 : 4

~ 		long i1 = EagRuntime.nanoTime() - l;

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 11

> CHANGE  1 : 12  @  1 : 7

~ 		if (!Display.contextLost()) {
~ 			EaglercraftGPU.optimize();
~ 			_wglBindFramebuffer(0x8D40, null);
~ 			GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
~ 			GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
~ 			GlStateManager.pushMatrix();
~ 			GlStateManager.clear(16640);
~ 			GlStateManager.enableTexture2D();
~ 			if (this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock()) {
~ 				this.gameSettings.thirdPersonView = 0;
~ 			}

> CHANGE  1 : 3  @  1 : 5

~ 			if (!this.skipRenderWorld) {
~ 				this.entityRenderer.func_181560_a(this.timer.renderPartialTicks, i);

> CHANGE  2 : 5  @  2 : 7

~ 			this.guiAchievement.updateAchievementWindow();
~ 			this.touchOverlayRenderer.render(displayWidth, displayHeight, scaledResolution);
~ 			GlStateManager.popMatrix();

> DELETE  2  @  2 : 12

> DELETE  1  @  1 : 9

> INSERT  1 : 2  @  1

+ 

> CHANGE  1 : 2  @  1 : 4

~ 		long k = EagRuntime.nanoTime();

> CHANGE  5 : 6  @  5 : 6

~ 			this.debug = HString.format("%d fps (%d chunk update%s) T: %s%s%s%s",

> CHANGE  5 : 7  @  5 : 9

~ 							this.gameSettings.fancyGraphics ? "" : " fast", this.gameSettings.clouds == 0 ? ""
~ 									: (this.gameSettings.clouds == 1 ? " fast-clouds" : " fancy-clouds") });

> DELETE  3  @  3 : 7

> CHANGE  2 : 5  @  2 : 7

~ //		if (this.isFramerateLimitBelowMax()) {
~ //			Display.sync(this.getLimitFramerate());
~ //		}

> CHANGE  1 : 2  @  1 : 2

~ 		Mouse.tickCursorShape();

> CHANGE  3 : 13  @  3 : 6

~ 		if (Display.isVSyncSupported()) {
~ 			Display.setVSync(this.gameSettings.enableVsync);
~ 		} else {
~ 			this.gameSettings.enableVsync = false;
~ 		}
~ 		if (!this.gameSettings.enableVsync && this.isFramerateLimitBelowMax()) {
~ 			Display.update(this.getLimitFramerate());
~ 		} else {
~ 			Display.update(0);
~ 		}

> CHANGE  4 : 7  @  4 : 5

~ 		float dpiFetch = -1.0f;
~ 		if (!this.fullscreen
~ 				&& (Display.wasResized() || (dpiFetch = Math.max(Display.getDPI(), 1.0f)) != this.displayDPI)) {

> INSERT  2 : 3  @  2

+ 			float f = this.displayDPI;

> CHANGE  2 : 4  @  2 : 3

~ 			this.displayDPI = dpiFetch == -1.0f ? Math.max(Display.getDPI(), 1.0f) : dpiFetch;
~ 			if (this.displayWidth != i || this.displayHeight != j || this.displayDPI != f) {

> DELETE  22  @  22 : 180

> CHANGE  5 : 7  @  5 : 6

~ 		boolean touch = PointerInputAbstraction.isTouchMode();
~ 		if (touch || Display.isActive()) {

> CHANGE  2 : 5  @  2 : 3

~ 				if (!touch && mouseGrabSupported) {
~ 					this.mouseHelper.grabMouseCursor();
~ 				}

> CHANGE  10 : 13  @  10 : 11

~ 			if (!PointerInputAbstraction.isTouchMode() && mouseGrabSupported) {
~ 				this.mouseHelper.ungrabMouseCursor();
~ 			}

> DELETE  6  @  6 : 10

> CHANGE  55 : 56  @  55 : 56

~ 	public void rightClickMouse() {

> CHANGE  52 : 53  @  52 : 92

~ 		Display.toggleFullscreen();

> INSERT  5 : 6  @  5

+ 		this.scaledResolution = new ScaledResolution(this);

> CHANGE  1 : 2  @  1 : 3

~ 			this.currentScreen.onResize(this, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());

> DELETE  3  @  3 : 5

> CHANGE  1 : 3  @  1 : 5

~ 		if (voiceOverlay != null) {
~ 			voiceOverlay.setResolution(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());

> INSERT  1 : 5  @  1

+ 		if (notifRenderer != null) {
+ 			notifRenderer.setResolution(this, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(),
+ 					scaledResolution.getScaleFactor());
+ 		}

> INSERT  1 : 4  @  1

+ 		EagRuntime.getConfiguration().getHooks().callScreenChangedHook(
+ 				currentScreen != null ? currentScreen.getClass().getName() : null, scaledResolution.getScaledWidth(),
+ 				scaledResolution.getScaledHeight(), displayWidth, displayHeight, scaledResolution.getScaleFactor());

> CHANGE  11 : 50  @  11 : 12

~ 		RateLimitTracker.tick();
~ 
~ 		boolean isHostingLAN = LANServerController.isHostingLAN();
~ 		this.isGamePaused = !isHostingLAN && this.isSingleplayer() && this.theWorld != null && this.thePlayer != null
~ 				&& this.currentScreen != null && this.currentScreen.doesGuiPauseGame();
~ 
~ 		if (isLANOpen && !isHostingLAN) {
~ 			ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("lanServer.relayDisconnected"));
~ 		}
~ 		isLANOpen = isHostingLAN;
~ 
~ 		if (wasPaused != isGamePaused) {
~ 			SingleplayerServerController.setPaused(this.isGamePaused);
~ 			wasPaused = isGamePaused;
~ 		}
~ 
~ 		PlatformWebRTC.runScheduledTasks();
~ 		WebViewOverlayController.runTick();
~ 		SingleplayerServerController.runTick();
~ 		RelayUpdateChecker.runTick();
~ 
~ 		UpdateResultObj update = UpdateService.getUpdateResult();
~ 		if (update != null) {
~ 			if (update.isSuccess()) {
~ 				UpdateDataObj updateSuccess = update.getSuccess();
~ 				if (EagRuntime.getConfiguration().isAllowBootMenu()) {
~ 					if (currentScreen == null || (!(currentScreen instanceof GuiUpdateDownloadSuccess)
~ 							&& !(currentScreen instanceof GuiUpdateInstallOptions))) {
~ 						displayGuiScreen(new GuiUpdateDownloadSuccess(currentScreen, updateSuccess));
~ 					}
~ 				} else {
~ 					UpdateService.quine(updateSuccess.clientSignature, updateSuccess.clientBundle);
~ 				}
~ 			} else {
~ 				displayGuiScreen(
~ 						new GuiScreenGenericErrorMessage("updateFailed.title", update.getFailure(), currentScreen));
~ 			}
~ 		}
~ 

> CHANGE  4 : 6  @  4 : 5

~ 		VoiceClientController.tickVoiceClient(this);
~ 

> DELETE  1  @  1 : 2

> CHANGE  4 : 8  @  4 : 5

~ 		if (this.thePlayer != null && this.thePlayer.sendQueue != null) {
~ 			this.thePlayer.sendQueue.getEaglerMessageController().flush();
~ 		}
~ 

> INSERT  2 : 4  @  2

+ 			GlStateManager.viewport(0, 0, displayWidth, displayHeight); // to be safe
+ 			GlStateManager.enableAlpha();

> INSERT  8 : 14  @  8

+ 			if (this.currentScreen == null && this.dontPauseTimer <= 0 && !PointerInputAbstraction.isTouchMode()) {
+ 				if (!Mouse.isMouseGrabbed()) {
+ 					this.setIngameNotInFocus();
+ 					this.displayInGameMenu();
+ 				}
+ 			}

> INSERT  7 : 12  @  7

+ 			this.dontPauseTimer = 6;
+ 		} else {
+ 			if (this.dontPauseTimer > 0) {
+ 				--this.dontPauseTimer;
+ 			}

> INSERT  2 : 9  @  2

+ 		String pastedStr;
+ 		while ((pastedStr = Touch.getPastedString()) != null) {
+ 			if (this.currentScreen != null) {
+ 				this.currentScreen.fireInputEvent(EnumInputEvent.CLIPBOARD_PASTE, pastedStr);
+ 			}
+ 		}
+ 

> CHANGE  8 : 9  @  8 : 9

~ 						return Minecraft.this.currentScreen.getClass().getName();

> CHANGE  13 : 14  @  13 : 14

~ 							return Minecraft.this.currentScreen.getClass().getName();

> DELETE  8  @  8 : 9

> CHANGE  1 : 39  @  1 : 7

~ 			boolean touched;
~ 			boolean moused = false;
~ 
~ 			while ((touched = Touch.next()) || (moused = Mouse.next())) {
~ 				boolean touch = false;
~ 				if (touched) {
~ 					PointerInputAbstraction.enterTouchModeHook();
~ 					boolean mouse = moused;
~ 					moused = false;
~ 					int tc = Touch.getEventTouchPointCount();
~ 					if (tc > 0) {
~ 						for (int i = 0; i < tc; ++i) {
~ 							final int uid = Touch.getEventTouchPointUID(i);
~ 							int x = Touch.getEventTouchX(i);
~ 							int y = Touch.getEventTouchY(i);
~ 							switch (Touch.getEventType()) {
~ 							case TOUCHSTART:
~ 								if (TouchControls.handleTouchBegin(uid, x, y)) {
~ 									break;
~ 								}
~ 								touch = true;
~ 								handlePlaceTouchStart();
~ 								break;
~ 							case TOUCHEND:
~ 								if (TouchControls.handleTouchEnd(uid, x, y)) {
~ 									touch = true;
~ 									break;
~ 								}
~ 								handlePlaceTouchEnd();
~ 								break;
~ 							default:
~ 								break;
~ 							}
~ 						}
~ 						TouchControls.handleInput();
~ 						if (!touch) {
~ 							continue;
~ 						}

> CHANGE  1 : 4  @  1 : 2

~ 						if (!mouse) {
~ 							continue;
~ 						}

> INSERT  3 : 16  @  3

+ 				if (!touch) {
+ 					int i = Mouse.getEventButton();
+ 					KeyBinding.setKeyBindState(i - 100, Mouse.getEventButtonState());
+ 					if (Mouse.getEventButtonState()) {
+ 						PointerInputAbstraction.enterMouseModeHook();
+ 						if (this.thePlayer.isSpectator() && i == 2) {
+ 							this.ingameGUI.getSpectatorGui().func_175261_b();
+ 						} else {
+ 							KeyBinding.onTick(i - 100);
+ 						}
+ 					}
+ 				}
+ 

> CHANGE  2 : 17  @  2 : 8

~ 					if (!touch) {
~ 						int j = Mouse.getEventDWheel();
~ 						if (j != 0) {
~ 							if (this.isZoomKey) {
~ 								this.adjustedZoomValue = MathHelper.clamp_float(adjustedZoomValue - j * 4.0f, 4.0f,
~ 										32.0f);
~ 							} else if (this.thePlayer.isSpectator()) {
~ 								j = j < 0 ? -1 : 1;
~ 								if (this.ingameGUI.getSpectatorGui().func_175262_a()) {
~ 									this.ingameGUI.getSpectatorGui().func_175259_b(-j);
~ 								} else {
~ 									float f = MathHelper.clamp_float(
~ 											this.thePlayer.capabilities.getFlySpeed() + (float) j * 0.005F, 0.0F, 0.2F);
~ 									this.thePlayer.capabilities.setFlySpeed(f);
~ 								}

> CHANGE  1 : 2  @  1 : 4

~ 								this.thePlayer.inventory.changeCurrentItem(j);

> DELETE  1  @  1 : 3

> CHANGE  4 : 7  @  4 : 5

~ 						if ((!this.inGameHasFocus || !(touch || Mouse.isActuallyGrabbed()))
~ 								&& (touch || Mouse.getEventButtonState())) {
~ 							this.inGameHasFocus = false;

> CHANGE  3 : 8  @  3 : 4

~ 						if (touch) {
~ 							this.currentScreen.handleTouchInput();
~ 						} else {
~ 							this.currentScreen.handleMouseInput();
~ 						}

> CHANGE  8 : 9  @  8 : 9

~ 			processTouchMine();

> INSERT  3 : 6  @  3

+ 				if (k == 0x1D && (Keyboard.areKeysLocked() || isFullScreen())) {
+ 					KeyBinding.setKeyBindState(gameSettings.keyBindSprint.getKeyCode(), Keyboard.getEventKeyState());
+ 				}

> CHANGE  19 : 27  @  19 : 21

~ 					if (EaglerDeferredPipeline.instance != null) {
~ 						if (k == 62) {
~ 							DebugFramebufferView.toggleDebugView();
~ 						} else if (k == 0xCB || k == 0xC8) {
~ 							DebugFramebufferView.switchView(-1);
~ 						} else if (k == 0xCD || k == 0xD0) {
~ 							DebugFramebufferView.switchView(1);
~ 						}

> CHANGE  5 : 6  @  5 : 6

~ 						if (k == 1 || (k > -1 && k == this.gameSettings.keyBindClose.getKeyCode())) {

> INSERT  11 : 18  @  11

+ 						if (k == 19 && Keyboard.isKeyDown(61)) { // F3+R
+ 							if (gameSettings.shaders) {
+ 								ShaderSource.clearCache();
+ 								this.renderGlobal.loadRenderers();
+ 							}
+ 						}
+ 

> INSERT  30 : 31  @  30

+ 							GlStateManager.recompileShaders();

> CHANGE  28 : 29  @  28 : 40

~ 							togglePerspective();

> DELETE  6  @  6 : 18

> INSERT  13 : 19  @  13

+ 			boolean zoomKey = this.gameSettings.keyBindZoomCamera.isKeyDown();
+ 			if (zoomKey != isZoomKey) {
+ 				adjustedZoomValue = startZoomValue;
+ 				isZoomKey = zoomKey;
+ 			}
+ 

> INSERT  26 : 28  @  26

+ 			boolean miningTouch = isMiningTouch();
+ 			boolean useTouch = thePlayer.getItemShouldUseOnTouchEagler();

> CHANGE  1 : 2  @  1 : 2

~ 				if (!this.gameSettings.keyBindUseItem.isKeyDown() && !miningTouch) {

> INSERT  19 : 28  @  19

+ 				if (miningTouch && !wasMiningTouch) {
+ 					if ((objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectType.ENTITY) || useTouch) {
+ 						this.rightClickMouse();
+ 					} else {
+ 						this.clickMouse();
+ 					}
+ 					wasMiningTouch = true;
+ 				}
+ 

> INSERT  8 : 9  @  8

+ 			wasMiningTouch = miningTouch;

> INSERT  6 : 10  @  6

+ 			if (miningTouch && useTouch && this.rightClickDelayTimer == 0 && !this.thePlayer.isUsingItem()) {
+ 				this.rightClickMouse();
+ 			}
+ 

> CHANGE  1 : 3  @  1 : 2

~ 					this.currentScreen == null && (this.gameSettings.keyBindAttack.isKeyDown() || miningTouch)
~ 							&& this.inGameHasFocus && !useTouch);

> DELETE  11  @  11 : 12

> DELETE  4  @  4 : 5

> DELETE  4  @  4 : 5

> INSERT  7 : 8  @  7

+ 			this.eagskullCommand.tick();

> DELETE  28  @  28 : 29

> DELETE  5  @  5 : 6

> DELETE  4  @  4 : 5

> CHANGE  3 : 46  @  3 : 15

~ 		if (this.theWorld != null) {
~ 			++joinWorldTickCounter;
~ 			if (bungeeOutdatedMsgTimer > 0) {
~ 				if (--bungeeOutdatedMsgTimer == 0 && this.thePlayer.sendQueue != null) {
~ 					String pluginBrand = this.thePlayer.sendQueue.getNetworkManager().getPluginBrand();
~ 					String pluginVersion = this.thePlayer.sendQueue.getNetworkManager().getPluginVersion();
~ 					if (pluginBrand != null && pluginVersion != null
~ 							&& EaglerXBungeeVersion.isUpdateToPluginAvailable(pluginBrand, pluginVersion)) {
~ 						String pfx = EnumChatFormatting.GOLD + "[EagX]" + EnumChatFormatting.AQUA;
~ 						ingameGUI.getChatGUI().printChatMessage(
~ 								new ChatComponentText(pfx + " ---------------------------------------"));
~ 						ingameGUI.getChatGUI().printChatMessage(
~ 								new ChatComponentText(pfx + " This server appears to be using version "
~ 										+ EnumChatFormatting.YELLOW + pluginVersion));
~ 						ingameGUI.getChatGUI().printChatMessage(
~ 								new ChatComponentText(pfx + " of the EaglerXBungee plugin which is outdated"));
~ 						ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(pfx));
~ 						ingameGUI.getChatGUI()
~ 								.printChatMessage(new ChatComponentText(pfx + " If you are the admin update to "
~ 										+ EnumChatFormatting.YELLOW + EaglerXBungeeVersion.getPluginVersion()
~ 										+ EnumChatFormatting.AQUA + " or newer"));
~ 						ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(pfx));
~ 						ingameGUI.getChatGUI().printChatMessage((new ChatComponentText(pfx + " Click: "))
~ 								.appendSibling((new ChatComponentText("" + EnumChatFormatting.GREEN
~ 										+ EnumChatFormatting.UNDERLINE + EaglerXBungeeVersion.getPluginButton()))
~ 												.setChatStyle((new ChatStyle()).setChatClickEvent(
~ 														new ClickEvent(ClickEvent.Action.EAGLER_PLUGIN_DOWNLOAD,
~ 																"plugin_download.zip")))));
~ 						ingameGUI.getChatGUI().printChatMessage(
~ 								new ChatComponentText(pfx + " ---------------------------------------"));
~ 					}
~ 				}
~ 			}
~ 		} else {
~ 			joinWorldTickCounter = 0;
~ 			if (currentScreen != null && currentScreen.shouldHangupIntegratedServer()) {
~ 				if (SingleplayerServerController.hangupEaglercraftServer()) {
~ 					this.displayGuiScreen(new GuiScreenIntegratedServerBusy(currentScreen,
~ 							"singleplayer.busy.stoppingIntegratedServer",
~ 							"singleplayer.failed.stoppingIntegratedServer", SingleplayerServerController::isReady));
~ 				}
~ 			}
~ 			TouchControls.resetSneak();

> CHANGE  2 : 30  @  2 : 4

~ 		if (reconnectURI != null) {
~ 			String reconURI = reconnectURI;
~ 			reconnectURI = null;
~ 			if (EagRuntime.getConfiguration().isAllowServerRedirects()) {
~ 				boolean enableCookies;
~ 				boolean msg;
~ 				if (this.currentServerData != null) {
~ 					enableCookies = this.currentServerData.enableCookies;
~ 					msg = false;
~ 				} else {
~ 					enableCookies = EagRuntime.getConfiguration().isEnableServerCookies();
~ 					msg = true;
~ 				}
~ 				if (theWorld != null) {
~ 					theWorld.sendQuittingDisconnectingPacket();
~ 					loadWorld(null);
~ 				}
~ 				logger.info("Recieved SPacketRedirectClientV4EAG, reconnecting to: {}", reconURI);
~ 				if (msg) {
~ 					logger.warn("No existing server connection, cookies will default to {}!",
~ 							enableCookies ? "enabled" : "disabled");
~ 				}
~ 				ServerAddress addr = AddressResolver.resolveAddressFromURI(reconURI);
~ 				this.displayGuiScreen(
~ 						new GuiConnecting(new GuiMainMenu(), this, addr.getIP(), addr.getPort(), enableCookies));
~ 			} else {
~ 				logger.warn("Server redirect blocked: {}", reconURI);
~ 			}

> CHANGE  2 : 4  @  2 : 13

~ 		this.systemTime = getSystemTime();
~ 	}

> CHANGE  1 : 4  @  1 : 2

~ 	private long placeTouchStartTime = -1l;
~ 	private long mineTouchStartTime = -1l;
~ 	private boolean wasMiningTouch = false;

> CHANGE  1 : 12  @  1 : 5

~ 	private void processTouchMine() {
~ 		if ((currentScreen == null || currentScreen.allowUserInput)
~ 				&& PointerInputAbstraction.isTouchingScreenNotButton()) {
~ 			if (PointerInputAbstraction.isDraggingNotTouching()) {
~ 				if (mineTouchStartTime != -1l) {
~ 					long l = EagRuntime.steadyTimeMillis();
~ 					if ((placeTouchStartTime == -1l || (l - placeTouchStartTime) < 350l)
~ 							|| (l - mineTouchStartTime) < 350l) {
~ 						mineTouchStartTime = -1l;
~ 					}
~ 				}

> CHANGE  1 : 4  @  1 : 2

~ 				if (mineTouchStartTime == -1l) {
~ 					mineTouchStartTime = EagRuntime.steadyTimeMillis();
~ 				}

> INSERT  1 : 5  @  1

+ 		} else {
+ 			mineTouchStartTime = -1l;
+ 		}
+ 	}

> CHANGE  1 : 23  @  1 : 5

~ 	private boolean isMiningTouch() {
~ 		if (mineTouchStartTime == -1l)
~ 			return false;
~ 		long l = EagRuntime.steadyTimeMillis();
~ 		return (placeTouchStartTime == -1l || (l - placeTouchStartTime) >= 350l) && (l - mineTouchStartTime) >= 350l;
~ 	}
~ 
~ 	private void handlePlaceTouchStart() {
~ 		if (placeTouchStartTime == -1l) {
~ 			placeTouchStartTime = EagRuntime.steadyTimeMillis();
~ 		}
~ 	}
~ 
~ 	private void handlePlaceTouchEnd() {
~ 		if (placeTouchStartTime != -1l) {
~ 			int len = (int) (EagRuntime.steadyTimeMillis() - placeTouchStartTime);
~ 			if (len < 350l && !PointerInputAbstraction.isDraggingNotTouching()) {
~ 				if (objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectType.ENTITY) {
~ 					clickMouse();
~ 				} else {
~ 					rightClickMouse();
~ 				}

> INSERT  1 : 2  @  1

+ 			placeTouchStartTime = -1l;

> INSERT  1 : 2  @  1

+ 	}

> CHANGE  1 : 14  @  1 : 8

~ 	public void togglePerspective() {
~ 		++this.gameSettings.thirdPersonView;
~ 		if (this.gameSettings.thirdPersonView > 2) {
~ 			this.gameSettings.thirdPersonView = 0;
~ 		}
~ 
~ 		if (this.gameSettings.thirdPersonView == 0) {
~ 			this.entityRenderer.loadEntityShader(this.getRenderViewEntity());
~ 		} else if (this.gameSettings.thirdPersonView == 1) {
~ 			this.entityRenderer.loadEntityShader((Entity) null);
~ 		}
~ 
~ 		this.renderGlobal.setDisplayListEntitiesDirty();

> INSERT  2 : 20  @  2

+ 	public void launchIntegratedServer(String folderName, String worldName, WorldSettings worldSettingsIn) {
+ 		this.loadWorld((WorldClient) null);
+ 		renderManager.setEnableFNAWSkins(this.gameSettings.enableFNAWSkins);
+ 		session.reset();
+ 		EaglerProfile.clearServerSkinOverride();
+ 		PauseMenuCustomizeState.reset();
+ 		SingleplayerServerController.launchEaglercraftServer(folderName, gameSettings.difficulty.getDifficultyId(),
+ 				Math.max(gameSettings.renderDistanceChunks, 2), worldSettingsIn);
+ 		EagRuntime.setMCServerWindowGlobal("singleplayer");
+ 		this.displayGuiScreen(new GuiScreenIntegratedServerBusy(
+ 				new GuiScreenSingleplayerConnecting(new GuiMainMenu(), "Connecting to " + folderName),
+ 				"singleplayer.busy.startingIntegratedServer", "singleplayer.failed.startingIntegratedServer",
+ 				() -> SingleplayerServerController.isWorldReady(), (t, u) -> {
+ 					Minecraft.this.displayGuiScreen(GuiScreenIntegratedServerBusy.createException(new GuiMainMenu(),
+ 							((GuiScreenIntegratedServerBusy) t).failMessage, u));
+ 				}));
+ 	}
+ 

> INSERT  10 : 15  @  10

+ 			session.reset();
+ 			EaglerProfile.clearServerSkinOverride();
+ 			PauseMenuCustomizeState.reset();
+ 			ClientUUIDLoadingCache.flushRequestCache();
+ 			WebViewOverlayController.setPacketSendCallback(null);

> DELETE  1  @  1 : 7

> DELETE  40  @  40 : 41

> CHANGE  20 : 21  @  20 : 22

~ 		this.thePlayer = this.playerController.func_178892_a(this.theWorld, new StatFileWriter());

> CHANGE  18 : 19  @  18 : 19

~ 		return EagRuntime.getConfiguration().isDemo();

> CHANGE  15 : 19  @  15 : 16

~ 		if (theMinecraft == null)
~ 			return false;
~ 		GameSettings g = theMinecraft.gameSettings;
~ 		return g.ambientOcclusion != 0 && !g.shadersAODisable;

> CHANGE  2 : 3  @  2 : 3

~ 	public void middleClickMouse() {

> CHANGE  127 : 128  @  127 : 128

~ 				return EagRuntime.getVersion();

> CHANGE  4 : 6  @  4 : 5

~ 				return EaglercraftGPU.glGetString(7937) + " GL version " + EaglercraftGPU.glGetString(7938) + ", "
~ 						+ EaglercraftGPU.glGetString(7936);

> CHANGE  2 : 5  @  2 : 5

~ 		theCrash.getCategory().addCrashSectionCallable("Is Eagler Shaders", new Callable<String>() {
~ 			public String call() throws Exception {
~ 				return Minecraft.this.gameSettings.shaders ? "Yes" : "No";

> CHANGE  2 : 6  @  2 : 5

~ 		theCrash.getCategory().addCrashSectionCallable("Is Dynamic Lights", new Callable<String>() {
~ 			public String call() throws Exception {
~ 				return !Minecraft.this.gameSettings.shaders && Minecraft.this.gameSettings.enableDynamicLights ? "Yes"
~ 						: "No";

> INSERT  2 : 47  @  2

+ 		theCrash.getCategory().addCrashSectionCallable("In Ext. Pipeline", new Callable<String>() {
+ 			public String call() throws Exception {
+ 				return GlStateManager.isExtensionPipeline() ? "Yes" : "No";
+ 			}
+ 		});
+ 		theCrash.getCategory().addCrashSectionCallable("GPU Shader5 Capable", new Callable<String>() {
+ 			public String call() throws Exception {
+ 				return EaglercraftGPU.checkShader5Capable() ? "Yes" : "No";
+ 			}
+ 		});
+ 		theCrash.getCategory().addCrashSectionCallable("GPU TexStorage Capable", new Callable<String>() {
+ 			public String call() throws Exception {
+ 				return EaglercraftGPU.checkTexStorageCapable() ? "Yes" : "No";
+ 			}
+ 		});
+ 		theCrash.getCategory().addCrashSectionCallable("GPU TextureLOD Capable", new Callable<String>() {
+ 			public String call() throws Exception {
+ 				return EaglercraftGPU.checkTextureLODCapable() ? "Yes" : "No";
+ 			}
+ 		});
+ 		theCrash.getCategory().addCrashSectionCallable("GPU Instancing Capable", new Callable<String>() {
+ 			public String call() throws Exception {
+ 				return EaglercraftGPU.checkInstancingCapable() ? "Yes" : "No";
+ 			}
+ 		});
+ 		theCrash.getCategory().addCrashSectionCallable("GPU VAO Capable", new Callable<String>() {
+ 			public String call() throws Exception {
+ 				return EaglercraftGPU.checkVAOCapable() ? "Yes" : "No";
+ 			}
+ 		});
+ 		theCrash.getCategory().addCrashSectionCallable("Is Software VAOs", new Callable<String>() {
+ 			public String call() throws Exception {
+ 				return EaglercraftGPU.areVAOsEmulated() ? "Yes" : "No";
+ 			}
+ 		});
+ 		theCrash.getCategory().addCrashSectionCallable("GPU Render-to-MipMap", new Callable<String>() {
+ 			public String call() throws Exception {
+ 				return EaglercraftGPU.checkFBORenderMipmapCapable() ? "Yes" : "No";
+ 			}
+ 		});
+ 		theCrash.getCategory().addCrashSectionCallable("Touch Mode", new Callable<String>() {
+ 			public String call() throws Exception {
+ 				return PointerInputAbstraction.isTouchMode() ? "Yes" : "No";
+ 			}
+ 		});

> CHANGE  2 : 3  @  2 : 6

~ 				return "Definitely Not; You're an eagler";

> CHANGE  32 : 33  @  32 : 34

~ 				return "N/A (disabled)";

> DELETE  2  @  2 : 7

> CHANGE  12 : 13  @  12 : 13

~ 		return this.addScheduledTaskFuture(new Runnable() {

> INSERT  1 : 3  @  1

+ 				Minecraft.this.loadingScreen.eaglerShow(I18n.format("resourcePack.load.refreshing"),
+ 						I18n.format("resourcePack.load.pleaseWait"));

> DELETE  5  @  5 : 32

> CHANGE  1 : 2  @  1 : 6

~ 		return this.currentServerData != null ? "multiplayer" : "out_of_game";

> DELETE  2  @  2 : 219

> INSERT  6 : 7  @  6

+ 		EagRuntime.setMCServerWindowGlobal(serverDataIn != null ? serverDataIn.serverIP : null);

> CHANGE  7 : 8  @  7 : 8

~ 		return SingleplayerServerController.isWorldRunning();

> CHANGE  3 : 4  @  3 : 4

~ 		return SingleplayerServerController.isWorldRunning();

> DELETE  2  @  2 : 6

> DELETE  1  @  1 : 6

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 6

> CHANGE  1 : 2  @  1 : 2

~ 		return EagRuntime.steadyTimeMillis();

> CHANGE  3 : 4  @  3 : 4

~ 		return Display.isFullscreen();

> DELETE  6  @  6 : 23

> DELETE  44  @  44 : 48

> CHANGE  6 : 8  @  6 : 46

~ 					if (i == this.gameSettings.keyBindScreenshot.getKeyCode()) {
~ 						this.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot());

> DELETE  1  @  1 : 3

> DELETE  1  @  1 : 2

> DELETE  4  @  4 : 12

> CHANGE  9 : 10  @  9 : 10

~ 	public <V> ListenableFuture<V> addScheduledTaskFuture(Callable<V> callableToSchedule) {

> CHANGE  1 : 5  @  1 : 13

~ 		ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(callableToSchedule);
~ 		synchronized (this.scheduledTasks) {
~ 			this.scheduledTasks.add(listenablefuturetask);
~ 			return listenablefuturetask;

> CHANGE  3 : 4  @  3 : 4

~ 	public ListenableFuture<Object> addScheduledTaskFuture(Runnable runnableToSchedule) {

> CHANGE  1 : 2  @  1 : 2

~ 		return this.addScheduledTaskFuture(Executors.callable(runnableToSchedule));

> CHANGE  2 : 4  @  2 : 4

~ 	public void addScheduledTask(Runnable runnableToSchedule) {
~ 		this.addScheduledTaskFuture(Executors.callable(runnableToSchedule));

> DELETE  26  @  26 : 34

> INSERT  7 : 44  @  7

+ 
+ 	public static int getGLMaximumTextureSize() {
+ 		return EaglercraftGPU.glGetInteger(GL_MAX_TEXTURE_SIZE);
+ 	}
+ 
+ 	public ModelManager getModelManager() {
+ 		return modelManager;
+ 	}
+ 
+ 	public ISaveFormat getSaveLoader() {
+ 		return SingleplayerServerController.instance;
+ 	}
+ 
+ 	public void clearTitles() {
+ 		ingameGUI.displayTitle(null, null, -1, -1, -1);
+ 	}
+ 
+ 	public boolean getEnableFNAWSkins() {
+ 		boolean ret = this.gameSettings.enableFNAWSkins;
+ 		if (this.thePlayer != null) {
+ 			if (this.thePlayer.sendQueue.currentFNAWSkinForcedState) {
+ 				ret = true;
+ 			} else {
+ 				ret &= this.thePlayer.sendQueue.currentFNAWSkinAllowedState;
+ 			}
+ 		}
+ 		return ret;
+ 	}
+ 
+ 	public void handleReconnectPacket(String redirectURI) {
+ 		this.reconnectURI = redirectURI;
+ 	}
+ 
+ 	public boolean isEnableProfanityFilter() {
+ 		return EagRuntime.getConfiguration().isForceProfanityFilter() || gameSettings.enableProfanityFilter;
+ 	}
+ 

> EOF
