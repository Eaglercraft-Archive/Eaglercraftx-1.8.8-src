
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 4  @  1 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;

> DELETE  7  @  7 : 8

> CHANGE  82 : 84  @  82 : 84

~ 		for (int i = 0; i < ChatAllowedCharacters.allowedCharactersArray.length; ++i) {
~ 			this.field_146336_i = this.field_146336_i.replace(ChatAllowedCharacters.allowedCharactersArray[i], '_');

> CHANGE  48 : 50  @  48 : 50

~ 		for (int i = 0; i < disallowedFilenames.length; ++i) {
~ 			if (parString1.equalsIgnoreCase(disallowedFilenames[i])) {

> CHANGE  15 : 16  @  15 : 16

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  165 : 166  @  165 : 166

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  16 : 17  @  16 : 17

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  16 : 19  @  16 : 18

~ 			this.drawString(this.fontRendererObj, I18n.format(
~ 					StringUtils.isNotEmpty(field_146335_h.text) ? "createWorld.seedNote" : "selectWorld.seedInfo",
~ 					new Object[0]), this.width / 2 - 100, 85, -6250336);

> INSERT  47 : 59  @  47

+ 
+ 	@Override
+ 	public boolean showCopyPasteButtons() {
+ 		return field_146333_g.isFocused() || field_146335_h.isFocused();
+ 	}
+ 
+ 	@Override
+ 	public void fireInputEvent(EnumInputEvent event, String param) {
+ 		field_146333_g.fireInputEvent(event, param);
+ 		field_146335_h.fireInputEvent(event, param);
+ 	}
+ 

> EOF
