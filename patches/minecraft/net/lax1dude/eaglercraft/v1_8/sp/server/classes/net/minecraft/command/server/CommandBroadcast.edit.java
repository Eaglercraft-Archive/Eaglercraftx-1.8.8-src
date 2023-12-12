
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  9 : 10  @  9

+ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.ChatComponentText;

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.StringUtils;

> INSERT  24 : 29  @  24

+ 			if (MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
+ 					.getBoolean("colorCodes")) {
+ 				ichatcomponent = new ChatComponentText(
+ 						StringUtils.translateControlCodesAlternate(ichatcomponent.getFormattedText()));
+ 			}

> EOF
