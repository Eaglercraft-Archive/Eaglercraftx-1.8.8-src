
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 3

~ import java.util.List;
~ 
~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> DELETE  1  @  1 : 8

> CHANGE  316 : 320  @  316 : 318

~ 			List<EntityItem> lst = this.worldObj.getEntitiesWithinAABB(EntityItem.class,
~ 					this.getEntityBoundingBox().expand(1.0D, 0.0D, 1.0D));
~ 			for (int i = 0, l = lst.size(); i < l; ++i) {
~ 				EntityItem entityitem = lst.get(i);

> CHANGE  497 : 499  @  497 : 498

~ 				EaglercraftUUID uuid = new EaglercraftUUID(this.leashNBTTag.getLong("UUIDMost"),
~ 						this.leashNBTTag.getLong("UUIDLeast"));

> CHANGE  1 : 5  @  1 : 3

~ 				List<EntityLivingBase> entities = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
~ 						this.getEntityBoundingBox().expand(10.0D, 10.0D, 10.0D));
~ 				for (int i = 0, l = entities.size(); i < l; ++i) {
~ 					EntityLivingBase entitylivingbase = entities.get(i);

> EOF
