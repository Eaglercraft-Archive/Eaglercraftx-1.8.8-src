
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 7  @  4 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.cache.EaglerCacheProvider;
~ import net.lax1dude.eaglercraft.v1_8.cache.EaglerLoadingCache;

> CHANGE  41 : 42  @  40 : 41

~ 			EaglerLoadingCache<BlockPos, BlockWorldState> lcache) {

> CHANGE  4 : 5  @  4 : 6

~ 					if (!this.blockMatches[k][j][i].apply(lcache.get(translateOffset(pos, finger, thumb, i, j, k)))) {

> CHANGE  12 : 13  @  13 : 14

~ 		EaglerLoadingCache loadingcache = func_181627_a(worldIn, false);

> CHANGE  20 : 22  @  20 : 22

~ 	public static EaglerLoadingCache<BlockPos, BlockWorldState> func_181627_a(World parWorld, boolean parFlag) {
~ 		return new EaglerLoadingCache<BlockPos, BlockWorldState>(new BlockPattern.CacheLoader(parWorld, parFlag));

> CHANGE  18 : 19  @  18 : 19

~ 	static class CacheLoader implements EaglerCacheProvider<BlockPos, BlockWorldState> {

> CHANGE  9 : 10  @  9 : 10

~ 		public BlockWorldState create(BlockPos parBlockPos) {

> CHANGE  9 : 10  @  9 : 10

~ 		private final EaglerLoadingCache<BlockPos, BlockWorldState> lcache;

> CHANGE  6 : 7  @  6 : 7

~ 				EaglerLoadingCache<BlockPos, BlockWorldState> parLoadingCache, int parInt1, int parInt2, int parInt3) {

> CHANGE  31 : 32  @  31 : 32

~ 			return (BlockWorldState) this.lcache.get(BlockPattern.translateOffset(this.pos, this.getFinger(),

> EOF
