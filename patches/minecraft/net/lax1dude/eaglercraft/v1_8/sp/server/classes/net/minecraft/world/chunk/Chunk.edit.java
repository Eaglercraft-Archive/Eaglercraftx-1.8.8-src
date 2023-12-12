
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

> INSERT  2 : 4  @  2

+ 
+ import net.lax1dude.eaglercraft.v1_8.sp.server.EaglerMinecraftServer;

> CHANGE  4 : 6  @  4 : 6

~ import net.minecraft.crash.CrashReport;
~ import net.minecraft.crash.CrashReportCategory;

> CHANGE  20 : 22  @  20 : 22

~ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
~ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> CHANGE  31 : 32  @  31 : 32

~ 	private List<BlockPos> tileEntityPosQueue;

> CHANGE  8 : 9  @  8 : 9

~ 		this.tileEntityPosQueue = new LinkedList();

> INSERT  135 : 136  @  135

+ 		++EaglerMinecraftServer.counterLightUpdate;

> INSERT  146 : 147  @  146

+ 			++EaglerMinecraftServer.counterLightUpdate;

> CHANGE  37 : 39  @  37 : 39

~ 					return CrashReportCategory.getCoordinateInfo(new net.minecraft.util.BlockPos(
~ 							Chunk.this.xPosition * 16 + x, y, Chunk.this.zPosition * 16 + z));

> CHANGE  14 : 16  @  14 : 15

~ 					return CrashReportCategory.getCoordinateInfo(
~ 							new net.minecraft.util.BlockPos(blockpos.getX(), blockpos.getY(), blockpos.getZ()));

> CHANGE  36 : 38  @  36 : 37

~ 						return CrashReportCategory
~ 								.getCoordinateInfo(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ()));

> CHANGE  50 : 51  @  50 : 55

~ 				block1.breakBlock(this.worldObj, pos, iblockstate);

> CHANGE  31 : 32  @  31 : 32

~ 				if (block1 != block) {

> CHANGE  254 : 256  @  254 : 256

~ 						&& (predicate == null || predicate.apply((T) entity))) {
~ 					list.add((T) entity);

> CHANGE  18 : 20  @  18 : 20

~ 	public EaglercraftRandom getRandomWithSeed(long i) {
~ 		return new EaglercraftRandom(this.worldObj.getSeed() + (long) (this.xPosition * this.xPosition * 4987142)

> CHANGE  83 : 84  @  83 : 84

~ 			this.recheckGaps(false);

> CHANGE  8 : 9  @  8 : 9

~ 			BlockPos blockpos = (BlockPos) this.tileEntityPosQueue.remove(0);

> EOF
