
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 7  @  4 : 9

~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  1  @  1 : 2

> CHANGE  11 : 12  @  11 : 12

~ 	protected String text = "";

> INSERT  44 : 56  @  44

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

> CHANGE  353 : 356  @  353 : 354

~ 		GlStateManager.color(0.2F, 0.2F, 1.0F, 1.0F);
~ 		GlStateManager.enableBlend();
~ 		GlStateManager.blendFunc(775, 770);

> DELETE  1  @  1 : 3

> CHANGE  6 : 7  @  6 : 7

~ 		GlStateManager.disableBlend();

> EOF
