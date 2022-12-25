
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  3 : 11  @  5

+ 
+ import com.google.common.collect.Sets;
+ 
+ import net.lax1dude.eaglercraft.v1_8.Keyboard;
+ import net.lax1dude.eaglercraft.v1_8.internal.KeyboardConstants;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;

> DELETE  13  @  7 : 9

> DELETE  14  @  10 : 11

> DELETE  22  @  19 : 20

> CHANGE  210 : 211  @  208 : 209

~ 				EaglerTextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite(s1);

> CHANGE  267 : 268  @  265 : 266

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  503 : 507  @  501 : 503

~ 	protected void keyTyped(char parChar1, int parInt1) {
~ 		if (parInt1 == this.mc.gameSettings.keyBindClose.getKeyCode()
~ 				|| parInt1 == this.mc.gameSettings.keyBindInventory.getKeyCode()
~ 				|| (parInt1 == 1 && this.mc.gameSettings.keyBindClose.getKeyCode() == 0)) {

> CHANGE  508 : 510  @  504 : 512

~ 			if (this.mc.currentScreen == null) {
~ 				this.mc.setIngameFocus();

> INSERT  511 : 522  @  513

+ 		} else if (parInt1 == 1) {
+ 			showingCloseKey = System.currentTimeMillis();
+ 		} else {
+ 			this.checkHotbarKeys(parInt1);
+ 			if (this.theSlot != null && this.theSlot.getHasStack()) {
+ 				if (parInt1 == this.mc.gameSettings.keyBindPickBlock.getKeyCode()) {
+ 					this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, 3);
+ 				} else if (parInt1 == this.mc.gameSettings.keyBindDrop.getKeyCode()) {
+ 					this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, 4);
+ 				}
+ 			}

> DELETE  523  @  514 : 515

> EOF
