
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  1  @  1 : 3

> CHANGE  4 : 5  @  4 : 5

~ 	public float sliderValue;

> INSERT  21 : 25  @  21

+ 	public GameSettings.Options getEnumOptions() {
+ 		return options;
+ 	}
+ 

> INSERT  40 : 44  @  40

+ 
+ 	public boolean isSliderTouchEvents() {
+ 		return true;
+ 	}

> EOF
