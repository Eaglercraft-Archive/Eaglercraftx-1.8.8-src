
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> CHANGE  5 : 6  @  5 : 6

~ import net.minecraft.nbt.NBTTagCompound;

> DELETE  1  @  1 : 2

> CHANGE  28 : 29  @  28 : 29

~ 			nbttagcompound.setString("Owner", "");

> CHANGE  1 : 2  @  1 : 2

~ 			nbttagcompound.setString("Owner", this.getOwnerId());

> CHANGE  8 : 10  @  8 : 13

~ 		if (nbttagcompound.hasKey("Owner", 8)) {
~ 			s = nbttagcompound.getString("Owner");

> CHANGE  83 : 84  @  83 : 84

~ 			EaglercraftUUID uuid = EaglercraftUUID.fromString(this.getOwnerId());

> CHANGE  45 : 47  @  45 : 47

~ 		if (this.worldObj.getGameRules().getBoolean("showDeathMessages") && this.hasCustomName()
~ 				&& this.getOwner() instanceof EntityPlayerMP) {

> EOF
