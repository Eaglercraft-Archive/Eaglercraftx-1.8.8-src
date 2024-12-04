
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

> CHANGE  60 : 66  @  60 : 65

~ 				BlockPos tmp = new BlockPos(0, 0, 0);
~ 				int i1 = this.getLight(pos.up(tmp), false);
~ 				int i = this.getLight(pos.east(tmp), false);
~ 				int j = this.getLight(pos.west(tmp), false);
~ 				int k = this.getLight(pos.south(tmp), false);
~ 				int l = this.getLight(pos.north(tmp), false);

> CHANGE  63 : 64  @  63 : 64

~ 			return Chunk.getNoSkyLightValue();

> CHANGE  10 : 16  @  10 : 15

~ 				BlockPos tmp = new BlockPos();
~ 				int i1 = this.getLightFor(type, pos.up(tmp));
~ 				int i = this.getLightFor(type, pos.east(tmp));
~ 				int j = this.getLightFor(type, pos.west(tmp));
~ 				int k = this.getLightFor(type, pos.south(tmp));
~ 				int l = this.getLightFor(type, pos.north(tmp));

> CHANGE  59 : 65  @  59 : 60

~ 		if (lightValue < 0) {
~ 			j += -lightValue;
~ 			if (j > 15) {
~ 				j = 15;
~ 			}
~ 		} else if (j < lightValue) {

> CHANGE  320 : 321  @  320 : 321

~ 		BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  72 : 73  @  72 : 73

~ 		BlockPos blockpos$mutableblockpos = new BlockPos();

> DELETE  185  @  185 : 188

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

> CHANGE  37 : 38  @  37 : 38

~ 		BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  22 : 23  @  22 : 23

~ 		BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  23 : 24  @  23 : 24

~ 			BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  28 : 29  @  28 : 29

~ 			BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  38 : 39  @  38 : 39

~ 		BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  22 : 23  @  22 : 23

~ 		BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  122 : 123  @  122 : 123

~ 				tileEntityIn.setPos(new BlockPos(pos));

> CHANGE  101 : 102  @  101 : 102

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

> CHANGE  28 : 31  @  28 : 30

~ 					BlockPos tmp = new BlockPos();
~ 					boolean flag = this.isWater(pos.west(tmp)) && this.isWater(pos.east(tmp))
~ 							&& this.isWater(pos.north(tmp)) && this.isWater(pos.south(tmp));

> CHANGE  63 : 66  @  63 : 64

~ 				EnumFacing[] facings = EnumFacing._VALUES;
~ 				for (int m = 0; m < facings.length; ++m) {
~ 					EnumFacing enumfacing = facings[m];

> DELETE  22  @  22 : 23

> CHANGE  25 : 26  @  25 : 26

~ 								BlockPos blockpos$mutableblockpos = new BlockPos();

> CHANGE  1 : 4  @  1 : 2

~ 								EnumFacing[] facings = EnumFacing._VALUES;
~ 								for (int m = 0; m < facings.length; ++m) {
~ 									EnumFacing enumfacing = facings[m];

> DELETE  20  @  20 : 23

> INSERT  6 : 7  @  6

+ 				BlockPos tmp = new BlockPos(0, 0, 0);

> CHANGE  10 : 11  @  10 : 11

~ 							if (this.getLightFor(lightType, blockpos1.west(tmp)) < j6) {

> CHANGE  4 : 5  @  4 : 5

~ 							if (this.getLightFor(lightType, blockpos1.east(tmp)) < j6) {

> CHANGE  4 : 5  @  4 : 5

~ 							if (this.getLightFor(lightType, blockpos1.down(tmp)) < j6) {

> CHANGE  4 : 5  @  4 : 5

~ 							if (this.getLightFor(lightType, blockpos1.up(tmp)) < j6) {

> CHANGE  4 : 5  @  4 : 5

~ 							if (this.getLightFor(lightType, blockpos1.north(tmp)) < j6) {

> CHANGE  4 : 5  @  4 : 5

~ 							if (this.getLightFor(lightType, blockpos1.south(tmp)) < j6) {

> DELETE  8  @  8 : 9

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

> CHANGE  51 : 53  @  51 : 52

~ 		BlockPos tmp = new BlockPos();
~ 		i = Math.max(i, this.getStrongPower(pos.down(tmp), EnumFacing.DOWN));

> CHANGE  3 : 4  @  3 : 4

~ 			i = Math.max(i, this.getStrongPower(pos.up(tmp), EnumFacing.UP));

> CHANGE  3 : 4  @  3 : 4

~ 				i = Math.max(i, this.getStrongPower(pos.north(tmp), EnumFacing.NORTH));

> CHANGE  3 : 4  @  3 : 4

~ 					i = Math.max(i, this.getStrongPower(pos.south(tmp), EnumFacing.SOUTH));

> CHANGE  3 : 4  @  3 : 4

~ 						i = Math.max(i, this.getStrongPower(pos.west(tmp), EnumFacing.WEST));

> CHANGE  3 : 4  @  3 : 4

~ 							i = Math.max(i, this.getStrongPower(pos.east(tmp), EnumFacing.EAST));

> CHANGE  19 : 26  @  19 : 25

~ 		BlockPos tmp = new BlockPos(0, 0, 0);
~ 		return this.getRedstonePower(pos.down(tmp), EnumFacing.DOWN) > 0 ? true
~ 				: (this.getRedstonePower(pos.up(tmp), EnumFacing.UP) > 0 ? true
~ 						: (this.getRedstonePower(pos.north(tmp), EnumFacing.NORTH) > 0 ? true
~ 								: (this.getRedstonePower(pos.south(tmp), EnumFacing.SOUTH) > 0 ? true
~ 										: (this.getRedstonePower(pos.west(tmp), EnumFacing.WEST) > 0 ? true
~ 												: this.getRedstonePower(pos.east(tmp), EnumFacing.EAST) > 0))));

> CHANGE  5 : 10  @  5 : 7

~ 		EnumFacing[] facings = EnumFacing._VALUES;
~ 		BlockPos tmp = new BlockPos(0, 0, 0);
~ 		for (int k = 0; k < facings.length; ++k) {
~ 			EnumFacing enumfacing = facings[k];
~ 			int j = this.getRedstonePower(pos.offsetEvenFaster(enumfacing, tmp), enumfacing);

> CHANGE  59 : 60  @  59 : 60

~ 	public EntityPlayer getPlayerEntityByUUID(EaglercraftUUID uuid) {

> DELETE  13  @  13 : 17

> CHANGE  180 : 181  @  180 : 181

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
