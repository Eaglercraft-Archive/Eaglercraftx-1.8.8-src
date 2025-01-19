
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 5  @  1 : 2

~ 
~ import com.carrotsearch.hppc.ObjectIntHashMap;
~ import com.carrotsearch.hppc.ObjectIntMap;
~ 

> DELETE  1  @  1 : 7

> DELETE  14  @  14 : 15

> INSERT  1 : 3  @  1

+ 	private static final ObjectIntMap<String> SHORTCUTS = new ObjectIntHashMap<>();
+ 

> CHANGE  116 : 117  @  116 : 117

~ 			return SHORTCUTS.get(shortcut);

> CHANGE  16 : 17  @  16 : 17

~ 												: getListOfStringsMatchingLastWord(astring, SHORTCUTS.keys()))));

> CHANGE  12 : 13  @  12 : 13

~ 			SHORTCUTS.put("slot.container." + i, i);

> CHANGE  3 : 4  @  3 : 4

~ 			SHORTCUTS.put("slot.hotbar." + j, j);

> CHANGE  3 : 4  @  3 : 4

~ 			SHORTCUTS.put("slot.inventory." + k, 9 + k);

> CHANGE  3 : 4  @  3 : 4

~ 			SHORTCUTS.put("slot.enderchest." + l, 200 + l);

> CHANGE  3 : 4  @  3 : 4

~ 			SHORTCUTS.put("slot.villager." + i1, 300 + i1);

> CHANGE  3 : 4  @  3 : 4

~ 			SHORTCUTS.put("slot.horse." + j1, 500 + j1);

> CHANGE  2 : 10  @  2 : 10

~ 		SHORTCUTS.put("slot.weapon", 99);
~ 		SHORTCUTS.put("slot.armor.head", 103);
~ 		SHORTCUTS.put("slot.armor.chest", 102);
~ 		SHORTCUTS.put("slot.armor.legs", 101);
~ 		SHORTCUTS.put("slot.armor.feet", 100);
~ 		SHORTCUTS.put("slot.horse.saddle", 400);
~ 		SHORTCUTS.put("slot.horse.armor", 401);
~ 		SHORTCUTS.put("slot.horse.chest", 499);

> EOF
