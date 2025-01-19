
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> INSERT  1 : 4  @  1

+ 
+ import com.google.common.collect.Lists;
+ 

> CHANGE  258 : 262  @  258 : 261

~ 				int l = this.minecartToSpawn.size();
~ 				if (l > 0) {
~ 					for (int i = 0; i < l; ++i) {
~ 						nbttaglist.appendTag(this.minecartToSpawn.get(i).toNBT());

> EOF
