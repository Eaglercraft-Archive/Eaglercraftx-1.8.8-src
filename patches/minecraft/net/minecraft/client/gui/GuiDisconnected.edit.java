
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  3 : 4  @  4 : 6

~ 

> INSERT  5 : 6  @  7

+ import net.minecraft.util.ChatComponentTranslation;

> CHANGE  21 : 22  @  22 : 23

~ 	protected void keyTyped(char parChar1, int parInt1) {

> CHANGE  34 : 35  @  35 : 36

~ 	protected void actionPerformed(GuiButton parGuiButton) {

> INSERT  55 : 59  @  56

+ 
+ 	public static GuiScreen createRateLimitKick(GuiScreen prev) {
+ 		return new GuiDisconnected(prev, "connect.failed", new ChatComponentTranslation("disconnect.tooManyRequests"));
+ 	}

> EOF
