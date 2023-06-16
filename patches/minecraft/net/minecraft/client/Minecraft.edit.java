
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 17

> DELETE  2  @  2 : 6

> DELETE  1  @  1 : 2

> CHANGE  2 : 3  @  2 : 6

~ import java.util.LinkedList;

> DELETE  1  @  1 : 4

> CHANGE  1 : 32  @  1 : 4

~ 
~ import net.lax1dude.eaglercraft.v1_8.internal.PlatformInput;
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

> DELETE  2  @  2 : 4

> DELETE  13  @  13 : 15

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

> DELETE  21  @  21 : 25

> DELETE  1  @  1 : 3

> DELETE  1  @  1 : 3

> DELETE  5  @  5 : 6

> INSERT  11 : 12  @  11

+ import net.minecraft.util.StringTranslate;

> DELETE  6  @  6 : 26

> CHANGE  1 : 2  @  1 : 2

~ public class Minecraft implements IThreadListener {

> CHANGE  2 : 3  @  2 : 9

~ 	public static final boolean isRunningOnMac = false;

> DELETE  12  @  12 : 14

> DELETE  19  @  19 : 20

> DELETE  6  @  6 : 8

> DELETE  1  @  1 : 3

> CHANGE  11 : 12  @  11 : 12

~ 	private EaglercraftNetworkManager myNetworkManager;

> DELETE  9  @  9 : 11

> CHANGE  4 : 5  @  4 : 7

~ 	private final List<FutureTask<?>> scheduledTasks = new LinkedList();

> INSERT  14 : 16  @  14

+ 	public int joinWorldTickCounter = 0;
+ 	private int dontPauseTimer = 0;

> CHANGE  3 : 4  @  3 : 6

~ 		StringTranslate.doCLINIT();

> CHANGE  1 : 2  @  1 : 9

~ 		this.mcDefaultResourcePack = new DefaultResourcePack();

> CHANGE  1 : 2  @  1 : 3

~ 		logger.info("Setting user: " + this.session.getProfile().getName());

> CHANGE  7 : 12  @  7 : 11

~ 		String serverToJoin = EagRuntime.getConfiguration().getServerToJoin();
~ 		if (serverToJoin != null) {
~ 			ServerAddress addr = AddressResolver.resolveAddressFromURI(serverToJoin);
~ 			this.serverName = addr.getIP();
~ 			this.serverPort = addr.getPort();

> DELETE  2  @  2 : 3

> CHANGE  15 : 17  @  15 : 17

~ 		try {
~ 			while (true) {

> DELETE  16  @  16 : 33

> CHANGE  1 : 16  @  1 : 3

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

> CHANGE  4 : 6  @  4 : 6

~ 	private void startGame() throws IOException {
~ 		this.gameSettings = new GameSettings(this);

> DELETE  1  @  1 : 2

> CHANGE  5 : 6  @  5 : 8

~ 		logger.info("EagRuntime Version: " + EagRuntime.getVersion());

> DELETE  1  @  1 : 4

> CHANGE  1 : 2  @  1 : 3

~ 		this.mcResourcePackRepository = new ResourcePackRepository(this.mcDefaultResourcePack, this.metadataSerializer_,

> DELETE  8  @  8 : 11

> CHANGE  3 : 5  @  3 : 5

~ 		this.fontRendererObj = new EaglerFontRenderer(this.gameSettings,
~ 				new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);

> CHANGE  5 : 6  @  5 : 6

~ 		this.standardGalacticFontRenderer = new EaglerFontRenderer(this.gameSettings,

> CHANGE  8 : 9  @  8 : 9

~ 					return HString.format(parString1, new Object[] { GameSettings

> CHANGE  10 : 11  @  10 : 11

~ 		GlStateManager.clearDepth(1.0f);

> INSERT  30 : 31  @  30

+ 		SkinPreviewRenderer.initialize();

> INSERT  2 : 6  @  2

+ 
+ 		ServerList.initServerList(this);
+ 		EaglerProfile.read();
+ 

> CHANGE  1 : 3  @  1 : 2

~ 			this.displayGuiScreen(new GuiScreenEditProfile(
~ 					new GuiConnecting(new GuiMainMenu(), this, this.serverName, this.serverPort)));

> CHANGE  1 : 2  @  1 : 2

~ 			this.displayGuiScreen(new GuiScreenEditProfile(new GuiMainMenu()));

> DELETE  5  @  5 : 17

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

> CHANGE  16 : 19  @  16 : 28

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

> CHANGE  31 : 32  @  31 : 32

~ 	public void checkGLError(String message) {

> CHANGE  1 : 2  @  1 : 2

~ 			int i = EaglercraftGPU.glGetError();

> CHANGE  1 : 2  @  1 : 2

~ 				String s = EaglercraftGPU.gluErrorString(i);

> DELETE  10  @  10 : 11

> CHANGE  10 : 11  @  10 : 11

~ 			EagRuntime.destroy();

> CHANGE  1 : 2  @  1 : 2

~ 				EagRuntime.exit();

> DELETE  3  @  3 : 5

> CHANGE  5 : 6  @  5 : 6

~ 		if (Display.isCloseRequested()) {

> CHANGE  14 : 15  @  14 : 15

~ 				Util.func_181617_a((FutureTask) this.scheduledTasks.remove(0), logger);

> DELETE  18  @  18 : 26

> CHANGE  1 : 11  @  1 : 5

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

> CHANGE  1 : 6  @  1 : 2

~ 			if (!this.skipRenderWorld) {
~ 				this.mcProfiler.endStartSection("gameRenderer");
~ 				this.entityRenderer.func_181560_a(this.timer.renderPartialTicks, i);
~ 				this.mcProfiler.endSection();
~ 			}

> CHANGE  1 : 13  @  1 : 5

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

> CHANGE  2 : 4  @  2 : 7

~ 			this.guiAchievement.updateAchievementWindow();
~ 			GlStateManager.popMatrix();

> DELETE  2  @  2 : 11

> DELETE  2  @  2 : 10

> INSERT  1 : 2  @  1

+ 

> CHANGE  1 : 2  @  1 : 3

~ 		this.isGamePaused = false;

> CHANGE  6 : 7  @  6 : 7

~ 			this.debug = HString.format("%d fps (%d chunk update%s) T: %s%s%s%s",

> CHANGE  5 : 7  @  5 : 9

~ 							this.gameSettings.fancyGraphics ? "" : " fast", this.gameSettings.clouds == 0 ? ""
~ 									: (this.gameSettings.clouds == 1 ? " fast-clouds" : " fancy-clouds") });

> DELETE  3  @  3 : 7

> DELETE  49  @  49 : 56

> CHANGE  48 : 49  @  48 : 49

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

> DELETE  11  @  11 : 12

> DELETE  2  @  2 : 10

> INSERT  9 : 11  @  9

+ 		RateLimitTracker.tick();
+ 

> INSERT  23 : 29  @  23

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

> CHANGE  40 : 42  @  40 : 41

~ 						if ((!this.inGameHasFocus || !Mouse.isActuallyGrabbed()) && Mouse.getEventButtonState()) {
~ 							this.inGameHasFocus = false;

> INSERT  16 : 19  @  16

+ 				if (k == 0x1D && (areKeysLocked() || isFullScreen())) {
+ 					KeyBinding.setKeyBindState(gameSettings.keyBindSprint.getKeyCode(), Keyboard.getEventKeyState());
+ 				}

> CHANGE  26 : 27  @  26 : 27

~ 						if (k == 1 || (k > -1 && k == this.gameSettings.keyBindClose.getKeyCode())) {

> INSERT  41 : 42  @  41

+ 							GlStateManager.recompileShaders();

> INSERT  206 : 212  @  206

+ 		if (this.theWorld != null) {
+ 			++joinWorldTickCounter;
+ 		} else {
+ 			joinWorldTickCounter = 0;
+ 		}
+ 

> CHANGE  5 : 6  @  5 : 54

~ 		throw new UnsupportedOperationException("singleplayer has been removed");

> INSERT  12 : 13  @  12

+ 			session.reset();

> DELETE  1  @  1 : 7

> DELETE  40  @  40 : 41

> CHANGE  20 : 21  @  20 : 22

~ 		this.thePlayer = this.playerController.func_178892_a(this.theWorld, new StatFileWriter());

> CHANGE  165 : 166  @  165 : 166

~ 				return EagRuntime.getVersion();

> CHANGE  4 : 6  @  4 : 5

~ 				return EaglercraftGPU.glGetString(7937) + " GL version " + EaglercraftGPU.glGetString(7938) + ", "
~ 						+ EaglercraftGPU.glGetString(7936);

> DELETE  2  @  2 : 12

> CHANGE  2 : 3  @  2 : 6

~ 				return "Definitely Not; You're an eagler";

> DELETE  36  @  36 : 41

> INSERT  14 : 16  @  14

+ 				Minecraft.this.loadingScreen.eaglerShow(I18n.format("resourcePack.load.refreshing"),
+ 						I18n.format("resourcePack.load.pleaseWait"));

> DELETE  5  @  5 : 32

> CHANGE  1 : 2  @  1 : 6

~ 		return this.currentServerData != null ? "multiplayer" : "out_of_game";

> DELETE  2  @  2 : 219

> CHANGE  17 : 18  @  17 : 18

~ 		return false;

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

> CHANGE  11 : 15  @  11 : 23

~ 		ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(callableToSchedule);
~ 		synchronized (this.scheduledTasks) {
~ 			this.scheduledTasks.add(listenablefuturetask);
~ 			return listenablefuturetask;

> DELETE  8  @  8 : 12

> DELETE  24  @  24 : 32

> INSERT  7 : 15  @  7

+ 
+ 	public static int getGLMaximumTextureSize() {
+ 		return EaglercraftGPU.glGetInteger(GL_MAX_TEXTURE_SIZE);
+ 	}
+ 
+ 	public boolean areKeysLocked() {
+ 		return PlatformInput.lockKeys;
+ 	}

> EOF
