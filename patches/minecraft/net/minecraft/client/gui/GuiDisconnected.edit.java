
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 2  @  2 : 4

~ 

> INSERT  2 : 3  @  3

+ import net.minecraft.util.ChatComponentTranslation;

> CHANGE  16 : 17  @  15 : 16

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  13 : 14  @  13 : 14

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  21 : 25  @  21

+ 
+ 	public static GuiScreen createRateLimitKick(GuiScreen prev) {
+ 		return new GuiDisconnected(prev, "connect.failed", new ChatComponentTranslation("disconnect.tooManyRequests"));
+ 	}

> EOF
