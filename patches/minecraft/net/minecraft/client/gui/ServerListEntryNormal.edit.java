
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 10

> CHANGE  3 : 7  @  11 : 13

~ 
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  8  @  14 : 18

> CHANGE  9 : 10  @  19 : 22

~ import net.minecraft.client.resources.I18n;

> DELETE  12  @  24 : 27

> DELETE  15  @  30 : 32

> DELETE  21  @  38 : 39

> DELETE  22  @  40 : 41

> DELETE  28  @  47 : 49

> DELETE  36  @  57 : 74

> CHANGE  44 : 54  @  82 : 85

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

> CHANGE  103 : 107  @  134 : 139

~ 		if (this.mc.gameSettings.touchscreen || flag) {
~ 			GlStateManager.enableShaderBlendAdd();
~ 			GlStateManager.setShaderBlendSrc(0.6f, 0.6f, 0.6f, 1.0f);
~ 			GlStateManager.setShaderBlendAdd(0.3f, 0.3f, 0.3f, 0.0f);

> CHANGE  108 : 110  @  140 : 143

~ 		if (field_148301_e.iconTextureObject != null) {
~ 			this.func_178012_a(j, k, field_148301_e.iconResourceLocation);

> INSERT  113 : 116  @  146

+ 		if (this.mc.gameSettings.touchscreen || flag) {
+ 			GlStateManager.disableShaderBlendAdd();
+ 		}

> CHANGE  127 : 128  @  157 : 158

~ 			// Gui.drawRect(j, k, j + 32, k + 32, -1601138544);

> INSERT  161 : 162  @  191

+ 		GlStateManager.blendFunc(770, 771);

> DELETE  170  @  199 : 238

> EOF
