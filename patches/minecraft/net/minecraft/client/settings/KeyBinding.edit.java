
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 4

> INSERT  2 : 8  @  2

+ 
+ import com.carrotsearch.hppc.IntObjectHashMap;
+ import com.carrotsearch.hppc.IntObjectMap;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Sets;
+ 

> DELETE  1  @  1 : 2

> CHANGE  3 : 4  @  3 : 4

~ 	private static final IntObjectMap<KeyBinding> hash = new IntObjectHashMap<>();

> CHANGE  10 : 11  @  10 : 11

~ 			KeyBinding keybinding = hash.get(keyCode);

> CHANGE  9 : 10  @  9 : 10

~ 			KeyBinding keybinding = hash.get(keyCode);

> CHANGE  8 : 10  @  8 : 10

~ 		for (int i = 0, l = keybindArray.size(); i < l; ++i) {
~ 			keybindArray.get(i).unpressKey();

> CHANGE  5 : 6  @  5 : 6

~ 		hash.clear();

> CHANGE  1 : 4  @  1 : 3

~ 		for (int i = 0, l = keybindArray.size(); i < l; ++i) {
~ 			KeyBinding keybinding = keybindArray.get(i);
~ 			hash.put(keybinding.keyCode, keybinding);

> CHANGE  14 : 15  @  14 : 15

~ 		hash.put(keyCode, this);

> EOF
