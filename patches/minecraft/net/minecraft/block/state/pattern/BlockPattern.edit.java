
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 7  @  4 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.cache.EaglerCacheProvider;
~ import net.lax1dude.eaglercraft.v1_8.cache.EaglerLoadingCache;

> CHANGE  45 : 46  @  44 : 45

~ 			EaglerLoadingCache<BlockPos, BlockWorldState> lcache) {

> CHANGE  49 : 50  @  48 : 50

~ 					if (!this.blockMatches[k][j][i].apply(lcache.get(translateOffset(pos, finger, thumb, i, j, k)))) {

> CHANGE  61 : 62  @  61 : 62

~ 		EaglerLoadingCache loadingcache = func_181627_a(worldIn, false);

> CHANGE  81 : 83  @  81 : 83

~ 	public static EaglerLoadingCache<BlockPos, BlockWorldState> func_181627_a(World parWorld, boolean parFlag) {
~ 		return new EaglerLoadingCache<BlockPos, BlockWorldState>(new BlockPattern.CacheLoader(parWorld, parFlag));

> CHANGE  99 : 100  @  99 : 100

~ 	static class CacheLoader implements EaglerCacheProvider<BlockPos, BlockWorldState> {

> CHANGE  108 : 109  @  108 : 109

~ 		public BlockWorldState create(BlockPos parBlockPos) {

> CHANGE  117 : 118  @  117 : 118

~ 		private final EaglerLoadingCache<BlockPos, BlockWorldState> lcache;

> CHANGE  123 : 124  @  123 : 124

~ 				EaglerLoadingCache<BlockPos, BlockWorldState> parLoadingCache, int parInt1, int parInt2, int parInt3) {

> CHANGE  154 : 155  @  154 : 155

~ 			return (BlockWorldState) this.lcache.get(BlockPattern.translateOffset(this.pos, this.getFinger(),

> EOF
