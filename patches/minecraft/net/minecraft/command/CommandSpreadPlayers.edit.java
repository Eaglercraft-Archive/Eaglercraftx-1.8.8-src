
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  9 : 12  @  9 : 10

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftRandom;
~ import net.lax1dude.eaglercraft.v1_8.HString;
~ import net.lax1dude.eaglercraft.v1_8.ThreadLocalRandom;

> DELETE  1  @  1 : 9

> INSERT  11 : 12  @  11

+ 

> CHANGE  61 : 62  @  61 : 62

~ 		EaglercraftRandom random = ThreadLocalRandom.current();

> CHANGE  15 : 16  @  15 : 16

~ 					new Object[] { HString.format("%.2f", new Object[] { Double.valueOf(d4) }), Integer.valueOf(i) }));

> CHANGE  7 : 9  @  7 : 8

~ 		for (int i = 0, l = parList.size(); i < l; ++i) {
~ 			Entity entity = parList.get(i);

> CHANGE  11 : 12  @  11 : 12

~ 			EaglercraftRandom parRandom, double parDouble2, double parDouble3, double parDouble4, double parDouble5,

> CHANGE  50 : 53  @  50 : 54

~ 				for (int k = 0; k < parArrayOfPosition.length; ++k) {
~ 					if (!parArrayOfPosition[k].func_111098_b(parWorld)) {
~ 						parArrayOfPosition[k].func_111097_a(parRandom, parDouble2, parDouble3, parDouble4, parDouble5);

> CHANGE  10 : 11  @  10 : 11

~ 							HString.format("%.2f", new Object[] { Double.valueOf(d0) }) });

> CHANGE  45 : 46  @  45 : 46

~ 	private CommandSpreadPlayers.Position[] func_110670_a(EaglercraftRandom parRandom, int parInt1, double parDouble1,

> CHANGE  98 : 99  @  98 : 99

~ 		public void func_111097_a(EaglercraftRandom parRandom, double parDouble1, double parDouble2, double parDouble3,

> EOF
