
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 7  @  4 : 9

~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  4  @  6 : 7

> CHANGE  11 : 12  @  12 : 13

~ 	protected String text = "";

> INSERT  45 : 57  @  45

+ 	public void updateText(String parString1) {
+ 		if (this.field_175209_y.apply(parString1)) {
+ 			if (parString1.length() > this.maxStringLength) {
+ 				this.text = parString1.substring(0, this.maxStringLength);
+ 			} else {
+ 				this.text = parString1;
+ 			}
+ 
+ 			this.setCursorPosition(cursorPosition);
+ 		}
+ 	}
+ 

> CHANGE  365 : 368  @  353 : 354

~ 		GlStateManager.color(0.2F, 0.2F, 1.0F, 1.0F);
~ 		GlStateManager.enableBlend();
~ 		GlStateManager.blendFunc(775, 770);

> DELETE  4  @  2 : 4

> CHANGE  6 : 7  @  8 : 9

~ 		GlStateManager.disableBlend();

> EOF
