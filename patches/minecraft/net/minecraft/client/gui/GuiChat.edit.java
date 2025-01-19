
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  2 : 18  @  2 : 4

~ 
~ import org.apache.commons.lang3.StringUtils;
~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.GuiScreenVisualViewport;
~ import net.lax1dude.eaglercraft.v1_8.notifications.GuiButtonNotifBell;
~ import net.lax1dude.eaglercraft.v1_8.notifications.GuiScreenNotifications;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.minecraft.client.resources.I18n;

> DELETE  6  @  6 : 11

> CHANGE  1 : 2  @  1 : 2

~ public class GuiChat extends GuiScreenVisualViewport {

> INSERT  10 : 13  @  10

+ 	private GuiButton exitButton;
+ 	private GuiButtonNotifBell notifBellButton;
+ 

> INSERT  9 : 17  @  9

+ 		if (!(this instanceof GuiSleepMP)) {
+ 			this.buttonList.add(exitButton = new GuiButton(69, this.width - 100, 3, 97, 20, I18n.format("chat.exit")));
+ 			if (!this.mc.isIntegratedServerRunning() && this.mc.thePlayer != null
+ 					&& this.mc.thePlayer.sendQueue.getEaglerMessageProtocol().ver >= 4) {
+ 				this.buttonList.add(notifBellButton = new GuiButtonNotifBell(70, this.width - 122, 3));
+ 				notifBellButton.setUnread(mc.thePlayer.sendQueue.getNotifManager().getUnread());
+ 			}
+ 		}

> CHANGE  14 : 15  @  14 : 15

~ 	public void updateScreen0() {

> INSERT  1 : 4  @  1

+ 		if (notifBellButton != null && mc.thePlayer != null) {
+ 			notifBellButton.setUnread(mc.thePlayer.sendQueue.getNotifManager().getUnread());
+ 		}

> CHANGE  2 : 4  @  2 : 11

~ 	protected void keyTyped(char parChar1, int parInt1) {
~ 		if (parInt1 == 1 && (this.mc.gameSettings.keyBindClose.getKeyCode() == 0 || Keyboard.areKeysLocked())) {

> CHANGE  1 : 5  @  1 : 10

~ 		} else {
~ 			this.waitingOnAutocomplete = false;
~ 			if (parInt1 == 15) {
~ 				this.autocompletePlayerNames();

> CHANGE  1 : 2  @  1 : 2

~ 				this.playerNamesFound = false;

> DELETE  1  @  1 : 6

> CHANGE  1 : 21  @  1 : 2

~ 			if (parInt1 != 28 && parInt1 != 156) {
~ 				if (parInt1 == 200) {
~ 					this.getSentHistory(-1);
~ 				} else if (parInt1 == 208) {
~ 					this.getSentHistory(1);
~ 				} else if (parInt1 == 201) {
~ 					this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
~ 				} else if (parInt1 == 209) {
~ 					this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
~ 				} else {
~ 					this.inputField.textboxKeyTyped(parChar1, parInt1);
~ 				}
~ 			} else {
~ 				String s = this.inputField.getText().trim();
~ 				if (s.length() > 0) {
~ 					this.sendChatMessage(s);
~ 				}
~ 
~ 				this.mc.displayGuiScreen((GuiScreen) null);
~ 			}

> CHANGE  25 : 26  @  25 : 26

~ 	protected void mouseClicked0(int parInt1, int parInt2, int parInt3) {

> CHANGE  1 : 3  @  1 : 2

~ 			IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI()
~ 					.getChatComponent(PointerInputAbstraction.getVCursorX(), PointerInputAbstraction.getVCursorY());

> INSERT  3 : 6  @  3

+ 			if (mc.notifRenderer.handleClicked(this, parInt1, parInt2)) {
+ 				return;
+ 			}

> CHANGE  3 : 4  @  3 : 4

~ 		super.mouseClicked0(parInt1, parInt2, parInt3);

> INSERT  2 : 10  @  2

+ 	protected void actionPerformed(GuiButton par1GuiButton) {
+ 		if (par1GuiButton.id == 69) {
+ 			this.mc.displayGuiScreen(null);
+ 		} else if (par1GuiButton.id == 70) {
+ 			this.mc.displayGuiScreen(new GuiScreenNotifications(this));
+ 		}
+ 	}
+ 

> CHANGE  32 : 34  @  32 : 33

~ 		int l = this.foundPlayerNames.size();
~ 		if (l > 1) {

> CHANGE  2 : 3  @  2 : 3

~ 			for (int i = 0; i < l; ++i) {

> CHANGE  4 : 5  @  4 : 5

~ 				stringbuilder.append(this.foundPlayerNames.get(i));

> CHANGE  41 : 42  @  41 : 42

~ 	public void drawScreen0(int i, int j, float f) {

> CHANGE  2 : 5  @  2 : 3

~ 		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
~ 		IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI()
~ 				.getChatComponent(PointerInputAbstraction.getVCursorX(), PointerInputAbstraction.getVCursorY());

> CHANGE  4 : 9  @  4 : 5

~ 		if (exitButton != null) {
~ 			exitButton.yPosition = 3 + mc.guiAchievement.getHeight();
~ 		}
~ 
~ 		super.drawScreen0(i, j, f);

> CHANGE  7 : 9  @  7 : 8

~ 			for (int i = 0; i < parArrayOfString.length; ++i) {
~ 				String s = parArrayOfString[i];

> INSERT  24 : 37  @  24

+ 
+ 	public boolean blockPTTKey() {
+ 		return true;
+ 	}
+ 
+ 	public boolean showCopyPasteButtons() {
+ 		return true;
+ 	}
+ 
+ 	public void fireInputEvent(EnumInputEvent event, String str) {
+ 		inputField.fireInputEvent(event, str);
+ 	}
+ 

> EOF
