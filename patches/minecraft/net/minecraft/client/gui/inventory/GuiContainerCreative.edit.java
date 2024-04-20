
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  4 : 11  @  4

+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.Keyboard;
+ import net.lax1dude.eaglercraft.v1_8.Mouse;
+ import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
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

> CHANGE  10 : 12  @  10 : 11

~ 		for (int i = 0; i < Enchantment.enchantmentsBookList.length; ++i) {
~ 			Enchantment enchantment = Enchantment.enchantmentsBookList[i];

> CHANGE  12 : 15  @  12 : 14

~ 			List<String> lst = itemstack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
~ 			for (int i = 0, l = lst.size(); i < l; ++i) {
~ 				if (EnumChatFormatting.getTextWithoutFormattingCodes(lst.get(i)).toLowerCase().contains(s1)) {

> CHANGE  24 : 25  @  24 : 25

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  4 : 6  @  4 : 6

~ 			for (int k = 0; k < CreativeTabs.creativeTabArray.length; ++k) {
~ 				if (this.func_147049_a(CreativeTabs.creativeTabArray[k], i, j)) {

> CHANGE  13 : 15  @  13 : 14

~ 			for (int m = 0; m < CreativeTabs.creativeTabArray.length; ++m) {
~ 				CreativeTabs creativetabs = CreativeTabs.creativeTabArray[m];

> CHANGE  127 : 130  @  127 : 129

~ 		for (int m = 0; m < CreativeTabs.creativeTabArray.length; ++m) {
~ 			if (this.renderCreativeInventoryHoveringText(CreativeTabs.creativeTabArray[m], i, j)) {
~ 				Mouse.showCursor(EnumCursorType.HAND);

> CHANGE  24 : 26  @  24 : 25

~ 					for (int m = 0; m < CreativeTabs.creativeTabArray.length; ++m) {
~ 						CreativeTabs creativetabs1 = CreativeTabs.creativeTabArray[m];

> CHANGE  33 : 35  @  33 : 34

~ 		for (int m = 0; m < CreativeTabs.creativeTabArray.length; ++m) {
~ 			CreativeTabs creativetabs1 = CreativeTabs.creativeTabArray[m];

> CHANGE  114 : 115  @  114 : 115

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  139 : 143  @  139

+ 
+ 	public boolean blockPTTKey() {
+ 		return searchField.isFocused();
+ 	}

> EOF
