
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 6  @  2 : 3

~ import org.apache.commons.lang3.StringUtils;
~ 
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

> INSERT  82 : 86  @  82

+ 		String ownerName = this.getOwnerId();
+ 		if (StringUtils.isEmpty(ownerName)) {
+ 			return null;
+ 		}

> CHANGE  1 : 2  @  1 : 2

~ 			EaglercraftUUID uuid = EaglercraftUUID.fromString(ownerName);

> CHANGE  2 : 3  @  2 : 3

~ 			return this.worldObj.getPlayerEntityByName(ownerName);

> EOF
