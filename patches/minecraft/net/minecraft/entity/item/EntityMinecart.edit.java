
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 5  @  2 : 4

~ import java.util.List;
~ import com.carrotsearch.hppc.IntObjectHashMap;
~ import com.carrotsearch.hppc.IntObjectMap;

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

> CHANGE  5 : 6  @  5 : 6

~ 		private static final IntObjectMap<EntityMinecart.EnumMinecartType> ID_LOOKUP = new IntObjectHashMap<>();

> CHANGE  17 : 18  @  17 : 19

~ 			EntityMinecart.EnumMinecartType entityminecart$enumminecarttype = ID_LOOKUP.get(id);

> CHANGE  4 : 7  @  4 : 7

~ 			EntityMinecart.EnumMinecartType[] types = values();
~ 			for (int i = 0; i < types.length; ++i) {
~ 				ID_LOOKUP.put(types[i].getNetworkID(), types[i]);

> EOF
