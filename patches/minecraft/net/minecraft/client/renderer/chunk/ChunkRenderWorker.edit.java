
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 9

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.ChunkUpdateManager;

> DELETE  5  @  9 : 13

> DELETE  2  @  6 : 8

> CHANGE  1 : 2  @  3 : 4

~ public class ChunkRenderWorker {

> CHANGE  2 : 3  @  2 : 3

~ 	private final ChunkUpdateManager chunkRenderDispatcher;

> CHANGE  3 : 4  @  3 : 4

~ 	public ChunkRenderWorker(ChunkUpdateManager parChunkRenderDispatcher) {

> CHANGE  4 : 5  @  4 : 5

~ 	public ChunkRenderWorker(ChunkUpdateManager chunkRenderDispatcherIn,

> DELETE  6  @  6 : 22

> CHANGE  1 : 5  @  17 : 27

~ 		if (generator.getStatus() != ChunkCompileTaskGenerator.Status.PENDING) {
~ 			if (!generator.isFinished()) {
~ 				LOGGER.warn("Chunk render task was " + generator.getStatus()
~ 						+ " when I expected it to be pending; ignoring task");

> CHANGE  6 : 7  @  12 : 15

~ 			return;

> INSERT  3 : 5  @  5

+ 		generator.setStatus(ChunkCompileTaskGenerator.Status.COMPILING);
+ 

> CHANGE  17 : 21  @  15 : 26

~ 			if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
~ 				if (!generator.isFinished()) {
~ 					LOGGER.warn("Chunk render task was " + generator.getStatus()
~ 							+ " when I expected it to be compiling; aborting task");

> CHANGE  6 : 8  @  13 : 16

~ 				this.freeRenderBuilder(generator);
~ 				return;

> INSERT  4 : 6  @  5

+ 			generator.setStatus(ChunkCompileTaskGenerator.Status.UPLOADING);
+ 

> CHANGE  3 : 6  @  1 : 2

~ 			if (compiledchunk == null) {
~ 				System.out.println(chunkcompiletaskgenerator$type);
~ 			}

> CHANGE  5 : 7  @  3 : 5

~ 					if (!compiledchunk.isLayerEmpty(enumworldblocklayer)) {
~ 						this.chunkRenderDispatcher.uploadChunk(enumworldblocklayer,

> CHANGE  3 : 6  @  3 : 4

~ 								generator.getRenderChunk(), compiledchunk);
~ 						generator.getRenderChunk().setCompiledChunk(compiledchunk);
~ 						generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);

> CHANGE  6 : 7  @  4 : 5

~ 				this.chunkRenderDispatcher.uploadChunk(

> CHANGE  3 : 6  @  3 : 4

~ 						generator.getRenderChunk(), compiledchunk);
~ 				generator.getRenderChunk().setCompiledChunk(compiledchunk);
~ 				generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);

> DELETE  5  @  3 : 43

> CHANGE  4 : 5  @  44 : 46

~ 		return this.regionRenderCacheBuilder;

> DELETE  4  @  5 : 8

> EOF
