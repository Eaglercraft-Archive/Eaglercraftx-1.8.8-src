
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  2 : 13  @  3 : 5

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

> DELETE  17  @  8 : 13

> INSERT  12 : 14  @  17

+ 	private GuiButton exitButton;
+ 

> INSERT  11 : 14  @  9

+ 		if (!(this instanceof GuiSleepMP)) {
+ 			this.buttonList.add(exitButton = new GuiButton(69, this.width - 100, 3, 97, 20, I18n.format("chat.exit")));
+ 		}

> CHANGE  21 : 22  @  18 : 19

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  8 : 9  @  8 : 11

~ 		if (parInt1 != 28 && parInt1 != 156) {

> CHANGE  44 : 45  @  46 : 47

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> INSERT  12 : 18  @  12

+ 	protected void actionPerformed(GuiButton par1GuiButton) {
+ 		if (par1GuiButton.id == 69) {
+ 			this.mc.displayGuiScreen(null);
+ 		}
+ 	}
+ 

> INSERT  91 : 92  @  85

+ 		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

> INSERT  6 : 10  @  5

+ 		if (exitButton != null) {
+ 			exitButton.yPosition = 3 + mc.guiAchievement.getHeight();
+ 		}
+ 

> EOF
