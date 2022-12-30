
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  4  @  3 : 5

> DELETE  1  @  3 : 4

> DELETE  1  @  2 : 3

> DELETE  10  @  11 : 12

> DELETE  4  @  5 : 7

> CHANGE  22 : 25  @  24 : 34

~ 			ScaledResolution scaledresolution = new ScaledResolution(this.mc);
~ 			GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(),
~ 					scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);

> INSERT  22 : 36  @  29

+ 	public void eaglerShow(String line1, String line2) {
+ 		if (!this.mc.running) {
+ 			if (!this.field_73724_e) {
+ 				throw new MinecraftError();
+ 			}
+ 		} else {
+ 			this.systemTime = 0L;
+ 			this.currentlyDisplayedText = line1;
+ 			this.message = line2;
+ 			this.setLoadingProgress(-1);
+ 			this.systemTime = 0L;
+ 		}
+ 	}
+ 

> CHANGE  27 : 28  @  13 : 20

~ 				GlStateManager.clear(256);

> CHANGE  8 : 10  @  14 : 17

~ 				GlStateManager.clear(16640);
~ 				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

> DELETE  46  @  47 : 52

> EOF
