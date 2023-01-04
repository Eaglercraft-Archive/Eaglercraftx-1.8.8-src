
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 5

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;

> CHANGE  3 : 9  @  3 : 4

~ 
~ import com.google.common.collect.Maps;
~ import com.google.common.collect.Sets;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  4  @  4 : 7

> DELETE  2  @  2 : 6

> DELETE  3  @  3 : 4

> DELETE  13  @  13 : 15

> CHANGE  3 : 4  @  3 : 5

~ 	private final float[] modelviewMatrix = new float[16];

> DELETE  13  @  13 : 19

> DELETE  11  @  11 : 15

> DELETE  31  @  31 : 32

> CHANGE  2 : 4  @  2 : 11

~ 		if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
~ 			return;

> INSERT  2 : 5  @  2

+ 		regionrendercache = new RegionRenderCache(this.world, blockpos.add(-1, -1, -1), blockpos1.add(1, 1, 1), 1);
+ 		generator.setCompiledChunk(compiledchunk);
+ 

> CHANGE  7 : 9  @  7 : 9

~ 			for (BlockPos blockpos$mutableblockpos : BlockPos.getAllInBox(blockpos, blockpos1)) {
~ 				IBlockState iblockstate = regionrendercache.getBlockStateFaster(blockpos$mutableblockpos);

> CHANGE  6 : 7  @  6 : 7

~ 					TileEntity tileentity = regionrendercache.getTileEntity(blockpos$mutableblockpos);

> DELETE  38  @  38 : 39

> CHANGE  1 : 8  @  1 : 12

~ 		HashSet hashset1 = Sets.newHashSet(hashset);
~ 		HashSet hashset2 = Sets.newHashSet(this.field_181056_j);
~ 		hashset1.removeAll(this.field_181056_j);
~ 		hashset2.removeAll(hashset);
~ 		this.field_181056_j.clear();
~ 		this.field_181056_j.addAll(hashset);
~ 		this.renderGlobal.func_181023_a(hashset2, hashset1);

> CHANGE  4 : 7  @  4 : 13

~ 		if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
~ 			this.compileTask.finish();
~ 			this.compileTask = null;

> DELETE  1  @  1 : 2

> DELETE  2  @  2 : 6

> DELETE  1  @  1 : 3

> CHANGE  1 : 4  @  1 : 9

~ 		this.finishCompileTask();
~ 		this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK);
~ 		chunkcompiletaskgenerator = this.compileTask;

> CHANGE  4 : 7  @  4 : 27

~ 		this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY);
~ 		this.compileTask.setCompiledChunk(this.compiledChunk);
~ 		return this.compileTask;

> CHANGE  37 : 38  @  37 : 45

~ 		this.compiledChunk = compiledChunkIn;

> DELETE  10  @  10 : 17

> EOF
