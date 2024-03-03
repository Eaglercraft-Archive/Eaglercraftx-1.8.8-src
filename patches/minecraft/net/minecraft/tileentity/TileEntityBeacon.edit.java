
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  25  @  25 : 26

> CHANGE  43 : 44  @  43 : 44

~ 			List<EntityPlayer> list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);

> CHANGE  1 : 3  @  1 : 3

~ 			for (int m = 0, l = list.size(); m < l; ++m) {
~ 				list.get(m).addPotionEffect(new PotionEffect(this.primaryEffect, 180, b0, true, true));

> CHANGE  3 : 5  @  3 : 5

~ 				for (int m = 0, l = list.size(); m < l; ++m) {
~ 					list.get(m).addPotionEffect(new PotionEffect(this.secondaryEffect, 180, 0, true, true));

> CHANGE  87 : 88  @  87 : 88

~ 			List<EntityPlayer> lst = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class,

> CHANGE  1 : 4  @  1 : 3

~ 							.expand(10.0D, 5.0D, 10.0D));
~ 			for (int m = 0, n = lst.size(); m < n; ++m) {
~ 				lst.get(m).triggerAchievement(AchievementList.fullBeacon);

> EOF
