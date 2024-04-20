
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

> INSERT  1 : 2  @  1

+ 

> DELETE  32  @  32 : 46

> CHANGE  24 : 25  @  24 : 25

~ 	protected int updateLCG = (new EaglercraftRandom()).nextInt();

> CHANGE  6 : 7  @  6 : 7

~ 	public final EaglercraftRandom rand = new EaglercraftRandom();

> CHANGE  9 : 10  @  9 : 10

~ 	private final Calendar theCalendar = EagRuntime.getLocaleCalendar();

> DELETE  1  @  1 : 2

> INSERT  7 : 8  @  7

+ 	public final boolean isRemote;

> DELETE  11  @  11 : 12

> INSERT  1 : 2  @  1

+ 		this.isRemote = client;

> CHANGE  17 : 19  @  17 : 18

~ 						return CrashReportCategory
~ 								.getCoordinateInfo(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ()));

> CHANGE  257 : 258  @  257 : 258

~ 							return HString.format("ID #%d (%s // %s)",

> CHANGE  128 : 129  @  128 : 129

~ 			return Chunk.getNoSkyLightValue();

> CHANGE  1299 : 1300  @  1299 : 1300

~ 						this.worldInfo.setThunderTime((this.rand.nextInt(12000) / 2) + 3600);

> CHANGE  1 : 2  @  1 : 2

~ 						this.worldInfo.setThunderTime((this.rand.nextInt(168000) + 12000) * 2);

> CHANGE  20 : 21  @  20 : 21

~ 						this.worldInfo.setRainTime((this.rand.nextInt(12000) + 12000) / 2);

> CHANGE  1 : 2  @  1 : 2

~ 						this.worldInfo.setRainTime((this.rand.nextInt(168000) + 12000) * 2);

> CHANGE  91 : 92  @  91 : 92

~ 	public void forceBlockUpdateTick(Block blockType, BlockPos pos, EaglercraftRandom random) {

> CHANGE  93 : 96  @  93 : 94

~ 				EnumFacing[] facings = EnumFacing._VALUES;
~ 				for (int m = 0; m < facings.length; ++m) {
~ 					EnumFacing enumfacing = facings[m];

> CHANGE  50 : 53  @  50 : 51

~ 								EnumFacing[] facings = EnumFacing._VALUES;
~ 								for (int m = 0; m < facings.length; ++m) {
~ 									EnumFacing enumfacing = facings[m];

> CHANGE  117 : 120  @  117 : 119

~ 		for (int i = 0, l = this.loadedEntityList.size(); i < l; ++i) {
~ 			Entity entity = this.loadedEntityList.get(i);
~ 			if (entityType.isAssignableFrom(entity.getClass()) && filter.apply((T) entity)) {

> CHANGE  10 : 13  @  10 : 12

~ 		for (int i = 0, l = this.playerEntities.size(); i < l; ++i) {
~ 			Entity entity = this.playerEntities.get(i);
~ 			if (playerType.isAssignableFrom(entity.getClass()) && filter.apply((T) entity)) {

> CHANGE  68 : 70  @  68 : 69

~ 		for (int j = 0, l = this.loadedEntityList.size(); j < l; ++j) {
~ 			Entity entity = this.loadedEntityList.get(j);

> CHANGE  102 : 107  @  102 : 104

~ 		EnumFacing[] facings = EnumFacing._VALUES;
~ 		BlockPos tmp = new BlockPos(0, 0, 0);
~ 		for (int k = 0; k < facings.length; ++k) {
~ 			EnumFacing enumfacing = facings[k];
~ 			int j = this.getRedstonePower(pos.offsetEvenFaster(enumfacing, tmp), enumfacing);

> CHANGE  59 : 60  @  59 : 60

~ 	public EntityPlayer getPlayerEntityByUUID(EaglercraftUUID uuid) {

> CHANGE  197 : 198  @  197 : 198

~ 	public EaglercraftRandom setRandomSeed(int parInt1, int parInt2, int parInt3) {

> CHANGE  67 : 70  @  67 : 68

~ 		EnumFacing[] facings = EnumFacing.Plane.HORIZONTAL.facingsArray;
~ 		for (int i = 0; i < facings.length; ++i) {
~ 			EnumFacing enumfacing = facings[i];

> INSERT  61 : 64  @  61

+ 		if (!MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
+ 				.getBoolean("loadSpawnChunks"))
+ 			return false;

> EOF
