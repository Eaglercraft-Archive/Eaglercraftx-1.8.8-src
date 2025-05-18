
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  2  @  2 : 4

> DELETE  1  @  1 : 2

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.client.resources.I18n;

> DELETE  1  @  1 : 2

> DELETE  7  @  7 : 9

> DELETE  3  @  3 : 6

> CHANGE  14 : 15  @  14 : 19

~ 		if (this.mc.running) {

> CHANGE  3 : 5  @  3 : 13

~ 			GlStateManager.ortho(0.0D, mc.scaledResolution.getScaledWidth_double(),
~ 					mc.scaledResolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);

> CHANGE  7 : 8  @  7 : 12

~ 		if (this.mc.running) {

> INSERT  7 : 21  @  7

+ 	public void eaglerShow(String line1, String line2) {
+ 		if (this.mc.running) {
+ 			this.systemTime = 0L;
+ 			this.currentlyDisplayedText = line1;
+ 			this.message = line2;
+ 			this.setLoadingProgress(-1);
+ 			this.systemTime = 0L;
+ 		}
+ 	}
+ 
+ 	public void eaglerShowRefreshResources() {
+ 		eaglerShow(I18n.format("resourcePack.load.refreshing"), I18n.format("resourcePack.load.pleaseWait"));
+ 	}
+ 

> CHANGE  1 : 2  @  1 : 6

~ 		if (this.mc.running) {

> CHANGE  3 : 4  @  3 : 4

~ 				ScaledResolution scaledresolution = mc.scaledResolution;

> CHANGE  3 : 4  @  3 : 10

~ 				GlStateManager.clear(256);

> CHANGE  7 : 9  @  7 : 10

~ 				GlStateManager.clear(16640);
~ 				GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

> CHANGE  41 : 45  @  41 : 47

~ 				if (this.message != null) {
~ 					this.mc.fontRendererObj.drawStringWithShadow(this.message,
~ 							(float) ((k - this.mc.fontRendererObj.getStringWidth(this.message)) / 2),
~ 							(float) (l / 2 - 4 + 8), 16777215);

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 8

> EOF
