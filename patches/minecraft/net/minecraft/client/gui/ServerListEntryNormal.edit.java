
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 10

> CHANGE  1 : 5  @  9 : 11

~ 
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  5  @  3 : 7

> CHANGE  1 : 2  @  5 : 8

~ import net.minecraft.client.resources.I18n;

> DELETE  3  @  5 : 8

> DELETE  3  @  6 : 8

> DELETE  6  @  8 : 9

> DELETE  1  @  2 : 3

> DELETE  6  @  7 : 9

> DELETE  8  @  10 : 27

> CHANGE  8 : 18  @  25 : 28

~ 		for (int k1 = 0; k1 < 2; ++k1) {
~ 			if (k1 < list.size()) {
~ 				this.mc.fontRendererObj.drawString((String) list.get(k1), j + 32 + 3,
~ 						k + 12 + this.mc.fontRendererObj.FONT_HEIGHT * k1, 8421504);
~ 			} else if (k1 == 1) {
~ 				this.mc.fontRendererObj.drawString(
~ 						this.field_148301_e.hideAddress ? I18n.format("selectServer.hiddenAddress", new Object[0])
~ 								: this.field_148301_e.serverIP,
~ 						j + 32 + 3, k + 12 + this.mc.fontRendererObj.FONT_HEIGHT * k1 + k1, 0x444444);
~ 			}

> CHANGE  59 : 63  @  52 : 57

~ 		if (this.mc.gameSettings.touchscreen || flag) {
~ 			GlStateManager.enableShaderBlendAdd();
~ 			GlStateManager.setShaderBlendSrc(0.6f, 0.6f, 0.6f, 1.0f);
~ 			GlStateManager.setShaderBlendAdd(0.3f, 0.3f, 0.3f, 0.0f);

> CHANGE  5 : 7  @  6 : 9

~ 		if (field_148301_e.iconTextureObject != null) {
~ 			this.func_178012_a(j, k, field_148301_e.iconResourceLocation);

> INSERT  5 : 8  @  6

+ 		if (this.mc.gameSettings.touchscreen || flag) {
+ 			GlStateManager.disableShaderBlendAdd();
+ 		}

> CHANGE  14 : 15  @  11 : 12

~ 			// Gui.drawRect(j, k, j + 32, k + 32, -1601138544);

> INSERT  34 : 35  @  34

+ 		GlStateManager.blendFunc(770, 771);

> DELETE  9  @  8 : 47

> EOF
