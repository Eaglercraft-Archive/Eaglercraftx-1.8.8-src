
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  4 : 6  @  4

+ 
+ import net.lax1dude.eaglercraft.v1_8.EagRuntime;

> DELETE  7  @  7 : 8

> CHANGE  86 : 89  @  86 : 87

~ 						List<Village> lst = this.worldObj.getVillageCollection().getVillageList();
~ 						for (int j = 0, l = lst.size(); j < l; ++j) {
~ 							Village village = lst.get(i);

> CHANGE  42 : 43  @  42 : 43

~ 				EagRuntime.debugPrintStackTrace(exception);

> EOF
