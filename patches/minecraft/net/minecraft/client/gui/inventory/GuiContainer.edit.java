
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 4

~ import java.util.List;

> INSERT  1 : 10  @  1

+ 
+ import com.google.common.collect.Sets;
+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.Keyboard;
+ import net.lax1dude.eaglercraft.v1_8.Touch;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.OpenGlHelper;

> DELETE  2  @  2 : 4

> DELETE  1  @  1 : 2

> DELETE  8  @  8 : 9

> INSERT  39 : 43  @  39

+ 		if (primaryTouchPoint != -1 && Touch.fetchPointIdx(primaryTouchPoint) == -1) {
+ 			primaryTouchPoint = -1;
+ 			mouseReleased(lastTouchX, lastTouchY, 0);
+ 		}

> CHANGE  30 : 31  @  30 : 31

~ 			if (!this.mc.gameSettings.touchscreen && slot.canBeHovered() && this.isMouseOverSlot(slot, i, j)) {

> INSERT  11 : 12  @  11

+ 			GlStateManager.enableAlpha();

> DELETE  21  @  21 : 22

> CHANGE  18 : 20  @  18 : 19

~ 		if (!this.mc.gameSettings.touchscreen && inventoryplayer.getItemStack() == null && this.theSlot != null
~ 				&& this.theSlot.getHasStack()) {

> CHANGE  66 : 67  @  66 : 67

~ 				EaglerTextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite(s1);

> CHANGE  56 : 57  @  56 : 57

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  20 : 24  @  20 : 24

~ //			if (this.mc.gameSettings.touchscreen && flag1 && this.mc.thePlayer.inventory.getItemStack() == null) {
~ //				this.mc.displayGuiScreen((GuiScreen) null);
~ //				return;
~ //			}

> CHANGE  102 : 105  @  102 : 103

~ 					List<Slot> lst = this.inventorySlots.inventorySlots;
~ 					for (int n = 0, m = lst.size(); n < m; ++n) {
~ 						Slot slot2 = lst.get(n);

> CHANGE  108 : 112  @  108 : 110

~ 	protected void keyTyped(char parChar1, int parInt1) {
~ 		if (parInt1 == this.mc.gameSettings.keyBindClose.getKeyCode()
~ 				|| parInt1 == this.mc.gameSettings.keyBindInventory.getKeyCode() || (parInt1 == 1
~ 						&& (this.mc.gameSettings.keyBindClose.getKeyCode() == 0 || Keyboard.areKeysLocked()))) {

> CHANGE  1 : 3  @  1 : 9

~ 			if (this.mc.currentScreen == null) {
~ 				this.mc.setIngameFocus();

> INSERT  1 : 12  @  1

+ 		} else if (parInt1 == 1) {
+ 			showingCloseKey = EagRuntime.steadyTimeMillis();
+ 		} else {
+ 			this.checkHotbarKeys(parInt1);
+ 			if (this.theSlot != null && this.theSlot.getHasStack()) {
+ 				if (parInt1 == this.mc.gameSettings.keyBindPickBlock.getKeyCode()) {
+ 					this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, 0, 3);
+ 				} else if (parInt1 == this.mc.gameSettings.keyBindDrop.getKeyCode()) {
+ 					this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, 4);
+ 				}
+ 			}

> DELETE  1  @  1 : 2

> INSERT  29 : 30  @  29

+ 			return;

> INSERT  1 : 6  @  1

+ 		if (primaryTouchPoint != -1 && Touch.fetchPointIdx(primaryTouchPoint) == -1) {
+ 			primaryTouchPoint = -1;
+ 			mouseReleased(lastTouchX, lastTouchY, 0);
+ 		}
+ 	}

> INSERT  1 : 3  @  1

+ 	protected float getTouchModeScale() {
+ 		return 1.25f;

> INSERT  1 : 44  @  1

+ 
+ 	private int primaryTouchPoint = -1;
+ 	private int lastTouchX = -1;
+ 	private int lastTouchY = -1;
+ 
+ 	protected void touchStarted(int touchX, int touchY, int uid) {
+ 		if (primaryTouchPoint == -1) {
+ 			primaryTouchPoint = uid;
+ 			lastTouchX = touchX;
+ 			lastTouchY = touchY;
+ 			mouseClicked(touchX, touchY, 0);
+ 		}
+ 	}
+ 
+ 	protected void touchMoved(int touchX, int touchY, int uid) {
+ 		if (primaryTouchPoint == uid) {
+ 			lastTouchX = touchX;
+ 			lastTouchY = touchY;
+ 			mouseClickMove(touchX, touchY, 0, 0l);
+ 		}
+ 	}
+ 
+ 	protected void touchEndMove(int touchX, int touchY, int uid) {
+ 		if (primaryTouchPoint == uid) {
+ 			primaryTouchPoint = -1;
+ 			lastTouchX = touchX;
+ 			lastTouchY = touchY;
+ 			mouseReleased(touchX, touchY, 0);
+ 		}
+ 	}
+ 
+ 	protected void touchTapped(int touchX, int touchY, int uid) {
+ 		if (primaryTouchPoint == uid) {
+ 			primaryTouchPoint = -1;
+ 			lastTouchX = touchX;
+ 			lastTouchY = touchY;
+ 			mouseReleased(touchX, touchY, 0);
+ 		}
+ 	}
+ 
+ 	protected boolean shouldTouchGenerateMouseEvents() {
+ 		return false;
+ 	}

> EOF
