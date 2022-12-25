
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 17

~ import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;

> DELETE  4  @  18 : 19

> CHANGE  7 : 8  @  22 : 23

~ 	public static void appMain(String[] astring) {

> DELETE  9  @  24 : 68

> DELETE  10  @  69 : 119

> CHANGE  11 : 15  @  120 : 131

~ 				new GameConfiguration.UserInformation(new Session()),
~ 				new GameConfiguration.DisplayInformation(854, 480, false, true),
~ 				new GameConfiguration.GameInformation(false, "1.8.8"));
~ 		PlatformRuntime.setThreadName("Client thread");

> DELETE  17  @  133 : 137

> EOF
