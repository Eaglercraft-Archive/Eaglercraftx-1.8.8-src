
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

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

> CHANGE  18 : 19  @  18 : 19

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  7 : 8  @  7 : 10

~ 		if (parInt1 != 28 && parInt1 != 156) {

> CHANGE  43 : 44  @  43 : 44

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
