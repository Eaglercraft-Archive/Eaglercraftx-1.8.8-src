
# Eagler Context Redacted Diff
# Copyright (c) 2023 lax1dude. All rights reserved.

# Version: 1.0
# Author: lax1dude

> DELETE  2  @  2 : 3

> CHANGE  1 : 4  @  1 : 2

~ import net.lax1dude.eaglercraft.v1_8.EaglercraftUUID;
~ 
~ import net.lax1dude.eaglercraft.v1_8.mojang.authlib.GameProfile;

> CHANGE  15 : 16  @  15 : 16

~ 		this.profile = new GameProfile((EaglercraftUUID) null, parPacketBuffer.readStringFromBuffer(16));

> EOF
