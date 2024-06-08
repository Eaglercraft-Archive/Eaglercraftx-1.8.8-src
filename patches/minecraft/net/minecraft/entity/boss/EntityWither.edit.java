
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  20  @  20 : 21

> CHANGE  53 : 57  @  53 : 57

~ 		this.dataWatcher.addObject(17, Integer.valueOf(0));
~ 		this.dataWatcher.addObject(18, Integer.valueOf(0));
~ 		this.dataWatcher.addObject(19, Integer.valueOf(0));
~ 		this.dataWatcher.addObject(20, Integer.valueOf(0));

> CHANGE  360 : 364  @  360 : 363

~ 			List<EntityPlayer> lst = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class,
~ 					this.getEntityBoundingBox().expand(50.0D, 100.0D, 50.0D));
~ 			for (int i = 0, l = lst.size(); i < l; ++i) {
~ 				lst.get(i).triggerAchievement(AchievementList.killWither);

> INSERT  13 : 17  @  13

+ 	protected float getEaglerDynamicLightsValueSimple(float partialTicks) {
+ 		return 1.0f;
+ 	}
+ 

> EOF
