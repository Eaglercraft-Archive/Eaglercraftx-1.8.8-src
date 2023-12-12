
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  13 : 14  @  13

+ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.ChatComponentText;

> INSERT  3 : 4  @  3

+ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.util.StringUtils;

> INSERT  35 : 40  @  35

+ 				if (MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
+ 						.getBoolean("colorCodes")) {
+ 					ichatcomponent = new ChatComponentText(
+ 							StringUtils.translateControlCodesAlternate(ichatcomponent.getFormattedText()));
+ 				}

> EOF
