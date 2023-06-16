
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 7  @  4 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.cache.EaglerCacheProvider;
~ import net.lax1dude.eaglercraft.v1_8.cache.EaglerLoadingCache;

> CHANGE  38 : 39  @  38 : 39

~ 			EaglerLoadingCache<BlockPos, BlockWorldState> lcache) {

> CHANGE  3 : 4  @  3 : 5

~ 					if (!this.blockMatches[k][j][i].apply(lcache.get(translateOffset(pos, finger, thumb, i, j, k)))) {

> CHANGE  11 : 12  @  11 : 12

~ 		EaglerLoadingCache loadingcache = func_181627_a(worldIn, false);

> CHANGE  19 : 21  @  19 : 21

~ 	public static EaglerLoadingCache<BlockPos, BlockWorldState> func_181627_a(World parWorld, boolean parFlag) {
~ 		return new EaglerLoadingCache<BlockPos, BlockWorldState>(new BlockPattern.CacheLoader(parWorld, parFlag));

> CHANGE  16 : 17  @  16 : 17

~ 	static class CacheLoader implements EaglerCacheProvider<BlockPos, BlockWorldState> {

> CHANGE  8 : 9  @  8 : 9

~ 		public BlockWorldState create(BlockPos parBlockPos) {

> CHANGE  8 : 9  @  8 : 9

~ 		private final EaglerLoadingCache<BlockPos, BlockWorldState> lcache;

> CHANGE  5 : 6  @  5 : 6

~ 				EaglerLoadingCache<BlockPos, BlockWorldState> parLoadingCache, int parInt1, int parInt2, int parInt3) {

> CHANGE  30 : 31  @  30 : 31

~ 			return (BlockWorldState) this.lcache.get(BlockPattern.translateOffset(this.pos, this.getFinger(),

> EOF
