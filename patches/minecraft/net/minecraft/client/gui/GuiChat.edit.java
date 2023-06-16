
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  2 : 13  @  2 : 4

~ 
~ import org.apache.commons.lang3.StringUtils;
~ 
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.minecraft.client.resources.I18n;

> DELETE  6  @  6 : 11

> INSERT  12 : 14  @  12

+ 	private GuiButton exitButton;
+ 

> INSERT  9 : 12  @  9

+ 		if (!(this instanceof GuiSleepMP)) {
+ 			this.buttonList.add(exitButton = new GuiButton(69, this.width - 100, 3, 97, 20, I18n.format("chat.exit")));
+ 		}

> CHANGE  18 : 20  @  18 : 27

~ 	protected void keyTyped(char parChar1, int parInt1) {
~ 		if (parInt1 == 1 && (this.mc.gameSettings.keyBindClose.getKeyCode() == 0 || this.mc.areKeysLocked())) {

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

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> INSERT  11 : 17  @  11

+ 	protected void actionPerformed(GuiButton par1GuiButton) {
+ 		if (par1GuiButton.id == 69) {
+ 			this.mc.displayGuiScreen(null);
+ 		}
+ 	}
+ 

> INSERT  85 : 86  @  85

+ 		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

> INSERT  5 : 9  @  5

+ 		if (exitButton != null) {
+ 			exitButton.yPosition = 3 + mc.guiAchievement.getHeight();
+ 		}
+ 

> EOF
