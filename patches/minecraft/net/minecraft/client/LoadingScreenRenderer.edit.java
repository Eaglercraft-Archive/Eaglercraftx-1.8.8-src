
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  2  @  2 : 4

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 2

> DELETE  10  @  10 : 11

> DELETE  4  @  4 : 6

> CHANGE  22 : 25  @  22 : 32

~ 			ScaledResolution scaledresolution = new ScaledResolution(this.mc);
~ 			GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(),
~ 					scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);

> INSERT  19 : 33  @  19

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

> CHANGE  13 : 14  @  13 : 20

~ 				GlStateManager.clear(256);

> CHANGE  7 : 9  @  7 : 10

~ 				GlStateManager.clear(16640);
~ 				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

> DELETE  44  @  44 : 49

> EOF
