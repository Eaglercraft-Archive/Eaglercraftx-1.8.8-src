
# Eagler Context Redacted Diff
# Copyright (c) 2022 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 4  @  2 : 3

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> CHANGE  20 : 21  @  18 : 19

~ 		EaglercraftUUID uuid = EaglercraftUUID.fromString(s);

> CHANGE  5 : 6  @  5 : 6

~ 		EaglercraftUUID uuid = this.profile.getId();

> EOF
