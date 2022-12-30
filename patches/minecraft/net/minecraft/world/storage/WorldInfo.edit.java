
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 4  @  3

+ 

> DELETE  3  @  2 : 3

> INSERT  6 : 8  @  7

+ import net.lax1dude.eaglercraft.v1_8.HString;
+ 

> CHANGE  239 : 240  @  237 : 238

~ 		// nbt.setLong("LastPlayed", MinecraftServer.getCurrentTimeMillis());

> CHANGE  302 : 303  @  302 : 303

~ 				return HString.format("ID %02d - %s, ver %d. Features enabled: %b",

> CHANGE  20 : 22  @  20 : 22

~ 				return HString.format("%d game time, %d day time", new Object[] {
~ 						Long.valueOf(WorldInfo.this.totalTime), Long.valueOf(WorldInfo.this.worldTime) });

> CHANGE  25 : 26  @  25 : 26

~ 				return HString.format("0x%05X - %s", new Object[] { Integer.valueOf(WorldInfo.this.saveVersion), s });

> CHANGE  5 : 6  @  5 : 6

~ 				return HString.format("Rain time: %d (now: %b), thunder time: %d (now: %b)",

> CHANGE  8 : 9  @  8 : 9

~ 				return HString.format("Game mode: %s (ID %d). Hardcore: %b. Cheats: %b", new Object[] {

> EOF
