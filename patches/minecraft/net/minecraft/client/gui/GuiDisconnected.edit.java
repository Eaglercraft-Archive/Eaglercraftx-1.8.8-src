
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 2  @  1 : 3

~ 

> INSERT  1 : 2  @  1

+ import net.minecraft.util.ChatComponentTranslation;

> CHANGE  15 : 16  @  15 : 16

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  12 : 13  @  12 : 13

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  20 : 24  @  20

+ 
+ 	public static GuiScreen createRateLimitKick(GuiScreen prev) {
+ 		return new GuiDisconnected(prev, "connect.failed", new ChatComponentTranslation("disconnect.tooManyRequests"));
+ 	}

> EOF
