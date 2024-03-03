
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import java.util.List;
+ 

> DELETE  11  @  11 : 13

> CHANGE  212 : 213  @  212 : 213

~ 			List<EntityPlayer> players = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class,

> CHANGE  2 : 5  @  2 : 3

~ 							(double) ((float) (k + 1) + f)));
~ 			for (int m = 0, l = players.size(); m < l; ++m) {
~ 				EntityPlayer entityplayer = players.get(m);

> EOF
