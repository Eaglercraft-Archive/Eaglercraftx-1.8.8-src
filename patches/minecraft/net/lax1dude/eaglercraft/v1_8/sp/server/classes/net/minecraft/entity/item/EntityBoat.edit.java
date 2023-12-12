
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  11 : 12  @  11 : 12

~ import net.minecraft.nbt.NBTTagCompound;

> CHANGE  76 : 77  @  76 : 77

~ 		} else if (!this.isDead) {

> CHANGE  138 : 139  @  138 : 167

~ 		{

> CHANGE  66 : 67  @  66 : 67

~ 				if (!this.isDead) {

> CHANGE  36 : 43  @  36 : 45

~ 			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this,
~ 					this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
~ 			if (list != null && !list.isEmpty()) {
~ 				for (int j2 = 0; j2 < list.size(); ++j2) {
~ 					Entity entity = (Entity) list.get(j2);
~ 					if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityBoat) {
~ 						entity.applyEntityCollision(this);

> DELETE  2  @  2 : 7

> INSERT  2 : 6  @  2

+ 
+ 		if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
+ 			this.riddenByEntity = null;
+ 		}

> CHANGE  22 : 23  @  22 : 23

~ 			{

> DELETE  2  @  2 : 3

> CHANGE  8 : 9  @  8 : 9

~ 				if (!this.isDead) {

> EOF
