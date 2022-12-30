
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  4 : 10  @  5

+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.Keyboard;
+ import net.lax1dude.eaglercraft.v1_8.Mouse;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  10  @  4 : 7

> DELETE  19  @  22 : 24

> CHANGE  186 : 187  @  188 : 189

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  14 : 17  @  14 : 15

~ 			if (parInt1 == getCloseKey()) {
~ 				mc.displayGuiScreen(null);
~ 			} else if (!this.checkHotbarKeys(parInt1)) {

> INSERT  13 : 18  @  11

+ 	protected int getCloseKey() {
+ 		return selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex() ? super.getCloseKey()
+ 				: mc.gameSettings.keyBindClose.getKeyCode();
+ 	}
+ 

> CHANGE  54 : 55  @  49 : 50

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  323 : 324  @  323 : 324

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> EOF
