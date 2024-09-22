
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 3

~ import java.util.List;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> DELETE  1  @  1 : 8

> CHANGE  56 : 58  @  56 : 59

~ 		this.tasks = new EntityAITasks();
~ 		this.targetTasks = new EntityAITasks();

> DELETE  76  @  76 : 77

> DELETE  4  @  4 : 6

> DELETE  171  @  171 : 172

> CHANGE  2 : 6  @  2 : 4

~ 			List<EntityItem> lst = this.worldObj.getEntitiesWithinAABB(EntityItem.class,
~ 					this.getEntityBoundingBox().expand(1.0D, 0.0D, 1.0D));
~ 			for (int i = 0, l = lst.size(); i < l; ++i) {
~ 				EntityItem entityitem = lst.get(i);

> DELETE  5  @  5 : 7

> DELETE  98  @  98 : 99

> DELETE  1  @  1 : 3

> DELETE  1  @  1 : 3

> DELETE  1  @  1 : 3

> DELETE  1  @  1 : 3

> DELETE  1  @  1 : 3

> DELETE  1  @  1 : 4

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 2

> DELETE  1  @  1 : 3

> CHANGE  365 : 367  @  365 : 366

~ 				EaglercraftUUID uuid = new EaglercraftUUID(this.leashNBTTag.getLong("UUIDMost"),
~ 						this.leashNBTTag.getLong("UUIDLeast"));

> CHANGE  1 : 5  @  1 : 3

~ 				List<EntityLivingBase> entities = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
~ 						this.getEntityBoundingBox().expand(10.0D, 10.0D, 10.0D));
~ 				for (int i = 0, l = entities.size(); i < l; ++i) {
~ 					EntityLivingBase entitylivingbase = entities.get(i);

> EOF
