
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 8  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  3  @  3 : 5

> DELETE  12  @  12 : 14

> CHANGE  80 : 81  @  80 : 81

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> CHANGE  37 : 39  @  37 : 38

~ 		for (int k = 0, l = this.buttonList.size(); k < l; ++k) {
~ 			GuiButton guibutton = this.buttonList.get(k);

> INSERT  78 : 80  @  78

+ 			if (this.enabled)
+ 				Mouse.showCursor(EnumCursorType.HAND);

> INSERT  10 : 12  @  10

+ 			if (this.enabled)
+ 				Mouse.showCursor(EnumCursorType.HAND);

> INSERT  17 : 19  @  17

+ 			if (this.enabled)
+ 				Mouse.showCursor(EnumCursorType.HAND);

> EOF
