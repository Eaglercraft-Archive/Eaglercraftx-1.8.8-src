
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 5

~ import net.lax1dude.eaglercraft.v1_8.internal.buffer.FloatBuffer;

> CHANGE  3 : 11  @  3 : 4

~ 
~ import com.google.common.collect.Maps;
~ import com.google.common.collect.Sets;
~ 
~ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
~ import net.lax1dude.eaglercraft.v1_8.opengl.VertexFormat;
~ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> DELETE  4  @  4 : 7

> DELETE  2  @  2 : 6

> DELETE  3  @  3 : 4

> INSERT  8 : 12  @  8

+ 	public static enum ShadowFrustumState {
+ 		OUTSIDE, OUTSIDE_BB, INTERSECT, INSIDE
+ 	}
+ 

> DELETE  5  @  5 : 7

> CHANGE  3 : 4  @  3 : 5

~ 	private final float[] modelviewMatrix = new float[16];

> INSERT  3 : 9  @  3

+ 	public int shadowLOD0FrameIndex = -1;
+ 	public int shadowLOD1FrameIndex = -1;
+ 	public int shadowLOD2FrameIndex = -1;
+ 	public ShadowFrustumState shadowLOD0InFrustum = ShadowFrustumState.OUTSIDE;
+ 	public ShadowFrustumState shadowLOD1InFrustum = ShadowFrustumState.OUTSIDE;
+ 	public ShadowFrustumState shadowLOD2InFrustum = ShadowFrustumState.OUTSIDE;

> DELETE  10  @  10 : 16

> DELETE  11  @  11 : 15

> INSERT  24 : 33  @  24

+ 		if (DeferredStateManager.isRenderingRealisticWater() && compiledchunk.getStateRealisticWater() != null
+ 				&& !compiledchunk.isLayerEmpty(EnumWorldBlockLayer.REALISTIC_WATER)) {
+ 			this.preRenderBlocks(generator.getRegionRenderCacheBuilder()
+ 					.getWorldRendererByLayer(EnumWorldBlockLayer.REALISTIC_WATER), this.position);
+ 			generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.REALISTIC_WATER)
+ 					.setVertexState(compiledchunk.getStateRealisticWater());
+ 			this.postRenderBlocks(EnumWorldBlockLayer.REALISTIC_WATER, x, y, z, generator.getRegionRenderCacheBuilder()
+ 					.getWorldRendererByLayer(EnumWorldBlockLayer.REALISTIC_WATER), compiledchunk);
+ 		}

> DELETE  7  @  7 : 8

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

> INSERT  21 : 34  @  21

+ 
+ 					if (block.eaglerShadersShouldRenderGlassHighlights()) {
+ 						enumworldblocklayer1 = EnumWorldBlockLayer.GLASS_HIGHLIGHTS;
+ 						worldrenderer = generator.getRegionRenderCacheBuilder()
+ 								.getWorldRendererByLayerId(enumworldblocklayer1.ordinal());
+ 						if (!compiledchunk.isLayerStarted(enumworldblocklayer1)) {
+ 							compiledchunk.setLayerStarted(enumworldblocklayer1);
+ 							this.preRenderBlocks(worldrenderer, blockpos);
+ 						}
+ 
+ 						aboolean[enumworldblocklayer1.ordinal()] |= blockrendererdispatcher.renderBlock(iblockstate,
+ 								blockpos$mutableblockpos, regionrendercache, worldrenderer);
+ 					}

> DELETE  17  @  17 : 18

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

> CHANGE  3 : 5  @  3 : 4

~ 		worldRendererIn.begin(7,
~ 				DeferredStateManager.isDeferredRenderer() ? VertexFormat.BLOCK_SHADERS : DefaultVertexFormats.BLOCK);

> CHANGE  5 : 7  @  5 : 6

~ 		if ((layer == EnumWorldBlockLayer.TRANSLUCENT || layer == EnumWorldBlockLayer.REALISTIC_WATER)
~ 				&& !compiledChunkIn.isLayerEmpty(layer)) {

> CHANGE  1 : 6  @  1 : 2

~ 			if (layer == EnumWorldBlockLayer.REALISTIC_WATER) {
~ 				compiledChunkIn.setStateRealisticWater(worldRendererIn.func_181672_a());
~ 			} else {
~ 				compiledChunkIn.setState(worldRendererIn.func_181672_a());
~ 			}

> CHANGE  25 : 26  @  25 : 33

~ 		this.compiledChunk = compiledChunkIn;

> DELETE  10  @  10 : 17

> EOF
