
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  11 : 13  @  11

+ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.player.EntityPlayer;
+ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.entity.projectile.EntityArrow;

> INSERT  3 : 4  @  3

+ import net.lax1dude.eaglercraft.v1_8.sp.server.classes.net.minecraft.server.MinecraftServer;

> INSERT  99 : 113  @  99

+ 
+ 	public boolean onBlockActivated(World world, BlockPos blockpos, IBlockState var3, EntityPlayer entityplayer,
+ 			EnumFacing var5, float var6, float var7, float var8) {
+ 		if (MinecraftServer.getServer().worldServers[0].getWorldInfo().getGameRulesInstance().getBoolean("clickToSit")
+ 				&& entityplayer.getHeldItem() == null) {
+ 			EntityArrow arrow = new EntityArrow(world, blockpos.getX() + 0.5D, blockpos.getY() - 0.4375D,
+ 					blockpos.getZ() + 0.5D);
+ 			arrow.isChair = true;
+ 			world.spawnEntityInWorld(arrow);
+ 			entityplayer.mountEntity(arrow);
+ 			return true;
+ 		}
+ 		return super.onBlockActivated(world, blockpos, var3, entityplayer, var5, var6, var7, var8);
+ 	}

> EOF
