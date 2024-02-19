
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  4 : 6  @  4 : 6

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ 

> INSERT  10 : 11  @  10

+ import net.minecraft.entity.projectile.EntityArrow;

> INSERT  1 : 2  @  1

+ import net.minecraft.server.MinecraftServer;

> CHANGE  13 : 15  @  13 : 17

~ 	public static PropertyEnum<BlockStairs.EnumHalf> HALF;
~ 	public static PropertyEnum<BlockStairs.EnumShape> SHAPE;

> INSERT  20 : 25  @  20

+ 	public static void bootstrapStates() {
+ 		HALF = PropertyEnum.<BlockStairs.EnumHalf>create("half", BlockStairs.EnumHalf.class);
+ 		SHAPE = PropertyEnum.<BlockStairs.EnumShape>create("shape", BlockStairs.EnumShape.class);
+ 	}
+ 

> CHANGE  344 : 345  @  344 : 345

~ 	public void randomDisplayTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> CHANGE  60 : 61  @  60 : 61

~ 	public void updateTick(World world, BlockPos blockpos, IBlockState iblockstate, EaglercraftRandom random) {

> INSERT  5 : 13  @  5

+ 		if (!world.isRemote && MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance()
+ 				.getBoolean("clickToSit") && entityplayer.getHeldItem() == null) {
+ 			EntityArrow arrow = new EntityArrow(world, blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5D);
+ 			arrow.isChair = true;
+ 			world.spawnEntityInWorld(arrow);
+ 			entityplayer.mountEntity(arrow);
+ 			return true;
+ 		}

> EOF
