
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  12  @  12 : 14

> CHANGE  24 : 26  @  24 : 26

~ 		for (int i = 0, l = this.villageList.size(); i < l; ++i) {
~ 			this.villageList.get(i).setWorld(worldIn);

> CHANGE  16 : 18  @  16 : 18

~ 		for (int i = 0, l = this.villageList.size(); i < l; ++i) {
~ 			this.villageList.get(i).tick(this.tickCounter);

> CHANGE  32 : 34  @  32 : 33

~ 		for (int i = 0, l = this.villageList.size(); i < l; ++i) {
~ 			Village village1 = this.villageList.get(i);

> CHANGE  59 : 61  @  59 : 60

~ 		for (int i = 0, l = this.newDoors.size(); i < l; ++i) {
~ 			VillageDoorInfo villagedoorinfo = this.newDoors.get(i);

> CHANGE  7 : 9  @  7 : 9

~ 		for (int i = 0, l = this.villageList.size(); i < l; ++i) {
~ 			VillageDoorInfo villagedoorinfo1 = this.villageList.get(i).getExistedDoor(doorBlock);

> CHANGE  35 : 37  @  35 : 37

~ 		for (int i = 0, l = this.villagerPositionsList.size(); i < l; ++i) {
~ 			if (this.villagerPositionsList.get(i).equals(pos)) {

> CHANGE  29 : 30  @  29 : 30

~ 		for (int i = 0, l = this.villageList.size(); i < l; ++i) {

> CHANGE  1 : 2  @  1 : 2

~ 			this.villageList.get(i).writeVillageDataToNBT(nbttagcompound1);

> EOF
