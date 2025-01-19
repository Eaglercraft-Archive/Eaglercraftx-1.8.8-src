
# Eagler Context Redacted Diff
# Copyright (c) 2025 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> CHANGE  2 : 3  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> CHANGE  1 : 2  @  1 : 2

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;

> DELETE  6  @  6 : 8

> CHANGE  60 : 62  @  60 : 61

~ 									gameprofile = new GameProfile((EaglercraftUUID) null,
~ 											nbttagcompound.getString("SkullOwner"));

> CHANGE  62 : 63  @  62 : 63

~ 			GameProfile gameprofile = new GameProfile((EaglercraftUUID) null, nbt.getString("SkullOwner"));

> EOF
