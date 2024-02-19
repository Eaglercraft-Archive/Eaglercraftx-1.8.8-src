
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  4  @  4 : 5

> INSERT  1 : 2  @  1

+ import java.util.LinkedList;

> CHANGE  2 : 4  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> CHANGE  1 : 3  @  1 : 2

~ 
~ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;

> DELETE  18  @  18 : 19

> DELETE  2  @  2 : 5

> CHANGE  1 : 4  @  1 : 4

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;
~ import net.lax1dude.eaglercraft.v1_8.opengl.ext.deferred.DeferredStateManager;

> CHANGE  24 : 25  @  24 : 25

~ 	private List<BlockPos> tileEntityPosQueue;

> CHANGE  8 : 9  @  8 : 9

~ 		this.tileEntityPosQueue = new LinkedList();

> INSERT  135 : 138  @  135

+ 		if (!this.worldObj.isRemote) {
+ 			++EaglerMinecraftServer.counterLightUpdate;
+ 		}

> INSERT  146 : 149  @  146

+ 			if (!this.worldObj.isRemote) {
+ 				++EaglerMinecraftServer.counterLightUpdate;
+ 			}

> CHANGE  61 : 70  @  61 : 65

~ 		try {
~ 			if (pos.getY() >= 0 && pos.getY() >> 4 < this.storageArrays.length) {
~ 				ExtendedBlockStorage extendedblockstorage = this.storageArrays[pos.getY() >> 4];
~ 				if (extendedblockstorage != null) {
~ 					int j = pos.getX() & 15;
~ 					int k = pos.getY() & 15;
~ 					int i = pos.getZ() & 15;
~ 					return extendedblockstorage.get(j, k, i);
~ 				}

> CHANGE  2 : 9  @  2 : 17

~ 			return Blocks.air.getDefaultState();
~ 		} catch (Throwable throwable) {
~ 			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block state");
~ 			CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being got");
~ 			crashreportcategory.addCrashSectionCallable("Location", new Callable<String>() {
~ 				public String call() throws Exception {
~ 					return CrashReportCategory.getCoordinateInfo(pos);

> INSERT  1 : 5  @  1

+ 			});
+ 			throw new ReportedException(crashreport);
+ 		}
+ 	}

> CHANGE  1 : 14  @  1 : 11

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

> INSERT  1 : 12  @  1

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

> CHANGE  110 : 112  @  110 : 111

~ 		return extendedblockstorage == null
~ 				? (this.canSeeSky(blockpos) ? enumskyblock.defaultLightValue : getNoSkyLightValue())

> CHANGE  1 : 2  @  1 : 2

~ 						? (this.worldObj.provider.getHasNoSky() ? getNoSkyLightValue()

> CHANGE  35 : 36  @  35 : 36

~ 					: getNoSkyLightValue();

> CHANGE  1 : 3  @  1 : 2

~ 			int i1 = this.worldObj.provider.getHasNoSky() ? getNoSkyLightValue()
~ 					: extendedblockstorage.getExtSkylightValue(j, k & 15, l);

> INSERT  10 : 14  @  10

+ 	public static int getNoSkyLightValue() {
+ 		return DeferredStateManager.isDeferredRenderer() ? 5 : 0;
+ 	}
+ 

> CHANGE  176 : 178  @  176 : 178

~ 						&& (predicate == null || predicate.apply((T) entity))) {
~ 					list.add((T) entity);

> CHANGE  18 : 20  @  18 : 20

~ 	public EaglercraftRandom getRandomWithSeed(long i) {
~ 		return new EaglercraftRandom(this.worldObj.getSeed() + (long) (this.xPosition * this.xPosition * 4987142)

> CHANGE  92 : 93  @  92 : 93

~ 			BlockPos blockpos = (BlockPos) this.tileEntityPosQueue.remove(0);

> CHANGE  113 : 114  @  113 : 114

~ 		if (chunkManager != null && k == 255) {

> EOF
