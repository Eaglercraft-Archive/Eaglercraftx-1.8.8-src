
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  9 : 10  @  9

+ import net.minecraft.util.ChatComponentText;

> INSERT  2 : 3  @  2

+ import net.minecraft.util.StringUtils;

> INSERT  2 : 3  @  2

+ 

> INSERT  15 : 20  @  15

+ 			if (MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
+ 					.getBoolean("colorCodes")) {
+ 				ichatcomponent = new ChatComponentText(
+ 						StringUtils.translateControlCodesAlternate(ichatcomponent.getFormattedText()));
+ 			}

> EOF
