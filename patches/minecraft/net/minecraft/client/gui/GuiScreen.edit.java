
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 11

> DELETE  1  @  1 : 3

> INSERT  4 : 19  @  4

+ 
+ import org.apache.commons.lang3.StringUtils;
+ 
+ import com.google.common.base.Splitter;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Sets;
+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.Keyboard;
+ import net.lax1dude.eaglercraft.v1_8.Mouse;
+ import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> CHANGE  1 : 2  @  1 : 9

~ import net.minecraft.client.gui.inventory.GuiContainer;

> DELETE  2  @  2 : 3

> INSERT  2 : 3  @  2

+ import net.minecraft.client.resources.I18n;

> DELETE  13  @  13 : 19

> CHANGE  17 : 19  @  17 : 18

~ 	private String clickedLinkURI;
~ 	protected long showingCloseKey = 0;

> INSERT  10 : 40  @  10

+ 		long millis = System.currentTimeMillis();
+ 		long closeKeyTimeout = millis - showingCloseKey;
+ 		if (closeKeyTimeout < 3000l) {
+ 			int alpha1 = 0xC0000000;
+ 			int alpha2 = 0xFF000000;
+ 			if (closeKeyTimeout > 2500l) {
+ 				float f = (float) (3000l - closeKeyTimeout) * 0.002f;
+ 				if (f < 0.03f)
+ 					f = 0.03f;
+ 				alpha1 = (int) (f * 192.0f) << 24;
+ 				alpha2 = (int) (f * 255.0f) << 24;
+ 			}
+ 			String str;
+ 			int k = getCloseKey();
+ 			if (k == KeyboardConstants.KEY_GRAVE) {
+ 				str = I18n.format("gui.exitKeyRetarded");
+ 			} else {
+ 				str = I18n.format("gui.exitKey", Keyboard.getKeyName(k));
+ 			}
+ 			int w = fontRendererObj.getStringWidth(str);
+ 			int x = (width - w - 4) / 2;
+ 			int y = 10;
+ 			drawRect(x, y, x + w + 4, y + 12, alpha1);
+ 			if (closeKeyTimeout > 2500l)
+ 				GlStateManager.enableBlend();
+ 			fontRendererObj.drawStringWithShadow(str, x + 2, y + 2, 0xFFAAAA | alpha2);
+ 			if (closeKeyTimeout > 2500l)
+ 				GlStateManager.disableBlend();
+ 		}
+ 

> CHANGE  2 : 14  @  2 : 4

~ 	protected int getCloseKey() {
~ 		if (this instanceof GuiContainer) {
~ 			return this.mc.gameSettings.keyBindInventory.getKeyCode();
~ 		} else {
~ 			return this.mc.gameSettings.keyBindClose.getKeyCode();
~ 		}
~ 	}
~ 
~ 	protected void keyTyped(char parChar1, int parInt1) {
~ 		if (((this.mc.theWorld == null || this.mc.thePlayer.getHealth() <= 0.0F) && parInt1 == 1)
~ 				|| parInt1 == this.mc.gameSettings.keyBindClose.getKeyCode()
~ 				|| (parInt1 == 1 && (this.mc.gameSettings.keyBindClose.getKeyCode() == 0 || this.mc.areKeysLocked()))) {

> INSERT  4 : 6  @  4

+ 		} else if (parInt1 == 1) {
+ 			showingCloseKey = System.currentTimeMillis();

> DELETE  1  @  1 : 2

> CHANGE  3 : 4  @  3 : 13

~ 		return EagRuntime.getClipboard();

> CHANGE  4 : 5  @  4 : 11

~ 			EagRuntime.setClipboard(copyText);

> CHANGE  68 : 71  @  68 : 69

~ 				if (s1.length() > 0) {
~ 					this.fontRendererObj.drawStringWithShadow(s1, (float) j2, (float) k2, -1);
~ 				}

> INSERT  107 : 108  @  107

+ 					String uri = clickevent.getValue();

> CHANGE  1 : 6  @  1 : 22

~ 					if (this.mc.gameSettings.chatLinksPrompt) {
~ 						this.clickedLinkURI = uri;
~ 						this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, clickevent.getValue(), 31102009, false));
~ 					} else {
~ 						this.openWebLink(uri);

> CHANGE  2 : 3  @  2 : 4

~ 					// rip

> CHANGE  5 : 12  @  5 : 11

~ 					/*
~ 					 * ChatUserInfo chatuserinfo =
~ 					 * this.mc.getTwitchStream().func_152926_a(clickevent.getValue()); if
~ 					 * (chatuserinfo != null) { this.mc.displayGuiScreen(new
~ 					 * GuiTwitchUserMode(this.mc.getTwitchStream(), chatuserinfo)); } else { }
~ 					 */
~ 					LOGGER.error("Tried to handle twitch user but couldn\'t find them!");

> CHANGE  23 : 24  @  23 : 24

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  24 : 25  @  24 : 25

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  119 : 121  @  119 : 128

~ 	private void openWebLink(String parURI) {
~ 		EagRuntime.openLink(parURI);

> EOF
