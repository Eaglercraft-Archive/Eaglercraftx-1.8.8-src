
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  11 : 12  @  11

+ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;

> DELETE  6  @  6 : 7

> CHANGE  7 : 10  @  7 : 9

~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  9 : 10  @  9 : 10

~ 	private List<Chunk> loadedChunks = Lists.newLinkedList();

> INSERT  48 : 49  @  48

+ 						++EaglerMinecraftServer.counterChunkGenerate;

> CHANGE  5 : 6  @  5 : 6

~ 								HString.format("%d,%d", new Object[] { Integer.valueOf(i), Integer.valueOf(j) }));

> INSERT  5 : 7  @  5

+ 			} else {
+ 				++EaglerMinecraftServer.counterChunkRead;

> CHANGE  32 : 34  @  32 : 33

~ 				logger.error("Couldn\'t load chunk");
~ 				logger.error(exception);

> CHANGE  10 : 12  @  10 : 11

~ 				logger.error("Couldn\'t save entities");
~ 				logger.error(exception);

> INSERT  10 : 11  @  10

+ 				++EaglerMinecraftServer.counterChunkWrite;

> CHANGE  1 : 3  @  1 : 5

~ 				logger.error("Couldn\'t save chunk");
~ 				logger.error(ioexception);

> DELETE  1  @  1 : 2

> CHANGE  29 : 30  @  29 : 30

~ 		for (int j = 0, l = arraylist.size(); j < l; ++j) {

> EOF
