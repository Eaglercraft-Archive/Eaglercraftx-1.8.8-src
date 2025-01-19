
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  4  @  4 : 7

> CHANGE  24 : 27  @  24 : 25

~ 		SoundCategory[] cats = SoundCategory._VALUES;
~ 		for (int j = 0; j < cats.length; ++j) {
~ 			SoundCategory soundcategory = cats[j];

> CHANGE  12 : 13  @  12 : 13

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  90 : 94  @  90

+ 
+ 		public boolean isSliderTouchEvents() {
+ 			return true;
+ 		}

> EOF
