
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 6  @  3

+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  4  @  4 : 6

> CHANGE  19 : 20  @  19 : 20

~ 		Calendar calendar = EagRuntime.getLocaleCalendar();

> DELETE  36  @  36 : 38

> INSERT  2 : 4  @  2

+ 				} else if (tileentitychest.getChestType() == 1) {
+ 					this.bindTexture(textureTrapped);

> DELETE  12  @  12 : 14

> INSERT  2 : 4  @  2

+ 				} else if (tileentitychest.getChestType() == 1) {
+ 					this.bindTexture(textureTrappedDouble);

> EOF
