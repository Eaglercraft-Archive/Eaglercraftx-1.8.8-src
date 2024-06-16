
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 17

~ import static net.lax1dude.eaglercraft.v1_8.internal.PlatformOpenGL._wglBindFramebuffer;
~ 

> DELETE  2  @  2 : 6

> DELETE  1  @  1 : 2

> CHANGE  2 : 3  @  2 : 6

~ import java.util.LinkedList;

> DELETE  1  @  1 : 4

> CHANGE  1 : 56  @  1 : 4

~ 
~ import net.lax1dude.eaglercraft.v1_8.internal.PlatformInput;
~ 
~ import org.apache.commons.lang3.Validate;
~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.Display;
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.EagUtils;
~ import net.lax1dude.eaglercraft.v1_8.EaglerXBungeeVersion;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ import net.lax1dude.eaglercraft.v1_8.futures.Executors;
~ import net.lax1dude.eaglercraft.v1_8.futures.FutureTask;
~ import net.lax1dude.eaglercraft.v1_8.futures.ListenableFuture;
~ import net.lax1dude.eaglercraft.v1_8.futures.ListenableFutureTask;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
~ import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFontRenderer;
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
~ import net.lax1dude.eaglercraft.v1_8.update.RelayUpdateChecker;
~ import net.lax1dude.eaglercraft.v1_8.voice.GuiVoiceOverlay;
~ import net.lax1dude.eaglercraft.v1_8.voice.VoiceClientController;

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

> DELETE  1  @  1 : 3

> DELETE  1  @  1 : 3

> INSERT  6 : 9  @  6

+ import net.minecraft.util.ChatComponentTranslation;
+ import net.minecraft.util.ChatStyle;
+ import net.minecraft.util.EnumChatFormatting;

> INSERT  11 : 12  @  11

+ import net.minecraft.util.StringTranslate;

> DELETE  6  @  6 : 7

> DELETE  1  @  1 : 19

> CHANGE  1 : 2  @  1 : 2

~ public class Minecraft implements IThreadListener {

> CHANGE  2 : 3  @  2 : 9

~ 	public static final boolean isRunningOnMac = false;

> DELETE  12  @  12 : 14

> INSERT  11 : 12  @  11

+ 	private boolean wasPaused;

> DELETE  8  @  8 : 9

> DELETE  6  @  6 : 8

> DELETE  1  @  1 : 3

> CHANGE  10 : 11  @  10 : 12

~ 	private EaglercraftNetworkManager myNetworkManager;

> DELETE  9  @  9 : 11

> CHANGE  4 : 5  @  4 : 7

~ 	private final List<FutureTask<?>> scheduledTasks = new LinkedList();

> INSERT  14 : 18  @  14

+ 	public int joinWorldTickCounter = 0;
+ 	private int dontPauseTimer = 0;
+ 	public int bungeeOutdatedMsgTimer = 0;
+ 	private boolean isLANOpen = false;

> INSERT  1 : 9  @  1

+ 	public SkullCommand eagskullCommand;
+ 
+ 	public GuiVoiceOverlay voiceOverlay;
+ 
+ 	public float startZoomValue = 18.0f;
+ 	public float adjustedZoomValue = 18.0f;
+ 	public boolean isZoomKey = false;
+ 

> CHANGE  2 : 3  @  2 : 5

~ 		StringTranslate.initClient();

> CHANGE  1 : 2  @  1 : 9

~ 		this.mcDefaultResourcePack = new DefaultResourcePack();

> CHANGE  1 : 2  @  1 : 4

~ 		logger.info("Setting user: " + this.session.getProfile().getName());

> CHANGE  6 : 11  @  6 : 10

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

> DELETE  8  @  8 : 11

> CHANGE  3 : 5  @  3 : 5

~ 		this.fontRendererObj = new EaglerFontRenderer(this.gameSettings,
~ 				new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);

> CHANGE  5 : 6  @  5 : 6

~ 		this.standardGalacticFontRenderer = new EaglerFontRenderer(this.gameSettings,

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

> INSERT  2 : 14  @  2

+ 		this.eagskullCommand = new SkullCommand(this);
+ 		this.voiceOverlay = new GuiVoiceOverlay(this);
+ 		ScaledResolution voiceRes = new ScaledResolution(this);
+ 		this.voiceOverlay.setResolution(voiceRes.getScaledWidth(), voiceRes.getScaledHeight());
+ 
+ 		ServerList.initServerList(this);
+ 		EaglerProfile.read();
+ 
+ 		GuiScreen mainMenu = new GuiMainMenu();
+ 		if (isDemo()) {
+ 			mainMenu = new GuiScreenDemoIntegratedServerStartup(mainMenu);
+ 		}

> CHANGE  1 : 2  @  1 : 4

~ 			mainMenu = new GuiConnecting(mainMenu, this, this.serverName, this.serverPort);

> INSERT  2 : 4  @  2

+ 		this.displayGuiScreen(new GuiScreenEditProfile(mainMenu));
+ 

> DELETE  3  @  3 : 15

> CHANGE  16 : 17  @  16 : 24

~ 		throw new UnsupportedOperationException("wtf u trying to twitch stream in a browser game?");

> CHANGE  2 : 5  @  2 : 24

~ 	private void createDisplay() {
~ 		Display.create();
~ 		Display.setTitle("Eaglercraft 1.8.8");

> DELETE  2  @  2 : 39

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

> CHANGE  7 : 10  @  7 : 19

~ 	private void updateDisplayMode() {
~ 		this.displayWidth = Display.getWidth();
~ 		this.displayHeight = Display.getHeight();

> CHANGE  2 : 6  @  2 : 46

~ 	private void drawSplashScreen(TextureManager textureManagerInstance) {
~ 		Display.update();
~ 		updateDisplayMode();
~ 		GlStateManager.viewport(0, 0, displayWidth, displayHeight);

> DELETE  2  @  2 : 5

> CHANGE  16 : 17  @  16 : 17

~ 					new DynamicTexture(ImageData.loadImageFile(inputstream)));

> DELETE  24  @  24 : 26

> DELETE  26  @  26 : 30

> CHANGE  31 : 42  @  31 : 32

~ 	public void shutdownIntegratedServer(GuiScreen cont) {
~ 		if (SingleplayerServerController.shutdownEaglercraftServer()
~ 				|| SingleplayerServerController.getStatusState() == IntegratedServerState.WORLD_UNLOADING) {
~ 			displayGuiScreen(new GuiScreenIntegratedServerBusy(cont, "singleplayer.busy.stoppingIntegratedServer",
~ 					"singleplayer.failed.stoppingIntegratedServer", SingleplayerServerController::isReady));
~ 		} else {
~ 			displayGuiScreen(cont);
~ 		}
~ 	}
~ 
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
+ 					EagUtils.sleep(50l);
+ 					SingleplayerServerController.runTick();
+ 				}
+ 			}
+ 			if (SingleplayerServerController.isIntegratedServerWorkerAlive()
+ 					&& SingleplayerServerController.canKillWorker()) {
+ 				SingleplayerServerController.killWorker();
+ 				EagUtils.sleep(50l);
+ 			}

> CHANGE  1 : 2  @  1 : 2

~ 			EagRuntime.destroy();

> CHANGE  1 : 2  @  1 : 2

~ 				EagRuntime.exit();

> DELETE  3  @  3 : 5

> CHANGE  5 : 6  @  5 : 6

~ 		if (Display.isCloseRequested()) {

> CHANGE  14 : 15  @  14 : 15

~ 				Util.func_181617_a((FutureTask) this.scheduledTasks.remove(0), logger);

> CHANGE  7 : 18  @  7 : 8

~ 		if (this.timer.elapsedTicks > 1) {
~ 			long watchdog = System.currentTimeMillis();
~ 			for (int j = 0; j < this.timer.elapsedTicks; ++j) {
~ 				this.runTick();
~ 				long millis = System.currentTimeMillis();
~ 				if (millis - watchdog > 50l) {
~ 					watchdog = millis;
~ 					EagUtils.sleep(0l);
~ 				}
~ 			}
~ 		} else if (this.timer.elapsedTicks == 1) {

> DELETE  10  @  10 : 18

> CHANGE  1 : 4  @  1 : 5

~ 		if (!Display.contextLost()) {
~ 			this.mcProfiler.startSection("EaglercraftGPU_optimize");
~ 			EaglercraftGPU.optimize();

> CHANGE  1 : 11  @  1 : 2

~ 			_wglBindFramebuffer(0x8D40, null);
~ 			GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
~ 			GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
~ 			GlStateManager.pushMatrix();
~ 			GlStateManager.clear(16640);
~ 			this.mcProfiler.startSection("display");
~ 			GlStateManager.enableTexture2D();
~ 			if (this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock()) {
~ 				this.gameSettings.thirdPersonView = 0;
~ 			}

> CHANGE  1 : 6  @  1 : 5

~ 			this.mcProfiler.endSection();
~ 			if (!this.skipRenderWorld) {
~ 				this.mcProfiler.endStartSection("gameRenderer");
~ 				this.entityRenderer.func_181560_a(this.timer.renderPartialTicks, i);
~ 				this.mcProfiler.endSection();

> CHANGE  2 : 18  @  2 : 7

~ 			this.mcProfiler.endSection();
~ 			if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart
~ 					&& !this.gameSettings.hideGUI) {
~ 				if (!this.mcProfiler.profilingEnabled) {
~ 					this.mcProfiler.clearProfiling();
~ 				}
~ 
~ 				this.mcProfiler.profilingEnabled = true;
~ 				this.displayDebugInfo(i1);
~ 			} else {
~ 				this.mcProfiler.profilingEnabled = false;
~ 				this.prevFrameTime = System.nanoTime();
~ 			}
~ 
~ 			this.guiAchievement.updateAchievementWindow();
~ 			GlStateManager.popMatrix();

> DELETE  2  @  2 : 11

> DELETE  2  @  2 : 10

> INSERT  1 : 2  @  1

+ 

> DELETE  1  @  1 : 3

> CHANGE  6 : 7  @  6 : 7

~ 			this.debug = HString.format("%d fps (%d chunk update%s) T: %s%s%s%s",

> CHANGE  5 : 7  @  5 : 9

~ 							this.gameSettings.fancyGraphics ? "" : " fast", this.gameSettings.clouds == 0 ? ""
~ 									: (this.gameSettings.clouds == 1 ? " fast-clouds" : " fancy-clouds") });

> DELETE  3  @  3 : 7

> INSERT  8 : 9  @  8

+ 		Mouse.tickCursorShape();

> INSERT  5 : 10  @  5

+ 		if (Display.isVSyncSupported()) {
+ 			Display.setVSync(this.gameSettings.enableVsync);
+ 		} else {
+ 			this.gameSettings.enableVsync = false;
+ 		}

> DELETE  34  @  34 : 52

> CHANGE  39 : 40  @  39 : 40

~ 			EaglercraftGPU.glLineWidth(1.0F);

> CHANGE  9 : 10  @  9 : 10

~ 					(double) ((float) j - (float) short1 * 0.6F - 16.0F), 0.0D).color(0, 0, 0, 100).endVertex();

> CHANGE  1 : 2  @  1 : 2

~ 					.color(0, 0, 0, 100).endVertex();

> CHANGE  1 : 2  @  1 : 2

~ 					.color(0, 0, 0, 100).endVertex();

> CHANGE  1 : 2  @  1 : 2

~ 					(double) ((float) j - (float) short1 * 0.6F - 16.0F), 0.0D).color(0, 0, 0, 100).endVertex();

> DELETE  110  @  110 : 114

> CHANGE  108 : 109  @  108 : 148

~ 		Display.toggleFullscreen();

> INSERT  5 : 6  @  5

+ 		ScaledResolution scaledresolution = new ScaledResolution(this);

> DELETE  1  @  1 : 2

> DELETE  4  @  4 : 6

> CHANGE  1 : 2  @  1 : 7

~ 		this.voiceOverlay.setResolution(scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());

> INSERT  11 : 30  @  11

+ 		RateLimitTracker.tick();
+ 
+ 		boolean isHostingLAN = LANServerController.isHostingLAN();
+ 		this.isGamePaused = !isHostingLAN && this.isSingleplayer() && this.theWorld != null && this.thePlayer != null
+ 				&& this.currentScreen != null && this.currentScreen.doesGuiPauseGame();
+ 
+ 		if (isLANOpen && !isHostingLAN) {
+ 			ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("lanServer.relayDisconnected"));
+ 		}
+ 		isLANOpen = isHostingLAN;
+ 
+ 		if (wasPaused != isGamePaused) {
+ 			SingleplayerServerController.setPaused(this.isGamePaused);
+ 			wasPaused = isGamePaused;
+ 		}
+ 
+ 		SingleplayerServerController.runTick();
+ 		RelayUpdateChecker.runTick();
+ 

> INSERT  5 : 8  @  5

+ 		this.mcProfiler.endStartSection("eaglerVoice");
+ 		VoiceClientController.tickVoiceClient(this);
+ 

> INSERT  10 : 11  @  10

+ 			GlStateManager.viewport(0, 0, displayWidth, displayHeight); // to be safe

> INSERT  8 : 14  @  8

+ 			if (this.currentScreen == null && this.dontPauseTimer <= 0) {
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

> CHANGE  10 : 11  @  10 : 11

~ 						return Minecraft.this.currentScreen.getClass().getName();

> CHANGE  13 : 14  @  13 : 14

~ 							return Minecraft.this.currentScreen.getClass().getName();

> CHANGE  25 : 28  @  25 : 26

~ 						if (this.isZoomKey) {
~ 							this.adjustedZoomValue = MathHelper.clamp_float(adjustedZoomValue - j * 4.0f, 5.0f, 32.0f);
~ 						} else if (this.thePlayer.isSpectator()) {

> CHANGE  14 : 16  @  14 : 15

~ 						if ((!this.inGameHasFocus || !Mouse.isActuallyGrabbed()) && Mouse.getEventButtonState()) {
~ 							this.inGameHasFocus = false;

> INSERT  16 : 19  @  16

+ 				if (k == 0x1D && (areKeysLocked() || isFullScreen())) {
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

> INSERT  71 : 77  @  71

+ 			boolean zoomKey = this.gameSettings.keyBindZoomCamera.isKeyDown();
+ 			if (zoomKey != isZoomKey) {
+ 				adjustedZoomValue = startZoomValue;
+ 				isZoomKey = zoomKey;
+ 			}
+ 

> INSERT  92 : 93  @  92

+ 			this.eagskullCommand.tick();

> INSERT  43 : 87  @  43

+ 		if (this.theWorld != null) {
+ 			++joinWorldTickCounter;
+ 			if (bungeeOutdatedMsgTimer > 0) {
+ 				if (--bungeeOutdatedMsgTimer == 0 && this.thePlayer.sendQueue != null) {
+ 					String pluginBrand = this.thePlayer.sendQueue.getNetworkManager().getPluginBrand();
+ 					String pluginVersion = this.thePlayer.sendQueue.getNetworkManager().getPluginVersion();
+ 					if (pluginBrand != null && pluginVersion != null
+ 							&& EaglerXBungeeVersion.isUpdateToPluginAvailable(pluginBrand, pluginVersion)) {
+ 						String pfx = EnumChatFormatting.GOLD + "[EagX]" + EnumChatFormatting.AQUA;
+ 						ingameGUI.getChatGUI().printChatMessage(
+ 								new ChatComponentText(pfx + " ---------------------------------------"));
+ 						ingameGUI.getChatGUI().printChatMessage(
+ 								new ChatComponentText(pfx + " This server appears to be using version "
+ 										+ EnumChatFormatting.YELLOW + pluginVersion));
+ 						ingameGUI.getChatGUI().printChatMessage(
+ 								new ChatComponentText(pfx + " of the EaglerXBungee plugin which is outdated"));
+ 						ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(pfx));
+ 						ingameGUI.getChatGUI()
+ 								.printChatMessage(new ChatComponentText(pfx + " If you are the admin update to "
+ 										+ EnumChatFormatting.YELLOW + EaglerXBungeeVersion.getPluginVersion()
+ 										+ EnumChatFormatting.AQUA + " or newer"));
+ 						ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(pfx));
+ 						ingameGUI.getChatGUI().printChatMessage((new ChatComponentText(pfx + " Click: "))
+ 								.appendSibling((new ChatComponentText("" + EnumChatFormatting.GREEN
+ 										+ EnumChatFormatting.UNDERLINE + EaglerXBungeeVersion.getPluginButton()))
+ 												.setChatStyle((new ChatStyle()).setChatClickEvent(
+ 														new ClickEvent(ClickEvent.Action.EAGLER_PLUGIN_DOWNLOAD,
+ 																"plugin_download.zip")))));
+ 						ingameGUI.getChatGUI().printChatMessage(
+ 								new ChatComponentText(pfx + " ---------------------------------------"));
+ 					}
+ 				}
+ 			}
+ 		} else {
+ 			joinWorldTickCounter = 0;
+ 			if (currentScreen != null && currentScreen.shouldHangupIntegratedServer()) {
+ 				if (SingleplayerServerController.hangupEaglercraftServer()) {
+ 					this.displayGuiScreen(new GuiScreenIntegratedServerBusy(currentScreen,
+ 							"singleplayer.busy.stoppingIntegratedServer",
+ 							"singleplayer.failed.stoppingIntegratedServer", SingleplayerServerController::isReady));
+ 				}
+ 			}
+ 		}
+ 

> CHANGE  6 : 17  @  6 : 54

~ 		Minecraft.getMinecraft().getRenderManager().setEnableFNAWSkins(this.gameSettings.enableFNAWSkins);
~ 		session.reset();
~ 		SingleplayerServerController.launchEaglercraftServer(folderName, gameSettings.difficulty.getDifficultyId(),
~ 				Math.max(gameSettings.renderDistanceChunks, 2), worldSettingsIn);
~ 		this.displayGuiScreen(new GuiScreenIntegratedServerBusy(
~ 				new GuiScreenSingleplayerConnecting(new GuiMainMenu(), "Connecting to " + folderName),
~ 				"singleplayer.busy.startingIntegratedServer", "singleplayer.failed.startingIntegratedServer",
~ 				() -> SingleplayerServerController.isWorldReady(), (t, u) -> {
~ 					Minecraft.this.displayGuiScreen(GuiScreenIntegratedServerBusy.createException(new GuiMainMenu(),
~ 							((GuiScreenIntegratedServerBusy) t).failMessage, u));
~ 				}));

> INSERT  12 : 13  @  12

+ 			session.reset();

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

> CHANGE  130 : 131  @  130 : 131

~ 				return EagRuntime.getVersion();

> CHANGE  4 : 6  @  4 : 5

~ 				return EaglercraftGPU.glGetString(7937) + " GL version " + EaglercraftGPU.glGetString(7938) + ", "
~ 						+ EaglercraftGPU.glGetString(7936);

> DELETE  2  @  2 : 12

> CHANGE  2 : 3  @  2 : 6

~ 				return "Definitely Not; You're an eagler";

> DELETE  36  @  36 : 41

> CHANGE  12 : 13  @  12 : 13

~ 		return this.addScheduledTaskFuture(new Runnable() {

> INSERT  1 : 3  @  1

+ 				Minecraft.this.loadingScreen.eaglerShow(I18n.format("resourcePack.load.refreshing"),
+ 						I18n.format("resourcePack.load.pleaseWait"));

> DELETE  5  @  5 : 32

> CHANGE  1 : 2  @  1 : 6

~ 		return this.currentServerData != null ? "multiplayer" : "out_of_game";

> DELETE  2  @  2 : 219

> CHANGE  13 : 14  @  13 : 14

~ 		return SingleplayerServerController.isWorldRunning();

> CHANGE  3 : 4  @  3 : 4

~ 		return SingleplayerServerController.isWorldRunning();

> DELETE  2  @  2 : 6

> DELETE  1  @  1 : 6

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 6

> CHANGE  1 : 2  @  1 : 2

~ 		return System.currentTimeMillis();

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

> INSERT  7 : 35  @  7

+ 
+ 	public static int getGLMaximumTextureSize() {
+ 		return EaglercraftGPU.glGetInteger(GL_MAX_TEXTURE_SIZE);
+ 	}
+ 
+ 	public boolean areKeysLocked() {
+ 		return PlatformInput.lockKeys;
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
+ 			ret &= this.thePlayer.sendQueue.currentFNAWSkinAllowedState;
+ 		}
+ 		return ret;
+ 	}

> EOF
