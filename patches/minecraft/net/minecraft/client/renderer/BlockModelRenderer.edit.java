
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  4 : 9  @  4

+ 
+ import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;
+ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.VertexMarkerState;

> DELETE  3  @  3 : 7

> INSERT  72 : 73  @  72

+ 		boolean isDeferred = DeferredStateManager.isDeferredRenderer();

> INSERT  1 : 2  @  1

+ 		float[] afloat = isDeferred ? new float[EnumFacing.values().length * 2] : null;

> INSERT  2 : 3  @  2

+ 		BlockPos.MutableBlockPos pointer = new BlockPos.MutableBlockPos();

> CHANGE  3 : 4  @  3 : 4

~ 				BlockPos blockpos = blockPosIn.offsetEvenFaster(enumfacing, pointer);

> CHANGE  3 : 4  @  3 : 4

~ 							worldRendererIn, list, bitset, afloat);

> CHANGE  8 : 9  @  8 : 9

~ 					worldRendererIn, list1, bitset, afloat);

> INSERT  9 : 10  @  9

+ 		boolean isDeferred = DeferredStateManager.isDeferredRenderer();

> CHANGE  8 : 9  @  8 : 9

~ 			if (!isDeferred && block$enumoffsettype == Block.EnumOffsetType.XYZ) {

> CHANGE  5 : 7  @  5 : 6

~ 			int[] vertData = isDeferred ? bakedquad.getVertexDataWithNormals() : bakedquad.getVertexData();
~ 			this.fillQuadBounds(blockIn, vertData, bakedquad.getFace(), quadBounds, boundsFlags, isDeferred ? 8 : 7);

> CHANGE  2 : 3  @  2 : 3

~ 			worldRendererIn.addVertexData(vertData);

> CHANGE  36 : 37  @  36 : 37

~ 			BitSet boundsFlags, int deferredStrideOverride) {

> CHANGE  8 : 12  @  8 : 11

~ 			int j = i * deferredStrideOverride;
~ 			float f6 = Float.intBitsToFloat(vertexData[j]);
~ 			float f7 = Float.intBitsToFloat(vertexData[j + 1]);
~ 			float f8 = Float.intBitsToFloat(vertexData[j + 2]);

> INSERT  53 : 60  @  53

+ 	private final BlockPos blockpos0 = new BlockPos(0, 0, 0);
+ 	private final BlockPos blockpos1 = new BlockPos(0, 0, 0);
+ 	private final BlockPos blockpos2 = new BlockPos(0, 0, 0);
+ 	private final BlockPos blockpos3 = new BlockPos(0, 0, 0);
+ 	private final BlockPos blockpos4 = new BlockPos(0, 0, 0);
+ 	private final BlockPos blockpos5 = new BlockPos(0, 0, 0);
+ 

> CHANGE  2 : 4  @  2 : 3

~ 			List<BakedQuad> listQuadsIn, BitSet boundsFlags, float[] quadBounds) {
~ 		boolean isDeferred = DeferredStateManager.isDeferredRenderer();

> CHANGE  11 : 12  @  11 : 12

~ 			if (!isDeferred && block$enumoffsettype == Block.EnumOffsetType.XYZ) {

> INSERT  5 : 10  @  5

+ 			EnumFacing facingIn = bakedquad.getFace();
+ 			int[] vertData = isDeferred ? bakedquad.getVertexDataWithNormals() : bakedquad.getVertexData();
+ 			blockPosIn.offsetEvenFaster(facingIn, blockpos0);
+ 			this.fillQuadBounds(blockIn, vertData, facingIn, quadBounds, boundsFlags, isDeferred ? 8 : 7);
+ 			boolean boundsFlags0 = boundsFlags.get(0);

> CHANGE  1 : 2  @  1 : 5

~ 				brightnessIn = boundsFlags0 ? blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos0)

> CHANGE  3 : 100  @  3 : 5

~ 			worldRendererIn.addVertexData(vertData);
~ 
~ 			if (isDeferred) {
~ 				BlockModelRenderer.EnumNeighborInfo blockmodelrenderer$enumneighborinfo = BlockModelRenderer.EnumNeighborInfo
~ 						.getNeighbourInfo(facingIn);
~ 				BlockPos blockPosIn2 = boundsFlags0 ? blockpos0 : blockPosIn;
~ 				blockPosIn2.offsetEvenFaster(blockmodelrenderer$enumneighborinfo.field_178276_g[0], blockpos1);
~ 				int i = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos1);
~ 				blockPosIn2.offsetEvenFaster(blockmodelrenderer$enumneighborinfo.field_178276_g[1], blockpos2);
~ 				int j = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos2);
~ 				blockPosIn2.offsetEvenFaster(blockmodelrenderer$enumneighborinfo.field_178276_g[2], blockpos3);
~ 				int k = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos3);
~ 				blockPosIn2.offsetEvenFaster(blockmodelrenderer$enumneighborinfo.field_178276_g[3], blockpos4);
~ 				int l = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos4);
~ 
~ 				blockpos1.offsetEvenFaster(blockmodelrenderer$enumneighborinfo.field_178276_g[2], blockpos5);
~ 				int i1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos5);
~ 
~ 				blockpos1.offsetEvenFaster(blockmodelrenderer$enumneighborinfo.field_178276_g[3], blockpos5);
~ 				int j1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos5);
~ 
~ 				blockpos2.offsetEvenFaster(blockmodelrenderer$enumneighborinfo.field_178276_g[2], blockpos5);
~ 				int k1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos5);
~ 
~ 				blockpos2.offsetEvenFaster(blockmodelrenderer$enumneighborinfo.field_178276_g[3], blockpos5);
~ 				int l1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos5);
~ 
~ 				int[] b = new int[4];
~ 
~ 				boolean upIsOpaque = !blockAccessIn.getBlockState(blockpos0).getBlock().isOpaqueCube();
~ 				int i3;
~ 				if (boundsFlags0 || upIsOpaque) {
~ 					i3 = (ownBrightness && boundsFlags0) ? brightnessIn
~ 							: blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos0);
~ 				} else {
~ 					i3 = (ownBrightness && !boundsFlags0) ? brightnessIn
~ 							: blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn);
~ 				}
~ 
~ 				BlockModelRenderer.VertexTranslations blockmodelrenderer$vertextranslations = BlockModelRenderer.VertexTranslations
~ 						.getVertexTranslations(facingIn);
~ 				if (boundsFlags.get(1) && blockmodelrenderer$enumneighborinfo.field_178289_i) {
~ 					float f13 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[0].field_178229_m]
~ 							* quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[1].field_178229_m];
~ 					float f14 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[2].field_178229_m]
~ 							* quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[3].field_178229_m];
~ 					float f15 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[4].field_178229_m]
~ 							* quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[5].field_178229_m];
~ 					float f16 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[6].field_178229_m]
~ 							* quadBounds[blockmodelrenderer$enumneighborinfo.field_178286_j[7].field_178229_m];
~ 					float f17 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[0].field_178229_m]
~ 							* quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[1].field_178229_m];
~ 					float f18 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[2].field_178229_m]
~ 							* quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[3].field_178229_m];
~ 					float f19 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[4].field_178229_m]
~ 							* quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[5].field_178229_m];
~ 					float f20 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[6].field_178229_m]
~ 							* quadBounds[blockmodelrenderer$enumneighborinfo.field_178287_k[7].field_178229_m];
~ 					float f21 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[0].field_178229_m]
~ 							* quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[1].field_178229_m];
~ 					float f22 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[2].field_178229_m]
~ 							* quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[3].field_178229_m];
~ 					float f23 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[4].field_178229_m]
~ 							* quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[5].field_178229_m];
~ 					float f24 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[6].field_178229_m]
~ 							* quadBounds[blockmodelrenderer$enumneighborinfo.field_178284_l[7].field_178229_m];
~ 					float f25 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[0].field_178229_m]
~ 							* quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[1].field_178229_m];
~ 					float f26 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[2].field_178229_m]
~ 							* quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[3].field_178229_m];
~ 					float f27 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[4].field_178229_m]
~ 							* quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[5].field_178229_m];
~ 					float f28 = quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[6].field_178229_m]
~ 							* quadBounds[blockmodelrenderer$enumneighborinfo.field_178285_m[7].field_178229_m];
~ 					int i2 = getAoBrightness(l, i, j1, i3);
~ 					int j2 = getAoBrightness(k, i, i1, i3);
~ 					int k2 = getAoBrightness(k, j, k1, i3);
~ 					int l2 = getAoBrightness(l, j, l1, i3);
~ 					b[blockmodelrenderer$vertextranslations.field_178191_g] = getVertexBrightness(i2, j2, k2, l2, f13,
~ 							f14, f15, f16);
~ 					b[blockmodelrenderer$vertextranslations.field_178200_h] = getVertexBrightness(i2, j2, k2, l2, f17,
~ 							f18, f19, f20);
~ 					b[blockmodelrenderer$vertextranslations.field_178201_i] = getVertexBrightness(i2, j2, k2, l2, f21,
~ 							f22, f23, f24);
~ 					b[blockmodelrenderer$vertextranslations.field_178198_j] = getVertexBrightness(i2, j2, k2, l2, f25,
~ 							f26, f27, f28);
~ 				} else {
~ 					b[blockmodelrenderer$vertextranslations.field_178191_g] = getAoBrightness(l, i, j1, i3);
~ 					b[blockmodelrenderer$vertextranslations.field_178200_h] = getAoBrightness(k, i, i1, i3);
~ 					b[blockmodelrenderer$vertextranslations.field_178201_i] = getAoBrightness(k, j, k1, i3);
~ 					b[blockmodelrenderer$vertextranslations.field_178198_j] = getAoBrightness(l, j, l1, i3);
~ 				}
~ 				worldRendererIn.putBrightness4(b[0], b[1], b[2], b[3]);
~ 			} else {
~ 				worldRendererIn.putBrightness4(brightnessIn, brightnessIn, brightnessIn, brightnessIn);
~ 			}
~ 

> INSERT  20 : 36  @  20

+ 	private static int getAoBrightness(int parInt1, int parInt2, int parInt3, int parInt4) {
+ 		if (parInt1 == 0) {
+ 			parInt1 = parInt4;
+ 		}
+ 
+ 		if (parInt2 == 0) {
+ 			parInt2 = parInt4;
+ 		}
+ 
+ 		if (parInt3 == 0) {
+ 			parInt3 = parInt4;
+ 		}
+ 
+ 		return parInt1 + parInt2 + parInt3 + parInt4 >> 2 & 16711935;
+ 	}
+ 

> CHANGE  45 : 47  @  45 : 46

~ 			worldrenderer.putNormal((float) vec3i.getX(), (float) vec3i.getY(), (float) vec3i.getZ(),
~ 					VertexMarkerState.markId);

> INSERT  9 : 16  @  9

+ 		private final BlockPos blockpos0 = new BlockPos(0, 0, 0);
+ 		private final BlockPos blockpos1 = new BlockPos(0, 0, 0);
+ 		private final BlockPos blockpos2 = new BlockPos(0, 0, 0);
+ 		private final BlockPos blockpos3 = new BlockPos(0, 0, 0);
+ 		private final BlockPos blockpos4 = new BlockPos(0, 0, 0);
+ 		private final BlockPos blockpos5 = new BlockPos(0, 0, 0);
+ 

> CHANGE  2 : 3  @  2 : 3

~ 			BlockPos blockpos = boundsFlags.get(0) ? blockPosIn.offsetEvenFaster(facingIn, blockpos0) : blockPosIn;

> CHANGE  2 : 3  @  2 : 6

~ 			blockpos.offsetEvenFaster(blockmodelrenderer$enumneighborinfo.field_178276_g[0], blockpos1);

> DELETE  1  @  1 : 4

> INSERT  1 : 6  @  1

+ 			blockpos1.offsetEvenFaster(facingIn, blockpos5);
+ 			boolean flag = blockAccessIn.getBlockState(blockpos5).getBlock().isTranslucent();
+ 
+ 			blockpos.offsetEvenFaster(blockmodelrenderer$enumneighborinfo.field_178276_g[1], blockpos2);
+ 			int j = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos2);

> INSERT  1 : 6  @  1

+ 			blockpos2.offsetEvenFaster(facingIn, blockpos5);
+ 			boolean flag1 = blockAccessIn.getBlockState(blockpos5).getBlock().isTranslucent();
+ 
+ 			blockpos.offsetEvenFaster(blockmodelrenderer$enumneighborinfo.field_178276_g[2], blockpos3);
+ 			int k = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos3);

> INSERT  1 : 6  @  1

+ 			blockpos3.offsetEvenFaster(facingIn, blockpos5);
+ 			boolean flag2 = blockAccessIn.getBlockState(blockpos5).getBlock().isTranslucent();
+ 
+ 			blockpos.offsetEvenFaster(blockmodelrenderer$enumneighborinfo.field_178276_g[3], blockpos4);
+ 			int l = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos4);

> CHANGE  1 : 4  @  1 : 5

~ 			blockpos4.offsetEvenFaster(facingIn, blockpos5);
~ 			boolean flag3 = blockAccessIn.getBlockState(blockpos5).getBlock().isTranslucent();
~ 

> CHANGE  6 : 7  @  6 : 7

~ 				blockpos1.offsetEvenFaster(blockmodelrenderer$enumneighborinfo.field_178276_g[2], blockpos5);

> CHANGE  10 : 13  @  10 : 13

~ 				blockpos1.offsetEvenFaster(blockmodelrenderer$enumneighborinfo.field_178276_g[3], blockpos5);
~ 				f5 = blockAccessIn.getBlockState(blockpos5).getBlock().getAmbientOcclusionLightValue();
~ 				j1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos5);

> CHANGE  8 : 11  @  8 : 11

~ 				blockpos2.offsetEvenFaster(blockmodelrenderer$enumneighborinfo.field_178276_g[2], blockpos5);
~ 				f6 = blockAccessIn.getBlockState(blockpos5).getBlock().getAmbientOcclusionLightValue();
~ 				k1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos5);

> CHANGE  8 : 11  @  8 : 11

~ 				blockpos2.offsetEvenFaster(blockmodelrenderer$enumneighborinfo.field_178276_g[3], blockpos5);
~ 				f7 = blockAccessIn.getBlockState(blockpos5).getBlock().getAmbientOcclusionLightValue();
~ 				l1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos5);

> INSERT  2 : 3  @  2

+ 			blockPosIn.offsetEvenFaster(facingIn, blockpos5);

> CHANGE  1 : 3  @  1 : 4

~ 			if (boundsFlags.get(0) || !blockAccessIn.getBlockState(blockpos5).getBlock().isOpaqueCube()) {
~ 				i3 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos5);

> CHANGE  52 : 64  @  52 : 64

~ 				int i2 = getAoBrightness(l, i, j1, i3);
~ 				int j2 = getAoBrightness(k, i, i1, i3);
~ 				int k2 = getAoBrightness(k, j, k1, i3);
~ 				int l2 = getAoBrightness(l, j, l1, i3);
~ 				this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178191_g] = getVertexBrightness(i2,
~ 						j2, k2, l2, f13, f14, f15, f16);
~ 				this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178200_h] = getVertexBrightness(i2,
~ 						j2, k2, l2, f17, f18, f19, f20);
~ 				this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178201_i] = getVertexBrightness(i2,
~ 						j2, k2, l2, f21, f22, f23, f24);
~ 				this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178198_j] = getVertexBrightness(i2,
~ 						j2, k2, l2, f25, f26, f27, f28);

> CHANGE  5 : 13  @  5 : 13

~ 				this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178191_g] = getAoBrightness(l, i, j1,
~ 						i3);
~ 				this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178200_h] = getAoBrightness(k, i, i1,
~ 						i3);
~ 				this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178201_i] = getAoBrightness(k, j, k1,
~ 						i3);
~ 				this.vertexBrightness[blockmodelrenderer$vertextranslations.field_178198_j] = getAoBrightness(l, j, l1,
~ 						i3);

> INSERT  7 : 8  @  7

+ 	}

> CHANGE  1 : 8  @  1 : 25

~ 	private static int getVertexBrightness(int parInt1, int parInt2, int parInt3, int parInt4, float parFloat1,
~ 			float parFloat2, float parFloat3, float parFloat4) {
~ 		int i = (int) ((float) (parInt1 >> 16 & 255) * parFloat1 + (float) (parInt2 >> 16 & 255) * parFloat2
~ 				+ (float) (parInt3 >> 16 & 255) * parFloat3 + (float) (parInt4 >> 16 & 255) * parFloat4) & 255;
~ 		int j = (int) ((float) (parInt1 & 255) * parFloat1 + (float) (parInt2 & 255) * parFloat2
~ 				+ (float) (parInt3 & 255) * parFloat3 + (float) (parInt4 & 255) * parFloat4) & 255;
~ 		return i << 16 | j;

> EOF
