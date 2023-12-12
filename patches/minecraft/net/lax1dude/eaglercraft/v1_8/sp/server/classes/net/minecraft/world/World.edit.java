
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  5 : 6  @  5

+ 

> CHANGE  5 : 8  @  5 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  1 : 4  @  1 : 2

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ 

> INSERT  1 : 3  @  1

+ 
+ import net.lax1dude.eaglercraft.v1_8.sp.server.CrashReportHelper;

> CHANGE  8 : 10  @  8 : 10

~ import net.minecraft.crash.CrashReport;
~ import net.minecraft.crash.CrashReportCategory;

> CHANGE  5 : 6  @  5 : 6

~ import net.minecraft.nbt.NBTTagCompound;

> CHANGE  61 : 62  @  61 : 62

~ 	protected int updateLCG = (new EaglercraftRandom()).nextInt();

> CHANGE  6 : 7  @  6 : 7

~ 	public final EaglercraftRandom rand = new EaglercraftRandom();

> CHANGE  9 : 10  @  9 : 10

~ 	private final Calendar theCalendar = EagRuntime.getLocaleCalendar();

> DELETE  1  @  1 : 2

> INSERT  10 : 12  @  10

+ 		if (client)
+ 			throw new IllegalArgumentException("Cannot construct client instance!");

> DELETE  8  @  8 : 9

> CHANGE  18 : 20  @  18 : 19

~ 						return CrashReportCategory
~ 								.getCoordinateInfo(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ()));

> CHANGE  111 : 112  @  111 : 112

~ 		} else if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {

> CHANGE  16 : 17  @  16 : 17

~ 				if ((flags & 2) != 0) {

> CHANGE  3 : 4  @  3 : 4

~ 				if ((flags & 1) != 0) {

> CHANGE  113 : 114  @  113 : 114

~ 		{

> CHANGE  10 : 11  @  10 : 11

~ 							return HString.format("ID #%d (%s // %s)",

> CHANGE  7 : 8  @  7 : 8

~ 				CrashReportHelper.addIntegratedServerBlockInfo(crashreportcategory, pos, iblockstate);

> CHANGE  1408 : 1409  @  1408 : 1409

~ 			{

> CHANGE  11 : 12  @  11 : 12

~ 						this.worldInfo.setThunderTime((this.rand.nextInt(12000) / 2) + 3600);

> CHANGE  1 : 2  @  1 : 2

~ 						this.worldInfo.setThunderTime((this.rand.nextInt(168000) + 12000) * 2);

> CHANGE  20 : 21  @  20 : 21

~ 						this.worldInfo.setRainTime((this.rand.nextInt(12000) + 12000) / 2);

> CHANGE  1 : 2  @  1 : 2

~ 						this.worldInfo.setRainTime((this.rand.nextInt(168000) + 12000) * 2);

> CHANGE  60 : 61  @  60 : 61

~ 		if (this.ambientTickCountdown == 0) {

> CHANGE  30 : 31  @  30 : 31

~ 	public void forceBlockUpdateTick(Block blockType, BlockPos pos, EaglercraftRandom random) {

> CHANGE  263 : 264  @  263 : 264

~ 			if (entityType.isAssignableFrom(entity.getClass()) && filter.apply((T) entity)) {

> CHANGE  11 : 12  @  11 : 12

~ 			if (playerType.isAssignableFrom(entity.getClass()) && filter.apply((T) entity)) {

> CHANGE  232 : 233  @  232 : 233

~ 	public EntityPlayer getPlayerEntityByUUID(EaglercraftUUID uuid) {

> CHANGE  181 : 183  @  181 : 182

~ 			crashreportcategory.addCrashSection("Block coordinates", CrashReportCategory
~ 					.getCoordinateInfo(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ())));

> CHANGE  15 : 16  @  15 : 16

~ 	public EaglercraftRandom setRandomSeed(int parInt1, int parInt2, int parInt3) {

> INSERT  129 : 132  @  129

+ 		if (!MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
+ 				.getBoolean("loadSpawnChunks"))
+ 			return false;

> EOF
