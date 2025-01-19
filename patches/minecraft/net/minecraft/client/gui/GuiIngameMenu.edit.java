
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 21  @  2 : 9

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ import net.lax1dude.eaglercraft.v1_8.PauseMenuCustomizeState;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.GuiButtonWithStupidIcons;
~ import net.lax1dude.eaglercraft.v1_8.notifications.GuiButtonNotifBell;
~ import net.lax1dude.eaglercraft.v1_8.notifications.GuiScreenNotifications;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenLANInfo;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenLANNotSupported;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiShareToLan;
~ import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerController;
~ import net.lax1dude.eaglercraft.v1_8.update.GuiUpdateCheckerOverlay;
~ import net.lax1dude.eaglercraft.v1_8.voice.GuiVoiceMenu;
~ import net.lax1dude.eaglercraft.v1_8.webview.GuiScreenPhishingWarning;
~ import net.lax1dude.eaglercraft.v1_8.webview.GuiScreenRecieveServerInfo;
~ import net.lax1dude.eaglercraft.v1_8.webview.GuiScreenServerInfo;
~ import net.minecraft.client.Minecraft;
~ import net.minecraft.client.audio.PositionedSoundRecord;

> CHANGE  4 : 7  @  4 : 5

~ import net.minecraft.util.ChatComponentText;
~ import net.minecraft.util.EnumChatFormatting;
~ import net.minecraft.util.ResourceLocation;

> DELETE  2  @  2 : 4

> INSERT  1 : 16  @  1

+ 	private GuiButton lanButton;
+ 
+ 	boolean hasSentAutoSave = !SingleplayerServerController.isWorldRunning();
+ 
+ 	private GuiUpdateCheckerOverlay updateCheckerOverlay;
+ 	private GuiVoiceMenu voiceMenu;
+ 	private GuiButtonNotifBell notifBellButton;
+ 
+ 	public GuiIngameMenu() {
+ 		updateCheckerOverlay = new GuiUpdateCheckerOverlay(true, this);
+ 		if (EagRuntime.getConfiguration().isAllowVoiceClient()) {
+ 			voiceMenu = new GuiVoiceMenu(this);
+ 		}
+ 	}
+ 

> DELETE  1  @  1 : 2

> INSERT  1 : 2  @  1

+ 		this.updateCheckerOverlay.setResolution(mc, width, height);

> CHANGE  2 : 6  @  2 : 4

~ 		this.buttonList.add(new GuiButtonWithStupidIcons(1, this.width / 2 - 100, this.height / 4 + 120 + b0,
~ 				I18n.format("menu.returnToMenu", new Object[0]), PauseMenuCustomizeState.icon_disconnect_L,
~ 				PauseMenuCustomizeState.icon_disconnect_L_aspect, PauseMenuCustomizeState.icon_disconnect_R,
~ 				PauseMenuCustomizeState.icon_disconnect_R_aspect));

> INSERT  2 : 6  @  2

+ 			if (this.mc.thePlayer != null && this.mc.thePlayer.sendQueue.getEaglerMessageProtocol().ver >= 4) {
+ 				this.buttonList.add(notifBellButton = new GuiButtonNotifBell(11, width - 22, height - 22));
+ 				notifBellButton.setUnread(mc.thePlayer.sendQueue.getNotifManager().getUnread());
+ 			}

> CHANGE  2 : 40  @  2 : 14

~ 		this.buttonList.add(new GuiButtonWithStupidIcons(4, this.width / 2 - 100, this.height / 4 + 24 + b0,
~ 				I18n.format("menu.returnToGame", new Object[0]), PauseMenuCustomizeState.icon_backToGame_L,
~ 				PauseMenuCustomizeState.icon_backToGame_L_aspect, PauseMenuCustomizeState.icon_backToGame_R,
~ 				PauseMenuCustomizeState.icon_backToGame_R_aspect));
~ 		this.buttonList.add(new GuiButtonWithStupidIcons(0, this.width / 2 - 100, this.height / 4 + 96 + b0, 98, 20,
~ 				I18n.format("menu.options", new Object[0]), PauseMenuCustomizeState.icon_options_L,
~ 				PauseMenuCustomizeState.icon_options_L_aspect, PauseMenuCustomizeState.icon_options_R,
~ 				PauseMenuCustomizeState.icon_options_R_aspect));
~ 		this.buttonList
~ 				.add(lanButton = new GuiButtonWithStupidIcons(7, this.width / 2 + 2, this.height / 4 + 96 + b0, 98, 20,
~ 						I18n.format(LANServerController.isLANOpen() ? "menu.closeLan" : "menu.openToLan",
~ 								new Object[0]),
~ 						PauseMenuCustomizeState.icon_discord_L, PauseMenuCustomizeState.icon_discord_L_aspect,
~ 						PauseMenuCustomizeState.icon_discord_R, PauseMenuCustomizeState.icon_discord_R_aspect));
~ 		this.buttonList.add(new GuiButtonWithStupidIcons(5, this.width / 2 - 100, this.height / 4 + 48 + b0, 98, 20,
~ 				I18n.format("gui.achievements", new Object[0]), PauseMenuCustomizeState.icon_achievements_L,
~ 				PauseMenuCustomizeState.icon_achievements_L_aspect, PauseMenuCustomizeState.icon_achievements_R,
~ 				PauseMenuCustomizeState.icon_achievements_R_aspect));
~ 		this.buttonList.add(new GuiButtonWithStupidIcons(6, this.width / 2 + 2, this.height / 4 + 48 + b0, 98, 20,
~ 				I18n.format("gui.stats", new Object[0]), PauseMenuCustomizeState.icon_statistics_L,
~ 				PauseMenuCustomizeState.icon_statistics_L_aspect, PauseMenuCustomizeState.icon_statistics_R,
~ 				PauseMenuCustomizeState.icon_statistics_R_aspect));
~ 		lanButton.enabled = SingleplayerServerController.isWorldRunning();
~ 		if (PauseMenuCustomizeState.discordButtonMode != PauseMenuCustomizeState.DISCORD_MODE_NONE) {
~ 			lanButton.enabled = true;
~ 			lanButton.id = 8;
~ 			lanButton.displayString = "" + PauseMenuCustomizeState.discordButtonText;
~ 		}
~ 		if (PauseMenuCustomizeState.serverInfoMode != PauseMenuCustomizeState.DISCORD_MODE_NONE) {
~ 			this.buttonList.add(new GuiButtonWithStupidIcons(9, this.width / 2 - 100, this.height / 4 + 72 + b0,
~ 					PauseMenuCustomizeState.serverInfoButtonText, PauseMenuCustomizeState.icon_serverInfo_L,
~ 					PauseMenuCustomizeState.icon_serverInfo_L_aspect, PauseMenuCustomizeState.icon_serverInfo_R,
~ 					PauseMenuCustomizeState.icon_serverInfo_R_aspect));
~ 		}
~ 		if (!hasSentAutoSave) {
~ 			hasSentAutoSave = true;
~ 			SingleplayerServerController.autoSave();
~ 		}

> CHANGE  2 : 3  @  2 : 3

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  5 : 6  @  5 : 7

~ 			boolean flag = this.mc.isIntegratedServerRunning() || this.mc.isDemo();

> CHANGE  4 : 5  @  4 : 8

~ 				this.mc.shutdownIntegratedServer(new GuiMainMenu());

> CHANGE  1 : 2  @  1 : 2

~ 				this.mc.shutdownIntegratedServer(new GuiMultiplayer(new GuiMainMenu()));

> CHANGE  16 : 69  @  16 : 17

~ 			if (!LANServerController.supported()) {
~ 				mc.displayGuiScreen(new GuiScreenLANNotSupported(this));
~ 			} else if (LANServerController.isLANOpen()) {
~ 				if (LANServerController.hasPeers()) {
~ 					mc.displayGuiScreen(new GuiYesNo(this, I18n.format("networkSettings.delete"),
~ 							I18n.format("lanServer.wouldYouLikeToKick"), 0));
~ 				} else {
~ 					confirmClicked(false, 0);
~ 				}
~ 			} else {
~ 				this.mc.displayGuiScreen(GuiScreenLANInfo.showLANInfoScreen(
~ 						new GuiShareToLan(this, this.mc.playerController.getCurrentGameType().getName())));
~ 			}
~ 			break;
~ 		case 8:
~ 			if (PauseMenuCustomizeState.discordButtonMode == PauseMenuCustomizeState.DISCORD_MODE_INVITE_URL
~ 					&& PauseMenuCustomizeState.discordInviteURL != null) {
~ 				EagRuntime.openLink(PauseMenuCustomizeState.discordInviteURL);
~ 			}
~ 			break;
~ 		case 9:
~ 			switch (PauseMenuCustomizeState.serverInfoMode) {
~ 			case PauseMenuCustomizeState.SERVER_INFO_MODE_EXTERNAL_URL:
~ 				if (PauseMenuCustomizeState.serverInfoURL != null) {
~ 					EagRuntime.openLink(PauseMenuCustomizeState.serverInfoURL);
~ 				}
~ 				break;
~ 			case PauseMenuCustomizeState.SERVER_INFO_MODE_SHOW_EMBED_OVER_HTTP:
~ 				if (PauseMenuCustomizeState.serverInfoURL != null) {
~ 					GuiScreen screen = GuiScreenServerInfo.createForCurrentState(this,
~ 							PauseMenuCustomizeState.serverInfoURL);
~ 					if (!this.mc.gameSettings.hasHiddenPhishWarning && !GuiScreenPhishingWarning.hasShownMessage) {
~ 						screen = new GuiScreenPhishingWarning(screen);
~ 					}
~ 					this.mc.displayGuiScreen(screen);
~ 				}
~ 				break;
~ 			case PauseMenuCustomizeState.SERVER_INFO_MODE_SHOW_EMBED_OVER_WS:
~ 				if (PauseMenuCustomizeState.serverInfoHash != null) {
~ 					GuiScreen screen = new GuiScreenRecieveServerInfo(this, PauseMenuCustomizeState.serverInfoHash);
~ 					if (!this.mc.gameSettings.hasHiddenPhishWarning && !GuiScreenPhishingWarning.hasShownMessage) {
~ 						screen = new GuiScreenPhishingWarning(screen);
~ 					}
~ 					this.mc.displayGuiScreen(screen);
~ 				}
~ 				break;
~ 			default:
~ 				break;
~ 			}
~ 			break;
~ 		case 11:
~ 			this.mc.displayGuiScreen(new GuiScreenNotifications(this));
~ 			break;

> CHANGE  6 : 16  @  6 : 7

~ 		if (EagRuntime.getConfiguration().isAllowVoiceClient()
~ 				&& (!mc.isSingleplayer() || LANServerController.isHostingLAN())) {
~ 			voiceMenu.updateScreen();
~ 		}
~ 		if (Mouse.isActuallyGrabbed()) {
~ 			Mouse.setGrabbed(false);
~ 		}
~ 		if (notifBellButton != null && mc.thePlayer != null) {
~ 			notifBellButton.setUnread(mc.thePlayer.sendQueue.getNotifManager().getUnread());
~ 		}

> CHANGE  4 : 80  @  4 : 7

~ 		String titleStr = I18n.format("menu.game", new Object[0]);
~ 		int titleStrWidth = fontRendererObj.getStringWidth(titleStr);
~ 		this.drawString(this.fontRendererObj, titleStr, (this.width - titleStrWidth) / 2, 20, 16777215);
~ 		if (PauseMenuCustomizeState.icon_title_L != null) {
~ 			mc.getTextureManager().bindTexture(PauseMenuCustomizeState.icon_title_L);
~ 			GlStateManager.pushMatrix();
~ 			GlStateManager.translate(
~ 					(this.width - titleStrWidth) / 2 - 6 - 16 * PauseMenuCustomizeState.icon_title_L_aspect, 16, 0.0f);
~ 			float f2 = 16.0f / 256.0f;
~ 			GlStateManager.scale(f2 * PauseMenuCustomizeState.icon_title_L_aspect, f2, f2);
~ 			this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
~ 			GlStateManager.popMatrix();
~ 		}
~ 		if (PauseMenuCustomizeState.icon_title_R != null) {
~ 			mc.getTextureManager().bindTexture(PauseMenuCustomizeState.icon_title_L);
~ 			GlStateManager.pushMatrix();
~ 			GlStateManager.translate((this.width - titleStrWidth) / 2 + titleStrWidth + 6, 16, 0.0f);
~ 			float f2 = 16.0f / 256.0f;
~ 			GlStateManager.scale(f2 * PauseMenuCustomizeState.icon_title_R_aspect, f2, f2);
~ 			this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
~ 			GlStateManager.popMatrix();
~ 		}
~ 
~ 		this.updateCheckerOverlay.drawScreen(i, j, f);
~ 
~ 		if (LANServerController.isLANOpen()) {
~ 			int offset = this.updateCheckerOverlay.getSharedWorldInfoYOffset();
~ 			String str = I18n.format("lanServer.pauseMenu0");
~ 			drawString(fontRendererObj, str, 6, 10 + offset, 0xFFFF55);
~ 
~ 			if (mc.gameSettings.hideJoinCode) {
~ 				GlStateManager.pushMatrix();
~ 				GlStateManager.translate(7.0f, 25.0f + offset, 0.0f);
~ 				GlStateManager.scale(0.75f, 0.75f, 0.75f);
~ 				str = I18n.format("lanServer.showCode");
~ 				int w = fontRendererObj.getStringWidth(str);
~ 				boolean hover = i > 4 && i < 8 + w * 3 / 4 && j > 24 + offset && j < 25 + offset + 8;
~ 				drawString(fontRendererObj, EnumChatFormatting.UNDERLINE + str, 0, 0, hover ? 0xEEEEAA : 0xCCCC55);
~ 				GlStateManager.popMatrix();
~ 			} else {
~ 				int w = fontRendererObj.getStringWidth(str);
~ 				GlStateManager.pushMatrix();
~ 				GlStateManager.translate(6 + w + 3, 11 + offset, 0.0f);
~ 				GlStateManager.scale(0.75f, 0.75f, 0.75f);
~ 				str = I18n.format("lanServer.hideCode");
~ 				int w2 = fontRendererObj.getStringWidth(str);
~ 				boolean hover = i > 6 + w + 2 && i < 6 + w + 3 + w2 * 3 / 4 && j > 11 + offset - 1
~ 						&& j < 11 + offset + 6;
~ 				drawString(fontRendererObj, EnumChatFormatting.UNDERLINE + str, 0, 0, hover ? 0xEEEEAA : 0xCCCC55);
~ 				GlStateManager.popMatrix();
~ 
~ 				drawString(
~ 						fontRendererObj, EnumChatFormatting.GRAY + I18n.format("lanServer.pauseMenu1") + " "
~ 								+ EnumChatFormatting.RESET + LANServerController.getCurrentURI(),
~ 						6, 25 + offset, 0xFFFFFF);
~ 				drawString(
~ 						fontRendererObj, EnumChatFormatting.GRAY + I18n.format("lanServer.pauseMenu2") + " "
~ 								+ EnumChatFormatting.RESET + LANServerController.getCurrentCode(),
~ 						6, 35 + offset, 0xFFFFFF);
~ 			}
~ 		}
~ 
~ 		try {
~ 			if (EagRuntime.getConfiguration().isAllowVoiceClient()
~ 					&& (!mc.isSingleplayer() || LANServerController.isHostingLAN())) {
~ 				if (voiceMenu.isBlockingInput()) {
~ 					super.drawScreen(0, 0, f);
~ 				} else {
~ 					super.drawScreen(i, j, f);
~ 				}
~ 				voiceMenu.drawScreen(i, j, f);
~ 			} else {
~ 				super.drawScreen(i, j, f);
~ 			}
~ 		} catch (GuiVoiceMenu.AbortedException ex) {
~ 		}

> INSERT  1 : 84  @  1

+ 
+ 	protected void keyTyped(char par1, int par2) {
+ 		try {
+ 			if (EagRuntime.getConfiguration().isAllowVoiceClient()
+ 					&& (!mc.isSingleplayer() || LANServerController.isHostingLAN())) {
+ 				voiceMenu.keyTyped(par1, par2);
+ 			}
+ 			super.keyTyped(par1, par2);
+ 		} catch (GuiVoiceMenu.AbortedException ex) {
+ 		}
+ 	}
+ 
+ 	public void confirmClicked(boolean par1, int par2) {
+ 		mc.displayGuiScreen(this);
+ 		LANServerController.closeLANNoKick();
+ 		if (par1) {
+ 			LANServerController.cleanupLAN();
+ 			SingleplayerServerController.configureLAN(this.mc.theWorld.getWorldInfo().getGameType(), false);
+ 		}
+ 		this.mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(I18n.format("lanServer.closed")));
+ 		this.lanButton.displayString = I18n.format("menu.openToLan");
+ 	}
+ 
+ 	protected void mouseClicked(int par1, int par2, int par3) {
+ 		try {
+ 			if (EagRuntime.getConfiguration().isAllowVoiceClient()
+ 					&& (!mc.isSingleplayer() || LANServerController.isHostingLAN())) {
+ 				voiceMenu.mouseClicked(par1, par2, par3);
+ 			}
+ 		} catch (GuiVoiceMenu.AbortedException ex) {
+ 			return;
+ 		}
+ 		if (par3 == 0) {
+ 			int offset = this.updateCheckerOverlay.getSharedWorldInfoYOffset();
+ 			if (mc.gameSettings.hideJoinCode) {
+ 				String str = I18n.format("lanServer.showCode");
+ 				int w = fontRendererObj.getStringWidth(str);
+ 				if (par1 > 4 && par1 < 8 + w * 3 / 4 && par2 > 24 + offset && par2 < 25 + offset + 8) {
+ 					mc.gameSettings.hideJoinCode = false;
+ 					this.mc.getSoundHandler()
+ 							.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
+ 					mc.gameSettings.saveOptions();
+ 				}
+ 			} else {
+ 				String str = I18n.format("lanServer.pauseMenu0");
+ 				int w = fontRendererObj.getStringWidth(str);
+ 				str = I18n.format("lanServer.hideCode");
+ 				int w2 = fontRendererObj.getStringWidth(str);
+ 				if (par1 > 6 + w + 2 && par1 < 6 + w + 3 + w2 * 3 / 4 && par2 > 11 + offset - 1
+ 						&& par2 < 11 + offset + 6) {
+ 					mc.gameSettings.hideJoinCode = true;
+ 					this.mc.getSoundHandler()
+ 							.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
+ 					mc.gameSettings.saveOptions();
+ 				}
+ 			}
+ 
+ 		}
+ 		this.updateCheckerOverlay.mouseClicked(par1, par2, par3);
+ 		super.mouseClicked(par1, par2, par3);
+ 	}
+ 
+ 	public void setWorldAndResolution(Minecraft par1Minecraft, int par2, int par3) {
+ 		super.setWorldAndResolution(par1Minecraft, par2, par3);
+ 		if (EagRuntime.getConfiguration().isAllowVoiceClient()) {
+ 			voiceMenu.setResolution(par1Minecraft, par2, par3);
+ 		}
+ 	}
+ 
+ 	protected void mouseReleased(int par1, int par2, int par3) {
+ 		try {
+ 			if (EagRuntime.getConfiguration().isAllowVoiceClient()
+ 					&& (!mc.isSingleplayer() || LANServerController.isHostingLAN())) {
+ 				voiceMenu.mouseReleased(par1, par2, par3);
+ 			}
+ 			super.mouseReleased(par1, par2, par3);
+ 		} catch (GuiVoiceMenu.AbortedException ex) {
+ 		}
+ 	}
+ 
+ 	protected boolean isPartOfPauseMenu() {
+ 		return true;
+ 	}

> EOF
