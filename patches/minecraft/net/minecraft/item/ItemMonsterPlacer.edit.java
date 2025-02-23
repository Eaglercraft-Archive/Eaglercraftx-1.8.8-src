
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 8  @  3

+ 
+ import com.carrotsearch.hppc.IntObjectHashMap;
+ import com.carrotsearch.hppc.SortedIterationIntObjectHashMap;
+ import com.carrotsearch.hppc.cursors.ObjectCursor;
+ 

> DELETE  11  @  11 : 13

> CHANGE  29 : 30  @  29 : 30

~ 				.get(itemstack.getMetadata());

> CHANGE  92 : 93  @  92 : 93

~ 		if (!EntityList.entityEggs.containsKey(entityID)) {

> CHANGE  24 : 27  @  24 : 26

~ 		for (ObjectCursor<EntityList.EntityEggInfo> entitylist$entityegginfo : new SortedIterationIntObjectHashMap<>(
~ 				(IntObjectHashMap<EntityList.EntityEggInfo>) EntityList.entityEggs, (a, b) -> a - b).values()) {
~ 			list.add(new ItemStack(item, 1, entitylist$entityegginfo.value.spawnedID));

> EOF
