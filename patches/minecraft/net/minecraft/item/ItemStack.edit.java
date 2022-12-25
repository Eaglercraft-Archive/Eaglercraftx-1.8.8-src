
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 5

> DELETE  5  @  8 : 9

> INSERT  6 : 14  @  10

+ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
+ import net.lax1dude.eaglercraft.v1_8.HString;
+ import java.util.Set;
+ 
+ import com.google.common.collect.HashMultimap;
+ import com.google.common.collect.Lists;
+ import com.google.common.collect.Multimap;
+ 

> DELETE  27  @  23 : 27

> CHANGE  212 : 213  @  212 : 213

~ 	public boolean attemptDamageItem(int amount, EaglercraftRandom rand) {

> CHANGE  462 : 463  @  462 : 463

~ 				s = s + HString.format("#%04d/%d%s",

> CHANGE  465 : 466  @  465 : 466

~ 				s = s + HString.format("#%04d%s", new Object[] { Integer.valueOf(i), s1 });

> CHANGE  522 : 523  @  522 : 523

~ 			for (Entry entry : (Set<Entry>) multimap.entries()) {

> EOF
