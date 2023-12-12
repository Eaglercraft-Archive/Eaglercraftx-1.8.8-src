
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;

> INSERT  7 : 9  @  7

+ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.player.EntityPlayer;
+ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.projectile.EntityArrow;

> INSERT  2 : 3  @  2

+ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.server.MinecraftServer;

> CHANGE  15 : 16  @  15 : 17

~ 	public static PropertyEnum<BlockSlab.EnumBlockHalf> HALF;

> INSERT  12 : 16  @  12

+ 	public static void bootstrapStates() {
+ 		HALF = PropertyEnum.<BlockSlab.EnumBlockHalf>create("half", BlockSlab.EnumBlockHalf.class);
+ 	}
+ 

> CHANGE  48 : 49  @  48 : 49

~ 	public int quantityDropped(EaglercraftRandom var1) {

> INSERT  65 : 78  @  65

+ 
+ 	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState var3, EntityPlayer entityplayer,
+ 			EnumFacing var5, float var6, float var7, float var8) {
+ 		if (MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance().getBoolean("clickToSit")
+ 				&& entityplayer.getHeldItem() == null) {
+ 			EntityArrow arrow = new EntityArrow(world, blockpos.getX() + 0.5D, blockpos.getY(), blockpos.getZ() + 0.5D);
+ 			arrow.isChair = true;
+ 			world.spawnEntityInWorld(arrow);
+ 			entityplayer.mountEntity(arrow);
+ 			return true;
+ 		}
+ 		return super.onBlockActivated(world, blockpos, var3, entityplayer, var5, var6, var7, var8);
+ 	}

> EOF
