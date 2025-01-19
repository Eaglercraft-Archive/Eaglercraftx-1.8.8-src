
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  3  @  3 : 4

> DELETE  3  @  3 : 4

> DELETE  9  @  9 : 11

> DELETE  4  @  4 : 5

> INSERT  23 : 45  @  23

+ 	private BlockPos[] positions = null;
+ 
+ 	private void calculateNewCheckPositions() {
+ 		if (this.center == null || this.center.equals(BlockPos.ORIGIN)) {
+ 			this.positions = null;
+ 		} else {
+ 			this.positions = new BlockPos[] { this.center.add(-this.villageRadius, 0, -this.villageRadius),
+ 					this.center.add(-this.villageRadius, 0, this.villageRadius),
+ 					this.center.add(this.villageRadius, 0, -this.villageRadius),
+ 					this.center.add(this.villageRadius, 0, this.villageRadius), this.center };
+ 		}
+ 	}
+ 
+ 	public boolean isVillageAreaLoaded() {
+ 		for (int i = 0; this.positions != null && i < this.positions.length; i++) {
+ 			if (this.worldObj.isBlockLoaded(this.positions[i])) {
+ 				return true;
+ 			}
+ 		}
+ 		return false;
+ 	}
+ 

> INSERT  5 : 8  @  5

+ 		if (!isVillageAreaLoaded()) {
+ 			return;
+ 		}

> CHANGE  111 : 113  @  111 : 112

~ 		for (int m = 0, n = this.villageDoorInfoList.size(); m < n; ++m) {
~ 			VillageDoorInfo villagedoorinfo1 = this.villageDoorInfoList.get(m);

> CHANGE  14 : 16  @  14 : 15

~ 		for (int m = 0, n = this.villageDoorInfoList.size(); m < n; ++m) {
~ 			VillageDoorInfo villagedoorinfo1 = this.villageDoorInfoList.get(m);

> CHANGE  20 : 22  @  20 : 21

~ 			for (int m = 0, n = this.villageDoorInfoList.size(); m < n; ++m) {
~ 				VillageDoorInfo villagedoorinfo = this.villageDoorInfoList.get(m);

> CHANGE  23 : 25  @  23 : 24

~ 		for (int i = 0, l = this.villageAgressors.size(); i < l; ++i) {
~ 			Village.VillageAggressor village$villageaggressor = this.villageAgressors.get(i);

> CHANGE  100 : 102  @  100 : 102

~ 			for (int m = 0, n = this.villageDoorInfoList.size(); m < n; ++m) {
~ 				j = Math.max(this.villageDoorInfoList.get(m).getDistanceToDoorBlockSq(this.center), j);

> INSERT  4 : 6  @  4

+ 
+ 		calculateNewCheckPositions();

> CHANGE  45 : 46  @  45 : 53

~ 			if (nbttagcompound1.hasKey("Name")) {

> INSERT  5 : 6  @  5

+ 		calculateNewCheckPositions();

> CHANGE  17 : 19  @  17 : 18

~ 		for (int m = 0, n = this.villageDoorInfoList.size(); m < n; ++m) {
~ 			VillageDoorInfo villagedoorinfo = this.villageDoorInfoList.get(m);

> CHANGE  15 : 18  @  15 : 22

~ 			nbttagcompound1.setString("Name", s);
~ 			nbttagcompound1.setInteger("S", ((Integer) this.playerReputation.get(s)).intValue());
~ 			nbttaglist1.appendTag(nbttagcompound1);

> EOF
