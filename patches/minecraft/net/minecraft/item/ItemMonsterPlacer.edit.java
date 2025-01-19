
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 6  @  3

+ 
+ import com.carrotsearch.hppc.cursors.ObjectCursor;
+ 

> DELETE  11  @  11 : 13

> CHANGE  147 : 149  @  147 : 149

~ 		for (ObjectCursor<EntityList.EntityEggInfo> entitylist$entityegginfo : EntityList.entityEggs.values()) {
~ 			list.add(new ItemStack(item, 1, entitylist$entityegginfo.value.spawnedID));

> EOF
