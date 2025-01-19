
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  5 : 10  @  5 : 11

~ 
~ import net.lax1dude.eaglercraft.v1_8.Keyboard;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.EnumInputEvent;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  1  @  1 : 2

> DELETE  4  @  4 : 5

> INSERT  37 : 42  @  37

+ 	public void handleTouchInput() throws IOException {
+ 		super.handleTouchInput();
+ 		this.field_175311_g.handleTouchInput();
+ 	}
+ 

> CHANGE  4 : 5  @  4 : 5

~ 	protected void mouseClicked(int parInt1, int parInt2, int parInt3) {

> CHANGE  4 : 5  @  4 : 5

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  6 : 7  @  6 : 7

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  35 : 45  @  35

+ 	@Override
+ 	public boolean showCopyPasteButtons() {
+ 		return field_175317_i.isFocused();
+ 	}
+ 
+ 	@Override
+ 	public void fireInputEvent(EnumInputEvent event, String param) {
+ 		field_175317_i.fireInputEvent(event, param);
+ 	}
+ 

> EOF
