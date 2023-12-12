
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  3 : 4  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> CHANGE  3 : 4  @  3 : 4

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> CHANGE  7 : 9  @  7 : 9

~ import net.minecraft.nbt.NBTTagCompound;
~ import net.minecraft.nbt.NBTTagList;

> DELETE  1  @  1 : 2

> CHANGE  363 : 364  @  363 : 371

~ 			if (nbttagcompound1.hasKey("Name")) {

> CHANGE  38 : 41  @  38 : 45

~ 			nbttagcompound1.setString("Name", s);
~ 			nbttagcompound1.setInteger("S", ((Integer) this.playerReputation.get(s)).intValue());
~ 			nbttaglist1.appendTag(nbttagcompound1);

> EOF
