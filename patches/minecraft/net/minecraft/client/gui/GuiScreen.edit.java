
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 11

> DELETE  3  @  12 : 14

> INSERT  7 : 22  @  18

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

> CHANGE  23 : 24  @  19 : 27

~ import net.minecraft.client.gui.inventory.GuiContainer;

> DELETE  26  @  29 : 30

> INSERT  28 : 29  @  32

+ import net.minecraft.client.resources.I18n;

> DELETE  42  @  45 : 51

> CHANGE  59 : 61  @  68 : 69

~ 	private String clickedLinkURI;
~ 	protected long showingCloseKey = 0;

> INSERT  71 : 101  @  79

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

> CHANGE  103 : 115  @  81 : 83

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
~ 				|| (parInt1 == 1 && this.mc.gameSettings.keyBindClose.getKeyCode() == 0)) {

> INSERT  119 : 121  @  87

+ 		} else if (parInt1 == 1) {
+ 			showingCloseKey = System.currentTimeMillis();

> DELETE  122  @  88 : 89

> CHANGE  125 : 126  @  92 : 102

~ 		return EagRuntime.getClipboard();

> CHANGE  130 : 131  @  106 : 113

~ 			EagRuntime.setClipboard(copyText);

> INSERT  307 : 308  @  289

+ 					String uri = clickevent.getValue();

> CHANGE  309 : 314  @  290 : 311

~ 					if (this.mc.gameSettings.chatLinksPrompt) {
~ 						this.clickedLinkURI = uri;
~ 						this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, clickevent.getValue(), 31102009, false));
~ 					} else {
~ 						this.openWebLink(uri);

> CHANGE  316 : 317  @  313 : 315

~ 					// rip

> CHANGE  322 : 329  @  320 : 326

~ 					/*
~ 					 * ChatUserInfo chatuserinfo =
~ 					 * this.mc.getTwitchStream().func_152926_a(clickevent.getValue()); if
~ 					 * (chatuserinfo != null) { this.mc.displayGuiScreen(new
~ 					 * GuiTwitchUserMode(this.mc.getTwitchStream(), chatuserinfo)); } else { }
~ 					 */
~ 					LOGGER.error("Tried to handle twitch user but couldn\'t find them!");

> CHANGE  352 : 353  @  349 : 350

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  377 : 378  @  374 : 375

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  497 : 499  @  494 : 503

~ 	private void openWebLink(String parURI) {
~ 		EagRuntime.openLink(parURI);

> EOF
