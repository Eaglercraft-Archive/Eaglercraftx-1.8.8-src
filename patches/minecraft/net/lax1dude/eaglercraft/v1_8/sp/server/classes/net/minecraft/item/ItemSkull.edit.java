
# Eagler Context Redacted Diff
# Copyright (c) 2024 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> CHANGE  1 : 2  @  1 : 2

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> CHANGE  8 : 10  @  8 : 10

~ import net.minecraft.nbt.NBTTagCompound;
~ import net.minecraft.nbt.NBTUtil;

> CHANGE  45 : 46  @  45 : 46

~ 				{

> CHANGE  19 : 21  @  19 : 20

~ 									gameprofile = new GameProfile((EaglercraftUUID) null,
~ 											nbttagcompound.getString("SkullOwner"));

> CHANGE  62 : 63  @  62 : 63

~ 			GameProfile gameprofile = new GameProfile((EaglercraftUUID) null, nbt.getString("SkullOwner"));

> EOF
