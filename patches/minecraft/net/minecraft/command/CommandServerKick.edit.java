
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  11 : 12  @  11

+ import net.minecraft.util.StringUtils;

> INSERT  2 : 3  @  2

+ 

> INSERT  23 : 27  @  23

+ 					if (MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
+ 							.getBoolean("colorCodes")) {
+ 						s = StringUtils.translateControlCodesAlternate(s);
+ 					}

> EOF
