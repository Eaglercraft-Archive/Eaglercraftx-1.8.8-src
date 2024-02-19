
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> INSERT  3 : 5  @  3

+ 
+ import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;

> DELETE  18  @  18 : 19

> DELETE  12  @  12 : 13

> CHANGE  1015 : 1020  @  1015 : 1016

~ 		if (worldObj.isRemote && !SingleplayerServerController.isClientInEaglerSingleplayerOrLAN()) {
~ 			nbttagcompound.setString("OwnerUUID", this.getOwnerId());
~ 		} else {
~ 			nbttagcompound.setString("Owner", this.getOwnerId());
~ 		}

> CHANGE  37 : 41  @  37 : 39

~ 		if (worldObj.isRemote && !SingleplayerServerController.isClientInEaglerSingleplayerOrLAN()) {
~ 			if (nbttagcompound.hasKey("OwnerUUID", 8)) {
~ 				s = nbttagcompound.getString("OwnerUUID");
~ 			}

> CHANGE  1 : 4  @  1 : 3

~ 			if (nbttagcompound.hasKey("Owner", 8)) {
~ 				s = nbttagcompound.getString("Owner");
~ 			}

> EOF
