
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 6  @  3

+ 
+ import net.lax1dude.eaglercraft.v1_8.ArrayUtils;
+ import net.lax1dude.eaglercraft.v1_8.PointerInputAbstraction;

> DELETE  1  @  1 : 4

> DELETE  4  @  4 : 5

> CHANGE  8 : 9  @  8 : 9

~ 		super(mcIn, controls.width, controls.height, 66, controls.height - 32, 20);

> CHANGE  8 : 10  @  8 : 9

~ 		for (int l = 0; l < akeybinding.length; ++l) {
~ 			KeyBinding keybinding = akeybinding[l];

> CHANGE  86 : 89  @  86 : 87

~ 				KeyBinding[] kb = GuiKeyBindingList.this.mc.gameSettings.keyBindings;
~ 				for (int m = 0; m < kb.length; ++m) {
~ 					KeyBinding keybindingx = kb[m];

> CHANGE  19 : 24  @  19 : 20

~ 			if (var4 != 0 && var4 != 12345)
~ 				return false;
~ 			boolean touchMode = PointerInputAbstraction.isTouchMode();
~ 			if ((!touchMode || (this.btnChangeKeyBinding.isSliderTouchEvents() == (var4 == 12345)))
~ 					&& this.btnChangeKeyBinding.mousePressed(GuiKeyBindingList.this.mc, i, j)) {

> CHANGE  2 : 4  @  2 : 3

~ 			} else if ((!touchMode || (this.btnReset.isSliderTouchEvents() == (var4 == 12345)))
~ 					&& this.btnReset.mousePressed(GuiKeyBindingList.this.mc, i, j)) {

> CHANGE  10 : 17  @  10 : 12

~ 			if (var4 != 0 && var4 != 12345)
~ 				return;
~ 			boolean touchMode = PointerInputAbstraction.isTouchMode();
~ 			if (!touchMode || (this.btnChangeKeyBinding.isSliderTouchEvents() == (var4 == 12345)))
~ 				this.btnChangeKeyBinding.mouseReleased(i, j);
~ 			if (!touchMode || (this.btnReset.isSliderTouchEvents() == (var4 == 12345)))
~ 				this.btnReset.mouseReleased(i, j);

> EOF
