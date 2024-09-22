
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  2  @  2 : 4

> DELETE  1  @  1 : 2

> CHANGE  1 : 2  @  1 : 2

~ import net.minecraft.client.resources.I18n;

> DELETE  9  @  9 : 11

> DELETE  3  @  3 : 6

> CHANGE  22 : 24  @  22 : 32

~ 			GlStateManager.ortho(0.0D, mc.scaledResolution.getScaledWidth_double(),
~ 					mc.scaledResolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);

> INSERT  19 : 37  @  19

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
+ 	public void eaglerShowRefreshResources() {
+ 		eaglerShow(I18n.format("resourcePack.load.refreshing"), I18n.format("resourcePack.load.pleaseWait"));
+ 	}
+ 

> CHANGE  9 : 10  @  9 : 10

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

> EOF
