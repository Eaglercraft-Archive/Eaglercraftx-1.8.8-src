
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 6  @  2

+ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
+ 
+ import java.util.List;
+ 

> CHANGE  1 : 2  @  1 : 3

~ 

> CHANGE  143 : 144  @  143 : 144

~ 	public Item getItemDropped(IBlockState var1, EaglercraftRandom var2, int var3) {

> CHANGE  38 : 42  @  38 : 41

~ 				List<EntityPlayer> list = worldIn.getEntitiesWithinAABB(EntityPlayer.class,
~ 						entitywither.getEntityBoundingBox().expand(50.0D, 50.0D, 50.0D));
~ 				for (int j = 0, l = list.size(); j < l; ++j) {
~ 					list.get(j).triggerAchievement(AchievementList.spawnWither);

> EOF
