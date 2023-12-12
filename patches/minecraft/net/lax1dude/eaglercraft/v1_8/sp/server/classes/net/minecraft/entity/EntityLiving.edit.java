
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> CHANGE  27 : 30  @  27 : 30

~ import net.minecraft.nbt.NBTTagCompound;
~ import net.minecraft.nbt.NBTTagFloat;
~ import net.minecraft.nbt.NBTTagList;

> CHANGE  147 : 148  @  147 : 164

~ 		this.worldObj.setEntityState(this, (byte) 20);

> CHANGE  13 : 14  @  13 : 17

~ 		this.updateLeashedState();

> CHANGE  118 : 119  @  118 : 120

~ 		if (this.canPickUpLoot() && !this.dead && this.worldObj.getGameRules().getBoolean("mobGriefing")) {

> CHANGE  462 : 463  @  462 : 463

~ 			if (dropLead) {

> CHANGE  3 : 4  @  3 : 4

~ 			if (sendPacket && this.worldObj instanceof WorldServer) {

> CHANGE  22 : 23  @  22 : 23

~ 		if (sendAttachNotification && this.worldObj instanceof WorldServer) {

> CHANGE  9 : 11  @  9 : 10

~ 				EaglercraftUUID uuid = new EaglercraftUUID(this.leashNBTTag.getLong("UUIDMost"),
~ 						this.leashNBTTag.getLong("UUIDLeast"));

> EOF
