
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  4 : 10  @  4

+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.Keyboard;
+ import net.lax1dude.eaglercraft.v1_8.Mouse;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  4  @  4 : 7

> DELETE  19  @  19 : 21

> CHANGE  186 : 187  @  186 : 187

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  13 : 16  @  13 : 14

~ 			if (parInt1 == getCloseKey() || (parInt1 == 1 && this.mc.areKeysLocked())) {
~ 				mc.displayGuiScreen(null);
~ 			} else if (!this.checkHotbarKeys(parInt1)) {

> INSERT  10 : 15  @  10

+ 	protected int getCloseKey() {
+ 		return selectedTabIndex != CreativeTabs.tabAllSearch.getTabIndex() ? super.getCloseKey()
+ 				: mc.gameSettings.keyBindClose.getKeyCode();
+ 	}
+ 

> CHANGE  49 : 50  @  49 : 50

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  322 : 323  @  322 : 323

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> EOF
