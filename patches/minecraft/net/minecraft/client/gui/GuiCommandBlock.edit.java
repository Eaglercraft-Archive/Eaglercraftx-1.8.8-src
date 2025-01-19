
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 7  @  2 : 7

~ import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;

> DELETE  5  @  5 : 8

> CHANGE  44 : 45  @  44 : 45

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  24 : 25  @  24 : 25

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  13 : 14  @  13 : 14

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> INSERT  46 : 62  @  46

+ 
+ 	public boolean blockPTTKey() {
+ 		return commandTextField.isFocused() || previousOutputTextField.isFocused();
+ 	}
+ 
+ 	@Override
+ 	public boolean showCopyPasteButtons() {
+ 		return commandTextField.isFocused() || previousOutputTextField.isFocused();
+ 	}
+ 
+ 	@Override
+ 	public void fireInputEvent(EnumInputEvent event, String param) {
+ 		commandTextField.fireInputEvent(event, param);
+ 		previousOutputTextField.fireInputEvent(event, param);
+ 	}
+ 

> EOF
