
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  5 : 6  @  5

+ 

> CHANGE  5 : 7  @  5 : 6

~ 
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> CHANGE  1 : 4  @  1 : 2

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ 

> INSERT  1 : 2  @  1

+ 

> DELETE  16  @  16 : 17

> DELETE  15  @  15 : 29

> CHANGE  24 : 25  @  24 : 25

~ 	protected int updateLCG = (new EaglercraftRandom()).nextInt();

> CHANGE  6 : 7  @  6 : 7

~ 	public final EaglercraftRandom rand = new EaglercraftRandom();

> DELETE  8  @  8 : 9

> DELETE  2  @  2 : 3

> INSERT  7 : 8  @  7

+ 	public final boolean isRemote;

> CHANGE  1 : 2  @  1 : 3

~ 	protected World(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, boolean client) {

> DELETE  5  @  5 : 6

> DELETE  2  @  2 : 3

> INSERT  1 : 2  @  1

+ 		this.isRemote = client;

> CHANGE  17 : 19  @  17 : 18

~ 						return CrashReportCategory
~ 								.getCoordinateInfo(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ()));

> DELETE  123  @  123 : 124

> DELETE  1  @  1 : 2

> CHANGE  131 : 132  @  131 : 132

~ 							return HString.format("ID #%d (%s // %s)",

> CHANGE  128 : 129  @  128 : 129

~ 			return Chunk.getNoSkyLightValue();

> CHANGE  74 : 80  @  74 : 75

~ 		if (lightValue < 0) {
~ 			j += -lightValue;
~ 			if (j > 15) {
~ 				j = 15;
~ 			}
~ 		} else if (j < lightValue) {

> DELETE  579  @  579 : 582

> DELETE  23  @  23 : 24

> DELETE  16  @  16 : 17

> DELETE  12  @  12 : 13

> DELETE  11  @  11 : 13

> DELETE  11  @  11 : 12

> DELETE  2  @  2 : 3

> DELETE  36  @  36 : 37

> DELETE  20  @  20 : 22

> DELETE  48  @  48 : 49

> DELETE  38  @  38 : 39

> CHANGE  400 : 401  @  400 : 401

~ 		if (!this.provider.getHasNoSky() && this.getGameRules().getBoolean("doWeatherCycle")) {

> CHANGE  12 : 13  @  12 : 13

~ 						this.worldInfo.setThunderTime((this.rand.nextInt(12000) / 2) + 3600);

> CHANGE  1 : 2  @  1 : 2

~ 						this.worldInfo.setThunderTime((this.rand.nextInt(168000) + 12000) * 2);

> CHANGE  20 : 21  @  20 : 21

~ 						this.worldInfo.setRainTime((this.rand.nextInt(12000) + 12000) / 2);

> CHANGE  1 : 2  @  1 : 2

~ 						this.worldInfo.setRainTime((this.rand.nextInt(168000) + 12000) * 2);

> DELETE  23  @  23 : 24

> DELETE  14  @  14 : 15

> DELETE  4  @  4 : 5

> DELETE  9  @  9 : 10

> DELETE  5  @  5 : 6

> DELETE  23  @  23 : 24

> CHANGE  7 : 8  @  7 : 8

~ 	public void forceBlockUpdateTick(Block blockType, BlockPos pos, EaglercraftRandom random) {

> CHANGE  93 : 96  @  93 : 94

~ 				EnumFacing[] facings = EnumFacing._VALUES;
~ 				for (int m = 0; m < facings.length; ++m) {
~ 					EnumFacing enumfacing = facings[m];

> DELETE  22  @  22 : 23

> CHANGE  27 : 30  @  27 : 28

~ 								EnumFacing[] facings = EnumFacing._VALUES;
~ 								for (int m = 0; m < facings.length; ++m) {
~ 									EnumFacing enumfacing = facings[m];

> DELETE  20  @  20 : 23

> DELETE  50  @  50 : 51

> CHANGE  43 : 46  @  43 : 45

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
