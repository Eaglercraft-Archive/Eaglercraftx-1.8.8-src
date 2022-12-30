
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 17

> DELETE  2  @  17 : 21

> DELETE  1  @  5 : 6

> CHANGE  2 : 3  @  3 : 7

~ import java.util.LinkedList;

> DELETE  2  @  5 : 8

> CHANGE  1 : 32  @  4 : 7

~ 
~ import org.apache.commons.lang3.Validate;
~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.Display;
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.IOUtils;
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ import net.lax1dude.eaglercraft.v1_8.futures.Executors;
~ import net.lax1dude.eaglercraft.v1_8.futures.FutureTask;
~ import net.lax1dude.eaglercraft.v1_8.futures.Futures;
~ import net.lax1dude.eaglercraft.v1_8.futures.ListenableFuture;
~ import net.lax1dude.eaglercraft.v1_8.futures.ListenableFutureTask;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
~ import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFontRenderer;
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ImageData;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
~ import net.lax1dude.eaglercraft.v1_8.profile.EaglerProfile;
~ import net.lax1dude.eaglercraft.v1_8.profile.GuiScreenEditProfile;
~ import net.lax1dude.eaglercraft.v1_8.profile.SkinPreviewRenderer;
~ import net.lax1dude.eaglercraft.v1_8.socket.AddressResolver;
~ import net.lax1dude.eaglercraft.v1_8.socket.EaglercraftNetworkManager;
~ import net.lax1dude.eaglercraft.v1_8.socket.RateLimitTracker;

> DELETE  33  @  5 : 7

> DELETE  13  @  15 : 17

> DELETE  3  @  5 : 6

> INSERT  3 : 4  @  4

+ import net.minecraft.client.multiplayer.ServerAddress;

> INSERT  2 : 3  @  1

+ import net.minecraft.client.multiplayer.ServerList;

> DELETE  2  @  1 : 2

> DELETE  4  @  5 : 6

> DELETE  1  @  2 : 3

> DELETE  2  @  3 : 4

> DELETE  15  @  16 : 17

> DELETE  2  @  3 : 4

> DELETE  14  @  15 : 16

> DELETE  1  @  2 : 4

> DELETE  21  @  23 : 27

> DELETE  1  @  5 : 7

> DELETE  1  @  3 : 5

> DELETE  5  @  7 : 8

> INSERT  11 : 12  @  12

+ import net.minecraft.util.StringTranslate;

> DELETE  7  @  6 : 26

> CHANGE  1 : 2  @  21 : 22

~ public class Minecraft implements IThreadListener {

> CHANGE  3 : 4  @  3 : 10

~ 	public static final boolean isRunningOnMac = false;

> DELETE  13  @  19 : 21

> DELETE  19  @  21 : 22

> DELETE  6  @  7 : 9

> DELETE  1  @  3 : 5

> CHANGE  11 : 12  @  13 : 14

~ 	private EaglercraftNetworkManager myNetworkManager;

> DELETE  10  @  10 : 12

> CHANGE  4 : 5  @  6 : 9

~ 	private final List<FutureTask<?>> scheduledTasks = new LinkedList();

> INSERT  15 : 17  @  17

+ 	public int joinWorldTickCounter = 0;
+ 	private int dontPauseTimer = 0;

> CHANGE  5 : 6  @  3 : 6

~ 		StringTranslate.doCLINIT();

> CHANGE  2 : 3  @  4 : 12

~ 		this.mcDefaultResourcePack = new DefaultResourcePack();

> CHANGE  2 : 3  @  9 : 11

~ 		logger.info("Setting user: " + this.session.getProfile().getName());

> CHANGE  8 : 13  @  9 : 13

~ 		String serverToJoin = EagRuntime.getConfiguration().getServerToJoin();
~ 		if (serverToJoin != null) {
~ 			ServerAddress addr = AddressResolver.resolveAddressFromURI(serverToJoin);
~ 			this.serverName = addr.getIP();
~ 			this.serverPort = addr.getPort();

> DELETE  7  @  6 : 7

> CHANGE  15 : 17  @  16 : 18

~ 		try {
~ 			while (true) {

> DELETE  18  @  18 : 35

> CHANGE  1 : 16  @  18 : 20

~ 		} catch (MinecraftError var12) {
~ 			// ??
~ 		} catch (ReportedException reportedexception) {
~ 			this.addGraphicsAndWorldToCrashReport(reportedexception.getCrashReport());
~ 			this.freeMemory();
~ 			logger.fatal("Reported exception thrown!", reportedexception);
~ 			this.displayCrashReport(reportedexception.getCrashReport());
~ 		} catch (Throwable throwable1) {
~ 			CrashReport crashreport1 = this
~ 					.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", throwable1));
~ 			this.freeMemory();
~ 			logger.fatal("Unreported exception thrown!", throwable1);
~ 			this.displayCrashReport(crashreport1);
~ 		} finally {
~ 			this.shutdownMinecraftApplet();

> CHANGE  19 : 21  @  6 : 8

~ 	private void startGame() throws IOException {
~ 		this.gameSettings = new GameSettings(this);

> DELETE  3  @  3 : 4

> CHANGE  5 : 6  @  6 : 9

~ 		logger.info("EagRuntime Version: " + EagRuntime.getVersion());

> DELETE  2  @  4 : 7

> CHANGE  1 : 2  @  4 : 6

~ 		this.mcResourcePackRepository = new ResourcePackRepository(this.mcDefaultResourcePack, this.metadataSerializer_,

> DELETE  9  @  10 : 13

> CHANGE  3 : 5  @  6 : 8

~ 		this.fontRendererObj = new EaglerFontRenderer(this.gameSettings,
~ 				new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);

> CHANGE  7 : 8  @  7 : 8

~ 		this.standardGalacticFontRenderer = new EaglerFontRenderer(this.gameSettings,

> CHANGE  9 : 10  @  9 : 10

~ 					return HString.format(parString1, new Object[] { GameSettings

> CHANGE  11 : 12  @  11 : 12

~ 		GlStateManager.clearDepth(1.0f);

> INSERT  31 : 32  @  31

+ 		SkinPreviewRenderer.initialize();

> INSERT  3 : 7  @  2

+ 
+ 		ServerList.initServerList(this);
+ 		EaglerProfile.read();
+ 

> CHANGE  5 : 7  @  1 : 2

~ 			this.displayGuiScreen(new GuiScreenEditProfile(
~ 					new GuiConnecting(new GuiMainMenu(), this, this.serverName, this.serverPort)));

> CHANGE  3 : 4  @  2 : 3

~ 			this.displayGuiScreen(new GuiScreenEditProfile(new GuiMainMenu()));

> DELETE  6  @  6 : 18

> CHANGE  16 : 17  @  28 : 36

~ 		throw new UnsupportedOperationException("wtf u trying to twitch stream in a browser game?");

> CHANGE  3 : 6  @  10 : 32

~ 	private void createDisplay() {
~ 		Display.create();
~ 		Display.setTitle("Eaglercraft 1.8.8");

> DELETE  5  @  24 : 61

> CHANGE  1 : 2  @  38 : 48

~ 		return true;

> DELETE  3  @  12 : 16

> DELETE  4  @  8 : 25

> CHANGE  6 : 18  @  23 : 36

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

> DELETE  13  @  14 : 15

> INSERT  7 : 9  @  8

+ 		GlStateManager.recompileShaders();
+ 

> CHANGE  16 : 18  @  14 : 15

~ 			logger.info("Caught error stitching, removing all assigned resourcepacks");
~ 			logger.info(runtimeexception);

> CHANGE  18 : 21  @  17 : 29

~ 	private void updateDisplayMode() {
~ 		this.displayWidth = Display.getWidth();
~ 		this.displayHeight = Display.getHeight();

> CHANGE  5 : 9  @  14 : 58

~ 	private void drawSplashScreen(TextureManager textureManagerInstance) {
~ 		Display.update();
~ 		updateDisplayMode();
~ 		GlStateManager.viewport(0, 0, displayWidth, displayHeight);

> DELETE  6  @  46 : 49

> CHANGE  16 : 17  @  19 : 20

~ 					new DynamicTexture(ImageData.loadImageFile(inputstream)));

> DELETE  25  @  25 : 27

> DELETE  26  @  28 : 32

> CHANGE  31 : 32  @  35 : 36

~ 	public void checkGLError(String message) {

> CHANGE  2 : 3  @  2 : 3

~ 			int i = EaglercraftGPU.glGetError();

> CHANGE  2 : 3  @  2 : 3

~ 				String s = EaglercraftGPU.gluErrorString(i);

> DELETE  11  @  11 : 12

> CHANGE  10 : 11  @  11 : 12

~ 			EagRuntime.destroy();

> CHANGE  2 : 3  @  2 : 3

~ 				EagRuntime.exit();

> DELETE  4  @  4 : 6

> CHANGE  5 : 6  @  7 : 8

~ 		if (Display.isCloseRequested()) {

> CHANGE  15 : 16  @  15 : 16

~ 				Util.func_181617_a((FutureTask) this.scheduledTasks.remove(0), logger);

> DELETE  19  @  19 : 27

> CHANGE  1 : 11  @  9 : 13

~ 		if (!Display.contextLost()) {
~ 			GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 1.0f);
~ 			GlStateManager.pushMatrix();
~ 			GlStateManager.clear(16640);
~ 			this.mcProfiler.startSection("display");
~ 			GlStateManager.enableTexture2D();
~ 			if (this.thePlayer != null && this.thePlayer.isEntityInsideOpaqueBlock()) {
~ 				this.gameSettings.thirdPersonView = 0;
~ 			}
~ 

> CHANGE  11 : 16  @  5 : 6

~ 			if (!this.skipRenderWorld) {
~ 				this.mcProfiler.endStartSection("gameRenderer");
~ 				this.entityRenderer.func_181560_a(this.timer.renderPartialTicks, i);
~ 				this.mcProfiler.endSection();
~ 			}

> CHANGE  6 : 18  @  2 : 6

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

> CHANGE  14 : 16  @  6 : 11

~ 			this.guiAchievement.updateAchievementWindow();
~ 			GlStateManager.popMatrix();

> DELETE  4  @  7 : 16

> DELETE  2  @  11 : 19

> INSERT  1 : 2  @  9

+ 

> CHANGE  2 : 3  @  1 : 3

~ 		this.isGamePaused = false;

> CHANGE  7 : 8  @  8 : 9

~ 			this.debug = HString.format("%d fps (%d chunk update%s) T: %s%s%s%s",

> CHANGE  6 : 8  @  6 : 10

~ 							this.gameSettings.fancyGraphics ? "" : " fast", this.gameSettings.clouds == 0 ? ""
~ 									: (this.gameSettings.clouds == 1 ? " fast-clouds" : " fancy-clouds") });

> DELETE  5  @  7 : 11

> DELETE  49  @  53 : 60

> CHANGE  48 : 49  @  55 : 56

~ 			EaglercraftGPU.glLineWidth(1.0F);

> CHANGE  10 : 11  @  10 : 11

~ 					(double) ((float) j - (float) short1 * 0.6F - 16.0F), 0.0D).color(0, 0, 0, 100).endVertex();

> CHANGE  2 : 3  @  2 : 3

~ 					.color(0, 0, 0, 100).endVertex();

> CHANGE  2 : 3  @  2 : 3

~ 					.color(0, 0, 0, 100).endVertex();

> CHANGE  2 : 3  @  2 : 3

~ 					(double) ((float) j - (float) short1 * 0.6F - 16.0F), 0.0D).color(0, 0, 0, 100).endVertex();

> DELETE  111  @  111 : 115

> CHANGE  108 : 109  @  112 : 152

~ 		logger.error("Use F11 to toggle fullscreen!");

> DELETE  12  @  51 : 52

> DELETE  2  @  3 : 11

> INSERT  9 : 11  @  17

+ 		RateLimitTracker.tick();
+ 

> INSERT  25 : 31  @  23

+ 			if (this.currentScreen == null && this.dontPauseTimer <= 0) {
+ 				if (!Mouse.isMouseGrabbed()) {
+ 					this.setIngameNotInFocus();
+ 					this.displayInGameMenu();
+ 				}
+ 			}

> INSERT  13 : 18  @  7

+ 			this.dontPauseTimer = 6;
+ 		} else {
+ 			if (this.dontPauseTimer > 0) {
+ 				--this.dontPauseTimer;
+ 			}

> CHANGE  15 : 16  @  10 : 11

~ 						return Minecraft.this.currentScreen.getClass().getName();

> CHANGE  14 : 15  @  14 : 15

~ 							return Minecraft.this.currentScreen.getClass().getName();

> CHANGE  41 : 43  @  41 : 42

~ 						if ((!this.inGameHasFocus || !Mouse.isActuallyGrabbed()) && Mouse.getEventButtonState()) {
~ 							this.inGameHasFocus = false;

> CHANGE  44 : 45  @  43 : 44

~ 						if (k == 1 || (k > -1 && k == this.gameSettings.keyBindClose.getKeyCode())) {

> INSERT  42 : 43  @  42

+ 							GlStateManager.recompileShaders();

> INSERT  207 : 213  @  206

+ 		if (this.theWorld != null) {
+ 			++joinWorldTickCounter;
+ 		} else {
+ 			joinWorldTickCounter = 0;
+ 		}
+ 

> CHANGE  11 : 12  @  5 : 54

~ 		throw new UnsupportedOperationException("singleplayer has been removed");

> INSERT  13 : 14  @  61

+ 			session.reset();

> DELETE  2  @  1 : 7

> DELETE  40  @  46 : 47

> CHANGE  20 : 21  @  21 : 23

~ 		this.thePlayer = this.playerController.func_178892_a(this.theWorld, new StatFileWriter());

> CHANGE  166 : 167  @  167 : 168

~ 				return EagRuntime.getVersion();

> CHANGE  5 : 7  @  5 : 6

~ 				return EaglercraftGPU.glGetString(7937) + " GL version " + EaglercraftGPU.glGetString(7938) + ", "
~ 						+ EaglercraftGPU.glGetString(7936);

> DELETE  4  @  3 : 13

> CHANGE  2 : 3  @  12 : 16

~ 				return "Definitely Not; You're an eagler";

> DELETE  37  @  40 : 45

> INSERT  14 : 16  @  19

+ 				Minecraft.this.loadingScreen.eaglerShow(I18n.format("resourcePack.load.refreshing"),
+ 						I18n.format("resourcePack.load.pleaseWait"));

> DELETE  7  @  5 : 32

> CHANGE  1 : 2  @  28 : 33

~ 		return this.currentServerData != null ? "multiplayer" : "out_of_game";

> DELETE  3  @  7 : 224

> CHANGE  17 : 18  @  234 : 235

~ 		return false;

> DELETE  3  @  3 : 7

> DELETE  1  @  5 : 10

> DELETE  1  @  6 : 7

> DELETE  2  @  3 : 7

> CHANGE  1 : 2  @  5 : 6

~ 		return System.currentTimeMillis();

> DELETE  11  @  11 : 28

> DELETE  44  @  61 : 65

> CHANGE  6 : 8  @  10 : 50

~ 					if (i == this.gameSettings.keyBindScreenshot.getKeyCode()) {
~ 						this.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot());

> DELETE  3  @  41 : 43

> DELETE  1  @  3 : 4

> DELETE  4  @  5 : 13

> CHANGE  11 : 15  @  19 : 31

~ 		ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(callableToSchedule);
~ 		synchronized (this.scheduledTasks) {
~ 			this.scheduledTasks.add(listenablefuturetask);
~ 			return listenablefuturetask;

> DELETE  12  @  20 : 24

> DELETE  24  @  28 : 36

> INSERT  7 : 11  @  15

+ 
+ 	public static int getGLMaximumTextureSize() {
+ 		return EaglercraftGPU.glGetInteger(GL_MAX_TEXTURE_SIZE);
+ 	}

> EOF
