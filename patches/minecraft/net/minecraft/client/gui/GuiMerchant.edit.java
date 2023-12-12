
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 8  @  2 : 4

~ import net.lax1dude.eaglercraft.v1_8.netty.Unpooled;
~ import net.lax1dude.eaglercraft.v1_8.Mouse;
~ import net.lax1dude.eaglercraft.v1_8.internal.EnumCursorType;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 2

> DELETE  13  @  13 : 15

> CHANGE  44 : 45  @  44 : 45

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  118 : 121  @  118

+ 				if (flag && this.enabled) {
+ 					Mouse.showCursor(EnumCursorType.HAND);
+ 				}

> EOF
