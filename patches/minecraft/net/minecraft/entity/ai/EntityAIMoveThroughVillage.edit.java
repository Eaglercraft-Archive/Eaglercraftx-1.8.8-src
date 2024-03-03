
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  5  @  5 : 7

> CHANGE  92 : 95  @  92 : 93

~ 		List<VillageDoorInfo> lst = villageIn.getVillageDoorInfoList();
~ 		for (int k = 0, l = lst.size(); k < l; ++k) {
~ 			VillageDoorInfo villagedoorinfo1 = lst.get(k);

> CHANGE  12 : 14  @  12 : 13

~ 		for (int i = 0, l = this.doorList.size(); i < l; ++i) {
~ 			VillageDoorInfo villagedoorinfo = this.doorList.get(i);

> EOF
