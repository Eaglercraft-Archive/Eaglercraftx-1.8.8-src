
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 5

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;

> CHANGE  6 : 12  @  8 : 9

~ 
~ import com.google.common.collect.Maps;
~ import com.google.common.collect.Sets;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;

> DELETE  16  @  13 : 16

> DELETE  18  @  18 : 22

> DELETE  21  @  25 : 26

> DELETE  34  @  39 : 41

> CHANGE  37 : 38  @  44 : 46

~ 	private final float[] modelviewMatrix = new float[16];

> DELETE  51  @  59 : 65

> DELETE  62  @  76 : 80

> DELETE  93  @  111 : 112

> CHANGE  95 : 97  @  114 : 123

~ 		if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING) {
~ 			return;

> INSERT  99 : 102  @  125

+ 		regionrendercache = new RegionRenderCache(this.world, blockpos.add(-1, -1, -1), blockpos1.add(1, 1, 1), 1);
+ 		generator.setCompiledChunk(compiledchunk);
+ 

> CHANGE  109 : 111  @  132 : 134

~ 			for (BlockPos blockpos$mutableblockpos : BlockPos.getAllInBox(blockpos, blockpos1)) {
~ 				IBlockState iblockstate = regionrendercache.getBlockStateFaster(blockpos$mutableblockpos);

> CHANGE  117 : 118  @  140 : 141

~ 					TileEntity tileentity = regionrendercache.getTileEntity(blockpos$mutableblockpos);

> DELETE  156  @  179 : 180

> CHANGE  157 : 164  @  181 : 192

~ 		HashSet hashset1 = Sets.newHashSet(hashset);
~ 		HashSet hashset2 = Sets.newHashSet(this.field_181056_j);
~ 		hashset1.removeAll(this.field_181056_j);
~ 		hashset2.removeAll(hashset);
~ 		this.field_181056_j.clear();
~ 		this.field_181056_j.addAll(hashset);
~ 		this.renderGlobal.func_181023_a(hashset2, hashset1);

> CHANGE  168 : 171  @  196 : 205

~ 		if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE) {
~ 			this.compileTask.finish();
~ 			this.compileTask = null;

> DELETE  172  @  206 : 207

> DELETE  174  @  209 : 213

> DELETE  175  @  214 : 216

> CHANGE  176 : 179  @  217 : 225

~ 		this.finishCompileTask();
~ 		this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK);
~ 		chunkcompiletaskgenerator = this.compileTask;

> CHANGE  183 : 186  @  229 : 252

~ 		this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY);
~ 		this.compileTask.setCompiledChunk(this.compiledChunk);
~ 		return this.compileTask;

> CHANGE  223 : 224  @  289 : 297

~ 		this.compiledChunk = compiledChunkIn;

> DELETE  234  @  307 : 314

> EOF
