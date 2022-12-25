
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 9

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.minecraft.ChunkUpdateManager;

> DELETE  7  @  11 : 15

> DELETE  9  @  17 : 19

> CHANGE  10 : 11  @  20 : 21

~ public class ChunkRenderWorker {

> CHANGE  12 : 13  @  22 : 23

~ 	private final ChunkUpdateManager chunkRenderDispatcher;

> CHANGE  15 : 16  @  25 : 26

~ 	public ChunkRenderWorker(ChunkUpdateManager parChunkRenderDispatcher) {

> CHANGE  19 : 20  @  29 : 30

~ 	public ChunkRenderWorker(ChunkUpdateManager chunkRenderDispatcherIn,

> DELETE  25  @  35 : 51

> CHANGE  26 : 30  @  52 : 62

~ 		if (generator.getStatus() != ChunkCompileTaskGenerator.Status.PENDING) {
~ 			if (!generator.isFinished()) {
~ 				LOGGER.warn("Chunk render task was " + generator.getStatus()
~ 						+ " when I expected it to be pending; ignoring task");

> CHANGE  32 : 33  @  64 : 67

~ 			return;

> INSERT  35 : 37  @  69

+ 		generator.setStatus(ChunkCompileTaskGenerator.Status.COMPILING);
+ 

> CHANGE  52 : 56  @  84 : 95

~ 			if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
~ 				if (!generator.isFinished()) {
~ 					LOGGER.warn("Chunk render task was " + generator.getStatus()
~ 							+ " when I expected it to be compiling; aborting task");

> CHANGE  58 : 60  @  97 : 100

~ 				this.freeRenderBuilder(generator);
~ 				return;

> INSERT  62 : 64  @  102

+ 			generator.setStatus(ChunkCompileTaskGenerator.Status.UPLOADING);
+ 

> CHANGE  65 : 68  @  103 : 104

~ 			if (compiledchunk == null) {
~ 				System.out.println(chunkcompiletaskgenerator$type);
~ 			}

> CHANGE  70 : 72  @  106 : 108

~ 					if (!compiledchunk.isLayerEmpty(enumworldblocklayer)) {
~ 						this.chunkRenderDispatcher.uploadChunk(enumworldblocklayer,

> CHANGE  73 : 76  @  109 : 110

~ 								generator.getRenderChunk(), compiledchunk);
~ 						generator.getRenderChunk().setCompiledChunk(compiledchunk);
~ 						generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);

> CHANGE  79 : 80  @  113 : 114

~ 				this.chunkRenderDispatcher.uploadChunk(

> CHANGE  82 : 85  @  116 : 117

~ 						generator.getRenderChunk(), compiledchunk);
~ 				generator.getRenderChunk().setCompiledChunk(compiledchunk);
~ 				generator.setStatus(ChunkCompileTaskGenerator.Status.DONE);

> DELETE  87  @  119 : 159

> CHANGE  91 : 92  @  163 : 165

~ 		return this.regionRenderCacheBuilder;

> DELETE  95  @  168 : 171

> EOF
