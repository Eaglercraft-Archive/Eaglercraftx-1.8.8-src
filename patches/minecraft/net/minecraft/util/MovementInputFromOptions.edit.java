
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import net.lax1dude.eaglercraft.v1_8.touch_gui.EnumTouchControl;
+ import net.lax1dude.eaglercraft.v1_8.touch_gui.TouchControls;

> DELETE  1  @  1 : 2

> CHANGE  11 : 14  @  11 : 12

~ 		if (this.gameSettings.keyBindForward.isKeyDown() || TouchControls.isPressed(EnumTouchControl.DPAD_UP)
~ 				|| TouchControls.isPressed(EnumTouchControl.DPAD_UP_LEFT)
~ 				|| TouchControls.isPressed(EnumTouchControl.DPAD_UP_RIGHT)) {

> CHANGE  3 : 4  @  3 : 4

~ 		if (this.gameSettings.keyBindBack.isKeyDown() || TouchControls.isPressed(EnumTouchControl.DPAD_DOWN)) {

> CHANGE  3 : 5  @  3 : 4

~ 		if (this.gameSettings.keyBindLeft.isKeyDown() || TouchControls.isPressed(EnumTouchControl.DPAD_LEFT)
~ 				|| TouchControls.isPressed(EnumTouchControl.DPAD_UP_LEFT)) {

> CHANGE  3 : 5  @  3 : 4

~ 		if (this.gameSettings.keyBindRight.isKeyDown() || TouchControls.isPressed(EnumTouchControl.DPAD_RIGHT)
~ 				|| TouchControls.isPressed(EnumTouchControl.DPAD_UP_RIGHT)) {

> CHANGE  3 : 7  @  3 : 5

~ 		this.jump = this.gameSettings.keyBindJump.isKeyDown() || TouchControls.isPressed(EnumTouchControl.JUMP)
~ 				|| TouchControls.isPressed(EnumTouchControl.FLY_UP);
~ 		this.sneak = this.gameSettings.keyBindSneak.isKeyDown() || TouchControls.getSneakToggled()
~ 				|| TouchControls.isPressed(EnumTouchControl.FLY_DOWN);

> EOF
