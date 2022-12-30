
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 5

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;

> CHANGE  4 : 10  @  6 : 7

~ 
~ import com.google.common.collect.Maps;
~ import com.google.common.collect.Sets;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  10  @  5 : 8

> DELETE  2  @  5 : 9

> DELETE  3  @  7 : 8

> DELETE  13  @  14 : 16

> CHANGE  3 : 4  @  5 : 7

~ 	private final float[] modelviewMatrix = new float[16];

> DELETE  14  @  15 : 21

> DELETE  11  @  17 : 21

> DELETE  31  @  35 : 36

> CHANGE  2 : 4  @  3 : 12

~ 		if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
~ 			return;

> INSERT  4 : 7  @  11

+ 		regionrendercache = new RegionRenderCache(this.world, blockpos.add(-1, -1, -1), blockpos1.add(1, 1, 1), 1);
+ 		generator.setCompiledChunk(compiledchunk);
+ 

> CHANGE  10 : 12  @  7 : 9

~ 			for (BlockPos blockpos$mutableblockpos : BlockPos.getAllInBox(blockpos, blockpos1)) {
~ 				IBlockState iblockstate = regionrendercache.getBlockStateFaster(blockpos$mutableblockpos);

> CHANGE  8 : 9  @  8 : 9

~ 					TileEntity tileentity = regionrendercache.getTileEntity(blockpos$mutableblockpos);

> DELETE  39  @  39 : 40

> CHANGE  1 : 8  @  2 : 13

~ 		HashSet hashset1 = Sets.newHashSet(hashset);
~ 		HashSet hashset2 = Sets.newHashSet(this.field_181056_j);
~ 		hashset1.removeAll(this.field_181056_j);
~ 		hashset2.removeAll(hashset);
~ 		this.field_181056_j.clear();
~ 		this.field_181056_j.addAll(hashset);
~ 		this.renderGlobal.func_181023_a(hashset2, hashset1);

> CHANGE  11 : 14  @  15 : 24

~ 		if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
~ 			this.compileTask.finish();
~ 			this.compileTask = null;

> DELETE  4  @  10 : 11

> DELETE  2  @  3 : 7

> DELETE  1  @  5 : 7

> CHANGE  1 : 4  @  3 : 11

~ 		this.finishCompileTask();
~ 		this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK);
~ 		chunkcompiletaskgenerator = this.compileTask;

> CHANGE  7 : 10  @  12 : 35

~ 		this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY);
~ 		this.compileTask.setCompiledChunk(this.compiledChunk);
~ 		return this.compileTask;

> CHANGE  40 : 41  @  60 : 68

~ 		this.compiledChunk = compiledChunkIn;

> DELETE  11  @  18 : 25

> EOF
