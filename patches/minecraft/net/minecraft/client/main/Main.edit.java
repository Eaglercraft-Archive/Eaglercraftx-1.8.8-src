
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 17

~ import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;

> DELETE  2  @  16 : 17

> CHANGE  3 : 4  @  4 : 5

~ 	public static void appMain(String[] astring) {

> DELETE  2  @  2 : 46

> DELETE  1  @  45 : 95

> CHANGE  1 : 5  @  51 : 62

~ 				new GameConfiguration.UserInformation(new Session()),
~ 				new GameConfiguration.DisplayInformation(854, 480, false, true),
~ 				new GameConfiguration.GameInformation(false, "1.8.8"));
~ 		PlatformRuntime.setThreadName("Client thread");

> DELETE  6  @  13 : 17

> EOF
