
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 4  @  3

+ 

> DELETE  1  @  1 : 6

> CHANGE  6 : 8  @  6 : 7

~ 			GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY,
~ 			GameSettings.Options.EAGLER_TOUCH_CONTROL_OPACITY };

> CHANGE  42 : 48  @  42 : 43

~ 	public void handleTouchInput() throws IOException {
~ 		super.handleTouchInput();
~ 		this.keyBindingList.handleTouchInput();
~ 	}
~ 
~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  3 : 6  @  3 : 5

~ 			KeyBinding[] arr = this.mc.gameSettings.keyBindings;
~ 			for (int i = 0; i < arr.length; ++i) {
~ 				arr[i].setKeyCode(arr[i].getKeyCodeDefault());

> CHANGE  11 : 12  @  11 : 12

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  17 : 18  @  17 : 18

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  24 : 27  @  24 : 26

~ 		KeyBinding[] arr = this.options.keyBindings;
~ 		for (int k = 0; k < arr.length; ++k) {
~ 			if (arr[k].getKeyCode() != arr[k].getKeyCodeDefault()) {

> EOF
