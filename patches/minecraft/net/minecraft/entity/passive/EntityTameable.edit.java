
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 4  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ import net.lax1dude.eaglercraft.v1_8.sp.SingleplayerServerController;

> DELETE  3  @  3 : 4

> DELETE  3  @  3 : 4

> INSERT  5 : 6  @  5

+ 

> CHANGE  15 : 21  @  15 : 17

~ 		if (worldObj.isRemote && !SingleplayerServerController.isClientInEaglerSingleplayerOrLAN()) {
~ 			if (this.getOwnerId() == null) {
~ 				nbttagcompound.setString("OwnerUUID", "");
~ 			} else {
~ 				nbttagcompound.setString("OwnerUUID", this.getOwnerId());
~ 			}

> CHANGE  1 : 6  @  1 : 2

~ 			if (this.getOwnerId() == null) {
~ 				nbttagcompound.setString("Owner", "");
~ 			} else {
~ 				nbttagcompound.setString("Owner", this.getOwnerId());
~ 			}

> CHANGE  8 : 12  @  8 : 10

~ 		if (worldObj.isRemote && !SingleplayerServerController.isClientInEaglerSingleplayerOrLAN()) {
~ 			if (nbttagcompound.hasKey("OwnerUUID", 8)) {
~ 				s = nbttagcompound.getString("OwnerUUID");
~ 			}

> CHANGE  1 : 4  @  1 : 3

~ 			if (nbttagcompound.hasKey("Owner", 8)) {
~ 				s = nbttagcompound.getString("Owner");
~ 			}

> CHANGE  83 : 84  @  83 : 84

~ 			EaglercraftUUID uuid = EaglercraftUUID.fromString(this.getOwnerId());

> EOF
