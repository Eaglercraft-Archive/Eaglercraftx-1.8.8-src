
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 14  @  2 : 9

~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenLANInfo;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiScreenLANNotSupported;
~ import net.lax1dude.eaglercraft.v1_8.sp.gui.GuiShareToLan;
~ import net.lax1dude.eaglercraft.v1_8.sp.lan.LANServerController;
~ import net.lax1dude.eaglercraft.v1_8.update.GuiUpdateCheckerOverlay;
~ import net.lax1dude.eaglercraft.v1_8.voice.GuiVoiceMenu;
~ import net.minecraft.client.Minecraft;
~ import net.minecraft.client.audio.PositionedSoundRecord;

> CHANGE  4 : 7  @  4 : 5

~ import net.minecraft.util.ChatComponentText;
~ import net.minecraft.util.EnumChatFormatting;
~ import net.minecraft.util.ResourceLocation;

> DELETE  2  @  2 : 4

> INSERT  1 : 15  @  1

+ 	private GuiButton lanButton;
+ 
+ 	boolean hasSentAutoSave = !SingleplayerServerController.isWorldRunning();
+ 
+ 	private GuiUpdateCheckerOverlay updateCheckerOverlay;
+ 	private GuiVoiceMenu voiceMenu;
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

> CHANGE  12 : 14  @  12 : 15

~ 		this.buttonList.add(lanButton = new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 + b0, 98, 20,
~ 				I18n.format(LANServerController.isLANOpen() ? "menu.closeLan" : "menu.openToLan", new Object[0])));

> CHANGE  4 : 9  @  4 : 5

~ 		lanButton.enabled = SingleplayerServerController.isWorldRunning();
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

> CHANGE  16 : 30  @  16 : 17

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

> CHANGE  6 : 13  @  6 : 7

~ 		if (EagRuntime.getConfiguration().isAllowVoiceClient()
~ 				&& (!mc.isSingleplayer() || LANServerController.isHostingLAN())) {
~ 			voiceMenu.updateScreen();
~ 		}
~ 		if (Mouse.isActuallyGrabbed()) {
~ 			Mouse.setGrabbed(false);
~ 		}

> CHANGE  4 : 5  @  4 : 5

~ 		this.drawCenteredString(this.fontRendererObj, I18n.format("menu.game", new Object[0]), this.width / 2, 20,

> CHANGE  1 : 55  @  1 : 2

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

> INSERT  1 : 80  @  1

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

> EOF
