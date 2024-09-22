
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  7 : 12  @  7

+ 
+ import net.lax1dude.eaglercraft.v1_8.Keyboard;
+ import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  1  @  1 : 8

> DELETE  2  @  2 : 3

> DELETE  9  @  9 : 10

> INSERT  37 : 42  @  37

+ 	public void handleTouchInput() throws IOException {
+ 		super.handleTouchInput();
+ 		this.field_146435_s.handleTouchInput();
+ 	}
+ 

> CHANGE  4 : 5  @  4 : 5

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  4 : 5  @  4 : 5

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  6 : 7  @  6 : 7

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  55 : 57  @  55 : 57

~ 			for (int i = 0, l = parList.size(); i < l; ++i) {
~ 				flatgeneratorinfo.getWorldFeatures().put(parList.get(i), Maps.newHashMap());

> INSERT  132 : 143  @  132

+ 
+ 	@Override
+ 	public boolean showCopyPasteButtons() {
+ 		return field_146433_u.isFocused();
+ 	}
+ 
+ 	@Override
+ 	public void fireInputEvent(EnumInputEvent event, String param) {
+ 		field_146433_u.fireInputEvent(event, param);
+ 	}
+ 

> EOF
