
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 7  @  1

+ 
+ import com.google.common.collect.Lists;
+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;
+ import net.lax1dude.eaglercraft.v1_8.log4j.LogManager;
+ import net.lax1dude.eaglercraft.v1_8.log4j.Logger;

> DELETE  10  @  10 : 12

> CHANGE  48 : 49  @  48 : 49

~ 		long i = EagRuntime.steadyTimeMillis();

> CHANGE  1 : 3  @  1 : 3

~ 		for (int j = 0, k = this.chunkListing.size(); j < k; ++j) {
~ 			this.chunkListing.get(j).func_150804_b(EagRuntime.steadyTimeMillis() - i > 5L);

> CHANGE  2 : 3  @  2 : 3

~ 		if (EagRuntime.steadyTimeMillis() - i > 100L) {

> CHANGE  1 : 2  @  1 : 2

~ 					new Object[] { Long.valueOf(EagRuntime.steadyTimeMillis() - i) });

> EOF
