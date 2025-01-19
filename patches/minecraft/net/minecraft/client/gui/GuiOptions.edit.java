
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 21  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ import net.lax1dude.eaglercraft.v1_8.boot_menu.GuiScreenEnterBootMenu;
~ import net.lax1dude.eaglercraft.v1_8.cookie.GuiScreenRevokeSessionToken;
~ import net.lax1dude.eaglercraft.v1_8.cookie.ServerCookieDataStore;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumPlatformType;
~ import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerFolderResourcePack;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.GuiScreenGenericErrorMessage;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.EaglerDeferredPipeline;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.gui.GuiShaderConfig;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.gui.GuiShadersNotSupported;
~ import net.lax1dude.eaglercraft.v1_8.profile.GuiScreenImportExportProfile;
~ import net.lax1dude.eaglercraft.v1_8.recording.GuiScreenRecordingNote;
~ import net.lax1dude.eaglercraft.v1_8.recording.GuiScreenRecordingSettings;
~ import net.lax1dude.eaglercraft.v1_8.recording.ScreenRecordingController;
~ import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;

> DELETE  1  @  1 : 21

> DELETE  2  @  2 : 3

> INSERT  2 : 4  @  2

+ import net.minecraft.util.EnumChatFormatting;
+ import net.minecraft.util.ResourceLocation;

> INSERT  10 : 11  @  10

+ 	private GuiButton broadcastSettings;

> CHANGE  10 : 12  @  10 : 11

~ 		for (int j = 0; j < field_146440_f.length; ++j) {
~ 			GameSettings.Options gamesettings$options = field_146440_f[j];

> CHANGE  36 : 37  @  36 : 48

~ 				I18n.format("shaders.gui.optionsButton")));

> CHANGE  2 : 6  @  2 : 4

~ 		boolean support = ScreenRecordingController.isSupported();
~ 		this.buttonList.add(broadcastSettings = new GuiButton(107, this.width / 2 + 5, this.height / 6 + 72 - 6, 150,
~ 				20, I18n.format(support ? "options.screenRecording.button" : "options.screenRecording.unsupported")));
~ 		broadcastSettings.enabled = support;

> CHANGE  8 : 10  @  8 : 9

~ 		GuiButton btn;
~ 		this.buttonList.add(btn = new GuiButton(105, this.width / 2 - 155, this.height / 6 + 144 - 6, 150, 20,

> CHANGE  1 : 5  @  1 : 3

~ 		btn.enabled = EaglerFolderResourcePack.isSupported();
~ 		this.buttonList.add(btn = new GuiButton(104, this.width / 2 + 5, this.height / 6 + 144 - 6, 150, 20,
~ 				I18n.format("options.debugConsoleButton", new Object[0])));
~ 		btn.enabled = EagRuntime.getPlatformType() != EnumPlatformType.DESKTOP;

> CHANGE  10 : 11  @  10 : 11

~ 		return chatcomponenttext.getUnformattedText();

> INSERT  6 : 7  @  6

+ 			SingleplayerServerController.setDifficulty(-1);

> CHANGE  7 : 8  @  7 : 8

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  11 : 13  @  11

+ 				SingleplayerServerController
+ 						.setDifficulty(this.mc.theWorld.getWorldInfo().getDifficulty().getDifficultyId());

> CHANGE  21 : 27  @  21 : 22

~ 				if (EaglerDeferredPipeline.isSupported()) {
~ 					this.mc.displayGuiScreen(new GuiShaderConfig(this));
~ 				} else {
~ 					this.mc.displayGuiScreen(new GuiShadersNotSupported(this,
~ 							I18n.format(EaglerDeferredPipeline.getReasonUnsupported())));
~ 				}

> DELETE  22  @  22 : 27

> CHANGE  16 : 22  @  16 : 22

~ 				if (ScreenRecordingController.isSupported()) {
~ 					GuiScreen screen = new GuiScreenRecordingSettings(this);
~ 					if (!GuiScreenRecordingNote.hasShown) {
~ 						screen = new GuiScreenRecordingNote(screen);
~ 					}
~ 					this.mc.displayGuiScreen(screen);

> INSERT  3 : 6  @  3

+ 			if (parGuiButton.id == 104) {
+ 				EagRuntime.showDebugConsole();
+ 			}

> INSERT  6 : 49  @  6

+ 
+ 		if (mc.theWorld == null && !EagRuntime.getConfiguration().isDemo()) {
+ 			GlStateManager.pushMatrix();
+ 			GlStateManager.scale(0.75f, 0.75f, 0.75f);
+ 			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 
+ 			String text = I18n.format("editProfile.importExport");
+ 
+ 			int w = mc.fontRendererObj.getStringWidth(text);
+ 			boolean hover = i > 1 && j > 1 && i < (w * 3 / 4) + 7 && j < 12;
+ 			if (hover) {
+ 				Mouse.showCursor(EnumCursorType.HAND);
+ 			}
+ 
+ 			drawString(mc.fontRendererObj, EnumChatFormatting.UNDERLINE + text, 5, 5, hover ? 0xFFEEEE22 : 0xFFCCCCCC);
+ 
+ 			GlStateManager.popMatrix();
+ 		}
+ 
+ 		if (mc.theWorld == null && EagRuntime.getConfiguration().isAllowBootMenu()) {
+ 			drawCenteredString(mc.fontRendererObj, I18n.format("options.pressDeleteText"), width / 2, height / 6 + 22,
+ 					11184810);
+ 		}
+ 
+ 		if (EagRuntime.getConfiguration().isEnableServerCookies() && mc.thePlayer == null) {
+ 			GlStateManager.pushMatrix();
+ 			GlStateManager.scale(0.75f, 0.75f, 0.75f);
+ 			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
+ 
+ 			String text = I18n.format("revokeSessionToken.button");
+ 
+ 			int w = mc.fontRendererObj.getStringWidth(text);
+ 			boolean hover = i > width - 5 - (w + 5) * 3 / 4 && j > 1 && i < width - 2 && j < 12;
+ 			if (hover) {
+ 				Mouse.showCursor(EnumCursorType.HAND);
+ 			}
+ 
+ 			drawString(mc.fontRendererObj, EnumChatFormatting.UNDERLINE + text, (width - 1) * 4 / 3 - w - 5, 5,
+ 					hover ? 0xFFEEEE22 : 0xFFCCCCCC);
+ 
+ 			GlStateManager.popMatrix();
+ 		}
+ 

> INSERT  2 : 35  @  2

+ 
+ 	@Override
+ 	protected void keyTyped(char parChar1, int parInt1) {
+ 		super.keyTyped(parChar1, parInt1);
+ 		if (parInt1 == KeyboardConstants.KEY_DELETE || parInt1 == KeyboardConstants.KEY_BACK) {
+ 			if (mc.theWorld == null && EagRuntime.getConfiguration().isAllowBootMenu()) {
+ 				mc.displayGuiScreen(new GuiScreenEnterBootMenu(this));
+ 			}
+ 		}
+ 	}
+ 
+ 	protected void mouseClicked(int mx, int my, int button) {
+ 		super.mouseClicked(mx, my, button);
+ 		if (mc.theWorld == null && !EagRuntime.getConfiguration().isDemo()) {
+ 			int w = mc.fontRendererObj.getStringWidth(I18n.format("editProfile.importExport"));
+ 			if (mx > 1 && my > 1 && mx < (w * 3 / 4) + 7 && my < 12) {
+ 				mc.displayGuiScreen(new GuiScreenImportExportProfile(this));
+ 				mc.getSoundHandler()
+ 						.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
+ 			}
+ 		}
+ 		if (EagRuntime.getConfiguration().isEnableServerCookies() && mc.thePlayer == null) {
+ 			int w = mc.fontRendererObj.getStringWidth(I18n.format("revokeSessionToken.button"));
+ 			if (mx > width - 5 - (w + 5) * 3 / 4 && my > 1 && mx < width - 2 && my < 12) {
+ 				ServerCookieDataStore.flush();
+ 				mc.displayGuiScreen(ServerCookieDataStore.numRevokable() == 0
+ 						? new GuiScreenGenericErrorMessage("errorNoSessions.title", "errorNoSessions.desc", this)
+ 						: new GuiScreenRevokeSessionToken(this));
+ 				mc.getSoundHandler()
+ 						.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
+ 			}
+ 		}
+ 	}

> EOF
