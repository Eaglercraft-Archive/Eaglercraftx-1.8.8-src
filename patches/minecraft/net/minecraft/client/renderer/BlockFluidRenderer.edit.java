
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 7  @  2

+ import net.lax1dude.eaglercraft.v1_8.minecraft.EaglerTextureAtlasSprite;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.BlockVertexIDs;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.minecraft.block.Block;

> DELETE  4  @  4 : 6

> INSERT  1 : 2  @  1

+ import net.minecraft.init.Blocks;

> CHANGE  6 : 8  @  6 : 8

~ 	private EaglerTextureAtlasSprite[] atlasSpritesLava = new EaglerTextureAtlasSprite[2];
~ 	private EaglerTextureAtlasSprite[] atlasSpritesWater = new EaglerTextureAtlasSprite[2];

> INSERT  15 : 17  @  15

+ 		BlockPos tmp = new BlockPos(0, 0, 0);
+ 		boolean deferred = DeferredStateManager.isDeferredRenderer();

> INSERT  1 : 3  @  1

+ 		boolean lava = blockliquid.getMaterial() == Material.lava;
+ 		boolean realistic = !lava && DeferredStateManager.isRenderingRealisticWater();

> CHANGE  1 : 2  @  1 : 3

~ 		EaglerTextureAtlasSprite[] atextureatlassprite = lava ? this.atlasSpritesLava : this.atlasSpritesWater;

> CHANGE  4 : 10  @  4 : 6

~ 		boolean flag = blockliquid.shouldSideBeRendered(blockAccess, blockPosIn.up(tmp), EnumFacing.UP);
~ 		if (realistic && blockStateIn.getValue(BlockLiquid.LEVEL).intValue() == 0) {
~ 			Block blockUp = blockAccess.getBlockState(blockPosIn.up(tmp)).getBlock();
~ 			flag &= !blockUp.isFullCube() || !blockUp.isBlockSolid(blockAccess, blockPosIn.up(tmp), EnumFacing.DOWN);
~ 		}
~ 		boolean flag1 = blockliquid.shouldSideBeRendered(blockAccess, blockPosIn.down(tmp), EnumFacing.DOWN);

> CHANGE  1 : 5  @  1 : 5

~ 				blockliquid.shouldSideBeRendered(blockAccess, blockPosIn.north(tmp), EnumFacing.NORTH),
~ 				blockliquid.shouldSideBeRendered(blockAccess, blockPosIn.south(tmp), EnumFacing.SOUTH),
~ 				blockliquid.shouldSideBeRendered(blockAccess, blockPosIn.west(tmp), EnumFacing.WEST),
~ 				blockliquid.shouldSideBeRendered(blockAccess, blockPosIn.east(tmp), EnumFacing.EAST) };

> CHANGE  10 : 16  @  10 : 16

~ 			float f8 = this.getFluidHeight(blockAccess, blockPosIn.south(tmp), material);
~ 			float f9 = this.getFluidHeight(blockAccess, blockPosIn.east(tmp).south(tmp), material);
~ 			float f10 = this.getFluidHeight(blockAccess, blockPosIn.east(tmp), material);
~ 			double d0 = (double) blockPosIn.x;
~ 			double d1 = (double) blockPosIn.y;
~ 			double d2 = (double) blockPosIn.z;

> CHANGE  3 : 4  @  3 : 4

~ 				EaglerTextureAtlasSprite textureatlassprite = atextureatlassprite[0];

> CHANGE  17 : 22  @  17 : 20

~ 				if (realistic || f12 < -999.0F) {
~ 					f13 = realistic ? (f12 < -999.0F ? 0.0f : MathHelper.sin(f12))
~ 							: textureatlassprite.getInterpolatedU(0.0D);
~ 					f17 = realistic ? (f12 < -999.0F ? 0.0f : -MathHelper.cos(f12))
~ 							: textureatlassprite.getInterpolatedV(0.0D);

> CHANGE  1 : 3  @  1 : 3

~ 					f18 = realistic ? f17 : textureatlassprite.getInterpolatedV(16.0D);
~ 					f15 = realistic ? f13 : textureatlassprite.getInterpolatedU(16.0D);

> CHANGE  31 : 36  @  31 : 32

~ 				if (deferred)
~ 					worldRendererIn.genNormals(true, f12 <= -999.0F ? BlockVertexIDs.builtin_water_still_vertex_id
~ 							: BlockVertexIDs.builtin_water_flow_vertex_id);
~ 
~ 				if (blockliquid.func_176364_g(blockAccess, blockPosIn.up(tmp))) {

> INSERT  8 : 11  @  8

+ 					if (deferred)
+ 						worldRendererIn.genNormals(true, f12 <= -999.0F ? BlockVertexIDs.builtin_water_still_vertex_id
+ 								: BlockVertexIDs.builtin_water_flow_vertex_id);

> CHANGE  4 : 9  @  4 : 9

~ 				float f35 = realistic ? 0.0f : atextureatlassprite[0].getMinU();
~ 				float f36 = realistic ? 0.0f : atextureatlassprite[0].getMaxU();
~ 				float f37 = realistic ? 0.0f : atextureatlassprite[0].getMinV();
~ 				float f38 = realistic ? 0.0f : atextureatlassprite[0].getMaxV();
~ 				int l1 = blockliquid.getMixedBrightnessForBlock(blockAccess, blockPosIn.down(tmp));

> INSERT  10 : 12  @  10

+ 				if (deferred)
+ 					worldRendererIn.putNormal(0.0f, -1.0f, 0.0f, BlockVertexIDs.builtin_water_still_vertex_id);

> CHANGE  23 : 24  @  23 : 24

~ 				EaglerTextureAtlasSprite textureatlassprite1 = atextureatlassprite[1];

> CHANGE  38 : 45  @  38 : 43

~ 					float f41 = realistic ? 1.0f : textureatlassprite1.getInterpolatedU(0.0D);
~ 					float f27 = realistic ? 1.0f : textureatlassprite1.getInterpolatedU(8.0D);
~ 					float f28 = realistic ? 0.0f
~ 							: textureatlassprite1.getInterpolatedV((double) ((1.0F - f39) * 16.0F * 0.5F));
~ 					float f29 = realistic ? 0.0f
~ 							: textureatlassprite1.getInterpolatedV((double) ((1.0F - f40) * 16.0F * 0.5F));
~ 					float f30 = realistic ? 0.0f : textureatlassprite1.getInterpolatedV(8.0D);

> CHANGE  15 : 29  @  15 : 23

~ 					if (deferred)
~ 						worldRendererIn.putNormal(j1, 0.0f, k1, BlockVertexIDs.builtin_water_flow_vertex_id);
~ 					if (!realistic) {
~ 						worldRendererIn.pos(d3, d1 + 0.0D, d4).color(f32, f33, f34, 1.0F)
~ 								.tex((double) f41, (double) f30).lightmap(k, l).endVertex();
~ 						worldRendererIn.pos(d5, d1 + 0.0D, d6).color(f32, f33, f34, 1.0F)
~ 								.tex((double) f27, (double) f30).lightmap(k, l).endVertex();
~ 						worldRendererIn.pos(d5, d1 + (double) f40, d6).color(f32, f33, f34, 1.0F)
~ 								.tex((double) f27, (double) f29).lightmap(k, l).endVertex();
~ 						worldRendererIn.pos(d3, d1 + (double) f39, d4).color(f32, f33, f34, 1.0F)
~ 								.tex((double) f41, (double) f28).lightmap(k, l).endVertex();
~ 						if (deferred)
~ 							worldRendererIn.putNormal(-j1, 0.0f, -k1, BlockVertexIDs.builtin_water_flow_vertex_id);
~ 					}

> EOF
