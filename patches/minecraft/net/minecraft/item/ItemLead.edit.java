
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import java.util.List;
+ 

> DELETE  6  @  6 : 8

> CHANGE  33 : 37  @  33 : 36

~ 		List<EntityLiving> lst = worldIn.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB((double) i - d0,
~ 				(double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0));
~ 		for (int m = 0, l = lst.size(); m < l; ++m) {
~ 			EntityLiving entityliving = lst.get(m);

> EOF
