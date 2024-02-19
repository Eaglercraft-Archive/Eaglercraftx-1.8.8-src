
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> CHANGE  3 : 4  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> DELETE  10  @  10 : 11

> INSERT  8 : 9  @  8

+ 

> CHANGE  348 : 349  @  348 : 356

~ 			if (nbttagcompound1.hasKey("Name")) {

> CHANGE  38 : 41  @  38 : 45

~ 			nbttagcompound1.setString("Name", s);
~ 			nbttagcompound1.setInteger("S", ((Integer) this.playerReputation.get(s)).intValue());
~ 			nbttaglist1.appendTag(nbttagcompound1);

> EOF
