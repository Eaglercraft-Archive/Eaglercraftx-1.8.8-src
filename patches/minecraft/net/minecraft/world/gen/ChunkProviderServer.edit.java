
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  11 : 12  @  11

+ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;

> CHANGE  14 : 17  @  14 : 16

~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> INSERT  2 : 3  @  2

+ 

> INSERT  56 : 57  @  56

+ 						++EaglerMinecraftServer.counterChunkGenerate;

> CHANGE  5 : 6  @  5 : 6

~ 								HString.format("%d,%d", new Object[] { Integer.valueOf(i), Integer.valueOf(j) }));

> INSERT  5 : 7  @  5

+ 			} else {
+ 				++EaglerMinecraftServer.counterChunkRead;

> INSERT  54 : 55  @  54

+ 				++EaglerMinecraftServer.counterChunkWrite;

> EOF
