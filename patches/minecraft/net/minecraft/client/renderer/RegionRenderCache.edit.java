
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 4  @  3

+ 

> INSERT  7 : 8  @  7

+ import net.minecraft.world.biome.BiomeGenBase;

> CHANGE  3 : 4  @  3 : 4

~ 	private final IBlockState DEFAULT_STATE = Blocks.air.getDefaultState();

> CHANGE  1 : 6  @  1 : 3

~ 	private final BlockPos tmpStupid = new BlockPos();
~ 	private static final int[] combinedLights = new int[8000];
~ 	private static final IBlockState[] blockStates = new IBlockState[8000];
~ 	private static final int[] biomeColors = new int[1200];
~ 	private static final int[] biomeColorsBlended = new int[768];

> CHANGE  4 : 8  @  4 : 8

~ 		Arrays.fill(combinedLights, -1);
~ 		Arrays.fill(blockStates, null);
~ 		Arrays.fill(biomeColors, 0);
~ 		Arrays.fill(biomeColorsBlended, 0);

> CHANGE  10 : 11  @  10 : 11

~ 		int k = combinedLights[j];

> CHANGE  2 : 3  @  2 : 3

~ 			combinedLights[j] = k;

> CHANGE  7 : 8  @  7 : 8

~ 		IBlockState iblockstate = blockStates[i];

> CHANGE  2 : 3  @  2 : 3

~ 			blockStates[i] = iblockstate;

> INSERT  5 : 33  @  5

+ 	/**
+ 	 * only use with a regular "net.minecraft.util.BlockPos"!
+ 	 */
+ 	public IBlockState getBlockStateFaster(BlockPos blockpos) {
+ 		int i = this.getPositionIndexFaster(blockpos);
+ 		IBlockState iblockstate = blockStates[i];
+ 		if (iblockstate == null) {
+ 			iblockstate = this.getBlockStateRawFaster(blockpos);
+ 			blockStates[i] = iblockstate;
+ 		}
+ 
+ 		return iblockstate;
+ 	}
+ 
+ 	/**
+ 	 * only use with a regular "net.minecraft.util.BlockPos"!
+ 	 */
+ 	public int getBiomeColorForCoords(BlockPos blockpos, int colorIndex) {
+ 		int i = this.getPositionIndex16Faster(blockpos);
+ 		i += colorIndex * 256;
+ 		int j = biomeColorsBlended[i];
+ 		if (j == 0) {
+ 			j = getBiomeColorBlended(blockpos, colorIndex);
+ 			biomeColorsBlended[i] = j;
+ 		}
+ 		return j;
+ 	}
+ 

> INSERT  10 : 76  @  10

+ 	/**
+ 	 * only use with a regular "net.minecraft.util.BlockPos"!
+ 	 */
+ 	private IBlockState getBlockStateRawFaster(BlockPos pos) {
+ 		if (pos.y >= 0 && pos.y < 256) {
+ 			int i = (pos.x >> 4) - this.chunkX;
+ 			int j = (pos.z >> 4) - this.chunkZ;
+ 			return this.chunkArray[i][j].getBlockState(pos);
+ 		} else {
+ 			return DEFAULT_STATE;
+ 		}
+ 	}
+ 
+ 	private int getBiomeColorBlended(BlockPos blockpos, int colorIndex) {
+ 		BlockPos blockpos2 = tmpStupid;
+ 		blockpos2.y = blockpos.y;
+ 		int rad = 1;
+ 		int xmin = blockpos.x - rad;
+ 		int zmin = blockpos.z - rad;
+ 		int xmax = blockpos.x + rad;
+ 		int zmax = blockpos.z + rad;
+ 		int r = 0;
+ 		int g = 0;
+ 		int b = 0;
+ 		int rgb;
+ 		for (int x = xmin; x <= xmax; ++x) {
+ 			for (int z = zmin; z <= zmax; ++z) {
+ 				blockpos2.x = x;
+ 				blockpos2.z = z;
+ 				rgb = getBiomeColorRaw(blockpos2, colorIndex);
+ 				r += (rgb >> 16) & 0xFF;
+ 				g += (rgb >> 8) & 0xFF;
+ 				b += rgb & 0xFF;
+ 			}
+ 		}
+ 		rad = 1 + rad * 2;
+ 		rad *= rad;
+ 		return 0xFF000000 | ((r / rad) << 16) | ((g / rad) << 8) | (b / rad);
+ 	}
+ 
+ 	private int getBiomeColorRaw(BlockPos blockpos, int colorIndex) {
+ 		int i = getPositionIndex20Faster(blockpos);
+ 		i += colorIndex * 400;
+ 		int j = biomeColors[i];
+ 		BiomeGenBase biome;
+ 		int ii, jj;
+ 		if (j == 0) {
+ 			if (blockpos.y >= 0 && blockpos.y < 256) {
+ 				ii = (blockpos.x >> 4) - this.chunkX;
+ 				jj = (blockpos.z >> 4) - this.chunkZ;
+ 				biome = this.chunkArray[ii][jj].getBiome(blockpos, worldObj.getWorldChunkManager());
+ 				if (colorIndex == 0) {
+ 					j = biome.getGrassColorAtPos(blockpos);
+ 				} else if (colorIndex == 1) {
+ 					j = biome.getFoliageColorAtPos(blockpos);
+ 				} else {
+ 					j = biome.waterColorMultiplier;
+ 				}
+ 			} else {
+ 				j = 0xFFFFFFFF;
+ 			}
+ 			biomeColors[i] = j;
+ 		}
+ 		return j;
+ 	}
+ 

> INSERT  6 : 31  @  6

+ 
+ 	/**
+ 	 * only use with a regular "net.minecraft.util.BlockPos"!
+ 	 */
+ 	private int getPositionIndexFaster(BlockPos parBlockPos) {
+ 		int i = parBlockPos.x - this.position.x;
+ 		int j = parBlockPos.y - this.position.y;
+ 		int k = parBlockPos.z - this.position.z;
+ 		return i * 400 + k * 20 + j;
+ 	}
+ 
+ 	/**
+ 	 * only use with a regular "net.minecraft.util.BlockPos"!
+ 	 */
+ 	private int getPositionIndex16Faster(BlockPos parBlockPos) {
+ 		int i = parBlockPos.x - this.position.x - 2;
+ 		int k = parBlockPos.z - this.position.z - 2;
+ 		return i * 16 + k;
+ 	}
+ 
+ 	private int getPositionIndex20Faster(BlockPos parBlockPos) {
+ 		int i = parBlockPos.x - this.position.x;
+ 		int k = parBlockPos.z - this.position.z;
+ 		return i * 20 + k;
+ 	}

> EOF
