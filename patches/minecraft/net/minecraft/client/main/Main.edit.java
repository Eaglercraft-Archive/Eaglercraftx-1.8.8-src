
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 17

~ import net.lax1dude.eaglercraft.v1_8.internal.PlatformRuntime;

> DELETE  1  @  1 : 2

> CHANGE  3 : 5  @  3 : 100

~ 	public static void appMain() {
~ 		System.setProperty("java.net.preferIPv6Addresses", "true");

> CHANGE  1 : 5  @  1 : 12

~ 				new GameConfiguration.UserInformation(new Session()),
~ 				new GameConfiguration.DisplayInformation(854, 480, false, true),
~ 				new GameConfiguration.GameInformation(false, "1.8.8"));
~ 		PlatformRuntime.setThreadName("Client thread");

> DELETE  2  @  2 : 6

> EOF
