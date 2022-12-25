
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 5

~ import java.util.ArrayList;

> CHANGE  6 : 7  @  8 : 9

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  8 : 14  @  10 : 11

~ 
~ import com.google.common.base.Predicate;
~ import com.google.common.collect.Maps;
~ 
~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  32  @  29 : 30

> DELETE  33  @  31 : 35

> DELETE  34  @  36 : 39

> CHANGE  58 : 59  @  63 : 64

~ 	private List<BlockPos> tileEntityPosQueue;

> CHANGE  67 : 68  @  72 : 73

~ 		this.tileEntityPosQueue = new ArrayList();

> CHANGE  410 : 419  @  415 : 419

~ 		try {
~ 			if (pos.getY() >= 0 && pos.getY() >> 4 < this.storageArrays.length) {
~ 				ExtendedBlockStorage extendedblockstorage = this.storageArrays[pos.getY() >> 4];
~ 				if (extendedblockstorage != null) {
~ 					int j = pos.getX() & 15;
~ 					int k = pos.getY() & 15;
~ 					int i = pos.getZ() & 15;
~ 					return extendedblockstorage.get(j, k, i);
~ 				}

> CHANGE  421 : 428  @  421 : 436

~ 			return Blocks.air.getDefaultState();
~ 		} catch (Throwable throwable) {
~ 			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block state");
~ 			CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being got");
~ 			crashreportcategory.addCrashSectionCallable("Location", new Callable<String>() {
~ 				public String call() throws Exception {
~ 					return CrashReportCategory.getCoordinateInfo(pos);

> INSERT  429 : 433  @  437

+ 			});
+ 			throw new ReportedException(crashreport);
+ 		}
+ 	}

> CHANGE  434 : 447  @  438 : 448

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

> INSERT  448 : 459  @  449

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

> CHANGE  505 : 506  @  495 : 498

~ 				if (block1 instanceof ITileEntityProvider) {

> DELETE  539  @  531 : 535

> CHANGE  790 : 792  @  786 : 788

~ 						&& (predicate == null || predicate.apply((T) entity))) {
~ 					list.add((T) entity);

> CHANGE  810 : 812  @  806 : 808

~ 	public EaglercraftRandom getRandomWithSeed(long i) {
~ 		return new EaglercraftRandom(this.worldObj.getSeed() + (long) (this.xPosition * this.xPosition * 4987142)

> CHANGE  895 : 896  @  891 : 892

~ 			this.recheckGaps(true);

> CHANGE  904 : 905  @  900 : 901

~ 			BlockPos blockpos = (BlockPos) this.tileEntityPosQueue.remove(0);

> CHANGE  1014 : 1015  @  1010 : 1011

~ 	public BiomeGenBase getBiome(BlockPos pos) {

> DELETE  1018  @  1014 : 1020

> EOF
