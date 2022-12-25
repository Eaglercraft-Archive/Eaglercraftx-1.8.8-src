
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  3 : 6  @  4 : 5

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> CHANGE  23 : 24  @  22 : 23

~ 		EaglercraftUUID uuid = EaglercraftUUID.fromString(s);

> CHANGE  28 : 29  @  27 : 28

~ 		EaglercraftUUID uuid = this.profile.getId();

> EOF
