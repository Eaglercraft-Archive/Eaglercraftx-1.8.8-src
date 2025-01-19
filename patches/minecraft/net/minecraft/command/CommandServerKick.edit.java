
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  3  @  3 : 8

> INSERT  3 : 4  @  3

+ import net.minecraft.util.StringUtils;

> INSERT  2 : 3  @  2

+ 

> INSERT  23 : 27  @  23

+ 					if (MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
+ 							.getBoolean("colorCodes")) {
+ 						s = StringUtils.translateControlCodesAlternate(s);
+ 					}

> EOF
