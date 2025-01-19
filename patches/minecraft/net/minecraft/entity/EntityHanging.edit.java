
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  4  @  4 : 5

> INSERT  7 : 10  @  7

+ 
+ import java.util.List;
+ 

> CHANGE  98 : 101  @  98 : 101

~ 			List<Entity> lst = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());
~ 			for (int k = 0, l = lst.size(); k < l; ++k) {
~ 				if (lst.get(k) instanceof EntityHanging) {

> EOF
