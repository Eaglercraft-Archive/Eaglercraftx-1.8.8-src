
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  7 : 8  @  7 : 9

~ import net.minecraft.server.MinecraftServer;

> INSERT  15 : 17  @  15

+ 	private int lastFailure = 0;
+ 	private int pathfindFailures = 0;

> DELETE  30  @  30 : 31

> DELETE  4  @  4 : 5

> DELETE  19  @  19 : 20

> DELETE  5  @  5 : 6

> INSERT  5 : 11  @  5

+ 		int i = -1;
+ 		if (this.pathfindFailures > 10 && this.currentPath == null
+ 				&& (i = (int) (MinecraftServer.getCurrentTimeMillis() / 50l)) < this.lastFailure + 40) {
+ 			return false;
+ 		}
+ 

> CHANGE  1 : 11  @  1 : 2

~ 
~ 		if (pathentity != null && this.setPath(pathentity, speedIn)) {
~ 			this.lastFailure = 0;
~ 			this.pathfindFailures = 0;
~ 			return true;
~ 		} else {
~ 			this.pathfindFailures++;
~ 			this.lastFailure = i == -1 ? (int) (MinecraftServer.getCurrentTimeMillis() / 50l) : i;
~ 			return false;
~ 		}

> CHANGE  50 : 51  @  50 : 51

~ 					List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this.theEntity,

> CHANGE  4 : 6  @  4 : 5

~ 					for (int i = 0, l = list.size(); i < l; ++i) {
~ 						AxisAlignedBB axisalignedbb = list.get(i);

> INSERT  61 : 63  @  61

+ 		this.pathfindFailures = 0;
+ 		this.lastFailure = 0;

> EOF
