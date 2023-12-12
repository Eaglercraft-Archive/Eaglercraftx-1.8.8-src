
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> INSERT  12 : 13  @  12

+ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.server.MinecraftServer;

> CHANGE  17 : 18  @  17 : 19

~ 	public static PropertyEnum<BlockBed.EnumPartType> PART;

> INSERT  2 : 6  @  2

+ 	public static void bootstrapStates() {
+ 		PART = PropertyEnum.<BlockBed.EnumPartType>create("part", BlockBed.EnumPartType.class);
+ 	}
+ 

> CHANGE  9 : 10  @  9 : 12

~ 		{

> INSERT  7 : 8  @  7

+ 		}

> CHANGE  1 : 9  @  1 : 12

~ 		if (world.provider.canRespawnHere() && world.getBiomeGenForCoords(blockpos) != BiomeGenBase.hell) {
~ 			if (MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
~ 					.getBoolean("bedSpawnPoint") && Math.abs(entityplayer.posX - (double) blockpos.getX()) <= 3.0D
~ 					&& Math.abs(entityplayer.posY - (double) blockpos.getY()) <= 2.0D
~ 					&& Math.abs(entityplayer.posZ - (double) blockpos.getZ()) <= 3.0D) {
~ 				BlockPos blockpos1 = BlockBed.getSafeExitLocation(world, blockpos, 0);
~ 				if (blockpos1 == null) {
~ 					blockpos1 = blockpos.up();

> CHANGE  1 : 4  @  1 : 6

~ 				entityplayer.setSpawnPoint(blockpos1.add(0.5F, 0.1F, 0.5F), false);
~ 				entityplayer.addChatComponentMessage(new ChatComponentTranslation("tile.bed.setspawn"));
~ 				if (entityplayer.isSneaking()) {

> CHANGE  1 : 3  @  1 : 9

~ 				}
~ 			}

> INSERT  1 : 6  @  1

+ 			if (((Boolean) iblockstate.getValue(OCCUPIED)).booleanValue()) {
+ 				EntityPlayer entityplayer1 = this.getPlayerInBed(world, blockpos);
+ 				if (entityplayer1 != null) {
+ 					entityplayer
+ 							.addChatComponentMessage(new ChatComponentTranslation("tile.bed.occupied", new Object[0]));

> INSERT  2 : 12  @  2

+ 
+ 				iblockstate = iblockstate.withProperty(OCCUPIED, Boolean.valueOf(false));
+ 				world.setBlockState(blockpos, iblockstate, 4);
+ 			}
+ 
+ 			EntityPlayer.EnumStatus entityplayer$enumstatus = entityplayer.trySleep(blockpos);
+ 			if (entityplayer$enumstatus == EntityPlayer.EnumStatus.OK) {
+ 				iblockstate = iblockstate.withProperty(OCCUPIED, Boolean.valueOf(true));
+ 				world.setBlockState(blockpos, iblockstate, 4);
+ 				return true;

> CHANGE  1 : 7  @  1 : 5

~ 				if (entityplayer$enumstatus == EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW) {
~ 					entityplayer
~ 							.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep", new Object[0]));
~ 				} else if (entityplayer$enumstatus == EntityPlayer.EnumStatus.NOT_SAFE) {
~ 					entityplayer
~ 							.addChatComponentMessage(new ChatComponentTranslation("tile.bed.notSafe", new Object[0]));

> DELETE  2  @  2 : 4

> INSERT  2 : 12  @  2

+ 		} else {
+ 			world.setBlockToAir(blockpos);
+ 			BlockPos blockpos1 = blockpos.offset(((EnumFacing) iblockstate.getValue(FACING)).getOpposite());
+ 			if (world.getBlockState(blockpos1).getBlock() == this) {
+ 				world.setBlockToAir(blockpos1);
+ 			}
+ 
+ 			world.newExplosion((Entity) null, (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.5D,
+ 					(double) blockpos.getZ() + 0.5D, 5.0F, true, true);
+ 			return true;

> CHANGE  33 : 34  @  33 : 34

~ 			{

> CHANGE  6 : 7  @  6 : 7

~ 	public Item getItemDropped(IBlockState iblockstate, EaglercraftRandom var2, int var3) {

> EOF
