
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 4  @  3

+ 

> DELETE  2  @  2 : 3

> INSERT  6 : 8  @  6

+ import net.lax1dude.eaglercraft.v1_8.HString;
+ 

> CHANGE  237 : 238  @  237 : 238

~ 		// nbt.setLong("LastPlayed", MinecraftServer.getCurrentTimeMillis());

> CHANGE  301 : 302  @  301 : 302

~ 				return HString.format("ID %02d - %s, ver %d. Features enabled: %b",

> CHANGE  19 : 21  @  19 : 21

~ 				return HString.format("%d game time, %d day time", new Object[] {
~ 						Long.valueOf(WorldInfo.this.totalTime), Long.valueOf(WorldInfo.this.worldTime) });

> CHANGE  23 : 24  @  23 : 24

~ 				return HString.format("0x%05X - %s", new Object[] { Integer.valueOf(WorldInfo.this.saveVersion), s });

> CHANGE  4 : 5  @  4 : 5

~ 				return HString.format("Rain time: %d (now: %b), thunder time: %d (now: %b)",

> CHANGE  7 : 8  @  7 : 8

~ 				return HString.format("Game mode: %s (ID %d). Hardcore: %b. Cheats: %b", new Object[] {

> EOF
