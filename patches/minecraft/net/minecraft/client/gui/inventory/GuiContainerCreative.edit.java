
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  3 : 13  @  3 : 4

~ 
~ import com.carrotsearch.hppc.IntIntMap;
~ import com.google.common.collect.Lists;
~ 
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  4  @  4 : 7

> DELETE  19  @  19 : 21

> CHANGE  186 : 187  @  186 : 187

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  13 : 16  @  13 : 14

~ 			if (parInt1 == getCloseKey() || (parInt1 == 1 && Keyboard.areKeysLocked())) {
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

> INSERT  10 : 26  @  10

+ 	@Override
+ 	protected void touchTapped(int touchX, int touchY, int uid) {
+ 		int l = touchX - this.guiLeft;
+ 		int i1 = touchY - this.guiTop;
+ 
+ 		for (int m = 0; m < CreativeTabs.creativeTabArray.length; ++m) {
+ 			CreativeTabs creativetabs = CreativeTabs.creativeTabArray[m];
+ 			if (this.func_147049_a(creativetabs, l, i1)) {
+ 				this.setCurrentCreativeTab(creativetabs);
+ 				break;
+ 			}
+ 		}
+ 
+ 		super.touchTapped(touchX, touchY, uid);
+ 	}
+ 

> CHANGE  93 : 94  @  93 : 94

~ 		boolean flag = PointerInputAbstraction.getVCursorButtonDown(0);

> CHANGE  23 : 26  @  23 : 25

~ 		for (int m = 0; m < CreativeTabs.creativeTabArray.length; ++m) {
~ 			if (this.renderCreativeInventoryHoveringText(CreativeTabs.creativeTabArray[m], i, j)) {
~ 				Mouse.showCursor(EnumCursorType.HAND);

> CHANGE  16 : 18  @  16 : 17

~ 			List list = itemstack.getTooltipProfanityFilter(this.mc.thePlayer,
~ 					this.mc.gameSettings.advancedItemTooltips);

> CHANGE  2 : 3  @  2 : 3

~ 				IntIntMap map = EnchantmentHelper.getEnchantments(itemstack);

> CHANGE  1 : 2  @  1 : 3

~ 					Enchantment enchantment = Enchantment.getEnchantmentById(map.keys().iterator().next().value);

> CHANGE  1 : 3  @  1 : 2

~ 					for (int m = 0; m < CreativeTabs.creativeTabArray.length; ++m) {
~ 						CreativeTabs creativetabs1 = CreativeTabs.creativeTabArray[m];

> CHANGE  33 : 35  @  33 : 34

~ 		for (int m = 0; m < CreativeTabs.creativeTabArray.length; ++m) {
~ 			CreativeTabs creativetabs1 = CreativeTabs.creativeTabArray[m];

> CHANGE  114 : 115  @  114 : 115

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  139 : 154  @  139

+ 
+ 	public boolean blockPTTKey() {
+ 		return searchField.isFocused();
+ 	}
+ 
+ 	@Override
+ 	public boolean showCopyPasteButtons() {
+ 		return searchField.isFocused();
+ 	}
+ 
+ 	@Override
+ 	public void fireInputEvent(EnumInputEvent event, String param) {
+ 		searchField.fireInputEvent(event, param);
+ 	}
+ 

> EOF
