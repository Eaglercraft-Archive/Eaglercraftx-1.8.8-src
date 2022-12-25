
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 17

> DELETE  4  @  19 : 23

> DELETE  5  @  24 : 25

> CHANGE  7 : 8  @  27 : 31

~ import java.util.LinkedList;

> DELETE  9  @  32 : 35

> CHANGE  10 : 41  @  36 : 39

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

> DELETE  43  @  41 : 43

> DELETE  56  @  56 : 58

> DELETE  59  @  61 : 62

> INSERT  62 : 63  @  65

+ import net.minecraft.client.multiplayer.ServerAddress;

> INSERT  64 : 65  @  66

+ import net.minecraft.client.multiplayer.ServerList;

> DELETE  66  @  67 : 68

> DELETE  70  @  72 : 73

> DELETE  71  @  74 : 75

> DELETE  73  @  77 : 78

> DELETE  88  @  93 : 94

> DELETE  90  @  96 : 97

> DELETE  104  @  111 : 112

> DELETE  105  @  113 : 115

> DELETE  126  @  136 : 140

> DELETE  127  @  141 : 143

> DELETE  128  @  144 : 146

> DELETE  133  @  151 : 152

> INSERT  144 : 145  @  163

+ import net.minecraft.util.StringTranslate;

> DELETE  151  @  169 : 189

> CHANGE  152 : 153  @  190 : 191

~ public class Minecraft implements IThreadListener {

> CHANGE  155 : 156  @  193 : 200

~ 	public static final boolean isRunningOnMac = false;

> DELETE  168  @  212 : 214

> DELETE  187  @  233 : 234

> DELETE  193  @  240 : 242

> DELETE  194  @  243 : 245

> CHANGE  205 : 206  @  256 : 257

~ 	private EaglercraftNetworkManager myNetworkManager;

> DELETE  215  @  266 : 268

> CHANGE  219 : 220  @  272 : 275

~ 	private final List<FutureTask<?>> scheduledTasks = new LinkedList();

> INSERT  234 : 236  @  289

+ 	public int joinWorldTickCounter = 0;
+ 	private int dontPauseTimer = 0;

> CHANGE  239 : 240  @  292 : 295

~ 		StringTranslate.doCLINIT();

> CHANGE  241 : 242  @  296 : 304

~ 		this.mcDefaultResourcePack = new DefaultResourcePack();

> CHANGE  243 : 244  @  305 : 307

~ 		logger.info("Setting user: " + this.session.getProfile().getName());

> CHANGE  251 : 256  @  314 : 318

~ 		String serverToJoin = EagRuntime.getConfiguration().getServerToJoin();
~ 		if (serverToJoin != null) {
~ 			ServerAddress addr = AddressResolver.resolveAddressFromURI(serverToJoin);
~ 			this.serverName = addr.getIP();
~ 			this.serverPort = addr.getPort();

> DELETE  258  @  320 : 321

> CHANGE  273 : 275  @  336 : 338

~ 		try {
~ 			while (true) {

> DELETE  291  @  354 : 371

> CHANGE  292 : 307  @  372 : 374

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

> CHANGE  311 : 313  @  378 : 380

~ 	private void startGame() throws IOException {
~ 		this.gameSettings = new GameSettings(this);

> DELETE  314  @  381 : 382

> CHANGE  319 : 320  @  387 : 390

~ 		logger.info("EagRuntime Version: " + EagRuntime.getVersion());

> DELETE  321  @  391 : 394

> CHANGE  322 : 323  @  395 : 397

~ 		this.mcResourcePackRepository = new ResourcePackRepository(this.mcDefaultResourcePack, this.metadataSerializer_,

> DELETE  331  @  405 : 408

> CHANGE  334 : 336  @  411 : 413

~ 		this.fontRendererObj = new EaglerFontRenderer(this.gameSettings,
~ 				new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);

> CHANGE  341 : 342  @  418 : 419

~ 		this.standardGalacticFontRenderer = new EaglerFontRenderer(this.gameSettings,

> CHANGE  350 : 351  @  427 : 428

~ 					return HString.format(parString1, new Object[] { GameSettings

> CHANGE  361 : 362  @  438 : 439

~ 		GlStateManager.clearDepth(1.0f);

> INSERT  392 : 393  @  469

+ 		SkinPreviewRenderer.initialize();

> INSERT  395 : 399  @  471

+ 
+ 		ServerList.initServerList(this);
+ 		EaglerProfile.read();
+ 

> CHANGE  400 : 402  @  472 : 473

~ 			this.displayGuiScreen(new GuiScreenEditProfile(
~ 					new GuiConnecting(new GuiMainMenu(), this, this.serverName, this.serverPort)));

> CHANGE  403 : 404  @  474 : 475

~ 			this.displayGuiScreen(new GuiScreenEditProfile(new GuiMainMenu()));

> DELETE  409  @  480 : 492

> CHANGE  425 : 426  @  508 : 516

~ 		throw new UnsupportedOperationException("wtf u trying to twitch stream in a browser game?");

> CHANGE  428 : 431  @  518 : 540

~ 	private void createDisplay() {
~ 		Display.create();
~ 		Display.setTitle("Eaglercraft 1.8.8");

> DELETE  433  @  542 : 579

> CHANGE  434 : 435  @  580 : 590

~ 		return true;

> DELETE  437  @  592 : 596

> DELETE  441  @  600 : 617

> CHANGE  447 : 459  @  623 : 636

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

> DELETE  460  @  637 : 638

> INSERT  467 : 469  @  645

+ 		GlStateManager.recompileShaders();
+ 

> CHANGE  483 : 485  @  659 : 660

~ 			logger.info("Caught error stitching, removing all assigned resourcepacks");
~ 			logger.info(runtimeexception);

> CHANGE  501 : 504  @  676 : 688

~ 	private void updateDisplayMode() {
~ 		this.displayWidth = Display.getWidth();
~ 		this.displayHeight = Display.getHeight();

> CHANGE  506 : 510  @  690 : 734

~ 	private void drawSplashScreen(TextureManager textureManagerInstance) {
~ 		Display.update();
~ 		updateDisplayMode();
~ 		GlStateManager.viewport(0, 0, displayWidth, displayHeight);

> DELETE  512  @  736 : 739

> CHANGE  528 : 529  @  755 : 756

~ 					new DynamicTexture(ImageData.loadImageFile(inputstream)));

> DELETE  553  @  780 : 782

> DELETE  579  @  808 : 812

> CHANGE  610 : 611  @  843 : 844

~ 	public void checkGLError(String message) {

> CHANGE  612 : 613  @  845 : 846

~ 			int i = EaglercraftGPU.glGetError();

> CHANGE  614 : 615  @  847 : 848

~ 				String s = EaglercraftGPU.gluErrorString(i);

> DELETE  625  @  858 : 859

> CHANGE  635 : 636  @  869 : 870

~ 			EagRuntime.destroy();

> CHANGE  637 : 638  @  871 : 872

~ 				EagRuntime.exit();

> DELETE  641  @  875 : 877

> CHANGE  646 : 647  @  882 : 883

~ 		if (Display.isCloseRequested()) {

> CHANGE  661 : 662  @  897 : 898

~ 				Util.func_181617_a((FutureTask) this.scheduledTasks.remove(0), logger);

> DELETE  680  @  916 : 924

> CHANGE  681 : 691  @  925 : 929

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

> CHANGE  692 : 697  @  930 : 931

~ 			if (!this.skipRenderWorld) {
~ 				this.mcProfiler.endStartSection("gameRenderer");
~ 				this.entityRenderer.func_181560_a(this.timer.renderPartialTicks, i);
~ 				this.mcProfiler.endSection();
~ 			}

> CHANGE  698 : 710  @  932 : 936

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

> CHANGE  712 : 714  @  938 : 943

~ 			this.guiAchievement.updateAchievementWindow();
~ 			GlStateManager.popMatrix();

> DELETE  716  @  945 : 954

> DELETE  718  @  956 : 964

> INSERT  719 : 720  @  965

+ 

> CHANGE  721 : 722  @  966 : 968

~ 		this.isGamePaused = false;

> CHANGE  728 : 729  @  974 : 975

~ 			this.debug = HString.format("%d fps (%d chunk update%s) T: %s%s%s%s",

> CHANGE  734 : 736  @  980 : 984

~ 							this.gameSettings.fancyGraphics ? "" : " fast", this.gameSettings.clouds == 0 ? ""
~ 									: (this.gameSettings.clouds == 1 ? " fast-clouds" : " fancy-clouds") });

> DELETE  739  @  987 : 991

> DELETE  788  @  1040 : 1047

> CHANGE  836 : 837  @  1095 : 1096

~ 			EaglercraftGPU.glLineWidth(1.0F);

> CHANGE  846 : 847  @  1105 : 1106

~ 					(double) ((float) j - (float) short1 * 0.6F - 16.0F), 0.0D).color(0, 0, 0, 100).endVertex();

> CHANGE  848 : 849  @  1107 : 1108

~ 					.color(0, 0, 0, 100).endVertex();

> CHANGE  850 : 851  @  1109 : 1110

~ 					.color(0, 0, 0, 100).endVertex();

> CHANGE  852 : 853  @  1111 : 1112

~ 					(double) ((float) j - (float) short1 * 0.6F - 16.0F), 0.0D).color(0, 0, 0, 100).endVertex();

> DELETE  963  @  1222 : 1226

> CHANGE  1071 : 1072  @  1334 : 1374

~ 		logger.error("Use F11 to toggle fullscreen!");

> DELETE  1083  @  1385 : 1386

> DELETE  1085  @  1388 : 1396

> INSERT  1094 : 1096  @  1405

+ 		RateLimitTracker.tick();
+ 

> INSERT  1119 : 1125  @  1428

+ 			if (this.currentScreen == null && this.dontPauseTimer <= 0) {
+ 				if (!Mouse.isMouseGrabbed()) {
+ 					this.setIngameNotInFocus();
+ 					this.displayInGameMenu();
+ 				}
+ 			}

> INSERT  1132 : 1137  @  1435

+ 			this.dontPauseTimer = 6;
+ 		} else {
+ 			if (this.dontPauseTimer > 0) {
+ 				--this.dontPauseTimer;
+ 			}

> CHANGE  1147 : 1148  @  1445 : 1446

~ 						return Minecraft.this.currentScreen.getClass().getName();

> CHANGE  1161 : 1162  @  1459 : 1460

~ 							return Minecraft.this.currentScreen.getClass().getName();

> CHANGE  1202 : 1204  @  1500 : 1501

~ 						if ((!this.inGameHasFocus || !Mouse.isActuallyGrabbed()) && Mouse.getEventButtonState()) {
~ 							this.inGameHasFocus = false;

> CHANGE  1246 : 1247  @  1543 : 1544

~ 						if (k == 1 || (k > -1 && k == this.gameSettings.keyBindClose.getKeyCode())) {

> INSERT  1288 : 1289  @  1585

+ 							GlStateManager.recompileShaders();

> INSERT  1495 : 1501  @  1791

+ 		if (this.theWorld != null) {
+ 			++joinWorldTickCounter;
+ 		} else {
+ 			joinWorldTickCounter = 0;
+ 		}
+ 

> CHANGE  1506 : 1507  @  1796 : 1845

~ 		throw new UnsupportedOperationException("singleplayer has been removed");

> INSERT  1519 : 1520  @  1857

+ 			session.reset();

> DELETE  1521  @  1858 : 1864

> DELETE  1561  @  1904 : 1905

> CHANGE  1581 : 1582  @  1925 : 1927

~ 		this.thePlayer = this.playerController.func_178892_a(this.theWorld, new StatFileWriter());

> CHANGE  1747 : 1748  @  2092 : 2093

~ 				return EagRuntime.getVersion();

> CHANGE  1752 : 1754  @  2097 : 2098

~ 				return EaglercraftGPU.glGetString(7937) + " GL version " + EaglercraftGPU.glGetString(7938) + ", "
~ 						+ EaglercraftGPU.glGetString(7936);

> DELETE  1756  @  2100 : 2110

> CHANGE  1758 : 1759  @  2112 : 2116

~ 				return "Definitely Not; You're an eagler";

> DELETE  1795  @  2152 : 2157

> INSERT  1809 : 1811  @  2171

+ 				Minecraft.this.loadingScreen.eaglerShow(I18n.format("resourcePack.load.refreshing"),
+ 						I18n.format("resourcePack.load.pleaseWait"));

> DELETE  1816  @  2176 : 2203

> CHANGE  1817 : 1818  @  2204 : 2209

~ 		return this.currentServerData != null ? "multiplayer" : "out_of_game";

> DELETE  1820  @  2211 : 2428

> CHANGE  1837 : 1838  @  2445 : 2446

~ 		return false;

> DELETE  1840  @  2448 : 2452

> DELETE  1841  @  2453 : 2458

> DELETE  1842  @  2459 : 2460

> DELETE  1844  @  2462 : 2466

> CHANGE  1845 : 1846  @  2467 : 2468

~ 		return System.currentTimeMillis();

> DELETE  1856  @  2478 : 2495

> DELETE  1900  @  2539 : 2543

> CHANGE  1906 : 1908  @  2549 : 2589

~ 					if (i == this.gameSettings.keyBindScreenshot.getKeyCode()) {
~ 						this.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot());

> DELETE  1909  @  2590 : 2592

> DELETE  1910  @  2593 : 2594

> DELETE  1914  @  2598 : 2606

> CHANGE  1925 : 1929  @  2617 : 2629

~ 		ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(callableToSchedule);
~ 		synchronized (this.scheduledTasks) {
~ 			this.scheduledTasks.add(listenablefuturetask);
~ 			return listenablefuturetask;

> DELETE  1937  @  2637 : 2641

> DELETE  1961  @  2665 : 2673

> INSERT  1968 : 1972  @  2680

+ 
+ 	public static int getGLMaximumTextureSize() {
+ 		return EaglercraftGPU.glGetInteger(GL_MAX_TEXTURE_SIZE);
+ 	}

> EOF
