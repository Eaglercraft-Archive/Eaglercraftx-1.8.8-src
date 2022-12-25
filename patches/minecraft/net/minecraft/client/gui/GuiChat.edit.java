
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  4 : 15  @  5 : 7

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

> DELETE  21  @  13 : 18

> INSERT  33 : 35  @  30

+ 	private GuiButton exitButton;
+ 

> INSERT  44 : 47  @  39

+ 		if (!(this instanceof GuiSleepMP)) {
+ 			this.buttonList.add(exitButton = new GuiButton(69, this.width - 100, 3, 97, 20, I18n.format("chat.exit")));
+ 		}

> CHANGE  65 : 66  @  57 : 58

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  73 : 74  @  65 : 68

~ 		if (parInt1 != 28 && parInt1 != 156) {

> CHANGE  117 : 118  @  111 : 112

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> INSERT  129 : 135  @  123

+ 	protected void actionPerformed(GuiButton par1GuiButton) {
+ 		if (par1GuiButton.id == 69) {
+ 			this.mc.displayGuiScreen(null);
+ 		}
+ 	}
+ 

> INSERT  220 : 221  @  208

+ 		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

> INSERT  226 : 228  @  213

+ 		exitButton.yPosition = 3 + mc.guiAchievement.getHeight();
+ 

> EOF
