
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 9  @  3 : 9

~ 
~ import net.lax1dude.eaglercraft.v1_8.Display;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.GuiScreenVideoSettingsWarning;
~ import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.dynamiclights.DynamicLightsStateManager;
~ import net.lax1dude.eaglercraft.v1_8.recording.ScreenRecordingController;

> INSERT  8 : 12  @  8

+ 	private boolean vsyncLock = false;
+ 	/**
+ 	 * + An array of all of GameSettings.Options's video options.
+ 	 */

> CHANGE  2 : 10  @  2 : 7

~ 			GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.EAGLER_VSYNC, GameSettings.Options.ANAGLYPH,
~ 			GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.GAMMA,
~ 			GameSettings.Options.RENDER_CLOUDS, GameSettings.Options.PARTICLES, GameSettings.Options.FXAA,
~ 			GameSettings.Options.MIPMAP_LEVELS, GameSettings.Options.BLOCK_ALTERNATIVES,
~ 			GameSettings.Options.ENTITY_SHADOWS, GameSettings.Options.FOG, GameSettings.Options.EAGLER_DYNAMIC_LIGHTS,
~ 			GameSettings.Options.FULLSCREEN, GameSettings.Options.FNAW_SKINS, GameSettings.Options.HUD_FPS,
~ 			GameSettings.Options.HUD_COORDS, GameSettings.Options.HUD_PLAYER, GameSettings.Options.HUD_STATS,
~ 			GameSettings.Options.HUD_WORLD, GameSettings.Options.HUD_24H, GameSettings.Options.CHUNK_FIX };

> CHANGE  11 : 18  @  11 : 22

~ 		this.optionsRowList = new GuiOptionsRowList(this.mc, this.width, this.height, 32, this.height - 32, 25,
~ 				videoOptions);
~ 		if (!DynamicLightsStateManager.isSupported()) {
~ 			GuiOptionButton btn = ((GuiOptionsRowList) optionsRowList)
~ 					.getButtonFor(GameSettings.Options.EAGLER_DYNAMIC_LIGHTS);
~ 			if (btn != null) {
~ 				btn.enabled = false;

> DELETE  1  @  1 : 7

> CHANGE  1 : 17  @  1 : 2

~ 		if (EaglercraftGPU.checkOpenGLESVersion() < 300) {
~ 			GuiOptionSlider btn = ((GuiOptionsRowList) optionsRowList).getSliderFor(GameSettings.Options.MIPMAP_LEVELS);
~ 			if (btn != null) {
~ 				btn.displayString = I18n.format(GameSettings.Options.MIPMAP_LEVELS.getEnumString()) + ": N/A";
~ 				btn.sliderValue = 0.0f;
~ 				btn.enabled = false;
~ 			}
~ 		}
~ 		if (!Display.supportsFullscreen()) {
~ 			GuiOptionButton btn = ((GuiOptionsRowList) optionsRowList).getButtonFor(GameSettings.Options.FULLSCREEN);
~ 			if (btn != null) {
~ 				btn.displayString = I18n.format(GameSettings.Options.FULLSCREEN.getEnumString()) + ": "
~ 						+ I18n.format("options.off");
~ 				btn.enabled = false;
~ 			}
~ 		}

> CHANGE  7 : 13  @  7 : 8

~ 	public void handleTouchInput() throws IOException {
~ 		super.handleTouchInput();
~ 		this.optionsRowList.handleTouchInput();
~ 	}
~ 
~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  3 : 9  @  3 : 4

~ 				GuiScreen contScreen = parentGuiScreen;
~ 				int vidIssues = mc.gameSettings.checkBadVideoSettings();
~ 				if (vidIssues != 0) {
~ 					contScreen = new GuiScreenVideoSettingsWarning(contScreen, vidIssues);
~ 				}
~ 				this.mc.displayGuiScreen(contScreen);

> DELETE  1  @  1 : 2

> CHANGE  3 : 4  @  3 : 4

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  4 : 5  @  4 : 5

~ 			ScaledResolution scaledresolution = mc.scaledResolution = new ScaledResolution(mc);

> INSERT  3 : 5  @  3

+ 			this.mc.voiceOverlay.setResolution(j, k);
+ 			this.mc.notifRenderer.setResolution(this.mc, j, k, scaledresolution.getScaleFactor());

> CHANGE  9 : 10  @  9 : 10

~ 			ScaledResolution scaledresolution = mc.scaledResolution = new ScaledResolution(mc);

> INSERT  13 : 26  @  13

+ 
+ 	@Override
+ 	public void updateScreen() {
+ 		boolean vsyncLockEn = ScreenRecordingController.isVSyncLocked();
+ 		if (vsyncLockEn != vsyncLock) {
+ 			vsyncLock = vsyncLockEn;
+ 			GuiOptionButton btn = ((GuiOptionsRowList) optionsRowList).getButtonFor(GameSettings.Options.EAGLER_VSYNC);
+ 			if (btn != null) {
+ 				btn.enabled = !vsyncLockEn;
+ 			}
+ 		}
+ 	}
+ 

> EOF
