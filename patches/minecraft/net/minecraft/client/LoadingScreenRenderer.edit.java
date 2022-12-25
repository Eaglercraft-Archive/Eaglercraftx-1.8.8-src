
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  6  @  5 : 7

> DELETE  7  @  8 : 9

> DELETE  8  @  10 : 11

> DELETE  18  @  21 : 22

> DELETE  22  @  26 : 28

> CHANGE  44 : 47  @  50 : 60

~ 			ScaledResolution scaledresolution = new ScaledResolution(this.mc);
~ 			GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(),
~ 					scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);

> INSERT  66 : 80  @  79

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

> CHANGE  93 : 94  @  92 : 99

~ 				GlStateManager.clear(256);

> CHANGE  101 : 103  @  106 : 109

~ 				GlStateManager.clear(16640);
~ 				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

> DELETE  147  @  153 : 158

> EOF
