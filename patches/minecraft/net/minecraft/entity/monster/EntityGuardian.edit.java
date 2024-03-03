
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  2 : 4  @  2

+ import java.util.List;
+ 

> DELETE  13  @  13 : 14

> CHANGE  317 : 318  @  317 : 318

~ 				List<EntityPlayerMP> lst = this.worldObj.getPlayers(EntityPlayerMP.class,

> CHANGE  5 : 8  @  5 : 6

~ 						});
~ 				for (int i = 0, l = lst.size(); i < l; ++i) {
~ 					EntityPlayerMP entityplayermp = lst.get(i);

> EOF
