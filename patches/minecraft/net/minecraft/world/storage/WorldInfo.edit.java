
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 4  @  3

+ 

> DELETE  6  @  5 : 6

> INSERT  12 : 14  @  12

+ import net.lax1dude.eaglercraft.v1_8.HString;
+ 

> CHANGE  251 : 252  @  249 : 250

~ 		// nbt.setLong("LastPlayed", MinecraftServer.getCurrentTimeMillis());

> CHANGE  553 : 554  @  551 : 552

~ 				return HString.format("ID %02d - %s, ver %d. Features enabled: %b",

> CHANGE  573 : 575  @  571 : 573

~ 				return HString.format("%d game time, %d day time", new Object[] {
~ 						Long.valueOf(WorldInfo.this.totalTime), Long.valueOf(WorldInfo.this.worldTime) });

> CHANGE  598 : 599  @  596 : 597

~ 				return HString.format("0x%05X - %s", new Object[] { Integer.valueOf(WorldInfo.this.saveVersion), s });

> CHANGE  603 : 604  @  601 : 602

~ 				return HString.format("Rain time: %d (now: %b), thunder time: %d (now: %b)",

> CHANGE  611 : 612  @  609 : 610

~ 				return HString.format("Game mode: %s (ID %d). Hardcore: %b. Cheats: %b", new Object[] {

> EOF
