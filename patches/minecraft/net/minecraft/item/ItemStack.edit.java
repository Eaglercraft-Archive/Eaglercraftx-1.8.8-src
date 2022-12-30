
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> DELETE  3  @  6 : 7

> INSERT  1 : 9  @  2

+ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import java.util.Set;
+ 
+ import com.google.common.collect.HashMultimap;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Multimap;
+ 

> DELETE  21  @  13 : 17

> CHANGE  185 : 186  @  189 : 190

~ 	public boolean attemptDamageItem(int amount, EaglercraftRandom rand) {

> CHANGE  250 : 251  @  250 : 251

~ 				s = s + HString.format("#%04d/%d%s",

> CHANGE  3 : 4  @  3 : 4

~ 				s = s + HString.format("#%04d%s", new Object[] { Integer.valueOf(i), s1 });

> CHANGE  57 : 58  @  57 : 58

~ 			for (Entry entry : (Set<Entry>) multimap.entries()) {

> EOF
