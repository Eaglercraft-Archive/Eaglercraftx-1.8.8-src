
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 9

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.ChunkUpdateManager;

> DELETE  2  @  2 : 6

> DELETE  2  @  2 : 4

> CHANGE  1 : 2  @  1 : 2

~ public class ChunkRenderWorker {

> CHANGE  1 : 2  @  1 : 2

~ 	private final ChunkUpdateManager chunkRenderDispatcher;

> CHANGE  2 : 3  @  2 : 3

~ 	public ChunkRenderWorker(ChunkUpdateManager parChunkRenderDispatcher) {

> CHANGE  3 : 4  @  3 : 4

~ 	public ChunkRenderWorker(ChunkUpdateManager chunkRenderDispatcherIn,

> DELETE  5  @  5 : 21

> CHANGE  1 : 5  @  1 : 11

~ 		if (generator.getStatus() != ChunkCompileTaskGenerator.Status.PENDING) {
~ 			if (!generator.isFinished()) {
~ 				LOGGER.warn("Chunk render task was " + generator.getStatus()
~ 						+ " when I expected it to be pending; ignoring task");

> CHANGE  2 : 3  @  2 : 5

~ 			return;

> INSERT  2 : 4  @  2

+ 		generator.setStatus(ChunkCompileTaskGenerator.Status.COMPILING);
+ 

> CHANGE  15 : 19  @  15 : 26

~ 			if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
~ 				if (!generator.isFinished()) {
~ 					LOGGER.warn("Chunk render task was " + generator.getStatus()
~ 							+ " when I expected it to be compiling; aborting task");

> CHANGE  2 : 4  @  2 : 5

~ 				this.freeRenderBuilder(generator);
~ 				return;

> INSERT  2 : 4  @  2

+ 			generator.setStatus(ChunkCompileTaskGenerator.Status.UPLOADING);
+ 

> CHANGE  1 : 4  @  1 : 2

~ 			if (compiledchunk == null) {
~ 				System.out.println(chunkcompiletaskgenerator$type);
~ 			}

> CHANGE  2 : 4  @  2 : 4

~ 					if (!compiledchunk.isLayerEmpty(enumworldblocklayer)) {
~ 						this.chunkRenderDispatcher.uploadChunk(enumworldblocklayer,

> CHANGE  1 : 4  @  1 : 2

~ 								generator.getRenderChunk(), compiledchunk);
~ 						generator.getRenderChunk().setCompiledChunk(compiledchunk);
~ 						generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);

> CHANGE  3 : 4  @  3 : 4

~ 				this.chunkRenderDispatcher.uploadChunk(

> CHANGE  2 : 5  @  2 : 3

~ 						generator.getRenderChunk(), compiledchunk);
~ 				generator.getRenderChunk().setCompiledChunk(compiledchunk);
~ 				generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);

> DELETE  2  @  2 : 42

> CHANGE  4 : 5  @  4 : 6

~ 		return this.regionRenderCacheBuilder;

> DELETE  3  @  3 : 6

> EOF
