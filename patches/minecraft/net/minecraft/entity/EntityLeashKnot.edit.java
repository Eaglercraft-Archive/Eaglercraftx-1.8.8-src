
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import java.util.List;
+ 

> DELETE  1  @  1 : 4

> CHANGE  67 : 71  @  67 : 69

~ 			List<EntityLiving> entities = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(
~ 					this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + d0, this.posZ + d0));
~ 			for (int i = 0, l = entities.size(); i < l; ++i) {
~ 				EntityLiving entityliving = entities.get(i);

> CHANGE  12 : 13  @  12 : 13

~ 				List<EntityLiving> entities = this.worldObj.getEntitiesWithinAABB(EntityLiving.class,

> CHANGE  1 : 4  @  1 : 2

~ 								this.posY + d1, this.posZ + d1));
~ 				for (int i = 0, l = entities.size(); i < l; ++i) {
~ 					EntityLiving entityliving1 = entities.get(i);

> CHANGE  26 : 27  @  26 : 27

~ 		List<EntityLeashKnot> entities = worldIn.getEntitiesWithinAABB(EntityLeashKnot.class,

> CHANGE  1 : 4  @  1 : 2

~ 						(double) j + 1.0D, (double) k + 1.0D));
~ 		for (int m = 0, l = entities.size(); m < l; ++m) {
~ 			EntityLeashKnot entityleashknot = entities.get(m);

> EOF
