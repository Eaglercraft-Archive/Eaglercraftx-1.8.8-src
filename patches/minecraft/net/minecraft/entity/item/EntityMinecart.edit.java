
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import java.util.List;

> INSERT  1 : 4  @  1

+ 
+ import com.google.common.collect.Maps;
+ 

> DELETE  8  @  8 : 13

> CHANGE  68 : 73  @  68 : 73

~ 		this.dataWatcher.addObject(17, Integer.valueOf(0));
~ 		this.dataWatcher.addObject(18, Integer.valueOf(1));
~ 		this.dataWatcher.addObject(19, Float.valueOf(0.0F));
~ 		this.dataWatcher.addObject(20, Integer.valueOf(0));
~ 		this.dataWatcher.addObject(21, Integer.valueOf(6));

> DELETE  101  @  101 : 102

> DELETE  32  @  32 : 34

> CHANGE  62 : 66  @  62 : 64

~ 			List<Entity> lst = this.worldObj.getEntitiesWithinAABBExcludingEntity(this,
~ 					this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
~ 			for (int i = 0, m = lst.size(); i < m; ++i) {
~ 				Entity entity = lst.get(i);

> INSERT  533 : 537  @  533

+ 	public String getNameProfanityFilter() {
+ 		return getName();
+ 	}
+ 

> INSERT  23 : 27  @  23

+ 	public IChatComponent getDisplayNameProfanityFilter() {
+ 		return getDisplayName();
+ 	}
+ 

> CHANGE  29 : 32  @  29 : 32

~ 			EntityMinecart.EnumMinecartType[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				ID_LOOKUP.put(Integer.valueOf(types[i].getNetworkID()), types[i]);

> EOF
