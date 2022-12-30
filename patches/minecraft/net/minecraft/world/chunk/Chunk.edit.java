
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 5

~ import java.util.ArrayList;

> CHANGE  4 : 5  @  6 : 7

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  2 : 8  @  2 : 3

~ 
~ import com.google.common.base.Predicate;
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  24  @  19 : 20

> DELETE  1  @  2 : 6

> DELETE  1  @  5 : 8

> CHANGE  24 : 25  @  27 : 28

~ 	private List<BlockPos> tileEntityPosQueue;

> CHANGE  9 : 10  @  9 : 10

~ 		this.tileEntityPosQueue = new ArrayList();

> CHANGE  343 : 352  @  343 : 347

~ 		try {
~ 			if (pos.getY() >= 0 && pos.getY() >> 4 < this.storageArrays.length) {
~ 				ExtendedBlockStorage extendedblockstorage = this.storageArrays[pos.getY() >> 4];
~ 				if (extendedblockstorage != null) {
~ 					int j = pos.getX() & 15;
~ 					int k = pos.getY() & 15;
~ 					int i = pos.getZ() & 15;
~ 					return extendedblockstorage.get(j, k, i);
~ 				}

> CHANGE  11 : 18  @  6 : 21

~ 			return Blocks.air.getDefaultState();
~ 		} catch (Throwable throwable) {
~ 			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block state");
~ 			CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being got");
~ 			crashreportcategory.addCrashSectionCallable("Location", new Callable<String>() {
~ 				public String call() throws Exception {
~ 					return CrashReportCategory.getCoordinateInfo(pos);

> INSERT  8 : 12  @  16

+ 			});
+ 			throw new ReportedException(crashreport);
+ 		}
+ 	}

> CHANGE  5 : 18  @  1 : 11

~ 	/**
~ 	 * only use with a regular "net.minecraft.util.BlockPos"!
~ 	 */
~ 	public IBlockState getBlockStateFaster(final BlockPos pos) {
~ 		try {
~ 			if (pos.y >= 0 && pos.y >> 4 < this.storageArrays.length) {
~ 				ExtendedBlockStorage extendedblockstorage = this.storageArrays[pos.getY() >> 4];
~ 				if (extendedblockstorage != null) {
~ 					int j = pos.x & 15;
~ 					int k = pos.y & 15;
~ 					int i = pos.z & 15;
~ 					return extendedblockstorage.get(j, k, i);
~ 				}

> INSERT  14 : 25  @  11

+ 
+ 			return Blocks.air.getDefaultState();
+ 		} catch (Throwable throwable) {
+ 			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block state");
+ 			CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being got");
+ 			crashreportcategory.addCrashSectionCallable("Location", new Callable<String>() {
+ 				public String call() throws Exception {
+ 					return CrashReportCategory.getCoordinateInfo(pos);
+ 				}
+ 			});
+ 			throw new ReportedException(crashreport);

> CHANGE  57 : 58  @  46 : 49

~ 				if (block1 instanceof ITileEntityProvider) {

> DELETE  34  @  36 : 40

> CHANGE  251 : 253  @  255 : 257

~ 						&& (predicate == null || predicate.apply((T) entity))) {
~ 					list.add((T) entity);

> CHANGE  20 : 22  @  20 : 22

~ 	public EaglercraftRandom getRandomWithSeed(long i) {
~ 		return new EaglercraftRandom(this.worldObj.getSeed() + (long) (this.xPosition * this.xPosition * 4987142)

> CHANGE  85 : 86  @  85 : 86

~ 			this.recheckGaps(true);

> CHANGE  9 : 10  @  9 : 10

~ 			BlockPos blockpos = (BlockPos) this.tileEntityPosQueue.remove(0);

> CHANGE  110 : 111  @  110 : 111

~ 	public BiomeGenBase getBiome(BlockPos pos) {

> DELETE  4  @  4 : 10

> EOF
